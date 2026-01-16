package app.ui;

import app.edit.ModelEditor;
import app.io.ObjFileService;
import app.scene.Scene;
import app.scene.SceneController;
import app.scene.SceneObject;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {

    private final JLabel statusLabel = new JLabel("Готово");
    private boolean suppressComboEvents = false;

    private final Scene scene = new Scene();
    private final SceneController sceneController = new SceneController(scene);
    private final ObjFileService fileService = new ObjFileService();

    private final DefaultComboBoxModel<SceneObject> sceneComboModel = new DefaultComboBoxModel<>();
    private final JComboBox<SceneObject> sceneCombo = new JComboBox<>(sceneComboModel);

    private final RenderPanel renderPanel;

    public MainFrame() {
        super("PB3DViewer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Активная модель:"));

        sceneCombo.setPreferredSize(new Dimension(260, 28));
        sceneCombo.addActionListener(e -> {
            if (suppressComboEvents) return;

            int idx = sceneCombo.getSelectedIndex();
            if (idx >= 0 && idx < scene.getObjects().size()) {
                sceneController.setActiveIndex(idx);
                updateStatus();
            }
        });

        topPanel.add(sceneCombo);
        add(topPanel, BorderLayout.NORTH);

        add(statusLabel, BorderLayout.SOUTH);

        renderPanel = new RenderPanel(sceneController);

        add(renderPanel, BorderLayout.CENTER);

        updateStatus();
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Открыть...");
        JMenuItem saveItem = new JMenuItem("Сохранить...");
        JMenuItem removeItem = new JMenuItem("Удалить активную модель");

        openItem.addActionListener(e -> onOpen());
        saveItem.addActionListener(e -> onSave());
        removeItem.addActionListener(e -> onRemoveActive());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(removeItem);

        JMenu editMenu = new JMenu("Правка");
        JMenuItem removePoly = new JMenuItem("Удалить полигон...");
        JMenuItem removeVertex = new JMenuItem("Удалить вершину...");

        removePoly.addActionListener(e -> onRemovePolygon());
        removeVertex.addActionListener(e -> onRemoveVertex());

        editMenu.add(removePoly);
        editMenu.add(removeVertex);

        bar.add(fileMenu);
        bar.add(editMenu);

        return bar;
    }

    private void onOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Открыть OBJ");
        chooser.setFileFilter(new FileNameExtensionFilter("OBJ models (*.obj)", "obj"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try {
            var loadedModel = fileService.load(file);

            SceneObject obj = sceneController.addModel(file.getName(), loadedModel);

            suppressComboEvents = true;
            try {
                sceneComboModel.addElement(obj);
                sceneCombo.setSelectedIndex(sceneComboModel.getSize() - 1); // активная = последняя
            } finally {
                suppressComboEvents = false;
            }

            updateStatus();

        } catch (Exception ex) {
            ErrorDialog.show(this, "Не получилось открыть модель", ex);
        }
    }

    private void onSave() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Сначала открой модель (Файл → Открыть).",
                    "Нет модели",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Сохранить OBJ");
        chooser.setFileFilter(new FileNameExtensionFilter("OBJ models (*.obj)", "obj"));

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".obj")) {
            file = new File(file.getParentFile(), file.getName() + ".obj");
        }

        try {
            fileService.save(active.model(), file);
            statusLabel.setText("Сохранено: " + file.getName());
            renderPanel.repaint();

        } catch (Exception ex) {
            ErrorDialog.show(this, "Не получилось сохранить модель", ex);
        }
    }

    private void onRemoveActive() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Файл", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        suppressComboEvents = true;
        try {
            int idx = scene.getActiveIndex();

            sceneController.removeActive();
            sceneComboModel.removeElementAt(idx);

            if (sceneComboModel.getSize() > 0) {
                int newIdx = Math.min(idx, sceneComboModel.getSize() - 1);
                sceneController.setActiveIndex(newIdx);
                sceneCombo.setSelectedIndex(newIdx);
            } else {
                sceneController.setActiveIndex(-1);
                sceneCombo.setSelectedIndex(-1);
            }

            updateStatus();
        } catch (Exception ex) {
            ErrorDialog.show(this, "Не получилось удалить активную модель", ex);
        } finally {
            suppressComboEvents = false;
        }
    }

    private void onRemovePolygon() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Правка", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Integer idx = InputDialogs.askInt(this, "Удалить полигон", "Индекс полигона (0..):");
        if (idx == null) return;

        try {
            ModelEditor.removePolygon(active.model(), idx);
            updateStatus();
        } catch (Exception ex) {
            ErrorDialog.show(this, "Не получилось удалить полигон", ex);
        }
    }

    private void onRemoveVertex() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Правка", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Integer idx = InputDialogs.askInt(this, "Удалить вершину", "Индекс вершины (0..):");
        if (idx == null) return;

        try {
            ModelEditor.removeVertexAndPolygons(active.model(), idx);
            updateStatus();
        } catch (Exception ex) {
            ErrorDialog.show(this, "Не получилось удалить вершину", ex);
        }
    }

    private void updateStatus() {
        int total = scene.getObjects().size();
        SceneObject active = sceneController.getActive();

        if (active == null) {
            statusLabel.setText("Моделей: " + total + ". Активная: нет");
        } else {
            int v = active.model().getVertices().size();
            int p = active.model().getPolygons().size();
            statusLabel.setText("Моделей: " + total + ". Активная: " + active.name()
                    + " | вершин=" + v + ", полигонов=" + p);
        }
        renderPanel.repaint();
    }
}

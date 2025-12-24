package app.ui;

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
        RenderPanel renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);

        updateStatus();
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save...");
        JMenuItem removeItem = new JMenuItem("Remove active");
        removeItem.addActionListener(e -> onRemoveActive());

        JMenu editMenu = new JMenu("Edit");

        JMenuItem removePoly = new JMenuItem("Remove polygon...");
        removePoly.addActionListener(e -> onRemovePolygon());

        JMenuItem removeVertex = new JMenuItem("Remove vertex...");
        removeVertex.addActionListener(e -> onRemoveVertex());

        editMenu.add(removePoly);
        editMenu.add(removeVertex);

        openItem.addActionListener(e -> onOpen());
        saveItem.addActionListener(e -> onSave());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(removeItem);

        bar.add(fileMenu);
        bar.add(editMenu);

        return bar;
    }

    private void onOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open OBJ");
        chooser.setFileFilter(new FileNameExtensionFilter("OBJ models (*.obj)", "obj"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try {
            var loadedModel = fileService.load(file);

            SceneObject obj = sceneController.addModel(file.getName(), loadedModel);

            sceneComboModel.addElement(obj);
            sceneCombo.setSelectedIndex(sceneComboModel.getSize() - 1); // активная = последняя

            updateStatus();

        } catch (Exception ex) {
            showError("Не получилось открыть модель", ex);
        }
    }


    private void onSave() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Сначала открой модель (File → Open).",
                    "Нет модели",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save OBJ");

        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try {
            fileService.save(active.model(), file);
            statusLabel.setText("Сохранено: " + file.getName());

        } catch (Exception ex) {
            showError("Не получилось сохранить модель", ex);
        }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(
                this,
                title + ":\n" + ex.getMessage(),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }
    private void onRemoveActive() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Remove", JOptionPane.INFORMATION_MESSAGE);
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
        } finally {
            suppressComboEvents = false;
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
    }
    private void onRemovePolygon() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Индекс полигона (0..):", "Remove polygon", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        try {
            int idx = Integer.parseInt(input.trim());
            app.edit.ModelEditor.removePolygon(active.model(), idx);
            updateStatus();
        } catch (Exception ex) {
            showError("Не получилось удалить полигон", ex);
        }
    }

    private void onRemoveVertex() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Edit", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Индекс вершины (0..):", "Remove vertex", JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;

        try {
            int idx = Integer.parseInt(input.trim());
            app.edit.ModelEditor.removeVertexAndPolygons(active.model(), idx);
            updateStatus();
        } catch (Exception ex) {
            showError("Не получилось удалить вершину", ex);
        }
    }

}



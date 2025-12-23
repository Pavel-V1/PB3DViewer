package app.ui;

import app.model.Model;
import app.obj.ObjReader;
import app.obj.ObjWriter;
import java.io.FileWriter;
import app.scene.Scene;
import app.scene.SceneObject;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileReader;

public class MainFrame extends JFrame {

    private final JLabel statusLabel = new JLabel("Готово");

    private final Scene scene = new Scene();
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
            int idx = sceneCombo.getSelectedIndex();
            if (idx >= 0) {
                scene.setActiveIndex(idx);
                updateStatus();
            }
        });
        topPanel.add(sceneCombo);

        add(topPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);
        add(new JLabel("Тут позже будет рендер модели", SwingConstants.CENTER), BorderLayout.CENTER);

        updateStatus();

    }

    private JMenuBar createMenuBar() {
        JMenuItem removeItem = new JMenuItem("Remove active");
        removeItem.addActionListener(e -> onRemoveActive());


        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save...");

        openItem.addActionListener(e -> onOpen());
        saveItem.addActionListener(e -> onSave());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(removeItem);


        bar.add(fileMenu);
        return bar;
    }

    private void onOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open OBJ");
        chooser.setFileFilter(new FileNameExtensionFilter("OBJ models (*.obj)", "obj"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try (FileReader fr = new FileReader(chooser.getSelectedFile())) {
            ObjReader reader = new ObjReader();
            Model loaded = reader.read(fr);

            SceneObject obj = new SceneObject(chooser.getSelectedFile().getName(), loaded);
            scene.add(obj);
            sceneComboModel.addElement(obj);
            sceneCombo.setSelectedIndex(sceneComboModel.getSize() - 1);

            updateStatus();

        } catch (Exception ex) {
            showError("Не получилось открыть модель", ex);
        }
    }

    private void onSave() {
        SceneObject active = scene.getActive();
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

        try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
            ObjWriter writer = new ObjWriter();
            writer.write(active.model(), fw);


            statusLabel.setText("Сохранено: " + chooser.getSelectedFile().getName());
            updateStatus();


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
        SceneObject active = scene.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Remove", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idx = scene.getActiveIndex();
        scene.removeActive();
        sceneComboModel.removeElementAt(idx);

        if (sceneComboModel.getSize() > 0) {
            int newIdx = Math.min(idx, sceneComboModel.getSize() - 1);
            sceneCombo.setSelectedIndex(newIdx);
            scene.setActiveIndex(newIdx);
        } else {
            scene.setActiveIndex(-1);
        }

        updateStatus();
    }

    private void updateStatus() {
        int total = scene.getObjects().size();
        SceneObject active = scene.getActive();

        if (active == null) {
            statusLabel.setText("Моделей: " + total + ". Активная: нет");
        } else {
            int v = active.model().getVertices().size();
            int p = active.model().getPolygons().size();
            statusLabel.setText("Моделей: " + total + ". Активная: " + active.name()
                    + " | вершин=" + v + ", полигонов=" + p);
        }
    }

}



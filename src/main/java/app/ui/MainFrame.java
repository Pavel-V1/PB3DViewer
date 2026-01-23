package app.ui;

import app.edit.ModelEditor;
import app.io.ObjFileService;
import app.scene.Scene;
import app.scene.SceneController;
import app.scene.SceneObject;
import app.edit.ModelTransformApplier;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame {//главное окно приложения

    private final JLabel statusLabel = new JLabel("Готово");
    private boolean drawWireframe = true;
    private boolean useTexture = false;
    private boolean useLighting = false;

    public boolean isDrawWireframe() { return drawWireframe; }
    public boolean isUseTexture() { return useTexture; }
    public boolean isUseLighting() { return useLighting; }

    private boolean suppressComboEvents = false;

    private final Scene scene = new Scene();
    private final SceneController sceneController = new SceneController(scene);
    private final ObjFileService fileService = new ObjFileService();

    private final DefaultComboBoxModel<SceneObject> sceneComboModel = new DefaultComboBoxModel<>();
    private final JComboBox<SceneObject> sceneCombo = new JComboBox<>(sceneComboModel);

    private final RenderPanel renderPanel;
    private final TransformPanel transformPanel;

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
        transformPanel = new TransformPanel(sceneController, renderPanel);
        add(transformPanel, BorderLayout.EAST);

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

        JMenu transformMenu = new JMenu("Трансформация");

        JMenuItem translateItem = new JMenuItem("Переместить (X/Y/Z)...");
        JMenuItem rotateItem = new JMenuItem("Повернуть (X/Y/Z, градусы)...");
        JMenuItem scaleItem = new JMenuItem("Масштабировать (X/Y/Z)...");
        JMenuItem resetItem = new JMenuItem("Сбросить трансформации");

        translateItem.addActionListener(e -> onTranslate());
        rotateItem.addActionListener(e -> onRotate());
        scaleItem.addActionListener(e -> onScale());
        resetItem.addActionListener(e -> onResetTransform());

        transformMenu.add(translateItem);
        transformMenu.add(rotateItem);
        transformMenu.add(scaleItem);
        transformMenu.addSeparator();
        transformMenu.add(resetItem);

        bar.add(transformMenu);

        JMenu renderMenu = new JMenu("Режимы отрисовки");

        JCheckBoxMenuItem wireframeItem =
                new JCheckBoxMenuItem("Рисовать полигональную сетку", drawWireframe);

        JCheckBoxMenuItem textureItem =
                new JCheckBoxMenuItem("Использовать текстуру", useTexture);

        JCheckBoxMenuItem lightingItem =
                new JCheckBoxMenuItem("Использовать освещение", useLighting);

        wireframeItem.addActionListener(e -> {
            drawWireframe = wireframeItem.isSelected();
            renderPanel.repaint();
        });

        textureItem.addActionListener(e -> {
            useTexture = textureItem.isSelected();
            renderPanel.repaint();
        });

        lightingItem.addActionListener(e -> {
            useLighting = lightingItem.isSelected();
            renderPanel.repaint();
        });

        renderMenu.add(wireframeItem);
        renderMenu.add(textureItem);
        renderMenu.add(lightingItem);

        bar.add(renderMenu);


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
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Как сохранить модель?",
                    "Сохранение",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Исходную (без трансформаций)", "С трансформациями"},
                    "Исходную (без трансформаций)"
            );

            if (choice == JOptionPane.CLOSED_OPTION) return;

            var modelToSave = (choice == 1)
                    ? ModelTransformApplier.copyWithAppliedTransform(active.model(), active.transform())
                    : active.model();

            fileService.save(modelToSave, file);

            statusLabel.setText("Сохранено: " + file.getName());

            JOptionPane.showMessageDialog(
                    this,
                    "Модель успешно сохранена:\n" + file.getName(),
                    "Сохранение",
                    JOptionPane.INFORMATION_MESSAGE
            );

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

    private void onTranslate() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Трансформация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Float tx = InputDialogs.askFloat(this, "Перемещение", "tx:");
        if (tx == null) return;
        Float ty = InputDialogs.askFloat(this, "Перемещение", "ty:");
        if (ty == null) return;
        Float tz = InputDialogs.askFloat(this, "Перемещение", "tz:");
        if (tz == null) return;

        active.transform().setTranslation(tx, ty, tz);
        updateStatus();
    }

    private void onRotate() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Трансформация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Float rxDeg = InputDialogs.askFloat(this, "Поворот (градусы)", "rx:");
        if (rxDeg == null) return;
        Float ryDeg = InputDialogs.askFloat(this, "Поворот (градусы)", "ry:");
        if (ryDeg == null) return;
        Float rzDeg = InputDialogs.askFloat(this, "Поворот (градусы)", "rz:");
        if (rzDeg == null) return;

        float k = (float) (Math.PI / 180.0);
        active.transform().setRotationRad(rxDeg * k, ryDeg * k, rzDeg * k);
        updateStatus();
    }

    private void onScale() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Трансформация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Float sx = InputDialogs.askFloat(this, "Масштаб", "sx:");
        if (sx == null) return;
        Float sy = InputDialogs.askFloat(this, "Масштаб", "sy:");
        if (sy == null) return;
        Float sz = InputDialogs.askFloat(this, "Масштаб", "sz:");
        if (sz == null) return;

        active.transform().setScale(sx, sy, sz);
        updateStatus();
    }

    private void onResetTransform() {
        SceneObject active = sceneController.getActive();
        if (active == null) {
            JOptionPane.showMessageDialog(this, "Нет активной модели.", "Трансформация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        active.transform().reset();
        updateStatus();
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
        transformPanel.syncFromActive();
        renderPanel.repaint();
    }

}

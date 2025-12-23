package app.ui;

import app.model.Model;
import app.obj.ObjReader;
import app.obj.ObjWriter;
import java.io.FileWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileReader;

public class MainFrame extends JFrame {

    private final JLabel statusLabel = new JLabel("Готово");
    private Model currentModel;

    public MainFrame() {
        super("PB3DViewer");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());

        setLayout(new BorderLayout());
        add(statusLabel, BorderLayout.SOUTH);
        add(new JLabel("Тут позже будет рендер модели", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save...");

        openItem.addActionListener(e -> onOpen());
        saveItem.addActionListener(e -> onSave());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);

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
            currentModel = reader.read(fr);

            statusLabel.setText("Загружено: вершин = " +
                    currentModel.getVertices().size() +
                    ", полигонов = " +
                    currentModel.getPolygons().size());

        } catch (Exception ex) {
            showError("Не получилось открыть модель", ex);
        }
    }

    private void onSave() {
        if (currentModel == null) {
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
            writer.write(currentModel, fw);

            statusLabel.setText("Сохранено: " + chooser.getSelectedFile().getName());

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
}



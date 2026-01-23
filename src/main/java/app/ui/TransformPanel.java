package app.ui;

import app.math.Vector3;
import app.scene.SceneController;
import app.scene.SceneObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public final class TransformPanel extends JPanel {

    private final SceneController sceneController;
    private final RenderPanel renderPanel;

    // Слайдеры
    private final JSlider tx = slider(-10, 10, 0);
    private final JSlider ty = slider(-10, 10, 0);
    private final JSlider tz = slider(-10, 10, 0);

    private final JSlider rx = slider(-180, 180, 0);
    private final JSlider ry = slider(-180, 180, 0);
    private final JSlider rz = slider(-180, 180, 0);

    private final JSlider sx = slider(10, 300, 100);
    private final JSlider sy = slider(10, 300, 100);
    private final JSlider sz = slider(10, 300, 100);

    private boolean updating = false;

    public TransformPanel(SceneController sceneController, RenderPanel renderPanel) {
        this.sceneController = sceneController;
        this.renderPanel = renderPanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(280, 10));

        add(sectionTitle("Трансформация модели"));
        add(Box.createVerticalStrut(8));

        add(groupTitle("Перемещение"));
        add(row("X", tx));
        add(row("Y", ty));
        add(row("Z", tz));

        add(Box.createVerticalStrut(10));

        add(groupTitle("Поворот (градусы)"));
        add(row("X", rx));
        add(row("Y", ry));
        add(row("Z", rz));

        add(Box.createVerticalStrut(10));

        add(groupTitle("Масштаб (%)"));
        add(row("X", sx));
        add(row("Y", sy));
        add(row("Z", sz));

        add(Box.createVerticalStrut(12));

        JButton reset = new JButton("Сброс");
        reset.addActionListener(e -> resetSlidersAndModel());
        add(reset);

        ChangeListener listener = this::onSliderChanged;
        tx.addChangeListener(listener);
        ty.addChangeListener(listener);
        tz.addChangeListener(listener);
        rx.addChangeListener(listener);
        ry.addChangeListener(listener);
        rz.addChangeListener(listener);
        sx.addChangeListener(listener);
        sy.addChangeListener(listener);
        sz.addChangeListener(listener);

        syncFromActive();
    }

    public void syncFromActive() {
        SceneObject active = sceneController.getActive();
        updating = true;
        try {
            if (active == null) {
                setEnabledAll(false);
                setDefaults();
                return;
            }
            setEnabledAll(true);

            var t = active.transform();
            // translation
            tx.setValue(Math.round(t.getTranslation().x));
            ty.setValue(Math.round(t.getTranslation().y));
            tz.setValue(Math.round(t.getTranslation().z));

            // rotation rad -> deg
            rx.setValue((int) Math.round(Math.toDegrees(t.getRotationRad().x)));
            ry.setValue((int) Math.round(Math.toDegrees(t.getRotationRad().y)));
            rz.setValue((int) Math.round(Math.toDegrees(t.getRotationRad().z)));

            // scale -> %
            sx.setValue((int) Math.round(t.getScale().x * 100));
            sy.setValue((int) Math.round(t.getScale().y * 100));
            sz.setValue((int) Math.round(t.getScale().z * 100));
        } finally {
            updating = false;
        }
    }

    private void onSliderChanged(ChangeEvent e) {
        if (updating) return;
        SceneObject active = sceneController.getActive();
        if (active == null) return;

        var t = active.transform();

        // Перемещение
        t.setTranslation(new Vector3(tx.getValue(), ty.getValue(), tz.getValue()));

        // Поворот
        float k = (float) (Math.PI / 180.0);
        t.setRotationRad(new Vector3(rx.getValue() * k, ry.getValue() * k, rz.getValue() * k));

        // Масштаб
        t.setScale(new Vector3(sx.getValue() / 100f, sy.getValue() / 100f, sz.getValue() / 100f));

        // перерисовать окно
        renderPanel.repaint();
    }

    private void resetSlidersAndModel() {
        SceneObject active = sceneController.getActive();
        updating = true;
        try {
            setDefaults();
            if (active != null) {
                var t = active.transform();
                t.setTranslation(new Vector3(0, 0, 0));
                t.setRotationRad(new Vector3(0, 0, 0));
                t.setScale(new Vector3(1, 1, 1));
            }
        } finally {
            updating = false;
        }
        renderPanel.repaint();

    }

    private void setDefaults() {
        tx.setValue(0); ty.setValue(0); tz.setValue(0);
        rx.setValue(0); ry.setValue(0); rz.setValue(0);
        sx.setValue(100); sy.setValue(100); sz.setValue(100);
    }

    private void setEnabledAll(boolean enabled) {
        for (Component c : getComponents()) c.setEnabled(enabled);
        tx.setEnabled(enabled); ty.setEnabled(enabled); tz.setEnabled(enabled);
        rx.setEnabled(enabled); ry.setEnabled(enabled); rz.setEnabled(enabled);
        sx.setEnabled(enabled); sy.setEnabled(enabled); sz.setEnabled(enabled);
    }

    private static JSlider slider(int min, int max, int value) {
        JSlider s = new JSlider(min, max, value);
        s.setMajorTickSpacing((max - min) / 4);
        s.setPaintTicks(true);
        return s;
    }

    private static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 14f));
        return l;
    }

    private static JLabel groupTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 12f));
        return l;
    }

    private static JPanel row(String label, JSlider slider) {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(slider, BorderLayout.CENTER);
        return p;
    }
}

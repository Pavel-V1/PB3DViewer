package app.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public final class UiTheme {

    private UiTheme() {}

    public static void applyPinkTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Color bg = new Color(255, 240, 247);
        Color panel = new Color(255, 232, 242);
        Color accent = new Color(232, 120, 170);
        Color text = new Color(50, 20, 35);
        Color btn = new Color(255, 220, 236);
        Color btnBorder = new Color(232, 120, 170);

        setGlobalFont(new Font("Times New Roman", Font.PLAIN, 20));

        UIManager.put("Panel.background", panel);
        UIManager.put("Viewport.background", bg);

        UIManager.put("Label.foreground", text);

        UIManager.put("MenuBar.background", panel);
        UIManager.put("Menu.background", panel);
        UIManager.put("MenuItem.background", panel);
        UIManager.put("Menu.foreground", text);
        UIManager.put("MenuItem.foreground", text);

        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", text);

        UIManager.put("Button.background", btn);
        UIManager.put("Button.foreground", text);
        UIManager.put("Button.border", BorderFactory.createLineBorder(btnBorder, 2, true));
        UIManager.put("Button.focus", accent);

        UIManager.put("OptionPane.background", bg);
        UIManager.put("Panel.background", panel);
    }

    private static void setGlobalFont(Font font) {
        FontUIResource f = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}


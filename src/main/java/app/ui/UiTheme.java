package app.ui;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public final class UiTheme {

    private UiTheme() {}

    public static void applyPinkTheme() {
        // 1) Базовый Look&Feel (современный и стабильный)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 2) Цвета (мягкие розовые, без “вырви-глаз”)
        Color bg = new Color(255, 240, 247);       // фон
        Color panel = new Color(255, 232, 242);    // панели
        Color accent = new Color(232, 120, 170);   // акцент
        Color text = new Color(50, 20, 35);        // текст
        Color btn = new Color(255, 220, 236);      // кнопки
        Color btnBorder = new Color(232, 120, 170);

        // 3) Шрифт. Важно: Times New Roman иногда выглядит криво в UI.
        // Но если ты прям хочешь — ставим его. Если не найдётся, Swing возьмёт замену.
        setGlobalFont(new Font("Times New Roman", Font.PLAIN, 20));

        // 4) Прокидываем цвета через UIManager (это безопасно)
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
        UIManager.put("Panel.background", panel); // повтор норм
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


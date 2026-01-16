package app.ui;

import javax.swing.*;
import java.awt.*;

public final class ErrorDialog {
    private ErrorDialog() {}

    public static void show(Component parent, String title, Exception ex) {
        JOptionPane.showMessageDialog(
                parent,
                title + ":\n" + (ex == null ? "" : ex.getMessage()),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}

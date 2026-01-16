package app.ui;

import javax.swing.*;
import java.awt.*;

public final class ErrorDialog {
    private ErrorDialog() {}

    public static void show(Component parent, String title, Exception ex) {
        String details = (ex == null || ex.getMessage() == null) ? "" : ex.getMessage();
        JOptionPane.showMessageDialog(
                parent,
                title + (details.isEmpty() ? "" : ":\n" + details),
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

package app.ui;

import javax.swing.*;
import java.awt.*;

public final class ErrorDialog {//утилита для отображения окна с ошибками
    private ErrorDialog() {}

    public static void show(Component parent, String title, Exception ex) {
        String details = (ex == null || ex.getMessage() == null) ? "" : ex.getMessage();
        JOptionPane.showMessageDialog(//формирование текста сообщ
                parent,
                title + (details.isEmpty() ? "" : ":\n" + details),
                "Ошибка",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showMessage(Component parent, String title, String message) {//ошибка по строке
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
}

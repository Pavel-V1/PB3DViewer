package app.ui;

import javax.swing.*;
import java.awt.*;

public final class InputDialogs {//для ввода чисел через диалог
    private InputDialogs() {}

    public static Integer askInt(Component parent, String title, String prompt) {
        String input = JOptionPane.showInputDialog(//показ диалога ввода
                parent,
                prompt,
                title,
                JOptionPane.QUESTION_MESSAGE
        );
        if (input == null)//обработка отмены
            return null;
        input = input.trim();//удаление пробелов
        if (input.isEmpty())//если польз ничего не ввсел, то отказ от ввода
            return null;
        return Integer.parseInt(input);//преобраз строки в число
    }
    public static Float askFloat(Component parent, String title, String prompt) {
        String input = JOptionPane.showInputDialog(parent, prompt, title, JOptionPane.QUESTION_MESSAGE);
        if (input == null) return null;
        input = input.trim().replace(',', '.');
        if (input.isEmpty()) return null;
        return Float.parseFloat(input);
    }

}

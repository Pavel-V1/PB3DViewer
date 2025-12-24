package app.ui;

import javax.swing.*;
import java.awt.*;

public final class InputDialogs {
    private InputDialogs() {}

    public static Integer askInt(Component parent, String title, String prompt) {
        String input = JOptionPane.showInputDialog(parent, prompt, title, JOptionPane.QUESTION_MESSAGE);
        if (input == null) return null;
        input = input.trim();
        if (input.isEmpty()) return null;
        return Integer.parseInt(input);
    }
}


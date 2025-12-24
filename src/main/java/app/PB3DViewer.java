package app;

import app.ui.MainFrame;
import app.ui.UiTheme;

import javax.swing.*;

public class PB3DViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiTheme.applyPinkTheme();
            new MainFrame().setVisible(true);
        });
    }
}


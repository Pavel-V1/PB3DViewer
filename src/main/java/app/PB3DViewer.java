package app;

import app.ui.MainFrame;

import javax.swing.*;

public class PB3DViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}


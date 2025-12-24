package app.ui;

import javax.swing.*;
import java.awt.*;

public class RenderPanel extends JPanel {

    public RenderPanel() {
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int cx = w / 2;
        int cy = h / 2;

        g2.setColor(new Color(230, 230, 230));
        g2.drawLine(0, cy, w, cy);     // горизонтальная ось
        g2.drawLine(cx, 0, cx, h);     // вертикальная ось

        g2.setColor(Color.BLACK);
        g2.drawString("RenderPanel: тут будет отрисовка модели", 10, 20);
        g2.drawString("Этап 3/4: матрицы готовы, подключаем рендер", 10, 40);
    }
}

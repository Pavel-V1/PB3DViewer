package app.rasterization;

import javax.swing.*;
import java.awt.*;

public class PolygonRasterizationTest {
    public static void main(String[] args) {
        add();
        TriangleRasterizer.launch();

        JFrame frame = new JFrame("Triangle Rasterization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(TriangleRasterizer.getTriangleRasterizer());
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void add() {
        Point a = new Point(30, 10);
        Point a1 = new Point(10, 10);
        Point b = new Point(250, 200);
        Point b1 = new Point(200, 200);
        Point c = new Point(1200, 500);
        Point d = new Point(120, 1100);
        Point p1 = new Point(350, 500);
        Point p2 = new Point(550, 500);
        Point p3 = new Point(450, 350);
        Color c1 = new Color(255, 128, 0);
        Color c2 = Color.BLACK;
        Color c3 = Color.GRAY;

        TriangleRasterizer.makeTriangle(a, b, c, c1, c2, c3);
        TriangleRasterizer.makeTriangle(a1, b1, d, c1);
        TriangleRasterizer.makeTriangle(p1, p2, p3, c2);
        TriangleRasterizer.makeTriangle(new Point(p1.x + 100, p1.y + 100),
                new Point(p2.x + 100, p2.y + 100),
                new Point(p3.x + 100, p3.y + 100), c3);
        TriangleRasterizer.makeTriangle(new Point(270, 270), new Point(270, 300), new Point(300, 350), Color.RED);
    }
}

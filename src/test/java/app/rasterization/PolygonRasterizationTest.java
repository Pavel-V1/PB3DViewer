package app.rasterization;

import app.model.Vertex;

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
        Vertex a = new Vertex(30, 10, 0);
        Vertex a1 = new Vertex(10, 10, 0);
        Vertex b = new Vertex(250, 200, 0);
        Vertex b1 = new Vertex(200, 200, 0);
        Vertex c = new Vertex(1200, 500, 0);
        Vertex d = new Vertex(120, 1100, 0);
        Vertex p1 = new Vertex(350, 500, 0);
        Vertex p2 = new Vertex(550, 500, 0);
        Vertex p3 = new Vertex(450, 350, 0);
        Color c1 = new Color(255, 128, 0);
        Color c2 = Color.BLACK;
        Color c3 = Color.GRAY;

        TriangleRasterizer.makeTriangle(a, b, c, c1, c2, c3);
        TriangleRasterizer.makeTriangle(a1, b1, d, c1);
        TriangleRasterizer.makeTriangle(p1, p2, p3, c2);
        TriangleRasterizer.makeTriangle(new Vertex(p1.x() + 100, p1.y() + 100, 0),
                new Vertex(p2.x() + 100, p2.y() + 100, 0),
                new Vertex(p3.x() + 100, p3.y() + 100, 0), c3);
        TriangleRasterizer.makeTriangle(new Vertex(270, 270, 0), new Vertex(270, 300, 0), new Vertex(300, 350, 0), Color.RED);
    }
}

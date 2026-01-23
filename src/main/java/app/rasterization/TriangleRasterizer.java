package app.rasterization;

import app.model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;

public class TriangleRasterizer extends JPanel {
    private BufferedImage canvas;
    private int[] pixels;
    private static ArrayList<Triangle> mainArrayT = new ArrayList<Triangle>();
    private float[] zBuffer;

    public static void makeTriangle(Vertex a, Vertex b, Vertex c, Color c1, Color c2, Color c3) {
        mainArrayT.add(new Triangle(a, b, c, c1, c2, c3));
    }

    public static void makeTriangle(Vertex a, Vertex b, Vertex c, Color cl) {
        mainArrayT.add(new Triangle(a, b, c, cl, cl, cl));
    }

    public static void removeTriangles() {
        mainArrayT.clear();
    }

    public static TriangleRasterizer getTriangleRasterizer() {
        return new TriangleRasterizer();
    }

    public static void launch() {
        JFrame frame = new JFrame("Triangle Rasterization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TriangleRasterizer());
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // 1. Инициализация или очистка Z-буфера
        if (canvas == null || canvas.getWidth() != w || canvas.getHeight() != h) {
            canvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            pixels = ((DataBufferInt) canvas.getRaster().getDataBuffer()).getData();
            zBuffer = new float[w * h];
        }
        Arrays.fill(pixels, Integer.MAX_VALUE);
        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);

        for (Triangle triangle : mainArrayT) {
            rasterizeTriangle(w, h, triangle.a, triangle.b, triangle.c, triangle.c1, triangle.c2, triangle.c3);
        }
        g.drawImage(canvas, 0, 0, null);
    }

    public void rasterizeTriangle(int width, int height, Vertex v1, Vertex v2, Vertex v3, Color c1, Color c2, Color c3) {
        Point a = new Point((int)v1.x(), (int)v1.y());
        Point b = new Point((int)v2.x(), (int)v2.y());
        Point c = new Point((int)v3.x(), (int)v3.y());

        Point bigger = (a.y > b.y ? a : b);
        Point lower = (a.y < b.y ? a : b);
        Point top = bigger.y > c.y ? bigger : c;
        Point bottom = lower.y < c.y ? lower : c;
        Point middle = a != top && a != bottom ? a : b != top && b != bottom ? b : c;

        drawScanline(width, height, bottom.y, middle.y, bottom, middle, top, bottom, v1, v2, v3, c1, c2, c3);
        drawScanline(width, height, middle.y, top.y, middle, top, top, bottom, v1, v2, v3, c1, c2, c3);
    }

    private void drawScanline(int width, int height, int yStart, int yEnd, Point p1, Point p2, Point top,
                              Point bottom, Vertex v1, Vertex v2, Vertex v3, Color c1, Color c2, Color c3) {
        if (yStart == yEnd) return;
        for (int y = yStart; y < yEnd; y++) {
            if (y < 0 || y >= getHeight()) continue;

            int x1 = ((y - yStart) * (p2.x - p1.x) / (yEnd - yStart)) + p1.x;
            int x2 = ((y - bottom.y) * (top.x - bottom.x) / (top.y - bottom.y)) + bottom.x;

            if (x1 > x2) { int t = x1; x1 = x2; x2 = t; }

            for (int x = x1; x <= x2; x++) {
                if (x < 0 || x >= width) continue;

                double[] bary = calculateBaryCoords(new Point(x, y),
                        new Point((int)v1.x(), (int)v1.y()),
                        new Point((int)v2.x(), (int)v2.y()),
                        new Point((int)v3.x(), (int)v3.y()));

                float currentZ = (float)(v1.z() * bary[0] + v2.z() * bary[1] + v3.z() * bary[2]);
                int bufferIndex = y * width + x;

                if (currentZ < zBuffer[bufferIndex]) {
                    zBuffer[bufferIndex] = currentZ;
                    Color col = interpolateColor(bary, c1, c2, c3);
                    pixels[bufferIndex] = (col.getRed() << 16) | (col.getGreen() << 8) | col.getBlue();
                }
            }
        }
    }

    private double[] calculateBaryCoords(Point p, Point a, Point b, Point c) {
        double areaTotal = triangleArea(a, b, c);
        double areaA = triangleArea(p, b, c) / areaTotal;
        double areaB = triangleArea(a, p, c) / areaTotal;
        double areaC = triangleArea(a, b, p) / areaTotal;
        return new double[]{areaA, areaB, areaC};
    }

    private double triangleArea(Point a, Point b, Point c) {
        return abs((a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y))) / 2d;
    }

    private double abs(double x) {
        if (x < 0) {
            return -x;
        } else {
            return x;
        }
    }

    private Color interpolateColor(double[] baryCoords, Color c1, Color c2, Color c3) {
        int r = (int) (c1.getRed() * baryCoords[0] +
                c2.getRed() * baryCoords[1] +
                c3.getRed() * baryCoords[2]);

        int g = (int) (c1.getGreen() * baryCoords[0] +
                c2.getGreen() * baryCoords[1] +
                c3.getGreen() * baryCoords[2]);

        int b = (int) (c1.getBlue() * baryCoords[0] +
                c2.getBlue() * baryCoords[1] +
                c3.getBlue() * baryCoords[2]);

        return new Color(r > 255 ? 255 : Math.max(r, 0),
                        g > 255 ? 255 : Math.max(g, 0),
                        b > 255 ? 255 : Math.max(b, 0));
    }
}

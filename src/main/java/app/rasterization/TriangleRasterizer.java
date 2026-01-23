package app.rasterization;

import app.model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TriangleRasterizer extends JPanel {
    private static ArrayList<Triangle> mainArrayT = new ArrayList<>();
    private static Float[][] zBufferThis;

    private void setZBuffer(Float[][] zBuffer) {
        if (zBufferThis == null) {
            zBufferThis = zBuffer;
        }
    }

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
        for (Triangle triangle : mainArrayT) {
            rasterizeTriangle(g, 500, 500, triangle.a, triangle.b, triangle.c,
                    triangle.c1, triangle.c2, triangle.c3);
        }
    }

    public void rasterizeTriangle(Graphics g, int width, int height, Vertex a0, Vertex b0, Vertex c0,
                                  Color c1, Color c2, Color c3, Float[][] zBuffer) {
        setZBuffer(zBuffer);
        rasterizeTriangle(g, width, height, a0, b0, c0, c1, c2, c3);
    }

    private void rasterizeTriangle(Graphics g, int width, int height, Vertex a0, Vertex b0, Vertex c0,
                                  Color c1, Color c2, Color c3) {
        Point a = getPointFromVertex(a0, width, height);
        Point b = getPointFromVertex(b0, width, height);
        Point c = getPointFromVertex(c0, width, height);

        Point bigger = (a.y > b.y ? a : b);
        Point lower = (a.y < b.y ? a : b);
        Point top = bigger.y > c.y ? bigger : c;
        Point bottom = lower.y < c.y ? lower : c;
        Point middle = a != top && a != bottom ? a : b != top && b != bottom ? b : c;

        rasterize(g, bottom, middle, bottom, top, middle, c1, c2, c3, a0, b0, c0);
        rasterize(g, middle, top, bottom, top, middle, c1, c2, c3, a0, b0, c0);
    }

    private void rasterize(Graphics g, Point down, Point up, Point bottom, Point top,
                           Point middle, Color c1, Color c2, Color c3, Vertex a0, Vertex b0, Vertex c0) {
        for (int y = down.y; y < up.y; y++) {
            int x1 = ((y - down.y) * (up.x - down.x) / (up.y - down.y)) + down.x;
            int x2 = ((y - bottom.y) * (top.x - bottom.x) / (top.y - bottom.y)) + bottom.x;
            if (x1 > x2) {
                int xt = x1;
                x1 = x2;
                x2 = xt;
            }
            for (int x = x1; x <= x2; x++) {
                double[] baryCoords = calculateBaryCoords(new Point(x, y), bottom, middle, top);
                if (checkWithZBuffer(baryCoords, new Point(x, y), a0, b0, c0)) {
                    Color interpolatedColor = interpolateColor(baryCoords, c1, c2, c3);
                    g.setColor(interpolatedColor);
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    public static Point getPointFromVertex(Vertex v, int width, int height) {
        return new Point((int) (v.x() * width + width / 2.0F), (int) (-v.y() * height + height / 2.0F));
    }

    private static boolean checkWithZBuffer(double[] baryCoords, Point p, Vertex a, Vertex b, Vertex c) {
        float z = (float) (a.z() * baryCoords[0] + b.z() * baryCoords[1] + c.z() * baryCoords[2]);
        float zBuff = zBufferThis[p.x][p.y];

        if (z < zBuff) {
            zBufferThis[p.x][p.y] = z;
        }
        return z < zBuff;
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

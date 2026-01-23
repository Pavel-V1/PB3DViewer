package app.rasterization;

import app.model.TexCoord;
import app.model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class TriangleRasterizer {
    private ArrayList<Triangle> mainArrayT = new ArrayList<>();
    private float[] zBuffer;

    public void makeTriangle(int size, Vertex a, Vertex b, Vertex c, Color c1, Color c2, Color c3) {
        makeTriangle(size, a, b, c, c1, c2, c3, null, null, null);
    }

    public void makeTriangle(int size, Vertex a, Vertex b, Vertex c, Color c1, Color c2, Color c3, TexCoord tc1, TexCoord tc2, TexCoord tc3) {
        Vertex a0 = new Vertex(a.x() * size, a.y() * size, a.z() * size);
        Vertex b0 = new Vertex(b.x() * size, b.y() * size, b.z() * size);
        Vertex c0 = new Vertex(c.x() * size, c.y() * size, c.z() * size);
        mainArrayT.add(new Triangle(a0, b0, c0, c1, c2, c3, tc1, tc2, tc3));
    }

    public void makeTriangle(int size, Vertex a, Vertex b, Vertex c, Color cl, TexCoord tc1, TexCoord tc2, TexCoord tc3) {
        makeTriangle(size, a, b, c, cl, cl, cl, tc1, tc2, tc3);
    }

    public void removeTriangles() {
        mainArrayT.clear();
    }

    public static TriangleRasterizer getTriangleRasterizer() {
        return new TriangleRasterizer();
    }

    public void paint(Graphics g, int width, int height, boolean isTex, Image texture) {
        int size = width * height;
        if (zBuffer == null || zBuffer.length != size) {
            zBuffer = new float[size];
        }
        Arrays.fill(zBuffer, Float.POSITIVE_INFINITY);

        for (Triangle triangle : mainArrayT) {
            rasterizeTriangle(g, width, height, triangle.a, triangle.b, triangle.c, triangle.c1, triangle.c2, triangle.c3,
                    isTex, triangle.tc1, triangle.tc2, triangle.tc3, texture);
        }
    }

    public void rasterizeTriangle(Graphics g, int width, int height, Vertex v1, Vertex v2, Vertex v3, Color c1,
                                  Color c2, Color c3, boolean isTex, TexCoord tc1, TexCoord tc2, TexCoord tc3, Image texture) {

        Point a = new Point((int)v1.x(), (int)v1.y());
        Point b = new Point((int)v2.x(), (int)v2.y());
        Point c = new Point((int)v3.x(), (int)v3.y());

        Point bigger = (a.y > b.y ? a : b);
        Point lower = (a.y < b.y ? a : b);
        Point top = bigger.y > c.y ? bigger : c;
        Point bottom = lower.y < c.y ? lower : c;
        Point middle = a != top && a != bottom ? a : b != top && b != bottom ? b : c;

        drawScanline(g, width, height, bottom.y, middle.y, bottom, middle, top, bottom,
                v1, v2, v3, c1, c2, c3, isTex, tc1, tc2, tc3, texture
        );
        drawScanline(g, width, height, middle.y, top.y, middle, top, top, bottom,
                v1, v2, v3, c1, c2, c3, isTex, tc1, tc2, tc3, texture
        );
    }

    private void drawScanline(Graphics g, int width, int height, int yStart, int yEnd, Point p1, Point p2,
                              Point top, Point bottom, Vertex v1, Vertex v2, Vertex v3, Color c1, Color c2,
                              Color c3, boolean isTex, TexCoord tc1, TexCoord tc2, TexCoord tc3, Image texture) {

        if (yStart == yEnd) return;

        Point pV1 = new Point((int)v1.x(), (int)v1.y());
        Point pV2 = new Point((int)v2.x(), (int)v2.y());
        Point pV3 = new Point((int)v3.x(), (int)v3.y());

        for (int y = yStart; y < yEnd; y++) {
            if (y < 0 || y >= height) continue;

            int x1 = ((y - yStart) * (p2.x - p1.x) / (yEnd - yStart)) + p1.x;
            int x2 = ((y - bottom.y) * (top.x - bottom.x) / (top.y - bottom.y)) + bottom.x;

            if (x1 > x2) { int t = x1; x1 = x2; x2 = t; }

            for (int x = x1; x <= x2; x++) {
                if (x < 0 || x >= width) continue;

                double[] bary = calculateBaryCoords(new Point(x, y), pV1, pV2, pV3);

                if (bary[0] >= -0.01 && bary[1] >= -0.01 && bary[2] >= -0.01) {
                    float currentZ = (float)(v1.z() * bary[0] + v2.z() * bary[1] + v3.z() * bary[2]);
                    int idx = y * width + x;

                    if (currentZ < zBuffer[idx]) {
                        zBuffer[idx] = currentZ;
                        if (isTex) {
                            TexCoord tc = interpolateTex(bary, tc1, tc2, tc3);
                            BufferedImage bi = (BufferedImage) texture;
                            g.setColor(new Color(bi.getRGB((int) tc.u(), (int) tc.v())));
                        } else {
                            g.setColor(interpolateColor(bary, c1, c2, c3));
                        }
                        g.fillRect(x, y, 2, 2);
                    }
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
        return x < 0 ? -x : x;
    }

    private TexCoord interpolateTex(double[] baryCoords, TexCoord tc1, TexCoord tc2, TexCoord tc3) {
        if (tc1 != null && tc2 != null && tc3 != null) {
            return new TexCoord((int) (tc1.u() * baryCoords[0] + tc2.u() * baryCoords[1] + tc3.u() * baryCoords[2]),
                    (int) (tc1.v() * baryCoords[0] + tc2.v() * baryCoords[1] + tc3.v() * baryCoords[2]));
        } else {
            return null;
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

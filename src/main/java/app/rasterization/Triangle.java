package app.rasterization;

import java.awt.*;

public class Triangle {
    Point a;
    Point b;
    Point c;
    Color c1;
    Color c2;
    Color c3;

    public Triangle(Point a, Point b, Point c, Color c1, Color c2, Color c3) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
}

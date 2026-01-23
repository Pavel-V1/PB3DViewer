package app.rasterization;

import app.model.Vertex;

import java.awt.*;

public class Triangle {
    Vertex a;
    Vertex b;
    Vertex c;
    Color c1;
    Color c2;
    Color c3;

    public Triangle(Vertex a, Vertex b, Vertex c, Color c1, Color c2, Color c3) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
}

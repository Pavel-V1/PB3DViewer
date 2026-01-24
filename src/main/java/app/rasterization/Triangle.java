package app.rasterization;

import app.model.TexCoord;
import app.model.Vertex;

import java.awt.*;

public class Triangle {
    Vertex a;
    Vertex b;
    Vertex c;
    Color c1;
    Color c2;
    Color c3;
    TexCoord tc1;
    TexCoord tc2;
    TexCoord tc3;

    public Triangle(Vertex a, Vertex b, Vertex c, Color c1, Color c2, Color c3, TexCoord tc1, TexCoord tc2, TexCoord tc3) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.tc1 = tc1;
        this.tc2 = tc2;
        this.tc3 = tc3;
    }
}

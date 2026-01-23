package app.rasterization;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;

import javax.swing.*;
import java.awt.*;

public class ModelRasterization {
    public static void rasterizeModel(Model model, int size, Color color) {
        for (Polygon polygon : model.getPolygons()) {
            Vertex a = model.getVertices().get(polygon.vertexIndices().get(0));
            Vertex b = model.getVertices().get(polygon.vertexIndices().get(1));
            Vertex c = model.getVertices().get(polygon.vertexIndices().get(2));
            TriangleRasterizer.makeTriangle(size, a, b, c, color);
        }
        TriangleRasterizer.launch();
    }
}

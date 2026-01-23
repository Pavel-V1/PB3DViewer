package app.rasterization;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;

import java.awt.*;

public class ModelRasterization {
    public static void rasterizeModel(Model model, int width, int height, Graphics g) {
        Float[][] zBuffer = new Float[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                zBuffer[x][y] = Float.MAX_VALUE;
            }
        }
        TriangleRasterizer triangleRasterizer = new TriangleRasterizer();

        for (Polygon polygon : model.getPolygons()) {
            Color COLOR = Color.ORANGE;
            Vertex a = model.getVertices().get(polygon.vertexIndices().get(0));
            Vertex b = model.getVertices().get(polygon.vertexIndices().get(1));
            Vertex c = model.getVertices().get(polygon.vertexIndices().get(2));
            triangleRasterizer.rasterizeTriangle(g, width, height, a, b, c, COLOR, COLOR, COLOR, zBuffer);
        }
    }
}

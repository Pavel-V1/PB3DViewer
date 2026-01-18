package app.rend_prepare;

import app.model.Polygon;
import app.model.Vertex;
import java.util.ArrayList;
import java.util.List;

public class ModelTriangulation {

    public static List<Polygon> triangulate(List<Polygon> polygons, List<Vertex> modelVertices) {
        if (polygons.isEmpty() || modelVertices.isEmpty()) {
            return new ArrayList<>();
        }

        List<Polygon> triangles = new ArrayList<>();

        for (Polygon polygon : polygons) {
            int n = polygon.vertexIndices().size();
            int index = polygon.vertexIndices().get(0);
            for (int i = index + 1; i < index + n - 1; i++) {
                List<Integer> v = new ArrayList<>();
                v.add(index);
                v.add(i);
                v.add(i + 1);
                Polygon triangle = new Polygon(v, polygon.texCoordIndices(), polygon.normalIndices());
                triangles.add(triangle);
            }
        }

        return triangles;
    }
}

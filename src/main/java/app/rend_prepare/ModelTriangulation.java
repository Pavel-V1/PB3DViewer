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
            for (int i = 1; i < n - 1; i++) {
                List<Integer> v = new ArrayList<>();
                List<Integer> t = new ArrayList<>();
                List<Integer> m = new ArrayList<>();

                v.add(polygon.vertexIndices().get(0));
                v.add(polygon.vertexIndices().get(i));
                v.add(polygon.vertexIndices().get(i + 1));

                if (polygon.texCoordIndices().size() > 2) {
                    t.add(polygon.texCoordIndices().get(0));
                    t.add(polygon.texCoordIndices().get(i));
                    t.add(polygon.texCoordIndices().get(i + 1));
                }

                if (polygon.normalIndices().size() > 2) {
                    m.add(polygon.normalIndices().get(0));
                    m.add(polygon.normalIndices().get(i));
                    m.add(polygon.normalIndices().get(i + 1));
                }

                Polygon triangle = new Polygon(v, t, m);
                triangles.add(triangle);
            }
        }

        return triangles;
    }
}

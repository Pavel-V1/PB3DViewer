package app.rend_prepare;

import app.math.Vector3;
import app.model.Normal;
import app.model.Polygon;
import app.model.Vertex;
import java.util.ArrayList;
import java.util.List;

public class NormalsCalculating {

    public static List<Normal> calculateNormals(List<Polygon> polygons, List<Vertex> vertices) {
        if (polygons.isEmpty() || vertices.isEmpty()) {
            return new ArrayList<>();
        }

        List<Normal> newNormals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            ArrayList<Normal> normalsOfPolygonsWithThisVertex = new ArrayList<>();
            for (Polygon polygon : polygons) {
                if (polygon.vertexIndices().contains(i)) {
                    normalsOfPolygonsWithThisVertex.add(calculateNormalForPolygon(polygon, vertices));
                }
            }

            if (normalsOfPolygonsWithThisVertex.isEmpty()) {
                continue;
            }

            int counter = 0;
            float x = 0;
            float y = 0;
            float z = 0;
            for (Normal norm : normalsOfPolygonsWithThisVertex) {
                x += norm.x();
                y += norm.y();
                z += norm.z();
                counter++;
            }
            x /= counter;
            y /= counter;
            z /= counter;

            Normal normal = new Normal(x, y, z);
            normal = normalize(normal);
            newNormals.add(normal);
        }

        return newNormals;
    }

    private static Normal calculateNormalForPolygon(Polygon polygon, List<Vertex> vertices) {
        ArrayList<Vertex> list = new ArrayList<>(); // Берем первые 3 вершины в полигоне.
        for (int i = 0; i < 3; i++) {
            int index = polygon.vertexIndices().get(i);
            list.add(vertices.get(index));
        }

        Vector3 v1 = new Vector3(
                list.get(1).x() - list.get(0).x(),
                list.get(1).y() - list.get(0).y(),
                list.get(1).z() - list.get(0).z()
        );
        Vector3 v2 = new Vector3(
                list.get(2).x() - list.get(0).x(),
                list.get(2).y() - list.get(0).y(),
                list.get(2).z() - list.get(0).z()
        );

        return normalize(new Normal(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x
        ));
    }

    private static Normal normalize(Normal normal) {
        double l = Math.sqrt(Math.pow(normal.x(), 2) + Math.pow(normal.y(), 2) + Math.pow(normal.z(), 2));
        return new Normal((float) (normal.x() / l), (float) (normal.y() / l), (float) (normal.z() / l));
    }
}

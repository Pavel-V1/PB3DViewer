package app.rend_prepare;

import app.model.Model;
import app.model.Normal;
import app.model.Polygon;
import app.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class NormalsCalculating {
    public static Model calculateNormals(Model model) {
        List<Normal> normals = calculateNormals(model.getPolygons(), model.getVertices());
        Model newModel = new Model();
        newModel.getTexCoords().addAll(model.getTexCoords());
        newModel.getVertices().addAll(model.getVertices());
        newModel.getPolygons().addAll(model.getPolygons());
        newModel.getNormals().addAll(normals);
        return newModel;
    }

    public static List<Normal> calculateNormals(List<Polygon> polygons, List<Vertex> vertices) {
        if (polygons.isEmpty() || vertices.isEmpty()) {
            return new ArrayList<>();
        }

        List<Normal> newNormals = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            ArrayList<Normal> normalsOfPolygonsWithVertex = new ArrayList<>();
            for (Polygon polygon : polygons) {
                if (polygon.vertexIndices().contains(i)) {
                    normalsOfPolygonsWithVertex.add(calculateNormalForPolygon(polygon, vertices));
                }
            }

            if (normalsOfPolygonsWithVertex.isEmpty()) {
                continue;
            }

            int counter = 0;
            float x = 0;
            float y = 0;
            float z = 0;
            for (Normal norm : normalsOfPolygonsWithVertex) {
                x += norm.x();
                y += norm.y();
                z += norm.z();
                counter++;
            }
            x /= counter;
            y /= counter;
            z /= counter;

            Normal normal = new Normal(x, y, z);
            normalize(normal);
            newNormals.add(normal);
        }

        return newNormals;
    }

    private static Normal calculateNormalForPolygon(Polygon polygon, List<Vertex> vertices) {
        ArrayList<Vertex> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int index = polygon.vertexIndices().get(i);
            list.add(vertices.get(index));
        }
        // вот список из 3 вершин, нужно реализовать правильный порядок
        // подбора векторов и затем выполнить их векторное произведение.
    }

    private static void normalize(Normal normal) {
        //
    }
}

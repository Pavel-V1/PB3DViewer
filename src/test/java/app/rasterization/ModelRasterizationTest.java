package app.rasterization;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import app.rend_prepare.ModelTriangulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModelRasterizationTest {
    private static ArrayList<List> getVerticesAndPolygons() {
        java.util.List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(1, 3, 0));
        vertices.add(new Vertex(2, 2, 0));
        vertices.add(new Vertex(2, 1, 0));
        vertices.add(new Vertex(1, 0, 0));
        vertices.add(new Vertex(0, 1, 0));
        vertices.add(new Vertex(0, 2, 0));
        vertices.add(new Vertex(0, 2, 1));
        vertices.add(new Vertex(0, 1, 1));
        vertices.add(new Vertex(1, 1, 1));

        List<Polygon> polygons = new ArrayList<>(); /// Создаем полигоны в соответствии с вершинами.
        List<Integer> vi1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            vi1.add(i);
        }
        polygons.add(new app.model.Polygon(vi1, null, null));
        List<Integer> vi2 = new ArrayList<>();
        vi2.add(5);
        vi2.add(4);
        vi2.add(7);
        vi2.add(6);
        polygons.add(new app.model.Polygon(vi2, null, null));
        List<Integer> vi3 = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            vi3.add(i);
        }
        polygons.add(new Polygon(vi3, null, null));

        ArrayList<List> list = new ArrayList<>();
        list.add(vertices);
        list.add(polygons);
        return list;
    }

    public static void main(String[] args) {
        List<Vertex> vertices = getVerticesAndPolygons().get(0);
        List<Polygon> polygons = getVerticesAndPolygons().get(1);
        List<Integer> indexes = new ArrayList<>();
        indexes.add(3);
        indexes.add(4);
        indexes.add(7);
        polygons.add(new Polygon(indexes, null, null));

        Model model = new Model();
        model.getPolygons().addAll(ModelTriangulation.triangulate(polygons, vertices));
        model.getVertices().addAll(vertices);

        ModelRasterization.rasterizeModel(model, 200, new Color(255, 119, 21));
    }
}

package app.rend_prepare;

import app.model.Model;
import app.model.Normal;
import app.model.Polygon;
import app.model.Vertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PrepareTest {

    private ArrayList<List> getVerticesAndPolygons() {
        List<Vertex> vertices = new ArrayList<>();
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
        polygons.add(new Polygon(vi1, null, null));
        List<Integer> vi2 = new ArrayList<>();
        vi2.add(5);
        vi2.add(4);
        vi2.add(7);
        vi2.add(6);
        polygons.add(new Polygon(vi2, null, null));
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

    @Test
    public void triangulationTest() {
        List<Vertex> vertices = getVerticesAndPolygons().get(0);
        List<Polygon> polygons = getVerticesAndPolygons().get(1);

        /// Создаем список ожидаемых полигонов-треугольников.
        List<Polygon> expectedPolygons = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            List<Integer> pl = new ArrayList<>();
            pl.add(0);
            pl.add(i);
            pl.add(i + 1);
            expectedPolygons.add(new Polygon(pl, null, null));
        }
        List<Integer> pl1 = new ArrayList<>();
        pl1.add(5);
        pl1.add(4);
        pl1.add(7);
        expectedPolygons.add(new Polygon(pl1, null, null));
        List<Integer> pl2 = new ArrayList<>();
        pl2.add(5);
        pl2.add(7);
        pl2.add(6);
        expectedPolygons.add(new Polygon(pl2, null, null));
        List<Integer> pl3 = new ArrayList<>();
        pl3.add(6);
        pl3.add(7);
        pl3.add(8);
        expectedPolygons.add(new Polygon(pl3, null, null));

        /// Получаем список реальных полигонов-треугольников.
        List<Polygon> actualPolygons = ModelTriangulation.triangulate(polygons, vertices);

        Assertions.assertEquals(expectedPolygons, actualPolygons);
    }

    @Test
    public void normalsCalculatingTest() {
        List<Vertex> vertices = getVerticesAndPolygons().get(0);
        List<Polygon> polygons = getVerticesAndPolygons().get(1);

        /// Создаем список ожидаемых нормалей.
        List<Normal> expectedNormals = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            expectedNormals.add(new Normal(0, 0, -1));
        }
        for (int i = 0; i < 2; i++) {
            expectedNormals.add(new Normal((float) -Math.cos(Math.toRadians(45)), 0, (float) -Math.sin(Math.toRadians(45))));
        }
        for (int i = 0; i < 2; i++) {
            expectedNormals.add(new Normal((float) -Math.cos(Math.toRadians(45)), 0, (float) Math.sin(Math.toRadians(45))));
        }
        expectedNormals.add(new Normal(0, 0, 1));

        /// Получаем список реальных нормалей.
        List<Normal> actualNormals = NormalsCalculating.calculateNormals(polygons, vertices);

        Assertions.assertEquals(expectedNormals, actualNormals);
    }

    @Test
    public void getNewModelTest() {
        Model model = new Model();
        List<Vertex> vertices = getVerticesAndPolygons().get(0);
        List<Polygon> polygons = getVerticesAndPolygons().get(1);
        model.getVertices().addAll(vertices);
        model.getPolygons().addAll(polygons);

        List<Polygon> newPolygons = ModelTriangulation.triangulate(polygons, vertices);
        List<Normal> newNormals = NormalsCalculating.calculateNormals(polygons, vertices);
        Model newModel = GetNewModel.getNewModel(model);

        Assertions.assertEquals(newPolygons, newModel.getPolygons());
        Assertions.assertEquals(newNormals, newModel.getNormals());
    }
}

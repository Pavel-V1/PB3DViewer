package app.rend_prepare;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PrepareTest {
    @Test
    public void triangulationTest() {
        Model model = new Model();
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
        model.getVertices().addAll(vertices); /// Добавляем вершины в модель.

        List<Polygon> polygons = new ArrayList<>(); /// Создаем полигоны в соответствии с вершинами.
        List<Integer> vi1 = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            vi1.add(i);
        }
        polygons.add(new Polygon(vi1, null, null));
        List<Integer> vi2 = new ArrayList<>();
        for (int i = 4; i < 8; i++) {
            vi2.add(i);
        }
        polygons.add(new Polygon(vi2, null, null));
        List<Integer> vi3 = new ArrayList<>();
        for (int i = 6; i < 9; i++) {
            vi3.add(i);
        }
        polygons.add(new Polygon(vi3, null, null));
        model.getPolygons().addAll(polygons); /// Добавляем полигоны в модель.

        List<Polygon> expectedPolygons = new ArrayList<>(); /// Создаем список ожидаемых полигонов-треугольников.
        for (int i = 1; i < 5; i++) {
            List<Integer> pl = new ArrayList<>();
            pl.add(0);
            pl.add(i);
            pl.add(i + 1);
            expectedPolygons.add(new Polygon(pl, null, null));
        }
        for (int i = 5; i < 7; i++) {
            List<Integer> pl = new ArrayList<>();
            pl.add(4);
            pl.add(i);
            pl.add(i + 1);
            expectedPolygons.add(new Polygon(pl, null, null));
        }
        List<Integer> pl = new ArrayList<>();
        pl.add(6);
        pl.add(7);
        pl.add(8);
        expectedPolygons.add(new Polygon(pl, null, null));

        /// Получаем список реальных полигонов-треугольников.
        List<Polygon> newPolygonList = ModelTriangulation.triangulate(polygons, vertices);

        Assertions.assertEquals(expectedPolygons, newPolygonList);
    }

    @Test
    public void normalsCalculatingTest(){
        //
    }

    @Test
    public void getNewModelTest() {
        //
    }
}

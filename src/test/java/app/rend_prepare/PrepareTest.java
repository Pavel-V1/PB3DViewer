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
        vertices.add(new Vertex(0, 1, 0));
        vertices.add(new Vertex(0, 2, 0));
        vertices.add(new Vertex(1, 0, 0));
        vertices.add(new Vertex(1, 3, 0));
        vertices.add(new Vertex(2, 1, 0));
        vertices.add(new Vertex(2, 2, 0));
        vertices.add(new Vertex(0, 1, 1));
        vertices.add(new Vertex(0, 2, 1));
        vertices.add(new Vertex(1, 1, 1));
        model.getVertices().addAll(vertices);

        List<Integer> vi = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            vi.add(i);
        }
        model.getPolygons().add(new Polygon(vi, null, null));
        vi.clear();
        for (int i = 4; i < 8; i++) {
            vi.add(i);
        }
        model.getPolygons().add(new Polygon(vi, null, null));
        vi.clear();
        for (int i = 6; i < 9; i++) {
            vi.add(i);
        }
        model.getPolygons().add(new Polygon(vi, null, null));

        Model newModel = ModelTriangulation.triangulate(model);
        List<Polygon> newPolygons = new ArrayList<>();
        //
        Assertions.assertEquals(newPolygons, newModel.getPolygons());
    }

    @Test
    public void normalsCalculateTest(){
        //
    }
}

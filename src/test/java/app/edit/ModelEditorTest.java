package app.edit;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelEditorTest {

    @Test
    void removePolygonRemovesCorrect() {

        Model m = new Model();
        m.getVertices().addAll(List.of(
                new Vertex(0, 0, 0), // индекс 0
                new Vertex(1, 0, 0), // индекс 1
                new Vertex(0, 1, 0)  // индекс 2
        ));

        m.getPolygons().add(new Polygon(List.of(0, 1, 2), List.of(), List.of()));
        m.getPolygons().add(new Polygon(List.of(0, 2, 1), List.of(), List.of()));

        assertEquals(2, m.getPolygons().size());
        ModelEditor.removePolygon(m, 0);
        assertEquals(1, m.getPolygons().size());
    }

    @Test
    void removeVertexAndPolygonsDeletesPolysUsingIt_andFixesIndices() {
        Model m = new Model();

        m.getVertices().addAll(List.of(
                new Vertex(0, 0, 0),
                new Vertex(1, 0, 0),
                new Vertex(0, 1, 0),
                new Vertex(0, 0, 1)
        ));

        m.getPolygons().add(new Polygon(List.of(0, 1, 2), List.of(), List.of()));
        m.getPolygons().add(new Polygon(List.of(0, 2, 3), List.of(), List.of()));

        ModelEditor.removeVertexAndPolygons(m, 1);
        assertEquals(3, m.getVertices().size());
        assertEquals(1, m.getPolygons().size());

        assertEquals(List.of(0, 1, 2), m.getPolygons().get(0).vertexIndices());
    }
}

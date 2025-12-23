package app.obj;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObjWriterTest {

    @Test
    void writesSimpleModel() throws Exception {
        Model m = new Model();
        m.getVertices().addAll(List.of(
                new Vertex(0, 0, 0),
                new Vertex(1, 0, 0),
                new Vertex(0, 1, 0)
        ));
        m.getPolygons().add(new Polygon(List.of(0, 1, 2),
                List.of(),
                List.of()));

        StringWriter out = new StringWriter();
        new ObjWriter().write(m, out);

        String s = out.toString();

        assertTrue(s.contains("v 0.0 0.0 0.0"));
        assertTrue(s.contains("v 1.0 0.0 0.0"));
        assertTrue(s.contains("v 0.0 1.0 0.0"));

        assertTrue(s.contains("f 1 2 3"));
    }

    @Test
    void throwsOnNullModel() {
        assertThrows(IllegalArgumentException.class,
                () -> new ObjWriter().write(null, new StringWriter()));
    }

    @Test
    void throwsOnNullWriter() {
        assertThrows(IllegalArgumentException.class,
                () -> new ObjWriter().write(new Model(), null));
    }
}


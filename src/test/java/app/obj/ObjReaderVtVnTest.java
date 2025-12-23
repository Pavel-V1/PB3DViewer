package app.obj;

import app.model.Model;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class ObjReaderVtVnTest {

    @Test
    void readsVtAndVn() throws Exception {
        String obj = """
                v 0 0 0
                v 1 0 0
                v 0 1 0
                vt 0 0
                vt 1 0
                vt 0 1
                vn 0 0 1
                f 1/1/1 2/2/1 3/3/1
                """;

        Model m = new ObjReader().read(new StringReader(obj));

        assertEquals(3, m.getVertices().size());
        assertEquals(3, m.getTexCoords().size());
        assertEquals(1, m.getNormals().size());
        assertEquals(1, m.getPolygons().size());
    }
}


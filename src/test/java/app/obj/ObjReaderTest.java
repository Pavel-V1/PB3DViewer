package app.obj;

import app.model.Model;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class ObjReaderTest {

    @Test
    void readsSimpleModel() throws Exception {
        String obj = """
                v 0 0 0
                v 1 0 0
                v 0 1 0
                f 1 2 3
                """;

        ObjReader reader = new ObjReader();
        Model model = reader.read(new StringReader(obj));

        assertEquals(3, model.getVertices().size());
        assertEquals(1, model.getPolygons().size());
    }

    @Test
    void ignoresCommentsAndEmptyLines() throws Exception {
        String obj = """
                # comment

                v 0 0 0
                # another comment
                v 1 0 0

                f 1 2 1
                """;

        ObjReader reader = new ObjReader();
        Model model = reader.read(new StringReader(obj));

        assertEquals(2, model.getVertices().size());
        assertEquals(1, model.getPolygons().size());
    }

    @Test
    void throwsOnInvalidVertex() {
        String obj = "v 0 0";

        ObjReader reader = new ObjReader();

        assertThrows(
                IllegalArgumentException.class,
                () -> reader.read(new StringReader(obj))
        );
    }
}


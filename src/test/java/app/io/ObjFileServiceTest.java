package app.io;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObjFileServiceTest {

    @Test
    void saveThenLoadKeepsCounts() throws Exception {
        Model m = new Model();
        m.getVertices().addAll(List.of(
                new Vertex(0,0,0),
                new Vertex(1,0,0),
                new Vertex(0,1,0)
        ));
        m.getPolygons().add(new Polygon(List.of(0,1,2), List.of(), List.of()));

        File tmp = Files.createTempFile("model", ".obj").toFile();
        tmp.deleteOnExit();

        ObjFileService svc = new ObjFileService();
        svc.save(m, tmp);

        Model loaded = svc.load(tmp);

        assertEquals(3, loaded.getVertices().size());
        assertEquals(1, loaded.getPolygons().size());
    }
}



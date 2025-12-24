package app.scene;

import app.model.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceneControllerTest {

    @Test
    void addModelMakesItActive() {
        Scene scene = new Scene();
        SceneController c = new SceneController(scene);

        c.addModel("A", new Model());
        assertNotNull(c.getActive());
        assertEquals("A", c.getActive().name());
        assertEquals(0, scene.getActiveIndex());
    }
}

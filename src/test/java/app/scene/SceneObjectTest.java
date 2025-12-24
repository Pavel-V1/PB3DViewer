package app.scene;

import app.model.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceneObjectTest {

    @Test
    void sceneObjectHasTransform() {
        Model dummy = new Model();
        SceneObject obj = new SceneObject("Test", dummy);
        assertNotNull(obj.transform());
        assertNotNull(obj.transform().getScale());
    }
}

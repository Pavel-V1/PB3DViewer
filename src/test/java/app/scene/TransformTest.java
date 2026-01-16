package app.scene;

import app.math.Vector3;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransformTest {

    @Test
    void defaultsAreIdentityLike() {
        Transform t = new Transform();
        assertEquals(0, t.getTranslation().x, 1e-6);
        assertEquals(0, t.getTranslation().y, 1e-6);
        assertEquals(0, t.getTranslation().z, 1e-6);

        assertEquals(0, t.getRotationRad().x, 1e-6);
        assertEquals(0, t.getRotationRad().y, 1e-6);
        assertEquals(0, t.getRotationRad().z, 1e-6);

        assertEquals(1, t.getScale().x, 1e-6);
        assertEquals(1, t.getScale().y, 1e-6);
        assertEquals(1, t.getScale().z, 1e-6);
    }

    @Test
    void canSetValues() {
        Transform t = new Transform();
        t.setTranslation(new Vector3(1,2,3));
        t.setRotationRad(new Vector3(0.1f,0.2f,0.3f));
        t.setScale(new Vector3(2,2,2));

        assertEquals(1, t.getTranslation().x, 1e-6);
        assertEquals(2, t.getTranslation().y, 1e-6);
        assertEquals(3, t.getTranslation().z, 1e-6);

        assertEquals(2, t.getScale().x, 1e-6);
    }
}

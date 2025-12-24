package app.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphicConveyorTest {

    private static void assertFloatEquals(float expected, float actual) {
        assertEquals(expected, actual, 1e-5f);
    }

    @Test
    void lookAt_defaultCameraGivesIdentity() {
        // Камера в (0,0,0) смотрит в -Z, up = +Y -> видовая матрица должна быть identity
        Matrix4 view = GraphicConveyor.lookAt(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, -1),
                new Vector3(0, 1, 0)
        );
        assertFloatEquals(1, view.get(0, 0));
        assertFloatEquals(1, view.get(1, 1));
        assertFloatEquals(1, view.get(2, 2));
        assertFloatEquals(1, view.get(3, 3));

        assertFloatEquals(0, view.get(0, 3));
        assertFloatEquals(0, view.get(1, 3));
        assertFloatEquals(0, view.get(2, 3));
    }

    @Test
    void lookAt_movesWorldOppositeToEye() {
        Matrix4 view = GraphicConveyor.lookAt(
                new Vector3(0, 0, 5),
                new Vector3(0, 0, 0),
                new Vector3(0, 1, 0)
        );

        Vector4 origin = new Vector4(0, 0, 0, 1);
        Vector4 r = view.mul(origin);

        assertFloatEquals(0, r.x);
        assertFloatEquals(0, r.y);
        assertFloatEquals(-5, r.z);
        assertFloatEquals(1, r.w);
    }

    @Test
    void perspective_hasExpectedStructure() {
        Matrix4 p = GraphicConveyor.perspective((float) Math.toRadians(90), 1.0f, 1.0f, 10.0f);

        assertFloatEquals(-1, p.get(3, 2));
        assertFloatEquals(0, p.get(3, 3));

        assertTrue(p.get(0, 0) > 0);
        assertTrue(p.get(1, 1) > 0);
    }
}

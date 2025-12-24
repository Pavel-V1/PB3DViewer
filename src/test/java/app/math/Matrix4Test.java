package app.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Matrix4Test {

    private static void assertFloatEquals(float expected, float actual) {
        assertEquals(expected, actual, 1e-5f);
    }

    @Test
    void identityDoesNotChangeVector() {
        Vector4 v = new Vector4(1, 2, 3, 1);
        Vector4 r = Matrix4.identity().mul(v);

        assertFloatEquals(1, r.x);
        assertFloatEquals(2, r.y);
        assertFloatEquals(3, r.z);
        assertFloatEquals(1, r.w);
    }

    @Test
    void translationMovesPoint() {
        Vector4 v = new Vector4(1, 2, 3, 1);
        Vector4 r = Matrix4.translation(10, 20, 30).mul(v);

        assertFloatEquals(11, r.x);
        assertFloatEquals(22, r.y);
        assertFloatEquals(33, r.z);
        assertFloatEquals(1, r.w);
    }

    @Test
    void scaleScalesPoint() {
        Vector4 v = new Vector4(1, 2, 3, 1);
        Vector4 r = Matrix4.scale(2, 3, 4).mul(v);

        assertFloatEquals(2, r.x);
        assertFloatEquals(6, r.y);
        assertFloatEquals(12, r.z);
        assertFloatEquals(1, r.w);
    }
}

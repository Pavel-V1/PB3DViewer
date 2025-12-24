package app.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AffineTransformsTest {

    private static void assertF(float expected, float actual) {
        assertEquals(expected, actual, 1e-5f);
    }

    @Test
    void modelMatrix_translationMovesPoint() {
        Matrix4 m = GraphicConveyor.modelMatrix(
                new Vector3(10, 0, 0),
                new Vector3(0, 0, 0),
                new Vector3(1, 1, 1)
        );

        Vector4 p = new Vector4(1, 2, 3, 1);
        Vector4 r = m.mul(p);

        assertF(11, r.x);
        assertF(2, r.y);
        assertF(3, r.z);
        assertF(1, r.w);
    }

    @Test
    void modelMatrix_scaleScalesPoint() {
        Matrix4 m = GraphicConveyor.modelMatrix(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, 0),
                new Vector3(2, 3, 4)
        );

        Vector4 p = new Vector4(1, 1, 1, 1);
        Vector4 r = m.mul(p);

        assertF(2, r.x);
        assertF(3, r.y);
        assertF(4, r.z);
        assertF(1, r.w);
    }

    @Test
    void modelMatrix_rotationZ_90deg() {
        Matrix4 m = GraphicConveyor.modelMatrix(
                new Vector3(0, 0, 0),
                new Vector3(0, 0, (float) Math.toRadians(90)),
                new Vector3(1, 1, 1)
        );

        Vector4 p = new Vector4(1, 0, 0, 1);
        Vector4 r = m.mul(p);

        assertF(0, r.x);
        assertF(1, r.y);
        assertF(0, r.z);
        assertF(1, r.w);
    }
}

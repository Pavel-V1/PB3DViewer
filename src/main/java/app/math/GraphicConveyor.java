package app.math;

public final class GraphicConveyor {

    private GraphicConveyor() {}

    public static Matrix4 lookAt(Vector3 eye, Vector3 target, Vector3 up) {
        Vector3 f = target.sub(eye).normalize();
        Vector3 s = f.cross(up).normalize();
        Vector3 u = s.cross(f);

        float[][] m = new float[4][4];
        m[0][0] = s.x;  m[0][1] = s.y;  m[0][2] = s.z;  m[0][3] = -s.dot(eye);
        m[1][0] = u.x;  m[1][1] = u.y;  m[1][2] = u.z;  m[1][3] = -u.dot(eye);
        m[2][0] = -f.x; m[2][1] = -f.y; m[2][2] = -f.z; m[2][3] =  f.dot(eye);
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return Matrix4.fromArray(m);
    }

    public static Matrix4 perspective(float fovRad, float aspect, float nearZ, float farZ) {
        if (nearZ <= 0 || farZ <= 0 || farZ <= nearZ) {
            throw new IllegalArgumentException("Invalid near/far planes");
        }
        if (aspect <= 0) {
            throw new IllegalArgumentException("Aspect must be > 0");
        }

        float f = 1.0f / (float) Math.tan(fovRad / 2.0f);

        float[][] m = new float[4][4];
        m[0][0] = f / aspect;
        m[1][1] = f;

        m[2][2] = (farZ + nearZ) / (nearZ - farZ);
        m[2][3] = (2.0f * farZ * nearZ) / (nearZ - farZ);
        m[3][2] = -1.0f;
        m[3][3] = 0.0f;

        return Matrix4.fromArray(m);
    }
}

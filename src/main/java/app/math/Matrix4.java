package app.math;

public class Matrix4 {

    private final float[][] m; // m[row][col]

    private Matrix4(float[][] values) {
        this.m = values;
    }

    public static Matrix4 identity() { //единичная матрица
        float[][] v = new float[4][4];
        v[0][0] = 1; v[1][1] = 1; v[2][2] = 1; v[3][3] = 1;
        return new Matrix4(v);
    }

    public static Matrix4 translation(float tx, float ty, float tz) { //матрица переноса
        Matrix4 r = identity();
        // для вектора-столбца перенос сидит в последнем столбце
        r.m[0][3] = tx;
        r.m[1][3] = ty;
        r.m[2][3] = tz;
        return r;
    }

    public static Matrix4 scale(float sx, float sy, float sz) { //матрица масштабирования
        float[][] v = new float[4][4];
        v[0][0] = sx;
        v[1][1] = sy;
        v[2][2] = sz;
        v[3][3] = 1;
        return new Matrix4(v);
    }

    public static Matrix4 rotationX(float angleRad) {
        Matrix4 r = identity();
        float c = (float) Math.cos(angleRad);
        float s = (float) Math.sin(angleRad);

        r.m[1][1] = c;
        r.m[1][2] = -s;
        r.m[2][1] = s;
        r.m[2][2] = c;
        return r;
    }

    public static Matrix4 rotationY(float angleRad) {
        Matrix4 r = identity();
        float c = (float) Math.cos(angleRad);
        float s = (float) Math.sin(angleRad);

        r.m[0][0] = c;
        r.m[0][2] = s;
        r.m[2][0] = -s;
        r.m[2][2] = c;
        return r;
    }

    public static Matrix4 rotationZ(float angleRad) {
        Matrix4 r = identity();
        float c = (float) Math.cos(angleRad);
        float s = (float) Math.sin(angleRad);

        r.m[0][0] = c;
        r.m[0][1] = -s;
        r.m[1][0] = s;
        r.m[1][1] = c;
        return r;
    }

    public Matrix4 mul(Matrix4 other) { //умножение матрицы на матрицу
        float[][] res = new float[4][4];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sum = 0;
                for (int k = 0; k < 4; k++) {
                    sum += this.m[row][k] * other.m[k][col];
                }
                res[row][col] = sum;
            }
        }
        return new Matrix4(res);
    }

    public Vector4 mul(Vector4 v) {
        float rx = m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3] * v.w; //умножение матрицы на вектор-столбец
        float ry = m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3] * v.w;
        float rz = m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3] * v.w;
        float rw = m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3] * v.w;
        return new Vector4(rx, ry, rz, rw);
    }

    public float get(int row, int col) {
        return m[row][col];
    }
}

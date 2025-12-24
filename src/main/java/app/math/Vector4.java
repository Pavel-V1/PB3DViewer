package app.math;

public class Vector4 {

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vector4 fromVector3(Vector3 v) { // создание вектор4 из вектор 3 с w = 1
        return new Vector4(v.x, v.y, v.z, 1.0f);
    }

    public Vector4 mul(float scalar) { //умножение на скаляр
        return new Vector4(
                x * scalar,
                y * scalar,
                z * scalar,
                w * scalar
        );
    }

    public Vector3 toVector3() { //преобразование обратно в вектор3
        if (w == 0.0f) {
            return new Vector3(x, y, z);
        }
        return new Vector3(x / w, y / w, z / w);
    }

    @Override
    public String toString() {
        return "Vector4(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}

package app.math;

public class Vector3 {

    public final float x;
    public final float y;
    public final float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) { //сложение векторов
        return new Vector3(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    public Vector3 sub(Vector3 other) { //вычитание векторов
        return new Vector3(
                this.x - other.x,
                this.y - other.y,
                this.z - other.z
        );
    }

    public Vector3 mul(float scalar) { //умножение вектора на скаляр
        return new Vector3(
                this.x * scalar,
                this.y * scalar,
                this.z * scalar
        );
    }

    public float dot(Vector3 other) { //скалярное произведение
        return this.x * other.x
                + this.y * other.y
                + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) { //векторное произведение
        return new Vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public float length() { //длина вектора
        return (float) Math.sqrt(this.dot(this));
    }

    public Vector3 normalize() { //нормализация
        float len = length();
        if (len == 0.0f) {
            return new Vector3(0, 0, 0);
        }
        return this.mul(1.0f / len);
    }

    @Override
    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}

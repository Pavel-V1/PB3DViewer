package app.scene;

import app.math.Vector3;

public final class Transform {
    private Vector3 translation = new Vector3(0, 0, 0);
    private Vector3 rotationRad = new Vector3(0, 0, 0);
    private Vector3 scale = new Vector3(1, 1, 1);

    public Vector3 getTranslation() { return translation; }
    public Vector3 getRotationRad() { return rotationRad; }
    public Vector3 getScale() { return scale; }

    public void setTranslation(Vector3 translation) { this.translation = translation; }
    public void setRotationRad(Vector3 rotationRad) { this.rotationRad = rotationRad; }
    public void setScale(Vector3 scale) { this.scale = scale; }
}

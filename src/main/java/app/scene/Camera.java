package app.scene;

import app.math.Vector3;

public final class Camera {
    private Vector3 target = new Vector3(0, 0, 0);
    private Vector3 up = new Vector3(0, 1, 0);

    private float distance = 5.0f;     // расстояние до target
    private float yawRad = 0.0f;       // вокруг Y
    private float pitchRad = 0.0f;     // вверх/вниз

    // перспектива
    private float fovRad = (float) Math.toRadians(60.0);
    private float nearZ = 0.1f;
    private float farZ = 1000.0f;

    public Vector3 target() { return target; }
    public Vector3 up() { return up; }
    public float distance() { return distance; }
    public float yawRad() { return yawRad; }
    public float pitchRad() { return pitchRad; }

    public float fovRad() { return fovRad; }
    public float nearZ() { return nearZ; }
    public float farZ() { return farZ; }

    public void setTarget(Vector3 target) { this.target = target; }
    public void setUp(Vector3 up) { this.up = up; }

    public void setDistance(float distance) { this.distance = Math.max(0.05f, distance); }
    public void setYawRad(float yawRad) { this.yawRad = yawRad; }
    public void setPitchRad(float pitchRad) {
        // чтобы не переворачивало
        float limit = (float) Math.toRadians(89.0);
        this.pitchRad = Math.max(-limit, Math.min(limit, pitchRad));
    }

    public void setFovRad(float fovRad) { this.fovRad = fovRad; }
    public void setNearZ(float nearZ) { this.nearZ = nearZ; }
    public void setFarZ(float farZ) { this.farZ = farZ; }

    public Vector3 eye() {
        float cy = (float) Math.cos(yawRad);
        float sy = (float) Math.sin(yawRad);
        float cp = (float) Math.cos(pitchRad);
        float sp = (float) Math.sin(pitchRad);

        // направление от target к eye
        Vector3 dir = new Vector3(cp * sy, sp, cp * cy);
        return target.add(dir.mul(distance));
    }

    public void reset() {
        target = new Vector3(0, 0, 0);
        up = new Vector3(0, 1, 0);
        distance = 5.0f;
        yawRad = 0.0f;
        pitchRad = 0.0f;
        fovRad = (float) Math.toRadians(60.0);
        nearZ = 0.1f;
        farZ = 1000.0f;
    }
}

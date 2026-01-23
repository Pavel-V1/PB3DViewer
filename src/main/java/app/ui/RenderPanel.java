package app.ui;

import app.math.GraphicConveyor;
import app.math.Matrix4;
import app.math.Vector4;
import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import app.scene.Camera;
import app.scene.SceneController;
import app.scene.SceneObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RenderPanel extends JPanel {

    private final SceneController sceneController;
    private final Camera camera = new Camera();

    // для мыши
    private int lastX, lastY;
    private boolean rotating = false;
    private boolean panning = false;

    // чтобы один раз подобрать дистанцию под модель
    private String fittedModelId = null;

    public RenderPanel(SceneController sceneController) {
        this.sceneController = sceneController;
        setBackground(Color.WHITE);

        setFocusable(true);
        setupMouse();
        setupKeys();
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        SceneObject active = sceneController.getActive();
        if (active == null) {
            g2.setColor(Color.BLACK);
            g2.drawString("Нет активной модели. Открой OBJ: Файл → Открыть…", 10, 20);
            return;
        }

        Model m = active.model();
        List<Vertex> vs = m.getVertices();
        List<Polygon> ps = m.getPolygons();
        if (vs.isEmpty() || ps.isEmpty()) {
            g2.setColor(Color.BLACK);
            g2.drawString("Модель пустая (нет вершин/полигонов).", 10, 20);
            return;
        }

        autoFitCameraOnce(active, vs);

        var tr = active.transform();
        Matrix4 model = GraphicConveyor.modelMatrix(tr.getTranslation(), tr.getRotationRad(), tr.getScale());

        float aspect = (h == 0) ? 1.0f : (float) w / (float) h;

        Matrix4 view = GraphicConveyor.lookAt(camera.eye(), camera.target(), camera.up());
        Matrix4 proj = GraphicConveyor.perspective(camera.fovRad(), aspect, camera.nearZ(), camera.farZ());

        Matrix4 mvp = proj.mul(view).mul(model);

        g2.setColor(Color.BLACK);

        for (Polygon p : ps) {
            List<Integer> idxs = p.vertexIndices();
            int n = idxs.size();
            if (n < 2) continue;

            for (int i = 0; i < n; i++) {
                int ia = idxs.get(i);
                int ib = idxs.get((i + 1) % n);
                if (ia < 0 || ia >= vs.size() || ib < 0 || ib >= vs.size()) continue;

                Vertex a = vs.get(ia);
                Vertex b = vs.get(ib);

                Vector4 ca = mvp.mul(new Vector4(a.x(), a.y(), a.z(), 1f));
                Vector4 cb = mvp.mul(new Vector4(b.x(), b.y(), b.z(), 1f));

                if (Math.abs(ca.w) < 1e-6f || Math.abs(cb.w) < 1e-6f) continue;

                float ax = ca.x / ca.w;
                float ay = ca.y / ca.w;

                float bx = cb.x / cb.w;
                float by = cb.y / cb.w;

                int x1 = Math.round((ax + 1f) * 0.5f * w);
                int y1 = Math.round((1f - (ay + 1f) * 0.5f) * h);

                int x2 = Math.round((bx + 1f) * 0.5f * w);
                int y2 = Math.round((1f - (by + 1f) * 0.5f) * h);

                g2.drawLine(x1, y1, x2, y2);
            }
        }

        g2.drawString("Активная: " + active.name() + " | Камера: ЛКМ-вращ, ПКМ-pan, колесо-zoom, WASD/QE", 10, 20);
    }

    private void autoFitCameraOnce(SceneObject active, List<Vertex> vs) {
        String id;
        try {
            id = active.id();
        } catch (Exception ex) {
            id = active.name();
        }

        if (id != null && id.equals(fittedModelId)) return;
        fittedModelId = id;

        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

        for (Vertex v : vs) {
            minX = Math.min(minX, v.x()); maxX = Math.max(maxX, v.x());
            minY = Math.min(minY, v.y()); maxY = Math.max(maxY, v.y());
            minZ = Math.min(minZ, v.z()); maxZ = Math.max(maxZ, v.z());
        }

        float cx = (minX + maxX) * 0.5f;
        float cy = (minY + maxY) * 0.5f;
        float cz = (minZ + maxZ) * 0.5f;

        float dx = (maxX - minX);
        float dy = (maxY - minY);
        float dz = (maxZ - minZ);

        float radius = 0.5f * (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
        radius = Math.max(radius, 0.5f);

        camera.setTarget(new app.math.Vector3(cx, cy, cz));

        float fov = camera.fovRad();
        float dist = (float) (radius / Math.tan(fov * 0.5f)) * 1.5f;
        camera.setDistance(dist);

        camera.setYawRad(0f);
        camera.setPitchRad(0f);
    }

    private void setupMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                lastX = e.getX();
                lastY = e.getY();
                rotating = SwingUtilities.isLeftMouseButton(e);
                panning = SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rotating = false;
                panning = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int dx = x - lastX;
                int dy = y - lastY;
                lastX = x;
                lastY = y;

                if (rotating) {
                    float sens = 0.01f;
                    camera.setYawRad(camera.yawRad() + dx * sens);
                    camera.setPitchRad(camera.pitchRad() - dy * sens);
                    repaint();
                } else if (panning) {

                    var eye = camera.eye();
                    var forward = camera.target().sub(eye).normalize();
                    var right = forward.cross(camera.up()).normalize();

                    float panSpeed = 0.0025f * camera.distance();
                    camera.setTarget(
                            camera.target()
                                    .add(right.mul(-dx * panSpeed))
                                    .add(camera.up().mul(dy * panSpeed))
                    );
                    repaint();
                }
            }
        });

        addMouseWheelListener(e -> {
            float factor = (float) Math.pow(1.1, e.getPreciseWheelRotation());
            camera.setDistance(camera.distance() * factor);
            repaint();
        });
    }

    private void setupKeys() {
        int cond = JComponent.WHEN_FOCUSED;

        bind(cond, "W", () -> moveTarget(0, 0, -1));
        bind(cond, "S", () -> moveTarget(0, 0, 1));
        bind(cond, "A", () -> moveTarget(-1, 0, 0));
        bind(cond, "D", () -> moveTarget(1, 0, 0));
        bind(cond, "Q", () -> moveTarget(0, -1, 0));
        bind(cond, "E", () -> moveTarget(0, 1, 0));

        bind(cond, "R", () -> {
            camera.reset();
            fittedModelId = null;
            repaint();
        });
    }

    private void moveTarget(int x, int y, int z) {
        var eye = camera.eye();
        var forward = camera.target().sub(eye).normalize();
        var right = forward.cross(camera.up()).normalize();
        var up = camera.up();

        float speed = 0.03f * camera.distance();

        var delta = right.mul(x * speed)
                .add(up.mul(y * speed))
                .add(forward.mul(z * speed));

        camera.setTarget(camera.target().add(delta));
        repaint();
    }

    private void bind(int cond, String key, Runnable action) {
        String id = "act_" + key;
        getInputMap(cond).put(KeyStroke.getKeyStroke(key), id);
        getActionMap().put(id, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }
}

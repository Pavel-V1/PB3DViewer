package app.ui;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;
import app.scene.SceneController;
import app.scene.SceneObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RenderPanel extends JPanel {

    private final SceneController sceneController;

    public RenderPanel(SceneController sceneController) {
        this.sceneController = sceneController;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;

        // оси
        g2.setColor(new Color(230, 230, 230));
        g2.drawLine(0, cy, w, cy);
        g2.drawLine(cx, 0, cx, h);

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

        // 1) ищем bounds по X/Y, чтобы красиво вписать
        float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;

        for (Vertex v : vs) {
            if (v.x() < minX) minX = v.x();
            if (v.x() > maxX) maxX = v.x();
            if (v.y() < minY) minY = v.y();
            if (v.y() > maxY) maxY = v.y();
        }

        float dx = Math.max(1e-6f, maxX - minX);
        float dy = Math.max(1e-6f, maxY - minY);

        // 2) масштаб + отступы
        float pad = 40f;
        float sx = (w - 2 * pad) / dx;
        float sy = (h - 2 * pad) / dy;
        float s = Math.min(sx, sy);

        // 3) центрирование: переносим модель так, чтобы её центр оказался в центре панели
        float midX = (minX + maxX) / 2f;
        float midY = (minY + maxY) / 2f;

        g2.setColor(Color.BLACK);

        // рисуем каркас: для каждого полигона соединяем вершины по кругу
        for (Polygon p : ps) {
            List<Integer> idxs = p.vertexIndices();
            int n = idxs.size();
            if (n < 2) continue;

            for (int i = 0; i < n; i++) {
                int a = idxs.get(i);
                int b = idxs.get((i + 1) % n);

                if (a < 0 || a >= vs.size() || b < 0 || b >= vs.size()) continue;

                Vertex va = vs.get(a);
                Vertex vb = vs.get(b);

                int x1 = cx + Math.round((va.x() - midX) * s);
                int y1 = cy - Math.round((va.y() - midY) * s); // y инвертируем для экрана
                int x2 = cx + Math.round((vb.x() - midX) * s);
                int y2 = cy - Math.round((vb.y() - midY) * s);

                g2.drawLine(x1, y1, x2, y2);
            }
        }

        // подпись
        g2.drawString("Активная: " + active.name() + " | каркас (X/Y)", 10, 20);
    }
}

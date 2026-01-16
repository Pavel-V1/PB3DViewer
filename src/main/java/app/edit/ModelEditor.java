package app.edit;

import app.model.Model;
import app.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public final class ModelEditor {

    private ModelEditor() {}

    public static void removePolygon(Model model, int polygonIndex) {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (polygonIndex < 0 || polygonIndex >= model.getPolygons().size()) {
            throw new IllegalArgumentException("polygonIndex out of range: " + polygonIndex);
        }
        model.getPolygons().remove(polygonIndex);
    }

    public static void removeVertexAndPolygons(Model model, int vertexIndex) {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (vertexIndex < 0 || vertexIndex >= model.getVertices().size()) {
            throw new IllegalArgumentException("vertexIndex out of range: " + vertexIndex);
        }

        // 1) Удаляем полигоны, которые используют эту вершину
        model.getPolygons().removeIf(p -> usesVertex(p, vertexIndex));

        // 2) Удаляем вершину (сдвигает индексы справа)
        model.getVertices().remove(vertexIndex);

        // 3) Обновляем индексы вершин в оставшихся полигонах (> vertexIndex уменьшаем на 1)
        List<Polygon> fixed = new ArrayList<>(model.getPolygons().size());
        for (Polygon p : model.getPolygons()) {
            List<Integer> newV = new ArrayList<>(p.vertexIndices().size());
            for (int vi : p.vertexIndices()) {
                newV.add(vi > vertexIndex ? vi - 1 : vi);
            }
            fixed.add(new Polygon(newV, p.texCoordIndices(), p.normalIndices()));
        }

        model.getPolygons().clear();
        model.getPolygons().addAll(fixed);
    }

    private static boolean usesVertex(Polygon p, int vertexIndex) {
        for (int idx : p.vertexIndices()) {
            if (idx == vertexIndex) return true;
        }
        return false;
    }
}

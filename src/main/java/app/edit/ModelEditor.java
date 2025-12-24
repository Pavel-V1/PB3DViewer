package app.edit;

import app.model.Model;
import app.model.Polygon;

import java.util.HashSet;
import java.util.Set;

public final class ModelEditor {

    private ModelEditor() {}

    public static void removePolygon(Model model, int polygonIndex) {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (polygonIndex < 0 || polygonIndex >= model.getPolygons().size()) {
            throw new IllegalArgumentException("polygonIndex out of range: " + polygonIndex);
        }
        model.getPolygons().remove(polygonIndex);
    }

    /**
     * Удаляет вершину и все полигоны, которые на неё ссылаются.
     * Индексы остальных вершин НЕ перенумеровываются (безопасно).
     */
    public static void removeVertexAndPolygons(Model model, int vertexIndex) {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (vertexIndex < 0 || vertexIndex >= model.getVertices().size()) {
            throw new IllegalArgumentException("vertexIndex out of range: " + vertexIndex);
        }

        model.getPolygons().removeIf(p -> usesVertex(p, vertexIndex));

        model.getVertices().set(vertexIndex, model.getVertices().get(vertexIndex));
    }

    private static boolean usesVertex(Polygon p, int vertexIndex) {
        for (int idx : p.vertexIndices()) {
            if (idx == vertexIndex) return true;
        }
        return false;
    }
}


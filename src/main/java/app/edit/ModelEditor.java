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

        model.getPolygons().removeIf(p -> usesVertex(p, vertexIndex));
        model.getVertices().remove(vertexIndex);
        
        List<Polygon> fixed = new ArrayList<>(model.getPolygons().size());
        for (Polygon p : model.getPolygons()) {
            List<Integer> newV = new ArrayList<>(p.vertexIndices().size());
            for (int vi : p.vertexIndices()) {
                if (vi > vertexIndex) newV.add(vi - 1);
                else newV.add(vi);
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

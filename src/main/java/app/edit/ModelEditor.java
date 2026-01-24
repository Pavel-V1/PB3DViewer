package app.edit;

import app.model.Model;
import app.model.Polygon;
import java.util.ArrayList;
import java.util.List;

public final class ModelEditor {//редактор модели

    private ModelEditor() {//чтобы нельзя было создать объект этого класса
    }

    public static void removePolygon(Model model, int polygonIndex) {//удаление одного полигона по индексу
        if (model == null)
            throw new IllegalArgumentException("model is null");
        if (polygonIndex < 0 || polygonIndex >= model.getPolygons().size()) {//если индекс вне диапозона
            throw new IllegalArgumentException("polygonIndex out of range: " + polygonIndex);
        }
        model.getPolygons().remove(polygonIndex);//удаляет полигон из списка
    }

    public static void removeVertexAndPolygons(Model model, int vertexIndex) {//удаление вершины
        if (model == null)
            throw new IllegalArgumentException("model is null");
        if (vertexIndex < 0 || vertexIndex >= model.getVertices().size()) {
            throw new IllegalArgumentException("vertexIndex out of range: " + vertexIndex);
        }

        model.getPolygons().removeIf(p -> usesVertex(p, vertexIndex));//удалить полигон, где есть вершина
        model.getVertices().remove(vertexIndex);//удалить саму вершину

        //исправляем индексы в оставшихся полигонах
        List<Polygon> fixed = new ArrayList<>(model.getPolygons().size());//новый список полигонов
        for (Polygon p : model.getPolygons()) {
            List<Integer> newV = new ArrayList<>(p.vertexIndices().size());//новый список исправленных индексы вершин
            for (int vi : p.vertexIndices()) {
                newV.add(vi > vertexIndex ? vi - 1 : vi);//если индекс вершины vi был справа от удалённой вершины он должен стать на 1 меньше
            }
            fixed.add(new Polygon(newV, p.texCoordIndices(), p.normalIndices()));//создаем новый полигон
        }
        model.getPolygons().clear();
        model.getPolygons().addAll(fixed);//заменяем старые полигоны на новые
    }

    private static boolean usesVertex(Polygon p, int vertexIndex) {//проверка есть ли вершина в полигоне
        for (int idx : p.vertexIndices()) {
            if (idx == vertexIndex) return true;
        }
        return false;
    }
}

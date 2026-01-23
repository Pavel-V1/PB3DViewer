package app.rend_prepare;

import app.model.Model;
import app.model.Normal;
import app.model.Polygon;

import java.util.List;

public class GetNewModel {

    public static Model getNewModel(Model model) {
        List<Polygon> polygons = ModelTriangulation.triangulate(model.getPolygons(), model.getVertices());
        List<Normal> normals = NormalsCalculating.calculateNormals(model.getPolygons(), model.getVertices());

        Model newModel = new Model();
        newModel.getTexCoords().addAll(model.getTexCoords());
        newModel.getVertices().addAll(model.getVertices());
        newModel.getNormals().addAll(normals);
        newModel.getPolygons().addAll(polygons);

        return newModel;
    }
}

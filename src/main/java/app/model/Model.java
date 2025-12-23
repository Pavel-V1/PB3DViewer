package app.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Polygon> polygons = new ArrayList<>();
    private final List<TexCoord> texCoords = new ArrayList<>();
    private final List<Normal> normals = new ArrayList<>();

    public List<Vertex> getVertices(){
        return vertices;
    }
    public List<Polygon> getPolygons(){
        return polygons;
    }
    public List<TexCoord> getTexCoords() {
        return texCoords;
    }

    public List<Normal> getNormals() {
        return normals;
    }
}

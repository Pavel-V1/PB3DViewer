package app.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Polygon> polygons = new ArrayList<>();

    public List<Vertex> getVertices(){
        return vertices;
    }
    public List<Polygon> getPolygons(){
        return polygons;
    }
}

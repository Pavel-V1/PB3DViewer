package app.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Face> faces = new ArrayList<>();

    public List<Vertex> getVertices(){
        return vertices;
    }
    public List<Face> getFaces(){
        return faces;
    }
}

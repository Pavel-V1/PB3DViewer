package app.obj;

import app.model.Polygon;
import app.model.Model;
import app.model.Vertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {

    public Model read(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);

        Model model = new Model();
        List<Vertex> vertices = model.getVertices();
        List<Polygon> polygons = model.getPolygons();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            if (line.startsWith("v ")) {
                vertices.add(parseVertex(line));
            } else if (line.startsWith("f ")) {
                polygons.add(parsePolygon(line));

            }
        }

        return model;
    }

    private Vertex parseVertex(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Некорректная вершина: " + line);
        }

        float x = Float.parseFloat(parts[1]);
        float y = Float.parseFloat(parts[2]);
        float z = Float.parseFloat(parts[3]);

        return new Vertex(x, y, z);
    }

    private Polygon parsePolygon(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Некорректный полигон: " + line);
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String token = parts[i];
            String indexStr = token.split("/")[0];
            indices.add(Integer.parseInt(indexStr) - 1);
        }

        return new Polygon(indices);
    }
}

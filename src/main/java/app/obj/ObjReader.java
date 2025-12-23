package app.obj;

import app.model.Polygon;
import app.model.Model;
import app.model.Vertex;
import app.model.Normal;
import app.model.TexCoord;

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
        List<TexCoord> texCoords = model.getTexCoords();
        List<Normal> normals = model.getNormals();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            if (line.startsWith("v ")) {
                vertices.add(parseVertex(line));
            } else if (line.startsWith("vt ")) {
                texCoords.add(parseTexCoord(line));
            } else if (line.startsWith("vn ")) {
                normals.add(parseNormal(line));
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

        List<Integer> v = new ArrayList<>();
        List<Integer> vt = new ArrayList<>();
        List<Integer> vn = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            String[] idx = parts[i].split("/");

            v.add(Integer.parseInt(idx[0]) - 1);

            if (idx.length > 1 && !idx[1].isEmpty()) {
                vt.add(Integer.parseInt(idx[1]) - 1);
            }

            if (idx.length > 2) {
                vn.add(Integer.parseInt(idx[2]) - 1);
            }
        }

        return new Polygon(v, vt, vn);
    }

    private TexCoord parseTexCoord(String line) {
        String[] p = line.split("\\s+");
        return new TexCoord(
                Float.parseFloat(p[1]),
                Float.parseFloat(p[2])
        );
    }

    private Normal parseNormal(String line) {
        String[] p = line.split("\\s+");
        return new Normal(
                Float.parseFloat(p[1]),
                Float.parseFloat(p[2]),
                Float.parseFloat(p[3])
        );
    }

}

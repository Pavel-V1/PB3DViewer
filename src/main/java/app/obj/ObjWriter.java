package app.obj;

import app.model.Model;
import app.model.Polygon;
import app.model.Vertex;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ObjWriter {

    public void write(Model model, Writer writer) throws IOException {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (writer == null) throw new IllegalArgumentException("writer is null");

        writeVertices(model.getVertices(), writer);
        writePolygons(model.getPolygons(), writer);
        writer.flush();
    }

    private void writeVertices(List<Vertex> vertices, Writer writer) throws IOException {
        for (Vertex v : vertices) {
            writer.write("v ");
            writer.write(Float.toString(v.x()));
            writer.write(" ");
            writer.write(Float.toString(v.y()));
            writer.write(" ");
            writer.write(Float.toString(v.z()));
            writer.write("\n");
        }
    }

    private void writePolygons(List<Polygon> polygons, Writer writer) throws IOException {
        for (Polygon p : polygons) {
            writer.write("f");
            for (Integer idx0 : p.vertexIndices()) {
                // В OBJ индексы начинаются с 1, а у нас в модели — с 0
                writer.write(" ");
                writer.write(Integer.toString(idx0 + 1));
            }
            writer.write("\n");
        }
    }
}


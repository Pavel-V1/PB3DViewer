package app.obj;

import app.model.Model;
import app.model.Normal;
import app.model.Polygon;
import app.model.TexCoord;
import app.model.Vertex;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ObjWriter {

    public void write(Model model, Writer writer) throws IOException {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (writer == null) throw new IllegalArgumentException("writer is null");

        writeVertices(model.getVertices(), writer);
        writeTexCoords(model.getTexCoords(), writer);
        writeNormals(model.getNormals(), writer);
        writePolygons(model.getPolygons(), writer);

        writer.flush();
    }

    private void writeVertices(List<Vertex> vertices, Writer writer) throws IOException {
        for (Vertex v : vertices) {
            writer.write("v " + v.x() + " " + v.y() + " " + v.z() + "\n");
        }
    }

    private void writeTexCoords(List<TexCoord> tex, Writer writer) throws IOException {
        for (TexCoord t : tex) {
            writer.write("vt " + t.u() + " " + t.v() + "\n");
        }
    }

    private void writeNormals(List<Normal> normals, Writer writer) throws IOException {
        for (Normal n : normals) {
            writer.write("vn " + n.x() + " " + n.y() + " " + n.z() + "\n");
        }
    }

    private void writePolygons(List<Polygon> polygons, Writer writer) throws IOException {
        for (Polygon p : polygons) {
            writer.write("f");

            boolean hasVT = p.texCoordIndices() != null && !p.texCoordIndices().isEmpty();
            boolean hasVN = p.normalIndices() != null && !p.normalIndices().isEmpty();

            for (int i = 0; i < p.vertexIndices().size(); i++) {
                int vIdx = p.vertexIndices().get(i) + 1;

                Integer vtIdx = null;
                Integer vnIdx = null;

                if (hasVT && i < p.texCoordIndices().size()) vtIdx = p.texCoordIndices().get(i) + 1;
                if (hasVN && i < p.normalIndices().size()) vnIdx = p.normalIndices().get(i) + 1;

                writer.write(" ");
                if (vtIdx != null && vnIdx != null) {
                    writer.write(vIdx + "/" + vtIdx + "/" + vnIdx);
                } else if (vtIdx != null) {
                    writer.write(vIdx + "/" + vtIdx);
                } else if (vnIdx != null) {
                    writer.write(vIdx + "//" + vnIdx);
                } else {
                    writer.write(Integer.toString(vIdx));
                }
            }

            writer.write("\n");
        }
    }
}



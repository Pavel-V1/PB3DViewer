package app.rasterization;

import app.math.Vector4;
import app.model.Model;
import app.model.Polygon;
import app.model.TexCoord;
import app.model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ModelRasterization {
    public static void rasterizeModel(Model model, List<Vector4> transformedVs, Graphics g, int w, int h, float s, int cx, int cy,
                                      float midX, float midY, Color color, Color color2, Color color3, boolean isTex, Image tex) {

        TriangleRasterizer tr = TriangleRasterizer.getTriangleRasterizer();

        float depth = 600f;

        for (Polygon poly : model.getPolygons()) {
            List<Integer> idx = poly.vertexIndices();

            Vector4 v1 = transformedVs.get(idx.get(0));
            Vector4 v2 = transformedVs.get(idx.get(1));
            Vector4 v3 = transformedVs.get(idx.get(2));

            Vertex screenV1 = project(v1, depth, s, cx, cy, midX, midY);
            Vertex screenV2 = project(v2, depth, s, cx, cy, midX, midY);
            Vertex screenV3 = project(v3, depth, s, cx, cy, midX, midY);

            if (!model.getTexCoords().isEmpty()) {
                TexCoord tc1 = model.getTexCoords().get(poly.texCoordIndices().get(0));
                TexCoord tc2 = model.getTexCoords().get(poly.texCoordIndices().get(1));
                TexCoord tc3 = model.getTexCoords().get(poly.texCoordIndices().get(2));
                tr.makeTriangle(1, screenV1, screenV2, screenV3, color, color2, color3, tc1, tc2, tc3);
            } else {
                isTex = false;
                tr.makeTriangle(1, screenV1, screenV2, screenV3, color, color2, color3);
            }

        }
        tr.paint(g, w, h, isTex, tex);
    }

    private static Vertex project(Vector4 v, float depth, float s, int cx, int cy, float midX, float midY) {
        float k = depth / (depth + v.z);
        float x = cx + (v.x - midX) * s * k;
        float y = cy - (v.y - midY) * s * k;
        return new Vertex(x, y, v.z);
    }
}

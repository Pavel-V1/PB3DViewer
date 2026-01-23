package app.edit;

import app.math.Matrix4;
import app.math.Vector4;
import app.model.Model;
import app.model.Vertex;
import app.scene.Transform;

import java.util.List;

public final class ModelTransformApplier {

    private ModelTransformApplier() {}

    public static Model copyWithAppliedTransform(Model src, Transform tr) {
        if (src == null) throw new IllegalArgumentException("src model is null");
        if (tr == null) throw new IllegalArgumentException("transform is null");

        Matrix4 modelMatrix =
                Matrix4.translation(tr.getTranslation().x, tr.getTranslation().y, tr.getTranslation().z)
                        .mul(Matrix4.rotationZ(tr.getRotationRad().z))
                        .mul(Matrix4.rotationY(tr.getRotationRad().y))
                        .mul(Matrix4.rotationX(tr.getRotationRad().x))
                        .mul(Matrix4.scale(tr.getScale().x, tr.getScale().y, tr.getScale().z));

        Model dst = new Model();

        List<Vertex> outVertices = dst.getVertices();
        for (Vertex v : src.getVertices()) {
            Vector4 p = new Vector4(v.x(), v.y(), v.z(), 1f);
            Vector4 tp = modelMatrix.mul(p);
            outVertices.add(new Vertex(tp.x, tp.y, tp.z));
        }

        dst.getPolygons().addAll(src.getPolygons());
        dst.getTexCoords().addAll(src.getTexCoords());
        dst.getNormals().addAll(src.getNormals());

        return dst;
    }
}

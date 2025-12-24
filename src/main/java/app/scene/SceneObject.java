package app.scene;

import app.model.Model;

import java.util.Objects;
import java.util.UUID;

public final class SceneObject {
    private final String id = UUID.randomUUID().toString();
    private String name;
    private final Model model;

    private final Transform transform = new Transform();

    public SceneObject(String name, Model model) {
        this.name = Objects.requireNonNullElse(name, "Model");
        this.model = Objects.requireNonNull(model);
    }

    public String id() { return id; }
    public String name() { return name; }
    public void setName(String name) { this.name = Objects.requireNonNullElse(name, "Model"); }
    public Model model() { return model; }

    public Transform transform() { return transform; }

    @Override
    public String toString() { return name; }
}


package app.scene;

import app.model.Model;
import java.util.Objects;
import java.util.UUID;

public final class SceneObject {//превращает модель в объект сцены
    private final String id = UUID.randomUUID().toString();//уникаьный айди
    private String name;
    private final Model model;
    private final Transform transform = new Transform();

    public SceneObject(String name, Model model) {//конструктор
        this.name = Objects.requireNonNullElse(name, "Model");
        this.model = Objects.requireNonNull(model);
    }

    public String id() {
        return id; }
    public String name() {
        return name; }
    public void setName(String name) {//если имени нет то будет Model, если есть, то сохранится
        this.name = Objects.requireNonNullElse(name, "Model"); }
    public Model model() {
        return model; }

    public Transform transform() {
        return transform; }

    @Override
    public String toString() {//для UI
        return name; }
}


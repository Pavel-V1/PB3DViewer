package app.scene;

import app.model.Model;

import java.io.File;
import java.util.Objects;

public final class SceneController {

    private final Scene scene;

    public SceneController(Scene scene) {
        this.scene = Objects.requireNonNull(scene);
    }

    public Scene getScene() {
        return scene;
    }

    public SceneObject addModel(String name, Model model) {
        SceneObject obj = new SceneObject(name, model);
        scene.add(obj);
        return obj;
    }

    public void setActiveIndex(int index) {
        scene.setActiveIndex(index);
    }

    public SceneObject getActive() {
        return scene.getActive();
    }

    public void removeActive() {
        scene.removeActive();
    }
}


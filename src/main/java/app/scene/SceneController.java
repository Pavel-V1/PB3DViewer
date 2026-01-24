package app.scene;
import app.model.Model;
import java.io.File;
import java.util.Objects;

public final class SceneController {//посредник между интерфейсом и сценой

    private final Scene scene;//ссылка на сцену, с которой работает контроллер(он работает только с одной сценой)

    public SceneController(Scene scene) {//конструктор
        this.scene = Objects.requireNonNull(scene);//контроллер без сцены сущ не может
    }

    public Scene getScene() {//возвращ сцену
        return scene;
    }

    public SceneObject addModel(String name, Model model) {//добавление модели в сцену
        SceneObject obj = new SceneObject(name, model);
        scene.add(obj);//добавл объект в сцену
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


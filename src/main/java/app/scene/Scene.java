package app.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Scene {//хранит список загруженных моделей, умеет добавлять/удал их
    private final List<SceneObject> objects = new ArrayList<>();//список всех загруженных моделей
    private int activeIndex = -1;//активной модели нет

    public List<SceneObject> getObjects() {
        return Collections.unmodifiableList(objects);//возвращ список, который нельзя менять
    }

    public SceneObject getActive() {//получить активную модель
        if (activeIndex < 0 || activeIndex >= objects.size())
            return null;
        return objects.get(activeIndex);
    }

    public int getActiveIndex() {//отдает текущ индекс
        return activeIndex;
    }

    public void setActiveIndex(int index) {//выбор активной модели
        if (index < -1 || index >= objects.size()) {
            throw new IllegalArgumentException("Active index out of range: " + index);
        }
        activeIndex = index;
    }

    public void add(SceneObject obj) {//добавление модели
        objects.add(obj);//добавл в список
        activeIndex = objects.size() - 1;//делаем активной последнюю
    }

    public void removeActive() {//удалить активную модель
        if (getActive() == null)
            return;
        objects.remove(activeIndex);
        if (objects.isEmpty()) activeIndex = -1;//если удалили послед модель, то активн нет
        else activeIndex = Math.min(activeIndex, objects.size() - 1);//если спис не пуст, то выбираем новактивную иодель
    }
}


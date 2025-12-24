package app.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Scene {
    private final List<SceneObject> objects = new ArrayList<>();
    private int activeIndex = -1;

    public List<SceneObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public SceneObject getActive() {
        if (activeIndex < 0 || activeIndex >= objects.size()) return null;
        return objects.get(activeIndex);
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int index) {
        if (index < -1 || index >= objects.size()) {
            throw new IllegalArgumentException("Active index out of range: " + index);
        }
        activeIndex = index;
    }

    public void add(SceneObject obj) {
        objects.add(obj);
        activeIndex = objects.size() - 1;
    }

    public void removeActive() {
        if (getActive() == null) return;
        objects.remove(activeIndex);
        if (objects.isEmpty()) activeIndex = -1;
        else activeIndex = Math.min(activeIndex, objects.size() - 1);
    }
}


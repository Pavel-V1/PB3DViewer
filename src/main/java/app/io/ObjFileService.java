package app.io;//читает через обжридер и сохранет через ритер

import app.model.Model;
import app.obj.ObjReader;
import app.obj.ObjWriter;
import java.io.*;

public final class ObjFileService {//final-не предполагается наследование

    public Model load(File file)//загрузить
            throws IOException {
        if (file == null)
            throw new IllegalArgumentException("file is null");
        try (FileReader fr = new FileReader(file)) {//открой этот файл и читай из него текст
            return new ObjReader().read(fr);
        }//FileReader читает текст из файла
    }

    public void save(Model model, File file)//сохранить
            throws IOException {
        if (model == null)
            throw new IllegalArgumentException("model is null");
        if (file == null)
            throw new IllegalArgumentException("file is null");
        try (FileWriter fw = new FileWriter(file)) {//открой этот файл чтобы писать в него текст
            new ObjWriter().write(model, fw);
        }
    }
}


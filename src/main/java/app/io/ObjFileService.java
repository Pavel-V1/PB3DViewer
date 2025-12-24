package app.io;

import app.model.Model;
import app.obj.ObjReader;
import app.obj.ObjWriter;

import java.io.*;

public final class ObjFileService {

    public Model load(File file) throws IOException {
        if (file == null) throw new IllegalArgumentException("file is null");
        try (FileReader fr = new FileReader(file)) {
            return new ObjReader().read(fr);
        }
    }

    public void save(Model model, File file) throws IOException {
        if (model == null) throw new IllegalArgumentException("model is null");
        if (file == null) throw new IllegalArgumentException("file is null");
        try (FileWriter fw = new FileWriter(file)) {
            new ObjWriter().write(model, fw);
        }
    }
}


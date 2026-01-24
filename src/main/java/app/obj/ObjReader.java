package app.obj;

import app.model.Polygon;
import app.model.Model;
import app.model.Vertex;
import app.model.Normal;
import app.model.TexCoord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ObjReader {//читает

    public Model read(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);//BufferedReader делает чтение быстрее и даёт удобный метод readLine()

        Model model = new Model();
        List<Vertex> vertices = model.getVertices();
        List<Polygon> polygons = model.getPolygons();
        List<TexCoord> texCoords = model.getTexCoords();
        List<Normal> normals = model.getNormals();

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();//убирает пробелы в начале/конце

            if (line.isEmpty() || line.startsWith("#")) {
                continue;//пропуск пустых строк и комментариев
            }

            if (line.startsWith("v ")) {//определение типа строки
                vertices.add(parseVertex(line));
            } else if (line.startsWith("vt ")) {
                texCoords.add(parseTexCoord(line));
            } else if (line.startsWith("vn ")) {
                normals.add(parseNormal(line));
            } else if (line.startsWith("f ")) {
                polygons.add(parsePolygon(line));
            }

        }

        return model;//возвращаем заполненную модель
    }

    private Vertex parseVertex(String line) {//берёт одну строку OBJфайла вида v x y z и превращает её в объект Vertex
        String[] parts = line.split("\\s+");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Некорректная вершина: " + line);
        }

        float x = Float.parseFloat(parts[1]);
        float y = Float.parseFloat(parts[2]);
        float z = Float.parseFloat(parts[3]);

        return new Vertex(x, y, z);
    }

    private Polygon parsePolygon(String line) {//Берёт строку грани f ... и превращает её в объект Polygon, где хранятся индексы вершин, текстур и нормалей
        String[] parts = line.split("\\s+");

        List<Integer> v = new ArrayList<>();
        List<Integer> vt = new ArrayList<>();
        List<Integer> vn = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {//цикл по вершинам граней
            String[] idx = parts[i].split("/", -1); // важно: -1 сохраняет пустые поля

            if (idx.length < 1 || idx[0].isEmpty()) {
                throw new IllegalArgumentException("Некорректная грань (нет индекса вершины): " + line);
            }
            v.add(Integer.parseInt(idx[0]) - 1);

            if (idx.length > 1 && !idx[1].isEmpty()) {
                vt.add(Integer.parseInt(idx[1]) - 1);
            }

            if (idx.length > 2 && !idx[2].isEmpty()) {
                vn.add(Integer.parseInt(idx[2]) - 1);
            }
        }

        return new Polygon(v, vt, vn);
    }


    private TexCoord parseTexCoord(String line) {//берёт строку vt u v и превращает её в объект TexCoord(u, v)
        String[] p = line.split("\\s+");
        return new TexCoord(
                Float.parseFloat(p[1]),
                Float.parseFloat(p[2])
        );
    }

    private Normal parseNormal(String line) {//берёт строку vn x y z и превращает её в объект Normal(x, y, z)
        String[] p = line.split("\\s+");
        return new Normal(
                Float.parseFloat(p[1]),
                Float.parseFloat(p[2]),
                Float.parseFloat(p[3])
        );
    }

}

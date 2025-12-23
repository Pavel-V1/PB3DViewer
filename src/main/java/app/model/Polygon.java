package app.model;

import java.util.List;

public record Polygon(List<Integer> vertexIndices,
                      List<Integer> texCoordIndices,
                      List<Integer> normalIndices)
{ }


package com.cgvsu.model;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureVertices = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();


    private List<Group> groups = new ArrayList<>();

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textureVertices, ArrayList<Vector3f> normals, ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public Model(Model model) {
        List<Vector3f> checkList = model.getVertices();
        if (checkList != null) {
            this.vertices.addAll(model.vertices);
        }
        List<Vector2f> checkList2 = model.getTextureVertices();
        if (checkList2 != null) {
            this.textureVertices.addAll(model.textureVertices);
        }

        checkList = model.getNormals();
        if (checkList != null) {
            this.normals.addAll(model.normals);
        }

        List<Polygon> checkListP = model.getPolygons();
        if (checkListP != null) {
            this.polygons.addAll(model.polygons);
        }
    }

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addTextureVertex(Vector2f textureVertex) {
        textureVertices.add(textureVertex);
    }

    public void addNormal(Vector3f normal) {
        normals.add(normal);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Polygon getFirstPolygon() {
        return polygons.get(0);
    }


    public int getVerticesSize() {
        return vertices.size();
    }

    public int getTextureVerticesSize() {
        return textureVertices.size();
    }

    public int getNormalsSize() {
        return normals.size();
    }

    public int getPolygonsSize() {
        return polygons.size();
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public List<Group> getGroups() {
        return groups;
    }
}

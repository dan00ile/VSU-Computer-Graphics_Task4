package com.cgvsu.model;

import com.cgvsu.math.vector.Vector3f;

import java.util.ArrayList;

public class ModelAxis {

    public static Model makeAxisModel(float length) {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(0, 0, 0));
        vertex.add(new Vector3f(length, 0, 0));
        vertex.add(new Vector3f(0, length, 0));
        vertex.add(new Vector3f(0, 0, length));

        ArrayList<Polygon> polygons = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            ArrayList<Integer> pv = new ArrayList<>();
            pv.add(0);
            pv.add(i);
            Polygon p = new Polygon();
            p.setVertexIndices(pv);
            polygons.add(p);
        }

        return new Model(vertex, new ArrayList<>(), new ArrayList<>(), polygons);
    }


    public static Model makeCenterModelAxis(Model model,float length) {
        Vector3f center = model.getCenter();
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(center);
        vertex.add(center.add(new Vector3f(length, 0, 0)));
        vertex.add(center.add(new Vector3f(0, length, 0)));
        vertex.add(center.add(new Vector3f(0, 0, length)));

        ArrayList<Polygon> polygons = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            ArrayList<Integer> pv = new ArrayList<>();
            pv.add(0);
            pv.add(i);
            Polygon p = new Polygon();
            p.setVertexIndices(pv);
            polygons.add(p);
        }

        return new Model(vertex, new ArrayList<>(), new ArrayList<>(), polygons);
    }
}

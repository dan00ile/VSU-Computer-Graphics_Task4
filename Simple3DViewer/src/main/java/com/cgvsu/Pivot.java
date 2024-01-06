package com.cgvsu;

import com.cgvsu.model.Model;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;

public class Pivot extends Group {
    private double mouseX, mouseY;

    public Pivot() {
        Sphere pivotSphere = new Sphere(10);
        pivotSphere.setMaterial(new javafx.scene.paint.PhongMaterial(Color.GREEN));

        Line xAxis = new Line(0, 0, 50, 0);
        Line yAxis = new Line(0, 0, 0, 50);
        Line zAxis = new Line(0, 0, 0, -50);

        xAxis.setStroke(Color.RED);
        yAxis.setStroke(Color.BLUE);
        zAxis.setStroke(Color.GREEN);

        getChildren().addAll(pivotSphere, xAxis, yAxis, zAxis);
    }

    public void attachToModel(Model model) {
        double centerX = model.getCenter().getX();
        double centerY = model.getCenter().getY();
        double centerZ = model.getCenter().getZ();

        setTranslateX(centerX);
        setTranslateY(centerY);
        setTranslateZ(centerZ);
    }

}

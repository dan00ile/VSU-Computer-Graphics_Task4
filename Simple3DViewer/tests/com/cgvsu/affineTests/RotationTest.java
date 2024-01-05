package com.cgvsu.affineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.cgvsu.affine.AffineBuilder.AffineBuilder;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Model;

import java.util.ArrayList;
import java.util.List;

public class RotationTest {
    @Test
    public void affineBuilderRotateTest1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byX(1000).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        double angle = Math.toRadians(1000);
        float x = -2.0f;
        float y = (float) (Math.cos(angle) + 0.1 * Math.sin(angle));
        float z =  (float) (- Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest2() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byY(99).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        double angle = Math.toRadians(99);
        float x = (float) (-2.0 * Math.cos(angle) + 0.1 * Math.sin(angle));
        float y = 1.0f;
        float z = (float) (2 * Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest3() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byZ(-36).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        double angle = Math.toRadians(-36);
        float x = (float) (-2.0 * Math.cos(angle) + Math.sin(angle));
        float y = (float) (2.0 * Math.sin(angle) + Math.cos(angle));
        float z = 0.1f;
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }
    @Test
    public void affineBuilderRotateTest4() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().XYZ(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(0.1f, 1.0f, 2.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest5() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().XZY(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-1.0f, 2.0f, -0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest6() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().YXZ(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(2.0f, -0.1f, -1.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest7() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().YZX(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(1.0f, 2.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest8() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().ZXY(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.1f, -1.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest9() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().ZYX(rotate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(0.1f, -1.0f, -2.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void affineBuilderRotateTest10() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 0.0f, 0.0f);
        Model resultModel1 = new AffineBuilder().rotate().ZYX(rotate).close().returnChangedModel(model);
        List<Vector3f> result1 = resultModel1.getVertices();
        Model resultModel2 = new AffineBuilder().rotate().YZX(rotate).close().returnChangedModel(model);
        List<Vector3f> result2 = resultModel2.getVertices();

        Assertions.assertEquals(result1.size(), result2.size());
        for (int i = 0; i < result1.size(); i++) {
            Assertions.assertEquals(result1, result2);
        }
    }

    @Test
    public void rotateWithoutChangingTest1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().XZY(new Vector3f(360.0f, 360.0f, 360.0f)).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void rotateWithoutChangingTest2() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().byX(100.0f).byX(-100.0f).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void rotateWithoutChangingTest3() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().byX(120).byX(120).XZY(new Vector3f(120, 0, 0)).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }
    @Test
    public void testRotateModelWithNullVector() {
        try {
            new AffineBuilder().rotate().XZY(null).close()
                    .changeModel(new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        } catch (Exception ex) {
            String expectedError = "Exception in affine transformation: Angles vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }
}

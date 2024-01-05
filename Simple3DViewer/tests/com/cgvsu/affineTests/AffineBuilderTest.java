package com.cgvsu.affineTests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.cgvsu.affine.AffineBuilder.AffineBuilder;
import com.cgvsu.affine.AffineExceptions;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Model;

import java.util.ArrayList;
import java.util.List;

public class AffineBuilderTest {
    @Test
    public void vertexChangingTest() {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model1 = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel1 = new AffineBuilder().scale().byVector(new Vector3f(3.0f, 2.0f, 1.0f))
                .close().returnChangedModel(model1);

        List<Vector3f> newVertexes = resultModel1.getVertices();

        Vector3f expected = new Vector3f(-6.0f, 2.0f, 0.1f);

        for (Vector3f vertNum : newVertexes) {
            Assertions.assertTrue(vertNum.equals(expected));
        }
    }

    @Test
    public void normalsChangingTest() {

        ArrayList<Vector3f> normal = new ArrayList<Vector3f>();
        normal.add(new Vector3f(-4.0f, 2.0f, 0.2f));

        Model model2 = new Model(new ArrayList<>(), new ArrayList<>(), normal, new ArrayList<>());

        Model resultModel2 = new AffineBuilder().scale().byVector(new Vector3f(3.0f, 2.0f, 1.0f))
                .close().returnChangedModel(model2);

        List<Vector3f> newNormals = resultModel2.getNormals();

        Vector3f expected = new Vector3f(-12.0f, 4.0f, 0.2f);

        for (Vector3f normNum : newNormals) {
            Assertions.assertTrue(normNum.equals(expected));
        }
    }

    @Test
    public void nullModelChangingTest() {
        Model model = null;
        try {
            Model result = new AffineBuilder().returnChangedModel(model);
        } catch (AffineExceptions ex) {
            String expectedError = "Exception in affine transformation: Model is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void emptyModelChangingTest() throws Exception {
        Model model = new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Model result = new AffineBuilder().scale().byVector(new Vector3f(1.0f, 0.0f, -16.7f)).close().rotate().byX(90).close()
                .translate().byVector(new Vector3f(0.0f, -11.0f, 0.4f)).close().returnChangedModel(model);

        Assertions.assertEquals(result.getVertices().size(), 0.0f);
        Assertions.assertEquals(result.getTextureVertices().size(), 0.0f);
        Assertions.assertEquals(result.getNormals().size(), 0.0f);
        Assertions.assertEquals(result.getPolygons().size(), 0.0f);
    }

    @Test
    public void emptyAffineBuilderReturnTest() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertexes1() {
        Model model = new Model(null, null, null, null);
        new AffineBuilder().changeModel(model);
        Assertions.assertNull(model.getVertices());
        Assertions.assertNull(model.getNormals());
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertexes2() {
        Model model = new Model(null, null, null, null);
        model = new AffineBuilder().returnChangedModel(model);
        Assertions.assertEquals(model.getVertices().size(), 0);
        Assertions.assertEquals(model.getNormals().size(), 0);
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertex() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(null);
        vertex.add(new Vector3f(0.0f, 0.0f, 0.0f));
        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        try {
            new AffineBuilder().changeModel(model);
        } catch (AffineExceptions ex) {
            String expectedError = "Exception in affine transformation: Dot in model is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void affinesWithoutChangingTest() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
        Model resultModel = affineBuilder.scale().byVector(scale).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }

        Vector3f translate = new Vector3f(0.0f, 0.0f, 0.0f);
        resultModel = affineBuilder.translate().byVector(translate).close().returnChangedModel(model);
        result = resultModel.getVertices();

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }

        resultModel = affineBuilder.rotate().XYZ(new Vector3f(0.0f, 360.0f, -42.3f))
                .byZ( 42.3f).close().returnChangedModel(model);
        result = resultModel.getVertices();

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(vector3f.equals(expected));
        }
    }

    @Test
    public void differentAffines1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder builder = new AffineBuilder();

        Model resultM1 = builder.scale().byX(2).close().
                rotate().byX(90).close().translate().byX(3).close().returnChangedModel(model);
        Model resultM2 = builder.translate().byX(3).close().scale().byX(2).close().
                rotate().byX(90).close().returnChangedModel(model);

        Vector3f result1 = resultM1.getVertices().get(0);
        Vector3f result2 = resultM2.getVertices().get(0);

        Assertions.assertFalse(result1.equals(result2));
    }
}

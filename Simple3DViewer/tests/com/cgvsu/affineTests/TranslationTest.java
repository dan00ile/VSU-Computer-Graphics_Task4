package com.cgvsu.affineTests;

import com.cgvsu.affine.AffineBuilder.AffineBuilder;
import com.cgvsu.affine.AffineMatrix;
import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TranslationTest {
    public final double DELTA = 10e-15;


    @Test
    public void affineBuilderTranslateTest1() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f translate = new Vector3f(2.0f, -0.133f, 0.0f);
        Model resultModel = new AffineBuilder().translate().byVector(translate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(0.0f, -0.133f, 0.1f);

        for (Vector3f v : result) {
            Assertions.assertTrue(v.equals(expected));
        }
    }

    @Test
    public void affineBuilderFewTranslateTest() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f translate1 = new Vector3f(2.0f, -0.13f, 0.0f);
        Model resultModel = new AffineBuilder().translate().byX(4.1f).byVector(translate1)
                .byZ(0.0f).byY(0.3f).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(4.1f, 0.17f, 0.1f);

        for (Vector3f v : result) {
            Assertions.assertTrue(v.equals(expected));
        }
    }

    @Test
    public void translateWithoutChangingTest() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Vector3f translate = new Vector3f(0.0f, 0.0f, 0.0f);
        Model resultModel = affineBuilder.translate().byVector(translate).close().returnChangedModel(model);
        List<Vector3f> result = resultModel.getVertices();

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        for (Vector3f v : result) {
            Assertions.assertTrue(v.equals(expected));
        }
    }

    @Test
    public void testTranslateModelWithNullVector() {
        try {
            new AffineBuilder().translate().byVector(null).close()
                    .changeModel(new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        } catch (NullPointerException ex) {
            String expectedError = "Exception in affine transformation: Vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void testTranslateMatrixWithNullVector() {
        try {
            AffineMatrix.translateMatrix(null);
        } catch (NullPointerException ex) {
            String expectedError = "Exception in affine transformation: Vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void testScaleMatrix() {
        Matrix4x4 matrix = AffineMatrix.translateMatrix(new Vector3f(5.5f, -10.0f, 3.0f));

        float[][] result = matrix.getArr();
        float[][] expectedResult = new float[][]{{1.0f, 0.0f, 0.0f, 5.5f},
                                                {0.0f, 1.0f, 0.0f, -10f},
                                                {0.0f, 0.0f, 1.0f, 3.0f},
                                                {0.0f, 0.0f, 0.0f, 1.0f}};

        int expectedLength = expectedResult.length;
        int expectedDepth = expectedResult[0].length;
        Assertions.assertEquals(result.length, expectedLength);
        Assertions.assertEquals(result[0].length, expectedDepth);

        for (int i = 0; i < expectedLength; i++) {
            for (int j = 0; j < expectedDepth; j++) {
                Assertions.assertEquals(expectedResult[i][j], result[i][j], DELTA);
            }
        }
    }
}
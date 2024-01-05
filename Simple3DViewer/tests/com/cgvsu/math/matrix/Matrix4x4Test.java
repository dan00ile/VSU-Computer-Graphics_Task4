package com.cgvsu.math.matrix;

import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector4f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Matrix4x4Test {
    @Test
    void zeroMatrix() {
        Matrix4x4 matrix1 = new Matrix4x4(
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0);
        Matrix4x4 matrix2 = new Matrix4x4();
        Assertions.assertTrue(matrix1.equals(matrix2));
    }

    @Test
    void identityMatrix() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
        Matrix4x4 matrix2 = Matrix4x4.identityMatrix();
        Assertions.assertTrue(matrix1.equals(matrix2));
    }

    @Test
    void add() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16);
        Matrix4x4 matrix2 = new Matrix4x4(
                4, 5, 3, 1,
                4, 5, 6, 9,
                0, 5, 5, 5,
                6, -7 ,8, 0);
        Matrix4x4 matrixResult = matrix1.add(matrix2);
        Matrix4x4 matrix = new Matrix4x4(
                5, 7, 6, 5,
                9, 11, 13, 17,
                9, 15, 16, 17,
                19, 7, 23, 16);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void sub() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16);
        Matrix4x4 matrix2 = new Matrix4x4(4, 5, 3, 1,
                4, 5, 6, 9,
                0, 5, 5, 5,
                6, -7 ,8, 0);
        Matrix4x4 matrixResult = matrix1.sub(matrix2);
        Matrix4x4 matrix = new Matrix4x4(
                -3, -3, 0, 3,
                1, 1, 1, -1,
                9, 5, 6, 7,
                7, 21, 7, 16);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void product() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16);
        Matrix4x4 matrix2 = new Matrix4x4(
                4, 5, 3, 1,
                4, 5, 6, 9,
                0, 5, 5, 5,
                6, -7 ,8, 0);
        Matrix4x4 matrixResult = matrix1.mulMatrix(matrix2);
        Matrix4x4 matrix = new Matrix4x4(
                36, 2, 62, 34,
                92, 34, 150, 94,
                148, 66, 238, 154,
                204, 98, 326, 214);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void product2() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16);
        Matrix4x4 matrix2 = new Matrix4x4(
                4, 5, 3, 1,
                4, 5, 6, 9,
                0, 5, 5, 5,
                6, -7 ,8, 0);
        Matrix4x4 matrixResult = matrix1.mulMatrix(matrix2).mulMatrix(matrix2).add(matrix2);
        Matrix4x4 matrix = new Matrix4x4(
                360, 267, 705, 365,
                1072, 727, 1988, 1157,
                1780, 1187, 3267, 1937,
                2498, 1635, 4550, 2716);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void productVector() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 16);
        Vector4f vector4f = new Vector4f(4, 4, 0, 6);
        Vector4f matrixResult = matrix1.mulVector(vector4f);
        Vector4f matrix = new Vector4f(36, 92, 148, 204);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void determinant() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 6, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 0);
        Assertions.assertEquals(-512, matrix1.determinantMatrix());
    }

    @Test
    void transposition() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 6, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 0);
        Matrix4x4 matrixTr = new Matrix4x4(
                1, 5, 9, 13,
                6, 6, 10, 14,
                3, 7, 11, 15,
                4, 8, 12, 0);
        Assertions.assertTrue(matrixTr.equals(matrix1.transposition()));
    }

    @Test
    void inverseMatrix() {
        Matrix4x4 matrix1 = new Matrix4x4(
                1, 2, 3, 4,
                2, 3, 1, 2,
                1, 1, 1, -1,
                1, 0, -2, -6);
        Matrix4x4 matrixIn = new Matrix4x4(
                22, -6, -26, 17,
                -17, 5, 20, -13,
                -1, 0, 2, -1,
                4, -1, -5, 3);
        Assertions.assertTrue(matrixIn.equals(matrix1.inverseMatrix()));
    }

    @Test
    void methodGauss() {
        Matrix4x4 matrix1 = new Matrix4x4(
                2, 5, 4, 1,
                1, 3, 2, 1,
                2, 10, 9, 7,
                3, 8, 9, 2);
        Vector4f vector4f = new Vector4f(
                20,
                11,
                40,
                37);

        Vector4f vectorAnswer = new Vector4f(1, 2, 2, 0);
        Vector4f vectorAnswer1 = matrix1.gaussMethod(vector4f);

        Assertions.assertEquals(vectorAnswer.getX(), vectorAnswer1.getX());
        Assertions.assertEquals(vectorAnswer.getY(), vectorAnswer1.getY());
        Assertions.assertEquals(vectorAnswer.getZ(), vectorAnswer1.getZ());
        Assertions.assertEquals(vectorAnswer.getW(), vectorAnswer1.getW());
    }

    @Test
    void methodGauss2() {
        Matrix4x4 matrix1 = new Matrix4x4(
                2, 5, 4, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0);
        Vector4f vector4f = new Vector4f(
                24,
                0,
                0,
                0);

        Vector4f vectorAnswer = new Vector4f(5, 2, 1, 0);
        Vector4f vectorAnswer1 = matrix1.gaussMethod(vector4f);

        Assertions.assertEquals(vectorAnswer.getX(), vectorAnswer1.getX());
        Assertions.assertEquals(vectorAnswer.getY(), vectorAnswer1.getY());
        Assertions.assertEquals(vectorAnswer.getZ(), vectorAnswer1.getZ());
        Assertions.assertEquals(vectorAnswer.getW(), vectorAnswer1.getW());
    }
}

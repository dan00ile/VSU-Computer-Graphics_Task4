package com.cgvsu.math.matrix;

import com.cgvsu.math.vector.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Matrix3x3Test {
    @Test
    void zeroMatrix() {
        Matrix3x3 matrix1 = new Matrix3x3(
                0, 0, 0,
                0, 0, 0,
                0, 0, 0);
        Matrix3x3 matrix2 = new Matrix3x3();
        Assertions.assertTrue(matrix1.equals(matrix2));
    }

    @Test
    void identityMatrix() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 0, 0,
                0, 1, 0,
                0, 0, 1);
        Matrix3x3 matrix2 = Matrix3x3.identityMatrix();
        Assertions.assertTrue(matrix1.equals(matrix2));
    }

    @Test
    void add() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);
        Matrix3x3 matrix2 = new Matrix3x3(
                4, 5, 3,
                1, 4, 5,
                6, 9, 0);
        Matrix3x3 matrixResult = matrix1.add(matrix2);
        Matrix3x3 matrix = new Matrix3x3(
                5, 7, 6,
                5, 9, 11,
                13, 17, 9);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void sub() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9);
        Matrix3x3 matrix2 = new Matrix3x3(
                4, 5, 3,
                1, 4, 5,
                6, 9, 0);
        Matrix3x3 matrixResult = matrix1.sub(matrix2);
        Matrix3x3 matrix = new Matrix3x3(
                -3, -3, 0,
                3, 1, 1,
                1, -1, 9);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void mul() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 4, 3,
                2, 1, 5,
                3, 2, 1);
        Matrix3x3 matrix2 = new Matrix3x3(
                5, 2, 1,
                4, 3, 2,
                2, 1, 5);
        Matrix3x3 matrixResult = matrix2.mulMatrix(matrix1);
        Matrix3x3 matrix = new Matrix3x3(
                12, 24, 26,
                16, 23, 29,
                19, 19, 16);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void mulX() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 4, 3,
                2, 1, 5,
                3, 2, 1);
        Matrix3x3 matrix2 = new Matrix3x3(
                5, 2, 1,
                4, 3, 2,
                2, 1, 5);
        Matrix3x3 matrixResult = matrix2.mulMatrix(matrix1).mulMatrix(matrix1).add(matrix1);
        Matrix3x3 matrix = new Matrix3x3(
                139,  128, 185,
                151, 146, 197,
                108, 129, 169);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void mulVector() {
        Matrix3x3 matrix1 = new Matrix3x3(
                2, 4, 0,
                -2, 1, 3,
                -1, 0, 1);
        Vector3f vector3f = new Vector3f(1, 2, -1);
        Vector3f matrixResult = matrix1.mulVector(vector3f);
        Vector3f matrix = new Vector3f(10, -3, -2);
        Assertions.assertTrue(matrix.equals(matrixResult));
    }

    @Test
    void determinant() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 4, 3,
                2, 1, 5,
                3, 2, 1);
        Assertions.assertEquals(46, matrix1.determinantMatrix());
    }

    @Test
    void transposition() {
        Matrix3x3 matrix1 = new Matrix3x3(
                1, 4, 3,
                2, 1, 5,
                3, 2, 1);
        Matrix3x3 matrixTr = new Matrix3x3(
                1, 2, 3,
                4, 1, 2,
                3, 5, 1);
        Assertions.assertTrue(matrixTr.equals(matrix1.transposition()));
    }

    @Test
    void inverseMatrix() {
        Matrix3x3 matrix1 = new Matrix3x3(
                2, 5, 7,
                6, 3, 4,
                5, -2, -3);
        Matrix3x3 matrixIn = new Matrix3x3(
                1, -1, 1,
                -38, 41, -34,
                27, -29, 24);
        Assertions.assertTrue(matrixIn.equals(matrix1.inverseMatrix()));
    }

    @Test
    void methodGauss() {
        Matrix3x3 matrix1 = new Matrix3x3(
                3, 2, -5,
                2, -1, 3,
                1, 2, -1);
        Vector3f vector3f = new Vector3f(-1, 13, 9);
        Vector3f vectorAnswer = new Vector3f(3, 5, 4);
        Vector3f vectorAnswer1 = matrix1.gaussMethod(vector3f);

        Assertions.assertEquals(vectorAnswer.getX(), vectorAnswer1.getX());
        Assertions.assertEquals(vectorAnswer.getY(), vectorAnswer1.getY());
        Assertions.assertEquals(vectorAnswer.getZ(), vectorAnswer1.getZ());
    }

    @Test
    void methodGauss2() {
        Matrix3x3 matrix1 = new Matrix3x3(
                3, 2, -5,
                0, 0, 0,
                0, 0, 0);
        Vector3f vector3f = new Vector3f(-1, 0, 0);
        Vector3f vectorAnswer = new Vector3f(-1, 1, 0);
        Vector3f vectorAnswer1 = matrix1.gaussMethod(vector3f);

        Assertions.assertEquals(vectorAnswer.getX(), vectorAnswer1.getX());
        Assertions.assertEquals(vectorAnswer.getY(), vectorAnswer1.getY());
        Assertions.assertEquals(vectorAnswer.getZ(), vectorAnswer1.getZ());
    }

    @Test
    void methodGauss_freeVariable() {
        Matrix3x3 matrix1 = new Matrix3x3(
                3, 2, -5,
                2, -1, 3,
                6, 4, -10);
        Vector3f vector3f = new Vector3f(
                10,
                2,
                4);

        Vector3f vectorAnswer = new Vector3f(2, 2, 0);
        Vector3f vectorAnswer1 = matrix1.gaussMethod(vector3f);

        Assertions.assertEquals(vectorAnswer.getX(), vectorAnswer1.getX());
        Assertions.assertEquals(vectorAnswer.getY(), vectorAnswer1.getY());
        Assertions.assertEquals(vectorAnswer.getZ(), vectorAnswer1.getZ());
    }
}

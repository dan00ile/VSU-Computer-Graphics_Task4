package com.cgvsu.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector3fTest {
    @Test
    void add_vector() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        Vector3f v2 = new Vector3f(4, 8, 7);
        Vector3f v = new Vector3f(5, 10, 12);
        Assertions.assertTrue(v.equals(v1.add(v2)));
    }

    @Test
    void add_params() {
        Vector3f v1 = new Vector3f(1, 2, 8);
        float x = 3;
        float y = 7;
        float z = 2;
        Vector3f v = new Vector3f(4, 9, 10);
        Assertions.assertTrue(v.equals(v1.add(x, y, z)));
    }

    @Test
    void sub_vector() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        Vector3f v2 = new Vector3f(4, 8, 7);
        Vector3f v = new Vector3f(-3, -6, -2);
        Assertions.assertTrue(v.equals(v1.sub(v2)));
    }

    @Test
    void sub_params() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        float x = 3;
        float y = 7;
        float z = 8;
        Vector3f v = new Vector3f(-2, -5, -3);
        Assertions.assertTrue(v.equals(v1.sub(x, y, z)));
    }

    @Test
    void mul() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        float scalar = 3;
        Vector3f v = new Vector3f(3, 6, 15);
        Assertions.assertTrue(v.equals(v1.mul(scalar)));
    }

    @Test
    void mulX() {
        Vector3f v1 = new Vector3f(0.1f, 0.2f, 0.3f);
        Vector3f v2 = v1.mul(3).mul(3).add(new Vector3f(0.1f, 0.2f, 0.3f));
        Vector3f v = new Vector3f(1, 2, 3);
        Assertions.assertTrue(v.equals(v2));
    }

    @Test
    void div_scalarNotEqualsZero() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        float scalar = 4;
        Vector3f v = new Vector3f(0.25f, 0.5f, 1.25f);
        Assertions.assertTrue(v.equals(v1.div(scalar)));
    }

    @Test
    void div_scalarNotEqualsZero2() {
        Vector3f v1 = new Vector3f(1, 2, 5);
        float scalar = -4;
        Vector3f v = new Vector3f(-0.25f, -0.5f, -1.25f);
        Assertions.assertTrue(v.equals(v1.div(scalar)));
    }

    @Test
    void div_scalarEqualsZero() {
        Vector3f v = new Vector3f(1, 2, 5);
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class, () -> v.div(0));
        Assertions.assertTrue(exception.getMessage().contains("You cant divide on 0"));
    }

    @Test
    void length() {
        Vector3f v1 = new Vector3f(6, 8, 3);
        Assertions.assertEquals((float) Math.sqrt(36 + 64 + 9), v1.length());
    }

    @Test
    void normalization_vectorLengthNotEqualsZero() {
        Vector3f v1 = new Vector3f(6, 8, 3);
        Vector3f v = new Vector3f((float) (6 / Math.sqrt(36 + 64 + 9)), (float) (8 / Math.sqrt(36 + 64 + 9)),
                (float) (3 / Math.sqrt(36 + 64 + 9)));
        Assertions.assertTrue(v.equals(v1.normalization()));
    }

    @Test
    void normalization_vectorLengthEqualsZero() {
        Vector3f v1 = new Vector3f(0, 0, 0);
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class, v1::normalization);
        Assertions.assertTrue(exception.getMessage().contains("This vector cannot be normalized because its length is zero"));
    }

    @Test
    void dotProduct() {
        Vector3f v1 = new Vector3f(1, 2, -4);
        Vector3f v2 = new Vector3f(6, -1, 2);
        Assertions.assertEquals(-4, v1.dotProduct(v2));
    }

    @Test
    void vectorProduct() {
        Vector3f v1 = new Vector3f(-1, 2, -3);
        Vector3f v2 = new Vector3f(0, -4, 1);
        Vector3f v = new Vector3f(-10, 1, 4);
        Assertions.assertTrue(v.equals(v1.vectorProduct(v2)));
    }
}

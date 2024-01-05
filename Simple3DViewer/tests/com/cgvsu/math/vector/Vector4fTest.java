package com.cgvsu.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector4fTest {
    @Test
    void add_vector() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        Vector4f v2 = new Vector4f(4, 8, 7, -1);
        Vector4f v = new Vector4f(5, 10, 12, 3);
        Assertions.assertTrue(v.equals(v1.add(v2)));
    }

    @Test
    void add_params() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        float x = 3;
        float y = 7;
        float z = 2;
        float w = -9;
        Vector4f v = new Vector4f(4, 9, 7, -5);
        Assertions.assertTrue(v.equals(v1.add(x, y, z, w)));
    }

    @Test
    void sub_vector() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        Vector4f v2 = new Vector4f(4, 8, 7, -1);
        Vector4f v = new Vector4f(-3, -6, -2, 5);
        Assertions.assertTrue(v.equals(v1.sub(v2)));
    }

    @Test
    void sub_params() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        float x = 4;
        float y = 8;
        float z = 7;
        float w = -1;
        Vector4f v = new Vector4f(-3, -6, -2, 5);
        Assertions.assertTrue(v.equals(v1.sub(x, y, z, w)));
    }

    @Test
    void mul() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        float scalar = 3;
        Vector4f v = new Vector4f(3, 6, 15, 12);
        Assertions.assertTrue(v.equals(v1.mul(scalar)));
    }

    @Test
    void mulX() {
        Vector4f v1 = new Vector4f(0.1f, 0.2f, 0.3f, -0.4f);
        Vector4f v2 = v1.mul(3).mul(3).add(new Vector4f(0.1f, 0.2f, 0.3f, -0.4f));
        Vector4f v = new Vector4f(1, 2, 3, -4);
        Assertions.assertTrue(v.equals(v2));
    }

    @Test
    void div_scalarNotEqualsZero2() {
        Vector4f v1 = new Vector4f(1, 2, 5, 8);
        float scalar = -4;
        Vector4f v = new Vector4f(-0.25f, -0.5f, -1.25f, -2);
        Assertions.assertTrue(v.equals(v1.div(scalar)));
    }

    @Test
    void div_scalarNotEqualsZero() {
        Vector4f v1 = new Vector4f(3, 6, 15, 12);
        float scalar = 3;
        Vector4f v = new Vector4f(1, 2, 5, 4);
        Assertions.assertTrue(v.equals(v1.div(scalar)));
    }

    @Test
    void div_scalarEqualsZero() {
        Vector4f v = new Vector4f(3, 6, 15, 12);
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class, () -> v.div(0));
        Assertions.assertTrue(exception.getMessage().contains("You cant divide on 0"));
    }

    @Test
    void length() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        Assertions.assertEquals((float) Math.sqrt(1 + 4 + 25 + 16), v1.length());
    }

    @Test
    void normalization_vectorLengthNotEqualsZero() {
        Vector4f v1 = new Vector4f(1, 2, 5, 4);
        Vector4f v = new Vector4f((float) (1 / Math.sqrt(1 + 4 + 25 + 16)), (float) (2 / Math.sqrt(1 + 4 + 25 + 16)),
                (float) (5 / Math.sqrt(1 + 4 + 25 + 16)), (float) (4 / Math.sqrt(1 + 4 + 25 + 16)));
        Assertions.assertTrue(v.equals(v1.normalization()));
    }

    @Test
    void normalization_vectorLengthEqualsZero() {
        Vector4f v1 = new Vector4f(0, 0, 0, 0);
        ArithmeticException exception = Assertions.assertThrows(ArithmeticException.class, v1::normalization);
        Assertions.assertTrue(exception.getMessage().contains("This vector cannot be normalized because its length is zero"));
    }

    @Test
    void dotProduct() {
        Vector4f v1 = new Vector4f(1, 2, -4, 10);
        Vector4f v2 = new Vector4f(6, -1, 2, 3);
        Assertions.assertEquals(26, v1.dotProduct(v2));
    }
}

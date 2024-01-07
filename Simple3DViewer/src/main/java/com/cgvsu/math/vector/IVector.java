package com.cgvsu.math.vector;

public interface IVector<T extends IVector<T>> {
    T add(T vector);

    T sub(T vector);

    T mul(float scalar);

    T div(float scalar);

    float length();

    T normalize();

    float dotProduct(T vector);

    boolean equals(T vector);

    String toString();

    float[] getVector();
}

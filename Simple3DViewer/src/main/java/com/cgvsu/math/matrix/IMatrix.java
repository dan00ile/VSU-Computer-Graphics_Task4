package com.cgvsu.math.matrix;

import com.cgvsu.math.vector.IVector;

public interface IMatrix<T extends IMatrix<T, V>, V extends IVector<V>> {
    float getValue(int i, int j);

    void setValue(int i, int j, float value);

    boolean equals(T matrix);

    T add(T matrix);

    T sub(T matrix);

    T mulMatrix(T matrix);

    float determinantMatrix();

    T transposition();

    T inverseMatrix();

    V mulVector(V vector);

    V gaussMethod(V vector);
}

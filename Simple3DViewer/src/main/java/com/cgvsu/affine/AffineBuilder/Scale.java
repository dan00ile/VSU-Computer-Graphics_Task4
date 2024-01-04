package com.cgvsu.affine.AffineBuilder;

import com.cgvsu.affine.AffineMatrix;
import com.cgvsu.affine.AxisEnum;
import com.cgvsu.math.vector.Vector3f;

public class Scale {
    AffineBuilder builder;

    public Scale(AffineBuilder builder) {
        this.builder = builder;
    }

    public Scale byVector(Vector3f scaleVector) {
        builder.addScale(AffineMatrix.scaleMatrix(scaleVector));

        return this;
    }

    public Scale byAxis(AxisEnum axisEnum, float num) {
        switch (axisEnum) {
            case X -> byX(num);
            case Y -> byY(num);
            case Z -> byZ(num);
        }

        return this;
    }
    public Scale byX(float num) {
        byVector(new Vector3f(num, 1, 1));
        return this;
    }


    public Scale byY(float num) {
        byVector(new Vector3f(1, num, 1));
        return this;
    }


    public Scale byZ(float num) {
        byVector(new Vector3f(1, 1, num));
        return this;
    }

    public AffineBuilder close() {
        return builder;
    }
}

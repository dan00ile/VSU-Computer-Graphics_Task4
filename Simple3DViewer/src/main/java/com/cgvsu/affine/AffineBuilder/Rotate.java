package com.cgvsu.affine.AffineBuilder;

import com.cgvsu.affine.AffineExceptions;
import com.cgvsu.affine.AffineMatrix;
import com.cgvsu.affine.AxisEnum;
import com.cgvsu.math.vector.Vector3f;

public class Rotate {
    public enum RotateWayEnum{
        XYZ("XYZ"),
        XZY("XZY"),
        YXZ("YXZ"),
        YZX("YZX"),
        ZXY("ZXY"),
        ZYX("ZYX");

        String way;
        RotateWayEnum(String way) {
            this.way = way;
        }

        public String getWay() {
            return way;
        }
    }
    AffineBuilder builder;
    public Rotate(AffineBuilder builder) {
        this.builder = builder;
    }

    public void byAxisInRadians(AxisEnum axisEnum, double angle) throws Exception {
        builder.addRotate(AffineMatrix.rotateMatrix(axisEnum, angle));
    }


    public void byAxisInDegrees(AxisEnum axisEnum, double angle) throws Exception {
        byAxisInRadians(axisEnum, Math.toRadians(angle));
    }

    private void rotate(AxisEnum[] enums, Vector3f angles) throws Exception {
        if (angles == null) {
            throw new AffineExceptions("Angles vector is null");
        }
        for (int i = 0; i < enums.length; i++) {
            AxisEnum e = enums[i];
            float angle = 0;
            if (i == 0) {
                angle = angles.getX();
            } else if (i == 1) {
                angle = angles.getY();
            } else if (i == 2) {
                angle = angles.getZ();
            }
            switch (e) {
                case X -> byX(angle);
                case Y -> byY(angle);
                case Z -> byZ(angle);
            }
        }
    }

    public Rotate byX(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.X, angle);
        return this;
    }

    public Rotate byY(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.Y, angle);
        return this;
    }

    public Rotate byZ(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.Z, angle);
        return this;
    }

    public Rotate inOrder(RotateWayEnum way, Vector3f angle) throws Exception {
        switch (way) {
            case XYZ -> {
                return XYZ(angle);
            }
            case XZY -> {
                return XZY(angle);
            }
            case YXZ -> {
                return YXZ(angle);
            }
            case YZX -> {
                return YZX(angle);
            }
            case ZXY -> {
                return ZXY(angle);
            }
            case ZYX -> {
                return ZYX(angle);
            }
            default -> throw new AffineExceptions("Wrong way to rotate");
        }
    }
    public Rotate XYZ(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.X, AxisEnum.Y, AxisEnum.Z};
        rotate(enums, angle);
        return this;
    }

    public Rotate XZY(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.X, AxisEnum.Z, AxisEnum.Y};
        rotate(enums, angle);
        return this;
    }

    public Rotate YXZ(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Y, AxisEnum.X, AxisEnum.Z};
        rotate(enums, angle);
        return this;
    }
    public Rotate YZX(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Y, AxisEnum.Z, AxisEnum.X};
        rotate(enums, angle);
        return this;
    }

    public Rotate ZXY(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Z, AxisEnum.X, AxisEnum.Y};
        rotate(enums, angle);
        return this;
    }

    public Rotate ZYX(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Z, AxisEnum.Y, AxisEnum.X};
        rotate(enums, angle);
        return this;
    }

    public AffineBuilder close() {
        return builder;
    }


}

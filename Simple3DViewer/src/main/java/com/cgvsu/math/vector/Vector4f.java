package com.cgvsu.math.vector;

public class Vector4f implements IVector<Vector4f>{
    public float x;
    public float y;
    public float z;
    public float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(float[] arr) {
        if (arr.length != 4) {
            throw new ArithmeticException("Wrong array length to create vector");
        }
        this.x = arr[0];
        this.y = arr[1];
        this.z = arr[2];
        this.w = arr[3];
    }

    public Vector4f(Vector3f vector3F) {
        this.x = vector3F.getX();
        this.y = vector3F.getY();
        this.z = vector3F.getZ();
        this.w = 1;
    }

    /*
     * Получение вектора в виде массива столбца
     */
    @Override
    public float[][] getVector() {
        return new float[][]{{x}, {y}, {z}, {w}};
    }

    /*
     * Получение значения "X"
     */
    public float getX() {
        return x;
    }

    /*
     * Получение значения "Y"
     */
    public float getY() {
        return y;
    }

    /*
     * Получение значения "Z"
     */
    public float getZ() {
        return z;
    }

    /*
     * Получение значения "W"
     */
    public float getW() {
        return w;
    }

    /*
     * Сложение векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector4f add(Vector4f v) {
        return new Vector4f(this.x + v.x, this.y + v.y, this.z + v.z, this.w + v.w);
    }

    /*
     * Сложение векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    public Vector4f add(float x, float y, float z, float w) {
        return new Vector4f(this.x + x, this.y + y, this.z + z, this.w + w);
    }

    /*
     * Разница векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector4f sub(Vector4f v) {
        return new Vector4f(this.x - v.x, this.y - v.y, this.z - v.z, this.w - v.w);
    }

    /*
     * Разница векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    public Vector4f sub(float x, float y, float z, float w) {
        return new Vector4f(this.x - x, this.y - y, this.z - z, this.w - w);
    }

    /*
     * Произведение вектора на число
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector4f mul(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        float z = this.z * scalar;
        float w = this.w * scalar;
        return new Vector4f(x, y, z, w);
    }

    /*
     * Деление вектора на число
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector4f div(float scalar) {
        if (Math.abs(scalar) < 1e-14) {
            throw new ArithmeticException("You cant divide on 0");
        }
        float x = this.x / scalar;
        float y = this.y / scalar;
        float z = this.z / scalar;
        float w = this.w / scalar;
        return new Vector4f(x, y, z, w);
    }

    /*
     * Получение длины вектора
     */
    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    /*
     * Нормализация вектора
     */
    @Override
    public Vector4f normalization() {
        float len = this.length();
        if (Math.abs(len) < 1e-14) {
            throw new ArithmeticException("This vector cannot be normalized because its length is zero");
        }
        return this.div(len);
    }

    /*
     * Скалярное произведение векторов
     */
    @Override
    public float dotProduct(Vector4f v) {
        return this.x * v.x + this.y * v.y + this.z * v.z + this.w * v.w;
    }

    /*
     * Скалярное произведение векторов
     */
    @Override
    public boolean equals(Vector4f v) {
        return Math.abs(this.x - v.x) < 1e-14 && Math.abs(this.y - v.y) < 1e-14 &&
                Math.abs(this.z - v.z) < 1e-14 && Math.abs(this.w - v.w) < 1e-14;
    }

    @Override
    public String toString() {
        return "Vector4f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}

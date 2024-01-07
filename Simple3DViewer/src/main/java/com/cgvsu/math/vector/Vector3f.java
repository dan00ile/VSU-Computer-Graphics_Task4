package com.cgvsu.math.vector;

public class Vector3f implements IVector<Vector3f>{
    private final float x;
    private final float y;
    private final float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector4f vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }


    public Vector3f(float[] arr) {
        if (arr.length != 3) {
            throw new ArithmeticException("Wrong array length to create vector");
        }
        this.x = arr[0];
        this.y = arr[1];
        this.z = arr[2];
    }

    /*
     * Получение вектора в виде массива столбца
     */
    @Override
    public float[] getVector() {
        return new float[]{x, y, z};
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
     * Сложение векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector3f add(Vector3f v) {
        return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    /*
     * Сложение векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    public Vector3f add(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    /*
     * Разница векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector3f sub(Vector3f v) {
        return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    /*
     * Разница векторов
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    public Vector3f sub(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    /*
     * Произведение вектора на число
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector3f mul(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        float z = this.z * scalar;
        return new Vector3f(x, y, z);
    }

    /*
     * Деление вектора на число
     * Исходный вектор не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector3f div(float scalar) {
        if (Math.abs(scalar) < 1e-14) {
            throw new ArithmeticException("You cant divide on 0");
        }
        float x = this.x / scalar;
        float y = this.y / scalar;
        float z = this.z / scalar;
        return new Vector3f(x, y, z);
    }

    /*
     * Получение длины вектора
     */
    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    /*
     * Нормализация вектора
     */
    @Override
    public Vector3f normalize() {
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
    public float dotProduct(Vector3f v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    /*
     * Векторное произведение
     */
    public Vector3f vectorProduct(Vector3f v) {
        float x = this.y * v.z - this.z * v.y;
        float y = this.z * v.x - this.x * v.z;
        float z = this.x * v.y - this.y * v.x;
        return new Vector3f(x, y, z);
    }


    /*
     * Сравнение векторов
     */
    @Override
    public boolean equals(Vector3f v) {
        double e = 1e-7;
        return Math.abs(this.x - v.x) < e && Math.abs(this.y - v.y) < e &&
                Math.abs(this.z - v.z) < e;
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public String coordstoStringSplitBySpace() {
        return x+" "+y+" "+z;
    }
}

package com.cgvsu.math.matrix;

import com.cgvsu.math.vector.Vector3f;

public class Matrix3x3 implements IMatrix<Matrix3x3, Vector3f> {
    private final float[][] matrix = new float[3][3];

    public Matrix3x3(
            float a11, float a12, float a13,
            float a21, float a22, float a23,
            float a31, float a32, float a33) {
        matrix[0][0] = a11;
        matrix[0][1] = a12;
        matrix[0][2] = a13;
        matrix[1][0] = a21;
        matrix[1][1] = a22;
        matrix[1][2] = a23;
        matrix[2][0] = a31;
        matrix[2][1] = a32;
        matrix[2][2] = a33;
    }

    public Matrix3x3(float[] matrix) {
        if (matrix.length != 9) {
            throw new ArithmeticException("Wrong array length to create matrix. The length should be 9");
        }
        this.matrix[0][0] = matrix[0];
        this.matrix[0][1] = matrix[1];
        this.matrix[0][2] = matrix[2];
        this.matrix[1][0] = matrix[3];
        this.matrix[1][1] = matrix[4];
        this.matrix[1][2] = matrix[5];
        this.matrix[2][0] = matrix[6];
        this.matrix[2][1] = matrix[7];
        this.matrix[2][2] = matrix[8];
    }

    public Matrix3x3(float[][] matrix) {
        if (matrix.length != 3) {
            for (int i = 0; i < 3; i++) {
                if (matrix[i].length != 3) {
                    throw new ArithmeticException("Wrong array length to create matrix. The length should be 3x3");
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, 3);
        }
    }

    /*
     * Создание нулевой матрицы (все значения равны 0)
     */
    public Matrix3x3 () {
        float[] arr = new float[9];
        new Matrix3x3(arr);
    }

    /*
     * Получение элемента матрицы по номеру строки(i) и столбца(j)
     */
    @Override
    public float getValue(int i, int j) {
        if (i > 2 || i < 0) {
            throw new ArithmeticException("wrong value for 'i'");
        }
        if (j > 2 || j < 0) {
            throw new ArithmeticException("wrong value for 'j'");
        }
        return matrix[i][j];
    }

    /*
     * Замена элемента матрицы на новый(value) по номеру строки(i) и столбца(j)
     */
    @Override
    public void setValue(int i, int j, float value) {
        matrix[i][j] = value;
    }

    private float[][] getArr() {
        return matrix;
    }

    /*
     * Сравнение исходной матрицы с новой(matrix)
     */
    @Override
    public boolean equals(Matrix3x3 matrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Math.abs(this.getValue(i, j) - matrix.getValue(i, j)) > 1e-14) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Создание единичной матрицы
     */
    public static Matrix3x3 identityMatrix() {
        float[][] arr = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    arr[i][j] = 1;
                }
            }
        }
        return new Matrix3x3(arr);
    }

    /*
     * Сложение матриц
     * Исходная матрица изменяется
     */
    @Override
    public Matrix3x3 add(Matrix3x3 matrix) {
        Matrix3x3 matrix3x3 = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float value = this.matrix[i][j] + matrix.getValue(i, j);
                matrix3x3.setValue(i, j, value);
            }
        }
        return matrix3x3;
    }

    /*
     * Вычитание матрицы из исходной
     * Исходная матрица изменяется
     */
    @Override
    public Matrix3x3 sub(Matrix3x3 matrix) {
        Matrix3x3 matrix3x3 = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float value = this.matrix[i][j] - matrix.getValue(i, j);
                matrix3x3.setValue(i, j, value);
            }
        }
        return matrix3x3;
    }

    /*
     * Произведение матриц
     * Исходная матрица не изменяется
     * Возвращается новая матрица
     */
    @Override
    public Matrix3x3 mulMatrix(Matrix3x3 matrix) {
        Matrix3x3 arrResult = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    float value = arrResult.getValue(i, j);
                    value += this.getValue(i, k) * matrix.getValue(k, j);
                    arrResult.setValue(i, j, value);
                }
            }
        }
        return arrResult;
    }

    /*
     * Произведение матрицы на вектор
     * Исходная матрица не изменяется
     * Возвращается новый вектор
     */
    @Override
    public Vector3f mulVector(Vector3f vector) {
        float[] arrResult = new float[3];
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                arrResult[i] += this.getValue(i, k) * vector.getVector()[k][0];
            }
        }
        return new Vector3f(arrResult[0], arrResult[1], arrResult[2]);
    }

    /*
     * Детерминант матрицы
     */
    @Override
    public float determinantMatrix() {
        float v1 = this.getValue(0, 0) * this.getValue(1, 1) * this.getValue(2, 2);
        float v2 = this.getValue(0, 1) * this.getValue(1, 2) * this.getValue(2, 0);
        float v3 = this.getValue(0, 2) * this.getValue(1, 0) * this.getValue(2, 1);
        float v4 = this.getValue(0, 2) * this.getValue(1, 1) * this.getValue(2, 0);
        float v5 = this.getValue(0, 1) * this.getValue(1, 0) * this.getValue(2, 2);
        float v6 = this.getValue(0, 0) * this.getValue(1, 2) * this.getValue(2, 1);
        return v1 + v2 + v3 - v4 - v5 - v6;
    }

    /*
     * Транспонирование матрицы
     * Исходная матрица не изменяется
     */
    @Override
    public Matrix3x3 transposition() {
        Matrix3x3 arrTransposition = new Matrix3x3();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                arrTransposition.setValue(j, i, this.getValue(i, j));
            }
        }
        return arrTransposition;
    }

    /*
     * Получение обратной матрицы
     * Исходная матрица не изменяется
     */
    @Override
    public Matrix3x3 inverseMatrix() {
        float[][] arr = this.getArr();
        float delta = this.determinantMatrix();
        if (delta == 0) {
            return null;
        }
        Matrix3x3 arrTrans = this.transposition();
        float[][] arrInverse = new float[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                float[][] arrReduction = Search.matrixReduction(arrTrans.getArr(), i, j);
                arrInverse[i][j] = (float) Math.pow(-1, i + j) * Search.detMatrix(arrReduction) / delta;
            }
        }
        return new Matrix3x3(arrInverse);
    }

    /*
     * Получение корней системы с помощью метода Гаусса
     * На вход нужен вектор правой части системы
     * Исходная матрица не изменяется
     */
    @Override
    public Vector3f gaussMethod(Vector3f vector) {
        float[][] inputArr = new float[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inputArr[i][j] = this.getValue(i, j);
            }
        }
        for (int i = 0; i < 3; i++) {
            inputArr[i][3] = vector.getVector()[i][0];
        }
        float[] arrOutput = SearchGaussMethod.gaussMethod(inputArr);
        return new Vector3f(arrOutput[0], arrOutput[1], arrOutput[2]);
    }
}

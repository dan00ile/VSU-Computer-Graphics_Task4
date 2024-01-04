package com.cgvsu.math.matrix;

public class Search {
    /*
     * Создание нового массива без заданной строки и столбца
     * Исходный массив не изменяется
     */
    protected static float[][] matrixReduction(float[][] arr, int row, int col) {
        int countRow = 0, countCol = 0;
        int len = arr.length;
        float[][] det = new float[len - 1][len - 1];

        for (int i = 0; i < len; i++) {
            if (i == row) {
                countRow++;
                continue;
            }
            for (int j = 0; j < len; j++) {
                if (j == col) {
                    countCol++;
                    continue;
                }
                det[i - countRow][j - countCol] = arr[i][j];
            }
            countCol--;
        }
        return det;
    }

    /*
     * Детерминант массива любого размера
     */
    protected static float detMatrix(float[][] arr) {
        float sum = 0;
        if (arr.length == 1) {
            return arr[0][0];
        } else if (arr.length == 2) {
            return arr[0][0] * arr[1][1] - arr[1][0] * arr[0][1];
        } else {
            for (int i = 0; i < arr.length; i++) {
                float[][] arrMatrix = matrixReduction(arr, i, 0);
                float arrDeterminant = detMatrix(arrMatrix);
                float minor = (arr[i][0] * (float) Math.pow(-1, i) * arrDeterminant);
                sum += minor;
            }
        }
        return sum;
    }
}

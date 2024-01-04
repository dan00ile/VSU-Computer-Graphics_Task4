package com.cgvsu.math.matrix;

import java.util.ArrayList;

public class SearchGaussMethod {
    public static float[] gaussMethod(float[][] inputArr) {
        for (int i = 0; i < inputArr.length - 1; i++) {
            for (int j = i; j < inputArr.length - 1; j++) {
                if (inputArr[i][i] != 0) {
                    float a = inputArr[j + 1][i] / inputArr[i][i];
                    for (int k = 0; k < inputArr[0].length; k++) {
                        inputArr[j + 1][k] -= a * inputArr[i][k];
                    }
                }
            }
        }

        ArrayList<Integer> nullLine = nullCheck(inputArr);
        ArrayList<Float> listOut = new ArrayList<>();
        if (nullLine.isEmpty()) {
            listOut = variableSubstitution(inputArr);

            float[] arrOut = new float[inputArr.length];
            for (int i = listOut.size() - 1; i >= 0; i--) {
                if (Math.abs(listOut.get(i)) < 1e-14) {
                    arrOut[inputArr.length - 1 - i] = 0;
                    continue;
                }
                arrOut[inputArr.length - 1 - i] = listOut.get(i);
            }
            return arrOut;
        } else {
            inputArr = movingNullLines(inputArr, nullLine, 0);
            float[][][] arr = solution(inputArr, nullLine.size());
            inputArr = arr[0];

            float[] arrX = arr[1][0];

            ArrayList<Float> outInvert = variableSubstitution(inputArr);

            for (int i = 0; i < outInvert.size(); i++) {
                listOut.add(outInvert.get(outInvert.size() - 1 - i));
            }

            for (float x : arrX) {
                listOut.add(x);
            }
        }

        float[] arrOut = new float[listOut.size()];
        for (int i = 0; i < listOut.size(); i++) {
            if (Math.abs(listOut.get(i)) <= 1e-14) {
                arrOut[i] = 0;
                continue;
            }
            arrOut[i] = listOut.get(i);
        }
        return arrOut;
    }

    private static ArrayList<Float> variableSubstitution(float[][] inputArr) {
        int len = inputArr.length;
        float x = inputArr[len - 1][len] / inputArr[len - 1][len - 1];

        float[][] arr = new float[len - 1][len];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (j != arr[i].length - 1) {
                    arr[i][j] = inputArr[i][j];
                } else {
                    arr[i][j] = inputArr[i][j + 1] - x * inputArr[i][j];
                }
            }
        }

        ArrayList<Float> output = new ArrayList<>();
        output.add(x);

        if (arr.length <= 1) {
            if (arr.length == 0) {
                return output;
            }
            output.add(arr[0][1] / arr[0][0]);
            return output;
        }

        ArrayList<Float> outNew = variableSubstitution(arr);
        output.addAll(outNew);

        return output;
    }

    private static ArrayList<Integer> nullCheck (float[][] arr) {
        ArrayList<Integer> out = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            int count = 0;
            for (int j = 0; j < arr.length; j++) {
                if (Math.abs(arr[i][j]) <= 1e-14) {
                    count++;
                    if (count == arr.length) {
                        out.add(i);
                    }
                }
            }
        }
        return out;
    }

    private static float[][] movingNullLines (float[][] arr, ArrayList<Integer> nullLine, int number) {
        float[] arrCopy = new float[arr.length + 1];
        System.arraycopy(arr[nullLine.get(number)], 0, arrCopy, 0, arr.length + 1);

        if (nullLine.get(number) != arr.length - 1) {
            for (int i = nullLine.get(number); i < arr.length; i++) {
                System.arraycopy(arr[nullLine.get(number + 1)], 0, arr[nullLine.get(number)], 0, arr.length);
            }
            System.arraycopy(arrCopy, 0, arr[arr.length - 1], 0, arr.length + 1);

            if (nullLine.size() != number + 1) {
                arr = movingNullLines(arr, nullLine, number + 1);
            }
        }
        return arr;
    }

    private static float[][][] solution (float[][] arr, int count) {
        float[] arrX = new float[count];
        float[][] arrSolution = new float[arr.length][arr[0].length];

        for (int i = 0; i < arrSolution.length; i++) {
            System.arraycopy(arr[i], 0, arrSolution[i], 0, arrSolution[0].length);
        }

        for (int x = 0; x < count; x++) {
            arrX[arrX.length - x - 1] = x;

            for (int i = 0; i < arrSolution.length - x; i++) {
                for (int j = 0; j < arrSolution[0].length - x; j++) {
                    if (j == arrSolution[0].length - 2 - x) {
                        arrSolution[i][j] = arrSolution[i][j + 1] - x * arrSolution[i][j];
                    }
                }
            }
        }

        float[][] arrOut  = new float[arr.length - count][arr.length + 1 - count];
        for (int i = 0; i < arrOut.length; i++) {
            System.arraycopy(arrSolution[i], 0, arrOut[i], 0, arrOut[0].length);
        }
        return new float[][][]{arrOut, {arrX}};
    }
}

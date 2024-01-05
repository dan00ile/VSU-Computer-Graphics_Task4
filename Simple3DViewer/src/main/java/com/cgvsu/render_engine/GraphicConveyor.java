package com.cgvsu.render_engine;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.matrix.Matrix4x4;



public class GraphicConveyor {

    public static Matrix4x4 rotateScaleTranslate() {
        float[] matrix = new float[]{
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4x4 lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultZ = target.sub(eye);
        Vector3f resultX = up.vectorProduct(resultZ);
        Vector3f resultY = resultZ.vectorProduct(resultX);

        resultX = resultX.normalization();
        resultY = resultY.normalization();
        resultZ = resultZ.normalization();

        float[] matrix = new float[]{
                resultX.x, resultX.y, resultX.z, -resultX.dotProduct(eye),
                resultY.x, resultY.y, resultY.z, -resultY.dotProduct(eye),
                resultZ.x, resultZ.y, resultZ.z, -resultZ.dotProduct(eye),
                0, 0, 0, 1};
        return new Matrix4x4(matrix);
    }

    public static Matrix4x4 perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4x4 result = new Matrix4x4();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.setValue(0,0, tangentMinusOnDegree / aspectRatio);
        result.setValue(1, 1, tangentMinusOnDegree);
        result.setValue(2, 2, (farPlane + nearPlane) / (farPlane - nearPlane));
        result.setValue(3,2, 1.0F);
        result.setValue(2, 3, 2 * (nearPlane * farPlane) / (nearPlane - farPlane));
        return result;
    }

    public static Vector3f multiplyMatrix4ByVector3(Matrix4x4 matrix, final Vector3f vertex) {
        final float x = (vertex.x * matrix.getValue(0,0)) + (vertex.y * matrix.getValue(0,1)) + (vertex.z * matrix.getValue(0,2)) + matrix.getValue(0,3);
        final float y = (vertex.x * matrix.getValue(1,0)) + (vertex.y * matrix.getValue(1,1)) + (vertex.z * matrix.getValue(1,2)) + matrix.getValue(1,3);
        final float z = (vertex.x * matrix.getValue(2,0)) + (vertex.y * matrix.getValue(2,1)) + (vertex.z * matrix.getValue(2,2)) + matrix.getValue(2,3);
        final float w = (vertex.x * matrix.getValue(3,0)) + (vertex.y * matrix.getValue(3,1)) + (vertex.z * matrix.getValue(3,2)) + matrix.getValue(3,3);
        return new Vector3f(x / w, y / w, z / w);
    }

    public static Vector2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Vector2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}

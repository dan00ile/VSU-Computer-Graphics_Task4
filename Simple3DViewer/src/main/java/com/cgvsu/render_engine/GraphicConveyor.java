package com.cgvsu.render_engine;

import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector4f;


public class GraphicConveyor {

    public static Matrix4x4 lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultZ = target.sub(eye);
        Vector3f resultX = up.vectorProduct(resultZ);
        Vector3f resultY = resultZ.vectorProduct(resultX);

        resultX = resultX.normalize();
        resultY = resultY.normalize();
        resultZ = resultZ.normalize();

        float[] matrix = new float[]{
                resultX.getX(), resultX.getY(), resultX.getZ(), -resultX.dotProduct(eye),
                resultY.getX(), resultY.getY(), resultY.getZ(), -resultY.dotProduct(eye),
                resultZ.getX(), resultZ.getY(), resultZ.getZ(), -resultZ.dotProduct(eye),
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

    public static Vector2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Vector2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }

    public static Vector3f multiplyMatrix4ByVector3(Matrix4x4 matrix, final Vector3f vertex) {
        Vector4f multiVertex = matrix.mulVector(new Vector4f(vertex));

        float w = multiVertex.getW();
        w = (w == 0? 1 : w);

        multiVertex = multiVertex.div(w);

        return new Vector3f(multiVertex.getX(), multiVertex.getY(), multiVertex.getZ());
    }

}

package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.affine.AffineBuilder.ModelAffine;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.vector.Vector4f;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.math.vector.Vector2f;
import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.model.Model;
import static com.cgvsu.render_engine.GraphicConveyor.*;

public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height, ModelAffine transformMatr) throws Exception {
        Matrix4x4 modelMatrix = transformMatr.modelMatrix();
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        Matrix4x4 projectionMatrix = camera.getProjectionMatrix();

        Matrix4x4 modelViewProjectionMatrix = new Matrix4x4(projectionMatrix.getArr())
                .mulMatrix(viewMatrix)
                .mulMatrix(modelMatrix);


        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Vector2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector4f multiVertex = modelViewProjectionMatrix.mulVector(new Vector4f(vertex));

                float w = multiVertex.getW();
                w = (w == 0? 1 : w);

                multiVertex = multiVertex.div(w);

                Vector2f resultPoint = vertexToPoint(multiVertex, width, height);

                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
        }
    }
}
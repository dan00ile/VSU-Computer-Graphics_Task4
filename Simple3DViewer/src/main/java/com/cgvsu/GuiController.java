package com.cgvsu;

import com.cgvsu.affine.AffineBuilder.AffineBuilder;
import com.cgvsu.affine.AffineBuilder.ModelAffine;
import com.cgvsu.affine.AffineBuilder.Rotate;
import com.cgvsu.affine.AxisEnum;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.vector.Vector4f;
import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    private float distance = 10;

    private Map<Model, ModelAffine> modelTransformation = new HashMap<>();
    public Spinner<Double> spinnerScaleY;
    public Spinner<Double> spinnerScaleZ;
    public Spinner<Double> spinnerScaleX;
    public Spinner<Double> spinnerTranslateX;
    public Spinner<Double> spinnerRotateZ;
    public Spinner<Double> spinnerRotateY;
    public Spinner<Double> spinnerRotateX;
    public Spinner<Double> spinnerTranslateY;
    public Spinner<Double> spinnerTranslateZ;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;


    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 10),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnScroll(this::handleScroll);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height, modelTransformation.get(mesh));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        checkSpinners();

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            modelTransformation.put(mesh, new ModelAffine());
            setCameraOnMesh(mesh);
        // todo: обработка ошибок
    } catch (IOException exception) {

    }
}

private void checkScale() {
        ArrayList<Spinner<Double>> list = new ArrayList<>();

        list.add(spinnerScaleX);
        list.add(spinnerScaleY);
        list.add(spinnerScaleZ);

        for (Spinner<Double> spinner : list) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    spinner.getValueFactory().setValue(0.0);
                }
                checkScaleSpinner();
            });
        }
    }

    private void checkScaleSpinner() {
        float x = spinnerScaleX.getValue().floatValue();
        float y = spinnerScaleY.getValue().floatValue();
        float z = spinnerScaleZ.getValue().floatValue();
        Vector3f scale = new Vector3f(x, y, z);

        try {
            modelTransformation.get(mesh).setScale(scale);
        } catch (Exception e) {

        }
    }

    private void checkTranslate() {
        ArrayList<Spinner<Double>> list = new ArrayList<>();

        list.add(spinnerTranslateX);
        list.add(spinnerTranslateY);
        list.add(spinnerTranslateZ);

        for (Spinner<Double> spinner : list) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    spinner.getValueFactory().setValue(0.0);
                }
                checkTranslateSpinner();
            });
        }
    }

    private void checkTranslateSpinner() {
        float x = spinnerTranslateX.getValue().floatValue();
        float y = spinnerTranslateY.getValue().floatValue();
        float z = spinnerTranslateZ.getValue().floatValue();
        Vector3f translate = new Vector3f(x, y, z);

        try {
            modelTransformation.get(mesh).setTranslate(translate);
        } catch (Exception e) {

        }
    }

    private void checkRotate() {
        ArrayList<Spinner<Double>> list = new ArrayList<>();

        list.add(spinnerRotateX);
        list.add(spinnerRotateY);
        list.add(spinnerRotateZ);

        for (Spinner<Double> spinner : list) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    spinner.getValueFactory().setValue(0.0);
                }
                checkRotateSpinner();
            });
        }
    }

    private void checkRotateSpinner() {
        float x = spinnerRotateX.getValue().floatValue();
        float y = spinnerRotateY.getValue().floatValue();
        float z = spinnerRotateZ.getValue().floatValue();
        Vector3f rotate = new Vector3f(x, y, z);

        try {
            modelTransformation.get(mesh).setRotate(Rotate.RotateWayEnum.valueOf("XYZ"), rotate);
        } catch (Exception e) {

        }
    }

    private void checkSpinners() {
        checkRotate();
        checkScale();
        checkTranslate();
        setCameraOnMesh(mesh);
    }
    private void affineSpinnersReset() {
        ModelAffine a = (modelTransformation.get(mesh) != null ?
                new ModelAffine(modelTransformation.get(mesh)) : new ModelAffine());

        spinnerScaleX.getValueFactory().setValue((double) (a.getScale().getX()));
        spinnerScaleY.getValueFactory().setValue((double) (a.getScale().getY()));
        spinnerScaleZ.getValueFactory().setValue((double) (a.getScale().getZ()));
        spinnerRotateX.getValueFactory().setValue((double) (a.getRotate().getX()));
        spinnerRotateY.getValueFactory().setValue((double) (a.getRotate().getY()));
        spinnerRotateZ.getValueFactory().setValue((double) (a.getRotate().getZ()));
        spinnerTranslateX.getValueFactory().setValue((double) (a.getTranslate().getX()));
        spinnerTranslateY.getValueFactory().setValue((double) (a.getTranslate().getY()));
        spinnerTranslateZ.getValueFactory().setValue((double) (a.getTranslate().getZ()));
    }

    private void setCameraOnMesh(Model mesh) {
        affineSpinnersReset();
        setCameraStandartWay(mesh, AxisEnum.Z);
    }
    private void setCameraStandartWay(Model mesh, AxisEnum axis) {
        Vector3f middle = new Vector3f(0, 0, 0);

        if (mesh != null) {
            distance = mesh.getMaxDistanceFromCenter() * 2;
            middle = mesh.getCenter();
            System.out.println(middle);

            camera.setNearPlane(0.1f * distance);
            camera.setFarPlane(10 * distance);
        }

        camera.setTarget(middle);
        switch (axis) {
            case X -> {
                camera.setPosition(new Vector3f(distance, 0, 0).add(middle));
                camera.setUp(new Vector3f(0, 1, 0));
            }
            case Y -> {
                camera.setPosition(new Vector3f(0, distance, 0).add(middle));
                camera.setUp(new Vector3f(1, 0, 0));
            }
            case Z -> {
                camera.setPosition(new Vector3f(0, 0, distance).add(middle));
                camera.setUp(new Vector3f(0, 1, 0));
            }
        }

    }

    private double mouseX, mouseY;

    private void handleMousePressed(MouseEvent event) {
        if (event.isMiddleButtonDown()) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (event.isMiddleButtonDown()) {
            double deltaX = (event.getSceneX() - mouseX);
            double deltaY = (event.getSceneY() - mouseY);

            try {
                if (event.isAltDown()) {
                    rotateCamera((float) (deltaX / canvas.getWidth()), (float) (deltaY / canvas.getWidth()));
                } else {
                    translateCamera((float) deltaX * 0.05f,(float) deltaY * 0.05f);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }
    private Vector3f lastMove = new Vector3f(0,0,0);
    private void handleScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();

        Vector3f eyeToTarget = camera.getTarget().sub(camera.getPosition());
        boolean stopApproach = eyeToTarget.length() <= camera.getNearPlane();

        eyeToTarget = eyeToTarget.normalization().mul(distance * 0.1f);

        float x = eyeToTarget.getX();
        float y = eyeToTarget.getY();
        float z = eyeToTarget.getZ();

        Vector3f movePosition = new Vector3f(x, y, z);

        if (deltaY > 0 && !stopApproach) {
            camera.setPosition(camera.getPosition().add(movePosition));
        } else if (deltaY < 0) {
            if (stopApproach) {
                movePosition = lastMove;
            }
            camera.setPosition(camera.getPosition().sub(movePosition));
        }

        lastMove = movePosition;
    }

    private void rotateCamera(float deltaX, float deltaY) throws Exception {
        Vector3f lastEye = camera.getPosition();
        AffineBuilder b = new AffineBuilder();
        if (deltaY != 0) {
            b.rotate().byX(360 * deltaY).close();
        }
        if (deltaX != 0) {
            b.rotate().byY(360 * deltaX).close();
        }

        Vector4f newEye = b.returnFinalMatrix().mulVector(new Vector4f(lastEye));
        Vector4f newUp = b.returnFinalMatrix().mulVector(new Vector4f(camera.getUp()));

        camera.setPosition(new Vector3f(newEye));
        camera.setUp(new Vector3f(newUp));
    }

    private void translateCamera(float deltaX, float deltaY) {
        Vector3f lastEye = camera.getPosition();
        Vector3f target = camera.getTarget();

        // Вычисляем вектор от камеры к цели
        Vector3f cameraToTarget = lastEye.sub(target).normalization();

        // Вычисляем вектор, перпендикулярный вектору от камеры к цели
        Vector3f up = camera.getUp().normalization();
        Vector3f right = up.vectorProduct(cameraToTarget).normalization();

        // Перемещаем камеру относительно цели
        Vector3f translation = right.mul(deltaX).add(up.mul(deltaY));


        camera.setPosition(lastEye.add(translation));
        camera.setTarget(target.add(translation));
    }



    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
        camera.moveTarget(new Vector3f(TRANSLATION, 0,0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
        camera.moveTarget(new Vector3f(-TRANSLATION, 0,0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
        camera.moveTarget(new Vector3f(0, TRANSLATION,0));
        camera.moveUp(new Vector3f(0, TRANSLATION,0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
        camera.moveTarget(new Vector3f(0, -TRANSLATION,0));
        camera.moveUp(new Vector3f(0, -TRANSLATION,0));
    }
}
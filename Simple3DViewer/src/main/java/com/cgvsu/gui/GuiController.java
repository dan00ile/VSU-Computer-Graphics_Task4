package com.cgvsu.gui;

import com.cgvsu.Pivot;
import com.cgvsu.affine.AffineBuilder.AffineBuilder;
import com.cgvsu.affine.AffineBuilder.ModelAffine;
import com.cgvsu.affine.AffineBuilder.Rotate;
import com.cgvsu.affine.AxisEnum;
import com.cgvsu.math.matrix.Matrix4x4;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.vector.Vector4f;
import com.cgvsu.model.ModelAxis;
import com.cgvsu.render_engine.RenderEngine;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {
    public TableView<LoadedModel> tableView;
    public TableColumn<LoadedModel, CheckBox> isActive;
    public TableColumn<LoadedModel, String> modelPath;
    public TableColumn<LoadedModel, CheckBox> isFrozen;

    private float distance = 10;
    private List<LoadedModel> models = new ArrayList<>();
    private Map<LoadedModel, ModelAffine> modelTransformation = new HashMap<>();
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

    Model activeMesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 10),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;
    private Vector3f lastMove = new Vector3f(0, 0, 0);
    private double mouseX, mouseY;
    private Pivot pivot = new Pivot();

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
            if (activeMesh != null) {
                double width = canvas.getWidth();
                double height = canvas.getHeight();

                canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
                camera.setAspectRatio((float) (width / height));

                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, ModelAxis.makeAxisModel(camera.getNearPlane() * 10),
                            (int) width, (int) height, new ModelAffine());
                    for (LoadedModel model : models) {
                        RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height, modelTransformation.get(model));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        checkSpinners();

        //  anchorPane.getChildren().add(pivot);

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
            activeMesh = ObjReader.read(fileContent);

            LoadedModel newModel = new LoadedModel(activeMesh, fileName.toString());

            CheckBox checkBox1 = new CheckBox();
            newModel.setIsActive(checkBox1);
            CheckBox checkBox2 = new CheckBox();
            checkBox2.setDisable(true);
            newModel.setIsFrozen(checkBox2);
            checkBox1.setOnAction(event -> {
                // Если checkBox1 выбран, активируем checkBox2, иначе деактивируем
                checkBox2.setDisable(!checkBox1.isSelected());
                if (!checkBox1.isSelected()) {
                    checkBox2.setSelected(false);
                }

            });

            final ObservableList<LoadedModel> data = tableView.getItems();

            data.add(newModel);
            models.add(newModel);
            tableView.setItems(data);

            modelTransformation.put(newModel, new ModelAffine());
            setCameraOnMesh(newModel);

        } catch (IOException exception) {

        }
    }

    private void setPivotOnMesh(Model mesh) {

        pivot.attachToModel(mesh);
    }


    private void checkSpinners() {
        checkRotate();
        checkScale();
        checkTranslate();
    }

    private void affineSpinnersReset() {
        ModelAffine a = (modelTransformation.get(activeMesh) != null ?
                new ModelAffine(modelTransformation.get(activeMesh)) : new ModelAffine());

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
        setCameraInitially(mesh, AxisEnum.Z);
    }

    private void setCameraInitially(Model mesh, AxisEnum axis) {
        Vector3f middle = new Vector3f(0, 0, 0);

        if (mesh != null) {
            distance = mesh.getMaxDistanceFromCenter() * 2;
            middle = mesh.getCenter();
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
                    translateCamera((float) deltaX * 0.05f, (float) deltaY * 0.05f);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        }
    }

    private void handleScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        Vector3f lastEye = camera.getPosition();

        Vector3f eyeToTarget = camera.getTarget().sub(lastEye);
        boolean stopFlag = eyeToTarget.length() <= camera.getNearPlane();

        eyeToTarget = eyeToTarget.normalization().mul(distance * 0.1f);

        // можно удалить
        Vector3f movePosition = new Vector3f(eyeToTarget.getX(), eyeToTarget.getY(), eyeToTarget.getZ());

        if (deltaY > 0 && !stopFlag) {
            camera.setPosition(camera.getPosition().add(movePosition));
        } else if (deltaY < 0) {
            if (stopFlag) {
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

        camera.setPosition(new Vector3f(newEye.getX(), newEye.getY(), newEye.getZ()));
        camera.setUp(new Vector3f(newUp.getX(), newUp.getY(), newUp.getZ()));

    }

    private void translateCamera(float deltaX, float deltaY) {
        Vector3f lastEye = camera.getPosition();
        Vector3f target = camera.getTarget();

        // Вычисляем вектор от камеры к цели
        Vector3f eyeToTarget = lastEye.sub(target).normalization();

        // Вычисляем вектор, перпендикулярный вектору от камеры к цели
        Vector3f up = camera.getUp().normalization();
        Vector3f right = up.vectorProduct(eyeToTarget).normalization();

        // Перемещаем камеру относительно цели
        Vector3f translation = right.mul(deltaX).add(up.mul(deltaY));


        camera.setPosition(lastEye.add(translation));
        camera.setTarget(target.add(translation));
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
            modelTransformation.get(activeMesh).setScale(scale);
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
            modelTransformation.get(activeMesh).setTranslate(translate);
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
            modelTransformation.get(activeMesh).setRotate(Rotate.RotateWayEnum.valueOf("XYZ"), rotate);
        } catch (Exception e) {

        }
    }


    @FXML
    public void handleCameraForward() {
        setCameraInitially(activeMesh, AxisEnum.Z);
    }

    @FXML
    public void handleCameraBackward() throws Exception {
        setCameraInitially(activeMesh, AxisEnum.Z);
        rotateCamera(0.5f, 0);
    }

    @FXML
    public void handleCameraLeft() {
        setCameraInitially(activeMesh, AxisEnum.X);
    }

    @FXML
    public void handleCameraRight() throws Exception {
        setCameraInitially(activeMesh, AxisEnum.X);
        rotateCamera(0.5f, 0);
    }

    @FXML
    public void handleCameraUp() {
        setCameraInitially(activeMesh, AxisEnum.Y);
    }

    @FXML
    public void handleCameraDown() throws Exception {
        setCameraInitially(activeMesh, AxisEnum.Y);
        rotateCamera(0, 0.5f);
    }
}
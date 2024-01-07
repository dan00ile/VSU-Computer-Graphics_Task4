package com.cgvsu.gui;

import com.cgvsu.affine.AffineBuilder.ModelAffine;
import com.cgvsu.affine.AffineBuilder.Rotate;
import com.cgvsu.affine.AffineMatrix;
import com.cgvsu.affine.AxisEnum;
import com.cgvsu.math.matrix.Matrix4f;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.math.vector.Vector4f;
import com.cgvsu.model.Model;
import com.cgvsu.model.ModelAxis;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiController {
    public TableView<LoadedModel> tableView;
    public TableColumn<LoadedModel, CheckBox> isActive;
    public TableColumn<LoadedModel, String> modelPath;
    public TableColumn<LoadedModel, CheckBox> isFrozen;
    public MenuItem scaleMenu;

    private float distance = 10;
    private final List<LoadedModel> models = new ArrayList<>();
    private final Map<LoadedModel, ModelAffine> modelTransformation = new HashMap<>();
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

    private final Camera camera = new Camera(
            new Vector3f(0, 0, 10),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;
    private Vector3f lastMove = new Vector3f(0, 0, 0);
    private double mouseX, mouseY;

    private void selectModel(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            LoadedModel selectedModel = tableView.getSelectionModel().getSelectedItem();

            if (selectedModel != null) {
                if (selectedModel.isActive()) {
                    setCameraOnMesh(selectedModel);
                }
            }
        }
    }
    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnScroll(this::handleScroll);
        tableView.setOnMouseClicked(this::selectModel);


        modelPath.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModelName()));

        isActive.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getActive()));

        isFrozen.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFrozen()));


        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableList<LoadedModel> selectedModels = tableView.getSelectionModel().getSelectedItems();
        selectedModels.addListener((ListChangeListener<? super LoadedModel>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (LoadedModel model : c.getAddedSubList()) {
                        model.setSelected(true);
                    }
                }
                if (c.wasRemoved()) {
                    for (LoadedModel model : c.getRemoved()) {
                        model.setSelected(false);
                    }
                }
            }
        });


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
                        if (model.isActive()) {
                            RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height, modelTransformation.get(model));
                        }
                    }
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
            activeMesh = ObjReader.read(fileContent);

            LoadedModel newModel = new LoadedModel(activeMesh, fileName.toString());

            CheckBox checkBox1 = new CheckBox();
            checkBox1.setSelected(true);
            newModel.setIsActive(checkBox1);
            CheckBox checkBox2 = new CheckBox();
            checkBox2.setDisable(true);
            newModel.setIsFrozen(checkBox2);
            checkBox1.setOnAction(event -> {
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


    private void checkSpinners() {
        checkRotate();
        checkScale();
        checkTranslate();
    }

    private void affineSpinnersReset(Model mesh) {
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
        affineSpinnersReset(mesh);
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
                    translateCamera((float) deltaX, (float) deltaY);
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

        eyeToTarget = eyeToTarget.normalize().mul(distance * 0.1f);

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

    public void rotateCamera(float deltaX, float deltaY) {
        float rotationSpeed = 360f;
        float angleX = deltaX * rotationSpeed;
        float angleY = deltaY * rotationSpeed;

        angleX = (float) Math.toRadians(angleX);
        angleY = (float) Math.toRadians(angleY);

        rotateCameraVertical(angleY);
        rotateCameraHorizontal(angleX);
    }

    private void rotateCameraVertical(float angle) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition()).normalize();
        Vector3f right = direction.vectorProduct(camera.getUp()).normalize();

        Matrix4f rotationMatrix = AffineMatrix.rotateAroundAxis(right, -angle);
        rotateCamera(rotationMatrix);
    }

    private void rotateCameraHorizontal(float angle) {
        Matrix4f rotationMatrix = AffineMatrix.rotateAroundAxis(camera.getUp(), angle);
        rotateCamera(rotationMatrix);
    }

    private void rotateCamera(Matrix4f rotationMatrix) {
        Vector3f direction = camera.getTarget().sub(camera.getPosition());
        Vector4f newDirection = new Vector4f(direction.getX(), direction.getY(), direction.getZ(), 1.0f);

        newDirection = rotationMatrix.mulVector(newDirection);

        camera.setPosition(camera.getTarget().sub(new Vector3f(newDirection.getX(), newDirection.getY(), newDirection.getZ())));
        camera.setUp(new Vector3f(rotationMatrix.mulVector(new Vector4f(camera.getUp()))));
    }


    // TODO: перемещение камеры зависит от размера модели
    private void translateCamera(float deltaX, float deltaY) {
        float speed = 0.01f;

        Vector3f lastEye = camera.getPosition();
        Vector3f target = camera.getTarget();

        // Вычисляем вектор от камеры к цели
        Vector3f eyeToTarget = lastEye.sub(target).normalize();

        // Вычисляем вектор, перпендикулярный вектору от камеры к цели
        Vector3f up = camera.getUp().normalize();
        Vector3f right = up.vectorProduct(eyeToTarget).normalize();

        // Перемещаем камеру относительно цели
        Vector3f translation = right.mul(deltaX * speed).add(up.mul(deltaY * speed));

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
                    spinner.getValueFactory().setValue(1.0);
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
            for (LoadedModel mesh : models) {
                if (!mesh.isFrozen() && mesh.isSelected()) {
                    modelTransformation.get(mesh).setScale(scale);
                }
            }
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
            for (LoadedModel mesh : models) {
                if (!mesh.isFrozen() && mesh.isSelected()) {
                    ModelAffine affine = modelTransformation.get(mesh);

                    affine.setTranslate(translate);
                }
            }
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
            for (LoadedModel mesh : models) {
                if (!mesh.isFrozen() && mesh.isSelected()) {
                    ModelAffine affine = modelTransformation.get(mesh);

                    affine.setRotate(Rotate.RotateWayEnum.valueOf("XYZ"), rotate);
                }
            }
        } catch (Exception e) {

        }
    }


    @FXML
    public void handleCameraForward() {
        setCameraInitially(activeMesh, AxisEnum.Z);
    }

    @FXML
    public void handleCameraBackward() {
        setCameraInitially(activeMesh, AxisEnum.Z);
        rotateCamera(0.5f, 0);
    }

    @FXML
    public void handleCameraLeft() {
        setCameraInitially(activeMesh, AxisEnum.X);
    }

    @FXML
    public void handleCameraRight() {
        setCameraInitially(activeMesh, AxisEnum.X);
        rotateCamera(0.5f, 0);
    }

    @FXML
    public void handleCameraUp() {
        setCameraInitially(activeMesh, AxisEnum.Y);
    }

    @FXML
    public void handleCameraDown() {
        setCameraInitially(activeMesh, AxisEnum.Y);
        rotateCamera(0, 0.5f);
    }
}
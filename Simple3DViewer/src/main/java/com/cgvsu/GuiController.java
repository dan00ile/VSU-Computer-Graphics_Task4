package com.cgvsu;

import com.cgvsu.affine.AffineBuilder.ModelAffine;
import com.cgvsu.affine.AffineBuilder.Rotate;
import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

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

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            modelTransformation.put(mesh, new ModelAffine());
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
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}
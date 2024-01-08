package com.cgvsu.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;
public class ModelSelectionDialog {

    @FXML
    private CheckBox TRSCheck;
    @FXML
    private ChoiceBox<LoadedModel> modelChoiceBox;

    private Stage dialogStage;
    private LoadedModel selectedModel;

    private List<LoadedModel> models;

    public void initialize() {
        if (models != null) {
            ObservableList<LoadedModel> modelList = FXCollections.observableArrayList(models);
            modelChoiceBox.setItems(modelList);

            modelChoiceBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(LoadedModel model) {
                    if (model != null) {
                        return model.getModelName();
                    } else {
                        return "";
                    }
                }
                @Override
                public LoadedModel fromString(String string) {
                    return null;
                }
            });
        }
    }



    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setModels(List<LoadedModel> models) {
        this.models = models;
        if (modelChoiceBox != null) {
            initialize();
        }
    }

    public boolean getTRSCheck() {
        return TRSCheck.isSelected();
    }

    public LoadedModel getSelectedModel() {
        return selectedModel;
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    @FXML
    private void onSelect() {
        selectedModel = modelChoiceBox.getValue();
        dialogStage.close();
    }
}

package com.cgvsu.gui;

import com.cgvsu.model.Model;
import javafx.scene.control.CheckBox;

public class LoadedModel extends Model {

    private String modelPath;
    private CheckBox isActive;
    private CheckBox isFrozen;

    public LoadedModel(Model model, String modelPath) {
        super(model);
        this.modelPath = modelPath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public boolean isActive() {
        return isActive.isSelected();
    }

    public CheckBox getActive() {
        return isActive;
    }

    public boolean isFrozen() {
        return isFrozen.isSelected();
    }

    public CheckBox getFrozen() {
        return isFrozen;
    }

    public void setIsActive(CheckBox isActive) {
        this.isActive = isActive;
    }

    public void setIsFrozen(CheckBox isFrozen) {
        this.isFrozen = isFrozen;
    }

    public String getModelName() {
        String[] pathSegments = modelPath.split("\\\\");
        return pathSegments[pathSegments.length - 1];
    }
}

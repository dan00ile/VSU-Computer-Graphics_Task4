package com.cgvsu.gui;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SnowflakeCheckBoxTableCell extends TableCell<LoadedModel, Boolean> {
    private final ImageView snowflakeImage = new ImageView(new Image("snowflake.png"));

    public SnowflakeCheckBoxTableCell() {
        snowflakeImage.setFitWidth(16);
        snowflakeImage.setFitHeight(16);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            // Установка изображения снежинки в зависимости от значения CheckBox
            if (item) {
                snowflakeImage.setStyle("-fx-font-weight: bold");
            } else {
                snowflakeImage.setStyle("-fx-font-weight: normal");
            }

            setGraphic(snowflakeImage);
        }
    }
}




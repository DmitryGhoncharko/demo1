package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.dao.MySQLConnectionPool;
import org.example.demo1.entity.Action;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddActionController {

    @FXML
    private TextField actionInfoField;

    @FXML
    private ImageView actionImageView;

    private File selectedImageFile;

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) actionImageView.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            actionImageView.setImage(image);
        }
    }

    @FXML
    private void handleSaveAction() {
        String actionInfo = actionInfoField.getText();

        if (actionInfo.isEmpty() || selectedImageFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля и загрузите изображение.");
            alert.showAndWait();
            return;
        }

        // Проверка и создание директории, если она не существует
        File directory = new File("images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Сохранение изображения в локальное хранилище
        try {
            Files.copy(selectedImageFile.toPath(), Paths.get(directory.getPath(), selectedImageFile.getName()));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при сохранении изображения.");
            alert.showAndWait();
            return;
        }

        // Логика для сохранения данных о акции в базе данных
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO action_ (info_, image) VALUES (?, ?)")) {
            preparedStatement.setString(1, actionInfo);
            preparedStatement.setString(2, selectedImageFile.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при сохранении данных акции.");
            alert.showAndWait();
            return;
        }

        // Очистка полей после сохранения
        actionInfoField.clear();
        actionImageView.setImage(null);
        selectedImageFile = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText("Акция успешно сохранена.");
        alert.showAndWait();
    }
    @FXML
    protected void backPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminPage.fxml"));
        Parent root = fxmlLoader.load();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        newStage.setTitle("Main Window");
        newStage.setScene(new Scene(root));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        newStage.setX(screenBounds.getMinX());
        newStage.setY(screenBounds.getMinY());
        newStage.setWidth(screenBounds.getWidth());
        newStage.setHeight(screenBounds.getHeight());
        newStage.setFullScreen(true);
        newStage.show();

    }
}

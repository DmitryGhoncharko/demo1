package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddProductController {

    @FXML
    private TextField productNameField;

    @FXML
    private TextArea productDescriptionField;

    @FXML
    private ImageView productImageView;

    private File selectedImageFile;

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) productImageView.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            productImageView.setImage(image);
        }
    }
    @FXML
    private void handleSaveProduct() {
        String productName = productNameField.getText();
        String productDescription = productDescriptionField.getText();

        if (productName.isEmpty() || productDescription.isEmpty()  || selectedImageFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля и загрузите изображение.");
            alert.showAndWait();
            return;
        }
        File directory = new File("images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

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

        try (Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("insert into product(product_name,product_date,product_image) values(?,?,?)")) {
            preparedStatement.setString(1, productName);
            preparedStatement.setString(2, productDescription);
            preparedStatement.setString(3, selectedImageFile.getName());
            preparedStatement.executeUpdate();
        }catch (SQLException e){

        }
        productNameField.clear();
        productDescriptionField.clear();
        productImageView.setImage(null);
        selectedImageFile = null;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText("Продукт успешно сохранен.");
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

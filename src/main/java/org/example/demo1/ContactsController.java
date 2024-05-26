package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactsController {

    @FXML
    private ImageView backButton;

    @FXML
    private void handleBackButtonAction(MouseEvent event) {
        try {
            // Переход на предыдущую страницу
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientPage.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class QrCodeController {
    @FXML
    private ImageView mapView;

    @FXML
    private ImageView backButton;

    @FXML
    private void initialize() {
        Image mapImage = new Image("/images/qr.png");
        mapView.setImage(mapImage);
        mapView.setFitHeight(1024);
        mapView.setFitWidth(768);
        mapView.setPreserveRatio(true);
    }

    @FXML
    private void handleBackButtonAction(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientPage.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

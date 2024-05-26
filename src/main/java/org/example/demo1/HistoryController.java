package org.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryController {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox historyContainer;

    @FXML
    public void initialize() {
        loadHistory();
    }

    private void loadHistory() {
        List<Purchase> purchaseHistory = generateDummyHistory();

        for (Purchase purchase : purchaseHistory) {
            addPurchaseToView(purchase);
        }
    }

    private List<Purchase> generateDummyHistory() {
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(new Purchase("Молоко", "01.01.2023", "/images/product1.jpg"));
        purchases.add(new Purchase("Яйца", "15.01.2023", "/images/product2.jpg"));
        purchases.add(new Purchase("Хлеб черный", "28.01.2023", "/images/product3.jpg"));
        return purchases;
    }

    private void addPurchaseToView(Purchase purchase) {
        HBox purchaseBox = new HBox(10);
        purchaseBox.setPadding(new Insets(10));
        purchaseBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1px; -fx-background-color: white;");

        ImageView purchaseImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream(purchase.getImagePath()));
            purchaseImage.setImage(image);
        } catch (Exception e) {
            purchaseImage.setImage(new Image(getClass().getResourceAsStream("/images/default.png")));
        }
        purchaseImage.setFitHeight(100);
        purchaseImage.setFitWidth(100);

        VBox purchaseInfo = new VBox(5);
        Label nameLabel = new Label(purchase.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label dateLabel = new Label("Дата: " + purchase.getDate());

        purchaseInfo.getChildren().addAll(nameLabel, dateLabel);
        purchaseBox.getChildren().addAll(purchaseImage, purchaseInfo);
        historyContainer.getChildren().add(purchaseBox);
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

    private static class Purchase {
        private final String name;
        private final String date;
        private final String imagePath;

        public Purchase(String name, String date, String imagePath) {
            this.name = name;
            this.date = date;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}

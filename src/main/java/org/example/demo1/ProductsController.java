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
import org.example.demo1.entity.Product;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductsController {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox productsContainer;

    @FXML
    public void initialize() {
        loadProducts();
    }

    private void loadProducts() {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_date"),
                        resultSet.getString("product_image")
                );
                addProductToView(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProductToView(Product product) {
        HBox productBox = new HBox(10);
        productBox.setPadding(new Insets(10));
        productBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1px; -fx-background-color: white;");

        ImageView productImage = new ImageView();
        try {
            Image image = new Image("file:images/" + product.getImage());
            productImage.setImage(image);
        } catch (Exception e) {
            productImage.setImage(new Image("file:images/map.png"));
        }
        productImage.setFitHeight(100);
        productImage.setFitWidth(100);

        VBox productInfo = new VBox(5);
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label dateLabel = new Label("Дата: " + product.getDate());

        productInfo.getChildren().addAll(nameLabel, dateLabel);
        productBox.getChildren().addAll(productImage, productInfo);
        productsContainer.getChildren().add(productBox);
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

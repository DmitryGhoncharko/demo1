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
import org.example.demo1.entity.Action;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionsController {

    @FXML
    private ImageView backButton;

    @FXML
    private VBox actionsContainer;

    @FXML
    public void initialize() {
        loadActions();
    }

    private void loadActions() {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM action_")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Action action = new Action(
                        resultSet.getInt("action_id"),
                        resultSet.getString("info_"),
                        resultSet.getString("image")
                );
                addActionToView(action);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionToView(Action action) {
        HBox actionBox = new HBox(10);
        actionBox.setPadding(new Insets(10));
        actionBox.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1px; -fx-background-color: white;");

        ImageView actionImage = new ImageView();
        try {
            Image image = new Image("file:images/" + action.getImage());
            actionImage.setImage(image);
        } catch (Exception e) {
            actionImage.setImage(new Image("file:images/map.png"));
        }
        actionImage.setFitHeight(100);
        actionImage.setFitWidth(100);

        VBox actionInfo = new VBox(5);
        Label infoLabel = new Label(action.getInfo());
        infoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        actionInfo.getChildren().add(infoLabel);
        actionBox.getChildren().addAll(actionImage, actionInfo);
        actionsContainer.getChildren().add(actionBox);
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

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
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.cache.Cache;
import org.example.demo1.dao.MySQLConnectionPool;
import org.example.demo1.entity.Role;
import org.example.demo1.entity.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPageController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    protected void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void handleMainPageAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || password == null || username.isEmpty() || password.isEmpty() || username.isBlank() || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка входа");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля.");
            alert.showAndWait();
            return;
        }

        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                int roleId = resultSet.getInt("role_id");
                Role role = null;
                if(roleId == 1) {
                    role = new Role(1,"ADMIN");
                }else if(roleId == 2) {
                    role = new Role(2,"USER");
                }
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("phone"),
                        resultSet.getString("card_number"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        role
                );


                Cache.users.clear();
                Cache.users.add(user);


                if(user.getRole().getRoleId()==1){
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
                }else if(user.getRole().getRoleId()==2){
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clientPage.fxml"));
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
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка входа");
                alert.setHeaderText(null);
                alert.setContentText("Неверное имя пользователя или пароль.");
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

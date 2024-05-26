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
import org.example.demo1.entity.Role;
import org.example.demo1.entity.User;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterPageController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField cardNumberField;
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
    protected void handleRegisterButtonAction(ActionEvent event) {

        String name = nameField.getText();
        String phone = phoneField.getText();
        String cardNumber = cardNumberField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (name == null || phone == null || cardNumber == null || username == null || password == null || name.isEmpty() || phone.isEmpty() || cardNumber.isEmpty() || username.isEmpty() || password.isEmpty() || name.isBlank() || phone.isBlank() || cardNumber.isBlank() || username.isBlank() || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка регистрации");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля.");
            alert.showAndWait();
            return;
        }
        try (Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement statement = connection.prepareStatement("select user_id from users where phone = ?")) {
            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка регистрации");
                alert.setHeaderText(null);
                alert.setContentText("Пользователь с таким номером телефона уже зарегистрирован");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement statement = connection.prepareStatement("insert into users (name, phone, card_number, username, password, role_id) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setString(3, cardNumber);
            statement.setString(4, username);
            statement.setString(5, password);
            statement.setInt(6, 2);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                User user = new User(id, name, phone, cardNumber, username, password, new Role(2, "CLIENT"));
                Cache.users.clear();
                Cache.users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}

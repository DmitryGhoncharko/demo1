package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.cache.Cache;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsController {

    @FXML
    private ImageView backButton;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField patronymicField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private ToggleButton maleToggleButton;

    @FXML
    private ToggleButton femaleToggleButton;

    @FXML
    private TextField birthdateField;

    @FXML
    private Button saveButton;

    private ToggleGroup genderToggleGroup;

    @FXML
    public void initialize() {
        genderToggleGroup = new ToggleGroup();
        maleToggleButton.setToggleGroup(genderToggleGroup);
        femaleToggleButton.setToggleGroup(genderToggleGroup);
        phoneField.setText(Cache.users.get(0).getPhone());
        loadUserDetails();
    }

    private void loadUserDetails() {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_details WHERE user_id = ?")) {
            preparedStatement.setInt(1, Cache.users.get(0).getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                surnameField.setText(resultSet.getString("user_surname"));
                nameField.setText(resultSet.getString("user_name"));
                patronymicField.setText(resultSet.getString("user_patronymic"));
                phoneField.setText(Cache.users.get(0).getPhone());
                emailField.setText(resultSet.getString("user_email"));
                birthdateField.setText(resultSet.getString("user_birthdate"));
                String gender = resultSet.getString("user_gender");

                if (gender.equals("Мужской")) {
                    maleToggleButton.setSelected(true);
                } else if (gender.equals("Женский")) {
                    femaleToggleButton.setSelected(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) throws IOException {
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
    }

    @FXML
    private void handleSaveButtonAction() {
        String surname = surnameField.getText();
        String name = nameField.getText();
        String patronymic = patronymicField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        try(Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("update users set phone = ? where user_id = ?")){
            preparedStatement.setString(1,phone);
            preparedStatement.setInt(2,Cache.users.get(0).getUserId());
        }catch (SQLException e){

        }
        String gender = ((ToggleButton) genderToggleGroup.getSelectedToggle()).getText();
        String birthdate = birthdateField.getText();
        try(Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_details WHERE user_id = ?")) {
            preparedStatement.setInt(1, Cache.users.get(0).getUserId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("user_details_id");
                try(Connection connection1 = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement1 = connection1.prepareStatement(
                        "UPDATE user_details SET user_surname = ?, user_name = ?, user_patronymic = ?, user_gender = ?, user_birthdate = ?, user_email = ?  WHERE user_details_id = ?"
                )) {
                    preparedStatement1.setString(1, surname);
                    preparedStatement1.setString(2, name);
                    preparedStatement1.setString(3, patronymic);
                    preparedStatement1.setString(4, gender);
                    preparedStatement1.setString(5, birthdate);
                    preparedStatement1.setString(6, email);
                    preparedStatement1.setInt(7, id);
                    preparedStatement1.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try (Connection connection1 = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement1 = connection1.prepareStatement(
                        "INSERT INTO user_details(user_surname, user_name, user_patronymic, user_gender, user_birthdate, user_id, user_email) VALUES(?, ?, ?, ?, ?, ?, ?)"
                )) {
                    preparedStatement1.setString(1, surname);
                    preparedStatement1.setString(2, name);
                    preparedStatement1.setString(3, patronymic);
                    preparedStatement1.setString(4, gender);
                    preparedStatement1.setString(5, birthdate);
                    preparedStatement1.setInt(6, Cache.users.get(0).getUserId());
                    preparedStatement1.setString(7, email);
                    preparedStatement1.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

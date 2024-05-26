package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.dao.MySQLConnectionPool;
import org.example.demo1.entity.Role;
import org.example.demo1.entity.Support;
import org.example.demo1.entity.User;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TexPageController implements Initializable {

    @FXML
    private VBox questionsVBox;

    @FXML
    private ScrollPane scrollPane;

    private List<Support> supportList1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSupportRequestsFromDatabase();
    }
    @FXML
    protected void handleBackPage(ActionEvent event) {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadSupportRequestsFromDatabase() {

        try(Connection connection = MySQLConnectionPool.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM support where status = false");
            List<Support> supportList = new ArrayList<>();
            while (resultSet.next()) {
                User user = null;
                try(Connection connection1 = MySQLConnectionPool.getConnection(); PreparedStatement statement1 = connection1.prepareStatement("select * from users where user_id = ?")) {
                    statement1.setInt(1, resultSet.getInt("user_id"));
                    ResultSet resultSet2 = statement1.executeQuery();
                    if(resultSet2.next()) {
                         user = new User(resultSet2.getInt("user_id"), resultSet2.getString("name"),
                                 resultSet2.getString("phone"),resultSet2.getString("card_number"),
                                 resultSet2.getString("username"),resultSet2.getString("password"), new Role(1,"CLIENT"));
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Support support = new Support(resultSet.getInt("support_id"),
                        user,
                        resultSet.getString("message"),
                        resultSet.getString("response"),
                        resultSet.getBoolean("status")
                        );
                supportList.add(support);
            }
            supportList1 = supportList;
            loadQuestions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestions() {
        questionsVBox.getChildren().clear();
        for (Support support : supportList1) {
            VBox questionBox = new VBox();
            questionBox.setSpacing(5);

            Label userNumberLabel = new Label("Мобильный номер пользователя: " + support.getUser().getUserId());
            Label questionLabel = new Label("Вопрос: " + support.getMessage());
            TextArea responseField = new TextArea();
            responseField.setPromptText("Введите ваш ответ сдесь");
            responseField.setPrefHeight(100);

            Button submitButton = new Button("Отправить ответ");
            submitButton.setOnAction(event -> {
                String response = responseField.getText();
                if (response == null || response.isEmpty() || response.isBlank()) {
                    return;
                }
                support.setResponse(response);
                support.setStatus(true);
                saveSupportResponse(support);
            });

            questionBox.getChildren().addAll(userNumberLabel, questionLabel, responseField, submitButton);
            questionsVBox.getChildren().add(questionBox);
        }
    }

    private void saveSupportResponse(Support support) {

        try (Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("update support set response = ?,  status = true where support_id = ?")) {
            preparedStatement.setString(1, support.getResponse());
            preparedStatement.setInt(2,support.getId());

            preparedStatement.executeUpdate();
            loadSupportRequestsFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}

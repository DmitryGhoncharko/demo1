package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo1.cache.Cache;
import org.example.demo1.dao.MySQLConnectionPool;
import org.example.demo1.entity.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportController {

    @FXML
    private ImageView backButton;

    @FXML
    private TextArea supportTextField;

    @FXML
    private Button submitButton;

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
    private void handleSubmitButtonAction() {
        String supportMessage = supportTextField.getText();
        if (supportMessage == null || supportMessage.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка входа");
            alert.setHeaderText(null);
            alert.setContentText("Пожалуйста, заполните все поля.");
            alert.showAndWait();
            return;
        }
        User user = Cache.users.get(0);
        try(Connection connection = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("insert into support(user_id,message,response,status) values (?,?,?,?)")){
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setString(2, supportMessage);
            preparedStatement.setString(3, "");
            preparedStatement.setBoolean(4, false);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Успех");
        alert.setHeaderText(null);
        alert.setContentText("Ваше сообщение в службу поддержки успешно отправлено");
        alert.showAndWait();
        supportTextField.clear();
    }
}

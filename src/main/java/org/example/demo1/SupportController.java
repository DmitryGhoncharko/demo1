package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
            // Переход на предыдущую страницу
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
            // Покажите сообщение об ошибке пользователю
            System.out.println("Пожалуйста, введите сообщение.");
            return;
        }

        // Логика для отправки сообщения в поддержку
        System.out.println("Сообщение отправлено: " + supportMessage);

        // Очистка поля после отправки сообщения
        supportTextField.clear();
    }
}

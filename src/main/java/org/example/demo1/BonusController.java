package org.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.demo1.cache.Cache;
import org.example.demo1.entity.Bonus;
import org.example.demo1.dao.MySQLConnectionPool;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class BonusController {

    @FXML
    private ImageView backButton;

    @FXML
    private Label bonusLabel;

    @FXML
    public void initialize() {
        loadBonus();
    }

    private void loadBonus() {
        int userId = Cache.users.get(0).getUserId();
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT bonus_count FROM bonus WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double bonusCount = resultSet.getDouble("bonus_count");
                int random = new Random().nextInt(100);
                if(random>0 && random<60){
                    bonusCount+=new Random().nextDouble(15)*100;
                    BigDecimal bd = new BigDecimal(bonusCount).setScale(1, RoundingMode.HALF_UP);
                    bonusCount = bd.doubleValue();
                }else{
                    bonusCount-=new Random().nextDouble(15)*100;
                    if(bonusCount<0){
                        bonusCount=0;
                    }
                }
                try(Connection connection1 = MySQLConnectionPool.getConnection(); PreparedStatement preparedStatement1 = connection1.prepareStatement("update bonus set bonus_count = ? where user_id = ?")){
                    preparedStatement1.setDouble(1, bonusCount);
                    preparedStatement1.setInt(2, userId);
                    preparedStatement1.executeUpdate();
                }catch (SQLException e) {

                }
                bonusLabel.setText(String.valueOf(bonusCount));
            }else {
                try(Connection connection1 = MySQLConnectionPool.getConnection();PreparedStatement preparedStatement1 = connection1.prepareStatement("insert into bonus(bonus_count,user_id) values(?,?) ")) {
                    preparedStatement1.setDouble(1, 0);
                    preparedStatement1.setInt(2, userId);
                    preparedStatement1.executeUpdate();
                }catch (SQLException e) {

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
}

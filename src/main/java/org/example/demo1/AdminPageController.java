package org.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.demo1.cache.Cache;

import java.io.IOException;

public class AdminPageController {
    @FXML
    protected void texPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("texPage.fxml"));
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
    protected void addProductPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addProduct.fxml"));
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
    protected void productListPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productList.fxml"));
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
    protected void addActionPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addAction.fxml"));
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
    protected void actionListPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("actionList.fxml"));
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
    protected void exitListPage(ActionEvent event) throws IOException {
        Cache.users.clear();
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
}

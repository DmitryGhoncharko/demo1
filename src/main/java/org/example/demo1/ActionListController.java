package org.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.demo1.dao.MySQLConnectionPool;
import org.example.demo1.entity.Action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActionListController {

    @FXML
    private TableView<Action> actionTableView;

    @FXML
    private TableColumn<Action, Integer> idColumn;

    @FXML
    private TableColumn<Action, String> infoColumn;

    @FXML
    private TableColumn<Action, String> imageColumn;

    @FXML
    private TableColumn<Action, Void> deleteColumn;

    private ObservableList<Action> actionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        infoColumn.setCellValueFactory(new PropertyValueFactory<>("info"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        imageColumn.setCellFactory(column -> new TableCell<Action, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image("file:images/" + item));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    setGraphic(imageView);
                }
            }
        });

        addButtonToTable();
        loadActionsFromDatabase();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Action, Void>, TableCell<Action, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Action, Void> call(final TableColumn<Action, Void> param) {
                final TableCell<Action, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Удалить");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Action action = getTableView().getItems().get(getIndex());
                            deleteAction(action);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        deleteColumn.setCellFactory(cellFactory);
    }

    private void loadActionsFromDatabase() {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM action_")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Action action = new Action(
                        resultSet.getInt("action_id"),
                        resultSet.getString("info_"),
                        resultSet.getString("image")
                );
                actionList.add(action);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        actionTableView.setItems(actionList);
    }

    private void deleteAction(Action action) {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM action_ WHERE action_id = ?")) {
            preparedStatement.setInt(1, action.getId());
            preparedStatement.executeUpdate();
            actionList.remove(action);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при удалении акции.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
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
    }
}

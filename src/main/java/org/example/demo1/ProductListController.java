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
import org.example.demo1.entity.Product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductListController {

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> dateColumn;

    @FXML
    private TableColumn<Product, String> imageColumn;

    @FXML
    private TableColumn<Product, Void> deleteColumn;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    @FXML
    protected void backPage(ActionEvent event) throws IOException {
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
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setCellFactory(column -> new TableCell<>() {
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
        loadProductsFromDatabase();
    }

    private void addButtonToTable() {
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Удалить");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product product = getTableView().getItems().get(getIndex());
                            deleteProduct(product);
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

    private void loadProductsFromDatabase() {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_date"),
                        resultSet.getString("product_image")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        productTableView.setItems(productList);
    }

    private void deleteProduct(Product product) {
        try (Connection connection = MySQLConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product WHERE product_id = ?")) {
            preparedStatement.setInt(1, product.getId());
            preparedStatement.executeUpdate();
            productList.remove(product);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка при удалении продукта.");
            alert.showAndWait();
        }
    }
}

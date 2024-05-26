package org.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.demo1.cache.Cache;
import org.example.demo1.entity.User;

public class ClientPageController {

    @FXML
    private ImageView profileImageView;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;

    @FXML
    public void initialize() {
        if (!Cache.users.isEmpty()) {
            User user = Cache.users.get(0);
            nameLabel.setText(user.getName());
            phoneLabel.setText(user.getPhone());


            Image profileImage = new Image("/mnt/data/image.png");
            profileImageView.setImage(profileImage);
        }
    }
}

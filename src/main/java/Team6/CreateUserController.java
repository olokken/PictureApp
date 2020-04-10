package Team6;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.UserService;

import java.io.IOException;

public class CreateUserController {
    @FXML
    TextField username;
    @FXML
    TextField password;

    UserService userService = new UserService();

    public CreateUserController() throws IOException {
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        if (username.getText() != null && password.getText()!= null) {
            userService.createUser(username.getText(), password.getText());
            App.setRoot("login");
        }
    }
}

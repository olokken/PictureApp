package Team6;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import services.UserService;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField password;
    @FXML
    TextField username;
    UserService userService = new UserService();

    public LoginController() throws IOException {
    }

    public void createNewUser(ActionEvent actionEvent) throws IOException {
        App.setRoot("createUser");
    }

    public void signIn(ActionEvent actionEvent) throws IOException {
        User user = userService.login(username.getText(), password.getText());
        if (user != null) {
            Context.getInstance().currentUser().setId(user.getId());
            Context.getInstance().currentUser().setUsername(user.getUsername());
            App.setRoot("primary");
        }
        else {
            System.out.println("Feil brukernavn eller passord din gjøk");
        }
    }
}

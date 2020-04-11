package Team6;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.UserService;

import java.io.IOException;

public class CreateUserController {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    PasswordField confirmPassword;
    @FXML
    Label label;

    UserService userService = new UserService();

    public CreateUserController() throws IOException {
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        if(checkUsername() && checkPassword() && passwordEqual()) {
            userService.createUser(username.getText(), password.getText());
            App.setRoot("login");
        }
    }

    public boolean checkUsername(){
        if(username.getText().trim().equals("")){
            label.setText("Username can't be empty!");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPassword(){
        if(password.getText().trim().equals("") || confirmPassword.getText().trim().equals("")){
            label.setText("One or both passwordfields\nare empty!");
            return false;
        } else {
            return true;
        }
    }

    public boolean passwordEqual(){
        if(password.getText().equals(confirmPassword.getText())){
            return true;
        } else {
            label.setText("Passwords doesn't match!");
            return false;
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        Context.getInstance().currentAlbum().setPictures(null);
        App.setRoot("login");
    }
}

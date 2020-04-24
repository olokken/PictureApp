package Team6.Controllers;

import Team6.App;
import Team6.services.AppLogger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Team6.services.UserService;

import java.io.IOException;
import java.util.logging.Level;

public class CreateUserView extends Base {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    PasswordField confirmPassword;
    @FXML
    Label label;

    UserService userService = new UserService();

    public CreateUserView() throws IOException {
    }

    public void createUser() {
        try{
            if(checkUsername() && checkPassword() && checkEqualPasswords()) {
                if (userService.createUser(username.getText(), password.getText())) {
                    userService.createUser(username.getText(), password.getText());
                    switchScene("createUser", "login");
                } else {
                    showInformationDialog("Username taken", "Please choose another username");
                }
            }
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
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

    public boolean checkEqualPasswords(){
        if(password.getText().equals(confirmPassword.getText())){
            return true;
        } else {
            label.setText("Passwords doesn't match!");
            return false;
        }
    }

    @FXML
    private void switchToLogin() throws IOException {
        try{
            Context.getInstance().currentAlbum().setPictures(null);
            switchScene("CreateUserView", "LoginView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }
}

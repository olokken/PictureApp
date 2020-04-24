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

/**
 * Controller for create user view.
 *
 * @author Team 6
 * @version 2020.04.24
 */
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

    /**
     * Constructor that creates an instance of CreateUserView, initialising the instance.
     */
    public CreateUserView() throws IOException {
    }

    /**
     * Creates user and switches back to the login scene.
     */
    public void createUser() {
        try{
            if(checkUsername() && checkPassword() && checkEqualPasswords()) {
                if (userService.createUser(username.getText(), password.getText())) {
                    userService.createUser(username.getText(), password.getText());
                    switchToLogin();
                } else {
                    showInformationDialog("Username taken", "Please choose another username");
                }
            }
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Checks if the username is empty.
     * @return True if username isn't empty.
     */
    public boolean checkUsername(){
        if(username.getText().trim().equals("")){
            label.setText("Username can't be empty!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the password fields are empty.
     * @return True if password fields isn't empty.
     */
    public boolean checkPassword(){
        if(password.getText().trim().equals("") || confirmPassword.getText().trim().equals("")){
            label.setText("One or both passwordfields\nare empty!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if the password and confirm password are equal.
     * @return True if passwords are equal.
     */
    public boolean checkEqualPasswords(){
        if(password.getText().equals(confirmPassword.getText())){
            return true;
        } else {
            label.setText("Passwords doesn't match!");
            return false;
        }
    }

    /**
     * Switches to the Login view.
     */
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

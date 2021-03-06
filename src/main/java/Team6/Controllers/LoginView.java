package Team6.Controllers;

import Team6.App;
import Team6.entities.User;
import Team6.services.AppLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Team6.services.UserService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Controller for LoginView.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class LoginView extends Base implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    TextField password;
    @FXML
    TextField username;
    @FXML
    Label label;
    UserService userService = new UserService();



    /**
     * Constructor that creates an instance of LoginView, initialising the instance.
     */
    public LoginView() throws IOException {
    }

    /**
     * Initialize the login view.
     *
     * @param url The url.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImage();
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Switches to the create user view.
     */
    public void createNewUser() throws IOException {
        try{
            switchScene("LoginView", "CreateUserView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Sets the logo image for the login view.
     * If image can't be found, a
     * {@link FileNotFoundException} will be thrown.
     */
    void setImage() throws FileNotFoundException {
        try{
            Image image = new Image(new FileInputStream("./images/pickles.png"));
            imageView.setImage(image);
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    /**
     * Sign in the user and changes scene.
     */
    public void signIn() {
        try{
            if(checkUsername() && checkPassword()){
                User user = userService.login(username.getText(), password.getText());
                if (user != null) {
                    Context.getInstance().currentUser().setId(user.getId());
                    Context.getInstance().currentUser().setUsername(user.getUsername());
                    switchScene("LoginView", "MainView");
                }
                else {
                    label.setText("Username and/or password is wrong!");
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
     * Checks if the password field is empty.
     * @return True if password field isn't empty.
     */
    public boolean checkPassword(){
        if(password.getText().trim().equals("")){
            label.setText("Passwordfield can't be empty!");
            return false;
        } else {
            return true;
        }
    }


}

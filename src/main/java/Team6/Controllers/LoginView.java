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




    public LoginView() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImage();
        } catch (FileNotFoundException ex) {
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    public void createNewUser() throws IOException {
        try{
            switchScene("LoginView", "CreateUserView");
        } catch (IOException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

    void setImage() throws FileNotFoundException {
        try{
            Image image = new Image(new FileInputStream("./images/pickles.png"));
            imageView.setImage(image);
        } catch (FileNotFoundException ex){
            AppLogger.getAppLogger().log(Level.FINE, ex.getMessage());
            AppLogger.closeHandler();
        }
    }

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

    public boolean checkUsername(){
        if(username.getText().trim().equals("")){
            label.setText("Username can't be empty!");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPassword(){
        if(password.getText().trim().equals("")){
            label.setText("Passwordfield can't be empty!");
            return false;
        } else {
            return true;
        }
    }


}

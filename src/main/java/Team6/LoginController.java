package Team6;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import services.UserService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    ImageView imageView;
    @FXML
    TextField password;
    @FXML
    TextField username;
    @FXML
    Label label;
    UserService userService = new UserService();




    public LoginController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser(ActionEvent actionEvent) throws IOException {
        App.setRoot("createUser");
    }

    void setImage() throws FileNotFoundException {
        Image image = new Image(new FileInputStream(".\\images\\pickles.png"));
        imageView.setImage(image);
    }
    public void signIn(ActionEvent actionEvent) throws IOException {
        if(checkUsername() && checkPassword()){
            User user = userService.login(username.getText(), password.getText());
            if (user != null) {
                Context.getInstance().currentUser().setId(user.getId());
                Context.getInstance().currentUser().setUsername(user.getUsername());
                App.setRoot("primary");
            }
            else {
                label.setText("Username and/or password is wrong!");
            }
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

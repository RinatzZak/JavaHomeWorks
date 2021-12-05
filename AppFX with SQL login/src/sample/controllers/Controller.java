package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Database.DatabaseHandler;
import sample.User;
import sample.animations.Shake;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSighButton;

    @FXML
    private Button loginSighUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    void initialize(){
        authSighButton.setOnAction(event -> {
            String loginText = login_field.getText().trim();
            String passText = password_field.getText().trim();
            if (!loginText.equals("") && !passText.equals("")){
                loginUser(loginText, passText);
            } else {
                System.out.println("Вы не заполнили поля для логина и пароля");
            }
        });


        loginSighUpButton.setOnAction(event ->{
            openNewScene("/sample/views/sample.fxml");
        });
    }

    private void loginUser(String loginText, String passText)  {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUserName(loginText);
        user.setPassword(passText);
        ResultSet resSet = dbHandler.getUser(user);
        int counter = 0;
            try {
                while (resSet.next()) {
                    counter++;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        if (counter >= 1){
            openNewScene("/sample/views/app.fxml");
        } else {
            Shake userLoginAnim = new Shake(login_field);
            Shake userPassAnim = new Shake(password_field);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
        }
    }

    public void openNewScene(String window){
        loginSighUpButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}

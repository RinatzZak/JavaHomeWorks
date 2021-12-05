package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Database.DatabaseHandler;
import sample.User;

public class SighUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Button sighUpButton;

    @FXML
    private TextField sighUpCountry;

    @FXML
    private CheckBox sighUpFemale;

    @FXML
    private TextField sighUpLastName;

    @FXML
    private CheckBox sighUpMale;

    @FXML
    private TextField sighUpName;

    @FXML
    void initialize() {
        sighUpButton.setOnAction(event -> {
            sighUpNewUser();
        });

    }

    private void sighUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String firstName = sighUpName.getText();
        String lastName = sighUpLastName.getText();
        String userName = login_field.getText();
        String password = password_field.getText();
        String location = sighUpCountry.getText();
        String gender = "";

        if (sighUpMale.isSelected()){
            gender = "Мужской";
        } else if (sighUpFemale.isSelected()){
            gender = "Женский";
        }

        User user = new User(firstName, lastName, userName, password, location, gender);

        dbHandler.sighUpUser(user);
    }

}


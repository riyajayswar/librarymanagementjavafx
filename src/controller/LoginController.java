package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin")
                && password.equals("1234")) {

            try {

                Parent root =
                        FXMLLoader.load(
                                getClass().getResource("/view/books.fxml"));

                Stage stage = new Stage();

                stage.setTitle("Library Management System");

                stage.setScene(new Scene(root));

                stage.show();

                // CLOSE LOGIN WINDOW
                Stage current =
                        (Stage) usernameField.getScene().getWindow();

                current.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setContentText("Invalid Username or Password");

            alert.show();
        }
    }
}
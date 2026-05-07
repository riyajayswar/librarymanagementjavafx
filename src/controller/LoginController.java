package controller;

import javafx.collections.FXCollections;
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
    private ComboBox<String> roleBox;

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {

        roleBox.setItems(FXCollections.observableArrayList(
                "Admin",
                "Student"
        ));

        roleBox.setValue("Student");
    }

    @FXML
    private void handleLogin() {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleBox.getValue();

        try {

            // ADMIN LOGIN
            if(username.equals("admin")
                    && password.equals("admin123")
                    && role.equals("Admin")) {

                Parent root = FXMLLoader.load(
                        getClass().getResource("/view/admin.fxml"));

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Admin Dashboard");
                stage.show();
            }

            // STUDENT LOGIN
            else if(username.equals("student")
                    && password.equals("student123")
                    && role.equals("Student")) {

                Parent root = FXMLLoader.load(
                        getClass().getResource("/view/student.fxml"));

                Stage stage = (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Student Dashboard");
                stage.show();
            }

            else {
                messageLabel.setText("Invalid Credentials!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
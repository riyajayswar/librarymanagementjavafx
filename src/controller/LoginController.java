package controller;

import dao.StudentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Student;
import session.Session;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private Label messageLabel;

    private StudentDAO studentDAO = new StudentDAO();

    @FXML
    public void initialize() {

        roleBox.setItems(FXCollections.observableArrayList(
                "Admin",
                "Student"
        ));

        roleBox.setValue("Student");
    }

    // LOGIN
    @FXML
    private void handleLogin() {

        String email = usernameField.getText();

        String password = passwordField.getText();

        String role = roleBox.getValue();

        try {

            // ADMIN LOGIN
            if (email.equals("admin")
                    && password.equals("admin123")
                    && role.equals("Admin")) {

                Parent root = FXMLLoader.load(
                        getClass().getResource("/view/admin.fxml"));

                Stage stage =
                        (Stage) usernameField.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Admin Dashboard");
                stage.show();
            }

            // STUDENT LOGIN
            else if (role.equals("Student")) {

                Student student =
                        studentDAO.loginStudent(email, password);

                if (student != null) {

                    // SAVE SESSION
                    Session.setStudent(
                            student.getId(),
                            student.getName()
                    );

                    Parent root = FXMLLoader.load(
                            getClass().getResource("/view/student.fxml"));

                    Stage stage =
                            (Stage) usernameField.getScene().getWindow();

                    stage.setScene(new Scene(root));
                    stage.setTitle("Student Dashboard");
                    stage.show();

                } else {

                    messageLabel.setText(
                            "Invalid Student Credentials!");
                }
            }

            else {

                messageLabel.setText("Invalid Credentials!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REGISTER STUDENT
    @FXML
    private void handleRegister() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Student Registration");

        // FORM
        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(15);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter Password");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(passField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType registerButton =
                new ButtonType(
                        "Register",
                        ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(registerButton, ButtonType.CANCEL);

        dialog.showAndWait();

        // SAVE STUDENT
        if (dialog.getResult() == registerButton) {

            String name = nameField.getText();

            String email = emailField.getText();

            String password = passField.getText();

            if (name.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()) {

                showAlert("Please fill all fields.");
                return;
            }

            // REGISTER STUDENT
            studentDAO.addStudent(
                    name,
                    email,
                    password
            );

            showAlert("Student Registered Successfully!");
        }
    }

    // ALERT
    private void showAlert(String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Library Management System");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}
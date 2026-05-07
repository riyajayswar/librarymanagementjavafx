import dao.BookDAO;
import dao.StudentDAO;
import dao.IssueDAO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // CREATE DATABASE TABLES
        new BookDAO().createTable();
        new StudentDAO().createStudentTable();
        new IssueDAO().createIssueTable();

        // LOAD LOGIN PAGE
        Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package controller;


import dao.IssueDAO;
import dao.StudentDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Label;

import javafx.stage.Stage;

import model.Student;

import session.Session;



public class StudentProfileController {


    @FXML
    private Label nameLabel;


    @FXML
    private Label emailLabel;


    @FXML
    private Label idLabel;


    @FXML
    private Label issuedLabel;


    @FXML
    private Label returnedLabel;


    @FXML
    private Label fineLabel;



    private StudentDAO studentDAO =
            new StudentDAO();


    private IssueDAO issueDAO =
            new IssueDAO();



    @FXML
    public void initialize(){


        String email =
                Session.getUsername();


        Student student =
                studentDAO.getStudentByEmail(email);



        if(student != null){


            nameLabel.setText(
                    "Name : "
                    + student.getName());


            emailLabel.setText(
                    "Email : "
                    + student.getEmail());


            idLabel.setText(
                    "ID : "
                    + student.getId());


        }



        int fine =
                issueDAO.getStudentPendingFine(email);


        fineLabel.setText(
                "Pending Fine : ₹"
                + fine);

    }



    @FXML
    private void goBack(){


        try{


            Parent root =
                    FXMLLoader.load(
                    getClass()
                    .getResource(
                    "/view/student.fxml"));


            Stage stage =
                    (Stage)
                    nameLabel
                    .getScene()
                    .getWindow();



            stage.setScene(
                    new Scene(root));


        }
        catch(Exception e){

            e.printStackTrace();

        }

    }

}
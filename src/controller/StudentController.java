package controller;

import dao.BookDAO;
import dao.IssueDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Book;

public class StudentController {

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> statusColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label issuedBooksLabel;

    @FXML
    private Label availableBooksLabel;

    @FXML
    private Label fineLabel;

    private BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author"));

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        loadBooks();

        loadDashboardStats();

        fineLabel.setText("₹0");
    }

    // LOAD BOOKS
    private void loadBooks() {

        ObservableList<Book> books =
                bookDAO.getAllBooks();

        bookTable.setItems(books);
    }

    // DASHBOARD STATS
    private void loadDashboardStats() {

        totalBooksLabel.setText(
                String.valueOf(
                        bookDAO.getTotalBooks()));

        issuedBooksLabel.setText(
                String.valueOf(
                        bookDAO.getIssuedBooksCount()));

        availableBooksLabel.setText(
                String.valueOf(
                        bookDAO.getAvailableBooksCount()));
    }

    // SEARCH
    @FXML
    private void handleSearch() {

        String keyword =
                searchField.getText().toLowerCase();

        ObservableList<Book> allBooks =
                bookDAO.getAllBooks();

        ObservableList<Book> filteredBooks =
                FXCollections.observableArrayList();

        for (Book book : allBooks) {

            if (book.getTitle().toLowerCase().contains(keyword)
                    || book.getAuthor().toLowerCase().contains(keyword)) {

                filteredBooks.add(book);
            }
        }

        bookTable.setItems(filteredBooks);
    }

    // ISSUE BOOK
    @FXML
    private void handleIssueBook() {

        Book selectedBook =
                bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showAlert("Please select a book.");
            return;
        }

        if (selectedBook.getStatus().equals("Issued")) {

            showAlert("Book already issued.");
            return;
        }

        bookDAO.issueBook(selectedBook.getId());

        IssueDAO issueDAO = new IssueDAO();

        issueDAO.issueBook(
                "student",
                selectedBook.getTitle());

        showAlert("Book issued successfully!");

        loadBooks();

        loadDashboardStats();
    }

    // RETURN BOOK
    @FXML
    private void handleReturnBook() {

        Book selectedBook =
                bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showAlert("Please select a book.");
            return;
        }

        if (selectedBook.getStatus().equals("Available")) {

            showAlert("Book already available.");
            return;
        }

        IssueDAO issueDAO = new IssueDAO();

        int fine =
                issueDAO.calculateFine(
                        selectedBook.getTitle());

        bookDAO.returnBook(selectedBook.getId());

        issueDAO.deleteIssuedBook(
                selectedBook.getTitle());

        fineLabel.setText("₹" + fine);

        showAlert(
                "Book returned successfully!\nFine: ₹" + fine);

        loadBooks();

        loadDashboardStats();

        fineLabel.setText("₹0");
    }

    // SIDEBAR SEARCH
    @FXML
    private void goToSearch() {

        searchField.requestFocus();
    }

    // LOGOUT
    @FXML
    private void handleLogout() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/login.fxml"));

            Stage stage =
                    (Stage) bookTable.getScene().getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {

            e.printStackTrace();
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
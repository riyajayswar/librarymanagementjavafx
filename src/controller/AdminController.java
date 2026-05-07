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
import model.Issue;

public class AdminController {

    // BOOK TABLE
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

    // ISSUE TABLE
    @FXML
    private TableView<Issue> issueTable;

    @FXML
    private TableColumn<Issue, String> studentColumn;

    @FXML
    private TableColumn<Issue, String> bookColumn;

    @FXML
    private TableColumn<Issue, String> issueDateColumn;

    @FXML
    private TableColumn<Issue, String> dueDateColumn;

    @FXML
    private TableColumn<Issue, Integer> fineColumn;

    // DASHBOARD LABELS
    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label issuedBooksLabel;

    @FXML
    private Label availableBooksLabel;

    // INPUT FIELDS
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField searchField;

    private BookDAO bookDAO = new BookDAO();

    private IssueDAO issueDAO = new IssueDAO();

    // INITIALIZE
    @FXML
    public void initialize() {

        // BOOK TABLE
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author"));

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        // ISSUE TABLE
        studentColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentName"));

        bookColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle"));

        issueDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("issueDate"));

        dueDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dueDate"));

        fineColumn.setCellValueFactory(
                new PropertyValueFactory<>("fine"));

        loadBooks();

        loadIssuedBooks();

        loadDashboardStats();
    }

    // LOAD BOOKS
    private void loadBooks() {

        ObservableList<Book> list =
                bookDAO.getAllBooks();

        bookTable.setItems(list);
    }

    // LOAD ISSUED BOOKS
    private void loadIssuedBooks() {

        ObservableList<Issue> list =
                issueDAO.getAllIssuedBooks();

        issueTable.setItems(list);
    }

    // LOAD DASHBOARD STATS
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

    // ADD BOOK
    @FXML
    private void handleAddBook() {

        String title = titleField.getText();

        String author = authorField.getText();

        if (title.isEmpty() || author.isEmpty()) {

            showAlert("Please fill all fields.");
            return;
        }

        bookDAO.addBook(title, author);

        titleField.clear();
        authorField.clear();

        showAlert("Book Added Successfully!");

        loadBooks();

        loadDashboardStats();
    }

    // DELETE BOOK
    @FXML
    private void handleDeleteBook() {

        Book selectedBook =
                bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {

            showAlert("Select a book first.");
            return;
        }

        bookDAO.deleteBook(selectedBook.getId());

        showAlert("Book Deleted Successfully!");

        loadBooks();

        loadDashboardStats();
    }

    // SEARCH BOOK
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
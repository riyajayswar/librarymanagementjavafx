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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Book;
import model.Issue;
import session.Session;

public class StudentController {

    // ROOT PANE
    @FXML
    private BorderPane rootPane;

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
    private TableColumn<Book, String> categoryColumn;

    @FXML
    private TableColumn<Book, Integer> totalQuantityColumn;

    @FXML
    private TableColumn<Book, Integer> availableQuantityColumn;

    // HISTORY TABLE
    @FXML
    private TableView<Issue> historyTable;

    @FXML
    private TableColumn<Issue, String> historyBookColumn;

    @FXML
    private TableColumn<Issue, String> issueDateColumn;

    @FXML
    private TableColumn<Issue, String> dueDateColumn;

    @FXML
    private TableColumn<Issue, String> returnDateColumn;

    @FXML
    private TableColumn<Issue, Integer> fineColumn;

    // SEARCH
    @FXML
    private TextField searchField;

    // LABELS
    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label issuedBooksLabel;

    @FXML
    private Label availableBooksLabel;

    @FXML
    private Label fineLabel;

    @FXML
    private Label pendingFineLabel;

    @FXML
    private Label overdueBooksLabel;

    // THEME
    private boolean darkMode = false;

    // DAO
    private BookDAO bookDAO = new BookDAO();

    private IssueDAO issueDAO = new IssueDAO();

    // CURRENT STUDENT
    private String currentStudent;

    // INITIALIZE
    @FXML
    public void initialize() {

        // SESSION USER
        currentStudent = Session.getUsername();

        // BOOK TABLE
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        authorColumn.setCellValueFactory(
                new PropertyValueFactory<>("author"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        totalQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("totalQuantity"));

        availableQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("availableQuantity"));

        // HISTORY TABLE
        historyBookColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle"));

        issueDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("issueDate"));

        dueDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dueDate"));

        returnDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("returnDate"));

        fineColumn.setCellValueFactory(
                new PropertyValueFactory<>("fine"));

        loadBooks();

        loadHistory();

        loadDashboardStats();

        loadPendingFine();

        bookTable.getSelectionModel().setSelectionMode(
                SelectionMode.SINGLE
        );
    }

    // LOAD BOOKS
    private void loadBooks() {

        ObservableList<Book> books =
                bookDAO.getAllBooks();

        bookTable.setItems(books);
    }

    // LOAD HISTORY
    private void loadHistory() {

        ObservableList<Issue> history =
                issueDAO.getStudentHistory(currentStudent);

        historyTable.setItems(history);
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
        overdueBooksLabel.setText(
                String.valueOf(
                        issueDAO.getOverdueBooksCount()
                ));
    }

    // SEARCH BOOKS
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
                    || book.getAuthor().toLowerCase().contains(keyword)
                    || book.getCategory().toLowerCase().contains(keyword)) {

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

        // CHECK AVAILABILITY
        if (selectedBook.getAvailableQuantity() <= 0) {

            showAlert("Book not available.");
            return;
        }

        boolean issued =
                issueDAO.issueBook(
                        currentStudent,
                        selectedBook.getTitle()
                );

        if (issued) {

            showAlert("Book issued successfully!");

        } else {

            showAlert("Issue failed.");
        }

        loadBooks();

        loadDashboardStats();

        loadHistory();

        loadPendingFine();
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

        int fine =
                issueDAO.returnBook(
                        currentStudent,
                        selectedBook.getTitle()
                );

        if (fine == -1) {

            showAlert("You have not issued this book.");
            return;
        }

        showAlert(
                "Book returned successfully!\nFine: ₹" + fine
        );

        loadBooks();

        loadDashboardStats();

        loadHistory();

        loadPendingFine();
    }

    // GO TO SEARCH
    @FXML
    private void goToSearch() {

        searchField.requestFocus();
    }

    // TOGGLE THEME
    @FXML
    private void toggleTheme() {

        if (darkMode) {

            rootPane.getStyleClass().remove("dark-mode");

            if (!rootPane.getStyleClass().contains("light-mode")) {

                rootPane.getStyleClass().add("light-mode");
            }

        } else {

            rootPane.getStyleClass().remove("light-mode");

            if (!rootPane.getStyleClass().contains("dark-mode")) {

                rootPane.getStyleClass().add("dark-mode");
            }
        }

        darkMode = !darkMode;
    }

    // LOGOUT
    @FXML
    private void handleLogout() {

        try {

            // CLEAR SESSION
            Session.clearSession();

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

    private void loadPendingFine() {

        int fine =
                issueDAO.getStudentPendingFine(
                        currentStudent
                );

        pendingFineLabel.setText("₹" + fine);
    }
}
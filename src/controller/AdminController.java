package controller;

import dao.BookDAO;
import dao.IssueDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;

import model.Book;
import model.Issue;

public class AdminController {

    // ROOT
    @FXML
    private BorderPane rootPane;

    // THEME TOGGLE
    @FXML
    private ToggleButton themeToggle;

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

    @FXML
    private TableColumn<Issue, String> statusColumn;

    // OVERDUE TABLE
    @FXML
    private TableView<Issue> overdueTable;

    @FXML
    private TableColumn<Issue, String> overdueStudentColumn;

    @FXML
    private TableColumn<Issue, String> overdueBookColumn;

    @FXML
    private TableColumn<Issue, String> overdueDueDateColumn;

    @FXML
    private TableColumn<Issue, Integer> overdueFineColumn;

    // CHARTS
    @FXML
    private PieChart bookPieChart;

    @FXML
    private BarChart<String, Number> categoryChart;

    // DASHBOARD LABELS
    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label issuedBooksLabel;

    @FXML
    private Label availableBooksLabel;

    @FXML
    private Label overdueBooksLabel;

    // INPUT FIELDS
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField searchField;

    // DAO
    private BookDAO bookDAO =
            new BookDAO();

    private IssueDAO issueDAO =
            new IssueDAO();

    // INITIALIZE
    @FXML
    public void initialize() {

        // DEFAULT THEME
        if (!rootPane.getStyleClass().contains("light-mode")) {

                rootPane.getStyleClass().add("light-mode");
        }

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
        
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        statusColumn.setCellFactory(column ->
                new TableCell<Issue, String>() {

                @Override
                protected void updateItem(
                                String status,
                                boolean empty) {

                        super.updateItem(status, empty);

                        if (empty || status == null) {

                                setText(null);
                                setStyle("");

                        } else {

                                setText(status);

                                switch (status) {

                                        case "Issued":
                                        setStyle(
                                                "-fx-text-fill: #22c55e;" +
                                                "-fx-font-weight: bold;"
                                        );
                                        break;

                                        case "Overdue":
                                        setStyle(
                                                "-fx-text-fill: #ef4444;" +
                                                "-fx-font-weight: bold;"
                                        );
                                        break;

                                        case "Returned":
                                        setStyle(
                                                "-fx-text-fill: #94a3b8;" +
                                                "-fx-font-weight: bold;"
                                        );
                                        break;
                                }
                        }
                }
                
       });

       //OVERDUE 
       overdueStudentColumn.setCellValueFactory(
                new PropertyValueFactory<>("studentName"));

        overdueBookColumn.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle"));

        overdueDueDateColumn.setCellValueFactory(
                new PropertyValueFactory<>("dueDate"));

        overdueFineColumn.setCellValueFactory(
                new PropertyValueFactory<>("fine"));

        // LOAD DATA
        loadBooks();

        loadIssuedBooks();

        loadOverdueBooks();

        loadDashboardStats();

        loadPieChart();

        loadCategoryChart();

        bookTable.getSelectionModel()
                        .selectedItemProperty()
                        .addListener((obs, oldBook, selectedBook) -> {

                if (selectedBook != null) {

                        titleField.setText(
                                selectedBook.getTitle());

                        authorField.setText(
                                selectedBook.getAuthor());

                        categoryField.setText(
                                selectedBook.getCategory());

                        quantityField.setText(
                                String.valueOf(
                                        selectedBook.getTotalQuantity()));
                }
        });
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
                        bookDAO.getTotalBooks()
                ));

        issuedBooksLabel.setText(
                String.valueOf(
                        bookDAO.getIssuedBooksCount()
                ));

        availableBooksLabel.setText(
                String.valueOf(
                        bookDAO.getAvailableBooksCount()
                ));
        overdueBooksLabel.setText(
                String.valueOf(
                        issueDAO.getOverdueBooksCount()
                ));
    }

    // PIE CHART
    private void loadPieChart() {

        bookPieChart.getData().clear();

        int issued =
                bookDAO.getIssuedBooksCount();

        int available =
                bookDAO.getAvailableBooksCount();

        ObservableList<PieChart.Data> pieData =
                FXCollections.observableArrayList(

                        new PieChart.Data(
                                "Issued",
                                issued
                        ),

                        new PieChart.Data(
                                "Available",
                                available
                        )
                );

        bookPieChart.setData(pieData);

        bookPieChart.setTitle(
                "Library Analytics"
        );
    }

    // CATEGORY CHART
    private void loadCategoryChart() {

        categoryChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        series.setName("Books by Category");

        ObservableList<Book> books =
                bookDAO.getAllBooks();

        for (Book book : books) {

            series.getData().add(
                    new XYChart.Data<>(
                            book.getCategory(),
                            book.getTotalQuantity()
                    )
            );
        }

        categoryChart.getData().add(series);
    }

    // ADD BOOK
    @FXML
    private void handleAddBook() {

        String title =
                titleField.getText();

        String author =
                authorField.getText();

        String category =
                categoryField.getText();

        String quantityText =
                quantityField.getText();

        if (title.isEmpty()
                || author.isEmpty()
                || category.isEmpty()
                || quantityText.isEmpty()) {

            showAlert(
                    "Please fill all fields."
            );

            return;
        }

        int quantity;

        try {

            quantity =
                    Integer.parseInt(
                            quantityText
                    );

        } catch (Exception e) {

            showAlert(
                    "Quantity must be a number."
            );

            return;
        }

        // ADD BOOK
        bookDAO.addBook(
                title,
                author,
                category,
                quantity
        );

        // CLEAR FIELDS
        titleField.clear();

        authorField.clear();

        categoryField.clear();

        quantityField.clear();

        showAlert(
                "Book Added Successfully!"
        );

        // RELOAD
        loadBooks();

        loadDashboardStats();

        loadPieChart();

        loadCategoryChart();

        loadIssuedBooks();

        loadOverdueBooks();
    }

    //UPDATE BOOK
    @FXML
    private void handleUpdateBook() {

        Book selectedBook =
                bookTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedBook == null) {

                showAlert(
                        "Select a book first."
                );

                return;
        }

        try {

                int quantity =
                        Integer.parseInt(
                                quantityField.getText());

                bookDAO.updateBook(
                        selectedBook.getId(),
                        titleField.getText(),
                        authorField.getText(),
                        categoryField.getText(),
                        quantity
                );

                showAlert(
                        "Book Updated Successfully!"
                );

                loadBooks();

                loadDashboardStats();

                loadCategoryChart();

                loadPieChart();

                loadOverdueBooks();

        } catch (Exception e) {

                showAlert(
                        "Invalid quantity."
                );
        }
    }

    // DELETE BOOK
    @FXML
    private void handleDeleteBook() {

        Book selectedBook =
                bookTable.getSelectionModel()
                        .getSelectedItem();

        if (selectedBook == null) {

            showAlert(
                    "Select a book first."
            );

            return;
        }

        bookDAO.deleteBook(
                selectedBook.getId()
        );

        showAlert(
                "Book Deleted Successfully!"
        );

        loadBooks();

        loadDashboardStats();

        loadPieChart();

        loadCategoryChart();

        loadIssuedBooks();

        loadOverdueBooks();
    }

    // SEARCH BOOK
    @FXML
    private void handleSearch() {

        String keyword =
                searchField.getText()
                        .toLowerCase();

        ObservableList<Book> allBooks =
                bookDAO.getAllBooks();

        ObservableList<Book> filteredBooks =
                FXCollections.observableArrayList();

        for (Book book : allBooks) {

            if (book.getTitle()
                    .toLowerCase()
                    .contains(keyword)

                    || book.getAuthor()
                    .toLowerCase()
                    .contains(keyword)

                    || book.getCategory()
                    .toLowerCase()
                    .contains(keyword)) {

                filteredBooks.add(book);
            }
        }

        bookTable.setItems(filteredBooks);
    }

    //LOAD OVERDUE BOOKS
    private void loadOverdueBooks() {

        ObservableList<Issue> overdueBooks =
                issueDAO.getOverdueBooks();

        overdueTable.setItems(overdueBooks);
    }

    // DARK MODE TOGGLE
    
    @FXML
    private void toggleTheme() {

        ObservableList<String> styles =
                rootPane.getStyleClass();

        // REMOVE BOTH FIRST
        styles.remove("light-mode");
        styles.remove("dark-mode");

        // APPLY NEW THEME
        if (themeToggle.isSelected()) {

                styles.add("dark-mode");

                themeToggle.setText("☀ Light Mode");

        } else {

                styles.add("light-mode");

                themeToggle.setText("🌙 Dark Mode");
        }

        // REFRESH UI
        bookTable.refresh();

        issueTable.refresh();

        overdueTable.refresh();

        rootPane.applyCss();

        rootPane.layout();
   }

    // LOGOUT
    @FXML
    private void handleLogout() {

        try {

            Parent root =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/view/login.fxml"
                            )
                    );

            Stage stage =
                    (Stage) bookTable
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ALERT
    private void showAlert(String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Library Management System"
        );

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}
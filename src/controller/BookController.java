package controller;

import dao.BookDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Book;

public class BookController {

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Integer> colId;

    @FXML
    private TableColumn<Book, String> colTitle;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableColumn<Book, String> colStatus;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField searchField;

    private BookDAO bookDAO = new BookDAO();

    // INITIALIZE
    @FXML
    public void initialize() {

        // CREATE TABLE
        bookDAO.createTable();

        // TABLE COLUMNS
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getId()
                ).asObject());

        colTitle.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTitle()));

        colAuthor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAuthor()));

        colStatus.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getStatus()));

        loadBooks();
    }

    // LOAD BOOKS
    private void loadBooks() {

        ObservableList<Book> list =
                bookDAO.getAllBooks();

        bookTable.setItems(list);
    }

    // ADD BOOK
    @FXML
    private void handleAddBook() {

        String title = titleField.getText();
        String author = authorField.getText();

        if (title.isEmpty() || author.isEmpty()) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setContentText("Fill all fields");
            alert.show();

            return;
        }

        bookDAO.addBook(title, author);

        loadBooks();

        titleField.clear();
        authorField.clear();
    }

    // DELETE BOOK
    @FXML
    private void handleDeleteBook() {

        Book selected =
                bookTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setContentText("Select a book");
            alert.show();

            return;
        }

        bookDAO.deleteBook(selected.getId());

        loadBooks();
    }

    // ISSUE BOOK
    @FXML
    private void handleIssueBook() {

        Book selected =
                bookTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setContentText("Select a book");
            alert.show();

            return;
        }

        bookDAO.issueBook(selected.getId());

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setContentText("Book Issued Successfully");
        alert.show();

        loadBooks();
    }

    // RETURN BOOK
    @FXML
    private void handleReturnBook() {

        Book selected =
                bookTable.getSelectionModel().getSelectedItem();

        if (selected == null) {

            Alert alert =
                    new Alert(Alert.AlertType.WARNING);

            alert.setContentText("Select a book");
            alert.show();

            return;
        }

        long fine =
                bookDAO.returnBook(selected.getId());

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Book Returned");

        alert.setContentText(
                "Fine = ₹" + fine
        );

        alert.show();

        loadBooks();
    }

    // SEARCH BOOK
    @FXML
    private void handleSearch() {

        String keyword =
                searchField.getText();

        ObservableList<Book> list =
                bookDAO.searchBooks(keyword);

        bookTable.setItems(list);
    }
}
package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;

import java.sql.*;

public class BookDAO {

    // CREATE TABLE
    public void createTable() {

        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "status TEXT DEFAULT 'Available')";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL BOOKS
    public ObservableList<Book> getAllBooks() {

        ObservableList<Book> list =
                FXCollections.observableArrayList();

        String sql = "SELECT * FROM books";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ADD BOOK
    public void addBook(String title, String author) {

        String sql =
                "INSERT INTO books(title, author, status) " +
                "VALUES (?, ?, 'Available')";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE BOOK
    public void deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public void issueBook(int id) {

        String sql =
                "UPDATE books SET status = 'Issued' WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RETURN BOOK
    public void returnBook(int id) {

        String sql =
                "UPDATE books SET status = 'Available' WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TOTAL BOOKS
    public int getTotalBooks() {

        int count = 0;

        String sql = "SELECT COUNT(*) FROM books";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {

                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // ISSUED BOOKS COUNT
    public int getIssuedBooksCount() {

        int count = 0;

        String sql =
                "SELECT COUNT(*) FROM books " +
                "WHERE status='Issued'";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {

                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    // AVAILABLE BOOKS COUNT
    public int getAvailableBooksCount() {

        int count = 0;

        String sql =
                "SELECT COUNT(*) FROM books " +
                "WHERE status='Available'";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {

                count = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
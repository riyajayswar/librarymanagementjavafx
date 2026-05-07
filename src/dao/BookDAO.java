package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;

import java.sql.*;

public class BookDAO {

    // CREATE TABLE
    public void createTable() {

        String sql =
                "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL," +
                "status TEXT DEFAULT 'Available'," +
                "issueDate TEXT)";

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
                "INSERT INTO books(title, author, status) VALUES (?, ?, 'Available')";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE BOOK
    public void deleteBook(int id) {

        String sql =
                "DELETE FROM books WHERE id=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public void issueBook(int id) {

        String sql =
                "UPDATE books SET status='Issued', issueDate=? WHERE id=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String today =
                    java.time.LocalDate.now().toString();

            ps.setString(1, today);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // RETURN BOOK + FINE
    public long returnBook(int id) {

        long fine = 0;

        try (Connection conn = DBConnection.connect()) {

            String getSql =
                    "SELECT issueDate FROM books WHERE id=?";

            PreparedStatement ps =
                    conn.prepareStatement(getSql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String issueDate =
                        rs.getString("issueDate");

                if (issueDate != null) {

                    java.time.LocalDate issue =
                            java.time.LocalDate.parse(issueDate);

                    java.time.LocalDate today =
                            java.time.LocalDate.now();

                    long days =
                            java.time.temporal.ChronoUnit.DAYS
                                    .between(issue, today);

                    if (days > 7) {

                        fine = (days - 7) * 5;
                    }
                }
            }

            String updateSql =
                    "UPDATE books SET status='Available', issueDate=NULL WHERE id=?";

            PreparedStatement ps2 =
                    conn.prepareStatement(updateSql);

            ps2.setInt(1, id);

            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fine;
    }

    // SEARCH BOOKS
    public ObservableList<Book> searchBooks(String keyword) {

        ObservableList<Book> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT * FROM books WHERE title LIKE ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

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
}
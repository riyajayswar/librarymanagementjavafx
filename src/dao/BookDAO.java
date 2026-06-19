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
                        "category TEXT NOT NULL," +
                        "total_quantity INTEGER NOT NULL," +
                        "available_quantity INTEGER NOT NULL)";

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

        String sql =
                "SELECT * FROM books ORDER BY id DESC";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("total_quantity"),
                        rs.getInt("available_quantity")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ADD BOOK
    public void addBook(String title,
                        String author,
                        String category,
                        int quantity) {

        String sql =
                "INSERT INTO books(title, author, category, total_quantity, available_quantity) " +
                        "VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, category);
            ps.setInt(4, quantity);
            ps.setInt(5, quantity);

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
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public boolean issueBook(int id) {

        String sql =
                "UPDATE books " +
                        "SET available_quantity = available_quantity - 1 " +
                        "WHERE id=? AND available_quantity > 0";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows =
                    ps.executeUpdate();

            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // RETURN BOOK
    public void returnBook(int id) {

        String sql =
                "UPDATE books " +
                        "SET available_quantity = available_quantity + 1 " +
                        "WHERE id=?";

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

        String sql =
                "SELECT SUM(total_quantity) FROM books";

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

    // ISSUED BOOKS
    public int getIssuedBooksCount() {

        int count = 0;

        String sql =
                "SELECT SUM(total_quantity - available_quantity) FROM books";

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

    // AVAILABLE BOOKS
    public int getAvailableBooksCount() {

        int count = 0;

        String sql =
                "SELECT SUM(available_quantity) FROM books";

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

    //Update Book
   public void updateBook(
        int id,
        String title,
        String author,
        String category,
        int quantity) {

    String sql =
            "UPDATE books " +
            "SET title=?, " +
            "author=?, " +
            "category=?, " +
            "total_quantity=?, " +
            "available_quantity=? " +
            "WHERE id=?";

    try (Connection conn =
                 DBConnection.connect();

         PreparedStatement ps =
                 conn.prepareStatement(sql)) {

        ps.setString(1, title);
        ps.setString(2, author);
        ps.setString(3, category);
        ps.setInt(4, quantity);

        // Reset available quantity
        ps.setInt(5, quantity);

        ps.setInt(6, id);

        ps.executeUpdate();

    } catch (Exception e) {

        e.printStackTrace();
    }
 }
}
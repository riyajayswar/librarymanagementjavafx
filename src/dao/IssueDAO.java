package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Issue;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueDAO {

    // CREATE TABLE
    public void createIssueTable() {

        String sql = "CREATE TABLE IF NOT EXISTS issued_books (" +
                "issue_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_name TEXT NOT NULL," +
                "book_title TEXT NOT NULL," +
                "issue_date TEXT," +
                "due_date TEXT," +
                "fine INTEGER DEFAULT 0)";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public void issueBook(String studentName, String bookTitle) {

        String checkSql =
                "SELECT * FROM issued_books WHERE book_title = ?";

        String insertSql =
                "INSERT INTO issued_books(student_name, book_title, issue_date, due_date, fine) " +
                "VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect()) {

            // CHECK DUPLICATE
            PreparedStatement checkPs =
                    conn.prepareStatement(checkSql);

            checkPs.setString(1, bookTitle);

            ResultSet rs = checkPs.executeQuery();

            // IF ALREADY EXISTS
            if (rs.next()) {

                return;
            }

            // INSERT NEW RECORD
            PreparedStatement ps =
                    conn.prepareStatement(insertSql);

            LocalDate issueDate =
                    LocalDate.now();

            // DUE AFTER 7 DAYS
            LocalDate dueDate =
                    issueDate.plusDays(7);

            ps.setString(1, studentName);
            ps.setString(2, bookTitle);
            ps.setString(3, issueDate.toString());
            ps.setString(4, dueDate.toString());
            ps.setInt(5, 0);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL ISSUED BOOKS
    public ObservableList<Issue> getAllIssuedBooks() {

        ObservableList<Issue> list =
                FXCollections.observableArrayList();

        String sql = "SELECT * FROM issued_books";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new Issue(
                        rs.getInt("issue_id"),
                        rs.getString("student_name"),
                        rs.getString("book_title"),
                        rs.getString("issue_date"),
                        rs.getString("due_date"),
                        rs.getInt("fine")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // CALCULATE FINE
    public int calculateFine(String bookTitle) {

        int fine = 0;

        String sql =
                "SELECT due_date FROM issued_books WHERE book_title = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, bookTitle);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dueDateStr =
                        rs.getString("due_date");

                LocalDate dueDate =
                        LocalDate.parse(dueDateStr);

                LocalDate today =
                        LocalDate.now();

                // OVERDUE CHECK
                if (today.isAfter(dueDate)) {

                    long daysLate =
                            ChronoUnit.DAYS.between(
                                    dueDate,
                                    today
                            );

                    fine = (int) daysLate * 10;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fine;
    }

    // DELETE ISSUE RECORD
    public void deleteIssuedBook(String bookTitle) {

        String sql =
                "DELETE FROM issued_books WHERE book_title = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, bookTitle);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
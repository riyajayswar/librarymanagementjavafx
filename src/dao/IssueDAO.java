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

        String sql =
                "CREATE TABLE IF NOT EXISTS issued_books (" +
                        "issue_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "student_name TEXT NOT NULL," +
                        "book_title TEXT NOT NULL," +
                        "issue_date TEXT," +
                        "due_date TEXT," +
                        "return_date TEXT," +
                        "fine INTEGER DEFAULT 0)";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ISSUE BOOK
    public boolean issueBook(String studentName,
                             String bookTitle) {

        String checkBookSql =
                "SELECT available_quantity " +
                        "FROM books " +
                        "WHERE title = ?";

        String checkAlreadyIssuedSql =
                "SELECT * FROM issued_books " +
                        "WHERE LOWER(student_name)=LOWER(?) " +
                        "AND book_title=? " +
                        "AND return_date='Not Returned'";

        String insertSql =
                "INSERT INTO issued_books(" +
                        "student_name, " +
                        "book_title, " +
                        "issue_date, " +
                        "due_date, " +
                        "return_date, " +
                        "fine) " +
                        "VALUES(?, ?, ?, ?, ?, ?)";

        String updateBookSql =
                "UPDATE books " +
                        "SET available_quantity = available_quantity - 1 " +
                        "WHERE title = ?";

        try (Connection conn = DBConnection.connect()) {

            // CHECK BOOK AVAILABILITY
            PreparedStatement checkBookPs =
                    conn.prepareStatement(checkBookSql);

            checkBookPs.setString(1, bookTitle);

            ResultSet bookRs =
                    checkBookPs.executeQuery();

            if (bookRs.next()) {

                int available =
                        bookRs.getInt("available_quantity");

                if (available <= 0) {

                    return false;
                }
            }

            // CHECK IF STUDENT ALREADY ISSUED SAME BOOK
            PreparedStatement alreadyPs =
                    conn.prepareStatement(
                            checkAlreadyIssuedSql);

            alreadyPs.setString(1, studentName);

            alreadyPs.setString(2, bookTitle);

            ResultSet alreadyRs =
                    alreadyPs.executeQuery();

            if (alreadyRs.next()) {

                return false;
            }

            // ISSUE DATE
            LocalDate issueDate =
                    LocalDate.now();

            // DUE DATE
            LocalDate dueDate =
                    issueDate.plusDays(7);

            // INSERT ISSUE RECORD
            PreparedStatement insertPs =
                    conn.prepareStatement(insertSql);

            insertPs.setString(1, studentName);

            insertPs.setString(2, bookTitle);

            insertPs.setString(
                    3,
                    issueDate.toString()
            );

            insertPs.setString(
                    4,
                    dueDate.toString()
            );

            insertPs.setString(
                    5,
                    "Not Returned"
            );

            insertPs.setInt(6, 0);

            insertPs.executeUpdate();

            // UPDATE BOOK QUANTITY
            PreparedStatement updatePs =
                    conn.prepareStatement(updateBookSql);

            updatePs.setString(1, bookTitle);

            updatePs.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // RETURN BOOK
    public int returnBook(String studentName,
                          String bookTitle) {

        int fine = 0;

        String checkSql =
                "SELECT * FROM issued_books " +
                        "WHERE LOWER(student_name)=LOWER(?) " +
                        "AND book_title=? " +
                        "AND return_date='Not Returned'";

        String updateIssueSql =
                "UPDATE issued_books " +
                        "SET return_date=?, fine=? " +
                        "WHERE LOWER(student_name)=LOWER(?) " +
                        "AND book_title=? " +
                        "AND return_date='Not Returned'";

        String updateBookSql =
                "UPDATE books " +
                        "SET available_quantity = available_quantity + 1 " +
                        "WHERE title=?";

        try (Connection conn = DBConnection.connect()) {

            // CHECK IF STUDENT ISSUED BOOK
            PreparedStatement checkPs =
                    conn.prepareStatement(checkSql);

            checkPs.setString(1, studentName);

            checkPs.setString(2, bookTitle);

            ResultSet rs =
                    checkPs.executeQuery();

            // BOOK NOT ISSUED
            if (!rs.next()) {

                return -1;
            }

            // GET DUE DATE
            String dueDateStr =
                    rs.getString("due_date");

            LocalDate dueDate =
                    LocalDate.parse(dueDateStr);

            LocalDate today =
                    LocalDate.now();

            // CALCULATE FINE
            if (today.isAfter(dueDate)) {

                long lateDays =
                        ChronoUnit.DAYS.between(
                                dueDate,
                                today
                        );

                fine = (int) lateDays * 10;
            }

            // UPDATE ISSUE RECORD
            PreparedStatement issuePs =
                    conn.prepareStatement(updateIssueSql);

            issuePs.setString(
                    1,
                    today.toString()
            );

            issuePs.setInt(2, fine);

            issuePs.setString(3, studentName);

            issuePs.setString(4, bookTitle);

            issuePs.executeUpdate();

            // UPDATE BOOK QUANTITY
            PreparedStatement bookPs =
                    conn.prepareStatement(updateBookSql);

            bookPs.setString(1, bookTitle);

            bookPs.executeUpdate();

            return fine;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }

    // CALCULATE FINE
    public int calculateFine(String bookTitle) {

        int fine = 0;

        String sql =
                "SELECT due_date FROM issued_books " +
                        "WHERE book_title=? " +
                        "AND return_date='Not Returned'";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, bookTitle);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                String dueDateStr =
                        rs.getString("due_date");

                LocalDate dueDate =
                        LocalDate.parse(dueDateStr);

                LocalDate today =
                        LocalDate.now();

                if (today.isAfter(dueDate)) {

                    long lateDays =
                            ChronoUnit.DAYS.between(
                                    dueDate,
                                    today
                            );

                    fine = (int) lateDays * 10;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return fine;
    }

    // GET ALL ISSUED BOOKS
    public ObservableList<Issue> getAllIssuedBooks() {

        ObservableList<Issue> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT * FROM issued_books " +
                        "ORDER BY issue_id DESC";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String dueDate =
                        rs.getString("due_date");

                String returnDate =
                        rs.getString("return_date");

                list.add(new Issue(
                        rs.getInt("issue_id"),
                        rs.getString("student_name"),
                        rs.getString("book_title"),
                        rs.getString("issue_date"),
                        dueDate,
                        returnDate,
                        rs.getInt("fine"),
                        getStatus(dueDate, returnDate)
                ));
                }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // GET ACTIVE ISSUED BOOKS
    public ObservableList<Issue> getActiveIssuedBooks() {

        ObservableList<Issue> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT * FROM issued_books " +
                        "WHERE return_date='Not Returned'";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String dueDate =
                        rs.getString("due_date");

                String returnDate =
                        rs.getString("return_date");

                list.add(new Issue(
                        rs.getInt("issue_id"),
                        rs.getString("student_name"),
                        rs.getString("book_title"),
                        rs.getString("issue_date"),
                        dueDate,
                        returnDate,
                        rs.getInt("fine"),
                        getStatus(dueDate, returnDate)
                ));
           }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // GET STUDENT HISTORY
    public ObservableList<Issue> getStudentHistory(
            String studentName) {

        ObservableList<Issue> list =
                FXCollections.observableArrayList();

        String sql =
        "SELECT * FROM issued_books " +
        "WHERE LOWER(student_name)=LOWER(?) " +
        "ORDER BY issue_id DESC";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql)) {

            ps.setString(1, studentName);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                String dueDate =
                        rs.getString("due_date");

                String returnDate =
                        rs.getString("return_date");

                list.add(new Issue(
                        rs.getInt("issue_id"),
                        rs.getString("student_name"),
                        rs.getString("book_title"),
                        rs.getString("issue_date"),
                        dueDate,
                        returnDate,
                        rs.getInt("fine"),
                        getStatus(dueDate, returnDate)
                ));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }
    // STUDENT PENDING FINE
    public int getStudentPendingFine(String studentName) {

        int totalFine = 0;

        String sql =
                "SELECT due_date " +
                "FROM issued_books " +
                "WHERE LOWER(student_name)=LOWER(?) " +
                "AND return_date='Not Returned'";

        try (Connection conn = DBConnection.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)) {

                ps.setString(1, studentName);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                        LocalDate dueDate =
                                LocalDate.parse(
                                        rs.getString("due_date")
                                );

                        LocalDate today =
                                LocalDate.now();

                if (today.isAfter(dueDate)) {

                        long lateDays =
                                ChronoUnit.DAYS.between(
                                        dueDate,
                                        today
                                );

                        totalFine += lateDays * 10;
                        }
                }

        } catch (Exception e) {

                e.printStackTrace();
        }

        return totalFine;
    }
    //Status
    private String getStatus(String dueDate,
                         String returnDate) {

        if (!returnDate.equals("Not Returned")) {

                return "Returned";
        }

        if (LocalDate.now()
                .isAfter(LocalDate.parse(dueDate))) {

                return "Overdue";
        }

        return "Issued";
    }
    //Overdue Books Count
    public int getOverdueBooksCount() {

        int count = 0;

        String sql =
                "SELECT COUNT(*) FROM issued_books " +
                "WHERE return_date='Not Returned' " +
                "AND due_date < ?";

        try (Connection conn = DBConnection.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)) {

                ps.setString(
                        1,
                        LocalDate.now().toString()
                );

                ResultSet rs =
                        ps.executeQuery();

                if (rs.next()) {

                        count = rs.getInt(1);
                }

        } catch (Exception e) {

                e.printStackTrace();
        }

        return count;
   }
   // GET OVERDUE BOOKS
   public ObservableList<Issue> getOverdueBooks() {

        ObservableList<Issue> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT * FROM issued_books " +
                "WHERE return_date='Not Returned'";

        try (Connection conn = DBConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

                LocalDate today = LocalDate.now();

                while (rs.next()) {

                        LocalDate dueDate =
                                LocalDate.parse(
                                        rs.getString("due_date"));

                        if (today.isAfter(dueDate)) {

                                long lateDays =
                                        ChronoUnit.DAYS.between(
                                                dueDate,
                                                today);

                                int fine =
                                        (int) lateDays * 10;

                                list.add(new Issue(
                                        rs.getInt("issue_id"),
                                        rs.getString("student_name"),
                                        rs.getString("book_title"),
                                        rs.getString("issue_date"),
                                        rs.getString("due_date"),
                                        rs.getString("return_date"),
                                        fine,
                                        "Overdue"
                                ));
                        }
                }

        } catch (Exception e) {
                e.printStackTrace();
        }

        return list;
   }
}

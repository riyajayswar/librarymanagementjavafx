package model;

public class Issue {

    private int issueId;

    private String studentName;

    private String bookTitle;

    private String issueDate;

    private String dueDate;

    private String returnDate;

    private String status;

    private int fine;

    public Issue(int issueId,
                 String studentName,
                 String bookTitle,
                 String issueDate,
                 String dueDate,
                 String returnDate,
                 int fine,
                 String status){

        this.issueId = issueId;
        this.studentName = studentName;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
        this.status = status;
    }

    public int getIssueId() {
        return issueId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public int getFine() {
        return fine;
    }
    public String getStatus() {
        return status;
    }
}
package model;

public class Issue {

    private int issueId;
    private String studentName;
    private String bookTitle;
    private String issueDate;
    private String dueDate;
    private int fine;

    public Issue(int issueId,
                 String studentName,
                 String bookTitle,
                 String issueDate,
                 String dueDate,
                 int fine) {

        this.issueId = issueId;
        this.studentName = studentName;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fine = fine;
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

    public int getFine() {
        return fine;
    }
}
package session;

public class Session {

    private static int studentId;

    private static String username;

    // SET SESSION
    public static void setStudent(
            int id,
            String name
    ) {

        studentId = id;

        username = name;
    }

    // GET ID
    public static int getStudentId() {

        return studentId;
    }

    // GET USERNAME
    public static String getUsername() {

        return username;
    }

    // CLEAR SESSION
    public static void clearSession() {

        studentId = 0;

        username = null;
    }
}
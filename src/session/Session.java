package session;

public class Session {

    private static int studentId;

    private static String studentName;

    private static String studentEmail;


    // SET SESSION
    public static void setStudent(
            int id,
            String name,
            String email
    ){

        studentId = id;

        studentName = name;

        studentEmail = email;
    }


    // GET ID
    public static int getStudentId(){

        return studentId;
    }


    // GET NAME
    public static String getUsername(){

        return studentName;
    }


    // GET EMAIL
    public static String getEmail(){

        return studentEmail;
    }


    // CLEAR
    public static void clearSession(){

        studentId = 0;

        studentName = null;

        studentEmail = null;
    }
}
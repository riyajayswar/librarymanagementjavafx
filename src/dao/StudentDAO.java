package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Student;

import java.sql.*;

public class StudentDAO {

    // CREATE TABLE
    public void createStudentTable() {

        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // REGISTER STUDENT
    public void addStudent(String name,
                           String email,
                           String password) {

        String sql =
                "INSERT INTO students(name, email, password) VALUES(?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOGIN STUDENT
    public Student loginStudent(String email,
                                String password) {

        String sql =
                "SELECT * FROM students WHERE email=? AND password=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // GET ALL STUDENTS
    public ObservableList<Student> getAllStudents() {

        ObservableList<Student> list =
                FXCollections.observableArrayList();

        String sql = "SELECT * FROM students";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // DELETE STUDENT
    public void deleteStudent(int id) {

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // GET STUDENT PROFILE
    public Student getStudentProfile(String email) {

        String sql =
                "SELECT * FROM students WHERE email=?";

        try (Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    //student profile
    public Student getStudentByEmail(String email){

        String sql =
                "SELECT * FROM students WHERE email=?";


        try(Connection conn =
                DBConnection.connect();

            PreparedStatement ps =
                conn.prepareStatement(sql)){


            ps.setString(1,email);


            ResultSet rs =
                    ps.executeQuery();



            if(rs.next()){


                return new Student(

                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );

            }


        }
        catch(Exception e){

            e.printStackTrace();

        }


        return null;

    }
    // UPDATE STUDENT PROFILE
    public void updateStudent(int id,
                          String name,
                          String email,
                          String password) {


        String sql =
                "UPDATE students SET name=?, email=?, password=? WHERE id=?";


        try(Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){


            ps.setString(1, name);

            ps.setString(2, email);

            ps.setString(3, password);

            ps.setInt(4, id);


            ps.executeUpdate();


        }catch(Exception e){

            e.printStackTrace();

        }

    }

    //new
    public Student getStudentById(int id){

        String sql =
                "SELECT * FROM students WHERE id=?";


        try(Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){


            ps.setInt(1,id);


            ResultSet rs = ps.executeQuery();


            if(rs.next()){

                return new Student(

                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")

                );
            }


        }catch(Exception e){

            e.printStackTrace();

        }


        return null;

    }

    //PASSWORD VERIFICATION
    public boolean checkPassword(int id, String password){

        String sql =
                "SELECT * FROM students WHERE id=? AND password=?";


        try(Connection conn = DBConnection.connect();
            PreparedStatement ps = conn.prepareStatement(sql)){


            ps.setInt(1,id);

            ps.setString(2,password);


            ResultSet rs = ps.executeQuery();


            return rs.next();


        }catch(Exception e){

            e.printStackTrace();

        }


        return false;
    }
}
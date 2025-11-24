package gpaapp;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:courses.db";

    static {

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSql = """
                    CREATE TABLE IF NOT EXISTS courses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        course_name   TEXT NOT NULL,
                        course_code   TEXT NOT NULL,
                        credit        REAL NOT NULL,
                        teacher1      TEXT,
                        teacher2      TEXT,
                        grade         TEXT,
                        student_roll  TEXT,
                        student_name  TEXT
                    )
                    """;

            stmt.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static ObservableList<Course> fetchAllCourses() throws SQLException {
        ObservableList<Course> list = FXCollections.observableArrayList();

        String sql = "SELECT id, course_name, course_code, credit, teacher1, teacher2, grade, student_roll, student_name FROM courses";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Course c = new Course(
                        rs.getInt("id"),
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        rs.getDouble("credit"),
                        rs.getString("teacher1"),
                        rs.getString("teacher2"),
                        rs.getString("grade"),
                        rs.getString("student_roll"),
                        rs.getString("student_name")
                );
                list.add(c);
            }
        }

        return list;
    }

    public static int insertCourse(Course c) throws SQLException {
        String sql = "INSERT INTO courses(course_name, course_code, credit, teacher1, teacher2, grade, student_roll, student_name) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setDouble(3, c.getCredit());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getGrade());
            ps.setString(7, c.getStudentRoll());
            ps.setString(8, c.getStudentName());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public static void updateCourse(Course c) throws SQLException {
        if (c.getId() == null) {
            throw new IllegalArgumentException("Course ID is null. Cannot update record without ID.");
        }

        String sql = "UPDATE courses SET course_name=?, course_code=?, credit=?, teacher1=?, teacher2=?, grade=?, student_roll=?, student_name=? " +
                "WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getCode());
            ps.setDouble(3, c.getCredit());
            ps.setString(4, c.getTeacher1());
            ps.setString(5, c.getTeacher2());
            ps.setString(6, c.getGrade());
            ps.setString(7, c.getStudentRoll());
            ps.setString(8, c.getStudentName());
            ps.setInt(9, c.getId());

            ps.executeUpdate();
        }
    }

    public static void deleteCourse(Course c) throws SQLException {
        if (c.getId() == null) {
            throw new IllegalArgumentException("Course ID is null. Cannot delete record without ID.");
        }

        String sql = "DELETE FROM courses WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getId());
            ps.executeUpdate();
        }
    }}

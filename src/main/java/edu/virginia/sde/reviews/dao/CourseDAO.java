package edu.virginia.sde.reviews.dao;

import edu.virginia.sde.reviews.model.Course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling course-related database operations.
 * 
 * This class provides methods for:
 * - Retrieving courses from the database with various filters
 * - Creating new courses
 * - Calculating average ratings for courses based on reviews
 * 
 * It manages the database connection and translates between database records
 * and Course model objects.
 */
public class CourseDAO {
    /** SQLite database connection URL */
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    /**
     * Retrieves all courses from the database with their average ratings.
     * Used in the Course Search scene to display all available courses.
     *
     * @return List of all courses with calculated average ratings
     */
    public List<Course> getAllCourses() {
        String sql = "SELECT c.id, c.subject, c.number, c.title, " +
                     "IFNULL(AVG(r.rating), 0) as avg_rating " +
                     "FROM courses c " +
                     "LEFT JOIN reviews r ON c.id = r.course_id " +
                     "GROUP BY c.id";
        
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
        
        return courses;
    }

    /**
     * Searches for courses matching the provided criteria.
     * All parameters are optional - if null/empty, that filter is not applied.
     *
     * @param subjectFilter Filter by subject mnemonic (case-insensitive)
     * @param numberFilter Filter by exact course number
     * @param titleFilter Filter by course title containing text (case-insensitive)
     * @return List of matching courses with calculated average ratings
     */
    public List<Course> searchCourses(String subjectFilter, Integer numberFilter, String titleFilter) {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT c.id, c.subject, c.number, c.title, " +
                "IFNULL(AVG(r.rating), 0) as avg_rating " +
                "FROM courses c " +
                "LEFT JOIN reviews r ON c.id = r.course_id WHERE 1=1");
        
        List<Object> params = new ArrayList<>();
        
        if (subjectFilter != null && !subjectFilter.isEmpty()) {
            sqlBuilder.append(" AND UPPER(c.subject) = UPPER(?)");
            params.add(subjectFilter);
        }
        
        if (numberFilter != null) {
            sqlBuilder.append(" AND c.number = ?");
            params.add(numberFilter);
        }
        
        if (titleFilter != null && !titleFilter.isEmpty()) {
            sqlBuilder.append(" AND UPPER(c.title) LIKE UPPER(?)");
            params.add("%" + titleFilter + "%");
        }
        
        sqlBuilder.append(" GROUP BY c.id");
        String sql = sqlBuilder.toString();
        
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) param);
                }
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = extractCourseFromResultSet(rs);
                courses.add(course);
            }
            
        } catch (SQLException e) {
            System.out.println("Error searching courses: " + e.getMessage());
        }
        
        return courses;
    }

    /**
     * Retrieves a specific course by its ID.
     * Includes the calculated average rating from all reviews.
     *
     * @param id The course ID to retrieve
     * @return The course if found, null otherwise
     */
    public Course getCourseById(int id) {
        String sql = "SELECT c.id, c.subject, c.number, c.title, " +
                     "IFNULL(AVG(r.rating), 0) as avg_rating " +
                     "FROM courses c " +
                     "LEFT JOIN reviews r ON c.id = r.course_id " +
                     "WHERE c.id = ? " +
                     "GROUP BY c.id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving course: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Creates a new course in the database.
     * Stores the subject in uppercase for consistency.
     *
     * @param course The course object containing data to insert
     * @return true if course was created successfully, false otherwise
     */
    public boolean createCourse(Course course) {
        String sql = "INSERT INTO courses(subject, number, title) VALUES(?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, course.getSubject().toUpperCase());
            pstmt.setInt(2, course.getNumber());
            pstmt.setString(3, course.getTitle());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error creating course: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all courses that have been reviewed by a specific user.
     * Used in the My Reviews scene to display the user's reviews.
     *
     * @param userId The ID of the user whose reviewed courses to retrieve
     * @return List of courses reviewed by the user
     */
    public List<Course> getCoursesForUserReviews(int userId) {
        String sql = "SELECT c.id, c.subject, c.number, c.title, r.rating as avg_rating " +
                     "FROM courses c " +
                     "JOIN reviews r ON c.id = r.course_id " +
                     "WHERE r.user_id = ?";
        
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course(
                        rs.getInt("id"),
                        rs.getString("subject"),
                        rs.getInt("number"),
                        rs.getString("title")
                );
                course.setAverageRating(rs.getDouble("avg_rating"));
                courses.add(course);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving user courses: " + e.getMessage());
        }
        
        return courses;
    }

    /**
     * Helper method to extract a Course object from a database result set.
     *
     * @param rs The ResultSet containing course data
     * @return A populated Course object
     * @throws SQLException If a database access error occurs
     */
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course(
                rs.getInt("id"),
                rs.getString("subject"),
                rs.getInt("number"),
                rs.getString("title")
        );
        course.setAverageRating(rs.getDouble("avg_rating"));
        return course;
    }
} 
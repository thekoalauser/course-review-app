package edu.virginia.sde.reviews.dao;

import edu.virginia.sde.reviews.model.Review;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling all database operations related to reviews.
 * This class provides methods to create, read, update, and delete review records in the database.
 * It encapsulates all SQL operations and database access logic for the Review entity.
 */
public class ReviewDAO {
    /** The database URL for connecting to the SQLite database */
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    /**
     * Retrieves all reviews for a specific course from the database.
     *
     * @param courseId The ID of the course to get reviews for
     * @return A list of Review objects for the specified course
     */
    public List<Review> getReviewsForCourse(int courseId) {
        String sql = "SELECT id, user_id, course_id, rating, comment, timestamp " +
                     "FROM reviews WHERE course_id = ?";
        
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Review review = extractReviewFromResultSet(rs);
                reviews.add(review);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving reviews: " + e.getMessage());
        }
        
        return reviews;
    }

    /**
     * Retrieves a specific user's review for a specific course.
     * A user can only have one review per course.
     *
     * @param userId The ID of the user
     * @param courseId The ID of the course
     * @return The Review object if found, or null if the user hasn't reviewed this course
     */
    public Review getUserReviewForCourse(int userId, int courseId) {
        String sql = "SELECT id, user_id, course_id, rating, comment, timestamp " +
                     "FROM reviews WHERE user_id = ? AND course_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving user review: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Creates a new review in the database.
     *
     * @param review The Review object to be saved to the database
     * @return true if the review was successfully created, false otherwise
     */
    public boolean createReview(Review review) {
        String sql = "INSERT INTO reviews(user_id, course_id, rating, comment, timestamp) " +
                     "VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getCourseId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setTimestamp(5, review.getTimestamp());
            
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error creating review: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing review in the database.
     *
     * @param review The Review object with updated values
     * @return true if the review was successfully updated, false otherwise
     */
    public boolean updateReview(Review review) {
        String sql = "UPDATE reviews SET rating = ?, comment = ?, timestamp = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, review.getRating());
            pstmt.setString(2, review.getComment());
            pstmt.setTimestamp(3, review.getTimestamp());
            pstmt.setInt(4, review.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error updating review: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a review from the database by its ID.
     *
     * @param reviewId The ID of the review to delete
     * @return true if the review was successfully deleted, false otherwise
     */
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reviewId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all reviews submitted by a specific user.
     *
     * @param userId The ID of the user to get reviews for
     * @return A list of Review objects created by the specified user
     */
    public List<Review> getReviewsByUser(int userId) {
        String sql = "SELECT id, user_id, course_id, rating, comment, timestamp " +
                     "FROM reviews WHERE user_id = ?";
        
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Review review = extractReviewFromResultSet(rs);
                reviews.add(review);
            }
            
        } catch (SQLException e) {
            System.out.println("Error retrieving user reviews: " + e.getMessage());
        }
        
        return reviews;
    }

    /**
     * Helper method to extract a Review object from a database result set.
     *
     * @param rs The ResultSet to extract data from
     * @return A new Review object populated with data from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int courseId = rs.getInt("course_id");
        int rating = rs.getInt("rating");
        String comment = rs.getString("comment");
        Timestamp timestamp = rs.getTimestamp("timestamp");
        
        return new Review(id, userId, courseId, rating, comment, timestamp);
    }
} 
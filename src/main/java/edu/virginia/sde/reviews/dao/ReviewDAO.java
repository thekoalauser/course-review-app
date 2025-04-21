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

public class ReviewDAO {
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

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
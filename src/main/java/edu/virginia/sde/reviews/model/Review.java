package edu.virginia.sde.reviews.model;

import java.sql.Timestamp;

/**
 * Model class representing a user's review of a course.
 * Each review contains a rating, a text comment, and metadata about
 * who created the review, which course it's for, and when it was created.
 */
public class Review {
    /** Unique identifier for the review */
    private int id;
    
    /** ID of the user who created this review */
    private int userId;
    
    /** ID of the course being reviewed */
    private int courseId;
    
    /** Numerical rating assigned to the course (typically 1-5) */
    private int rating;
    
    /** User's written comment about the course */
    private String comment;
    
    /** Timestamp of when the review was created or last updated */
    private Timestamp timestamp;

    /**
     * Creates a new Review with a specified ID.
     *
     * @param id The unique identifier for this review
     * @param userId The ID of the user who created this review
     * @param courseId The ID of the course being reviewed
     * @param rating The numerical rating assigned to the course
     * @param comment The user's written comment about the course
     * @param timestamp The timestamp of when the review was created or last updated
     */
    public Review(int id, int userId, int courseId, int rating, String comment, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    /**
     * Creates a new Review without an assigned ID (typically used for new reviews before persisting to the database).
     * Sets the ID to -1, which indicates the review has not yet been saved to the database.
     *
     * @param userId The ID of the user who created this review
     * @param courseId The ID of the course being reviewed
     * @param rating The numerical rating assigned to the course
     * @param comment The user's written comment about the course
     * @param timestamp The timestamp of when the review was created
     */
    public Review(int userId, int courseId, int rating, String comment, Timestamp timestamp) {
        this(-1, userId, courseId, rating, comment, timestamp);
    }

    /**
     * Gets the review's unique identifier.
     * @return The review ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the review's unique identifier.
     * @param id The new review ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the user who created this review.
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user who created this review.
     * @param userId The new user ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the ID of the course being reviewed.
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Sets the ID of the course being reviewed.
     * @param courseId The new course ID
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the numerical rating assigned to the course.
     * @return The rating value
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the numerical rating assigned to the course.
     * @param rating The new rating value
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Gets the user's written comment about the course.
     * @return The comment text
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the user's written comment about the course.
     * @param comment The new comment text
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Gets the timestamp of when the review was created or last updated.
     * @return The timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of when the review was created or last updated.
     * @param timestamp The new timestamp
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Provides a string representation of this review, showing its rating and timestamp.
     * @return A string containing the rating and timestamp
     */
    @Override
    public String toString() {
        return "Rating: " + rating + " (" + timestamp + ")";
    }
} 
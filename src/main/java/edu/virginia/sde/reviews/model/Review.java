package edu.virginia.sde.reviews.model;

import java.sql.Timestamp;

public class Review {
    private int id;
    private int userId;
    private int courseId;
    private int rating;
    private String comment;
    private Timestamp timestamp;

    public Review(int id, int userId, int courseId, int rating, String comment, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Review(int userId, int courseId, int rating, String comment, Timestamp timestamp) {
        this(-1, userId, courseId, rating, comment, timestamp);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Rating: " + rating + " (" + timestamp + ")";
    }
} 
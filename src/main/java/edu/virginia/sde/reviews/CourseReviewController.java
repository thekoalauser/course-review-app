package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.dao.ReviewDAO;
import edu.virginia.sde.reviews.model.Course;
import edu.virginia.sde.reviews.model.Review;
import edu.virginia.sde.reviews.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.List;

/**
 * Controller class for the Course Review scene.
 * This class manages the UI interactions for viewing, creating, editing, and deleting course reviews.
 * It displays course information, average ratings, and all existing reviews for a specific course.
 * Users can submit new reviews, edit their existing reviews, or delete their reviews.
 */
public class CourseReviewController {
    /** Label displaying the course information (subject, catalog number, title) */
    @FXML
    private Label courseInfoLabel;
    /** Label displaying the average rating for the course */
    @FXML
    private Label averageRatingLabel;
    /** Container for displaying all reviews for the course */
    @FXML
    private VBox reviewsContainer;
    /** Spinner for selecting a rating value (1-5) */
    @FXML
    private Spinner<Integer> ratingSpinner;
    /** Text area for entering review comments */
    @FXML
    private TextArea commentTextArea;
    /** Button to submit a new review */
    @FXML
    private Button submitReviewButton;
    /** Button to edit an existing review */
    @FXML
    private Button editReviewButton;
    /** Button to delete an existing review */
    @FXML
    private Button deleteReviewButton;
    /** Button to navigate back to the course search scene */
    @FXML
    private Button backButtonCourses;

    /** The course being reviewed */
    private Course course;
    /** Data access object for review operations */
    private ReviewDAO reviewDAO;
    /** The current user's review for this course, if it exists */
    private Review userReview;

    /**
     * Initializes the controller.
     * Sets up the rating spinner, configures button handlers, and initializes the ReviewDAO.
     */
    public void initialize() {
        reviewDAO = new ReviewDAO();
        
        // Initialize the rating spinner with values 1-5
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);
        
        // Set up button handlers
        backButtonCourses.setOnAction(event -> handleBackButtonCourses());
        submitReviewButton.setOnAction(event -> handleSubmitReview());
        editReviewButton.setOnAction(event -> handleEditReview());
        deleteReviewButton.setOnAction(event -> handleDeleteReview());
        
        System.out.println("CourseReviewController initialized");
    }

    /**
     * Initializes the controller with course data.
     * Updates the UI with course information, loads existing reviews,
     * and checks if the current user already has a review for this course.
     *
     * @param course The course to display reviews for
     */
    public void initData(Course course) {
        this.course = course;
        
        // Debug output
        System.out.println("initData called with course: " + course.getId() + " - " + course.toString());
        
        // Update UI with course info
        courseInfoLabel.setText(course.toString());
        String avgRating = course.getFormattedAverageRating();
        averageRatingLabel.setText(avgRating.isEmpty() ? "No ratings yet" : "Average Rating: " + avgRating);
        
        // Load reviews for this course
        loadReviews();
        
        // Check if current user already has a review
        checkUserReview();
    }

    /**
     * Loads and displays all reviews for the current course.
     * If no reviews exist, displays a message indicating there are no reviews.
     */
    private void loadReviews() {
        reviewsContainer.getChildren().clear();
        
        List<Review> reviews = reviewDAO.getReviewsForCourse(course.getId());
        System.out.println("Loaded " + reviews.size() + " reviews for course ID: " + course.getId());
        
        if (reviews.isEmpty()) {
            Label noReviewsLabel = new Label("No reviews yet for this course.");
            reviewsContainer.getChildren().add(noReviewsLabel);
            System.out.println("No reviews found for course: " + course.toString());
        } else {
            for (Review review : reviews) {
                VBox reviewBox = createReviewBox(review);
                reviewsContainer.getChildren().add(reviewBox);
                System.out.println("Added review ID: " + review.getId() + " with rating: " + review.getRating());
            }
        }
    }

    /**
     * Creates a VBox containing the visual representation of a review.
     * Includes rating, timestamp, and comment (if provided).
     *
     * @param review The review to display
     * @return A VBox containing the styled review information
     */
    private VBox createReviewBox(Review review) {
        VBox reviewBox = new VBox(5);
        reviewBox.getStyleClass().add("review-box");
        
        Label ratingLabel = new Label("Rating: " + review.getRating() + "/5");
        Label timestampLabel = new Label("Posted on: " + review.getTimestamp().toString());
        
        reviewBox.getChildren().addAll(ratingLabel, timestampLabel);
        
        if (review.getComment() != null && !review.getComment().trim().isEmpty()) {
            TextArea commentArea = new TextArea(review.getComment());
            commentArea.setEditable(false);
            commentArea.setWrapText(true);
            commentArea.setPrefRowCount(3);
            reviewBox.getChildren().add(commentArea);
        }
        
        return reviewBox;
    }

    /**
     * Checks if the current user has already submitted a review for this course.
     * Updates the UI accordingly to show either submission or edit/delete options.
     */
    private void checkUserReview() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            userReview = reviewDAO.getUserReviewForCourse(currentUser.getId(), course.getId());
            
            if (userReview != null) {
                // User already has a review - show edit mode
                ratingSpinner.getValueFactory().setValue(userReview.getRating());
                commentTextArea.setText(userReview.getComment());
                
                submitReviewButton.setVisible(false);
                editReviewButton.setVisible(true);
                deleteReviewButton.setVisible(true);
            } else {
                // User doesn't have a review yet - show create mode
                submitReviewButton.setVisible(true);
                editReviewButton.setVisible(false);
                deleteReviewButton.setVisible(false);
            }
        } else {
            // No user logged in (shouldn't happen)
            submitReviewButton.setVisible(false);
            editReviewButton.setVisible(false);
            deleteReviewButton.setVisible(false);
        }
    }

    /**
     * Handles the back button for courses action.
     * Returns to the course browse scene.
     */
    private void handleBackButtonCourses() {
        Stage stage = (Stage) backButtonCourses.getScene().getWindow();
        SceneManager.switchToCourseBrowseScene(stage);
    }

    /**
     * Handles the submit review button action.
     * Creates a new review with the current user's input and saves it to the database.
     * Updates the UI to reflect the new review and refreshes the course rating.
     */
    private void handleSubmitReview() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return;
        
        int rating = ratingSpinner.getValue();
        String comment = commentTextArea.getText().trim();
        
        Review newReview = new Review(
                currentUser.getId(),
                course.getId(),
                rating,
                comment,
                new Timestamp(System.currentTimeMillis())
        );
        
        if (reviewDAO.createReview(newReview)) {
            loadReviews();
            checkUserReview();
            refreshCourseRating();
        } else {
            showAlert("Error", "Could not create review. Please try again.");
        }
    }

    /**
     * Handles the edit review button action.
     * Updates the current user's existing review with new input values.
     * Refreshes the UI to display the updated review and course rating.
     */
    private void handleEditReview() {
        if (userReview == null) return;
        
        int rating = ratingSpinner.getValue();
        String comment = commentTextArea.getText().trim();
        
        userReview.setRating(rating);
        userReview.setComment(comment);
        userReview.setTimestamp(new Timestamp(System.currentTimeMillis()));
        
        if (reviewDAO.updateReview(userReview)) {
            loadReviews();
            refreshCourseRating();
        } else {
            showAlert("Error", "Could not update review. Please try again.");
        }
    }

    /**
     * Handles the delete review button action.
     * Removes the current user's review from the database.
     * Resets the review form and updates the UI accordingly.
     */
    private void handleDeleteReview() {
        if (userReview == null) return;
        
        if (reviewDAO.deleteReview(userReview.getId())) {
            userReview = null;
            commentTextArea.clear();
            ratingSpinner.getValueFactory().setValue(3);
            loadReviews();
            checkUserReview();
            refreshCourseRating();
        } else {
            showAlert("Error", "Could not delete review. Please try again.");
        }
    }

    /**
     * Refreshes the course's average rating after a review has been added, updated, or deleted.
     * Updates the average rating label in the UI.
     */
    private void refreshCourseRating() {
        // Get the updated course with new average rating
        edu.virginia.sde.reviews.dao.CourseDAO courseDAO = new edu.virginia.sde.reviews.dao.CourseDAO();
        Course updatedCourse = courseDAO.getCourseById(course.getId());
        if (updatedCourse != null) {
            course.setAverageRating(updatedCourse.getAverageRating());
            String avgRating = course.getFormattedAverageRating();
            averageRatingLabel.setText(avgRating.isEmpty() ? "No ratings yet" : "Average Rating: " + avgRating);
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title The title of the alert dialog
     * @param message The message to display in the alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 
package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.dao.CourseDAO;
import edu.virginia.sde.reviews.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller class for the "My Reviews" view that displays all courses reviewed by the current user.
 * This controller manages the UI for users to see and access their own reviews.
 * Users can view the list of courses they've reviewed and navigate to those course reviews
 * for viewing or editing their own reviews.
 */
public class MyReviewsController {
    /** ListView displaying all courses that the current user has reviewed */
    @FXML
    private ListView<Course> reviewedCoursesListView;
    
    /** Button to navigate back to the course list */
    @FXML
    private Button backButton;
    
    /** Label that displays the current user's username */
    @FXML
    private Label userLabel;
    
    /** Label shown when the user has no reviews */
    @FXML
    private Label noReviewsLabel;

    /** Data access object for course-related database operations */
    private CourseDAO courseDAO;
    
    /** Observable list of courses that the current user has reviewed */
    private ObservableList<Course> reviewedCourses;

    /**
     * Initializes the controller, setting up UI elements and loading the user's reviewed courses.
     * This method is automatically called after the FXML has been loaded.
     */
    public void initialize() {
        courseDAO = new CourseDAO();
        reviewedCourses = FXCollections.observableArrayList();
        reviewedCoursesListView.setItems(reviewedCourses);
        
        // Set up user label
        if (SessionManager.getInstance().isLoggedIn()) {
            userLabel.setText(SessionManager.getInstance().getCurrentUser().getUsername() + "'s Reviews");
        } else {
            userLabel.setText("My Reviews");
        }
        
        // Load reviewed courses
        loadReviewedCourses();
        
        // Set up back button
        backButton.setOnAction(event -> handleBackButton());
        
        // Set up double-click on list view
        reviewedCoursesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !reviewedCoursesListView.getSelectionModel().isEmpty()) {
                Course selectedCourse = reviewedCoursesListView.getSelectionModel().getSelectedItem();
                handleCourseSelection(selectedCourse);
            }
        });
    }

    /**
     * Loads all courses that the current user has reviewed.
     * Updates the UI based on whether there are any reviewed courses:
     * - If there are reviewed courses, they are displayed in the list view
     * - If there are no reviewed courses, a "no reviews" message is displayed
     */
    private void loadReviewedCourses() {
        if (SessionManager.getInstance().isLoggedIn()) {
            int userId = SessionManager.getInstance().getCurrentUser().getId();
            List<Course> courses = courseDAO.getCoursesForUserReviews(userId);
            
            reviewedCourses.clear();
            reviewedCourses.addAll(courses);
            
            if (courses.isEmpty()) {
                noReviewsLabel.setText("You haven't reviewed any courses yet.");
                noReviewsLabel.setVisible(true);
                reviewedCoursesListView.setVisible(false);
            } else {
                noReviewsLabel.setVisible(false);
                reviewedCoursesListView.setVisible(true);
            }
        }
    }

    /**
     * Handles the back button action, navigating back to the home page.
     */
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneManager.goBack(stage);
    }

    /**
     * Handles the selection of a course from the list view, navigating to the
     * course review scene for the selected course.
     *
     * @param course The selected Course object to view reviews for
     */
    private void handleCourseSelection(Course course) {
        if (course != null) {
            Stage stage = (Stage) reviewedCoursesListView.getScene().getWindow();
            SceneManager.switchToCourseReviewScene(stage, course);
        }
    }
} 
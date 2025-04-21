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

public class MyReviewsController {
    @FXML
    private ListView<Course> reviewedCoursesListView;
    @FXML
    private Button backButton;
    @FXML
    private Label userLabel;
    @FXML
    private Label noReviewsLabel;

    private CourseDAO courseDAO;
    private ObservableList<Course> reviewedCourses;

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

    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneManager.switchToCourseSearchScene(stage);
    }

    private void handleCourseSelection(Course course) {
        if (course != null) {
            Stage stage = (Stage) reviewedCoursesListView.getScene().getWindow();
            SceneManager.switchToCourseReviewScene(stage, course);
        }
    }
} 
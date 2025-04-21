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
 * Controller for the Course Browse scene.
 * This controller manages the UI for displaying and selecting courses
 * to view their reviews.
 */
public class CourseBrowseController {
    /** ListView displaying all available courses */
    @FXML
    private ListView<Course> coursesListView;
    
    /** Button to navigate back to the home page */
    @FXML
    private Button backButton;
    
    /** Label shown when there are no courses in the database */
    @FXML
    private Label noCoursesLabel;

    /** Data access object for course-related database operations */
    private CourseDAO courseDAO;
    
    /** Observable list of all courses in the database */
    private ObservableList<Course> courses;

    /**
     * Initializes the controller, setting up UI elements and loading all courses.
     * This method is automatically called after the FXML has been loaded.
     */
    public void initialize() {
        courseDAO = new CourseDAO();
        courses = FXCollections.observableArrayList();
        coursesListView.setItems(courses);
        
        // Load all courses
        loadCourses();
        
        // Set up back button
        backButton.setOnAction(event -> handleBackButton());
        
        // Set up double-click on list view
        coursesListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !coursesListView.getSelectionModel().isEmpty()) {
                Course selectedCourse = coursesListView.getSelectionModel().getSelectedItem();
                handleCourseSelection(selectedCourse);
            }
        });
    }

    /**
     * Loads all courses from the database.
     * Updates the UI based on whether there are any courses:
     * - If there are courses, they are displayed in the list view
     * - If there are no courses, a "no courses" message is displayed
     */
    private void loadCourses() {
        List<Course> courseList = courseDAO.getAllCourses();
        
        courses.clear();
        courses.addAll(courseList);
        
        if (courseList.isEmpty()) {
            noCoursesLabel.setVisible(true);
            coursesListView.setVisible(false);
        } else {
            noCoursesLabel.setVisible(false);
            coursesListView.setVisible(true);
        }
    }

    /**
     * Handles the back button action, navigating back to the home page.
     */
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneManager.switchToHomeScene(stage);
    }

    /**
     * Handles the selection of a course from the list view, navigating to the
     * course review scene for the selected course.
     *
     * @param course The selected Course object to view reviews for
     */
    private void handleCourseSelection(Course course) {
        if (course != null) {
            Stage stage = (Stage) coursesListView.getScene().getWindow();
            SceneManager.switchToCourseReviewScene(stage, course);
        }
    }
} 
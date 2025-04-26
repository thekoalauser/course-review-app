package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.dao.CourseDAO;
import edu.virginia.sde.reviews.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the Course Search scene.
 * Allows users to search for courses by keyword and view results.
 */
public class CourseSearchController {
    @FXML
    private Button backButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<Course> resultsListView;

    @FXML
    private Label noResultsLabel;

    @FXML
    private Button addCourseButton;

    private CourseDAO courseDAO;
    private ObservableList<Course> searchResults;

    /**
     * Initializes the controller. Sets up button actions and UI components.
     */
    public void initialize() {
        courseDAO = new CourseDAO();
        searchResults = FXCollections.observableArrayList();
        resultsListView.setItems(searchResults);

        searchButton.setOnAction(e -> performSearch());
        backButton.setOnAction(e -> handleBackButton());
        addCourseButton.setOnAction(e -> openAddCourseDialog());


        resultsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !resultsListView.getSelectionModel().isEmpty()) {
                Course selectedCourse = resultsListView.getSelectionModel().getSelectedItem();
                handleCourseSelection(selectedCourse);
            }
        });
    }

    /**
     * Performs a search query using the searchField text.
     * Updates the resultsListView with matches or displays a no-results label.
     */
    private void performSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            noResultsLabel.setText("Please enter a search term.");
            noResultsLabel.setVisible(true);
            searchResults.clear();
            return;
        }

        List<Course> matchingCourses;

        if (query.matches("\\d{4}")) {
            // 4 digit number -> search by course number
            Integer number = Integer.parseInt(query);
            matchingCourses = courseDAO.searchCourses(null, number, null);
        } else if (query.matches("[a-zA-Z]{2,4}")) {
            // 2-4 letters -> search by subject
            matchingCourses = courseDAO.searchCourses(query, null, null);
        } else {
            // general title search
            matchingCourses = courseDAO.searchCourses(null, null, query);
        }

        searchResults.setAll(matchingCourses);

        if (matchingCourses.isEmpty()) {
            noResultsLabel.setText("No matching courses found.");
            noResultsLabel.setVisible(true);
        } else {
            noResultsLabel.setVisible(false);
        }
    }



    /**
     * Handles the back button, switching to the home scene.
     */
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneManager.switchToHomeScene(stage);
    }

    /**
     * Opens the course review scene for the selected course.
     */
    private void handleCourseSelection(Course course) {
        if (course != null) {
            Stage stage = (Stage) resultsListView.getScene().getWindow();
            SceneManager.switchToCourseReviewScene(stage, course);
        }
    }

    private void openAddCourseDialog() {
        AddCourseDialog.showAndWait(result -> {
            if (result != null) {
                Course newCourse = new Course(
                        result.getSubject().toUpperCase(),
                        result.getNumber(),
                        result.getTitle()
                );
                boolean success = courseDAO.createCourse(newCourse);
                if (success) {
                    showInfoAlert("Course Added", "The course was added successfully.");
                    performSearch(); // refresh search results
                } else {
                    showErrorAlert("Failed to add course. Please try again.");
                }
            }
            return null;
        });
    }

    /**
     * Utility method to show an information alert (for success messages).
     *
     * @param title The title of the alert window
     * @param message The message to display
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Utility method to show an error alert (for error messages).
     *
     * @param message The error message to display
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null); // No header
        alert.setContentText(message);
        alert.showAndWait();
    }
}

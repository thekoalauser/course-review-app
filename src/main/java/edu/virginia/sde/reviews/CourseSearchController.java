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

        // Try to parse course number if input is a number
        Integer number = null;
        try {
            number = Integer.parseInt(query);
        } catch (NumberFormatException ignored) {}

        // Attempt search across all fields with fuzzy title match
        List<Course> matchingCourses = courseDAO.searchCourses(query, number, query);
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
}

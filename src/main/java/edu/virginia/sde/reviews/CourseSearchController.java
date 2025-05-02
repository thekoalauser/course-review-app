package edu.virginia.sde.reviews;

import edu.virginia.sde.reviews.dao.CourseDAO;
import edu.virginia.sde.reviews.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class CourseSearchController {

    @FXML private Button backButton;
    @FXML private Button searchButton;
    @FXML private Button addCourseButton;

    @FXML private TextField subjectField;
    @FXML private TextField numberField;
    @FXML private TextField titleField;

    @FXML private TableView<Course> resultsTableView;
    @FXML private TableColumn<Course, String> subjectColumn;
    @FXML private TableColumn<Course, Integer> numberColumn;
    @FXML private TableColumn<Course, String> titleColumn;
    @FXML private TableColumn<Course, Double> averageColumn;

    @FXML private Label noResultsLabel;

    private CourseDAO courseDAO;
    private ObservableList<Course> searchResults;

    public void initialize() {
        courseDAO = new CourseDAO();
        searchResults = FXCollections.observableArrayList();
        resultsTableView.setItems(searchResults);

        // Use classic JavaFX PropertyValueFactory for column binding
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        averageColumn.setCellFactory(column -> new TableCell<Course, Double>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null || rating == 0.0) {
                    setText(""); // show blank
                } else {
                    setText(String.format("%.2f", rating)); // show formatted rating
                }
            }
        });

        loadAllCoursesSortedByTitle();

        searchButton.setOnAction(e -> performSearch());
        addCourseButton.setOnAction(e -> openAddCourseDialog());
        backButton.setOnAction(e -> handleBackButton());

        resultsTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !resultsTableView.getSelectionModel().isEmpty()) {
                Course selectedCourse = resultsTableView.getSelectionModel().getSelectedItem();
                handleCourseSelection(selectedCourse);
            }
        });
    }

    private void loadAllCoursesSortedByTitle() {
        List<Course> courses = courseDAO.getAllCourses();

        // Sort by title (case-insensitive)
        courses.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));

        resultsTableView.getItems().setAll(courses);
        noResultsLabel.setVisible(courses.isEmpty());
    }

    private void performSearch() {
        String subject = subjectField.getText().trim();
        String numberText = numberField.getText().trim();
        String title = titleField.getText().trim();

        Integer number = null;
        if (!numberText.isEmpty()) {
            try {
                number = Integer.parseInt(numberText);
            } catch (NumberFormatException e) {
                showErrorAlert("Course number must be numeric.");
                return;
            }
        }

        List<Course> results = courseDAO.searchCourses(subject, number, title);
        searchResults.setAll(results);
        noResultsLabel.setVisible(results.isEmpty());
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
                    loadAllCoursesSortedByTitle();
                } else {
                    showErrorAlert("Failed to add course. Please try again.");
                }
            }
            return null;
        });
    }

    private void handleCourseSelection(Course course) {
        if (course != null) {
            Stage stage = (Stage) resultsTableView.getScene().getWindow();
            SceneManager.switchToCourseReviewScene(stage, course);
        }
    }

    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        SceneManager.goBack(stage);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package edu.virginia.sde.reviews;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * A simple dialog for adding a new course.
 */
public class AddCourseDialog {

    /**
     * Opens a dialog to add a new course and passes the result to a callback.
     * @param callback The action to perform with the course details
     */
    public static void showAndWait(Callback<CourseInputResult, Void> callback) {
        Dialog<CourseInputResult> dialog = new Dialog<>();
        dialog.setTitle("Add New Course");
        dialog.setHeaderText("Enter course details");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject (2-4 letters)");

        TextField numberField = new TextField();
        numberField.setPromptText("Number (4 digits)");

        TextField titleField = new TextField();
        titleField.setPromptText("Title (1-50 characters)");

        grid.add(new Label("Subject:"), 0, 0);
        grid.add(subjectField, 1, 0);
        grid.add(new Label("Number:"), 0, 1);
        grid.add(numberField, 1, 1);
        grid.add(new Label("Title:"), 0, 2);
        grid.add(titleField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String subject = subjectField.getText().trim();
                String numberText = numberField.getText().trim();
                String title = titleField.getText().trim();

                // Input validation
                if (!subject.matches("[a-zA-Z]{2,4}")) {
                    showAlert("Subject must be 2-4 letters.");
                    return null;
                }

                if (!numberText.matches("\\d{4}")) {
                    showAlert("Course number must be exactly 4 digits.");
                    return null;
                }

                if (title.isEmpty() || title.length() > 50) {
                    showAlert("Title must be between 1 and 50 characters.");
                    return null;
                }

                int number = Integer.parseInt(numberText);
                return new CourseInputResult(subject, number, title);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(callback::call);
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper class to carry input result.
     */
    public static class CourseInputResult {
        private final String subject;
        private final int number;
        private final String title;

        public CourseInputResult(String subject, int number, String title) {
            this.subject = subject;
            this.number = number;
            this.title = title;
        }

        public String getSubject() {
            return subject;
        }

        public int getNumber() {
            return number;
        }

        public String getTitle() {
            return title;
        }
    }
}


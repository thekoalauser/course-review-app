package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CourseReviewsApplication extends Application {
    private static final String DB_URL = "jdbc:sqlite:course_reviews.db";

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database
        initializeDatabase();
        
        // Load the login scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        
        // Set the stage properties
        stage.setTitle("UVA Course Reviews");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            // Create Users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL)");
            
            // Create Courses table
            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subject TEXT NOT NULL," +
                    "number INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "UNIQUE(subject, number, title))");
            
            // Create Reviews table
            stmt.execute("CREATE TABLE IF NOT EXISTS reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "rating INTEGER NOT NULL," +
                    "comment TEXT," +
                    "timestamp TIMESTAMP NOT NULL," +
                    "FOREIGN KEY(user_id) REFERENCES users(id)," +
                    "FOREIGN KEY(course_id) REFERENCES courses(id)," +
                    "UNIQUE(user_id, course_id))");
            
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 
cat << 'EOF' > README.md
Course Review Application

This project is a JavaFX-based CRUD (Create, Read, Update, Delete) desktop application that allows students to create accounts, search for university courses, and write anonymous course reviews. The application persists all data using a file-based SQLite database and provides a graphical user interface built with JavaFX.

This repository represents my implementation of a capstone project focused on database-backed application design, GUI development, and persistent data management.

--------------------------------------------------

FEATURES

- User account creation and login
- Username uniqueness and password length enforcement
- Course creation and searchable course catalog
- Case-insensitive search by subject, number, or title
- Anonymous course reviews with ratings and optional comments
- Edit and delete functionality for user-owned reviews
- Average course rating calculation
- Persistent data storage using SQLite
- User-facing error messages within the GUI

--------------------------------------------------

APPLICATION OVERVIEW

Login
- Users may log in with an existing username and password
- New users can create an account with a unique username
- Passwords must be at least 8 characters
- All errors are displayed within the GUI

Course Search
- Displays a list of all courses in the database
- Supports searching by subject mnemonic, course number, or title
- Allows users to add new courses with validated input
- Provides navigation to course reviews and user-specific reviews

Course Reviews
- Displays all reviews for a selected course
- Shows average rating with two decimal precision
- Users may submit only one review per course
- Reviews can be edited or deleted by their author
- Review timestamps are automatically generated
- Reviewer identities remain anonymous

My Reviews
- Displays all reviews written by the logged-in user
- Each review links back to the corresponding course review view

--------------------------------------------------

TECH STACK

Language: Java  
GUI Framework: JavaFX 21.0.9  
JDK: OpenJDK 21.0.9  
Database: SQLite (file-based)  
Build Tool: Gradle  
Persistence: JDBC  

--------------------------------------------------

DATABASE DESIGN

The application uses a normalized relational schema that persists users, courses, and reviews. Key constraints include unique usernames, prevention of duplicate reviews per user per course, and automatic database creation if the database file is missing.

All data remains available across application restarts.

--------------------------------------------------

RUNNING THE APPLICATION

Prerequisites:
- Java JDK 21.0.9
- JavaFX 21.0.9 (configured via Gradle)

Run the application from the project root:
./gradlew run

No VM arguments or command-line interaction are required.

--------------------------------------------------

ERROR HANDLING AND USABILITY

- The application does not crash on invalid input
- All validation errors are displayed within the GUI
- The UI fits within a 1280x720 resolution
- Navigation between screens is consistent and intuitive

--------------------------------------------------

MY CONTRIBUTIONS

I was responsible for designing the application architecture, implementing JavaFX scenes, building the SQLite-backed persistence layer, writing database queries, enforcing constraints, implementing CRUD workflows, and ensuring application stability and usability.

--------------------------------------------------

NOTES

Passwords are stored in plaintext as permitted by the assignment scope. Real credentials should not be used. The project was built from scratch without copying code from external repositories.
EOF

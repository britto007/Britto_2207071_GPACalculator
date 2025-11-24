package gpaapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseEntryController {

    @FXML private TextField courseName;
    @FXML private TextField courseCode;
    @FXML private TextField courseCredit;
    @FXML private TextField teacher1;
    @FXML private TextField teacher2;
    @FXML private ComboBox<String> gradeBox;
    @FXML private TextField studentRoll;
    @FXML private TextField studentName;
    @FXML private TableView<Course> table;
    @FXML private Button calcBtn;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private double totalCredits = 0.0;
    private final double REQUIRED_CREDITS = 15.0;


    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("DB-Executor");
        return t;
    });

    @FXML
    void initialize() {
        gradeBox.setItems(FXCollections.observableArrayList(
                "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"
        ));
        table.setItems(courses);
        calcBtn.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                courseName.setText(newSel.getName());
                courseCode.setText(newSel.getCode());
                courseCredit.setText(String.valueOf(newSel.getCredit()));
                teacher1.setText(newSel.getTeacher1());
                teacher2.setText(newSel.getTeacher2());
                gradeBox.setValue(newSel.getGrade());
                studentRoll.setText(newSel.getStudentRoll());
                studentName.setText(newSel.getStudentName());
            }
        });

        loadCoursesFromDatabase();
    }

    private void loadCoursesFromDatabase() {
        dbExecutor.submit(() -> {
            try {
                var dbCourses = Database.fetchAllCourses();
                Platform.runLater(() -> {
                    courses.setAll(dbCourses);
                    totalCredits = courses.stream().mapToDouble(Course::getCredit).sum();
                    calcBtn.setDisable(totalCredits < REQUIRED_CREDITS);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to load courses from database: " + ex.getMessage()).show();
                });
            }
        });
    }

    @FXML
    void addCourse() {
        try {
            String name = courseName.getText().trim();
            String code = courseCode.getText().trim();
            String creditText = courseCredit.getText().trim();
            String t1 = teacher1.getText().trim();
            String t2 = teacher2.getText().trim();
            String grade = gradeBox.getValue();
            String roll = studentRoll.getText().trim();
            String sName = studentName.getText().trim();

            if (name.isEmpty() || code.isEmpty() || creditText.isEmpty()
                    || grade == null || roll.isEmpty() || sName.isEmpty()) {
                throw new IllegalArgumentException("Please fill all fields including student roll and name.");
            }

            double credit = Double.parseDouble(creditText);

            Course newCourse = new Course(name, code, credit, t1, t2, grade, roll, sName);


            dbExecutor.submit(() -> {
                try {
                    int generatedId = Database.insertCourse(newCourse);
                    if (generatedId > 0) {
                        newCourse.setId(generatedId);
                    }
                    Platform.runLater(() -> {
                        courses.add(newCourse);
                        totalCredits += newCourse.getCredit();
                        if (totalCredits >= REQUIRED_CREDITS) {
                            calcBtn.setDisable(false);
                        }
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION,
                                "Course added and saved to database successfully!").show();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR,
                                "Failed to add course: " + ex.getMessage()).show();
                    });
                }
            });

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Credit must be a number.").show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    @FXML
    void updateCourse() {
        Course selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a course from the table to update.").show();
            return;
        }

        try {
            String name = courseName.getText();
            String code = courseCode.getText();
            String creditText = courseCredit.getText();
            String t1 = teacher1.getText();
            String t2 = teacher2.getText();
            String grade = gradeBox.getValue();
            String roll = studentRoll.getText();
            String sName = studentName.getText();

            if (name.isEmpty() || code.isEmpty() || creditText.isEmpty()
                    || grade == null || roll.isEmpty() || sName.isEmpty()) {
                throw new IllegalArgumentException("Please fill all fields including student roll and name.");
            }

            double newCredit = Double.parseDouble(creditText);
            double oldCredit = selected.getCredit();


            selected.setName(name);
            selected.setCode(code);
            selected.setCredit(newCredit);
            selected.setTeacher1(t1);
            selected.setTeacher2(t2);
            selected.setGrade(grade);
            selected.setStudentRoll(roll);
            selected.setStudentName(sName);

            dbExecutor.submit(() -> {
                try {
                    Database.updateCourse(selected);
                    Platform.runLater(() -> {
                        table.refresh();
                        totalCredits = totalCredits - oldCredit + newCredit;
                        calcBtn.setDisable(totalCredits < REQUIRED_CREDITS);
                        new Alert(Alert.AlertType.INFORMATION,
                                "Course updated successfully!").show();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR,
                                "Failed to update course: " + ex.getMessage()).show();
                    });
                }
            });

        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Credit must be a number.").show();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
        }
    }

    @FXML
    void deleteCourse() {
        Course selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a course from the table to delete.").show();
            return;
        }

        double selectedCredit = selected.getCredit();

        dbExecutor.submit(() -> {
            try {
                Database.deleteCourse(selected);
                Platform.runLater(() -> {
                    courses.remove(selected);
                    totalCredits -= selectedCredit;
                    if (totalCredits < REQUIRED_CREDITS) {
                        calcBtn.setDisable(true);
                    }
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION,
                            "Course deleted successfully!").show();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR,
                            "Failed to delete course: " + ex.getMessage()).show();
                });
            }
        });
    }

    private void clearFields() {
        courseName.clear();
        courseCode.clear();
        courseCredit.clear();
        teacher1.clear();
        teacher2.clear();
        gradeBox.getSelectionModel().clearSelection();
        studentRoll.clear();
        studentName.clear();
        table.getSelectionModel().clearSelection();
    }

    @FXML
    void calculateGPA() throws Exception {
        double gpa = GPAUtils.calculate(courses);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gpaapp/result.fxml"));
        Stage stage = (Stage) calcBtn.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        ResultController controller = loader.getController();
        controller.showResults(courses, gpa);
        stage.setScene(scene);
        stage.setTitle("GPA Result");
    }
}
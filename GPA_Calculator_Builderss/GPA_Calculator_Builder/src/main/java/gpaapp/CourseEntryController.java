package gpaapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseEntryController {

    @FXML private TextField courseName;
    @FXML private TextField courseCode;
    @FXML private TextField courseCredit;
    @FXML private TextField teacher1;
    @FXML private TextField teacher2;
    @FXML private ComboBox<String> gradeBox;
    @FXML private TableView<Course> table;
    @FXML private Button calcBtn;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private double totalCredits = 0.0;
    private final double REQUIRED_CREDITS = 15.0;

    @FXML
    void initialize() {
        gradeBox.setItems(FXCollections.observableArrayList(
                "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"
        ));
        table.setItems(courses);
        calcBtn.setDisable(true);
    }

    @FXML
    void addCourse() {
        try {
            String name = courseName.getText();
            String code = courseCode.getText();
            double credit = Double.parseDouble(courseCredit.getText().trim());
            String t1 = teacher1.getText();
            String t2 = teacher2.getText();
            String grade = gradeBox.getValue();

            if (name.isEmpty() || code.isEmpty() || grade == null) {
                throw new IllegalArgumentException("Fill name, code and grade.");
            }

            courses.add(new Course(name, code, credit, t1, t2, grade));
            totalCredits += credit;

            if (totalCredits >= REQUIRED_CREDITS) {
                calcBtn.setDisable(false);
            }
            clearFields();
            new Alert(Alert.AlertType.INFORMATION, "Course added successfully!").show();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Credit must be a number.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        courseName.clear();
        courseCode.clear();
        courseCredit.clear();
        teacher1.clear();
        teacher2.clear();
        gradeBox.getSelectionModel().clearSelection();
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

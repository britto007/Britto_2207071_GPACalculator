package gpaapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ResultController {
    @FXML private TableView<Course> table;
    @FXML private Label gpaLabel;

    public void showResults(ObservableList<Course> list, double gpa) {
        if (table == null || gpaLabel == null) {
            new Alert(Alert.AlertType.ERROR, "FXML not wired correctly.").show();
            return;
        }
        table.setItems(list);
        gpaLabel.setText(String.format("Your GPA: %.2f", gpa));
    }
}

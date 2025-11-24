package gpaapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomeController {
    @FXML
    void startApp(ActionEvent e) throws Exception {
        Stage stage = (Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gpaapp/course_entry (1).fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("GPA Calculator - Courses");
    }
}

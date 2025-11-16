package gpaapp;

import java.util.List;

public class GPAUtils {
    public static double gradePoint(String grade) {
        if (grade == null) return 0.0;
        return switch (grade) {
            case "A+" -> 4.0;
            case "A" -> 3.75;
            case "A-" -> 3.5;
            case "B+" -> 3.25;
            case "B" -> 3.0;
            case "B-" -> 2.75;
            case "C+" -> 2.5;
            case "C" -> 2.25;
            case "C-" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }

    public static double calculate(List<Course> courses) {
        double totalCredit = 0.0;
        double totalPoints = 0.0;
        for (Course c : courses) {
            totalCredit += c.getCredit();
            totalPoints += gradePoint(c.getGrade()) * c.getCredit();
        }
        return totalCredit == 0 ? 0.0 : totalPoints / totalCredit;
    }
}

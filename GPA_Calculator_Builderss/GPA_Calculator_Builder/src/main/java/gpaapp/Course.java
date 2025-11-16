package gpaapp;

public class Course {
    private final String name;
    private final String code;
    private final String teacher1;
    private final String teacher2;
    private final String grade;
    private final double credit;

    public Course(String name, String code, double credit, String teacher1, String teacher2, String grade) {
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public double getCredit() { return credit; }
    public String getTeacher1() { return teacher1; }
    public String getTeacher2() { return teacher2; }
    public String getGrade() { return grade; }
}

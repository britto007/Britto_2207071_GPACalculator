package gpaapp;

public class Course {
    private Integer id;
    private String name;
    private String code;
    private String teacher1;
    private String teacher2;
    private String grade;
    private double credit;
    private String studentRoll;
    private String studentName;


    public Course(Integer id,
                  String name,
                  String code,
                  double credit,
                  String teacher1,
                  String teacher2,
                  String grade,
                  String studentRoll,
                  String studentName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
        this.studentRoll = studentRoll;
        this.studentName = studentName;
    }


    public Course(String name,
                  String code,
                  double credit,
                  String teacher1,
                  String teacher2,
                  String grade,
                  String studentRoll,
                  String studentName) {
        this(null, name, code, credit, teacher1, teacher2, grade, studentRoll, studentName);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }

    public String getTeacher1() { return teacher1; }
    public void setTeacher1(String teacher1) { this.teacher1 = teacher1; }

    public String getTeacher2() { return teacher2; }
    public void setTeacher2(String teacher2) { this.teacher2 = teacher2; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getStudentRoll() { return studentRoll; }
    public void setStudentRoll(String studentRoll) { this.studentRoll = studentRoll; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}
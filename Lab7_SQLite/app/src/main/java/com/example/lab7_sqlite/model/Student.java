package com.example.lab7_sqlite.model;

public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String dateOfBirth;
    private String major;
    private double gpa;

    // Constructor đầy đủ
    public Student(int id, String studentCode, String fullName, String dateOfBirth, String major, double gpa) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.major = major;
        this.gpa = gpa;
    }

    // Constructor không có id (dùng khi thêm mới)
    public Student(String studentCode, String fullName, String dateOfBirth, String major, double gpa) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.major = major;
        this.gpa = gpa;
    }

    // Getters
    public int getId() { return id; }
    public String getStudentCode() { return studentCode; }
    public String getFullName() { return fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getMajor() { return major; }
    public double getGpa() { return gpa; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setMajor(String major) { this.major = major; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", major='" + major + '\'' +
                ", gpa=" + gpa +
                '}';
    }
}

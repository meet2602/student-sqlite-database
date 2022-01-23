package com.materialsouk.sqlitedatabase;

public class Student {

    private String id;
    private String studentName;
    private String studentAddress;
    private String studentPhoneNo;
    private String studentEmail;
    private String studentDOB;
    private String password;


    public Student(String id, String studentName, String studentAddress, String studentPhoneNo, String studentEmail, String studentDOB, String password) {
        this.id = id;
        this.studentName = studentName;
        this.studentAddress = studentAddress;
        this.studentPhoneNo = studentPhoneNo;
        this.studentEmail = studentEmail;
        this.studentDOB = studentDOB;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    public String getStudentPhoneNo() {
        return studentPhoneNo;
    }

    public void setStudentPhoneNo(String studentPhoneNo) {
        this.studentPhoneNo = studentPhoneNo;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentDOB() {
        return studentDOB;
    }

    public void setStudentDOB(String studentDOB) {
        this.studentDOB = studentDOB;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

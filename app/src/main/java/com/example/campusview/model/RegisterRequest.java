package com.example.campusview.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("username")
    private String username;
    
    @SerializedName("password")
    private String password;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("realName")
    private String realName;
    
    @SerializedName("studentId")
    private String studentId;
    
    @SerializedName("phone")
    private String phone;

    @SerializedName("department")
    private String department;

    // 基本构造函数
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // 完整构造函数
    public RegisterRequest(String username, String password, String email, 
                          String phone, String studentId, String department) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.studentId = studentId;
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
} 
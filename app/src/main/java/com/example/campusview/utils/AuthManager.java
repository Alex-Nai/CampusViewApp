package com.example.campusview.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.example.campusview.model.LoginResponse;
import android.util.Log;

public class AuthManager {
    private static final String PREF_NAME = "AuthPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLES = "roles";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_ROLE = "role";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_AVATAR = "avatar";

    private static AuthManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    private String token;
    private Long userId;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String role;
    private String email;
    private String phone;
    private String studentId;
    private String department;
    private String avatar;

    private AuthManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadUserData();
    }

    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context.getApplicationContext());
        }
        return instance;
    }

    private void loadUserData() {
//        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userIdStr = prefs.getString(KEY_USER_ID, null);
        if (userIdStr != null && !userIdStr.equals("null")) {
            try {
                userId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                userId = null;
            }
        } else {
            userId = null;
        }
        username = prefs.getString(KEY_USERNAME, null);
        token = prefs.getString(KEY_TOKEN, null);
        role = prefs.getString(KEY_ROLE, null);
        email = prefs.getString(KEY_EMAIL, null);
        phone = prefs.getString(KEY_PHONE, null);
        studentId = prefs.getString(KEY_STUDENT_ID, null);
        department = prefs.getString(KEY_DEPARTMENT, null);
        avatar = prefs.getString(KEY_AVATAR, null);
        
        // 加载角色
        String rolesJson = prefs.getString(KEY_ROLES, null);
        if (rolesJson != null) {
            Type rolesType = new TypeToken<List<String>>(){}.getType();
            roles = gson.fromJson(rolesJson, rolesType);
        }
        
        // 加载权限
        String permissionsJson = prefs.getString(KEY_PERMISSIONS, null);
        if (permissionsJson != null) {
            Type permissionsType = new TypeToken<List<String>>(){}.getType();
            permissions = gson.fromJson(permissionsJson, permissionsType);
        }
    }

    public void saveUserData(String token, Long userId, String username, List<String> roles, List<String> permissions) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        if (userId != null) {
            editor.putLong(KEY_USER_ID, userId);
        } else {
            editor.remove(KEY_USER_ID);
        }
        editor.putString(KEY_USERNAME, username);
        
        // 保存角色
        if (roles != null) {
            String rolesJson = gson.toJson(roles);
            editor.putString(KEY_ROLES, rolesJson);
        }
        
        // 保存权限
        if (permissions != null) {
            String permissionsJson = gson.toJson(permissions);
            editor.putString(KEY_PERMISSIONS, permissionsJson);
        }
        
        editor.apply();
    }

    public void clearUserData() {
        token = null;
        userId = null;
        username = null;
        roles = null;
        permissions = null;

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_ROLES);
        editor.remove(KEY_PERMISSIONS);
        editor.apply();
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }

    public void logout() {
        clearUserData();
    }

    public void clearAuth() {
        clearUserData();
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public void saveLoginInfo(LoginResponse response) {
        Log.d("AuthManager", "开始保存登录信息");
        SharedPreferences.Editor editor = prefs.edit();
        
        // 保存基本认证信息
        editor.putString(KEY_TOKEN, response.getToken());
        editor.putString(KEY_USER_ID, String.valueOf(response.getUserId()));
        editor.putString(KEY_USERNAME, response.getUsername());
        
        // 保存用户详细信息
        editor.putString(KEY_ROLE, response.getRole());
        editor.putString(KEY_EMAIL, response.getEmail());
        editor.putString(KEY_PHONE, response.getPhone());
        editor.putString(KEY_STUDENT_ID, response.getStudentId());
        editor.putString(KEY_DEPARTMENT, response.getDepartment());
        
        // 保存角色和权限信息
        if (response.getRoles() != null) {
            editor.putString(KEY_ROLES, gson.toJson(response.getRoles()));
        }
        if (response.getPermissions() != null) {
            editor.putString(KEY_PERMISSIONS, gson.toJson(response.getPermissions()));
        }
        
        boolean success = editor.commit();
        Log.d("AuthManager", "登录信息保存" + (success ? "成功" : "失败"));
        
        // 更新内存中的值
        loadUserData();
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    public String getAvatar() {
        return avatar;
    }
} 
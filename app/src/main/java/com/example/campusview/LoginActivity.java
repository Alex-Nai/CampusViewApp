package com.example.campusview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.campusview.api.ApiService;
import com.example.campusview.api.AuthInterceptor;
import com.example.campusview.api.ApiConfig;
import com.example.campusview.model.LoginRequest;
import com.example.campusview.model.LoginResponse;
import com.example.campusview.utils.AuthManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    
    private EditText usernameEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private Button registerButton;
    private CheckBox rememberCheckBox;
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 开始创建LoginActivity");
        
        // 设置布局
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: 设置布局完成");

        // 初始化AuthManager
        authManager = AuthManager.getInstance(this);
        Log.d(TAG, "onCreate: AuthManager初始化完成");

        // 初始化视图
        initViews();
        Log.d(TAG, "onCreate: 视图初始化完成");
        
        // 加载保存的用户名和密码
        loadSavedCredentials();
        Log.d(TAG, "onCreate: 加载保存的凭据完成");

        // 初始化Retrofit
        initRetrofit();
        Log.d(TAG, "onCreate: Retrofit初始化完成");

        // 设置按钮点击事件
        setupClickListeners();
        Log.d(TAG, "onCreate: 按钮点击事件设置完成");

        // 检查登录状态
        if (authManager.isLoggedIn()) {
            Log.d(TAG, "onCreate: 用户已登录，准备跳转到主界面");
            startMainActivity();
            finish();
            return;
        }
    }

    private void initViews() {
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
    }

    private void initRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        AuthInterceptor authInterceptor = new AuthInterceptor(this);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        registerButton.setOnClickListener(v -> startRegisterActivity());
    }

    private void loadSavedCredentials() {
        android.content.SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean remember = prefs.getBoolean(KEY_REMEMBER, false);
        rememberCheckBox.setChecked(remember);
        
        if (remember) {
            String username = prefs.getString(KEY_USERNAME, "");
            String password = prefs.getString(KEY_PASSWORD, "");
            usernameEdit.setText(username);
            passwordEdit.setText(password);
        }
    }

    private void saveCredentials(String username, String password) {
        android.content.SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        
        if (rememberCheckBox.isChecked()) {
            editor.putBoolean(KEY_REMEMBER, true);
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    private void attemptLogin() {
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存登录信息
        saveCredentials(username, password);

        LoginRequest request = new LoginRequest(username, password);
        Log.d(TAG, "开始登录请求，用户名: " + username);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "登录成功，保存登录信息");
                    authManager.saveLoginInfo(loginResponse);
                    Log.d(TAG, "准备跳转到主界面");
                    startMainActivity();
                } else {
                    String errorMessage = "登录失败: " + response.code();
                    Log.e(TAG, errorMessage);
                    try {
                        String errorBody = response.errorBody() != null ? 
                            response.errorBody().string() : "未知错误";
                        Toast.makeText(LoginActivity.this, "登录失败: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMessage = "网络错误: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        try {
            Log.d(TAG, "开始启动主界面");
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.d(TAG, "主界面启动成功，准备结束登录界面");
            finish();
        } catch (Exception e) {
            Log.e(TAG, "启动主活动时发生错误", e);
            Toast.makeText(this, "启动主活动失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
} 
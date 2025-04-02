package com.example.campusview.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.campusview.R;
import com.example.campusview.LoginActivity;
import com.example.campusview.api.ApiService;
import com.example.campusview.api.RetrofitClient;
import com.example.campusview.model.LoginResponse;
import com.example.campusview.model.UserUpdateDto;
import com.example.campusview.utils.AuthManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private CircleImageView avatarImage;
    private TextView usernameText;
    private TextView studentIdText;
    private Button editProfileButton;
    private Button changePasswordButton;
    private Button changeAvatarButton;
    private Button logoutButton;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // 初始化视图
        avatarImage = view.findViewById(R.id.avatar_image);
        usernameText = view.findViewById(R.id.username_text);
        studentIdText = view.findViewById(R.id.student_id_text);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        changePasswordButton = view.findViewById(R.id.change_password_button);
        changeAvatarButton = view.findViewById(R.id.change_avatar_button);
        logoutButton = view.findViewById(R.id.logout_button);

        apiService = RetrofitClient.getInstance(requireContext()).getApiService();
        
        // 加载用户信息
        loadUserInfo();
        
        // 设置头像点击事件
        changeAvatarButton.setOnClickListener(v -> showImagePicker());
        
        // 设置修改个人信息按钮点击事件
        editProfileButton.setOnClickListener(v -> showEditProfileDialog());
        
        // 设置修改密码按钮点击事件
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());
        
        // 设置退出登录按钮点击事件
        logoutButton.setOnClickListener(v -> logout());
        
        return view;
    }

    private void loadUserInfo() {
        Long userId = AuthManager.getInstance(requireContext()).getUserId();
        if (userId != null) {
            apiService.getUserInfo(userId).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        LoginResponse userInfo = response.body();
                        updateUI(userInfo);
                    } else {
                        String errorMessage = "获取用户信息失败: " + response.code();
                        if (response.errorBody() != null) {
                            try {
                                errorMessage += "\n" + response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    String errorMessage = "网络错误: " + t.getMessage();
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getContext(), "用户ID为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(LoginResponse userInfo) {
        if (userInfo == null) {
            Toast.makeText(getContext(), "用户信息为空", Toast.LENGTH_SHORT).show();
            return;
        }

        usernameText.setText(userInfo.getUsername() != null ? userInfo.getUsername() : "未设置");
        studentIdText.setText(userInfo.getStudentId() != null ? userInfo.getStudentId() : "未设置");

        // 加载头像
        if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(userInfo.getAvatar())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(avatarImage);
        } else {
            avatarImage.setImageResource(R.drawable.ic_person);
        }
    }

    private void showImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadAvatar(imageUri);
        }
    }

    private void uploadAvatar(Uri imageUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            File file = new File(getContext().getCacheDir(), "avatar.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

            Long userId = AuthManager.getInstance(requireContext()).getUserId();
            if (userId != null) {
                apiService.uploadAvatar(userId, avatarPart).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                            updateUI(response.body());
                        } else {
                            Toast.makeText(getContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "文件处理失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        EditText emailInput = dialogView.findViewById(R.id.email_input);
        EditText realNameInput = dialogView.findViewById(R.id.real_name_input);
        EditText phoneInput = dialogView.findViewById(R.id.phone_input);
        EditText departmentInput = dialogView.findViewById(R.id.department_input);
        Button saveButton = dialogView.findViewById(R.id.save_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(v -> {
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setEmail(emailInput.getText().toString());
            updateDto.setRealName(realNameInput.getText().toString());
            updateDto.setPhone(phoneInput.getText().toString());
            updateDto.setDepartment(departmentInput.getText().toString());

            Long userId = AuthManager.getInstance(requireContext()).getUserId();
            if (userId != null) {
                apiService.updateUser(userId, updateDto).enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                            updateUI(response.body());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);

        EditText currentPasswordInput = dialogView.findViewById(R.id.current_password_input);
        EditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
        EditText confirmPasswordInput = dialogView.findViewById(R.id.confirm_password_input);
        Button confirmButton = dialogView.findViewById(R.id.confirm_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        confirmButton.setOnClickListener(v -> {
            String currentPassword = currentPasswordInput.getText().toString();
            String newPassword = newPasswordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                UserUpdateDto updateDto = new UserUpdateDto();
                updateDto.setPassword(currentPassword);
                updateDto.setNewPassword(newPassword);

                Long userId = AuthManager.getInstance(requireContext()).getUserId();
                if (userId != null) {
                    apiService.updateUser(userId, updateDto).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "密码修改失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        if (getContext() != null) {
            AuthManager.getInstance(getContext()).clearAuth();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
} 
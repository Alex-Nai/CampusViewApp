package com.example.campusview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.campusview.api.ApiService;
import com.example.campusview.config.ApiConfig;
import com.example.campusview.model.ScenicSpotResponse;
import com.google.android.material.button.MaterialButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.File;

public class ScenicSpotActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private MaterialButton selectButton;
    private MaterialButton recognizeButton;
    private Uri selectedImageUri;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenic_spot);

        // 初始化视图
        imageView = findViewById(R.id.imageView);
        selectButton = findViewById(R.id.selectButton);
        recognizeButton = findViewById(R.id.recognizeButton);

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // 设置按钮点击事件
        selectButton.setOnClickListener(v -> selectImage());
        recognizeButton.setOnClickListener(v -> recognizeScenicSpot());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    private void recognizeScenicSpot() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        // 将Uri转换为File
        String[] projection = {MediaStore.Images.Media.DATA};
        android.database.Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            apiService.recognizeScenicSpot(body).enqueue(new Callback<ScenicSpotResponse>() {
                @Override
                public void onResponse(Call<ScenicSpotResponse> call, Response<ScenicSpotResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ScenicSpotResponse scenicSpotResponse = response.body();
                        if (scenicSpotResponse.isSuccess() && scenicSpotResponse.getData() != null && !scenicSpotResponse.getData().isEmpty()) {
                            ScenicSpotResponse.ScenicSpot scenicSpot = scenicSpotResponse.getData().get(0);
                            Toast.makeText(ScenicSpotActivity.this, 
                                "识别结果：" + scenicSpot.getName() + "\n" + scenicSpot.getDescription(), 
                                Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ScenicSpotActivity.this, "识别失败：" + scenicSpotResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ScenicSpotActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ScenicSpotResponse> call, Throwable t) {
                    Toast.makeText(ScenicSpotActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
} 
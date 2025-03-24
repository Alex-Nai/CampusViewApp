package com.example.campusview;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.example.campusview.api.ApiService;
import com.example.campusview.config.ApiConfig;
import com.example.campusview.model.NavigationResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends AppCompatActivity {
    private MapView mapView;
    private AMap aMap;
    private TextInputEditText startPointInput;
    private TextInputEditText endPointInput;
    private MaterialButton searchButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // 初始化地图
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        // 初始化输入框和按钮
        startPointInput = findViewById(R.id.startPointInput);
        endPointInput = findViewById(R.id.endPointInput);
        searchButton = findViewById(R.id.searchButton);

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // 设置搜索按钮点击事件
        searchButton.setOnClickListener(v -> searchRoute());
    }

    private void searchRoute() {
        String startPoint = startPointInput.getText().toString();
        String endPoint = endPointInput.getText().toString();

        if (startPoint.isEmpty() || endPoint.isEmpty()) {
            Toast.makeText(this, "请输入起点和终点", Toast.LENGTH_SHORT).show();
            return;
        }

        // 这里应该先通过地点名称获取经纬度，然后再请求路线
        // 为了演示，这里使用固定的经纬度
        double startLat = 39.9087;
        double startLng = 116.3975;
        double endLat = 39.9087;
        double endLng = 116.3975;

        apiService.getNavigationRoute(startLat, startLng, endLat, endLng)
                .enqueue(new Callback<NavigationResponse>() {
                    @Override
                    public void onResponse(Call<NavigationResponse> call, Response<NavigationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            displayRoute(response.body());
                        } else {
                            Toast.makeText(NavigationActivity.this, "获取路线失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NavigationResponse> call, Throwable t) {
                        Toast.makeText(NavigationActivity.this, "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayRoute(NavigationResponse response) {
        if (response.getData() != null && !response.getData().isEmpty()) {
            PolylineOptions polylineOptions = new PolylineOptions();
            for (NavigationResponse.NavigationStep step : response.getData()) {
                for (NavigationResponse.NavigationStep.LatLng latLng : step.getPath()) {
                    polylineOptions.add(new LatLng(latLng.getLatitude(), latLng.getLongitude()));
                }
            }
            polylineOptions.width(10).color(getResources().getColor(android.R.color.holo_blue_dark));
            aMap.addPolyline(polylineOptions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
} 
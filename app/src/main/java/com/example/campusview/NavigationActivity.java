package com.example.campusview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.UiSettings;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkStep;
import com.example.campusview.api.ApiService;
import com.example.campusview.api.ApiConfig;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int PICK_IMAGE_REQUEST = 1002;
    private static final String[] REQUIRED_PERMISSIONS = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private MapView mapView;
    private AMap aMap;
    private TextInputEditText startPointInput;
    private TextInputEditText endPointInput;
    private MaterialButton searchButton;
    private MaterialButton uploadImageButton;
    private CardView navigationTipCard;
    private TextView navigationTipText;
    private ApiService apiService;
    private List<LatLng> routePoints = new ArrayList<>();
    private RouteSearch routeSearch;
    private int currentRouteIndex = 0;

    // 西安交通大学主要地点坐标
    private static final LatLng XJTU_CENTER = new LatLng(34.246033, 108.984598);
    private static final LatLng LIBRARY = new LatLng(34.246033, 108.984598); // 图书馆
    private static final LatLng MAIN_BUILDING = new LatLng(34.247033, 108.985598); // 主楼
    private static final LatLng TEACHING_BUILDING = new LatLng(34.245033, 108.983598); // 教学楼
    private static final LatLng STUDENT_CENTER = new LatLng(34.248033, 108.986598); // 学生中心

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        try {
            // 设置Toolbar
            androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationOnClickListener(v -> onBackPressed());

            // 检查并请求必要的权限
            checkAndRequestPermissions();

            // 初始化地图
            mapView = findViewById(R.id.mapView);
            if (mapView == null) {
                Toast.makeText(this, "地图视图初始化失败", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                mapView.onCreate(savedInstanceState);
                aMap = mapView.getMap();
                
                if (aMap == null) {
                    Toast.makeText(this, "地图对象初始化失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 设置地图UI控件
                UiSettings uiSettings = aMap.getUiSettings();
                if (uiSettings != null) {
                    uiSettings.setZoomControlsEnabled(true);
                    uiSettings.setZoomPosition(1);
                    uiSettings.setScaleControlsEnabled(true);
                    uiSettings.setCompassEnabled(true);
                }

                // 设置地图初始位置为西安交通大学中心，缩放级别为17
                try {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(XJTU_CENTER)
                            .zoom(17)
                            .build();
                    aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newCameraPosition(cameraPosition));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "地图视角设置失败", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "地图初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            // 初始化视图
            startPointInput = findViewById(R.id.startPointInput);
            endPointInput = findViewById(R.id.endPointInput);
            searchButton = findViewById(R.id.searchButton);
            uploadImageButton = findViewById(R.id.uploadImageButton);
            navigationTipCard = findViewById(R.id.navigationTipView);
            navigationTipText = findViewById(R.id.navigationTipText);

            if (startPointInput == null || endPointInput == null || searchButton == null || 
                uploadImageButton == null || navigationTipCard == null || navigationTipText == null) {
                Toast.makeText(this, "界面组件初始化失败", Toast.LENGTH_SHORT).show();
                return;
            }

            // 初始化Retrofit
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                apiService = retrofit.create(ApiService.class);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "网络服务初始化失败", Toast.LENGTH_SHORT).show();
            }

            // 初始化路线搜索
            try {
                routeSearch = new RouteSearch(this);
                routeSearch.setRouteSearchListener(this);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "路线搜索初始化失败", Toast.LENGTH_SHORT).show();
            }

            // 设置上传图片按钮点击事件
            uploadImageButton.setOnClickListener(v -> {
                if (checkPermissions()) {
                    openImagePicker();
                } else {
                    requestPermissions();
                }
            });

            // 设置搜索按钮点击事件
            searchButton.setOnClickListener(v -> {
                String startPoint = startPointInput.getText().toString();
                String endPoint = endPointInput.getText().toString();
                if (startPoint.isEmpty() || endPoint.isEmpty()) {
                    Toast.makeText(this, "请输入起点和终点", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyboard();
                searchRoute(startPoint, endPoint);
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void searchRoute(String start, String end) {
        try {
            if (aMap == null) {
                Toast.makeText(this, "地图未初始化", Toast.LENGTH_SHORT).show();
                return;
            }

            // 清除地图上的所有标记和路线
            aMap.clear();
            routePoints.clear();
            currentRouteIndex = 0;

            // 根据输入的地点选择对应的坐标
            LatLng startPoint = null;
            LatLng endPoint = null;

            switch (start.toLowerCase()) {
                case "图书馆":
                    startPoint = LIBRARY;
                    break;
                case "主楼":
                    startPoint = MAIN_BUILDING;
                    break;
                case "教学楼":
                    startPoint = TEACHING_BUILDING;
                    break;
                case "学生中心":
                    startPoint = STUDENT_CENTER;
                    break;
                default:
                    Toast.makeText(this, "起点位置无效", Toast.LENGTH_SHORT).show();
                    return;
            }

            switch (end.toLowerCase()) {
                case "图书馆":
                    endPoint = LIBRARY;
                    break;
                case "主楼":
                    endPoint = MAIN_BUILDING;
                    break;
                case "教学楼":
                    endPoint = TEACHING_BUILDING;
                    break;
                case "学生中心":
                    endPoint = STUDENT_CENTER;
                    break;
                default:
                    Toast.makeText(this, "终点位置无效", Toast.LENGTH_SHORT).show();
                    return;
            }

            // 创建步行路线查询
            FromAndTo fromAndTo = new FromAndTo(
                new LatLonPoint(startPoint.latitude, startPoint.longitude),
                new LatLonPoint(endPoint.latitude, endPoint.longitude)
            );

            WalkRouteQuery query = new WalkRouteQuery(fromAndTo);
            routeSearch.calculateWalkRouteAsyn(query);

            // 添加起点和终点标记
            aMap.addMarker(new MarkerOptions()
                .position(startPoint)
                .title("起点")
                .snippet(start));
            aMap.addMarker(new MarkerOptions()
                .position(endPoint)
                .title("终点")
                .snippet(end));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "路线规划失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null && !result.getPaths().isEmpty()) {
                WalkPath walkPath = result.getPaths().get(0);
                List<WalkStep> walkSteps = walkPath.getSteps();
                
                // 清除之前的路线点
                routePoints.clear();
                
                // 添加路线点
                for (WalkStep step : walkSteps) {
                    List<LatLonPoint> points = step.getPolyline();
                    for (LatLonPoint point : points) {
                        routePoints.add(new LatLng(point.getLatitude(), point.getLongitude()));
                    }
                }

                // 绘制路线
                PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(routePoints)
                    .width(10)
                    .color(0xFF2196F3);
                aMap.addPolyline(polylineOptions);

                // 调整地图视野以显示整个路线
                aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds.Builder()
                        .include(routePoints.get(0))
                        .include(routePoints.get(routePoints.size() - 1))
                        .build(),
                    100
                ));

                // 显示路线信息
                String distance = String.format("%.0f", walkPath.getDistance());
                String duration = String.format("%.0f", walkPath.getDuration() / 60);
                String tip = String.format("总距离：%s米，预计时间：%s分钟", distance, duration);
                navigationTipText.setText(tip);
                navigationTipCard.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "未找到合适的步行路线", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "路线规划失败: " + errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    // 实现其他必要的接口方法
    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {}

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {}

    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {}

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void checkAndRequestPermissions() {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                openImagePicker();
            } else {
                Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                uploadImage(selectedImageUri);
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        // 实现图片上传逻辑
        Toast.makeText(this, "图片上传功能待实现", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }
} 
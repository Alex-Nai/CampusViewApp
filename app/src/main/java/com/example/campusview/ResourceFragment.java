package com.example.campusview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.adapter.ResourceAdapter;
import com.example.campusview.api.ApiService;
import com.example.campusview.api.AuthInterceptor;
import com.example.campusview.api.ApiConfig;
import com.example.campusview.model.Resource;
import com.example.campusview.model.BookingDto;
import com.example.campusview.utils.AuthManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import okhttp3.OkHttpClient;

public class ResourceFragment extends Fragment implements ResourceAdapter.OnItemClickListener, ResourceAdapter.BookingStatusProvider {
    private static final String TAG = "ResourceFragment";
    private RecyclerView recyclerView;
    private ResourceAdapter adapter;
    private List<Resource> resourceList = new ArrayList<>();
    private ApiService apiService;
    private Map<Long, Boolean> resourceBookingStatus = new HashMap<>();
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        
        // 初始化AuthManager
        authManager = AuthManager.getInstance(requireContext());
        
        // 检查登录状态
        if (!authManager.isLoggedIn()) {
            Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return view;
        }
        
        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.resourceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResourceAdapter(resourceList, this, this);
        recyclerView.setAdapter(adapter);

        // 初始化Retrofit
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(getContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // 加载资源列表
        loadResources();
        
        // 加载预约状态
        loadBookingStatus();

        return view;
    }

    private void loadResources() {
        Log.d(TAG, "开始加载资源列表，API地址: " + ApiConfig.BASE_URL);
        Log.d(TAG, "当前token: " + authManager.getToken());
        
        // 检查网络连接
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 加载非教室资源
        apiService.getNonClassroomResources().enqueue(new Callback<List<Resource>>() {
            @Override
            public void onResponse(Call<List<Resource>> call, Response<List<Resource>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Resource> resources = response.body();
                    Log.d(TAG, "成功获取非教室资源列表，数量: " + resources.size());
                    
                    // 打印每个资源的详细信息
                    for (Resource resource : resources) {
                        Log.d(TAG, String.format("资源详情 - ID: %d, 名称: %s, 类型ID: %s, 位置: %s, 描述: %s, 可用: %b",
                            resource.getId(), 
                            resource.getName(), 
                            resource.getTypeId() != null ? resource.getTypeId() : "null",
                            resource.getLocation(), 
                            resource.getDescription(), 
                            resource.getAvailable()));
                    }
                    
                    // 在主线程中更新UI
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                resourceList.clear();
                                resourceList.addAll(resources);
                                Log.d(TAG, "更新后的resourceList大小: " + resourceList.size());
                                
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                    Log.d(TAG, "UI已更新，RecyclerView中的项目数: " + adapter.getItemCount());
                                } else {
                                    Log.e(TAG, "adapter为null");
                                }
                                
                                // 检查RecyclerView的状态
                                if (recyclerView != null) {
                                    Log.d(TAG, "RecyclerView可见性: " + recyclerView.getVisibility());
                                    Log.d(TAG, "RecyclerView高度: " + recyclerView.getHeight());
                                    Log.d(TAG, "RecyclerView是否可见: " + recyclerView.isShown());
                                    
                                    // 确保RecyclerView可见
                                    recyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    Log.e(TAG, "recyclerView为null");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "更新UI时发生错误", e);
                            }
                        });
                    }
                    
                    // 加载预约状态
                    loadBookingStatus();
                } else {
                    String errorMessage = "加载资源列表失败: " + response.code();
                    Log.e(TAG, errorMessage);
                    if (response.code() == 401) {
                        // 处理未授权错误
                        authManager.clearUserData();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Resource>> call, Throwable t) {
                String errorMessage = "网络请求失败: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) getContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            android.net.NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void loadBookingStatus() {
        apiService.getMyBookings().enqueue(new Callback<List<BookingDto>>() {
            @Override
            public void onResponse(Call<List<BookingDto>> call, Response<List<BookingDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resourceBookingStatus.clear();
                    for (BookingDto booking : response.body()) {
                        // 只标记未取消的预约
                        if (!"CANCELLED".equals(booking.getStatus())) {
                            resourceBookingStatus.put(booking.getResourceId(), true);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BookingDto>> call, Throwable t) {
                Log.e(TAG, "加载预约状态失败", t);
            }
        });
    }

    @Override
    public void onItemClick(Resource resource) {
        Log.d(TAG, "点击资源: " + resource.getName());
        Toast.makeText(getContext(), "点击了: " + resource.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookClick(Resource resource) {
        if (!resource.getAvailable()) {
            Toast.makeText(getContext(), "该资源当前不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        // 检查资源是否已被预约
        if (resourceBookingStatus.containsKey(resource.getId())) {
            Toast.makeText(getContext(), "该资源已被预约", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建预约对话框
        showBookingDialog(resource);
    }

    @Override
    public void onCancelClick(Resource resource) {
        // 查找该资源的预约
        apiService.getMyBookings().enqueue(new Callback<List<BookingDto>>() {
            @Override
            public void onResponse(Call<List<BookingDto>> call, Response<List<BookingDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (BookingDto booking : response.body()) {
                        if (booking.getResourceId().equals(resource.getId()) && 
                            !booking.getStatus().equals("CANCELLED")) {
                            cancelBooking(booking.getId());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingDto>> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelBooking(Long bookingId) {
        apiService.cancelBooking(bookingId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "取消预约成功", Toast.LENGTH_SHORT).show();
                    loadBookingStatus();
                } else {
                    Toast.makeText(getContext(), "取消预约失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBookingDialog(Resource resource) {
        // 创建预约对话框
        BookingDialogFragment dialog = BookingDialogFragment.newInstance(resource);
        dialog.setBookingListener(new BookingDialogFragment.BookingListener() {
            @Override
            public void onBookingSuccess() {
                Toast.makeText(getContext(), "预约成功", Toast.LENGTH_SHORT).show();
                loadBookingStatus();
            }

            @Override
            public void onBookingFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getChildFragmentManager(), "booking_dialog");
    }

    @Override
    public boolean isResourceBooked(Long resourceId) {
        return resourceBookingStatus.containsKey(resourceId);
    }
} 
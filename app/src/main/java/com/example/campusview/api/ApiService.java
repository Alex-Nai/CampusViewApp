package com.example.campusview.api;

import com.example.campusview.model.Classroom;
import com.example.campusview.model.Resource;
import com.example.campusview.model.ScenicSpotResponse;
import com.example.campusview.model.ScenicSpotResponse.ScenicSpot;
import com.example.campusview.model.NavigationResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {
    // 获取教室列表
    @GET("classrooms")
    Call<List<Classroom>> getClassrooms();

    // 获取资源列表
    @GET("resources")
    Call<List<Resource>> getResources();

    // 预约教室
    @POST("classrooms/{id}/book")
    Call<ResponseBody> bookClassroom(@Path("id") String classroomId);

    // 预约资源
    @POST("resources/{id}/book")
    Call<ResponseBody> bookResource(@Path("id") String resourceId);

    // 识别景点
    @Multipart
    @POST("scenic/recognize")
    Call<ScenicSpotResponse> recognizeScenicSpot(@Part MultipartBody.Part image);

    // 获取导航路线
    @GET("navigation/route")
    Call<NavigationResponse> getNavigationRoute(
            @Query("startLat") double startLat,
            @Query("startLng") double startLng,
            @Query("endLat") double endLat,
            @Query("endLng") double endLng
    );
} 
package com.example.campusview.api;

import com.example.campusview.model.BookingCreateRequest;
import com.example.campusview.model.Classroom;
import com.example.campusview.model.Resource;
import com.example.campusview.model.ScenicSpotResponse;
import com.example.campusview.model.ScenicSpotResponse.ScenicSpot;
import com.example.campusview.model.NavigationResponse;
import com.example.campusview.model.BookingDto;
import com.example.campusview.model.LoginRequest;
import com.example.campusview.model.LoginResponse;
import com.example.campusview.model.RegisterRequest;
import com.example.campusview.model.RegisterResponse;
import com.example.campusview.model.UserUpdateDto;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {
    // 认证相关接口
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest);

    // 资源相关接口
    @GET("/resource")
    Call<List<Resource>> getAllResources();

    @GET("/resource/other")
    Call<List<Resource>> getNonClassroomResources();

    @GET("/resource/{id}")
    Call<Resource> getResource(@Path("id") Long id);

    @GET("/resource/available")
    Call<List<Resource>> getAvailableResources();

    @GET("/resource/type/{type}")
    Call<List<Resource>> getResourcesByType(@Path("type") String type);

    @GET("/resource/available/type/{type}")
    Call<List<Resource>> getAvailableResourcesByType(@Path("type") String type);

    @GET("/resource/available/timeslot")
    Call<List<Resource>> getAvailableResourcesForTimeSlot(
            @Query("startTime") String startTime,
            @Query("endTime") String endTime
    );

    @GET("/api/booking/my")
    Call<List<BookingDto>> getMyBookings();

    @GET("/api/booking/resource/{resourceId}")
    Call<List<BookingDto>> getBookingsByResource(@Path("resourceId") Long resourceId);

    @POST("/api/booking")
    Call<BookingDto> createBooking(@Body BookingCreateRequest request);

    @DELETE("/api/booking/{bookingId}")
    Call<Void> cancelBooking(@Path("bookingId") Long bookingId);

    @PUT("/api/booking/{bookingId}/status")
    Call<BookingDto> updateBookingStatus(@Path("bookingId") Long bookingId, @Query("status") String status);

    // 管理员相关接口
    @GET("/api/admin/bookings")
    Call<List<BookingDto>> getAllBookings();

    @PUT("/api/admin/bookings/{id}/approve")
    Call<BookingDto> approveBooking(@Path("id") Long bookingId);

    @PUT("/api/admin/bookings/{id}/reject")
    Call<BookingDto> rejectBooking(@Path("id") Long bookingId);

    // 用户权限相关接口
    @GET("/api/user/roles")
    Call<List<String>> getUserRoles();

    @GET("/api/user/permissions")
    Call<List<String>> getUserPermissions();

    // 景点识别相关接口
    @Multipart
    @POST("/api/scenic/recognize")
    Call<ScenicSpotResponse> recognizeScenicSpot(@Part MultipartBody.Part image);

    // 导航相关接口
    @GET("/api/navigation/route")
    Call<NavigationResponse> getNavigationRoute(
            @Query("startLat") double startLat,
            @Query("startLng") double startLng,
            @Query("endLat") double endLat,
            @Query("endLng") double endLng
    );

    // 用户信息相关接口
    @GET("/api/users/{userId}")
    Call<LoginResponse> getUserInfo(@Path("userId") Long userId);

    @PUT("/api/users/{userId}")
    Call<LoginResponse> updateUser(@Path("userId") Long userId, @Body UserUpdateDto updateDto);

    @Multipart
    @POST("/api/users/{userId}/avatar")
    Call<LoginResponse> uploadAvatar(
            @Path("userId") Long userId,
            @Part MultipartBody.Part avatar
    );
} 
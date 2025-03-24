package com.example.campusview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusview.adapter.ClassroomAdapter;
import com.example.campusview.api.ApiService;
import com.example.campusview.config.ApiConfig;
import com.example.campusview.model.Classroom;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;
import java.util.List;

public class ClassroomFragment extends Fragment implements ClassroomAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ClassroomAdapter adapter;
    private List<Classroom> classroomList = new ArrayList<>();
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classroom, container, false);
        
        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.classroomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClassroomAdapter(classroomList, this);
        recyclerView.setAdapter(adapter);

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // 加载教室列表
        loadClassrooms();

        return view;
    }

    private void loadClassrooms() {
        apiService.getClassrooms().enqueue(new Callback<List<Classroom>>() {
            @Override
            public void onResponse(Call<List<Classroom>> call, Response<List<Classroom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    classroomList.clear();
                    classroomList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "加载教室列表失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Classroom>> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBookClick(Classroom classroom) {
        bookClassroom(classroom.getId());
    }

    private void bookClassroom(String classroomId) {
        apiService.bookClassroom(classroomId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "预约成功", Toast.LENGTH_SHORT).show();
                    loadClassrooms(); // 刷新列表
                } else {
                    Toast.makeText(getContext(), "预约失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "网络请求失败: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 
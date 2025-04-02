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
import com.example.campusview.api.ApiConfig;
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

        // 加载测试数据
        loadTestData();

        return view;
    }

    private void loadTestData() {
        // 添加测试数据
        classroomList.add(new Classroom("1", "主楼A-204", "普通教室", "主楼A区2层", 50, true));
        classroomList.add(new Classroom("2", "主楼B-304", "多媒体教室", "主楼B区3层", 100, false));
        classroomList.add(new Classroom("3", "中楼三-2303", "实验室", "中楼三区23层", 30, true));
        classroomList.add(new Classroom("4", "主楼A-101", "阶梯教室", "主楼A区1层", 200, true));
        classroomList.add(new Classroom("5", "主楼B-205", "普通教室", "主楼B区2层", 60, false));
        classroomList.add(new Classroom("6", "中楼三-1502", "多媒体教室", "中楼三区15层", 80, true));
        classroomList.add(new Classroom("7", "主楼A-305", "实验室", "主楼A区3层", 40, true));
        classroomList.add(new Classroom("8", "主楼B-406", "普通教室", "主楼B区4层", 70, false));
        classroomList.add(new Classroom("9", "中楼三-1801", "阶梯教室", "中楼三区18层", 150, true));
        classroomList.add(new Classroom("10", "主楼A-502", "多媒体教室", "主楼A区5层", 90, true));
        
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBookClick(Classroom classroom) {
        // 模拟预约成功
        Toast.makeText(getContext(), "预约教室 " + classroom.getName() + " 成功", Toast.LENGTH_SHORT).show();
        classroom.setAvailable(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelClick(Classroom classroom) {
        // 模拟取消预约成功
        Toast.makeText(getContext(), "取消预约教室 " + classroom.getName() + " 成功", Toast.LENGTH_SHORT).show();
        classroom.setAvailable(true);
        adapter.notifyDataSetChanged();
    }
} 
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
import java.util.ArrayList;
import java.util.List;

public class ResourceFragment extends Fragment {
    private RecyclerView recyclerView;
    private ResourceAdapter adapter;
    private List<Resource> resourceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        recyclerView = view.findViewById(R.id.resourceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 初始化数据
        resourceList = new ArrayList<>();
        loadResourceData();
        
        adapter = new ResourceAdapter(resourceList, new ResourceAdapter.OnItemClickListener() {
            @Override
            public void onBookClick(Resource resource) {
                // TODO: 实现预约功能
                Toast.makeText(getContext(), "预约资源: " + resource.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadResourceData() {
        // TODO: 从后端API加载资源数据
        // 这里先添加一些测试数据
        resourceList.add(new Resource("讲座厅A", "讲座", "可用", "可容纳200人的大型讲座厅"));
        resourceList.add(new Resource("实验室B", "实验室", "可用", "配备专业实验设备的实验室"));
        resourceList.add(new Resource("会议室C", "会议室", "可用", "适合小组讨论的会议室"));
        resourceList.add(new Resource("活动室D", "活动室", "可用", "适合举办各类活动的多功能厅"));
    }
} 
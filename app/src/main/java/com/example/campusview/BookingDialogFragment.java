package com.example.campusview;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.campusview.api.ApiService;
import com.example.campusview.api.AuthInterceptor;
import com.example.campusview.api.ApiConfig;
import com.example.campusview.model.Resource;
import com.example.campusview.model.BookingCreateRequest;
import com.example.campusview.model.BookingDto;
import com.example.campusview.utils.AuthManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import android.content.DialogInterface;

public class BookingDialogFragment extends DialogFragment {
    private static final String ARG_RESOURCE = "resource";
    private Resource resource;
    private BookingListener listener;
    private TextView startTimeText;
    private TextView endTimeText;
    private EditText purposeEditText;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ApiService apiService;
    private AuthManager authManager;

    public interface BookingListener {
        void onBookingSuccess();
        void onBookingFailure(String message);
    }

    public static BookingDialogFragment newInstance(Resource resource) {
        BookingDialogFragment fragment = new BookingDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESOURCE, resource);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resource = (Resource) getArguments().getSerializable(ARG_RESOURCE);
        }
        
        // 初始化AuthManager
        authManager = AuthManager.getInstance(requireContext());
        
        // 初始化Retrofit
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(requireContext()))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_booking, null);

        // 初始化视图
        startTimeText = view.findViewById(R.id.startTimeText);
        endTimeText = view.findViewById(R.id.endTimeText);
        purposeEditText = view.findViewById(R.id.purposeEditText);
        Button selectStartTimeButton = view.findViewById(R.id.selectStartTimeButton);
        Button selectEndTimeButton = view.findViewById(R.id.selectEndTimeButton);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // 设置标题
        TextView titleText = view.findViewById(R.id.titleText);
        titleText.setText("预约 " + resource.getName());

        // 设置时间选择器
        selectStartTimeButton.setOnClickListener(v -> showDateTimePicker(true));
        selectEndTimeButton.setOnClickListener(v -> showDateTimePicker(false));

        // 设置确认按钮
        confirmButton.setOnClickListener(v -> {
            if (validateInput()) {
                createBooking();
            }
        });

        // 设置取消按钮
        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        return builder.create();
    }

    private void showDateTimePicker(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        if (isStartTime && startTime != null) {
            calendar.set(startTime.getYear(), startTime.getMonthValue() - 1, startTime.getDayOfMonth(),
                    startTime.getHour(), startTime.getMinute());
        } else if (!isStartTime && endTime != null) {
            calendar.set(endTime.getYear(), endTime.getMonthValue() - 1, endTime.getDayOfMonth(),
                    endTime.getHour(), endTime.getMinute());
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            (view1, hourOfDay, minute) -> {
                                LocalDateTime selectedTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute);
                                if (isStartTime) {
                                    startTime = selectedTime;
                                    startTimeText.setText("开始时间：" + formatDateTime(startTime));
                                } else {
                                    endTime = selectedTime;
                                    endTimeText.setText("结束时间：" + formatDateTime(endTime));
                                }
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // 设置最小日期为今天
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private boolean validateInput() {
        if (startTime == null) {
            Toast.makeText(getContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endTime == null) {
            Toast.makeText(getContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTime.isAfter(endTime)) {
            Toast.makeText(getContext(), "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            Toast.makeText(getContext(), "开始时间不能早于当前时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        String purpose = purposeEditText.getText().toString().trim();
        if (purpose.isEmpty()) {
            Toast.makeText(getContext(), "请输入预约用途", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createBooking() {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setResourceId(resource.getId());
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setPurpose(purposeEditText.getText().toString().trim());

        apiService.createBooking(request).enqueue(new Callback<BookingDto>() {
            @Override
            public void onResponse(Call<BookingDto> call, Response<BookingDto> response) {
                if (response.isSuccessful()) {
                    if (listener != null) {
                        listener.onBookingSuccess();
                    }
                    dismiss();
                } else {
                    String errorMessage = "预约失败: " + response.code();
                    if (listener != null) {
                        listener.onBookingFailure(errorMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingDto> call, Throwable t) {
                String errorMessage = "网络错误: " + t.getMessage();
                if (listener != null) {
                    listener.onBookingFailure(errorMessage);
                }
            }
        });
    }

    public void setBookingListener(BookingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        // 确保在视图销毁时关闭输入法
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && purposeEditText != null) {
            imm.hideSoftInputFromWindow(purposeEditText.getWindowToken(), 0);
        }
        super.onDestroyView();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        // 确保在对话框关闭时关闭输入法
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && purposeEditText != null) {
            imm.hideSoftInputFromWindow(purposeEditText.getWindowToken(), 0);
        }
        super.onDismiss(dialog);
    }
} 
package com.example.lab_2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Khai báo các widget
    private TextView textViewTitle, textViewResult, textViewInstruction;
    private Button buttonClick, buttonSubmit, buttonStartProgress;
    private ImageView imageViewDemo;
    private EditText editTextName;
    private CheckBox checkBoxJava, checkBoxKotlin, checkBoxFlutter;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private ProgressBar progressBarLoading;
    private LinearLayout layoutResult;

    private int progressStatus = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các widget
        initializeViews();
        // Thiết lập các sự kiện
        setupEventListeners();
    }

    private void initializeViews() {
        // TextView - Hiển thị văn bản
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewResult = findViewById(R.id.textViewResult);
        textViewInstruction = findViewById(R.id.textViewInstruction);

        // Button - Nút bấm
        buttonClick = findViewById(R.id.buttonClick);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonStartProgress = findViewById(R.id.buttonStartProgress);

        // ImageView - Hiển thị hình ảnh
        imageViewDemo = findViewById(R.id.imageViewDemo);

        // EditText - Ô nhập liệu
        editTextName = findViewById(R.id.editTextName);

        // CheckBox - Hộp kiểm
        checkBoxJava = findViewById(R.id.checkBoxJava);
        checkBoxKotlin = findViewById(R.id.checkBoxKotlin);
        checkBoxFlutter = findViewById(R.id.checkBoxFlutter);

        // RadioButton & RadioGroup - Nút chọn
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);

        // ProgressBar - Thanh tiến trình
        progressBarLoading = findViewById(R.id.progressBarLoading);

        // Layout hiển thị kết quả
        layoutResult = findViewById(R.id.layoutResult);
    }

    private void setupEventListeners() {
        // Sự kiện Button Click
        buttonClick.setOnClickListener(v -> {
            textViewTitle.setText("Bạn đã nhấn nút!");
            textViewTitle.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
            Toast.makeText(MainActivity.this, "Button được click!", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện ImageView Click - Thay đổi hình ảnh khi click
        imageViewDemo.setOnClickListener(new View.OnClickListener() {
            private int imageIndex = 0;
            private final int[] images = {
                    // Các icon này có sẵn trong Android SDK, không cần thêm vào
                    android.R.drawable.ic_menu_camera,
                    android.R.drawable.ic_menu_gallery,
                    android.R.drawable.ic_menu_edit,
                    android.R.drawable.ic_menu_view
            };

            @Override
            public void onClick(View v) {
                imageIndex = (imageIndex + 1) % images.length;
                imageViewDemo.setImageResource(images[imageIndex]);
                Toast.makeText(MainActivity.this, "Đã thay đổi hình ảnh!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện Submit Button
        buttonSubmit.setOnClickListener(v -> collectAndDisplayData());

        // Sự kiện CheckBox Java
        checkBoxJava.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this, "Đã chọn Java", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện CheckBox Kotlin
        checkBoxKotlin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this, "Đã chọn Kotlin", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện CheckBox Flutter
        checkBoxFlutter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this, "Đã chọn Flutter", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện RadioGroup
        radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonMale) {
                Toast.makeText(MainActivity.this, "Đã chọn Nam", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.radioButtonFemale) {
                Toast.makeText(MainActivity.this, "Đã chọn Nữ", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện ProgressBar Button
        buttonStartProgress.setOnClickListener(v -> startProgressBar());
    }

    private void collectAndDisplayData() {
        // Lấy dữ liệu từ EditText
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            editTextName.setError("Tên không được để trống");
            return;
        }

        // Lấy dữ liệu từ CheckBox
        StringBuilder languages = new StringBuilder();
        if (checkBoxJava.isChecked()) languages.append("Java ");
        if (checkBoxKotlin.isChecked()) languages.append("Kotlin ");
        if (checkBoxFlutter.isChecked()) languages.append("Flutter ");

        // Lấy dữ liệu từ RadioButton
        String gender = "Chưa chọn";
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == R.id.radioButtonMale) {
            gender = "Nam";
        } else if (selectedId == R.id.radioButtonFemale) {
            gender = "Nữ";
        }

        // Hiển thị kết quả lên TextView
        String result = "=== THÔNG TIN ĐÃ NHẬP ===\n\n"
                + "Tên: " + name + "\n"
                + "Giới tính: " + gender + "\n"
                + "Ngôn ngữ quan tâm: " + (languages.length() > 0 ? languages.toString().trim() : "Chưa chọn");

        textViewResult.setText(result);
        layoutResult.setVisibility(View.VISIBLE);

        Toast.makeText(this, "Đã ghi nhận thông tin!", Toast.LENGTH_SHORT).show();
    }

    private void startProgressBar() {
        progressStatus = 0;
        progressBarLoading.setProgress(0);
        buttonStartProgress.setEnabled(false);
        buttonStartProgress.setText("Đang xử lý...");

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;

                // Cập nhật UI trên Main Thread
                handler.post(() -> progressBarLoading.setProgress(progressStatus));

                try {
                    Thread.sleep(50); // Delay 50ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            // Khi hoàn thành
            handler.post(() -> {
                if (progressStatus >= 100) {
                    Toast.makeText(MainActivity.this, "Hoàn thành 100%!", Toast.LENGTH_SHORT).show();
                }
                buttonStartProgress.setEnabled(true);
                buttonStartProgress.setText("Bắt đầu tiến trình");
            });
        }).start();
    }
}
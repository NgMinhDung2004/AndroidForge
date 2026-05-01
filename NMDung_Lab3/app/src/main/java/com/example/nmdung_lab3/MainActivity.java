package com.example.nmdung_lab3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loadMainMenu();
    }

    private void loadMainMenu() {
        setContentView(R.layout.activity_main);
        
        // Cài đặt padding cho System Bars (Status bar, navigation bar)
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Tìm các nút bấm và gán sự kiện chuyển layout
        Button btn1 = findViewById(R.id.btn_bai1);
        Button btn2 = findViewById(R.id.btn_bai2);
        Button btn3 = findViewById(R.id.btn_bai3);
        Button btn4 = findViewById(R.id.btn_bai4);
        Button btn5 = findViewById(R.id.btn_bai5);
        Button btn6 = findViewById(R.id.btn_bai6);

        if (btn1 != null) btn1.setOnClickListener(v -> setContentView(R.layout.layout_bai1_linear));
        if (btn2 != null) btn2.setOnClickListener(v -> setContentView(R.layout.layout_bai2_relative));
        if (btn3 != null) btn3.setOnClickListener(v -> setContentView(R.layout.layout_bai3_constraint));
        if (btn4 != null) btn4.setOnClickListener(v -> setContentView(R.layout.layout_bai4_table));
        if (btn5 != null) btn5.setOnClickListener(v -> setContentView(R.layout.layout_bai5_grid));
        if (btn6 != null) btn6.setOnClickListener(v -> setContentView(R.layout.layout_bai6_frame));
    }

    @Override
    public void onBackPressed() {
        // Nếu nhấn nút Back, nếu không ở menu chính thì quay lại menu chính
        if (findViewById(R.id.btn_bai1) == null) {
            loadMainMenu();
        } else {
            super.onBackPressed();
        }
    }
}
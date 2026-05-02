package com.example.lab4_bai1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Khai báo các view
    private EditText editTextMaSinhVien;
    private EditText editTextHoTen;
    private Button buttonTiepTheo;

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

        // Ánh xạ view từ XML
        editTextMaSinhVien = findViewById(R.id.editTextMaSinhVien);
        editTextHoTen = findViewById(R.id.editTextHoTen);
        buttonTiepTheo = findViewById(R.id.buttonTiepTheo);

        // Xử lý sự kiện click button
        buttonTiepTheo.setOnClickListener(v -> chuyenSangScoreActivity());
    }

    /**
     * Phương thức chuyển sang ScoreActivity
     * - Kiểm tra dữ liệu đầu vào
     * - Tạo Intent để chuyển Activity
     * - Truyền dữ liệu qua Intent bằng putExtra()
     */
    private void chuyenSangScoreActivity() {
        // Lấy dữ liệu từ EditText
        String maSinhVien = editTextMaSinhVien.getText().toString().trim();
        String hoTen = editTextHoTen.getText().toString().trim();

        // Kiểm tra dữ liệu rỗng
        if (maSinhVien.isEmpty() || hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Intent để chuyển Activity
        // Intent(Context nguồn, Class đích)
        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

        // Truyền dữ liệu qua Intent
        // putExtra(String key, String value)
        intent.putExtra("MASV", maSinhVien);
        intent.putExtra("HOTEN", hoTen);

        // Khởi chạy Activity mới
        startActivity(intent);
    }
}
package com.example.lab4_bai1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoreActivity extends AppCompatActivity {
    // Khai báo các view
    private TextView textViewThongTinSinhVien;
    private EditText editTextDiemToan;
    private EditText editTextDiemLy;
    private EditText editTextDiemHoa;
    private Button buttonXemKetQua;

    // Biến lưu thông tin sinh viên từ MainActivity
    private String maSinhVien;
    private String hoTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        textViewThongTinSinhVien = findViewById(R.id.textViewThongTinSinhVien);
        editTextDiemToan = findViewById(R.id.editTextDiemToan);
        editTextDiemLy = findViewById(R.id.editTextDiemLy);
        editTextDiemHoa = findViewById(R.id.editTextDiemHoa);
        buttonXemKetQua = findViewById(R.id.buttonXemKetQua);

        // Nhận dữ liệu từ MainActivity
        nhanDuLieuTuMainActivity();

        // Xử lý sự kiện click
        buttonXemKetQua.setOnClickListener(v -> chuyenSangResultActivity());
    }

    /**
     * Phương thức nhận dữ liệu từ Activity trước
     * - Lấy Intent đã khởi chạy Activity này
     * - Dùng getStringExtra() để lấy dữ liệu theo key
     */
    private void nhanDuLieuTuMainActivity() {
        // Lấy Intent đã được gửi từ MainActivity
        Intent intent = getIntent();
        // Lấy dữ liệu từ Intent theo key đã đặt
        // getStringExtra(String key) trả về giá trị String
        maSinhVien = intent.getStringExtra("MASV");
        hoTen = intent.getStringExtra("HOTEN");
        // Hiển thị thông tin sinh viên
        String thongTin = "Mã SV: " + maSinhVien + "\nHọ tên: " + hoTen;
        textViewThongTinSinhVien.setText(thongTin);
    }

    /**
     * Phương thức chuyển sang ResultActivity
     * - Truyền cả thông tin sinh viên và điểm số
     */
    private void chuyenSangResultActivity() {
        // Lấy điểm từ EditText
        String diemToanStr = editTextDiemToan.getText().toString().trim();
        String diemLyStr = editTextDiemLy.getText().toString().trim();
        String diemHoaStr = editTextDiemHoa.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (diemToanStr.isEmpty() || diemLyStr.isEmpty() || diemHoaStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ điểm!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Chuyển String sang double
            double diemToan = Double.parseDouble(diemToanStr);
            double diemLy = Double.parseDouble(diemLyStr);
            double diemHoa = Double.parseDouble(diemHoaStr);

            // Kiểm tra điểm hợp lệ (0-10)
            if (diemToan < 0 || diemToan > 10 || diemLy < 0 || diemLy > 10 || diemHoa < 0 || diemHoa > 10) {
                Toast.makeText(this, "Điểm phải từ 0 đến 10!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo Intent chuyển sang ResultActivity
            Intent intent = new Intent(ScoreActivity.this, ResultActivity.class);

            // Truyền thông tin sinh viên (nhận từ MainActivity)
            intent.putExtra("MASV", maSinhVien);
            intent.putExtra("HOTEN", hoTen);

            // Truyền điểm số
            // putExtra() có nhiều phiên bản: String, int, double, boolean...
            intent.putExtra("DIEMTOAN", diemToan);
            intent.putExtra("DIEMLY", diemLy);
            intent.putExtra("DIEMHOA", diemHoa);

            // Khởi chạy ResultActivity
            startActivity(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Điểm phải là số!", Toast.LENGTH_SHORT).show();
        }
    }
}
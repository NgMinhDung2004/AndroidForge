package com.example.lab4_bai2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    private TextView textViewMaSinhVien;
    private TextView textViewHoTen;
    private TextView textViewDiemToan;
    private TextView textViewDiemLy;
    private TextView textViewDiemHoa;
    private TextView textViewDiemTrungBinh;
    private TextView textViewXepLoai;
    private Button buttonQuayLai;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_result, container,
                false);

        // Ánh xạ các View
        textViewMaSinhVien = view.findViewById(R.id.textViewMaSinhVien);
        textViewHoTen = view.findViewById(R.id.textViewHoTen);
        textViewDiemToan = view.findViewById(R.id.textViewDiemToan);
        textViewDiemLy = view.findViewById(R.id.textViewDiemLy);

        textViewDiemHoa = view.findViewById(R.id.textViewDiemHoa);
        textViewDiemTrungBinh = view.findViewById(R.id.textViewDiemTrungBinh);
        textViewXepLoai = view.findViewById(R.id.textViewXepLoai);
        buttonQuayLai = view.findViewById(R.id.buttonQuayLai);
        // Nhận dữ liệu từ Fragment trước
        Bundle bundle = getArguments();
        if (bundle != null) {
            String maSinhVien = bundle.getString("maSinhVien");
            String hoTen = bundle.getString("hoTen");
            double diemToan = bundle.getDouble("diemToan");
            double diemLy = bundle.getDouble("diemLy");
            double diemHoa = bundle.getDouble("diemHoa");
            // Tính điểm trung bình
            double diemTrungBinh = (diemToan + diemLy + diemHoa) / 3;
            // Xếp loại
            String xepLoai = xacDinhXepLoai(diemTrungBinh);
            // Hiển thị thông tin
            textViewMaSinhVien.setText("Mã sinh viên: " + maSinhVien);
            textViewHoTen.setText("Họ tên: " + hoTen);
            textViewDiemToan.setText("Điểm toán: " + diemToan);
            textViewDiemLy.setText("Điểm Lý: " + diemLy);
            textViewDiemHoa.setText("Điểm Hóa: " + diemHoa);
            textViewDiemTrungBinh.setText("Điểm trung bình: " +
                    String.format("%.2f", diemTrungBinh));

            textViewXepLoai.setText("Xếp loại: " + xepLoai);
        }
        // Xử lý sự kiện nút Quay lại
        buttonQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.quayLaiStudentInfoFragment();
                }
            }
        });
        return view;
    }

    private String xacDinhXepLoai(double diemTrungBinh) {

        if (diemTrungBinh >= 8.0) {
            return "Giỏi";
        } else if (diemTrungBinh >= 6.5) {
            return "Khá";
        } else if (diemTrungBinh >= 5.0) {
            return "Trung bình";
        } else {
            return "Yếu";
        }
    }
}

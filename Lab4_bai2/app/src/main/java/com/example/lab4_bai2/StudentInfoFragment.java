package com.example.lab4_bai2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class StudentInfoFragment extends Fragment {
    private EditText editTextMaSinhVien;
    private EditText editTextHoTen;
    private Button buttonTiep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_info, container,
                false);

        // Ánh xạ các View
        editTextMaSinhVien = view.findViewById(R.id.editTextMaSinhVien);
        editTextHoTen = view.findViewById(R.id.editTextHoTen);
        buttonTiep = view.findViewById(R.id.buttonTiep);
        // Xử lý sự kiện nút Tiếp
        buttonTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChuyenFragment();
            }
        });

        return view;
    }

    private void xuLyChuyenFragment() {
        String maSinhVien = editTextMaSinhVien.getText().toString().trim();
        String hoTen = editTextHoTen.getText().toString().trim();
        // Kiểm tra dữ liệu
        if (maSinhVien.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa nhập mã sinh viên",
                    Toast.LENGTH_SHORT).show();
            editTextMaSinhVien.requestFocus();
            return;
        }
        if (hoTen.isEmpty()) {
            Toast.makeText(getActivity(), "Chưa nập họ và tên",
                    Toast.LENGTH_SHORT).show();
            editTextHoTen.requestFocus();
            return;
        }
        // Gửi dữ liệu qua MainActivity để chuyển Fragment
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.chuyenSangScoreFragment(maSinhVien, hoTen);
        }
    }
}

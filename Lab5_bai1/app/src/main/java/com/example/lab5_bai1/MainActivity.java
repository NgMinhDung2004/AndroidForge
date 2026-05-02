package com.example.lab5_bai1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerPerson = findViewById(R.id.spinnerPerson);

        // Tạo danh sách Person
        List<Person> listPerson = new ArrayList<>();
        // Sử dụng các ID tài nguyên ảnh (thay bằng ảnh thật nếu có)
        listPerson.add(new Person(R.drawable.hutao, "Người thứ 1"));
        listPerson.add(new Person(R.drawable.kokomi, "Người thứ 2"));
        listPerson.add(new Person(R.drawable.mualali, "Người thứ 3"));
        listPerson.add(new Person(R.drawable.nahida, "Người thứ 4"));

        // Tạo Adapter
        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.spinner_item, listPerson);

        // Set Adapter cho Spinner
        spinnerPerson.setAdapter(personAdapter);

        // Xử lý sự kiện trong Spinner
        spinnerPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng Person tại vị trí được chọn
                Person selectedPerson = (Person) parent.getItemAtPosition(position);
                if (selectedPerson != null) {
                    // Hiển thị thông báo Toast khi chọn
                    String selectedName = selectedPerson.getNamePerson();
                    Toast.makeText(MainActivity.this, "Bạn đã chọn " + selectedName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
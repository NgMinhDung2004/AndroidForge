package com.example.lab10_internal_and_axternal_memory;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exercise1Activity extends AppCompatActivity {

    private EditText editTextContent;
    private Button buttonSave, buttonRead;
    private TextView textViewContent;
    private final String FILE_NAME = "myFile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise1);

        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonRead = findViewById(R.id.buttonRead);
        textViewContent = findViewById(R.id.textViewContent);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editTextContent.getText().toString();
                saveToFile(content);
            }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = readFromFile();
                textViewContent.setText(content);
            }
        });
    }

    private void saveToFile(String content) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(content.getBytes());
            editTextContent.setText("");
            textViewContent.setText("Đã lưu vào file thành công!");
            Toast.makeText(this, "Lưu file thành công!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            textViewContent.setText("Có lỗi xảy ra khi lưu file.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readFromFile() {
        FileInputStream fis = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fis = openFileInput(FILE_NAME);
            int c;
            while ((c = fis.read()) != -1) {
                stringBuilder.append((char) c);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Có lỗi xảy ra khi đọc file hoặc file không tồn tại.";
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
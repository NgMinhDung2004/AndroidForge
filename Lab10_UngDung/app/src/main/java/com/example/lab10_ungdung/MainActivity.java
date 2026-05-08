package com.example.lab10_ungdung;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edtContent;
    private Button btnPickImage, btnSave, btnView;
    private ImageView imgDiary;
    private Uri selectedImageUri;
    private static final String TEXT_FILE_NAME = "diary_data.txt";
    private static final String PREFS_NAME = "DiaryPrefs";
    private static final String KEY_IMAGE_PATH = "last_saved_image_uri";

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imgDiary.setImageURI(selectedImageUri);
                }
            }
    );

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

        initViews();
        setupListeners();
    }

    private void initViews() {
        edtContent = findViewById(R.id.edtContent);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);
        imgDiary = findViewById(R.id.imgDiary);
    }

    private void setupListeners() {
        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveDiary());

        btnView.setOnClickListener(v -> viewDiary());
    }

    private void saveDiary() {
        String content = edtContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cảm nghĩ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Lưu text vào Internal Storage
        try (FileOutputStream fos = openFileOutput(TEXT_FILE_NAME, MODE_PRIVATE)) {
            fos.write(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu text", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Lưu ảnh vào Gallery
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "DIARY_" + timeStamp + ".jpg";
        Uri savedImageUri = saveImageToExternal(selectedImageUri, fileName);

        if (savedImageUri != null) {
            // Lưu URI vào SharedPreferences
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putString(KEY_IMAGE_PATH, savedImageUri.toString()).apply();
            
            Toast.makeText(this, "Đã lưu nhật ký thành công", Toast.LENGTH_SHORT).show();
            
            // Trả về trang gốc (xóa trắng các ô)
            clearInputs();
        } else {
            Toast.makeText(this, "Lỗi khi lưu ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputs() {
        edtContent.setText("");
        imgDiary.setImageResource(android.R.drawable.ic_menu_gallery);
        selectedImageUri = null;
    }

    private Uri saveImageToExternal(Uri sourceUri, String fileName) {
        OutputStream imageOutStream;
        Uri uri = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DiaryApp");
            }
            
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri == null) return null;

            imageOutStream = getContentResolver().openOutputStream(uri);

            try (InputStream in = getContentResolver().openInputStream(sourceUri)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    imageOutStream.write(buffer, 0, bytesRead);
                }
            }
            imageOutStream.close();
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void viewDiary() {
        // Thông báo xem lại nhật ký
        Toast.makeText(this, "Xem lại nhật ký", Toast.LENGTH_SHORT).show();

        // 1. Đọc text
        File file = new File(getFilesDir(), TEXT_FILE_NAME);
        if (!file.exists()) {
            Toast.makeText(this, "Chưa có dữ liệu lưu", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = openFileInput(TEXT_FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            edtContent.setText(sb.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Hiển thị ảnh
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastImageUriStr = prefs.getString(KEY_IMAGE_PATH, null);
        if (lastImageUriStr != null) {
            try {
                Uri lastUri = Uri.parse(lastImageUriStr);
                InputStream is = getContentResolver().openInputStream(lastUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imgDiary.setImageBitmap(bitmap);
                if (is != null) is.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tải ảnh cũ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không tìm thấy ảnh đã lưu", Toast.LENGTH_SHORT).show();
        }
    }
}

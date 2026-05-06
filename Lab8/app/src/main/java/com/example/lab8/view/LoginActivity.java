package com.example.lab8.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.lab8.R;
import com.example.lab8.model.User;
import com.example.lab8.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewForgotPassword;
    private LoginViewModel loginViewModel;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các widget từ giao diện
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        // Khởi tạo ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Xử lý sự kiện khi người dùng nhấn nút đăng nhập
        buttonLogin.setOnClickListener(v -> handleLogin());

        // Xử lý khi người dùng nhấn vào "Forgot Password?"
        textViewForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng quên mật khẩu đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Log.d(TAG, "Input Email: " + email + ", Input Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        loginViewModel.login(email).observe(this, user -> {
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    navigateToRoleBasedActivity(user);
                } else {
                    Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng với email này", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToRoleBasedActivity(User user) {
        if ("admin".equals(user.getRole())) {
            startActivity(new Intent(this, AdminActivity.class));
            Toast.makeText(this, "Chào mừng Admin", Toast.LENGTH_SHORT).show();
        } else if ("customer".equals(user.getRole())) {
            startActivity(new Intent(this, CustomerActivity.class));
            Toast.makeText(this, "Chào mừng khách hàng", Toast.LENGTH_SHORT).show();
        }
        finish(); // Kết thúc màn hình đăng nhập
    }
}

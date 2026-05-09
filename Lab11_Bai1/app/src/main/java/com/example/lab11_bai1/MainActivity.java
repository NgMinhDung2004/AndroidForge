package com.example.lab11_bai1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private TextView stepCountView;
    private int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gán view hiển thị số bước
        stepCountView = findViewById(R.id.stepCountView);

        // Khởi tạo SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Kiểm tra quyền ACTIVITY_RECOGNITION cho Android 10+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 100);
            }
        }

        // Kiểm tra và lấy cảm biến
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            // Nếu thiết bị hỗ trợ cảm biến bước chân
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        } else if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Dùng cảm biến gia tốc nếu không có cảm biến bước chân
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Toast.makeText(this, "Sử dụng cảm biến gia tốc", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thiết bị không hỗ trợ cảm biến!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // Tăng số bước mỗi khi nhận sự kiện bước chân
            stepCount++;
            stepCountView.setText("Số bước chân đã đi: " + stepCount);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Xử lý đơn giản cho cảm biến gia tốc (ví dụ phát hiện rung động mạnh)
            // Trong thực tế cần thuật toán phức tạp hơn để đếm bước từ gia tốc
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z);
            if (acceleration > 12) { // Ngưỡng giả định cho một bước chân
                stepCount++;
                stepCountView.setText("Số bước chân đã đi (gia tốc): " + stepCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền truy cập hoạt động thể chất", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền bị từ chối, ứng dụng có thể không hoạt động chính xác", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
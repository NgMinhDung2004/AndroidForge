package com.example.lab9_service_bai1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo MediaPlayer với tệp nhạc breakfast đã đưa vào dự án từ trước và đặt trong res/raw
        // Lưu ý: Bạn cần tạo thư mục res/raw và thêm file breakfast.mp3 vào đó
        mediaPlayer = MediaPlayer.create(this, R.raw.breakfast);
        mediaPlayer.setLooping(true); // Đặt phát nhạc lặp lại
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start(); // Bắt đầu phát nhạc
        Toast.makeText(this, "Phát nhạc nền", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); // Giải phóng tài nguyên khi dừng Service
        }
        Toast.makeText(this, "Dừng nhạc nền", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Không cần bind vì đây là Started Service
    }
}

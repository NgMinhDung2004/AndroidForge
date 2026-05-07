package com.example.lab9_service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Giải phóng nhạc cũ nếu đang chơi
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        String uriString = null;
        if (intent != null) {
            uriString = intent.getStringExtra("URI");
        }

        if (uriString != null) {
            // Phát nhạc từ URI (Người dùng chọn)
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(uriString));
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                Toast.makeText(this, "Đang phát nhạc bạn chọn", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi phát tệp nhạc", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Phát nhạc mặc định (nếu có file breakfast)
            int resId = getResources().getIdentifier("breakfast", "raw", getPackageName());
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(this, resId);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    Toast.makeText(this, "Phát nhạc nền mặc định", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Vui lòng chọn nhạc hoặc thêm file 'breakfast.mp3' vào res/raw", Toast.LENGTH_LONG).show();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Toast.makeText(this, "Dừng nhạc nền", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

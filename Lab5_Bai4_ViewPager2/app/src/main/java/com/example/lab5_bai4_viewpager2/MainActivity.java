package com.example.lab5_bai4_viewpager2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager;

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

        viewPager = findViewById(R.id.viewPager);
        List<OnboardingItem> list = new ArrayList<>();
        // Các ảnh có tên viewpager1-2-3 đặt trong drawable
        list.add(new OnboardingItem(
                R.drawable.columbia,
                "columbia",
                "Đây là ViewPager2-1"
        ));
        list.add(new OnboardingItem(
                R.drawable.furina,
                "furina",
                "Đây là ViewPager2-2"
        ));
        list.add(new OnboardingItem(
                R.drawable.zibai,
                "zibai",
                "Đây là ViewPager2-3"
        ));

        OnboardingAdapter adapter = new OnboardingAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }
}

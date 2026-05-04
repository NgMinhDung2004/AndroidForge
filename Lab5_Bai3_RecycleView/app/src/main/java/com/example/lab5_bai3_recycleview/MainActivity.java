package com.example.lab5_bai3_recycleview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        recyclerView = findViewById(R.id.recyclerView);
        Button buttonAdd = findViewById(R.id.buttonAdd);

        // Dữ liệu hardcode giả lập
        productList = new ArrayList<>();
        productList.add(new Product("Raiden Shogun", 1.364, "https://i.ebayimg.com/images/g/N2cAAOSw4~NoNA94/s-l1600.webp"));
        productList.add(new Product("Sản phẩm 2", 20.49, "https://via.placeholder.com/150/4CAF50/FFFFFF?text=SP2"));
        productList.add(new Product("Sản phẩm 3", 15.00, "https://via.placeholder.com/150/2196F3/FFFFFF?text=SP3"));

        // Setup RecyclerView
        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Thêm item mới khi click button - sử dụng dialog
        buttonAdd.setOnClickListener(v -> {
            // Gọi dialog thêm mới (isEdit = false)
            adapter.showProductDialog(null, -1, false);
        });
    }
}

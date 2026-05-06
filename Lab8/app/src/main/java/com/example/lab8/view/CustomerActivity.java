package com.example.lab8.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab8.R;
import com.example.lab8.view.adapter.ProductAdapter;
import com.example.lab8.viewmodel.ProductViewModel;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // Ánh xạ RecyclerView từ giao diện
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter và gán cho RecyclerView
        productAdapter = new ProductAdapter();
        recyclerViewProducts.setAdapter(productAdapter);

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Lấy danh sách sản phẩm và cập nhật giao diện
        productViewModel.getProducts().observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                Log.d("CustomerActivity", "Sản phẩm đã được nhận: " + products.size());
                productAdapter.setProducts(products);
            } else {
                Log.d("CustomerActivity", "Không có sản phẩm được hiển thị");
                Toast.makeText(this, "Không có sản phẩm nào để hiển thị", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

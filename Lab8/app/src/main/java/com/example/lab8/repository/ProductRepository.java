package com.example.lab8.repository;

import android.util.Log;
import com.example.lab8.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductRepository {
    private final DatabaseReference database;

    public ProductRepository() {
        database = FirebaseDatabase.getInstance().getReference("products");
    }

    // Lấy tất cả sản phẩm
    public Task<DataSnapshot> getAllProducts() {
        Log.d("ProductRepository", "Đang lấy tất cả sản phẩm...");
        return database.get();
    }

    // Thêm sản phẩm mới
    public Task<Void> addProduct(Product product) {
        if (product.getProductID() == null || product.getProductID().isEmpty()) {
            // Tạo ID sản phẩm nếu chưa có
            String productId = database.push().getKey();
            if (productId != null) {
                product.setProductID(productId);
                Log.d("ProductRepository", "ID sản phẩm mới được tạo: " + productId);
            } else {
                Log.e("ProductRepository", "Không thể tạo ID sản phẩm");
                return Tasks.forException(new Exception("Không thể tạo ID sản phẩm"));
            }
        }
        Log.d("ProductRepository", "Thêm sản phẩm: " + product);
        return database.child(product.getProductID()).setValue(product);
    }

    // Cập nhật sản phẩm
    public Task<Void> updateProduct(Product product) {
        if (product.getProductID() != null && !product.getProductID().isEmpty()) {
            Log.d("ProductRepository", "Cập nhật sản phẩm: " + product);
            return database.child(product.getProductID()).setValue(product);
        } else {
            Log.e("ProductRepository", "ID sản phẩm không hợp lệ khi cập nhật");
            return Tasks.forException(new Exception("ID sản phẩm không hợp lệ"));
        }
    }

    // Xóa sản phẩm
    public Task<Void> deleteProduct(String productID) {
        if (productID != null && !productID.isEmpty()) {
            Log.d("ProductRepository", "Xóa sản phẩm với ID: " + productID);
            return database.child(productID).removeValue();
        } else {
            Log.e("ProductRepository", "ID sản phẩm không hợp lệ khi xóa");
            return Tasks.forException(new Exception("ID sản phẩm không hợp lệ"));
        }
    }
}

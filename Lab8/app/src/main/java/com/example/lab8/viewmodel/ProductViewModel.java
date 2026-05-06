package com.example.lab8.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.lab8.model.Product;
import com.example.lab8.repository.ProductRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final ProductRepository productRepository = new ProductRepository();
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();

    public ProductViewModel() {
        observeProducts();
    }

    // Lắng nghe dữ liệu thời gian thực
    private void observeProducts() {
        FirebaseDatabase.getInstance().getReference("products")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Product> products = new ArrayList<>();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.getValue(Product.class);
                        if (product != null) {
                            products.add(product);
                        }
                    }
                    productsLiveData.setValue(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ProductViewModel", "Lỗi Firebase: " + error.getMessage());
                }
            });
    }

    public LiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product).addOnFailureListener(e -> 
            Log.e("ProductViewModel", "Thêm thất bại: " + e.getMessage()));
    }

    public void updateProduct(Product product) {
        productRepository.updateProduct(product).addOnFailureListener(e -> 
            Log.e("ProductViewModel", "Cập nhật thất bại: " + e.getMessage()));
    }

    public void deleteProduct(String productID) {
        productRepository.deleteProduct(productID).addOnFailureListener(e -> 
            Log.e("ProductViewModel", "Xóa thất bại: " + e.getMessage()));
    }

    public String generateProductID() {
        return FirebaseDatabase.getInstance().getReference("products").push().getKey();
    }
}

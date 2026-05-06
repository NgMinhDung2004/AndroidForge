package com.example.lab8.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab8.R;
import com.example.lab8.model.Product;
import com.example.lab8.view.adapter.ProductAdapterAdmin;
import com.example.lab8.viewmodel.ProductViewModel;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapterAdmin adapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        adapter = new ProductAdapterAdmin();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        
        // Quan sát danh sách sản phẩm (tự động cập nhật UI)
        productViewModel.getProducts().observe(this, products -> {
            if (products != null) {
                adapter.setProducts(products);
            }
        });

        buttonAddProduct.setOnClickListener(v -> openEditProductDialog(null));
        
        adapter.setOnEditClickListener(this::openEditProductDialog);
        
        adapter.setOnDeleteClickListener(product -> {
            new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    productViewModel.deleteProduct(product.getProductID());
                    Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
    }

    private void openEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        if (product != null) {
            builder.setTitle("Sửa sản phẩm");
            editTextProductName.setText(product.getName());
            editTextProductPrice.setText(String.valueOf(product.getPrice()));
            editTextProductQuantity.setText(String.valueOf(product.getQuantity()));
        } else {
            builder.setTitle("Thêm sản phẩm mới");
        }

        builder.setPositiveButton("Lưu", null); // Đặt null để xử lý sau (tránh tự đóng dialog)
        builder.setNegativeButton("Hủy", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý nút Lưu thủ công để kiểm tra dữ liệu đầu vào
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String productName = editTextProductName.getText().toString().trim();
            String priceStr = editTextProductPrice.getText().toString().trim();
            String quantityStr = editTextProductQuantity.getText().toString().trim();

            if (productName.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int productPrice = Integer.parseInt(priceStr);
                int productQuantity = Integer.parseInt(quantityStr);

                if (product == null) {
                    // Thêm sản phẩm mới
                    String newProductID = productViewModel.generateProductID();
                    Product newProduct = new Product(productName, productPrice, newProductID, productQuantity);
                    productViewModel.addProduct(newProduct);
                    Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật sản phẩm
                    product.setName(productName);
                    product.setPrice(productPrice);
                    product.setQuantity(productQuantity);
                    productViewModel.updateProduct(product);
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss(); // Chỉ đóng khi lưu thành công
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá và số lượng phải là số", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

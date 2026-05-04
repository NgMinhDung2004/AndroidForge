package com.example.lab5_bai3_recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(holder.getAdapterPosition());
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("$" + product.getPrice());

        // Load image dùng Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imageView);

        // Xử lý click item
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Sản phẩm: " + product.getName() + " - Giá: $" + product.getPrice(),
                    Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút Sửa
        holder.buttonEdit.setOnClickListener(v -> {
            showEditDialog(product, holder.getAdapterPosition());
        });

        // Xử lý nút Xóa
        holder.buttonDelete.setOnClickListener(v -> {
            showDeleteConfirmDialog(product, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Thêm item mới động
    public void addProduct(Product product) {
        productList.add(product);
        notifyItemInserted(productList.size() - 1);
    }

    // Hiển thị dialog cho cả thêm mới và sửa sản phẩm
    public void showProductDialog(Product product, int position, boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        // Tìm các view trong dialog
        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextName);
        TextInputEditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        TextInputEditText editTextImageUrl = dialogView.findViewById(R.id.editTextImageUrl);

        // Thiết lập tiêu đề và dữ liệu
        if (isEdit) {
            dialogTitle.setText("Sửa thông tin sản phẩm");
            editTextName.setText(product.getName());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextImageUrl.setText(product.getImageUrl());
        } else {
            dialogTitle.setText("Thêm sản phẩm mới");
        }

        builder.setPositiveButton(isEdit ? "Lưu" : "Thêm", (dialog, which) -> {
            String newName = editTextName.getText().toString().trim();
            String newPriceStr = editTextPrice.getText().toString().trim();
            String newImageUrl = editTextImageUrl.getText().toString().trim();

            // Validate
            if (newName.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double newPrice = Double.parseDouble(newPriceStr);
                String finalImageUrl = newImageUrl.isEmpty() ? "https://via.placeholder.com/150" : newImageUrl;

                if (isEdit) {
                    // Cập nhật sản phẩm
                    product.setName(newName);
                    product.setPrice(newPrice);
                    product.setImageUrl(finalImageUrl);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật sản phẩm!", Toast.LENGTH_SHORT).show();
                } else {
                    // Thêm sản phẩm mới
                    Product newProduct = new Product(newName, newPrice, finalImageUrl);
                    addProduct(newProduct);
                    Toast.makeText(context, "Đã thêm sản phẩm mới!", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Giá không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Hiển thị dialog sửa sản phẩm
    private void showEditDialog(Product product, int position) {
        showProductDialog(product, position, true);
    }

    // Hiển thị dialog xác nhận xóa
    private void showDeleteConfirmDialog(Product product, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.getName() + "\" không?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            productList.remove(position);
            notifyItemRemoved(position);
            // Quan trọng: Cập nhật lại dải vị trí sau khi xóa để tránh lỗi IndexOutOfBounds
            notifyItemRangeChanged(position, productList.size() - position);
            Toast.makeText(context, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;
        Button buttonEdit;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

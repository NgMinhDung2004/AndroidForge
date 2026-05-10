package com.example.kiemtra_moneymanager.view;

import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiemtra_moneymanager.R;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThuChiAdapter extends RecyclerView.Adapter<ThuChiAdapter.BaseViewHolder> {
    private List<ThuChiEntity> list = new ArrayList<>();
    private OnItemClickListener listener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public interface OnItemClickListener {
        void onItemClick(ThuChiEntity item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<ThuChiEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ThuChiEntity getItemAt(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thu_chi, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        ThuChiEntity item = list.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvTitle, tvAmount, tvCategory, tvDate;
        View indicator;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            indicator = itemView.findViewById(R.id.indicator_type);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(ThuChiEntity item) {
            tvTitle.setText(item.getTieuDe());
            tvCategory.setText(item.getDanhMuc());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDate.setText(sdf.format(new Date(item.getNgayThuChi())));

            if (item.getLoaiThuChi() == 1) { // Thu
                tvAmount.setText(String.format(Locale.getDefault(), "+%,.0f VNĐ", item.getSoTien()));
                tvAmount.setTextColor(Color.parseColor("#4CAF50")); // Green
                indicator.setBackgroundColor(Color.parseColor("#4CAF50"));
            } else { // Chi
                tvAmount.setText(String.format(Locale.getDefault(), "-%,.0f VNĐ", item.getSoTien()));
                tvAmount.setTextColor(Color.parseColor("#F44336")); // Red
                indicator.setBackgroundColor(Color.parseColor("#F44336"));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Tùy chọn");
            menu.add(0, 1, 0, "Chỉnh sửa");
            menu.add(0, 2, 1, "Xóa bỏ");
        }
    }
}

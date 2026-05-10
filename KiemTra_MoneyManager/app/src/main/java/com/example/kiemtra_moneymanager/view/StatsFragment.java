package com.example.kiemtra_moneymanager.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.kiemtra_moneymanager.R;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import com.example.kiemtra_moneymanager.viewmodel.ThuChiViewModel;
import java.util.Locale;

public class StatsFragment extends Fragment {
    private ThuChiViewModel viewModel;
    private TextView tvTotalThu, tvTotalChi, tvBalance, tvMaxThu, tvMaxChi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        tvTotalThu = view.findViewById(R.id.tv_total_thu);
        tvTotalChi = view.findViewById(R.id.tv_total_chi);
        tvBalance = view.findViewById(R.id.tv_balance);
        tvMaxThu = view.findViewById(R.id.tv_max_thu);
        tvMaxChi = view.findViewById(R.id.tv_max_chi);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ThuChiViewModel.class);

        viewModel.getTotalThu().observe(getViewLifecycleOwner(), total -> {
            tvTotalThu.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", total));
        });

        viewModel.getTotalChi().observe(getViewLifecycleOwner(), total -> {
            tvTotalChi.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", total));
        });

        viewModel.getBalance().observe(getViewLifecycleOwner(), balance -> {
            tvBalance.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", balance));
            if (balance < 0) {
                tvBalance.setTextColor(Color.RED);
            } else {
                tvBalance.setTextColor(Color.parseColor("#212121"));
            }
            
            updateMaxValues();
        });
    }

    private void updateMaxValues() {
        ThuChiEntity maxThu = viewModel.getMaxThu();
        if (maxThu != null) {
            tvMaxThu.setText(String.format(Locale.getDefault(), "%s (%,.0f VNĐ)", maxThu.getTieuDe(), maxThu.getSoTien()));
        } else {
            tvMaxThu.setText("N/A");
        }

        ThuChiEntity maxChi = viewModel.getMaxChi();
        if (maxChi != null) {
            tvMaxChi.setText(String.format(Locale.getDefault(), "%s (%,.0f VNĐ)", maxChi.getTieuDe(), maxChi.getSoTien()));
        } else {
            tvMaxChi.setText("N/A");
        }
    }
}

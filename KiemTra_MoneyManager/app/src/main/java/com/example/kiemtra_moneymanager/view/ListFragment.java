package com.example.kiemtra_moneymanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiemtra_moneymanager.R;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import com.example.kiemtra_moneymanager.viewmodel.ThuChiViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListFragment extends Fragment {
    private ThuChiViewModel viewModel;
    private ThuChiAdapter adapter;
    private RecyclerView recyclerView;
    private ExtendedFloatingActionButton fabAdd;
    private Spinner spType, spMonth, spYear;
    private ActivityResultLauncher<Intent> addEditLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAdd = view.findViewById(R.id.fab_add);
        spType = view.findViewById(R.id.spinner_filter_type);
        spMonth = view.findViewById(R.id.spinner_filter_month);
        spYear = view.findViewById(R.id.spinner_filter_year);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ThuChiViewModel.class);
        setupActivityResultLauncher();
        
        setupRecyclerView();
        setupFilters();

        viewModel.getAllThuChi().observe(getViewLifecycleOwner(), items -> {
            adapter.setList(items);
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditActivity.class);
            addEditLauncher.launch(intent);
        });

        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(getContext(), AddEditActivity.class);
            intent.putExtra("item", item);
            addEditLauncher.launch(intent);
        });

        // Tự động thu gọn FAB khi cuộn danh sách
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) fabAdd.shrink();
                else if (dy < 0) fabAdd.extend();
            }
        });
    }

    private void setupActivityResultLauncher() {
        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                        resetFiltersAndRefresh();
                    }
                }
        );
    }

    private void resetFiltersAndRefresh() {
        spType.setSelection(0);
        spMonth.setSelection(0);
        spYear.setSelection(0);
        viewModel.refreshData();
        recyclerView.scrollToPosition(0);
    }

    private void setupRecyclerView() {
        adapter = new ThuChiAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupFilters() {
        String[] types = {"Tất cả", "Chi", "Thu"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        String[] months = {"Tất cả tháng", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthAdapter);

        List<String> years = new ArrayList<>();
        years.add("Tất cả năm");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 5; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spType.setOnItemSelectedListener(filterListener);
        spMonth.setOnItemSelectedListener(filterListener);
        spYear.setOnItemSelectedListener(filterListener);
    }

    private void applyFilters() {
        int type = spType.getSelectedItemPosition() - 1;
        int month = spMonth.getSelectedItemPosition() - 1;
        String yearStr = spYear.getSelectedItem().toString();
        int year = yearStr.equals("Tất cả năm") ? -1 : Integer.parseInt(yearStr);
        viewModel.filter(type, month, year);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            ThuChiEntity selectedItem = adapter.getItemAt(adapter.getPosition());
            if (item.getItemId() == 1) { // Sửa
                Intent intent = new Intent(getContext(), AddEditActivity.class);
                intent.putExtra("item", selectedItem);
                addEditLauncher.launch(intent);
                return true;
            } else if (item.getItemId() == 2) { // Xóa
                viewModel.delete(selectedItem.getMa());
                Toast.makeText(getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception ignored) {}
        return super.onContextItemSelected(item);
    }
}

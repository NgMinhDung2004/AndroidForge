package com.example.kiemtra_moneymanager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import com.example.kiemtra_moneymanager.view.ListFragment;
import com.example.kiemtra_moneymanager.view.StatsFragment;
import com.example.kiemtra_moneymanager.viewmodel.ThuChiViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ThuChiViewModel viewModel;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra an toàn cho main_layout
        View mainLayout = findViewById(R.id.main_layout);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Khởi tạo ViewModel an toàn
        try {
            viewModel = new ViewModelProvider(this).get(ThuChiViewModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                String title = "";
                if (item.getItemId() == R.id.nav_list) {
                    selectedFragment = new ListFragment();
                    title = "Danh sách thu chi";
                } else if (item.getItemId() == R.id.nav_stats) {
                    selectedFragment = new StatsFragment();
                    title = "Thống kê";
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment, title);
                }
                return true;
            });
        }

        // Load fragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new ListFragment(), "Danh sách thu chi");
        }
    }

    private void loadFragment(Fragment fragment, String title) {
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_export_csv) {
            if (checkPermission()) {
                showExportOptionsDialog();
            } else {
                requestPermission();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            return true;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void showExportOptionsDialog() {
        String[] options = {"Xuất toàn bộ dữ liệu", "Xuất theo bộ lọc"};
        new AlertDialog.Builder(this)
                .setTitle("Xuất CSV")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        exportAllData();
                    } else {
                        showFilterExportDialog();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showFilterExportDialog() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        int padding = dpToPx(20);
        container.setPadding(padding, padding, padding, dpToPx(8));

        Spinner spType = new Spinner(this);
        Spinner spMonth = new Spinner(this);
        Spinner spYear = new Spinner(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = dpToPx(10);
        spType.setLayoutParams(params);
        spMonth.setLayoutParams(params);
        spYear.setLayoutParams(params);

        String[] types = {"Tất cả loại", "Chi", "Thu"};
        String[] months = {"Tất cả tháng", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        List<String> years = new ArrayList<>();
        years.add("Tất cả năm");
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = currentYear; y >= currentYear - 10; y--) {
            years.add(String.valueOf(y));
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(typeAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(monthAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(yearAdapter);

        container.addView(spType);
        container.addView(spMonth);
        container.addView(spYear);

        new AlertDialog.Builder(this)
                .setTitle("Xuất theo điều kiện")
                .setView(container)
                .setPositiveButton("Xuất", (dialog, which) -> {
                    int type = spType.getSelectedItemPosition() - 1;   // -1: tất cả, 0: chi, 1: thu
                    int month = spMonth.getSelectedItemPosition() - 1; // -1: tất cả, 0-11: tháng
                    String yearStr = spYear.getSelectedItem().toString();
                    int year = yearStr.equals("Tất cả năm") ? -1 : Integer.parseInt(yearStr);
                    exportFilteredData(type, month, year);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void exportAllData() {
        if (viewModel == null) return;
        List<ThuChiEntity> list = viewModel.getOriginalListSnapshot();
        if (list == null || list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu để xuất", Toast.LENGTH_SHORT).show();
            return;
        }
        exportToCSV(list, "all");
    }

    private void exportFilteredData(int type, int month, int year) {
        if (viewModel == null) return;
        List<ThuChiEntity> source = viewModel.getOriginalListSnapshot();
        List<ThuChiEntity> filtered = new ArrayList<>();

        for (ThuChiEntity item : source) {
            boolean matchType = (type == -1) || (item.getLoaiThuChi() == type);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(item.getNgayThuChi());
            boolean matchMonth = (month == -1) || (cal.get(Calendar.MONTH) == month);
            boolean matchYear = (year == -1) || (cal.get(Calendar.YEAR) == year);
            if (matchType && matchMonth && matchYear) {
                filtered.add(item);
            }
        }

        if (filtered.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu phù hợp bộ lọc", Toast.LENGTH_SHORT).show();
            return;
        }

        String suffix = String.format(Locale.getDefault(), "filter_t%s_m%s_y%s",
                type == -1 ? "all" : String.valueOf(type),
                month == -1 ? "all" : String.valueOf(month + 1),
                year == -1 ? "all" : String.valueOf(year));
        exportToCSV(filtered, suffix);
    }

    private void exportToCSV(List<ThuChiEntity> list, String suffix) {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (folder == null) return;
        String fileName = "MoneyManager_" + suffix + "_" + System.currentTimeMillis() + ".csv";
        File file = new File(folder, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(0xef); fos.write(0xbb); fos.write(0xbf); // UTF-8 BOM
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.println("Mã,Tiêu đề,Số tiền,Loại,Danh mục,Ngày,Ghi chú");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            for (ThuChiEntity item : list) {
                writer.print(item.getMa() + ",");
                writer.print(escapeCsv(item.getTieuDe()) + ",");
                writer.print(item.getSoTien() + ",");
                writer.print((item.getLoaiThuChi() == 1 ? "Thu" : "Chi") + ",");
                writer.print(escapeCsv(item.getDanhMuc()) + ",");
                writer.print(sdf.format(new Date(item.getNgayThuChi())) + ",");
                writer.println(escapeCsv(item.getGhiChu()));
            }
            writer.flush();
            showExportSuccessDialog(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất file", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExportSuccessDialog(File file) {
        String relativePath = "Download/" + file.getName();
        String message = "Đã lưu vào: " + relativePath + "\n\nBạn muốn làm gì với file này?";

        new AlertDialog.Builder(this)
                .setTitle("Xuất CSV thành công")
                .setMessage(message)
                .setNeutralButton("Chia sẻ", (dialog, which) -> shareCsvFile(file))
                .setNegativeButton("Đóng", null)
                .setPositiveButton("Mở file", (dialog, which) -> openCsvFile(file))
                .show();
    }

    private void shareCsvFile(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_STREAM, getCsvUri(file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Chia sẻ file CSV"));
        } catch (Exception e) {
            Toast.makeText(this, "Không thể chia sẻ file", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCsvFile(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(getCsvUri(file), "text/csv");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Mở file CSV"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không có ứng dụng hỗ trợ mở CSV", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Không thể mở file", Toast.LENGTH_SHORT).show();
        }
    }

    private android.net.Uri getCsvUri(File file) {
        return FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                file
        );
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showExportOptionsDialog();
        }
    }
}

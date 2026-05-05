package com.example.lab6_menu_toolbar;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Giao diện
    private DrawerLayout drawerLayoutMain;
    private MaterialToolbar materialToolbarMain;
    private NavigationView navigationViewMain;
    private RecyclerView recyclerViewCongViec;
    private FloatingActionButton fabThemCongViec;
    private SearchView searchViewTimCongViec;

    // Data
    private List<CongViec> danhSachCongViec;
    private CongViecAdapter adapter;
    private int idCounter = 1;

    // Context Menu
    private CongViec congViecDangChon;
    private int viTriDangChon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Insets cho ROOT layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        khoiTaoView();
        khoiTaoDuLieu();
        cauHinhToolbar();
        cauHinhRecyclerView();
        cauHinhNavigationDrawer();
        cauHinhFAB();

        // Insets riêng cho HEADER của NavigationView
        NavigationView navView = findViewById(R.id.navigationViewMain);
        View header = navView.getHeaderView(0);

        ViewCompat.setOnApplyWindowInsetsListener(header, (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    systemBars.top,
                    v.getPaddingRight(),
                    v.getPaddingBottom()
            );
            return insets;
        });

        // BACK PRESS HANDLER (Dispatcher)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
                    drawerLayoutMain.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void khoiTaoView() {
        drawerLayoutMain = findViewById(R.id.main);
        materialToolbarMain = findViewById(R.id.materialToolbarMain);
        navigationViewMain = findViewById(R.id.navigationViewMain);
        recyclerViewCongViec = findViewById(R.id.recyclerViewCongViec);
        fabThemCongViec = findViewById(R.id.fabThemCongViec);
    }

    private void khoiTaoDuLieu() {
        danhSachCongViec = new ArrayList<>();
        // Thêm dữ liệu mẫu
        danhSachCongViec.add(new CongViec(idCounter++, "Hoàn thành báo cáo tuần", "CONG_VIEC"));
        danhSachCongViec.add(new CongViec(idCounter++, "Mua sắm cuối tuần", "CA_NHAN"));
        danhSachCongViec.add(new CongViec(idCounter++, "Họp khẩn với khách hàng", "KHAN_CAP"));
        danhSachCongViec.add(new CongViec(idCounter++, "Chuẩn bị presentation", "CONG_VIEC"));
    }

    private void cauHinhToolbar() {
        setSupportActionBar(materialToolbarMain);

        // Hamburger icon để mở drawer
        materialToolbarMain.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        materialToolbarMain.setNavigationOnClickListener(v -> {
            drawerLayoutMain.openDrawer(GravityCompat.START);
        });
    }

    private void cauHinhRecyclerView() {
        adapter = new CongViecAdapter(danhSachCongViec);
        recyclerViewCongViec.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCongViec.setAdapter(adapter);

        adapter.setOnItemClickListener(new CongViecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CongViec congViec, int position) {
                Toast.makeText(MainActivity.this,
                        "Đã chọn: " + congViec.getTenCongViec(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(CongViec congViec, int position, View view) {
                congViecDangChon = congViec;
                viTriDangChon = position;
                hienThiContextMenu(view);
            }

            @Override
            public void onCheckBoxClick(CongViec congViec, int position) {
                congViec.setDaHoanThanh(!congViec.isDaHoanThanh());
                adapter.notifyItemChanged(position);
                Toast.makeText(MainActivity.this,
                        congViec.isDaHoanThanh() ? "Đã hoàn thành" : "Chưa hoàn thành",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cauHinhNavigationDrawer() {
        navigationViewMain.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_tat_ca) {
                hienThiTatCaCongViec();
            } else if (id == R.id.nav_cong_viec) {
                locCongViecTheoLoai("CONG_VIEC");
            } else if (id == R.id.nav_ca_nhan) {
                locCongViecTheoLoai("CA_NHAN");
            } else if (id == R.id.nav_khan_cap) {
                locCongViecTheoLoai("KHAN_CAP");
            } else if (id == R.id.nav_hoan_thanh) {
                hienThiCongViecHoanThanh();
            } else if (id == R.id.nav_cai_dat) {
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_gioi_thieu) {
                hienThiGioiThieu();
            }

            drawerLayoutMain.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void cauHinhFAB() {
        fabThemCongViec.setOnClickListener(v -> hienThiPopupMenuFAB(v));
    }

    // ===== OPTIONS MENU (Toolbar) =====
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        // Cấu hình SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchViewTimCongViec = (SearchView) searchItem.getActionView();
        searchViewTimCongViec.setQueryHint("Tìm kiếm công việc...");

        searchViewTimCongViec.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Sắp xếp
        if (id == R.id.action_sap_xep_ten) {
            sapXepTheoTen();
            return true;
        } else if (id == R.id.action_sap_xep_ngay) {
            sapXepTheoNgay();
            return true;
        }

        // Lọc
        if (id == R.id.action_loc_cong_viec) {
            locCongViecTheoLoai("CONG_VIEC");
            return true;
        } else if (id == R.id.action_loc_ca_nhan) {
            locCongViecTheoLoai("CA_NHAN");
            return true;
        } else if (id == R.id.action_loc_khan_cap) {
            locCongViecTheoLoai("KHAN_CAP");
            return true;
        } else if (id == R.id.action_loc_hoan_thanh) {
            hienThiCongViecHoanThanh();
            return true;
        }

        // Xóa tất cả
        if (id == R.id.action_xoa_tat_ca) {
            xoaTatCaCongViec();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ===== POPUP MENU (FAB) =====
    private void hienThiPopupMenuFAB(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_fab_popup, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.popup_them_cong_viec) {
                hienThiDialogThemCongViec("CONG_VIEC");
                return true;
            } else if (id == R.id.popup_them_ca_nhan) {
                hienThiDialogThemCongViec("CA_NHAN");
                return true;
            } else if (id == R.id.popup_them_khan_cap) {
                hienThiDialogThemCongViec("KHAN_CAP");
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    // ===== CONTEXT MENU =====
    private void hienThiContextMenu(View view) {
        PopupMenu contextMenu = new PopupMenu(this, view);
        contextMenu.getMenuInflater().inflate(R.menu.menu_context, contextMenu.getMenu());

        // Thay đổi text của menu item dựa vào trạng thái
        MenuItem danhDauItem = contextMenu.getMenu().findItem(R.id.context_danh_dau);
        if (congViecDangChon.isDaHoanThanh()) {
            danhDauItem.setTitle("Đánh dấu chưa hoàn thành");
        } else {
            danhDauItem.setTitle("Đánh dấu hoàn thành");
        }

        contextMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.context_sua) {
                hienThiDialogSuaCongViec();
                return true;
            } else if (id == R.id.context_xoa) {
                xoaCongViec();
                return true;
            } else if (id == R.id.context_danh_dau) {
                danhDauHoanThanh();
                return true;
            }

            return false;
        });

        contextMenu.show();
    }

    // ===== DIALOG THÊM CÔNG VIỆC =====
    private void hienThiDialogThemCongViec(String loaiCongViec) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_them_cong_viec, null);

        EditText editTextTenCongViec = dialogView.findViewById(R.id.editTextTenCongViec);

        String tieuDe = "";
        switch (loaiCongViec) {
            case "CONG_VIEC":
                tieuDe = "Thêm Công việc";
                break;
            case "CA_NHAN":
                tieuDe = "Thêm Cá nhân";
                break;
            case "KHAN_CAP":
                tieuDe = "Thêm Khẩn cấp";
                break;
        }

        builder.setTitle(tieuDe)
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String tenCongViec = editTextTenCongViec.getText().toString().trim();
                    if (!tenCongViec.isEmpty()) {
                        CongViec congViecMoi = new CongViec(idCounter++, tenCongViec, loaiCongViec);
                        danhSachCongViec.add(0, congViecMoi);
                        adapter.updateDanhSach(danhSachCongViec);
                        Toast.makeText(this, "Đã thêm công việc", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Vui lòng nhập tên công việc", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ===== DIALOG SỬA CÔNG VIỆC =====
    private void hienThiDialogSuaCongViec() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_them_cong_viec, null);

        EditText editTextTenCongViec = dialogView.findViewById(R.id.editTextTenCongViec);
        editTextTenCongViec.setText(congViecDangChon.getTenCongViec());

        builder.setTitle("Sửa công việc")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String tenMoi = editTextTenCongViec.getText().toString().trim();
                    if (!tenMoi.isEmpty()) {
                        congViecDangChon.setTenCongViec(tenMoi);
                        adapter.notifyItemChanged(viTriDangChon);
                        Toast.makeText(this, "Đã cập nhật công việc", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ===== CÁC CHỨC NĂNG =====
    private void xoaCongViec() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa công việc này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    danhSachCongViec.remove(congViecDangChon);
                    adapter.updateDanhSach(danhSachCongViec);
                    Toast.makeText(this, "Đã xóa công việc", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void danhDauHoanThanh() {
        congViecDangChon.setDaHoanThanh(!congViecDangChon.isDaHoanThanh());
        adapter.notifyItemChanged(viTriDangChon);
        Toast.makeText(this,
                congViecDangChon.isDaHoanThanh() ? "Đã hoàn thành" : "Chưa hoàn thành",
                Toast.LENGTH_SHORT).show();
    }

    private void xoaTatCaCongViec() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa tất cả")
                .setMessage("Bạn có chắc muốn xóa tất cả công việc?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    danhSachCongViec.clear();
                    adapter.updateDanhSach(danhSachCongViec);
                    Toast.makeText(this, "Đã xóa tất cả công việc", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void sapXepTheoTen() {
        Collections.sort(danhSachCongViec, (cv1, cv2) ->
                cv1.getTenCongViec().compareTo(cv2.getTenCongViec())
        );
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, "Đã sắp xếp theo tên", Toast.LENGTH_SHORT).show();
    }

    private void sapXepTheoNgay() {
        Collections.sort(danhSachCongViec, (cv1, cv2) ->
                Long.compare(cv2.getNgayTao(), cv1.getNgayTao())
        );
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, "Đã sắp xếp theo ngày", Toast.LENGTH_SHORT).show();
    }

    private void hienThiTatCaCongViec() {
        adapter.updateDanhSach(danhSachCongViec);
        Toast.makeText(this, "Hiển thị tất cả công việc", Toast.LENGTH_SHORT).show();
    }

    private void locCongViecTheoLoai(String loai) {
        List<CongViec> danhSachLoc = new ArrayList<>();
        for (CongViec cv : danhSachCongViec) {
            if (cv.getLoaiCongViec().equals(loai)) {
                danhSachLoc.add(cv);
            }
        }
        adapter.updateDanhSach(danhSachLoc);
        Toast.makeText(this, "Lọc: " + loai, Toast.LENGTH_SHORT).show();
    }

    private void hienThiCongViecHoanThanh() {
        List<CongViec> danhSachLoc = new ArrayList<>();
        for (CongViec cv : danhSachCongViec) {
            if (cv.isDaHoanThanh()) {
                danhSachLoc.add(cv);
            }
        }
        adapter.updateDanhSach(danhSachLoc);
        Toast.makeText(this, "Đã hoàn thành", Toast.LENGTH_SHORT).show();
    }

    private void hienThiGioiThieu() {
        new AlertDialog.Builder(this)
                .setTitle("Giới thiệu")
                .setMessage("Todo List App v1.0\n\n" +
                        "Ứng dụng quản lý công việc với đầy đủ các loại menu:\n" +
                        "• Context Menu (nhấn giữ)\n" +
                        "• Toolbar Options Menu\n" +
                        "• Popup Menu (FAB)\n" +
                        "• Navigation Drawer\n\n" +
                        "Phát triển bởi: Nguyễn Minh Dũng")
                .setPositiveButton("Đóng", null)
                .show();
    }
}

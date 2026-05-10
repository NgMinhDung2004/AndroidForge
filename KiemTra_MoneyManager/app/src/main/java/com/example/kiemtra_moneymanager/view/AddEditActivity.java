package com.example.kiemtra_moneymanager.view;

import android.app.DatePickerDialog;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.kiemtra_moneymanager.R;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import com.example.kiemtra_moneymanager.viewmodel.ThuChiViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {
    private EditText etTitle, etAmount, etNote;
    private RadioGroup rgType;
    private RadioButton rbChi, rbThu;
    private Spinner spinnerCategory;
    private Button btnPickDate, btnSave;
    private TextView tvSelectedDate;

    private ThuChiViewModel viewModel;
    private Calendar calendar = Calendar.getInstance();
    private ThuChiEntity currentItem;

    private String[] categoriesChi = {"Ăn uống", "Sinh hoạt", "Nhà ở", "Sức khỏe", "Di chuyển", "Giải trí", "Giáo dục", "Khác"};
    private String[] categoriesThu = {"Lương", "Thưởng", "Kinh doanh", "Tiền lãi", "Quà tặng", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initViews();
        viewModel = new ViewModelProvider(this).get(ThuChiViewModel.class);

        currentItem = (ThuChiEntity) getIntent().getSerializableExtra("item");
        if (currentItem != null) {
            populateData();
        } else {
            updateSpinner(0); // Mặc định là Chi
            updateDateText();
        }

        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_chi) {
                updateSpinner(0);
            } else {
                updateSpinner(1);
            }
        });

        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateText();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> saveData());
    }

    private void initViews() {
        etTitle = findViewById(R.id.et_title);
        etAmount = findViewById(R.id.et_amount);
        etNote = findViewById(R.id.et_note);
        rgType = findViewById(R.id.rg_type);
        rbChi = findViewById(R.id.rb_chi);
        rbThu = findViewById(R.id.rb_thu);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnPickDate = findViewById(R.id.btn_pick_date);
        btnSave = findViewById(R.id.btn_save);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
    }

    private void populateData() {
        etTitle.setText(currentItem.getTieuDe());
        etAmount.setText(String.valueOf(currentItem.getSoTien()));
        etNote.setText(currentItem.getGhiChu());
        calendar.setTimeInMillis(currentItem.getNgayThuChi());
        updateDateText();

        if (currentItem.getLoaiThuChi() == 1) {
            rbThu.setChecked(true);
            updateSpinner(1);
            setSpinnerSelection(categoriesThu, currentItem.getDanhMuc());
        } else {
            rbChi.setChecked(true);
            updateSpinner(0);
            setSpinnerSelection(categoriesChi, currentItem.getDanhMuc());
        }
    }

    private void updateSpinner(int type) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, type == 0 ? categoriesChi : categoriesThu);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void setSpinnerSelection(String[] categories, String value) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(value)) {
                spinnerCategory.setSelection(i);
                break;
            }
        }
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvSelectedDate.setText("Ngày: " + sdf.format(calendar.getTime()));
    }

    private void saveData() {
        String title = etTitle.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Tiêu đề không được để trống");
            return;
        }
        if (amountStr.isEmpty()) {
            etAmount.setError("Số tiền không được để trống");
            return;
        }

        double amount = Double.parseDouble(amountStr);
        if (amount <= 0) {
            etAmount.setError("Số tiền phải > 0");
            return;
        }

        int type = rbThu.isChecked() ? 1 : 0;
        String category = spinnerCategory.getSelectedItem().toString();
        String note = etNote.getText().toString().trim();

        if (currentItem == null) {
            currentItem = new ThuChiEntity();
        }

        currentItem.setTieuDe(title);
        currentItem.setSoTien(amount);
        currentItem.setLoaiThuChi(type);
        currentItem.setDanhMuc(category);
        currentItem.setNgayThuChi(calendar.getTimeInMillis());
        currentItem.setGhiChu(note);

        if (currentItem.getMa() == 0) {
            viewModel.insert(currentItem);
        } else {
            viewModel.update(currentItem);
        }

        Toast.makeText(this, "Đã lưu", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        finish();
    }
}

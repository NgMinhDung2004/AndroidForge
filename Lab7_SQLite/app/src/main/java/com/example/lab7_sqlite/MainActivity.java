package com.example.lab7_sqlite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab7_sqlite.model.Student;
import com.example.lab7_sqlite.view.StudentAdapter;
import com.example.lab7_sqlite.viewmodel.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private StudentViewModel viewModel;
    private FloatingActionButton fabAdd;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Views
        initViews();
        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(StudentViewModel.class);
        // Setup RecyclerView
        setupRecyclerView();
        // Observe LiveData
        observeViewModel();
        // Setup listeners
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewStudents);
        fabAdd = findViewById(R.id.fabAdd);
        etSearch = findViewById(R.id.etSearch);
    }

    private void setupRecyclerView() {
        adapter = new StudentAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // Thiết lập listener cho adapter
        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Student student) {
                showEditStudentDialog(student);
            }

            @Override
            public void onDeleteClick(Student student) {
                showDeleteConfirmDialog(student);
            }

            @Override
            public void onItemClick(Student student) {
                showStudentDetailDialog(student);
            }
        });
    }

    private void observeViewModel() {
        // Observe danh sách sinh viên
        viewModel.getStudentListLiveData().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                adapter.setStudentList(students);
            }
        });

        // Observe message
        viewModel.getMessageLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupListeners() {
        // Xử lý sự kiện click FAB để thêm sinh viên
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentDialog();
            }
        });

        // Xử lý tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchStudentByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Hiển thị dialog thêm sinh viên
    private void showAddStudentDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_student, null);
        final EditText etStudentCode = dialogView.findViewById(R.id.etStudentCode);
        final EditText etFullName = dialogView.findViewById(R.id.etFullName);
        final EditText etDateOfBirth = dialogView.findViewById(R.id.etDateOfBirth);
        final EditText etMajor = dialogView.findViewById(R.id.etMajor);
        final EditText etGpa = dialogView.findViewById(R.id.etGpa);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm sinh viên mới");
        builder.setView(dialogView);
        builder.setPositiveButton("Thêm", null);
        builder.setNegativeButton("Hủy", null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Override positive button để validate trước khi đóng dialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentCode = etStudentCode.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();
                String dateOfBirth = etDateOfBirth.getText().toString().trim();
                String major = etMajor.getText().toString().trim();
                String gpaStr = etGpa.getText().toString().trim();

                // Validate
                if (studentCode.isEmpty()) {
                    etStudentCode.setError("Vui lòng nhập mã sinh viên");
                    return;
                }
                if (fullName.isEmpty()) {
                    etFullName.setError("Vui lòng nhập họ tên");
                    return;
                }
                if (dateOfBirth.isEmpty()) {
                    etDateOfBirth.setError("Vui lòng nhập ngày sinh");
                    return;
                }
                if (major.isEmpty()) {
                    etMajor.setError("Vui lòng nhập ngành học");
                    return;
                }
                if (gpaStr.isEmpty()) {
                    etGpa.setError("Vui lòng nhập GPA");
                    return;
                }

                double gpa;
                try {
                    gpa = Double.parseDouble(gpaStr);
                    if (gpa < 0.0 || gpa > 4.0) {
                        etGpa.setError("GPA phải từ 0.0 đến 4.0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    etGpa.setError("GPA không hợp lệ");
                    return;
                }

                // Tạo đối tượng Student và thêm vào database
                Student student = new Student(studentCode, fullName, dateOfBirth, major, gpa);
                viewModel.insertStudent(student);
                dialog.dismiss();
            }
        });
    }

    // Hiển thị dialog sửa sinh viên
    private void showEditStudentDialog(final Student student) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_student, null);
        final EditText etStudentCode = dialogView.findViewById(R.id.etStudentCode);
        final EditText etFullName = dialogView.findViewById(R.id.etFullName);
        final EditText etDateOfBirth = dialogView.findViewById(R.id.etDateOfBirth);
        final EditText etMajor = dialogView.findViewById(R.id.etMajor);
        final EditText etGpa = dialogView.findViewById(R.id.etGpa);

        // Fill dữ liệu hiện tại
        etStudentCode.setText(student.getStudentCode());
        etFullName.setText(student.getFullName());
        etDateOfBirth.setText(student.getDateOfBirth());
        etMajor.setText(student.getMajor());
        etGpa.setText(String.valueOf(student.getGpa()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật thông tin sinh viên");
        builder.setView(dialogView);
        builder.setPositiveButton("Cập nhật", null);
        builder.setNegativeButton("Hủy", null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentCode = etStudentCode.getText().toString().trim();
                String fullName = etFullName.getText().toString().trim();
                String dateOfBirth = etDateOfBirth.getText().toString().trim();
                String major = etMajor.getText().toString().trim();
                String gpaStr = etGpa.getText().toString().trim();

                // Validate
                if (studentCode.isEmpty()) {
                    etStudentCode.setError("Vui lòng nhập mã sinh viên");
                    return;
                }
                if (fullName.isEmpty()) {
                    etFullName.setError("Vui lòng nhập họ tên");
                    return;
                }
                if (dateOfBirth.isEmpty()) {
                    etDateOfBirth.setError("Vui lòng nhập ngày sinh");
                    return;
                }
                if (major.isEmpty()) {
                    etMajor.setError("Vui lòng nhập ngành học");
                    return;
                }
                if (gpaStr.isEmpty()) {
                    etGpa.setError("Vui lòng nhập GPA");
                    return;
                }

                double gpa;
                try {
                    gpa = Double.parseDouble(gpaStr);
                    if (gpa < 0.0 || gpa > 4.0) {
                        etGpa.setError("GPA phải từ 0.0 đến 4.0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    etGpa.setError("GPA không hợp lệ");
                    return;
                }

                // Cập nhật thông tin sinh viên
                student.setStudentCode(studentCode);
                student.setFullName(fullName);
                student.setDateOfBirth(dateOfBirth);
                student.setMajor(major);
                student.setGpa(gpa);
                viewModel.updateStudent(student);
                dialog.dismiss();
            }
        });
    }

    // Hiển thị dialog xác nhận xóa
    private void showDeleteConfirmDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sinh viên " + student.getFullName() + "?");
        builder.setPositiveButton("Xóa", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                viewModel.deleteStudent(student.getId());
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // Hiển thị dialog chi tiết sinh viên
    private void showStudentDetailDialog(Student student) {
        String message = "Mã sinh viên: " + student.getStudentCode() + "\n" +
                "Họ tên: " + student.getFullName() + "\n" +
                "Ngày sinh: " + student.getDateOfBirth() + "\n" +
                "Ngành học: " + student.getMajor() + "\n" +
                "GPA: " + String.format("%.2f", student.getGpa());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chi tiết sinh viên");
        builder.setMessage(message);
        builder.setPositiveButton("Đóng", null);
        builder.show();
    }
}

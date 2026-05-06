package com.example.lab7_sqlite.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.lab7_sqlite.model.Student;
import com.example.lab7_sqlite.model.StudentRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository repository;
    private MutableLiveData<List<Student>> studentListLiveData;
    private MutableLiveData<String> messageLiveData;
    private ExecutorService executorService;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
        studentListLiveData = new MutableLiveData<>();
        messageLiveData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
        loadAllStudents();
    }

    public LiveData<List<Student>> getStudentListLiveData() {
        return studentListLiveData;
    }

    public LiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void loadAllStudents() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Student> students = repository.getAllStudents();
                studentListLiveData.postValue(students);
            }
        });
    }

    public void insertStudent(final Student student) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long result = repository.insertStudent(student);
                if (result > 0) {
                    messageLiveData.postValue("Thêm sinh viên thành công!");
                    loadAllStudents();
                } else {
                    messageLiveData.postValue("Thêm sinh viên thất bại! Mã sinh viên có thể đã tồn tại.");
                }
            }
        });
    }

    public void updateStudent(final Student student) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int result = repository.updateStudent(student);
                if (result > 0) {
                    messageLiveData.postValue("Cập nhật sinh viên thành công!");
                    loadAllStudents();
                } else {
                    messageLiveData.postValue("Cập nhật sinh viên thất bại!");
                }
            }
        });
    }

    public void deleteStudent(final int studentId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int result = repository.deleteStudent(studentId);
                if (result > 0) {
                    messageLiveData.postValue("Xóa sinh viên thành công!");
                    loadAllStudents();
                } else {
                    messageLiveData.postValue("Xóa sinh viên thất bại!");
                }
            }
        });
    }

    public void searchStudentByName(final String name) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Student> students;
                if (name == null || name.trim().isEmpty()) {
                    students = repository.getAllStudents();
                } else {
                    students = repository.searchStudentByName(name);
                }
                studentListLiveData.postValue(students);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}

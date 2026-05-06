package com.example.lab7_sqlite.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab7_sqlite.R;
import com.example.lab7_sqlite.model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Student> studentList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Student student);
        void onDeleteClick(Student student);
        void onItemClick(Student student);
    }

    public StudentAdapter() {
        this.studentList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student, listener);
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStudentCode;
        private TextView tvFullName;
        private TextView tvDateOfBirth;
        private TextView tvMajor;
        private TextView tvGpa;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentCode = itemView.findViewById(R.id.tvStudentCode);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvDateOfBirth = itemView.findViewById(R.id.tvDateOfBirth);
            tvMajor = itemView.findViewById(R.id.tvMajor);
            tvGpa = itemView.findViewById(R.id.tvGpa);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final Student student, final OnItemClickListener listener) {
            tvStudentCode.setText("MSV: " + student.getStudentCode());
            tvFullName.setText(student.getFullName());
            tvDateOfBirth.setText("Ngày sinh: " + student.getDateOfBirth());
            tvMajor.setText("Ngành: " + student.getMajor());
            tvGpa.setText(String.format("GPA: %.2f", student.getGpa()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(student);
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEditClick(student);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onDeleteClick(student);
                    }
                }
            });
        }
    }
}

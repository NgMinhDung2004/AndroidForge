package com.example.lab7_sqlite.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private DatabaseHelper databaseHelper;

    public StudentRepository(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public long insertStudent(Student student) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, student.getFullName());
        values.put(DatabaseHelper.COLUMN_DATE_OF_BIRTH, student.getDateOfBirth());
        values.put(DatabaseHelper.COLUMN_MAJOR, student.getMajor());
        values.put(DatabaseHelper.COLUMN_GPA, student.getGpa());
        return db.insert(DatabaseHelper.TABLE_STUDENT, null, values);
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_STUDENT +
                " ORDER BY " + DatabaseHelper.COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String studentCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_OF_BIRTH));
                String major = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAJOR));
                double gpa = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GPA));

                Student student = new Student(id, studentCode, fullName, dateOfBirth, major, gpa);
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_CODE, student.getStudentCode());
        values.put(DatabaseHelper.COLUMN_FULL_NAME, student.getFullName());
        values.put(DatabaseHelper.COLUMN_DATE_OF_BIRTH, student.getDateOfBirth());
        values.put(DatabaseHelper.COLUMN_MAJOR, student.getMajor());
        values.put(DatabaseHelper.COLUMN_GPA, student.getGpa());
        return db.update(
                DatabaseHelper.TABLE_STUDENT,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())}
        );
    }

    public int deleteStudent(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.TABLE_STUDENT,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public List<Student> searchStudentByName(String name) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_STUDENT +
                " WHERE " + DatabaseHelper.COLUMN_FULL_NAME + " LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + name + "%"});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String studentCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_CODE));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FULL_NAME));
                String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_OF_BIRTH));
                String major = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MAJOR));
                double gpa = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GPA));
                Student student = new Student(id, studentCode, fullName, dateOfBirth, major, gpa);
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }
}

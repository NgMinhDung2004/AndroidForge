package com.example.lab7_sqlite.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "student_management.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "student";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_CODE = "student_code";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_MAJOR = "major";
    public static final String COLUMN_GPA = "gpa";

    private static final String CREATE_TABLE_STUDENT =
            "CREATE TABLE " + TABLE_STUDENT + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_CODE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                    COLUMN_DATE_OF_BIRTH + " TEXT NOT NULL, " +
                    COLUMN_MAJOR + " TEXT NOT NULL, " +
                    COLUMN_GPA + " REAL NOT NULL" +
                    ")";

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENT);
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_STUDENT +
                " (" + COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV001', 'Nguyễn Văn An', '15/03/2003', 'Công nghệ thông tin', 3.5)");
        db.execSQL("INSERT INTO " + TABLE_STUDENT +
                " (" + COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV002', 'Trần Thị Bình', '22/07/2002', 'Kế toán', 3.8)");
        db.execSQL("INSERT INTO " + TABLE_STUDENT +
                " (" + COLUMN_STUDENT_CODE + ", " + COLUMN_FULL_NAME + ", " +
                COLUMN_DATE_OF_BIRTH + ", " + COLUMN_MAJOR + ", " + COLUMN_GPA + ") VALUES " +
                "('SV003', 'Phạm Văn Cường', '10/12/2003', 'Quản trị kinh doanh', 3.2)");
    }

    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}

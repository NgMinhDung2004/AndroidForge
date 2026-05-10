package com.example.kiemtra_moneymanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MoneyManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_THUCHI = "thuChi";
    public static final String COLUMN_MA = "ma";
    public static final String COLUMN_TIEUDE = "tieuDe";
    public static final String COLUMN_SOTIEN = "soTien";
    public static final String COLUMN_LOAITHUCHI = "loaiThuChi";
    public static final String COLUMN_DANHMUC = "danhMuc";
    public static final String COLUMN_NGAYTHUCHI = "ngayThuChi";
    public static final String COLUMN_GHICHU = "ghiChu";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE = "CREATE TABLE " + TABLE_THUCHI + "("
                    + COLUMN_MA + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TIEUDE + " TEXT NOT NULL,"
                    + COLUMN_SOTIEN + " REAL NOT NULL,"
                    + COLUMN_LOAITHUCHI + " INTEGER NOT NULL,"
                    + COLUMN_DANHMUC + " TEXT,"
                    + COLUMN_NGAYTHUCHI + " INTEGER,"
                    + COLUMN_GHICHU + " TEXT" + ")";
            db.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUCHI);
        onCreate(db);
    }

    public long insertThuChi(ThuChiEntity item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIEUDE, item.getTieuDe());
            values.put(COLUMN_SOTIEN, item.getSoTien());
            values.put(COLUMN_LOAITHUCHI, item.getLoaiThuChi());
            values.put(COLUMN_DANHMUC, item.getDanhMuc());
            values.put(COLUMN_NGAYTHUCHI, item.getNgayThuChi());
            values.put(COLUMN_GHICHU, item.getGhiChu());
            return db.insert(TABLE_THUCHI, null, values);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting data", e);
            return -1;
        }
    }

    public int updateThuChi(ThuChiEntity item) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIEUDE, item.getTieuDe());
            values.put(COLUMN_SOTIEN, item.getSoTien());
            values.put(COLUMN_LOAITHUCHI, item.getLoaiThuChi());
            values.put(COLUMN_DANHMUC, item.getDanhMuc());
            values.put(COLUMN_NGAYTHUCHI, item.getNgayThuChi());
            values.put(COLUMN_GHICHU, item.getGhiChu());
            return db.update(TABLE_THUCHI, values, COLUMN_MA + " = ?", new String[]{String.valueOf(item.getMa())});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating data", e);
            return 0;
        }
    }

    public void deleteThuChi(int id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_THUCHI, COLUMN_MA + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error deleting data", e);
        }
    }

    public List<ThuChiEntity> getAllThuChi() {
        List<ThuChiEntity> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.query(TABLE_THUCHI, null, null, null, null, null, COLUMN_NGAYTHUCHI + " DESC");
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ThuChiEntity item = new ThuChiEntity(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIEUDE)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SOTIEN)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_LOAITHUCHI)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DANHMUC)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_NGAYTHUCHI)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHICHU))
                    );
                    list.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching data", e);
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}

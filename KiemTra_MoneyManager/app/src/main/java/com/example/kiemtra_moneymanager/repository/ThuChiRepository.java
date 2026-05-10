package com.example.kiemtra_moneymanager.repository;

import android.content.Context;
import com.example.kiemtra_moneymanager.model.DatabaseHelper;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import java.util.List;

public class ThuChiRepository {
    private DatabaseHelper dbHelper;

    public ThuChiRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<ThuChiEntity> getAll() {
        return dbHelper.getAllThuChi();
    }

    public long insert(ThuChiEntity item) {
        return dbHelper.insertThuChi(item);
    }

    public int update(ThuChiEntity item) {
        return dbHelper.updateThuChi(item);
    }

    public void delete(int id) {
        dbHelper.deleteThuChi(id);
    }
}

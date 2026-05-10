package com.example.kiemtra_moneymanager.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.kiemtra_moneymanager.model.ThuChiEntity;
import com.example.kiemtra_moneymanager.repository.ThuChiRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ThuChiViewModel extends AndroidViewModel {
    private final ThuChiRepository repository;
    private final MutableLiveData<List<ThuChiEntity>> allThuChi = new MutableLiveData<>(new ArrayList<>());
    private List<ThuChiEntity> originalList = new ArrayList<>();
    private final MutableLiveData<Double> totalThu = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalChi = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> balance = new MutableLiveData<>(0.0);

    public ThuChiViewModel(@NonNull Application application) {
        super(application);
        repository = new ThuChiRepository(application);
        refreshData();
    }

    public LiveData<List<ThuChiEntity>> getAllThuChi() { return allThuChi; }
    public LiveData<Double> getTotalThu() { return totalThu; }
    public LiveData<Double> getTotalChi() { return totalChi; }
    public LiveData<Double> getBalance() { return balance; }
    public List<ThuChiEntity> getOriginalListSnapshot() { return new ArrayList<>(originalList); }

    public void refreshData() {
        List<ThuChiEntity> data = repository.getAll();
        originalList = (data != null) ? data : new ArrayList<>();
        allThuChi.setValue(originalList);
        calculateStats(originalList);
    }

    public void filter(int type, int month, int year) {
        List<ThuChiEntity> filtered = new ArrayList<>();
        for (ThuChiEntity item : originalList) {
            boolean matchType = (type == -1) || (item.getLoaiThuChi() == type);
            
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(item.getNgayThuChi());
            boolean matchMonth = (month == -1) || (cal.get(Calendar.MONTH) == month);
            boolean matchYear = (year == -1) || (cal.get(Calendar.YEAR) == year);

            if (matchType && matchMonth && matchYear) {
                filtered.add(item);
            }
        }
        allThuChi.setValue(filtered);
    }

    private void calculateStats(List<ThuChiEntity> list) {
        double thu = 0;
        double chi = 0;
        for (ThuChiEntity item : list) {
            if (item.getLoaiThuChi() == 1) thu += item.getSoTien();
            else chi += item.getSoTien();
        }
        totalThu.setValue(thu);
        totalChi.setValue(chi);
        balance.setValue(thu - chi);
    }

    public void insert(ThuChiEntity item) {
        repository.insert(item);
        refreshData();
    }

    public void update(ThuChiEntity item) {
        repository.update(item);
        refreshData();
    }

    public void delete(int id) {
        repository.delete(id);
        refreshData();
    }

    public ThuChiEntity getMaxThu() {
        ThuChiEntity max = null;
        for (ThuChiEntity item : originalList) {
            if (item.getLoaiThuChi() == 1) {
                if (max == null || item.getSoTien() > max.getSoTien()) max = item;
            }
        }
        return max;
    }

    public ThuChiEntity getMaxChi() {
        ThuChiEntity max = null;
        for (ThuChiEntity item : originalList) {
            if (item.getLoaiThuChi() == 0) {
                if (max == null || item.getSoTien() > max.getSoTien()) max = item;
            }
        }
        return max;
    }
}

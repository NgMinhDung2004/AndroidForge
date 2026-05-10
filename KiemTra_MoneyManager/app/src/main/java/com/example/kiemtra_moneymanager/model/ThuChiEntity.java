package com.example.kiemtra_moneymanager.model;

import java.io.Serializable;

public class ThuChiEntity implements Serializable {
    private int ma;
    private String tieuDe;
    private double soTien;
    private int loaiThuChi; // 0: Chi, 1: Thu
    private String danhMuc;
    private long ngayThuChi; // Timestamp
    private String ghiChu;

    public ThuChiEntity() {}

    public ThuChiEntity(int ma, String tieuDe, double soTien, int loaiThuChi, String danhMuc, long ngayThuChi, String ghiChu) {
        this.ma = ma;
        this.tieuDe = tieuDe;
        this.soTien = soTien;
        this.loaiThuChi = loaiThuChi;
        this.danhMuc = danhMuc;
        this.ngayThuChi = ngayThuChi;
        this.ghiChu = ghiChu;
    }

    public int getMa() { return ma; }
    public void setMa(int ma) { this.ma = ma; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public int getLoaiThuChi() { return loaiThuChi; }
    public void setLoaiThuChi(int loaiThuChi) { this.loaiThuChi = loaiThuChi; }

    public String getDanhMuc() { return danhMuc; }
    public void setDanhMuc(String danhMuc) { this.danhMuc = danhMuc; }

    public long getNgayThuChi() { return ngayThuChi; }
    public void setNgayThuChi(long ngayThuChi) { this.ngayThuChi = ngayThuChi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}

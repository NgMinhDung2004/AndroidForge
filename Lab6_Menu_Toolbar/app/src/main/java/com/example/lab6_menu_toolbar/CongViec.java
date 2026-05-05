package com.example.lab6_menu_toolbar;

public class CongViec {
    private int id;
    private String tenCongViec;
    private String loaiCongViec; // "CONG_VIEC", "CA_NHAN", "KHAN_CAP"
    private boolean daHoanThanh;
    private long ngayTao;

    public CongViec(int id, String tenCongViec, String loaiCongViec) {
        this.id = id;
        this.tenCongViec = tenCongViec;
        this.loaiCongViec = loaiCongViec;
        this.daHoanThanh = false;
        this.ngayTao = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenCongViec() {
        return tenCongViec;
    }

    public void setTenCongViec(String tenCongViec) {
        this.tenCongViec = tenCongViec;
    }

    public String getLoaiCongViec() {
        return loaiCongViec;
    }

    public void setLoaiCongViec(String loaiCongViec) {
        this.loaiCongViec = loaiCongViec;
    }

    public boolean isDaHoanThanh() {
        return daHoanThanh;
    }

    public void setDaHoanThanh(boolean daHoanThanh) {
        this.daHoanThanh = daHoanThanh;
    }

    public long getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(long ngayTao) {
        this.ngayTao = ngayTao;
    }
}

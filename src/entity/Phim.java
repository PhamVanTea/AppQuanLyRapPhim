package entity;

import java.util.Objects;

public class Phim {
    private String maPhim;
    private String tenPhim;
    private String daoDien;
    private String dienVien;
    private TheLoai theLoai;
    private int thoiLuong;
    private String xepHang;
    private String moTa;

    public Phim() {
    }

    public Phim(String maPhim) {
        this.maPhim = maPhim;
    }

    public Phim(String maPhim, String tenPhim, String daoDien, String dienVien, TheLoai theLoai, int thoiLuong, String xepHang, String moTa) {
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.xepHang = xepHang;
        this.moTa = moTa;
    }

    public String getMaPhim() {
        return maPhim;
    }

    public void setMaPhim(String maPhim) {
        this.maPhim = maPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
        this.daoDien = daoDien;
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
        this.dienVien = dienVien;
    }

    public TheLoai getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(TheLoai theLoai) {
        this.theLoai = theLoai;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getXepHang() {
        return xepHang;
    }

    public void setXepHang(String xepHang) {
        this.xepHang = xepHang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenPhim != null ? tenPhim : (maPhim != null ? maPhim : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phim phim = (Phim) o;
        return Objects.equals(maPhim, phim.maPhim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhim);
    }
}
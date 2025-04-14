package entity;

import java.util.Objects;

public class HoaDon {
    private String maHoaDon;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private float tongTien;
    private String ngayLapHoaDon;
    private String trangThai;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public HoaDon(String maHoaDon, NhanVien nhanVien, KhachHang khachHang, float tongTien, String ngayLapHoaDon, String trangThai) {
        this.maHoaDon = maHoaDon;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.tongTien = tongTien;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.trangThai = trangThai;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(String ngayLapHoaDon) {
        this.ngayLapHoaDon = ngayLapHoaDon;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {

        return maHoaDon != null ? maHoaDon : ""; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHoaDon, hoaDon.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }
}
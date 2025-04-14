package entity;

import java.util.Objects;

public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String chucVu;
    private String tenDangNhap;
    private String matKhau;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String tenNhanVien) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
    }

    public NhanVien(String maNhanVien, String tenNhanVien, String chucVu, String tenDangNhap, String matKhau) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.chucVu = chucVu;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
         return (tenNhanVien != null) ? tenNhanVien : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNhanVien, nhanVien.maNhanVien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien);
    }
}
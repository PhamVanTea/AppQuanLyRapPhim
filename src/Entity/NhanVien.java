package Entity;

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
    	if (maNhanVien != null && !maNhanVien.trim().isEmpty()) {
            this.maNhanVien = maNhanVien;
        } else {
            throw new IllegalArgumentException("Mã nhân viên không được để trống.");
        }
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
    	if (tenNhanVien != null && !tenNhanVien.trim().isEmpty()) {
            this.tenNhanVien = tenNhanVien.trim();
        } else {
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        }
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
    	if (chucVu != null && !chucVu.trim().isEmpty()) {
            this.chucVu = chucVu;
        } else {
            throw new IllegalArgumentException("Chức vụ không được để trống.");
        }
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
    	if (tenDangNhap != null && !tenDangNhap.trim().isEmpty()) {
            this.tenDangNhap = tenDangNhap;
        } else {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
    	if (matKhau != null && matKhau.length() >= 6) {
            this.matKhau = matKhau;
        } else {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        }
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
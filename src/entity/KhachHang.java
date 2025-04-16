package entity;

import java.util.Objects;

public class KhachHang {
    private String maKhachHang;
    private String tenKhachHang;
    private String sdt;
    private String email;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String tenKhachHang) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
    }

    public KhachHang(String maKhachHang, String tenKhachHang, String sdt, String email) {
        this.maKhachHang = maKhachHang;
        this.tenKhachHang = tenKhachHang;
        this.sdt = sdt;
        this.email = email;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
    	if (maKhachHang != null && !maKhachHang.trim().isEmpty()) {
            this.maKhachHang = maKhachHang;
        } else {
            throw new IllegalArgumentException("Mã khách hàng không được để trống.");
        }
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
    	if (tenKhachHang != null && !tenKhachHang.trim().isEmpty()) {
            this.tenKhachHang = tenKhachHang.trim();
        } else {
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }
    }

    public String getSDT() {
        return sdt;
    }

    //đổi SDT -> sdt (Nếu có lỗi biết chỗ fix)
    public void setSDT(String sdt) {
    	if (sdt != null && sdt.matches("\\d{10,11}")) {
            this.sdt = sdt;
        } else {
            throw new IllegalArgumentException("Số điện thoại phải có 10-11 chữ số.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
    	if (email != null) { //ktra nhập vào emmail ở lớp ui
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email không để trống.");
        }
    }

    @Override
    public String toString() {
        return tenKhachHang != null ? tenKhachHang : (maKhachHang != null ? maKhachHang : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKhachHang, khachHang.maKhachHang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKhachHang);
    }
}
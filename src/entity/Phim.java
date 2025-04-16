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
    	if (maPhim != null && !maPhim.trim().isEmpty()) {
            this.maPhim = maPhim;
        } else {
            throw new IllegalArgumentException("Mã phim không được để trống.");
        }
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
    	if (tenPhim != null && !tenPhim.trim().isEmpty()) {
            this.tenPhim = tenPhim;
        } else {
            throw new IllegalArgumentException("Tên phim không được để trống.");
        }
    }

    public String getDaoDien() {
        return daoDien;
    }

    public void setDaoDien(String daoDien) {
    	if (daoDien != null && !daoDien.trim().isEmpty()) {
            this.daoDien = daoDien;
        } else {
            throw new IllegalArgumentException("Đạo diễn không được để trống.");
        }
    }

    public String getDienVien() {
        return dienVien;
    }

    public void setDienVien(String dienVien) {
    	if (dienVien != null && !dienVien.trim().isEmpty()) {
            this.dienVien = dienVien;
        } else {
            throw new IllegalArgumentException("Diễn viên không được để trống.");
        }
    }

    public TheLoai getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(TheLoai theLoai) {
    	if (theLoai != null) {
            this.theLoai = theLoai;
        } else {
            throw new IllegalArgumentException("Thể loại không được null.");
        }
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
    	if (thoiLuong > 0) {
            this.thoiLuong = thoiLuong;
        } else {
            throw new IllegalArgumentException("Thời lượng phải lớn hơn 0.");
        }
    }

    public String getXepHang() {
        return xepHang;
    }

    public void setXepHang(String xepHang) {
    	if (xepHang != null && !xepHang.trim().isEmpty()) {
            this.xepHang = xepHang;
        } else {
            throw new IllegalArgumentException("Xếp hạng không được để trống.");
        }
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
    	//mô tả được null
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
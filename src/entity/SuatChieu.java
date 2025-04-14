package entity;

import java.util.Objects;

public class SuatChieu {
    private String maSuatChieu;
    private Phim phim;
    private PhongChieu phongChieu;
    private float gia;
    private String thoiGianBD;
    private String thoiGianKetThuc;

    public SuatChieu() {
    }

    public SuatChieu(String maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public SuatChieu(String maSuatChieu, Phim phim, PhongChieu phongChieu, float gia, String thoiGianBD, String thoiGianKetThuc) {
        this.maSuatChieu = maSuatChieu;
        this.phim = phim;
        this.phongChieu = phongChieu;
        this.gia = gia;
        this.thoiGianBD = thoiGianBD;
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getMaSuatChieu() {
        return maSuatChieu;
    }

    public void setMaSuatChieu(String maSuatChieu) {
        this.maSuatChieu = maSuatChieu;
    }

    public Phim getPhim() {
        return phim;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
    }

    public PhongChieu getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(PhongChieu phongChieu) {
        this.phongChieu = phongChieu;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        if (gia < 0) {
        	System.out.println("Lỗi: Giá phải là số dương!");
            return;
        } else {
            this.gia = gia;
        }
    }

    public String getThoiGianBD() {
        return thoiGianBD;
    }

    public void setThoiGianBD(String thoiGianBD) {
        this.thoiGianBD = thoiGianBD;
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    @Override
    public String toString() {
        String tenPhimStr = (phim != null && phim.getTenPhim() != null) ? phim.getTenPhim() : "N/A";
        String tenPhongStr = (phongChieu != null && phongChieu.getTenPhong() != null) ? phongChieu.getTenPhong() : "N/A";
        String thoiGianBDStr = (thoiGianBD != null && !thoiGianBD.trim().isEmpty()) ? thoiGianBD : "N/A";
        return tenPhimStr + " - " + tenPhongStr + " - " + thoiGianBDStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuatChieu suatChieu = (SuatChieu) o;
        return Objects.equals(maSuatChieu, suatChieu.maSuatChieu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSuatChieu);
    }
}
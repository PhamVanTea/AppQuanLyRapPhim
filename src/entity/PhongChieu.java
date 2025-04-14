package entity;

import java.util.Objects;

public class PhongChieu {
    private String maPhong;
    private String tenPhong;
    private int soGhe;

    public PhongChieu() {
    }

    public PhongChieu(String maPhong) {
        this.maPhong = maPhong;
    }

    public PhongChieu(String maPhong, String tenPhong, int soGhe) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.soGhe = soGhe;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    @Override
    public String toString() {
        return tenPhong != null ? tenPhong : (maPhong != null ? maPhong : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhongChieu that = (PhongChieu) o;
        return Objects.equals(maPhong, that.maPhong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhong);
    }
}
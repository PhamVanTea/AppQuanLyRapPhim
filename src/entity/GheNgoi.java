package entity;

import java.util.Objects;

public class GheNgoi {
    private String maGhe;
    private PhongChieu phongChieu;
    private String hang;
    private int soGhe;
    private String trangThai;

    public GheNgoi() {
    }

    public GheNgoi(String maGhe) {
        this.maGhe = maGhe;
    }

    public GheNgoi(String maGhe, PhongChieu phongChieu, String hang, int soGhe, String trangThai) {
        this.maGhe = maGhe;
        this.phongChieu = phongChieu;
        this.hang = hang;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public PhongChieu getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(PhongChieu phongChieu) {
        this.phongChieu = phongChieu;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }


    @Override
    public String toString() {
        return (hang != null ? hang : "") + soGhe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GheNgoi gheNgoi = (GheNgoi) o;
        return Objects.equals(maGhe, gheNgoi.maGhe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maGhe);
    }
}
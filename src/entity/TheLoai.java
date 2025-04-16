package entity;

import java.util.Objects;

public class TheLoai {

    private String maTheLoai;
    private String tenTheLoai;
    private String moTa;

    public TheLoai() {
    }

    public TheLoai(String maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public TheLoai(String maTheLoai, String tenTheLoai, String moTa) {
        this.maTheLoai = maTheLoai;
        this.tenTheLoai = tenTheLoai;
        this.moTa = moTa;
    }

    public String getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(String maTheLoai) {
    	if (maTheLoai != null && !maTheLoai.trim().isEmpty()) {
            this.maTheLoai = maTheLoai;
        } else {
            throw new IllegalArgumentException("Mã thể loại không được để trống.");
        }
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
    	if (tenTheLoai != null && !tenTheLoai.trim().isEmpty()) {
            this.tenTheLoai = tenTheLoai;
        } else {
            throw new IllegalArgumentException("Tên thể loại không được để trống.");
        }
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenTheLoai != null ? tenTheLoai : (maTheLoai != null ? maTheLoai : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheLoai theLoai = (TheLoai) o;
        return Objects.equals(maTheLoai, theLoai.maTheLoai);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maTheLoai);
    }
}
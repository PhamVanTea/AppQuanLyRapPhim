package Entity;

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
    	if (maGhe != null && !maGhe.trim().isEmpty()) {
    		this.maGhe = maGhe;
    	} else {
    		throw new IllegalArgumentException("Mã ghế không để trống!");
    	}
        
    }

    public PhongChieu getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(PhongChieu phongChieu) {
    	if (phongChieu != null) {
    		this.phongChieu = phongChieu;
    	} else {
    		throw new IllegalArgumentException("Ghế ngồi phải thuộc về một phòng chiếu hợp lệ.");
    	}
        
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
    	if (hang != null && hang.matches("[A-Z]")) {
    		this.hang = hang;
    	} else {
    		throw new IllegalArgumentException("Hàng ghế là một ký tự in hoa từ A-Z.");    	}
        
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
    	if (soGhe > 0) {
    		this.soGhe = soGhe;
    	} else {
    		 throw new IllegalArgumentException("Số ghế phải lớn hơn 0.");
    	}
        
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
    	if (trangThai != null && (trangThai.equalsIgnoreCase("Trống") ||
    			 trangThai.equalsIgnoreCase("Đã đặt") ||
    			 trangThai.equalsIgnoreCase("Bảo trì"))) {
    		this.trangThai = trangThai;
    	} else {
    		 throw new IllegalArgumentException("Trạng thái là: 'Trống' | 'Đã đặt' | 'Bảo trì'.");
    	}
        
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
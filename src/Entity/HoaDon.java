package Entity;

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
    	if (maHoaDon != null && !maHoaDon.trim().isEmpty()) {
    		this.maHoaDon = maHoaDon;
    	} else {
            throw new IllegalArgumentException("Mã hóa đơn không được để trống!");
        }
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
    	if (nhanVien != null) {
    		 this.nhanVien = nhanVien;
    	} else {
            throw new IllegalArgumentException("Hóa đơn phải có nhân viên lập.");
        }
       
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
    	if (khachHang != null) {
    		this.khachHang = khachHang;
    	} else {
            throw new IllegalArgumentException("Hóa đơn phải có khách hàng.");
        }
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
    	if (tongTien >= 0) {
    		this.tongTien = tongTien;
    	} else {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn hoặc bằng 0.");
        }
        
    }

    public String getNgayLapHoaDon() {
        return ngayLapHoaDon;
    }

    public void setNgayLapHoaDon(String ngayLapHoaDon) { //nên sửa lại LocalDateTim
        if (ngayLapHoaDon != null) {
        	this.ngayLapHoaDon = ngayLapHoaDon;
        } else {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được null.");
        }
    }
//    public LocalDateTime getNgayLap() { return ngayLapHoaDon; }
//    public void setNgayLapHoaDon(LocalDateTime ngayLap) {
//        if (ngayLapHoaDon != null && !ngayLapHoaDon.after(new Date())) {
//            this.ngayLapHoaDon = ngayLapHoaDon;
//        } else {
//            throw new IllegalArgumentException("Ngày lập hóa đơn không hợp lệ và k lập ở tương lai.");
//        }
//    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
    	if (trangThai != null && !trangThai.trim().isEmpty()) {
            this.trangThai = trangThai;
        } else {
            throw new IllegalArgumentException("Trạng thái hóa đơn không được để trống.");
        }
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
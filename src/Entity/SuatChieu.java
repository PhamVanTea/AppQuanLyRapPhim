package Entity;

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
    	if (maSuatChieu != null && !maSuatChieu.trim().isEmpty()) {
            this.maSuatChieu = maSuatChieu;
        } else {
            throw new IllegalArgumentException("Mã suất chiếu không được để trống.");
        }
    }

    public Phim getPhim() {
        return phim;
    }

    public void setPhim(Phim phim) {
    	 if (phim != null) {
             this.phim = phim;
         } else {
             throw new IllegalArgumentException("Phim không được null.");
         }
    }

    public PhongChieu getPhongChieu() {
        return phongChieu;
    }

    public void setPhongChieu(PhongChieu phongChieu) {
    	if (phongChieu != null) {
            this.phongChieu = phongChieu;
        } else {
            throw new IllegalArgumentException("Phòng chiếu không được null.");
        }
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
    	if (gia >= 0) {
            this.gia = gia;
        } else {
            throw new IllegalArgumentException("Giá phải là số dương.");
        }
    }

    public String getThoiGianBD() {
        return thoiGianBD;
    }

    public void setThoiGianBD(String thoiGianBD) { //nen sua LocalDateTime?
    	if (thoiGianBD != null) {
            this.thoiGianBD = thoiGianBD;
        } else {
            throw new IllegalArgumentException("Thời gian bắt đầu không được null.");
        }
    }

    public String getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }
//    public LocalDateTime getThoiGianKetThuc() { return thoiGianKetThuc; }

//    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
//        if (thoiGianKetThuc != null && thoiGianKetThuc.isAfter(thoiGianBD)) {
//            this.thoiGianKetThuc = thoiGianKetThuc;
//        } else {
//            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu.");
//        }
//    }
    public void setThoiGianKetThuc(String thoiGianKetThuc) {
    	if (thoiGianKetThuc != null) {
            this.thoiGianKetThuc = thoiGianKetThuc;
        } else {
            throw new IllegalArgumentException("Thời gian kết thúc không để trống.");
        }
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
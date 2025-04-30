package Entity;

public class Ve {
    private String maVe;
    private HoaDon hoaDon; 
    private SuatChieu suatChieu;  
    private float giaVe;
    private GheNgoi gheNgoi;  

    public Ve(String maVe, HoaDon hoaDon, SuatChieu suatChieu, float giaVe, GheNgoi gheNgoi) {
        this.maVe = maVe;
        this.hoaDon = hoaDon;
        this.suatChieu = suatChieu;
        this.giaVe = giaVe;
        this.gheNgoi = gheNgoi;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
    	if (maVe != null && !maVe.trim().isEmpty()) {
            this.maVe = maVe;
        } else {
            throw new IllegalArgumentException("Mã vé không được để trống.");
        }
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
    	if (hoaDon != null) {
            this.hoaDon = hoaDon;
        } else {
            throw new IllegalArgumentException("Vé phải thuộc về hóa đơn hợp lệ.");
        }
    }

    public SuatChieu getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
    	if (suatChieu != null) {
            this.suatChieu = suatChieu;
        } else {
            throw new IllegalArgumentException("Vé phải có suất chiếu hợp lệ.");
        }
    }

    public float getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(float giaVe) {
    	if (giaVe >= 0) {
            this.giaVe = giaVe;
        } else {
            throw new IllegalArgumentException("Giá vé không hợp lệ.");
        }
    }

    public GheNgoi getGheNgoi() {
        return gheNgoi;
    }

    public void setGheNgoi(GheNgoi gheNgoi) {
    	 if (gheNgoi != null) {
             this.gheNgoi = gheNgoi;
         } else {
             throw new IllegalArgumentException("Vé phải có ghế ngồi.");
         }
    }
}

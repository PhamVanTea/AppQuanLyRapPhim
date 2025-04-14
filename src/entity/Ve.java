package entity;

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
        this.maVe = maVe;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public SuatChieu getSuatChieu() {
        return suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
        this.suatChieu = suatChieu;
    }

    public float getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(float giaVe) {
        this.giaVe = giaVe;
    }

    public GheNgoi getGheNgoi() {
        return gheNgoi;
    }

    public void setGheNgoi(GheNgoi gheNgoi) {
        this.gheNgoi = gheNgoi;
    }
}

package Entity;

import java.math.BigDecimal; // Use BigDecimal for currency
import java.time.LocalDate; // Use LocalDate for Date
import java.util.Objects;

public class ThongKe {
    private String maThongKe;
    private BigDecimal tongDoanhThu;
    private int tongSoVe;
    private int tongSoKhachHang;
    private int tongSoNhanVien;
    private int tongSoPhim;
    private int tongSoPhongChieu;
    private LocalDate tuNgay;
    private LocalDate denNgay;

    // Constructors
    public ThongKe() {
        // Default constructor
        this.tongDoanhThu = BigDecimal.ZERO; // Initialize BigDecimal
    }

    public ThongKe(String maThongKe, BigDecimal tongDoanhThu, int tongSoVe, int tongSoKhachHang,
                   int tongSoNhanVien, int tongSoPhim, int tongSoPhongChieu,
                   LocalDate tuNgay, LocalDate denNgay) {
        this.maThongKe = maThongKe;
        this.tongDoanhThu = (tongDoanhThu != null) ? tongDoanhThu : BigDecimal.ZERO; // Handle null
        this.tongSoVe = tongSoVe;
        this.tongSoKhachHang = tongSoKhachHang;
        this.tongSoNhanVien = tongSoNhanVien;
        this.tongSoPhim = tongSoPhim;
        this.tongSoPhongChieu = tongSoPhongChieu;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
    }

    // Getters and Setters
    public String getMaThongKe() {
        return maThongKe;
    }

    public void setMaThongKe(String maThongKe) {
        this.maThongKe = maThongKe;
    }

    public BigDecimal getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(BigDecimal tongDoanhThu) {
        this.tongDoanhThu = (tongDoanhThu != null) ? tongDoanhThu : BigDecimal.ZERO;
    }

    public int getTongSoVe() {
        return tongSoVe;
    }

    public void setTongSoVe(int tongSoVe) {
        this.tongSoVe = tongSoVe;
    }

    public int getTongSoKhachHang() {
        return tongSoKhachHang;
    }

    public void setTongSoKhachHang(int tongSoKhachHang) {
        this.tongSoKhachHang = tongSoKhachHang;
    }

    public int getTongSoNhanVien() {
        return tongSoNhanVien;
    }

    public void setTongSoNhanVien(int tongSoNhanVien) {
        this.tongSoNhanVien = tongSoNhanVien;
    }

    public int getTongSoPhim() {
        return tongSoPhim;
    }

    public void setTongSoPhim(int tongSoPhim) {
        this.tongSoPhim = tongSoPhim;
    }

    public int getTongSoPhongChieu() {
        return tongSoPhongChieu;
    }

    public void setTongSoPhongChieu(int tongSoPhongChieu) {
        this.tongSoPhongChieu = tongSoPhongChieu;
    }

    public LocalDate getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(LocalDate tuNgay) {
        this.tuNgay = tuNgay;
    }

    public LocalDate getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(LocalDate denNgay) {
        this.denNgay = denNgay;
    }

    // toString, equals, hashCode
    @Override
    public String toString() {
        return "ThongKe{" +
               "maThongKe='" + maThongKe + '\'' +
               ", tongDoanhThu=" + tongDoanhThu +
               ", tongSoVe=" + tongSoVe +
               ", tongSoKhachHang=" + tongSoKhachHang +
               ", tongSoNhanVien=" + tongSoNhanVien +
               ", tongSoPhim=" + tongSoPhim +
               ", tongSoPhongChieu=" + tongSoPhongChieu +
               ", tuNgay=" + tuNgay +
               ", denNgay=" + denNgay +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThongKe thongKe = (ThongKe) o;
        return Objects.equals(maThongKe, thongKe.maThongKe); // Equality based on primary key
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThongKe);
    }
}
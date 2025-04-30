package DAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.ThongKe;

public class ThongKeDAO {

    private ThongKeDAO() {}

    // Phương thức tạo mã thống kê duy nhất
    public static String generateMaThongKe() {
        return "TK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Phương thức tạo mới một thống kê
    public static boolean create(ThongKe thongKe) {
        String sql = "INSERT INTO ThongKe (MaThongKe, TongDoanhThu, TongSoVe, TongSoKhachHang, " +
                     "TongSoNhanVien, TongSoPhim, TongSoPhongChieu, TuNgay, DenNgay) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, thongKe.getMaThongKe());
            stmt.setBigDecimal(2, thongKe.getTongDoanhThu());
            stmt.setInt(3, thongKe.getTongSoVe());
            stmt.setInt(4, thongKe.getTongSoKhachHang());
            stmt.setInt(5, thongKe.getTongSoNhanVien());
            stmt.setInt(6, thongKe.getTongSoPhim());
            stmt.setInt(7, thongKe.getTongSoPhongChieu());
            stmt.setDate(8, Date.valueOf(thongKe.getTuNgay()));
            stmt.setDate(9, Date.valueOf(thongKe.getDenNgay()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Kiểm tra vi phạm khóa chính
            if (e.getErrorCode() == 2627 || (e.getSQLState() != null && e.getSQLState().startsWith("23"))) {
                System.err.println("Tạo thống kê thất bại: Mã thống kê '" + thongKe.getMaThongKe() + "' đã tồn tại.");
            } else {
                System.err.println("Tạo thống kê thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
            }
            return false;
        }
    }

    // Phương thức ánh xạ từ ResultSet sang đối tượng ThongKe
    private static ThongKe mapResultSetToThongKe(ResultSet rs) throws SQLException {
        ThongKe tk = new ThongKe();
        tk.setMaThongKe(rs.getString("MaThongKe"));
        tk.setTongDoanhThu(rs.getBigDecimal("TongDoanhThu"));
        tk.setTongSoVe(rs.getInt("TongSoVe"));
        tk.setTongSoKhachHang(rs.getInt("TongSoKhachHang"));
        tk.setTongSoNhanVien(rs.getInt("TongSoNhanVien"));
        tk.setTongSoPhim(rs.getInt("TongSoPhim"));
        tk.setTongSoPhongChieu(rs.getInt("TongSoPhongChieu"));
        tk.setTuNgay(rs.getDate("TuNgay").toLocalDate());
        tk.setDenNgay(rs.getDate("DenNgay").toLocalDate());
        return tk;
    }

    // Phương thức đọc tất cả thống kê
    public static List<ThongKe> readAll() {
        List<ThongKe> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongKe ORDER BY TuNgay DESC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToThongKe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Đọc thống kê thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm thống kê theo mã
    public static ThongKe findById(String maThongKe) {
        String sql = "SELECT * FROM ThongKe WHERE MaThongKe = ?";
        ThongKe tk = null;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThongKe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tk = mapResultSetToThongKe(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm thống kê theo ID thất bại cho MaThongKe=" + maThongKe + ": [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return tk;
    }

    // Phương thức tìm thống kê theo khoảng thời gian
    public static List<ThongKe> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<ThongKe> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongKe WHERE TuNgay <= ? AND DenNgay >= ? ORDER BY TuNgay DESC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(endDate));
            stmt.setDate(2, Date.valueOf(startDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToThongKe(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm thống kê theo khoảng thời gian thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Phương thức cập nhật thông tin thống kê
    public static boolean update(ThongKe thongKe) {
        String sql = "UPDATE ThongKe SET TongDoanhThu = ?, TongSoVe = ?, TongSoKhachHang = ?, " +
                     "TongSoNhanVien = ?, TongSoPhim = ?, TongSoPhongChieu = ?, TuNgay = ?, DenNgay = ? " +
                     "WHERE MaThongKe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, thongKe.getTongDoanhThu());
            stmt.setInt(2, thongKe.getTongSoVe());
            stmt.setInt(3, thongKe.getTongSoKhachHang());
            stmt.setInt(4, thongKe.getTongSoNhanVien());
            stmt.setInt(5, thongKe.getTongSoPhim());
            stmt.setInt(6, thongKe.getTongSoPhongChieu());
            stmt.setDate(7, Date.valueOf(thongKe.getTuNgay()));
            stmt.setDate(8, Date.valueOf(thongKe.getDenNgay()));
            stmt.setString(9, thongKe.getMaThongKe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Cập nhật thống kê thất bại cho MaThongKe=" + thongKe.getMaThongKe() + ": [" + e.getErrorCode() + "] " + e.getMessage());
            return false;
        }
    }

    // Phương thức xóa thống kê
    public static boolean delete(String maThongKe) {
        String sql = "DELETE FROM ThongKe WHERE MaThongKe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThongKe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Xóa thống kê thất bại cho MaThongKe=" + maThongKe + ": [" + e.getErrorCode() + "] " + e.getMessage());
            return false;
        }
    }

    // Phương thức tính tổng doanh thu
    public static BigDecimal calculateTongDoanhThu(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT SUM(tongTien) FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) BETWEEN ? AND ?";
        BigDecimal total = BigDecimal.ZERO;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal sum = rs.getBigDecimal(1);
                    if (sum != null) {
                        total = sum;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng doanh thu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return total;
    }

    // Phương thức tính tổng số vé
    public static int calculateTongSoVe(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(v.maVe) FROM Ve v JOIN HoaDon h ON v.maHoaDon = h.maHoaDon WHERE CAST(h.ngayLapHoaDon AS DATE) BETWEEN ? AND ?";
        int count = 0;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng số vé thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    // Phương thức tính tổng số khách hàng
    public static int calculateTongSoKhachHang(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT maKhachHang) FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) BETWEEN ? AND ?";
        int count = 0;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng số khách hàng thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    // Phương thức tính tổng số nhân viên
    public static int calculateTongSoNhanVien(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT maNhanVien) FROM HoaDon WHERE CAST(ngayLapHoaDon AS DATE) BETWEEN ? AND ?";
        int count = 0;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng số nhân viên thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    // Phương thức tính tổng số phim
    public static int calculateTongSoPhim(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT maPhim) FROM SuatChieu WHERE CAST(thoiGianBD AS DATE) BETWEEN ? AND ?";
        int count = 0;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng số phim thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    // Phương thức tính tổng số phòng chiếu
    public static int calculateTongSoPhongChieu(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(DISTINCT maPhong) FROM SuatChieu WHERE CAST(thoiGianBD AS DATE) BETWEEN ? AND ?";
        int count = 0;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tính tổng số phòng chiếu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }
}

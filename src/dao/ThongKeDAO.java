package dao;

import connect.DbConnect;
import entity.ThongKe;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ThongKeDAO {

    private ThongKeDAO() {}

    public static String generateMaThongKe() {
        return "TK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

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
             // Check for SQL Server Primary Key violation (Error Number 2627)
             if (e.getErrorCode() == 2627 || (e.getSQLState() != null && e.getSQLState().startsWith("23"))) {
                 System.err.println("ThongKeDAO Create failed: Mã thống kê '" + thongKe.getMaThongKe() + "' đã tồn tại.");
             } else {
                System.err.println("ThongKeDAO Create failed: [" + e.getErrorCode() + "] " + e.getMessage());
             }
             return false;
        }
    }

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
            System.err.println("ThongKeDAO ReadAll failed: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

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
            System.err.println("ThongKeDAO FindById failed for MaThongKe=" + maThongKe + ": [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return tk;
    }

     public static List<ThongKe> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<ThongKe> list = new ArrayList<>();
        // Query finds records where the stats period overlaps with the query period
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
            System.err.println("ThongKeDAO findByDateRange failed: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
     }

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
            System.err.println("ThongKeDAO Update failed for MaThongKe=" + thongKe.getMaThongKe() + ": [" + e.getErrorCode() + "] " + e.getMessage());
            return false;
        }
    }

    public static boolean delete(String maThongKe) {
        String sql = "DELETE FROM ThongKe WHERE MaThongKe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maThongKe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ThongKeDAO Delete failed for MaThongKe=" + maThongKe + ": [" + e.getErrorCode() + "] " + e.getMessage());
            return false;
        }
    }

     public static BigDecimal calculateTongDoanhThu(LocalDate startDate, LocalDate endDate) {
         // Use CAST AS DATE for SQL Server compatibility
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
             System.err.println("Error calculating TongDoanhThu: [" + e.getErrorCode() + "] " + e.getMessage());
         }
         return total;
     }

     public static int calculateTongSoVe(LocalDate startDate, LocalDate endDate) {
         // Use CAST AS DATE for SQL Server compatibility
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
             System.err.println("Error calculating TongSoVe: [" + e.getErrorCode() + "] " + e.getMessage());
         }
         return count;
     }

    public static int calculateTongSoKhachHang(LocalDate startDate, LocalDate endDate) {
        // Use CAST AS DATE for SQL Server compatibility
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
            System.err.println("Error calculating TongSoKhachHang: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    public static int calculateTongSoNhanVien(LocalDate startDate, LocalDate endDate) {
        // Use CAST AS DATE for SQL Server compatibility
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
            System.err.println("Error calculating TongSoNhanVien: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    public static int calculateTongSoPhim(LocalDate startDate, LocalDate endDate) {
        // Use CAST AS DATE for SQL Server compatibility
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
            System.err.println("Error calculating TongSoPhim: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }

    public static int calculateTongSoPhongChieu(LocalDate startDate, LocalDate endDate) {
        // Use CAST AS DATE for SQL Server compatibility
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
            System.err.println("Error calculating TongSoPhongChieu: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return count;
    }
}
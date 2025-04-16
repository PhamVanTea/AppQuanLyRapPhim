package dao;

import connect.DbConnect;
import entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KhachHangDAO {

    // Phương thức tạo mã khách hàng ngẫu nhiên
    public static String generateMaKhachHang() {
        return "KH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Phương thức thêm mới khách hàng
    public static boolean create(KhachHang khachHang) {
        String sql = "INSERT INTO KhachHang (maKhachHang, tenKhachHang, SDT, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, khachHang.getMaKhachHang());
            stmt.setString(2, khachHang.getTenKhachHang());
            stmt.setString(3, khachHang.getSDT());
            stmt.setString(4, khachHang.getEmail());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
            return false;
        }
    }

    // Phương thức đọc danh sách tất cả khách hàng
    public static List<KhachHang> readAll() {
        String sql = "SELECT * FROM KhachHang";
        List<KhachHang> danhSach = new ArrayList<>();

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSach.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đọc danh sách khách hàng: " + e.getMessage());
        }
        return danhSach;
    }

    // Phương thức cập nhật thông tin khách hàng
    public static boolean update(KhachHang khachHang) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, SDT = ?, email = ? WHERE maKhachHang = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, khachHang.getTenKhachHang());
            stmt.setString(2, khachHang.getSDT());
            stmt.setString(3, khachHang.getEmail());
            stmt.setString(4, khachHang.getMaKhachHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
            return false;
        }
    }

    // Phương thức xóa khách hàng
    public static boolean delete(String maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
            return false;
        }
    }

    // Phương thức tìm kiếm khách hàng theo tên hoặc số điện thoại
    public static List<KhachHang> searchByName(String keyword) {
        String sql = "SELECT * FROM KhachHang WHERE tenKhachHang LIKE ? OR SDT LIKE ?";
        List<KhachHang> ketQua = new ArrayList<>();

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ketQua.add(mapKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
        }
        return ketQua;
    }

    // Phương thức ánh xạ kết quả ResultSet thành đối tượng KhachHang
    private static KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
                rs.getString("maKhachHang"),
                rs.getString("tenKhachHang"),
                rs.getString("SDT"),
                rs.getString("email")
        );
    }

    // Phương thức tìm khách hàng theo mã khách hàng
    public static KhachHang findById(String maKhachHang) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        KhachHang kh = null;
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kh = mapKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo ID: " + e.getMessage());
        }
        return kh;
    }
}

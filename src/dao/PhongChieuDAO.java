package dao;

import connect.DbConnect;
import entity.PhongChieu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhongChieuDAO {
    public static String generateMaPhongChieu() {
        return "PC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    public static boolean create(PhongChieu phongChieu) {
        String sql = "INSERT INTO PhongChieu (maPhong, tenPhong, soGhe) VALUES (?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phongChieu.getMaPhong());
            stmt.setString(2, phongChieu.getTenPhong());
            stmt.setInt(3, phongChieu.getSoGhe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
             if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                 System.err.println("PhongChieuDAO Create failed: Phòng chiếu với mã '" + phongChieu.getMaPhong() + "' đã tồn tại.");
             } else {
                System.err.println("PhongChieuDAO Create failed: " + e.getMessage());
             }
        }
        return false;
    }

    public static List<PhongChieu> readAll() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT maPhong, tenPhong, soGhe FROM PhongChieu ORDER BY tenPhong";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String tenPhong = rs.getString("tenPhong");
                int soGhe = rs.getInt("soGhe");
                list.add(new PhongChieu(maPhong, tenPhong, soGhe));
            }
        } catch (SQLException e) {
            System.err.println("PhongChieuDAO Read failed: " + e.getMessage());
        }
        return list;
    }

    public static PhongChieu findById(String maPhong) {
        PhongChieu phongChieu = null;
        String sql = "SELECT maPhong, tenPhong, soGhe FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tenPhong = rs.getString("tenPhong");
                    int soGhe = rs.getInt("soGhe");
                    phongChieu = new PhongChieu(maPhong, tenPhong, soGhe);
                }
            }
        } catch (SQLException e) {
            System.err.println("PhongChieuDAO Find by ID failed for maPhong=" + maPhong + ": " + e.getMessage());
        }
        return phongChieu;
    }

    public static boolean update(PhongChieu phongChieu) {
        String sql = "UPDATE PhongChieu SET tenPhong = ?, soGhe = ? WHERE maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phongChieu.getTenPhong());
            stmt.setInt(2, phongChieu.getSoGhe());
            stmt.setString(3, phongChieu.getMaPhong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PhongChieuDAO Update failed for maPhong=" + phongChieu.getMaPhong() + ": " + e.getMessage());
        }
        return false;
    }

    public static boolean delete(String maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
             if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 System.err.println("PhongChieuDAO Delete failed for maPhong=" + maPhong + ": Không thể xóa phòng chiếu vì đang được tham chiếu (ví dụ: trong SuatChieu hoặc GheNgoi).");
             } else {
                System.err.println("PhongChieuDAO Delete failed for maPhong=" + maPhong + ": " + e.getMessage());
             }
        }
        return false;
    }

    public static List<PhongChieu> searchByName(String keyword) {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT maPhong, tenPhong, soGhe FROM PhongChieu WHERE LOWER(tenPhong) LIKE LOWER(?) ORDER BY tenPhong";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maPhong = rs.getString("maPhong");
                    String tenPhong = rs.getString("tenPhong");
                    int soGhe = rs.getInt("soGhe");
                    list.add(new PhongChieu(maPhong, tenPhong, soGhe));
                }
            }
        } catch (SQLException e) {
            System.err.println("PhongChieuDAO Search failed for keyword='" + keyword + "': " + e.getMessage());
        }
        return list;
    }
}
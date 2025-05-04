package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Connect.DbConnect;
import Entity.GheNgoi;
import Entity.PhongChieu;

public class GheNgoiDAO {

    // Phương thức tạo mới một Ghế Ngồi
    public static boolean tao(GheNgoi gheNgoi) {
    	//Tạo câu lệnh SQL INSERT với 5 tham số (maGhe, maPhong, hang, soGhe, trangThai)
        String sql = "INSERT INTO GheNgoi (maGhe, maPhong, hang, soGhe, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gheNgoi.getMaGhe());
            stmt.setString(2, gheNgoi.getPhongChieu().getMaPhong());
            stmt.setString(3, gheNgoi.getHang());
            stmt.setInt(4, gheNgoi.getSoGhe());
            stmt.setString(5, gheNgoi.getTrangThai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
        	// ("duplicate entry", "unique constraint") vì đây là thông báo lỗi được trả về từ hệ quản trị cơ sở dữ liệu.
        	//Duplicate entry (Mục nhập trùng lặp)      
        	//Unique constraint (Ràng buộc duy nhất)
             if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                 System.err.println("Tạo Ghế Ngồi thất bại: Ghế với mã '" + gheNgoi.getMaGhe() + "' đã tồn tại.");
             } else if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 System.err.println("Tạo Ghế Ngồi thất bại: Mã phòng '" + gheNgoi.getPhongChieu().getMaPhong() + "' không tồn tại trong bảng Phòng Chiếu.");
             } else {
                 System.err.println("Tạo Ghế Ngồi thất bại: " + e.getMessage());
             }
        }
        return false;
    }

    // Phương thức đọc tất cả Ghế Ngồi
    public static List<GheNgoi> readAll() {
        List<GheNgoi> list = new ArrayList<>();
        String sql = "SELECT g.maGhe, g.hang, g.soGhe AS ghe_soGhe, g.trangThai, " +
                     "p.maPhong, p.tenPhong, p.soGhe AS phong_soGhe " +
                     "FROM GheNgoi g " +
                     "JOIN PhongChieu p ON g.maPhong = p.maPhong";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maGhe = rs.getString("maGhe");
                PhongChieu phongChieu = new PhongChieu(
                    rs.getString("maPhong"),
                    rs.getString("tenPhong"),
                    rs.getInt("phong_soGhe")
                );
                String hang = rs.getString("hang");
                int soGhe = rs.getInt("ghe_soGhe");
                String trangThai = rs.getString("trangThai");
                list.add(new GheNgoi(maGhe, phongChieu, hang, soGhe, trangThai));
            }
        } catch (SQLException e) {
            System.err.println("Đọc tất cả Ghế Ngồi thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức cập nhật Ghế Ngồi
    public static boolean capNhat(GheNgoi gheNgoi) {
        String sql = "UPDATE GheNgoi SET maPhong = ?, hang = ?, soGhe = ?, trangThai = ? WHERE maGhe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gheNgoi.getPhongChieu().getMaPhong());
            stmt.setString(2, gheNgoi.getHang());
            stmt.setInt(3, gheNgoi.getSoGhe());
            stmt.setString(4, gheNgoi.getTrangThai());
            stmt.setString(5, gheNgoi.getMaGhe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
             if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 System.err.println("Cập nhật Ghế Ngồi thất bại cho maGhe=" + gheNgoi.getMaGhe() + ": Mã phòng '" + gheNgoi.getPhongChieu().getMaPhong() + "' không tồn tại trong bảng Phòng Chiếu.");
             } else {
                System.err.println("Cập nhật Ghế Ngồi thất bại cho maGhe=" + gheNgoi.getMaGhe() + ": " + e.getMessage());
             }
        }
        return false;
    }

    // Phương thức xóa Ghế Ngồi
    public static boolean xoa(String maGhe) {
        String sql = "DELETE FROM GheNgoi WHERE maGhe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maGhe);

            int rowsAffected = stmt.executeUpdate();	//thực thi câu lệnh delete
            return rowsAffected > 0;
        } catch (SQLException e) {
             if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 System.err.println("Xóa Ghế Ngồi thất bại cho maGhe=" + maGhe + ": Không thể xóa ghế vì đang được tham chiếu (ví dụ: trong bảng Vé).");
             } else {
                System.err.println("Xóa Ghế Ngồi thất bại cho maGhe=" + maGhe + ": " + e.getMessage());
             }
        }
        return false;
    }

    // Phương thức tìm kiếm Ghế Ngồi theo mã Phòng Chiếu
    public static List<GheNgoi> searchByPhongChieu(String maPhongSearch) {
        List<GheNgoi> list = new ArrayList<>();
        String sql = "SELECT g.maGhe, g.hang, g.soGhe AS ghe_soGhe, g.trangThai, " +
                     "p.maPhong, p.tenPhong, p.soGhe AS phong_soGhe " +
                     "FROM GheNgoi g " +
                     "JOIN PhongChieu p ON g.maPhong = p.maPhong " +
                     "WHERE g.maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhongSearch);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maGhe = rs.getString("maGhe");
                    PhongChieu phongChieu = new PhongChieu(
                        rs.getString("maPhong"),
                        rs.getString("tenPhong"),
                        rs.getInt("phong_soGhe")
                    );
                    String hang = rs.getString("hang");
                    int soGhe = rs.getInt("ghe_soGhe");
                    String trangThai = rs.getString("trangThai");
                    list.add(new GheNgoi(maGhe, phongChieu, hang, soGhe, trangThai));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm kiếm Ghế Ngồi thất bại cho maPhong=" + maPhongSearch + ": " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm Ghế Ngồi theo ID
    public static GheNgoi findById(String maGhe) {
        GheNgoi gheNgoi = null;
        String sql = "SELECT g.maGhe, g.hang, g.soGhe AS ghe_soGhe, g.trangThai, " +
                     "p.maPhong, p.tenPhong, p.soGhe AS phong_soGhe " +
                     "FROM GheNgoi g " +
                     "JOIN PhongChieu p ON g.maPhong = p.maPhong " +
                     "WHERE g.maGhe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maGhe);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PhongChieu phongChieu = new PhongChieu(
                        rs.getString("maPhong"),
                        rs.getString("tenPhong"),
                        rs.getInt("phong_soGhe")
                    );
                    String hang = rs.getString("hang");
                    int soGhe = rs.getInt("ghe_soGhe");
                    String trangThai = rs.getString("trangThai");
                    gheNgoi = new GheNgoi(maGhe, phongChieu, hang, soGhe, trangThai);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm Ghế Ngồi theo ID thất bại cho maGhe=" + maGhe + ": " + e.getMessage());
        }
        return gheNgoi;
    }
}

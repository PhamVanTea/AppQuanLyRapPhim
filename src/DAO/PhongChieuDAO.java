package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.PhongChieu;

public class PhongChieuDAO {

    // Phương thức tạo mã phòng chiếu duy nhất
    public static String generateMaPhongChieu() {
        return "PC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Phương thức tạo mới một phòng chiếu
    public static boolean tao(PhongChieu phongChieu) {
        String sql = "INSERT INTO PhongChieu (maPhong, tenPhong, soGhe) VALUES (?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phongChieu.getMaPhong());	//Gán gtri cho tham số ?,?,? bằng phongChieu.get...
            stmt.setString(2, phongChieu.getTenPhong());
            stmt.setInt(3, phongChieu.getSoGhe());

            return stmt.executeUpdate() > 0;	//số dòng ảnh hường >0 - true
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) { //gặp lỗi duplicate entry | unique constraint từ sql -> THông báo lỗi đã tồn tại 
                System.err.println("Tạo phòng chiếu thất bại: Phòng chiếu với mã '" + phongChieu.getMaPhong() + "' đã tồn tại.");
            } else {
                System.err.println("Tạo phòng chiếu thất bại: " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức đọc tất cả phòng chiếu
    public static List<PhongChieu> readAll() {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT maPhong, tenPhong, soGhe FROM PhongChieu ORDER BY tenPhong"; //sx theo tên phòng
        try (Connection conn = DbConnect.getConnection();	//mở kết nối csdl
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPhong = rs.getString("maPhong");
                String tenPhong = rs.getString("tenPhong");
                int soGhe = rs.getInt("soGhe");
                list.add(new PhongChieu(maPhong, tenPhong, soGhe)); //tạo đối tượng phongChieu với dl lấy được thêm vào list
            }
        } catch (SQLException e) {
            System.err.println("Đọc phòng chiếu thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm phòng chiếu theo mã
    public static PhongChieu timTheoMaPhong(String maPhong) {
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
            System.err.println("Tìm phòng chiếu theo ID thất bại cho maPhong=" + maPhong + ": " + e.getMessage());
        }
        return phongChieu;
    }

    // Phương thức cập nhật thông tin phòng chiếu
    public static boolean capNhat(PhongChieu phongChieu) {
        String sql = "UPDATE PhongChieu SET tenPhong = ?, soGhe = ? WHERE maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phongChieu.getTenPhong());
            stmt.setInt(2, phongChieu.getSoGhe());
            stmt.setString(3, phongChieu.getMaPhong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Cập nhật phòng chiếu thất bại cho maPhong=" + phongChieu.getMaPhong() + ": " + e.getMessage());
        }
        return false;
    }

    // Phương thức xóa phòng chiếu
    public static boolean xoa(String maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);	//gán gtri maPhong vào ?

            int rowsAffected = stmt.executeUpdate();	//thực thi câu lệnh delete
            return rowsAffected > 0;
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("foreign key constraint")) { //ktra nếu có tb lỗi về khóa ngoại
                System.err.println("Xóa phòng chiếu thất bại cho maPhong=" + maPhong + ": Không thể xóa phòng chiếu vì đang được tham chiếu (ví dụ: trong Suất Chiếu hoặc Ghế Ngồi).");
            } else {
                System.err.println("Xóa phòng chiếu thất bại cho maPhong=" + maPhong + ": " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức tìm kiếm phòng chiếu theo tên
    public static List<PhongChieu> timTheoTenPhongChieu(String keyword) {
        List<PhongChieu> list = new ArrayList<>();
        String sql = "SELECT maPhong, tenPhong, soGhe FROM PhongChieu WHERE LOWER(tenPhong) LIKE LOWER(?) ORDER BY tenPhong";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maPhong = rs.getString("maPhong");
                    String tenPhong = rs.getString("tenPhong");
                    int soGhe = rs.getInt("soGhe");
                    list.add(new PhongChieu(maPhong, tenPhong, soGhe));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm kiếm phòng chiếu thất bại cho từ khóa='" + keyword + "': " + e.getMessage());
        }
        return list;
    }
}

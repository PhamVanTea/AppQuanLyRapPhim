package dao;

import connect.DbConnect;
import entity.TheLoai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TheLoaiDAO {

    // Phương thức tạo mã thể loại duy nhất
    public static String generateMaTheLoai() {
        return "TL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(); //UUID tạo mã ngẫu nhiên, lấy 8 kí tự đầu và tiền tố "TL"
    }
    
    // Phương thức tạo mới một thể loại
    public static boolean create(TheLoai theLoai) {
        String sql = "INSERT INTO TheLoai (maTheLoai, tenTheLoai, moTa) VALUES (?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //PreparedStatement chạy lệnh SQL Insert

            stmt.setString(1, theLoai.getMaTheLoai());
            stmt.setString(2, theLoai.getTenTheLoai());
            stmt.setString(3, theLoai.getMoTa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Tạo thể loại thất bại: " + e.getMessage());
        }
        return false;
    }

    // Phương thức đọc tất cả thể loại
    public static List<TheLoai> readAll() {
        List<TheLoai> list = new ArrayList<>();
        String sql = "SELECT * FROM TheLoai";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maTheLoai = rs.getString("maTheLoai");
                String tenTheLoai = rs.getString("tenTheLoai");
                String moTa = rs.getString("moTa");
                list.add(new TheLoai(maTheLoai, tenTheLoai, moTa));
            }
        } catch (SQLException e) {
            System.err.println("Đọc thể loại thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức cập nhật thông tin thể loại
    public static boolean update(TheLoai theLoai) {
        String sql = "UPDATE TheLoai SET tenTheLoai = ?, moTa = ? WHERE maTheLoai = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, theLoai.getTenTheLoai());
            stmt.setString(2, theLoai.getMoTa());
            stmt.setString(3, theLoai.getMaTheLoai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Cập nhật thể loại thất bại: " + e.getMessage());
        }
        return false;
    }

    // Phương thức xóa thể loại
    public static boolean delete(String maTheLoai) {
        String sql = "DELETE FROM TheLoai WHERE maTheLoai = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maTheLoai);

            return stmt.executeUpdate() > 0; //thực thi câu lệnh delete, số dòng >0 -> xóa thành công -> true
        } catch (SQLException e) {
            System.err.println("Xóa thể loại thất bại: " + e.getMessage());
        }
        return false;
    }

    // Phương thức tìm kiếm thể loại theo tên
    public static List<TheLoai> searchByName(String keyword) {
        List<TheLoai> list = new ArrayList<>();	//tạo ds rỗng để lưu kq
        String sql = "SELECT * FROM TheLoai WHERE tenTheLoai LIKE ?"; // " ? " tham số truyền vào keyword
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%"); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maTheLoai = rs.getString("maTheLoai"); //lấy dl dạng String theo tên cột
                String tenTheLoai = rs.getString("tenTheLoai");
                String moTa = rs.getString("moTa");
                list.add(new TheLoai(maTheLoai, tenTheLoai, moTa));
            }
        } catch (SQLException e) {
            System.err.println("Tìm kiếm thể loại thất bại: " + e.getMessage());
        }
        return list;
    }
}

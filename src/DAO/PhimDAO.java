package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.Phim;
import Entity.TheLoai;

public class PhimDAO {

    // Phương thức tạo mã phim duy nhất
	//tạo ra một giá trị UUID ngẫu nhiên. Giá trị này là một chuỗi ký tự rất dài, gồm 32 ký tự hex (các chữ số từ 0 đến 9 và các chữ cái a đến f),
    //phân cách bằng dấu gạch ngang.
	
    public static String generateMaPhim() {
        return "P" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Phương thức tạo mới một phim
    public static boolean tao(Phim phim) {
        String sql = "INSERT INTO Phim (maPhim, tenPhim, daoDien, dienVien, maTheLoai, thoiLuong, xepHang, moTa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getMaPhim());
            stmt.setString(2, phim.getTenPhim());
            stmt.setString(3, phim.getDaoDien());
            stmt.setString(4, phim.getDienVien());

            if (phim.getTheLoai() == null) {
                System.err.println("Tạo phim thất bại: Đối tượng Thể Loại bị null cho Phim: " + phim.getMaPhim());
                return false;
            }
            stmt.setString(5, phim.getTheLoai().getMaTheLoai());
            stmt.setInt(6, phim.getThoiLuong());
            stmt.setString(7, phim.getXepHang());
            stmt.setString(8, phim.getMoTa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PhimDAO Tạo thất bại: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("PhimDAO Tạo thất bại do NullPointerException (có thể do Thể Loại bị null): " + e.getMessage());
        }
        return false;
    }

    // Phương thức đọc tất cả phim
    

    public static List<Phim> readAll() {
        List<Phim> list = new ArrayList<>();

        String sql = "SELECT p.maPhim, p.tenPhim, p.daoDien, p.dienVien, p.maTheLoai, p.thoiLuong, p.xepHang, p.moTa, " +
                     "t.tenTheLoai, t.moTa AS moTaTheLoai " + 
                     "FROM Phim p JOIN TheLoai t ON p.maTheLoai = t.maTheLoai";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maPhim = rs.getString("maPhim");
                String tenPhim = rs.getString("tenPhim");
                String daoDien = rs.getString("daoDien");
                String dienVien = rs.getString("dienVien");
                String maTheLoai = rs.getString("maTheLoai");
                String tenTheLoai = rs.getString("tenTheLoai");
                String moTaTheLoai = rs.getString("moTaTheLoai"); 
            
                TheLoai theLoai = new TheLoai(maTheLoai, tenTheLoai, moTaTheLoai);
                int thoiLuong = rs.getInt("thoiLuong");
                String xepHang = rs.getString("xepHang");
                String moTaPhim = rs.getString("moTa"); 
                list.add(new Phim(maPhim, tenPhim, daoDien, dienVien, theLoai, thoiLuong, xepHang, moTaPhim));
            }
        } catch (SQLException e) {
            System.err.println("PhimDAO Đọc thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm phim theo mã
    public static Phim findById(String maPhim) {
        Phim phim = null;
        String sql = "SELECT p.maPhim, p.tenPhim, p.daoDien, p.dienVien, p.maTheLoai, p.thoiLuong, p.xepHang, p.moTa, " +
                     "t.tenTheLoai, t.moTa AS moTaTheLoai " + 
                     "FROM Phim p JOIN TheLoai t ON p.maTheLoai = t.maTheLoai WHERE p.maPhim = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhim);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tenPhim = rs.getString("tenPhim");
                    String daoDien = rs.getString("daoDien");
                    String dienVien = rs.getString("dienVien");
                    String maTheLoai = rs.getString("maTheLoai");
                    String tenTheLoai = rs.getString("tenTheLoai");
                    String moTaTheLoai = rs.getString("moTaTheLoai");
                    TheLoai theLoai = new TheLoai(maTheLoai, tenTheLoai, moTaTheLoai);
                    int thoiLuong = rs.getInt("thoiLuong");
                    String xepHang = rs.getString("xepHang");
                    String moTaPhim = rs.getString("moTa");
                    phim = new Phim(maPhim, tenPhim, daoDien, dienVien, theLoai, thoiLuong, xepHang, moTaPhim);
                }
            }
        } catch (SQLException e) {
            System.err.println("PhimDAO Tìm theo ID thất bại cho maPhim=" + maPhim + ": " + e.getMessage());
        }
        return phim;
    }

    // Phương thức cập nhật thông tin phim
    public static boolean capNhat(Phim phim) {
        String sql = "UPDATE Phim SET tenPhim = ?, daoDien = ?, dienVien = ?, maTheLoai = ?, thoiLuong = ?, xepHang = ?, moTa = ? WHERE maPhim = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getTenPhim());
            stmt.setString(2, phim.getDaoDien());
            stmt.setString(3, phim.getDienVien());
            if (phim.getTheLoai() == null) {
                System.err.println("Cập nhật thất bại: Đối tượng Thể Loại bị null cho Phim: " + phim.getMaPhim());
                return false;
            }
            stmt.setString(4, phim.getTheLoai().getMaTheLoai());
            stmt.setInt(5, phim.getThoiLuong());
            stmt.setString(6, phim.getXepHang());
            stmt.setString(7, phim.getMoTa());
            stmt.setString(8, phim.getMaPhim());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PhimDAO Cập nhật thất bại cho maPhim=" + phim.getMaPhim() + ": " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("PhimDAO Cập nhật thất bại do NullPointerException (có thể do Thể Loại bị null) cho maPhim=" + phim.getMaPhim() + ": " + e.getMessage());
        }
        return false;
    }

    // Phương thức xóa phim
    public static boolean xoa(String maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhim);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState()) || e.getMessage().toLowerCase().contains("foreign key constraint")) {
                System.err.println("PhimDAO Xóa thất bại cho maPhim=" + maPhim + ": Không thể xóa phim vì đang được tham chiếu bởi các bản ghi khác (ví dụ: Lịch Chiếu).");
            } else {
                System.err.println("PhimDAO Xóa thất bại cho maPhim=" + maPhim + ": " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức tìm kiếm phim theo tiêu đề
    
    //stmt.setString(1, "%" + keyword + "%") thiết lập tham số đầu vào của câu lệnh SQL, với keyword được bọc trong dấu % để tìm kiếm chuỗi con trong tên phim.

    //Dấu % đại diện cho bất kỳ chuỗi nào trước hoặc sau keyword, vì vậy nếu từ khóa là "horror", nó sẽ tìm tất cả các bộ phim có tên chứa từ "horror" 
    //bất kỳ đâu trong chuỗi.
    public static List<Phim> searchByTitle(String keyword) {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT p.maPhim, p.tenPhim, p.daoDien, p.dienVien, p.maTheLoai, p.thoiLuong, p.xepHang, p.moTa, " +
                     "t.tenTheLoai, t.moTa AS moTaTheLoai " +
                     "FROM Phim p JOIN TheLoai t ON p.maTheLoai = t.maTheLoai WHERE LOWER(p.tenPhim) LIKE LOWER(?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maPhim = rs.getString("maPhim");
                    String tenPhim = rs.getString("tenPhim");
                    String daoDien = rs.getString("daoDien");
                    String dienVien = rs.getString("dienVien");
                    String maTheLoai = rs.getString("maTheLoai");
                    String tenTheLoai = rs.getString("tenTheLoai");
                    String moTaTheLoai = rs.getString("moTaTheLoai");
                    TheLoai theLoai = new TheLoai(maTheLoai, tenTheLoai, moTaTheLoai);
                    int thoiLuong = rs.getInt("thoiLuong");
                    String xepHang = rs.getString("xepHang");
                    String moTaPhim = rs.getString("moTa");
                    list.add(new Phim(maPhim, tenPhim, daoDien, dienVien, theLoai, thoiLuong, xepHang, moTaPhim));
                }
            }
        } catch (SQLException e) {
            System.err.println("PhimDAO Tìm kiếm thất bại cho từ khóa='" + keyword + "': " + e.getMessage());
        }
        return list;
    }
}

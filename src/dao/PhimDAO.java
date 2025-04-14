package dao;

import connect.DbConnect;
import entity.Phim;
import entity.TheLoai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhimDAO {
    public static String generateMaPhim() {
        return "P" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static boolean create(Phim phim) {
        String sql = "INSERT INTO Phim (maPhim, tenPhim, daoDien, dienVien, maTheLoai, thoiLuong, xepHang, moTa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getMaPhim());
            stmt.setString(2, phim.getTenPhim());
            stmt.setString(3, phim.getDaoDien());
            stmt.setString(4, phim.getDienVien());
   
            if (phim.getTheLoai() == null) {
                 System.err.println("Create failed: TheLoai object is null for Phim: " + phim.getMaPhim());
                 return false;
            }
            stmt.setString(5, phim.getTheLoai().getMaTheLoai());
            stmt.setInt(6, phim.getThoiLuong());
            stmt.setString(7, phim.getXepHang());
            stmt.setString(8, phim.getMoTa());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PhimDAO Create failed: " + e.getMessage());
           
        } catch (NullPointerException e) {
             System.err.println("PhimDAO Create failed due to NullPointerException (likely null TheLoai): " + e.getMessage());
           
        }
        return false;
    }


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
            System.err.println("PhimDAO Read failed: " + e.getMessage());
        }
        return list;
    }

    public static Phim findById(String maPhim) {
        Phim phim = null;
        String sql = "SELECT p.maPhim, p.tenPhim, p.daoDien, p.dienVien, p.maTheLoai, p.thoiLuong, p.xepHang, p.moTa, " +
                     "t.tenTheLoai, t.moTa AS moTaTheLoai " + // Alias moTa for TheLoai
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
            System.err.println("PhimDAO Find by ID failed for maPhim=" + maPhim + ": " + e.getMessage());
        }
        return phim;
    }


    public static boolean update(Phim phim) {
        String sql = "UPDATE Phim SET tenPhim = ?, daoDien = ?, dienVien = ?, maTheLoai = ?, thoiLuong = ?, xepHang = ?, moTa = ? WHERE maPhim = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getTenPhim());
            stmt.setString(2, phim.getDaoDien());
            stmt.setString(3, phim.getDienVien());
            if (phim.getTheLoai() == null) {
                 System.err.println("Update failed: TheLoai object is null for Phim: " + phim.getMaPhim());
                 return false;
            }
            stmt.setString(4, phim.getTheLoai().getMaTheLoai());
            stmt.setInt(5, phim.getThoiLuong());
            stmt.setString(6, phim.getXepHang());
            stmt.setString(7, phim.getMoTa());
            stmt.setString(8, phim.getMaPhim());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PhimDAO Update failed for maPhim=" + phim.getMaPhim() + ": " + e.getMessage());
        } catch (NullPointerException e) {
             System.err.println("PhimDAO Update failed due to NullPointerException (likely null TheLoai) for maPhim=" + phim.getMaPhim() + ": " + e.getMessage());
        }
        return false;
    }

    public static boolean delete(String maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhim);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
             if ("23000".equals(e.getSQLState()) || e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 System.err.println("PhimDAO Delete failed for maPhim=" + maPhim + ": Cannot delete Phim because it is referenced by other records (e.g., LichChieu).");
             } else {
                System.err.println("PhimDAO Delete failed for maPhim=" + maPhim + ": " + e.getMessage());
             }
        }
        return false;
    }

    public static List<Phim> searchByTitle(String keyword) {
        List<Phim> list = new ArrayList<>();
        String sql = "SELECT p.maPhim, p.tenPhim, p.daoDien, p.dienVien, p.maTheLoai, p.thoiLuong, p.xepHang, p.moTa, " +
                     "t.tenTheLoai, t.moTa AS moTaTheLoai " +
                     "FROM Phim p JOIN TheLoai t ON p.maTheLoai = t.maTheLoai WHERE LOWER(p.tenPhim) LIKE LOWER(?)"; // Use LOWER for case-insensitive search
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
            System.err.println("PhimDAO Search failed for keyword='" + keyword + "': " + e.getMessage());
         
        }
        return list;
    }
}
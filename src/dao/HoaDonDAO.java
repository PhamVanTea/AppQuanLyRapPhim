package dao;
import connect.DbConnect;
import entity.HoaDon;
import entity.NhanVien;
import entity.KhachHang;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoaDonDAO {
	   
    public static boolean create(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, tongTien, ngayLapHoaDon, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoaDon.getMaHoaDon());
            stmt.setString(2, hoaDon.getNhanVien().getMaNhanVien());
            stmt.setString(3, hoaDon.getKhachHang().getMaKhachHang());
            stmt.setFloat(4, hoaDon.getTongTien());
            stmt.setString(5, hoaDon.getNgayLapHoaDon());
            stmt.setString(6, hoaDon.getTrangThai());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Create failed: " + e.getMessage());
        }
        return false;
    }

    public static List<HoaDon> readAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT h.maHoaDon, h.maNhanVien, h.maKhachHang, h.tongTien, h.ngayLapHoaDon, h.trangThai, " +
                "n.maNhanVien, n.tenNhanVien, " +
                "k.maKhachHang, k.tenKhachHang " +
                "FROM HoaDon h " +
                "JOIN NhanVien n ON h.maNhanVien = n.maNhanVien " +
                "JOIN KhachHang k ON h.maKhachHang = k.maKhachHang";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"), rs.getString("tenNhanVien"));
                KhachHang khachHang = new KhachHang(rs.getString("maKhachHang"), rs.getString("tenKhachHang"));
                float tongTien = rs.getFloat("tongTien");
                String ngayLapHoaDon = rs.getString("ngayLapHoaDon");
                String trangThai = rs.getString("trangThai");
                list.add(new HoaDon(maHoaDon, nhanVien, khachHang, tongTien, ngayLapHoaDon, trangThai));
            }
        } catch (SQLException e) {
            System.err.println("Read failed: " + e.getMessage());
        }
        return list;
    }

    public static boolean update(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET maNhanVien = ?, maKhachHang = ?, tongTien = ?, ngayLapHoaDon = ?, trangThai = ? WHERE maHoaDon = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hoaDon.getNhanVien().getMaNhanVien());
            stmt.setString(2, hoaDon.getKhachHang().getMaKhachHang());
            stmt.setFloat(3, hoaDon.getTongTien());
            stmt.setString(4, hoaDon.getNgayLapHoaDon());
            stmt.setString(5, hoaDon.getTrangThai());
            stmt.setString(6, hoaDon.getMaHoaDon());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
        return false;
    }

    public static boolean delete(String maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }
        return false;
    }

    public static List<HoaDon> searchByKhachHang(String maKhachHang) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT h.maHoaDon, h.maNhanVien, h.maKhachHang, h.tongTien, h.ngayLapHoaDon, h.trangThai, " +
                "n.maNhanVien, n.tenNhanVien, " +
                "k.maKhachHang, k.tenKhachHang " +
                "FROM HoaDon h " +
                "JOIN NhanVien n ON h.maNhanVien = n.maNhanVien " +
                "JOIN KhachHang k ON h.maKhachHang = k.maKhachHang " +
                "WHERE h.maKhachHang = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maKhachHang);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maHoaDon = rs.getString("maHoaDon");
                NhanVien nhanVien = new NhanVien(rs.getString("maNhanVien"), rs.getString("tenNhanVien"));
                KhachHang khachHang = new KhachHang(rs.getString("maKhachHang"), rs.getString("tenKhachHang"));
                float tongTien = rs.getFloat("tongTien");
                String ngayLapHoaDon = rs.getString("ngayLapHoaDon");
                String trangThai = rs.getString("trangThai");
                list.add(new HoaDon(maHoaDon, nhanVien, khachHang, tongTien, ngayLapHoaDon, trangThai));
            }
        } catch (SQLException e) {
            System.err.println("Search failed: " + e.getMessage());
        }
        return list;
    }
}

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.NhanVien;

public class NhanVienDAO {

    // Phương thức tạo mã nhân viên duy nhất
    public static String generateMaNhanVien() {
        return "NV" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Phương thức đăng nhập cho nhân viên
    public static NhanVien login(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM NhanVien WHERE tenDangNhap = ? AND matKhau = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String maNhanVien = rs.getString("maNhanVien");
                String tenNhanVien = rs.getString("tenNhanVien");
                String chucVu = rs.getString("chucVu");
                String fetchedTenDangNhap = rs.getString("tenDangNhap");
                String fetchedMatKhau = rs.getString("matKhau");
                return new NhanVien(maNhanVien, tenNhanVien, chucVu, fetchedTenDangNhap, fetchedMatKhau);
            }
        } catch (SQLException e) {
            System.err.println("Đăng nhập thất bại: " + e.getMessage());
        }
        return null;
    }

    // Phương thức tạo mới một nhân viên
    public static boolean create(NhanVien nv) {
        String sql = "INSERT INTO NhanVien (maNhanVien, tenNhanVien, chucVu, tenDangNhap, matKhau) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nv.getMaNhanVien());
            stmt.setString(2, nv.getTenNhanVien());
            stmt.setString(3, nv.getChucVu());
            stmt.setString(4, nv.getTenDangNhap());
            stmt.setString(5, nv.getMatKhau());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                System.err.println("Tạo nhân viên thất bại: Nhân viên với mã '" + nv.getMaNhanVien() + "' hoặc tên đăng nhập '" + nv.getTenDangNhap() + "' đã tồn tại.");
            } else {
                System.err.println("Tạo nhân viên thất bại: " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức đọc tất cả nhân viên
    public static List<NhanVien> readAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maNhanVien = rs.getString("maNhanVien");
                String tenNhanVien = rs.getString("tenNhanVien");
                String chucVu = rs.getString("chucVu");
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                list.add(new NhanVien(maNhanVien, tenNhanVien, chucVu, tenDangNhap, matKhau));
            }
        } catch (SQLException e) {
            System.err.println("Đọc nhân viên thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức cập nhật thông tin nhân viên
    public static boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNhanVien = ?, chucVu = ?, tenDangNhap = ?, matKhau = ? WHERE maNhanVien = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nv.getTenNhanVien());
            stmt.setString(2, nv.getChucVu());
            stmt.setString(3, nv.getTenDangNhap());
            stmt.setString(4, nv.getMatKhau());
            stmt.setString(5, nv.getMaNhanVien());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                System.err.println("Cập nhật nhân viên thất bại cho maNhanVien=" + nv.getMaNhanVien() + ": Tên đăng nhập '" + nv.getTenDangNhap() + "' đã tồn tại cho nhân viên khác.");
            } else {
                System.err.println("Cập nhật nhân viên thất bại cho maNhanVien=" + nv.getMaNhanVien() + ": " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức xóa nhân viên
    public static boolean delete(String maNhanVien) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maNhanVien);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                System.err.println("Xóa nhân viên thất bại cho maNhanVien=" + maNhanVien + ": Không thể xóa nhân viên vì đang được tham chiếu (ví dụ: trong bảng Hóa Đơn).");
            } else {
                System.err.println("Xóa nhân viên thất bại cho maNhanVien=" + maNhanVien + ": " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức tìm kiếm nhân viên theo tên
    public static List<NhanVien> searchByName(String keyword) {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE tenNhanVien LIKE ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maNhanVien = rs.getString("maNhanVien");
                String tenNhanVien = rs.getString("tenNhanVien");
                String chucVu = rs.getString("chucVu");
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                list.add(new NhanVien(maNhanVien, tenNhanVien, chucVu, tenDangNhap, matKhau));
            }
        } catch (SQLException e) {
            System.err.println("Tìm kiếm nhân viên thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm nhân viên theo ID
    public static NhanVien findById(String maNhanVien) {
        NhanVien nv = null;
        String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String tenNhanVien = rs.getString("tenNhanVien");
                String chucVu = rs.getString("chucVu");
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                nv = new NhanVien(maNhanVien, tenNhanVien, chucVu, tenDangNhap, matKhau);
            }
        } catch (SQLException e) {
            System.err.println("Tìm nhân viên theo ID thất bại cho maNhanVien=" + maNhanVien + ": " + e.getMessage());
        }
        return nv;
    }

    
}

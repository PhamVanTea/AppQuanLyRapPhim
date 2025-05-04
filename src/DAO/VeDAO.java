package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.GheNgoi;
import Entity.HoaDon;
import Entity.Phim;
import Entity.PhongChieu;
import Entity.SuatChieu;
import Entity.Ve;

public class VeDAO {

    // Phương thức tạo mã vé duy nhất
	//Dùng UUID để tạo chuỗi ngẫu nhiên, rồi cắt 8 ký tự đầu và thêm tiền tố "VE" → ví dụ: VEA1B2C3D4.
    public static String generateMaVe() {
        return "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // Phương thức tạo mới một vé
    public static boolean tao(Ve ve) {
        String sql = "INSERT INTO Ve (maVe, maHoaDon, maSuatChieu, giaVe, maGhe) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ve.getMaVe());
            stmt.setString(2, ve.getHoaDon().getMaHoaDon());
            stmt.setString(3, ve.getSuatChieu().getMaSuatChieu());
            stmt.setFloat(4, ve.getGiaVe());
            stmt.setString(5, ve.getGheNgoi().getMaGhe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
        	//contains("duplicate entry") kiểm tra nếu thông điệp lỗi có chứa chuỗi "duplicate entry",
        	//điều này thường xảy ra khi cố gắng chèn một giá trị trùng lặp vào trường có ràng buộc duy nhất (unique)
        	//unique constraint ràng buộc duy nhất
        	//vd: ERROR 1062 (23000): Duplicate entry 'VE12345' for key 'PRIMARY'
        	//có nghĩa là giá trị VE12345 đã tồn tại trong cột maVe vì nó bị ràng buộc là khóa chính hoặc có ràng buộc duy nhất
            if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                System.err.println("Tạo vé thất bại: Vé với mã '" + ve.getMaVe() + "' hoặc ghế '" + ve.getGheNgoi().getMaGhe() + "' cho suất chiếu '" + ve.getSuatChieu().getMaSuatChieu() + "' đã tồn tại.");
            } else if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                String failedKey = "";
                if (e.getMessage().contains("maHoaDon")) failedKey = "Hóa Đơn '" + ve.getHoaDon().getMaHoaDon() + "'";
                else if (e.getMessage().contains("maSuatChieu")) failedKey = "Suất Chiếu '" + ve.getSuatChieu().getMaSuatChieu() + "'";
                else if (e.getMessage().contains("maGhe")) failedKey = "Ghế Ngồi '" + ve.getGheNgoi().getMaGhe() + "'";
                System.err.println("Tạo vé thất bại: Không tìm thấy " + failedKey + ".");
            } else {
                System.err.println("Tạo vé thất bại: " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức ResultSet đối tượng Ve
    //Dùng mapResultSetToVe() để convert dữ liệu từng 	
    private static Ve mapResultSetToVe(ResultSet rs) throws SQLException {
        String maVe = rs.getString("maVe");
        float giaVe = rs.getFloat("giaVe");

        HoaDon hoaDon = new HoaDon(rs.getString("maHoaDon"));

        Phim phim = new Phim();
        phim.setMaPhim(rs.getString("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));
        PhongChieu phongChieu = new PhongChieu(
            rs.getString("maPhong"),
            rs.getString("tenPhong"),
            rs.getInt("phongSoGhe") 
        );

        SuatChieu suatChieu = new SuatChieu(
            rs.getString("maSuatChieu"),
            phim,
            phongChieu,
            rs.getFloat("suatChieuGia"),
            rs.getString("thoiGianBD"),
            rs.getString("thoiGianKetThuc")
        );

        GheNgoi gheNgoi = new GheNgoi(
            rs.getString("maGhe"),
            phongChieu, 
            rs.getString("hang"),
            rs.getInt("gheSoGhe"),
            rs.getString("trangThai")
        );

        return new Ve(maVe, hoaDon, suatChieu, giaVe, gheNgoi);
    }

    // Phương thức đọc tất cả vé
    public static List<Ve> readAll() {
        List<Ve> list = new ArrayList<>();
        String sql = "SELECT v.maVe, v.giaVe, " +
                     "h.maHoaDon, " +
                     "sc.maSuatChieu, sc.gia AS suatChieuGia, sc.thoiGianBD, sc.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe AS phongSoGhe, " +
                     "gn.maGhe, gn.hang, gn.soGhe AS gheSoGhe, gn.trangThai " +
                     "FROM Ve v " +
                     "JOIN HoaDon h ON v.maHoaDon = h.maHoaDon " +
                     "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "JOIN GheNgoi gn ON v.maGhe = gn.maGhe";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToVe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Đọc vé thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm vé theo mã
    public static Ve findById(String maVe) {
        Ve ve = null;
        String sql = "SELECT v.maVe, v.giaVe, " +
                     "h.maHoaDon, " +
                     "sc.maSuatChieu, sc.gia AS suatChieuGia, sc.thoiGianBD, sc.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe AS phongSoGhe, " +
                     "gn.maGhe, gn.hang, gn.soGhe AS gheSoGhe, gn.trangThai " +
                     "FROM Ve v " +
                     "JOIN HoaDon h ON v.maHoaDon = h.maHoaDon " +
                     "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "JOIN GheNgoi gn ON v.maGhe = gn.maGhe " +
                     "WHERE v.maVe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maVe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ve = mapResultSetToVe(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm vé theo ID thất bại cho maVe=" + maVe + ": " + e.getMessage());
        }
        return ve;
    }

    // Phương thức tìm vé theo mã hóa đơn
    public static List<Ve> findByHoaDonId(String maHoaDon) {
        List<Ve> list = new ArrayList<>();
        String sql = "SELECT v.maVe, v.giaVe, " +
                     "h.maHoaDon, " + 
                     "sc.maSuatChieu, sc.gia AS suatChieuGia, sc.thoiGianBD, sc.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe AS phongSoGhe, " +
                     "gn.maGhe, gn.hang, gn.soGhe AS gheSoGhe, gn.trangThai " +
                     "FROM Ve v " +
                     "JOIN HoaDon h ON v.maHoaDon = h.maHoaDon " + 
                     "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "JOIN GheNgoi gn ON v.maGhe = gn.maGhe " +
                     "WHERE v.maHoaDon = ?";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maHoaDon); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToVe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Tìm vé theo mã hóa đơn thất bại cho maHoaDon=" + maHoaDon + ": " + e.getMessage());
        }
        return list;
    }

    // Phương thức cập nhật thông tin vé
    public static boolean capNhat(Ve ve) {
        String sql = "UPDATE Ve SET maHoaDon = ?, maSuatChieu = ?, giaVe = ?, maGhe = ? WHERE maVe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ve.getHoaDon().getMaHoaDon());
            stmt.setString(2, ve.getSuatChieu().getMaSuatChieu());
            stmt.setFloat(3, ve.getGiaVe());
            stmt.setString(4, ve.getGheNgoi().getMaGhe());
            stmt.setString(5, ve.getMaVe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                String failedKey = "";
                if (e.getMessage().contains("maHoaDon")) failedKey = "Hóa Đơn '" + ve.getHoaDon().getMaHoaDon() + "'";
                else if (e.getMessage().contains("maSuatChieu")) failedKey = "Suất Chiếu '" + ve.getSuatChieu().getMaSuatChieu() + "'";
                else if (e.getMessage().contains("maGhe")) failedKey = "Ghế Ngồi '" + ve.getGheNgoi().getMaGhe() + "'";
                System.err.println("Cập nhật vé thất bại cho " + ve.getMaVe() + ": Không tìm thấy " + failedKey + ".");
            } else {
                System.err.println("Cập nhật vé thất bại cho maVe=" + ve.getMaVe() + ": " + e.getMessage());
            }
        }
        return false;
    }

    // Phương thức xóa vé
    public static boolean xoa(String maVe) {
        String sql = "DELETE FROM Ve WHERE maVe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maVe);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Xóa vé thất bại cho maVe=" + maVe + ": " + e.getMessage());
        }
        return false;
    }

    // Phương thức tìm vé theo khoảng giá
    public static List<Ve> searchByPrice(float minPrice, float maxPrice) {
        List<Ve> list = new ArrayList<>();
        String sql = "SELECT v.maVe, v.giaVe, " +
                     "h.maHoaDon, " +
                     "sc.maSuatChieu, sc.gia AS suatChieuGia, sc.thoiGianBD, sc.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe AS phongSoGhe, " +
                     "gn.maGhe, gn.hang, gn.soGhe AS gheSoGhe, gn.trangThai " +
                     "FROM Ve v " +
                     "JOIN HoaDon h ON v.maHoaDon = h.maHoaDon " +
                     "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "JOIN GheNgoi gn ON v.maGhe = gn.maGhe " +
                     "WHERE v.giaVe BETWEEN ? AND ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setFloat(1, minPrice);
            stmt.setFloat(2, maxPrice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToVe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Tìm vé theo khoảng giá thất bại: " + e.getMessage());
        }
        return list;
    }

    // Phương thức tìm vé theo mã suất chiếu
    public static List<Ve> findBySuatChieuId(String maSuatChieu) {
        List<Ve> list = new ArrayList<>();
        String sql = "SELECT v.maVe, v.giaVe, " +
                     "h.maHoaDon, " +
                     "sc.maSuatChieu, sc.gia AS suatChieuGia, sc.thoiGianBD, sc.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe AS phongSoGhe, " +
                     "gn.maGhe, gn.hang, gn.soGhe AS gheSoGhe, gn.trangThai " +
                     "FROM Ve v " +
                     "JOIN HoaDon h ON v.maHoaDon = h.maHoaDon " +
                     "JOIN SuatChieu sc ON v.maSuatChieu = sc.maSuatChieu " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "JOIN GheNgoi gn ON v.maGhe = gn.maGhe " +
                     "WHERE v.maSuatChieu = ?"; 
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSuatChieu); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToVe(rs));
            }
        } catch (SQLException e) {
            System.err.println("Tìm vé theo mã suất chiếu thất bại cho maSuatChieu=" + maSuatChieu + ": " + e.getMessage());
        }
        return list;
    }

    // Phương thức kiểm tra ghế đã được đặt cho suất chiếu chưa
    public static boolean isSeatBookedForShowtime(String maSuatChieu, String maGhe) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maSuatChieu = ? AND maGhe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSuatChieu);
            stmt.setString(2, maGhe);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Kiểm tra ghế đã được đặt cho suất chiếu thất bại: " + e.getMessage());
        }
        return false; 
    }
}

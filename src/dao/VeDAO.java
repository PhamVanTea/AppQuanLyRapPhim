package dao;

import connect.DbConnect;
import entity.Ve;
import entity.HoaDon;
import entity.SuatChieu;
import entity.GheNgoi;
import entity.Phim;
import entity.PhongChieu;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VeDAO {
	
    public static String generateMaVe() {
        return "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static boolean create(Ve ve) {
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
             if (e.getMessage().toLowerCase().contains("duplicate entry") || e.getMessage().toLowerCase().contains("unique constraint")) {
                 System.err.println("VeDAO Create failed: Vé với mã '" + ve.getMaVe() + "' hoặc ghế '" + ve.getGheNgoi().getMaGhe() + "' cho suất chiếu '" + ve.getSuatChieu().getMaSuatChieu() + "' đã tồn tại."); // More specific message
             } else if (e.getMessage().toLowerCase().contains("foreign key constraint")) {
                 String failedKey = "";
                 if (e.getMessage().contains("maHoaDon")) failedKey = "Hóa Đơn '" + ve.getHoaDon().getMaHoaDon() + "'";
                 else if (e.getMessage().contains("maSuatChieu")) failedKey = "Suất Chiếu '" + ve.getSuatChieu().getMaSuatChieu() + "'";
                 else if (e.getMessage().contains("maGhe")) failedKey = "Ghế Ngồi '" + ve.getGheNgoi().getMaGhe() + "'";
                 System.err.println("VeDAO Create failed: Không tìm thấy " + failedKey + ".");
             } else {
                System.err.println("VeDAO Create failed: " + e.getMessage());
             }
        }
        return false;
    }

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
            System.err.println("VeDAO ReadAll failed: " + e.getMessage());
        }
        return list;
    }

     public static Ve findById(String maVe) {
        Ve ve = null;
        // SQL remains the same
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
             System.err.println("VeDAO FindById failed for maVe=" + maVe + ": " + e.getMessage());
        }
        return ve;
     }
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
             System.err.println("VeDAO FindByHoaDonId failed for maHoaDon=" + maHoaDon + ": " + e.getMessage());
         }
         return list;
     }

    public static boolean update(Ve ve) {
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
                 System.err.println("VeDAO Update failed for " + ve.getMaVe() + ": Không tìm thấy " + failedKey + ".");
            } else {
                System.err.println("VeDAO Update failed for maVe=" + ve.getMaVe() + ": " + e.getMessage());
            }
        }
        return false;
    }

    public static boolean delete(String maVe) {
        String sql = "DELETE FROM Ve WHERE maVe = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maVe);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("VeDAO Delete failed for maVe=" + maVe + ": " + e.getMessage());
        }
        return false;
    }

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
            System.err.println("VeDAO SearchByPrice failed: " + e.getMessage());
        }
        return list;
    }

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
            System.err.println("VeDAO FindBySuatChieuId failed for maSuatChieu=" + maSuatChieu + ": " + e.getMessage());
        }
        return list;
    }

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
            System.err.println("VeDAO isSeatBookedForShowtime check failed: " + e.getMessage());
        }
        return false; 
    }

}
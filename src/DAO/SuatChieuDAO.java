package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Connect.DbConnect;
import Entity.Phim;
import Entity.PhongChieu;
import Entity.SuatChieu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SuatChieuDAO {

    // Định nghĩa định dạng ngày giờ thống nhất khi làm việc với kiểu DATETIME
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Tạo mã suất chiếu ngẫu nhiên
    public static String generateMaSuatChieu() {
        return "SC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Tạo mới một suất chiếu
    
    public static boolean tao(SuatChieu suatChieu) {
        String sql = "INSERT INTO SuatChieu (maSuatChieu, maPhim, maPhong, gia, thoiGianBD, thoiGianKetThuc) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, suatChieu.getMaSuatChieu());
            stmt.setString(2, suatChieu.getPhim().getMaPhim());
            stmt.setString(3, suatChieu.getPhongChieu().getMaPhong());
            stmt.setFloat(4, suatChieu.getGia());
            // Chuyển đổi chuỗi thành Timestamp để lưu vào SQL Server
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(suatChieu.getThoiGianBD(), DATETIME_FORMATTER)));
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.parse(suatChieu.getThoiGianKetThuc(), DATETIME_FORMATTER)));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Xử lý lỗi liên quan đến ràng buộc khóa chính hoặc khóa ngoại
            if (e.getErrorCode() == 2627 || (e.getSQLState() != null && e.getSQLState().startsWith("23"))) {
                System.err.println("Tạo suất chiếu thất bại: Mã '" + suatChieu.getMaSuatChieu() + "' đã tồn tại.");
            } else if (e.getErrorCode() == 547) {
                String failedKey = "Không xác định";
                if (e.getMessage().contains("FK__SuatChieu__maPhi__")) failedKey = "Phim '" + suatChieu.getPhim().getMaPhim() + "'";
                else if (e.getMessage().contains("FK__SuatChieu__maPho__")) failedKey = "Phòng Chiếu '" + suatChieu.getPhongChieu().getMaPhong() + "'";
                System.err.println("Tạo suất chiếu thất bại: Không tìm thấy khóa ngoại " + failedKey + ".");
            } else {
                System.err.println("Tạo suất chiếu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("Tạo suất chiếu thất bại do lỗi xử lý ngày giờ: " + ex.getMessage());
        }
        return false;
    }

    // Chuyển đổi ResultSet thành đối tượng SuatChieu
    private static SuatChieu mapResultSetToSuatChieu(ResultSet rs) throws SQLException {
        String maSuatChieu = rs.getString("maSuatChieu");
        Phim phim = new Phim();
        phim.setMaPhim(rs.getString("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));

        PhongChieu phongChieu = new PhongChieu(
            rs.getString("maPhong"),
            rs.getString("tenPhong"),
            rs.getInt("soGhe")
        );
        float gia = rs.getFloat("gia");
        // Chuyển Timestamp thành chuỗi
        String thoiGianBD = rs.getTimestamp("thoiGianBD").toLocalDateTime().format(DATETIME_FORMATTER);
        String thoiGianKetThuc = rs.getTimestamp("thoiGianKetThuc").toLocalDateTime().format(DATETIME_FORMATTER);

        return new SuatChieu(maSuatChieu, phim, phongChieu, gia, thoiGianBD, thoiGianKetThuc);
    }

    // Đọc tất cả các suất chiếu
    public static List<SuatChieu> readAll() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Đọc danh sách suất chiếu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Tìm suất chiếu theo mã
    public static SuatChieu findById(String maSuatChieu) {
        SuatChieu suatChieu = null;
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE s.maSuatChieu = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSuatChieu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    suatChieu = mapResultSetToSuatChieu(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm suất chiếu thất bại với mã " + maSuatChieu + ": [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return suatChieu;
    }

    // Cập nhật thông tin suất chiếu
    public static boolean capNhat(SuatChieu suatChieu) {
        String sql = "UPDATE SuatChieu SET maPhim = ?, maPhong = ?, gia = ?, thoiGianBD = ?, thoiGianKetThuc = ? WHERE maSuatChieu = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, suatChieu.getPhim().getMaPhim());
            stmt.setString(2, suatChieu.getPhongChieu().getMaPhong());
            stmt.setFloat(3, suatChieu.getGia());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.parse(suatChieu.getThoiGianBD(), DATETIME_FORMATTER)));
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.parse(suatChieu.getThoiGianKetThuc(), DATETIME_FORMATTER)));
            stmt.setString(6, suatChieu.getMaSuatChieu());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                String failedKey = "Không xác định";
                if (e.getMessage().contains("FK__SuatChieu__maPhi__")) failedKey = "Phim '" + suatChieu.getPhim().getMaPhim() + "'";
                else if (e.getMessage().contains("FK__SuatChieu__maPho__")) failedKey = "Phòng Chiếu '" + suatChieu.getPhongChieu().getMaPhong() + "'";
                System.err.println("Cập nhật thất bại: Không tìm thấy khóa ngoại " + failedKey + ".");
            } else {
                System.err.println("Cập nhật suất chiếu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("Cập nhật suất chiếu thất bại do lỗi xử lý ngày giờ: " + ex.getMessage());
        }
        return false;
    }

    // Xóa suất chiếu theo mã
    public static boolean xoa(String maSuatChieu) {
        String sql = "DELETE FROM SuatChieu WHERE maSuatChieu = ?";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maSuatChieu);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Trường hợp suất chiếu đang được tham chiếu ở bảng khác (ví dụ: bảng Vé)
            if (e.getErrorCode() == 547) {
                System.err.println("Xóa thất bại: Suất chiếu đang được sử dụng trong bảng khác.");
            } else {
                System.err.println("Xóa suất chiếu thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
            }
        }
        return false;
    }

    // Tìm suất chiếu theo mã phim
    public static List<SuatChieu> searchByPhim(String maPhim) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE s.maPhim = ? " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhim);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSuatChieu(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Tìm suất chiếu theo phim thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Tìm suất chiếu theo mã phòng
    public static List<SuatChieu> searchByPhongChieu(String maPhong) {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE s.maPhong = ? " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maPhong);
             try (ResultSet rs = stmt.executeQuery()) {
                 while (rs.next()) {
                    list.add(mapResultSetToSuatChieu(rs));
                }
             }
        } catch (SQLException e) {
            System.err.println("Tìm suất chiếu theo phòng thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Kiểm tra có trùng lịch chiếu không
    //maSuatChieuToExclude là một tham số trong phương thức hasOverlap, có chức năng loại trừ một suất chiếu cụ thể khỏi quá trình kiểm tra trùng lịch.
    
    //proposedEndTimeStr là tham số trong phương thức hasOverlap đại diện cho thời gian kết thúc của suất chiếu dự định (thời gian kết thúc mà bạn muốn
    //kiểm tra xem nó có bị trùng với bất kỳ suất chiếu nào khác trong cùng một phòng chiếu hay không).
    
    
    public static boolean hasOverlap(String maPhong, String proposedStartTimeStr, String proposedEndTimeStr, String maSuatChieuToExclude) {
        String sql = "SELECT COUNT(*) FROM SuatChieu " +
                     "WHERE maPhong = ? " +
                     "AND thoiGianBD < ? " +
                     "AND thoiGianKetThuc > ? ";
        //nếu có một maSuatChieuToExclude (mã suất chiếu muốn bỏ qua) tức là nếu muốn kiểm tra sự trùng lịch nhưng không tính
        //đến một suất chiếu cụ thể (ví dụ: khi cập nhật thông tin của một suất chiếu hiện tại và muốn bỏ qua chính nó), thì điều
        //kiện này sẽ đảm bảo không tính vào kết quả.
        
        if (maSuatChieuToExclude != null && !maSuatChieuToExclude.trim().isEmpty()) {
            sql += "AND maSuatChieu <> ? ";
        }

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Timestamp proposedEndTime = Timestamp.valueOf(LocalDateTime.parse(proposedEndTimeStr, DATETIME_FORMATTER));
            Timestamp proposedStartTime = Timestamp.valueOf(LocalDateTime.parse(proposedStartTimeStr, DATETIME_FORMATTER));

            stmt.setString(1, maPhong);
            stmt.setTimestamp(2, proposedEndTime);
            stmt.setTimestamp(3, proposedStartTime);

            if (maSuatChieuToExclude != null && !maSuatChieuToExclude.trim().isEmpty()) {
                stmt.setString(4, maSuatChieuToExclude);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra trùng lịch chiếu: [" + e.getErrorCode() + "] " + e.getMessage());
            return true;
        } catch (Exception ex) {
             System.err.println("Lỗi xử lý ngày giờ khi kiểm tra trùng lịch: " + ex.getMessage());
             return true;
        }
        return false;
    }

    // Lấy danh sách suất chiếu hôm nay
    public static List<SuatChieu> getShowtimesToday() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE CAST(s.thoiGianBD AS DATE) = CAST(GETDATE() AS DATE) " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lấy suất chiếu hôm nay thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Lấy các suất chiếu trong tương lai
    public static List<SuatChieu> getFutureShowtimes() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE s.thoiGianBD > GETDATE() " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lấy suất chiếu tương lai thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }

    // Lấy suất chiếu trong tuần hiện tại
    public static List<SuatChieu> getShowtimesThisWeek() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT s.maSuatChieu, s.gia, s.thoiGianBD, s.thoiGianKetThuc, " +
                     "p.maPhim, p.tenPhim, p.thoiLuong, " +
                     "pc.maPhong, pc.tenPhong, pc.soGhe " +
                     "FROM SuatChieu s " +
                     "JOIN Phim p ON s.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON s.maPhong = pc.maPhong " +
                     "WHERE CAST(s.thoiGianBD AS DATE) BETWEEN CAST(GETDATE() AS DATE) AND DATEADD(day, 6, CAST(GETDATE() AS DATE)) " +
                     "ORDER BY s.thoiGianBD ASC";
        try (Connection conn = DbConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lấy suất chiếu trong tuần thất bại: [" + e.getErrorCode() + "] " + e.getMessage());
        }
        return list;
    }
}

-- Switch to the correct database
USE CinemaManagementDB;
GO

-- Insert data into TheLoai (Genres)
INSERT INTO TheLoai (maTheLoai, tenTheLoai, moTa) VALUES
('TL01', N'Hành Động', N'Phim có các cảnh hành động mạnh mẽ, đầy kịch tính.'),
('TL02', N'Lãng Mạn', N'Phim tập trung vào tình yêu và mối quan hệ giữa các nhân vật.'),
('TL03', N'Khoa Học Viễn Tưởng', N'Phim lấy bối cảnh trong tương lai hoặc vũ trụ, thường có yếu tố khoa học.'),
('TL04', N'Hoạt Hình', N'Phim dành cho trẻ em hoặc người lớn, thường có hình ảnh hoạt hình.'),
('TL05', N'Kinh Dị', N'Phim gây cảm giác sợ hãi, hồi hộp với các tình huống căng thẳng.');
GO

-- Insert data into Phim (Movies)
INSERT INTO Phim (maPhim, tenPhim, daoDien, dienVien, maTheLoai, thoiLuong, xepHang, moTa) VALUES
('PH01', N'Người Hùng', N'Nguyễn Văn A', N'Lý Thị B, Trần Văn C', 'TL01', 120, 'PG-13', N'Một bộ phim hành động với các pha đấu võ kịch tính.'),
('PH02', N'Tình Yêu Màu Hồng', N'Lê Thị D', N'Nguyễn Thị E, Hoàng Văn F', 'TL02', 90, 'PG', N'Phim lãng mạn kể về câu chuyện tình yêu đẹp.'),
('PH03', N'Cuộc Chiến Thế Kỷ', N'Trần Văn G', N'Lý Thị H, Nguyễn Thị I', 'TL03', 150, 'R', N'Phim khoa học viễn tưởng về cuộc chiến tranh giữa các hành tinh.'),
('PH04', N'Phim Hoạt Hình Vui Nhộn', N'Nguyễn Thiên K', N'Hoàng Duy L, Lý Minh N', 'TL04', 85, 'G', N'Một bộ phim hoạt hình thú vị cho mọi lứa tuổi.'),
('PH05', N'Đêm Kinh Hoàng', N'Lê Tuấn M', N'Trần Thị N, Lý Hoàng O', 'TL05', 110, 'R', N'Phim kinh dị với các cảnh rùng rợn và hồi hộp.');
GO

-- Insert data into KhachHang (Customers)
INSERT INTO KhachHang (maKhachHang, tenKhachHang, SDT, email) VALUES
('KH01', N'Nguyễn Văn A', '0901234567', 'nguyenvana@gmail.com'),
('KH02', N'Lý Thị B', '0907654321', 'lithiba@yahoo.com'),
('KH03', N'Trần Minh C', '0912345678', 'tranminhc@hotmail.com'),
('KH04', N'Hoàng Thị D', '0923456789', 'hoangthid@outlook.com'),
('KH05', N'Nguyễn Thi E', '0934567890', 'nguyenhieu@gmail.com');
GO

-- Insert data into NhanVien (Employees)
INSERT INTO NhanVien (maNhanVien, tenNhanVien, chucVu, tenDangNhap, matKhau) VALUES
('NV01', N'Nguyễn Hoàng A', N'Quản lý', 'admin', '123456'),
('NV02', N'Lý Thị B', N'Thu ngân', 'cashier01', 'password1'),
('NV03', N'Trần Thi C', N'Phục vụ', 'server01', 'password2'),
('NV04', N'Hoàng Văn D', N'Bảo vệ', 'guard01', 'password3'),
('NV05', N'Nguyễn Thị E', N'Lễ tân', 'reception01', 'password4');
GO

-- Insert data into KhuyenMai (Promotions)
INSERT INTO KhuyenMai (maKM, tenKM, loaiKM, giaTriKM, moTa, ngayBDKM, ngayKTKM) VALUES
('KM01', N'Giảm giá 20%', N'Giảm giá', 20.0, N'Giảm giá 20% cho tất cả vé xem phim.', '2025-04-01', '2025-04-30'),
('KM02', N'Mua 1 Tặng 1', N'Khuyến mãi', 0.0, N'Mua 1 vé tặng 1 vé miễn phí.', '2025-04-01', '2025-04-15'),
('KM03', N'Giảm giá cho sinh viên', N'Giảm giá', 15.0, N'Giảm giá 15% cho sinh viên khi xuất trình thẻ sinh viên.', '2025-05-01', '2025-06-01');
GO

-- Insert data into PhongChieu (Theaters)
INSERT INTO PhongChieu (maPhong, tenPhong, soGhe) VALUES
('P01', N'Phòng 1', 100),
('P02', N'Phòng 2', 150),
('P03', N'Phòng 3', 80),
('P04', N'Phòng 4', 120),
('P05', N'Phòng 5', 200);
GO

-- Insert data into SuatChieu (Showtimes)
INSERT INTO SuatChieu (maSuatChieu, maPhim, maPhong, gia, thoiGianBD, thoiGianKetThuc) VALUES
('SC01', 'PH01', 'P01', 100000, '2025-04-11 18:00:00', '2025-04-11 20:00:00'),
('SC02', 'PH02', 'P02', 120000, '2025-04-12 19:00:00', '2025-04-12 21:00:00'),
('SC03', 'PH03', 'P03', 150000, '2025-04-13 20:00:00', '2025-04-13 22:30:00'),
('SC04', 'PH04', 'P04', 80000, '2025-04-14 15:00:00', '2025-04-14 16:30:00'),
('SC05', 'PH05', 'P05', 130000, '2025-04-15 21:00:00', '2025-04-15 23:30:00');
GO

-- Insert data into HoaDon (Invoices)
INSERT INTO HoaDon (maHoaDon, maNhanVien, maKhachHang, tongTien, ngayLapHoaDon, trangThai) VALUES
('HD01', 'NV01', 'KH01', 300000, '2025-04-05 17:00:00', N'Đã thanh toán'),
('HD02', 'NV02', 'KH02', 240000, '2025-04-06 18:00:00', N'Chưa thanh toán'),
('HD03', 'NV03', 'KH03', 450000, '2025-04-07 19:30:00', N'Đã thanh toán'),
('HD04', 'NV04', 'KH04', 150000, '2025-04-08 14:00:00', N'Chưa thanh toán'),
('HD05', 'NV05', 'KH05', 600000, '2025-04-09 20:30:00', N'Đã thanh toán');
GO

-- Drop the procedure if it exists
IF OBJECT_ID('dbo.InsertSeatsForRoom', 'P') IS NOT NULL
    DROP PROCEDURE dbo.InsertSeatsForRoom;
GO

-- Create a Stored Procedure to insert seats for a given room
CREATE PROCEDURE InsertSeatsForRoom (
    @room_code VARCHAR(50),
    @num_rows INT,
    @seats_per_row INT
)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @row_counter INT = 0;
    DECLARE @seat_counter INT = 0;
    DECLARE @current_row_char CHAR(1);
    DECLARE @seat_code VARCHAR(55);

    DELETE FROM GheNgoi WHERE maPhong = @room_code;

    WHILE @row_counter < @num_rows
    BEGIN
        SET @current_row_char = CHAR(65 + @row_counter);
        SET @seat_counter = 0;

        WHILE @seat_counter < @seats_per_row
        BEGIN
            -- Generate seat code (e.g., P01-A01) using zero-padding for seat number
            SET @seat_code = CONCAT(@room_code, '-', @current_row_char, FORMAT(@seat_counter + 1, '00'));

            INSERT INTO GheNgoi (maGhe, maPhong, hang, soGhe, trangThai)
            VALUES (@seat_code, @room_code, @current_row_char, @seat_counter + 1, N'Trống');

            SET @seat_counter = @seat_counter + 1;
        END;

        SET @row_counter = @row_counter + 1;
    END;
END;
GO

-- Execute the procedure for each room
EXEC InsertSeatsForRoom @room_code = 'P01', @num_rows = 10, @seats_per_row = 10;
EXEC InsertSeatsForRoom @room_code = 'P02', @num_rows = 10, @seats_per_row = 15;
EXEC InsertSeatsForRoom @room_code = 'P03', @num_rows = 8, @seats_per_row = 10;
EXEC InsertSeatsForRoom @room_code = 'P04', @num_rows = 12, @seats_per_row = 10;
EXEC InsertSeatsForRoom @room_code = 'P05', @num_rows = 10, @seats_per_row = 20;
GO

-- Optional: Verify the inserted data
SELECT maPhong, COUNT(*) AS SoLuongGhe FROM GheNgoi GROUP BY maPhong;
GO
SELECT TOP 20 * FROM GheNgoi WHERE maPhong = 'P01';
GO

-- You can drop the procedure after use if you don't need it anymore
-- IF OBJECT_ID('dbo.InsertSeatsForRoom', 'P') IS NOT NULL
--     DROP PROCEDURE dbo.InsertSeatsForRoom;
-- GO
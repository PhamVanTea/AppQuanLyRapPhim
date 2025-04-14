-- Create the database if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'CinemaManagementDB')
BEGIN
    CREATE DATABASE CinemaManagementDB;
END
GO

-- Switch to the newly created database
USE CinemaManagementDB;
GO

-- Create TheLoai table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TheLoai]') AND type in (N'U'))
BEGIN
    CREATE TABLE TheLoai (
        maTheLoai VARCHAR(50) PRIMARY KEY,
        tenTheLoai NVARCHAR(100),
        moTa NVARCHAR(MAX)
    );
END
GO

-- Create Phim table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Phim]') AND type in (N'U'))
BEGIN
    CREATE TABLE Phim (
        maPhim VARCHAR(50) PRIMARY KEY,
        tenPhim NVARCHAR(100),
        daoDien NVARCHAR(100),
        dienVien NVARCHAR(MAX),
        maTheLoai VARCHAR(50),
        thoiLuong INT,
        xepHang VARCHAR(50),
        moTa NVARCHAR(MAX),
        FOREIGN KEY (maTheLoai) REFERENCES TheLoai(maTheLoai)
    );
END
GO

-- Create KhachHang table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[KhachHang]') AND type in (N'U'))
BEGIN
    CREATE TABLE KhachHang (
        maKhachHang VARCHAR(50) PRIMARY KEY,
        tenKhachHang NVARCHAR(100),
        SDT VARCHAR(20),
        email VARCHAR(100)
    );
END
GO

-- Create NhanVien table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[NhanVien]') AND type in (N'U'))
BEGIN
    CREATE TABLE NhanVien (
        maNhanVien VARCHAR(50) PRIMARY KEY,
        tenNhanVien NVARCHAR(100),
        chucVu NVARCHAR(50), -- Changed VARCHAR to NVARCHAR
        tenDangNhap VARCHAR(50),
        matKhau VARCHAR(100) -- Consider using VARBINARY for hashed passwords in a real application
    );
END
GO

-- Create KhuyenMai table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[KhuyenMai]') AND type in (N'U'))
BEGIN
    CREATE TABLE KhuyenMai (
        maKM VARCHAR(50) PRIMARY KEY,
        tenKM NVARCHAR(100),
        loaiKM NVARCHAR(50), -- Changed VARCHAR to NVARCHAR
        giaTriKM FLOAT,
        moTa NVARCHAR(MAX),
        ngayBDKM DATE,
        ngayKTKM DATE
    );
END
GO

-- Create PhongChieu table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[PhongChieu]') AND type in (N'U'))
BEGIN
    CREATE TABLE PhongChieu (
        maPhong VARCHAR(50) PRIMARY KEY,
        tenPhong NVARCHAR(100),
        soGhe INT
    );
END
GO

-- Create GheNgoi table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[GheNgoi]') AND type in (N'U'))
BEGIN
    CREATE TABLE GheNgoi (
        maGhe VARCHAR(50) PRIMARY KEY,
        maPhong VARCHAR(50),
        hang CHAR(1),
        soGhe INT,
        trangThai NVARCHAR(50), -- Changed VARCHAR to NVARCHAR
        FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong)
    );
END
GO

-- Create SuatChieu table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SuatChieu]') AND type in (N'U'))
BEGIN
    CREATE TABLE SuatChieu (
        maSuatChieu VARCHAR(50) PRIMARY KEY,
        maPhim VARCHAR(50),
        maPhong VARCHAR(50),
        gia FLOAT, -- Consider DECIMAL or NUMERIC
        thoiGianBD DATETIME,
        thoiGianKetThuc DATETIME,
        FOREIGN KEY (maPhim) REFERENCES Phim(maPhim),
        FOREIGN KEY (maPhong) REFERENCES PhongChieu(maPhong)
    );
END
GO

-- Create HoaDon table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[HoaDon]') AND type in (N'U'))
BEGIN
    CREATE TABLE HoaDon (
        maHoaDon VARCHAR(50) PRIMARY KEY,
        maNhanVien VARCHAR(50),
        maKhachHang VARCHAR(50),
        tongTien FLOAT, -- Consider DECIMAL or NUMERIC
        ngayLapHoaDon DATETIME,
        trangThai NVARCHAR(50), -- Changed VARCHAR to NVARCHAR
        FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
        FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
    );
END
GO

-- Create Ve table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Ve]') AND type in (N'U'))
BEGIN
    CREATE TABLE Ve (
        maVe VARCHAR(50) PRIMARY KEY,
        maHoaDon VARCHAR(50),
        maSuatChieu VARCHAR(50),
        giaVe FLOAT, -- Consider DECIMAL or NUMERIC
        maGhe VARCHAR(50),
        FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
        FOREIGN KEY (maSuatChieu) REFERENCES SuatChieu(maSuatChieu),
        FOREIGN KEY (maGhe) REFERENCES GheNgoi(maGhe)
    );
END
GO

-- Create ThongKe table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ThongKe]') AND type in (N'U'))
BEGIN
    CREATE TABLE ThongKe (
        MaThongKe VARCHAR(50) PRIMARY KEY,
        TongDoanhThu DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
        TongSoVe INT NOT NULL DEFAULT 0,
        TongSoKhachHang INT NOT NULL DEFAULT 0,
        TongSoNhanVien INT NOT NULL DEFAULT 0,
        TongSoPhim INT NOT NULL DEFAULT 0,
        TongSoPhongChieu INT NOT NULL DEFAULT 0,
        TuNgay DATE NOT NULL,
        DenNgay DATE NOT NULL,

        CONSTRAINT CK_ThongKe_DoanhThuNonNegative CHECK (TongDoanhThu >= 0),
        CONSTRAINT CK_ThongKe_SoVeNonNegative CHECK (TongSoVe >= 0),
        CONSTRAINT CK_ThongKe_SoKhachHangNonNegative CHECK (TongSoKhachHang >= 0),
        CONSTRAINT CK_ThongKe_SoNhanVienNonNegative CHECK (TongSoNhanVien >= 0),
        CONSTRAINT CK_ThongKe_SoPhimNonNegative CHECK (TongSoPhim >= 0),
        CONSTRAINT CK_ThongKe_SoPhongChieuNonNegative CHECK (TongSoPhongChieu >= 0),
        CONSTRAINT CK_ThongKe_DateOrder CHECK (DenNgay >= TuNgay)
    );
END
GO
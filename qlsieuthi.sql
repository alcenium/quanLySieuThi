Drop Database If Exists QLBanHangSieuThi;
Create Database QLSieuThi;
Use QLSieuThi;

-- ============ Tạo tables ============
Drop Table If Exists taiKhoan;
Create Table taiKhoan (
    maTK int auto_increment primary key not null,
    tenTK varchar(32),
    matKhau varchar(64));

Drop Table If Exists khachHang;
Create Table khachHang (
    maKH int unique,
    ten varchar(32),
    sdt varchar(12),
    diachi varchar(128),
    Foreign Key (maKH) References taiKhoan(maTK) On Delete Cascade);

Drop Table If Exists nhanVien;
Create Table nhanVien (
    maNV int unique,
    ten varchar(32),
    ngaySinh date,
    gioiTinh varchar(10),
    sdt varchar(12),
    diaChi varchar(128),
    Foreign Key (maNV) References taiKhoan(maTK) On Delete Cascade);

Drop Table If Exists danhMuc;
Create Table danhMuc (
    maDM int auto_increment primary key not null,
    tenDM varchar(32));

Drop Table If Exists sanPham;
Create Table sanPham (
    maSP int auto_increment primary key not null,
    maDM int,
    tenSP varchar(32),
    gia int,
    soLuong int,
    Foreign Key (maDM) References danhMuc(maDM) On Delete Cascade);

Drop Table If Exists giaoHang;
create table giaoHang (
    maGiaoHang int auto_increment primary key not null,
    maKH int,
    ngayTao date,
    tinhTrang varchar(16),
    foreign key (maKH) references khachHang(maKH));

Drop Table If Exists hoaDon;
Create Table hoaDon (
    maHoaDon int auto_increment primary key not null,
    maNV int,
    ngayThanhToan date,
    foreign key (maNV) references nhanVien(maNV));

Drop Table If Exists gioHang;
Create Table gioHang (
    maGioHang int auto_increment primary key not null,
    maKH int,
    ngayTao date,
    foreign key (maKH) references khachHang(maKH) On Delete Cascade);

Drop Table If Exists item_giaoHang;
Create Table item_giaoHang (
    maItemGiaoHang int auto_increment primary key not null,
    maGiaoHang int,
    maSP int,
    soLuong int,
    foreign key (maGiaoHang) references giaoHang(maGiaoHang) On Delete Cascade,
    foreign key (maSP) references sanPham(maSP) On Delete Cascade);

Drop Table If Exists item_gioHang;
Create Table item_gioHang (
    maItemGioHang int auto_increment primary key not null,
    maGioHang int,
    maSP int,
    soLuong int,
    foreign key (maGioHang) references gioHang(maGioHang) On Delete Cascade,
    foreign key (maSP) references sanPham(maSP) On Delete Cascade);

Drop Table If Exists item_hoaDon;
Create Table item_hoaDon (
    maItemHoaDon int auto_increment primary key not null,
    maHoaDon int,
    maSP int,
    soLuong int,
    foreign key (maHoaDon) references hoaDon(maHoaDon) On Delete Cascade,
    foreign key (maSP) references sanPham(maSP) On Delete Cascade);

-- ========== Dữ liệu mẫu ==========
-- Tài khoản
INSERT INTO taiKhoan (tenTK, matKhau) VALUES
('admin', '123456'),
('tranthib', 'password123'),
('levanc', 'password123'),
('phamthid', 'password123'),
('hoangvane', 'password123'),
('admin01', 'admin123'),
('nhanvien02', 'nv123'),
('nhanvien03', 'nv123'),
('nhanvien04', 'nv123'),
('nhanvien05', 'nv123');

-- Khách hàng
INSERT INTO khachHang (maKH, ten, sdt, diachi) VALUES
(1, 'Nguyễn Văn An', '0901234567', '123 Đường Nguyễn Huệ, Quận 1, TP.HCM'),
(2, 'Trần Thị Bình', '0912345678', '456 Đường Lê Lợi, Quận 3, TP.HCM'),
(3, 'Lê Văn Cường', '0923456789', '789 Đường Trần Hưng Đạo, Quận 5, TP.HCM'),
(4, 'Phạm Thị Dung', '0934567890', '321 Đường Hai Bà Trưng, Quận 10, TP.HCM'),
(5, 'Hoàng Văn Em', '0945678901', '654 Đường Võ Văn Tần, Quận Bình Thạnh, TP.HCM');

-- Nhân viên
INSERT INTO nhanVien (maNV, ten, ngaySinh, gioiTinh, sdt, diaChi) VALUES
(6, 'Nguyễn Thị Hoa', '1990-05-15', 'Nữ', '0956789012', '111 Đường Cách Mạng Tháng 8, Quận Tân Bình, TP.HCM'),
(7, 'Trần Văn Kiên', '1988-08-20', 'Nam', '0967890123', '222 Đường Lý Thường Kiệt, Quận 11, TP.HCM'),
(8, 'Lê Thị Lan', '1992-03-10', 'Nữ', '0978901234', '333 Đường Phan Đình Phùng, Quận Phú Nhuận, TP.HCM'),
(9, 'Phạm Văn Minh', '1985-12-25', 'Nam', '0989012345', '444 Đường Điện Biên Phủ, Quận 3, TP.HCM'),
(10, 'Hoàng Thị Nga', '1995-07-30', 'Nữ', '0990123456', '555 Đường Hoàng Văn Thụ, Quận Tân Bình, TP.HCM');

-- Danh mục sản phẩm
INSERT INTO danhMuc (tenDM) VALUES
('Thực phẩm tươi sống'),
('Thực phẩm đóng hộp'),
('Đồ uống'),
('Gia vị'),
('Đồ dùng gia đình'),
('Vệ sinh cá nhân'),
('Văn phòng phẩm');

-- Sản phẩm
INSERT INTO sanPham (maDM, tenSP, gia, soLuong) VALUES
-- Thực phẩm tươi sống
(1, 'Thịt heo ba chỉ', 85000, 50),
(1, 'Thịt bò nạm', 180000, 30),
(1, 'Cá hồi Na Uy', 250000, 20),
(1, 'Rau cải xanh', 15000, 100),
(1, 'Cà chua', 20000, 80),
-- Thực phẩm đóng hộp
(2, 'Mì gói Hảo Hảo', 3500, 500),
(2, 'Cơm hộp SG Food', 25000, 200),
(2, 'Thịt hộp Spam', 65000, 150),
(2, 'Cá ngừ đóng hộp', 35000, 180),
-- Đồ uống
(3, 'Coca Cola 1.5L', 18000, 300),
(3, 'Nước suối Lavie', 5000, 500),
(3, 'Trà xanh C2', 10000, 400),
(3, 'Bia Heineken', 22000, 250),
(3, 'Sữa tươi Vinamilk', 32000, 200),
-- Gia vị
(4, 'Muối i-ốt', 8000, 300),
(4, 'Đường trắng', 20000, 250),
(4, 'Nước mắm Nam Ngư', 35000, 200),
(4, 'Dầu ăn Simply', 45000, 180),
-- Đồ dùng gia đình
(5, 'Bột giặt OMO', 120000, 100),
(5, 'Nước rửa chén Sunlight', 35000, 150),
(5, 'Giấy vệ sinh Pulppy', 45000, 200),
(5, 'Bàn chải đánh răng PS', 25000, 180),
-- Vệ sinh cá nhân
(6, 'Dầu gội Clear', 85000, 120),
(6, 'Sữa tắm Dove', 95000, 100),
(6, 'Kem đánh răng Colgate', 35000, 200),
-- Văn phòng phẩm
(7, 'Bút bi Thiên Long', 5000, 500),
(7, 'Vở kẻ ngang', 12000, 300),
(7, 'Bút chì 2B', 3000, 400);

-- Giỏ hàng
INSERT INTO gioHang (maKH, ngayTao) VALUES
(1, '2024-12-20'),
(2, '2024-12-21'),
(3, '2024-12-22');

-- Items trong giỏ hàng
INSERT INTO item_gioHang (maGioHang, maSP, soLuong) VALUES
(1, 1, 2),   -- 2 kg thịt heo
(1, 10, 3),  -- 3 chai Coca
(1, 11, 5),  -- 5 chai nước suối
(2, 3, 1),   -- 1 kg cá hồi
(2, 14, 2),  -- 2 hộp sữa tươi
(3, 6, 10),  -- 10 gói mì
(3, 20, 2);  -- 2 bột giặt

-- Giao hàng (với tình trạng)
INSERT INTO giaoHang (maKH, ngayTao, tinhTrang) VALUES
(1, '2024-12-18', 'Đã giao'),
(2, '2024-12-19', 'Đã giao'),
(4, '2024-12-20', 'Đang giao'),
(5, '2024-12-21', 'Chờ xử lý');

-- Items giao hàng
INSERT INTO item_giaoHang (maGiaoHang, maSP, soLuong) VALUES
(1, 1, 1),   -- Order 1: thịt heo
(1, 10, 2),  -- Order 1: 2 coca
(1, 15, 1),  -- Order 1: muối
(2, 3, 1),   -- Order 2: cá hồi
(2, 14, 3),  -- Order 2: 3 sữa tươi
(3, 6, 5),   -- Order 3: 5 gói mì
(3, 20, 1),  -- Order 3: bột giặt
(4, 23, 2);  -- Order 4: 2 dầu gội

-- Hóa đơn (chỉ cho đơn hàng đã giao)
INSERT INTO hoaDon (maNV, maHoaDon, ngayThanhToan) VALUES
(6, 1, '2024-12-18'),
(7, 2, '2024-12-19');

-- Items hóa đơn với giá gốc và giá bán
INSERT INTO item_hoaDon (maHoaDon, maSP, soLuong) VALUES
-- Hóa đơn 1 (giaoHang 1)
(1, 1, 1),   -- thịt heo giá thường
(1, 10, 2),  -- coca giá thường
(1, 15, 1),    -- muối giá thường
-- Hóa đơn 2 (giaoHang 2)
(2, 3, 1), -- cá hồi giảm giá 10k
(2, 14, 3);  -- sữa tươi giảm giá 2k/hộp

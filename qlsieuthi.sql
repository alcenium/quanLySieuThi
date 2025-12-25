Drop Database If Exists QLBanHangSieuThi;
Create Database QLSieuThi;
Use QLSieuThi;

-- ============ Tạo tables ============
Drop Table If Exists taiKhoan;
Create Table taiKhoan (
    maTK int auto_increment primary key not null,
    tenTK varchar(32),
    matKhau varchar(64),
    chucVu varchar(8));

Drop Table If Exists khachHang;
Create Table khachHang (
    maKH int auto_increment primary key not null,
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

Drop Table If Exists nhaCungCap;
Create Table nhaCungCap (
    maNCC int auto_increment primary key not null,
    tenNCC varchar(32),
    diaChi varchar(64),
    thanhPho varchar(32));

Drop Table If Exists sanPham;
Create Table sanPham (
    maSP int auto_increment primary key not null,
    maDM int,
    maNCC int,
    tenSP varchar(32),
    gia int,
    soLuong int,
    Foreign Key (maDM) References danhMuc(maDM) On Delete Cascade,
    Foreign Key (maNCC) References nhaCungCap(maNCC) On Delete Cascade);

Drop Table If Exists khuyenMai;
Create Table khuyenMai (
    maKhM int auto_increment primary key not null,
    tenKhM varchar(32),
    phanTramGiam int,
    ngayHieuLuc date,
    ngayKetThuc date);

Drop Table If Exists spKhuyenMai;
Create Table spKhuyenMai (
    maSP int,
    maKhM int,
    Foreign Key (maSP) References sanPham(maSP) On Delete Cascade,
    Foreign Key (maKhM) References khuyenMai(maKhM) On Delete Cascade);

Drop Table If Exists giaoHang;
create table giaoHang (
    maGiaoHang int auto_increment primary key not null,
    maKH int,
    ngayTao date,
    tinhTrang varchar(16),
    foreign key (maKH) references khachHang(maKH) on delete cascade);

Drop Table If Exists hoaDon;
Create Table hoaDon (
    maHoaDon int auto_increment primary key not null,
    maNV int,
    ngayThanhToan date,
    foreign key (maNV) references nhanVien(maNV) on delete cascade);

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
    maKhM int,
    foreign key (maKhM) references khuyenMai(maKhM) On Delete Cascade,
    foreign key (maGiaoHang) references giaoHang(maGiaoHang) On Delete Cascade,
    foreign key (maSP) references sanPham(maSP) On Delete Cascade);

Drop Table If Exists item_gioHang;
Create Table item_gioHang (
    maItemGioHang int auto_increment primary key not null,
    maGioHang int,
    maSP int,
    soLuong int,
    maKhM int,
    foreign key (maKhM) references khuyenMai(maKhM) On Delete Cascade,
    foreign key (maGioHang) references gioHang(maGioHang) On Delete Cascade,
    foreign key (maSP) references sanPham(maSP) On Delete Cascade);

Drop Table If Exists item_hoaDon;
Create Table item_hoaDon (
    maItemHoaDon int auto_increment primary key not null,
    maHoaDon int,
    tenSP varchar(32),
    gia int,
    soLuong int,
    phanTramGiam int,
    foreign key (maHoaDon) references hoaDon(maHoaDon) On Delete Cascade);

-- ========== Dữ liệu mẫu ==========
-- Tài khoản
Insert Into taiKhoan (tenTK, matKhau) Values
('nv001', 'nv123456'),
('nv002', 'nv789012'),
('nv003', 'nv345678'),
('nv004', 'nv901234'),
('nv005', 'nv567890'),
('admin', '123456');

-- Khách hàng
Insert Into khachHang (maKH, ten, sdt, diachi) Values
(1, 'Nguyễn Văn An', '0912345678', '123 Phố Huế, Hai Bà Trưng, Hà Nội'),
(2, 'Trần Thị Bình', '0923456789', '45 Đường Láng, Đống Đa, Hà Nội'),
(3, 'Lê Văn Cường', '0934567890', '78 Nguyễn Trãi, Thanh Xuân, Hà Nội'),
(4, 'Phạm Thị Dung', '0945678901', '12 Giải Phóng, Hoàng Mai, Hà Nội'),
(5, 'Hoàng Văn Em', '0956789012', '234 Trường Chinh, Thanh Xuân, Hà Nội');

-- Nhân viên
Insert Into nhanVien (maNV, ten, ngaySinh, gioiTinh, sdt, diaChi) Values
(1, 'Đỗ Thị Hoa', '1995-03-15', 'Nữ', '0967890123', '56 Xã Đàn, Đống Đa, Hà Nội'),
(2, 'Vũ Văn Hùng', '1992-07-20', 'Nam', '0978901234', '89 Láng Hạ, Ba Đình, Hà Nội'),
(3, 'Ngô Thị Lan', '1998-11-08', 'Nữ', '0989012345', '23 Kim Mã, Ba Đình, Hà Nội'),
(4, 'Bùi Văn Minh', '1994-05-25', 'Nam', '0990123456', '67 Tây Sơn, Đống Đa, Hà Nội'),
(5, 'Đinh Thị Nga', '1996-09-12', 'Nữ', '0901234567', '145 Chùa Bộc, Đống Đa, Hà Nội');

-- Danh mục
Insert Into danhMuc (tenDM) Values
('Thực phẩm tươi sống'),
('Đồ uống'),
('Bánh kẹo'),
('Gia vị'),
('Đồ dùng gia đình'),
('Chăm sóc cá nhân');

-- Nhà cung cấp
Insert Into nhaCungCap (tenNCC, diaChi, thanhPho) Values
('Công ty TNHH Thực phẩm Sạch', '45 Minh Khai, Hai Bà Trưng', 'Hà Nội'),
('Công ty CP Đồ uống Việt', '78 Phạm Ngọc Thạch, Đống Đa', 'Hà Nội'),
('Công ty TNHH Bánh kẹo Hà Nội', '123 Giảng Võ, Ba Đình', 'Hà Nội'),
('Công ty CP Gia vị Việt Nam', '56 Lê Duẩn, Hoàn Kiếm', 'Hà Nội'),
('Công ty TNHH Hóa mỹ phẩm', '89 Nguyễn Chí Thanh, Đống Đa', 'Hà Nội');

-- Sản phẩm
Insert Into sanPham (maDM, maNCC, tenSP, gia, soLuong) Values
(1, 1, 'Thịt heo ba chỉ', 120000, 50),
(1, 1, 'Cá thu tươi', 150000, 30),
(1, 1, 'Rau cải xanh', 15000, 100),
(2, 2, 'Nước ngọt Coca Cola', 10000, 200),
(2, 2, 'Bia Hà Nội', 12000, 150),
(2, 2, 'Nước khoáng Lavie', 5000, 300),
(3, 3, 'Bánh quy Cosy', 25000, 80),
(3, 3, 'Kẹo Alpenliebe', 35000, 60),
(3, 3, 'Bánh snack Oishi', 8000, 120),
(4, 4, 'Nước mắm Nam Ngư', 45000, 70),
(4, 4, 'Dầu ăn Simply', 55000, 90),
(4, 4, 'Muối I-ốt', 8000, 150),
(5, 5, 'Nước rửa chén Sunlight', 32000, 100),
(5, 5, 'Bột giặt OMO', 95000, 60),
(6, 5, 'Dầu gội Head & Shoulders', 120000, 50),
(6, 5, 'Kem đánh răng PS', 28000, 80);

-- Khuyến mãi
Insert Into khuyenMai (tenKhM, phanTramGiam, ngayHieuLuc, ngayKetThuc) Values
('Giảm giá cuối tuần', 10, '2024-12-20', '2024-12-22'),
('Khuyến mãi Noel', 15, '2024-12-24', '2024-12-26'),
('Giảm giá đầu năm', 20, '2025-01-01', '2025-01-07'),
('Flash sale', 25, '2024-12-23', '2024-12-23');

-- Sản phẩm khuyến mãi
Insert Into spKhuyenMai (maSP, maKhM) Values
(1, 1), (2, 1), (4, 2), (5, 2), (7, 2), (8, 2),
(10, 3), (11, 3), (13, 4), (14, 4), (15, 4);

-- Giỏ hàng
Insert Into gioHang (maKH, ngayTao) Values
(1, '2024-12-20'),
(2, '2024-12-21'),
(3, '2024-12-22');

-- Item giỏ hàng
Insert Into item_gioHang (maGioHang, maSP, soLuong, maKhM) Values
(1, 1, 2, 1),
(1, 4, 3, NULL),
(2, 7, 5, 2),
(2, 10, 1, NULL),
(3, 13, 2, NULL);

-- Giao hàng
Insert Into giaoHang (maKH, ngayTao, tinhTrang) Values
(1, '2024-12-18', 'Đã giao'),
(2, '2024-12-19', 'Đang giao'),
(4, '2024-12-21', 'Chờ xử lý'),
(5, '2024-12-22', 'Đang giao');

-- Item giao hàng
Insert Into item_giaoHang (maGiaoHang, maSP, soLuong, maKhM) Values
(1, 2, 2, NULL),
(1, 3, 5, NULL),
(2, 8, 3, 2),
(3, 11, 2, NULL),
(4, 15, 1, NULL);

-- Hóa đơn
Insert Into hoaDon (maNV, ngayThanhToan) Values
(6, '2024-12-18'),
(7, '2024-12-19'),
(8, '2024-12-20'),
(9, '2024-12-21');

-- Item hóa đơn
Insert Into item_hoaDon (maHoaDon, tenSP, gia, soLuong, phanTramGiam) Values
(1, 'Cá thu tươi', 150000, 2, 0),
(1, 'Rau cải xanh', 15000, 5, 0),
(2, 'Kẹo Alpenliebe', 35000, 3, 15),
(3, 'Dầu ăn Simply', 55000, 2, 0),
(4, 'Dầu gội Head & Shoulders', 120000, 1, 0);

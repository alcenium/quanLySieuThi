## Lưu ý trước khi chạy chương trình:
* __Chương trình sử dụng MySQL/MariaDB__
- [ ] Kiểm tra lại tên và mật khẩu trong `src/main/java/com/baiTap/DB.java`:
``` java
private String username = "root";
private String password = "";
private String database = "QLSieuThi";
private String url = "jdbc:mariadb://localhost:3306/" + database;
```

## Chạy chương trình:
* Chương trình sử dụng maven, có 2 file hỗ trợ chạy chương trình là `makefile` và `run.bat`
#### Cho người dùng window, nhập lệnh:
``` powershell
run.bat
```
hoặc
``` powershell
run
```
Khi đang ở thư mục gốc (`quanLySieuThi`) để chạy chương trình

> [!IMPORTANT]
> Lưu ý: yêu cầu đã cài maven và thêm vào path để có thể chạy được file `run.bat` mà không bị lỗi
-------------------
#### Cho người dùng linux/macos, nhập lệnh:
``` bash
make run
```
Khi đang ở thư mục gốc (`quanLySieuThi`) để chạy chương trình
> [!IMPORTANT]
> Lưu ý: yêu cầu phải cài makefile và maven

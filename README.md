# Gọi MySQL Stored Procedures từ JDBC

Java 17, Tomcat 10.1, Jakarta Servlet/JSP, JSTL và MySQL Connector/J. Ứng dụng quản lý User gồm danh sách, thêm, sửa và xóa.

## Phần thực hành

- IUserDAO: getUserById(int), insertUserStore(User).
- UserDAO gọi `{CALL get_user_by_id(?)}` và `{CALL insert_user(?, ?, ?)}` bằng CallableStatement, dùng try-with-resources đóng Connection, Statement và ResultSet.
- UserServlet tải form Edit bằng getUserById; thêm User bằng insertUserStore; redirect /users sau khi lưu tránh gửi lại khi F5.
- Danh sách, cập nhật và xóa giữ PreparedStatement; hai chức năng yêu cầu đã chuyển sang stored procedure.

## Thiết lập

1. Chạy sql/setup.sql trong MySQL. Script tạo database demo, bảng users nếu chưa có; tạo lại hai procedure (không xóa dữ liệu users).
2. Đặt biến môi trường trước khi khởi động Tomcat: DB_URL, DB_USER, DB_PASSWORD. Mặc định URL jdbc:mysql://localhost:3306/demo?serverTimezone=UTC&characterEncoding=UTF-8, user root, password trống. Không lưu mật khẩu thật trong repo.
3. Chạy `mvn clean package`, copy target/jdbc-stored-procedures.war vào webapps Tomcat 10.1.
4. Mở http://localhost:8080/jdbc-stored-procedures/users.
5. Thêm User, kiểm tra danh sách; bấm Edit xem dữ liệu lấy từ procedure; sửa và xóa khi cần.

Tài khoản MySQL cần quyền truy cập bảng users và EXECUTE các procedure trong demo. Đây là bài thực hành local, chưa tích hợp xác thực người dùng.

# Gọi MySQL Stored Procedures từ JDBC — Bài tập CRUD
Java 17, Maven WAR, Tomcat 10.1, Jakarta Servlet/JSP, MySQL Connector/J.

## Yêu cầu đã triển khai
- Danh sách: select_all_users(), UserDAO.selectAllUsersStore(), CallableStatement.executeQuery().
- Sửa: update_user(..., OUT found_user), UserDAO.updateUserStore(), setInt/setString và registerOutParameter. Sửa với giá trị không đổi vẫn thành công nếu User tồn tại.
- Xóa: delete_user(..., OUT deleted_rows), UserDAO.deleteUserStore(), trả false khi không có User.
- IUserDAO khai báo ba phương thức mới; UserServlet gọi trực tiếp các phương thức này. Sửa/xóa dùng POST; thành công redirect về danh sách; ID không tồn tại trả 404; lỗi DB được ghi log và trả 500.
- Giữ chức năng thêm và tìm theo ID bằng Stored Procedure của bài trước. Dùng try-with-resources đóng tài nguyên.

## Chạy
1. Chạy sql/setup.sql trên database demo trong MySQL. Script giữ dữ liệu bảng users nhưng tạo lại năm Stored Procedure của bài.
2. Đặt DB_URL, DB_USER, DB_PASSWORD trước khi chạy Tomcat. Mặc định MySQL localhost:3306/demo, root, password trống; không commit mật khẩu thật.
3. mvn clean package; copy target/jdbc-procedure-crud.war vào webapps Tomcat 10.1.
4. Mở http://localhost:8080/jdbc-procedure-crud/users.

## Kiểm tra thực tế
- Thêm User rồi xem danh sách. Sửa tên/email/quốc gia, xem danh sách và SELECT * FROM users để đối chiếu.
- Lưu form không thay đổi dữ liệu: không được trả 404.
- Xóa User, kiểm tra danh sách và DB không còn dòng đó.
- Sửa/xóa ID không tồn tại: 404. Danh sách trống vẫn hiển thị được.
- Dữ liệu nhập chứa dấu nháy và tiếng Việt: được truyền qua tham số CallableStatement; đầu ra JSP dùng c:out.
- CALL select_all_users(); CALL update_user(1,'Test','test@example.com','VN',@found); SELECT @found; CALL delete_user(1,@deleted); SELECT @deleted; (chỉ dùng ID dữ liệu thử của bạn).

Chưa tích hợp đăng nhập. Build Java/WAR không thay thế kiểm thử trực tiếp MySQL/Tomcat.
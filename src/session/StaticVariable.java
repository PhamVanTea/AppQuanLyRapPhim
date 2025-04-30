package session;

import Entity.NhanVien;
//mà bạn đưa ra là một lớp lưu biến toàn cục tạm thời trong ứng dụng – một cách đơn giản để chia
//sẻ dữ liệu giữa các phần khác nhau của chương trình mà không cần truyền tham số qua lại.
//là một cách đơn giản để giữ thông tin đăng nhập của nhân viên và giúp các phần khác của ứng dụng
//(như bán vé, thống kê, v.v.) dễ dàng truy cập thông tin nhân viên hiện tại.
public class StaticVariable {
	public static NhanVien nhanVien;
}

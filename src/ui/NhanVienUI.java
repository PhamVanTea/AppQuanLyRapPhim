package ui;

import dao.NhanVienDAO;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NhanVienUI extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public NhanVienUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 680));

        // --- Table Panel ---
        JPanel panelDanhSach = new JPanel();
        panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Nhân viên", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelDanhSach.setLayout(new BorderLayout());
        add(panelDanhSach, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        panelDanhSach.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        tableModel = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        scrollPane.setViewportView(table);

        loadTableData(NhanVienDAO.readAll());
    }

    private void loadTableData(List<NhanVien> list) {
        if (tableModel.getColumnCount() == 0) {
            tableModel.setColumnIdentifiers(new Object[]{
                "Mã NV", "Tên Nhân Viên", "Chức Vụ", "Tên Đăng Nhập"
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(60);
            table.getColumnModel().getColumn(1).setPreferredWidth(180);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
        }

        tableModel.setRowCount(0);

        if (list != null) {
            for (NhanVien nv : list) {
                tableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu(),
                    nv.getTenDangNhap()
                });
            }
<<<<<<< HEAD
=======
		}
	}

	private void saveNhanVien() {
		String ma;
		String ten = txtTenNhanVien.getText().trim();
		String chucVu = txtChucVu.getText().trim();
        String tenDN = txtTenDangNhap.getText().trim();
        char[] passwordChars = txtMatKhau.getPassword();
        String rawPassword = new String(passwordChars);

		if (ten.isEmpty()) { showValidationError("Tên nhân viên không được để trống.", txtTenNhanVien); Arrays.fill(passwordChars, ' '); return; }
		// Kiểm tra định dạng Tên Nhân Viên
		if (!ten.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
		    showValidationError("Tên nhân viên chỉ chứa chữ cái, viết hoa chữ cái đầu mỗi từ, cho phép khoảng trắng. Ví dụ: Trần Văn B", txtTenNhanVien);
		    Arrays.fill(passwordChars, ' ');
		    return;
		}
        if (chucVu.isEmpty()) { showValidationError("Chức vụ không được để trống.", txtChucVu); Arrays.fill(passwordChars, ' '); return; }
        if (!chucVu.matches("^[A-Za-zÀ-ỹ]+( [A-Za-zÀ-ỹ]+)*( [0-9]+)?$")) {
            showValidationError("Chức vụ không hợp lệ. Chứa chữ cái và có thể có số ở cuối từ sau cùng, được có khoảng trắng . VD: Nhân viên 1", txtChucVu);
            Arrays.fill(passwordChars, ' ');
            return;
        } //Ví dụ hợp lệ: Nhân viên 1 - VD không hợp lệ: Nhân vie1n 1 (không cho phép có số và chữ cái lần vào một từ) - Cho phép kh/trắng giữa các từ
        if (tenDN.isEmpty()) { showValidationError("Tên đăng nhập không được để trống.", txtTenDangNhap); Arrays.fill(passwordChars, ' '); return; }
        if (!tenDN.matches("^[a-z][a-z0-9._%+#-]*$")) {
            showValidationError("Tên đăng nhập không hợp lệ. Phải bắt đầu bằng chữ thường không dấu, không chứa chữ hoa, dấu hoặc khoảng trắng.", txtTenDangNhap);
            Arrays.fill(passwordChars, ' ');
            return;
        } //VD hợp lệ: admin, admin1, admin_1  -  VD không hợp lệ: Admin1, Âdmin, adm in,
        if (currentState == EditState.ADDING && rawPassword.isEmpty()) {
            showValidationError("Mật khẩu là bắt buộc khi thêm nhân viên mới.", txtMatKhau);
            Arrays.fill(passwordChars, ' ');
            return;
        }

        NhanVien nv = new NhanVien();
        nv.setTenNhanVien(ten);
        nv.setChucVu(chucVu);
        nv.setTenDangNhap(tenDN);

		boolean success = false;
		String successMessage = "";
		String errorMessage = "";

		try {
            String passwordToSave = null;

			if (currentState == EditState.ADDING) {
                ma = NhanVienDAO.generateMaNhanVien(); // Use DAO to generate ID
                nv.setMaNhanVien(ma);

                passwordToSave = rawPassword; // Placeholder - REPLACE with actual HASHING!
                if (passwordToSave == null && !rawPassword.isEmpty()) throw new RuntimeException("Password hashing failed");
                nv.setMatKhau(passwordToSave);

				success = NhanVienDAO.create(nv);
				successMessage = "Thêm nhân viên thành công!";
				errorMessage = "Thêm nhân viên thất bại. Mã hoặc Tên đăng nhập có thể đã tồn tại.";

			} else if (currentState == EditState.EDITING) {
                ma = txtMaNhanVien.getText().trim();
                if (ma.isEmpty() || ma.equals("(Tự động)")) {
                     showError("Lỗi: Không xác định được Mã Nhân viên để cập nhật.", null);
                     Arrays.fill(passwordChars, ' ');
                     return;
                 }
                nv.setMaNhanVien(ma);

                if (!rawPassword.isEmpty()) {
                    passwordToSave = rawPassword; // Placeholder - REPLACE with actual HASHING!
                    if (passwordToSave == null && !rawPassword.isEmpty()) throw new RuntimeException("Password hashing failed");
                    nv.setMatKhau(passwordToSave);
                } else {
                    NhanVien existingNV = NhanVienDAO.findById(ma);
                    if(existingNV != null) {
                        nv.setMatKhau(existingNV.getMatKhau());
                    } else {
                        showError("Lỗi: Không tìm thấy nhân viên hiện tại để giữ lại mật khẩu.", null);
                        Arrays.fill(passwordChars, ' ');
                        return;
                    }
                }

				success = NhanVienDAO.update(nv);
				successMessage = "Cập nhật nhân viên thành công!";
				errorMessage = "Cập nhật nhân viên thất bại. Tên đăng nhập có thể bị trùng.";
			} else {
                Arrays.fill(passwordChars, ' '); // Clear password if state is invalid
                return;
            }


			if (success) {
				JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
				loadTableData(NhanVienDAO.readAll());
			} else {
				showValidationError(errorMessage, (currentState == EditState.ADDING) ? txtTenDangNhap : txtTenNhanVien);
                 if (currentState == EditState.ADDING && !success) {
                      txtMaNhanVien.setText("(Tự động)");
                 }
			}
		} catch (Exception ex) {
            showError("Đã xảy ra lỗi hệ thống trong quá trình lưu.", ex);
		} finally {
             Arrays.fill(passwordChars, ' ');
        }
	}

	private void cancelEditMode() {
        setInitialState();
    }

    private void showValidationError(String message, Component componentToFocus) {
        JOptionPane.showMessageDialog(this, message, "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        if (componentToFocus != null) {
            SwingUtilities.invokeLater(() -> {
                 componentToFocus.requestFocusInWindow();
                 if (componentToFocus instanceof JTextField || componentToFocus instanceof JPasswordField) {
                     ((JTextField) componentToFocus).selectAll();
                 }
            });
>>>>>>> 6de2ee8af4f2dc8129bcc3927f83dd96bb8b667f
        }
    }
}

package ui;

import dao.NhanVienDAO;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Arrays; // Import for clearing password array


public class NhanVienUI extends JPanel {
	private JTable table;
	private JTextField txtMaNhanVien;
	private JTextField txtTenNhanVien;
	private JTextField txtChucVu;
	private JTextField txtTenDangNhap;
	private JPasswordField txtMatKhau;
	private JTextField txtTimKiem;

	private DefaultTableModel tableModel;
	private JButton btnTimKiem;
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLuu;
	private JButton btnHuy;
	private JButton btnThoat;

	private enum EditState { IDLE, ADDING, EDITING }
	private EditState currentState = EditState.IDLE;

	public NhanVienUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

		// --- Table Panel ---
		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Nhân viên", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 255, 927, 327);
		add(panelDanhSach);
		panelDanhSach.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelDanhSach.add(scrollPane);

		table = new JTable();
		tableModel = new DefaultTableModel() {
			@Override public boolean isCellEditable(int row, int column) { return false; }
		};
		table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && currentState == EditState.IDLE) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						populateFieldsFromSelectedRow(selectedRow);
					} else {
						clearFields();
					}
                    updateButtonStates();
				}
			}
		});
		scrollPane.setViewportView(table);


		// --- Info Panel ---
		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(null);
		panelInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Nhân viên", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelInfo.setBounds(38, 15, 927, 186);
		add(panelInfo);

        int labelX1 = 30;
        int fieldX1 = 140;
        int labelX2 = 480;
        int fieldX2 = 600;
        int fieldW = 290;
        int labelW = 100;
        int fieldH = 25;
        int row1Y = 35;
        int row2Y = row1Y + 40;
        int row3Y = row2Y + 40;

		JLabel lblMaNhanVien = new JLabel("Mã nhân viên:");
		lblMaNhanVien.setBounds(labelX1, row1Y, labelW, fieldH);
		panelInfo.add(lblMaNhanVien);

		txtMaNhanVien = new JTextField();
		txtMaNhanVien.setEditable(false);
        txtMaNhanVien.setBackground(Color.LIGHT_GRAY);
		txtMaNhanVien.setBounds(fieldX1, row1Y, fieldW, fieldH);
		panelInfo.add(txtMaNhanVien);

		JLabel lblTenNhanVien = new JLabel("Tên nhân viên:");
		lblTenNhanVien.setBounds(labelX2, row1Y, labelW, fieldH);
		panelInfo.add(lblTenNhanVien);

		txtTenNhanVien = new JTextField();
		txtTenNhanVien.setBounds(fieldX2, row1Y, fieldW, fieldH);
		panelInfo.add(txtTenNhanVien);

        JLabel lblChucVu = new JLabel("Chức vụ:");
		lblChucVu.setBounds(labelX1, row2Y, labelW, fieldH);
		panelInfo.add(lblChucVu);

		txtChucVu = new JTextField();
		txtChucVu.setBounds(fieldX1, row2Y, fieldW, fieldH);
		panelInfo.add(txtChucVu);

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
		lblTenDangNhap.setBounds(labelX2, row2Y, labelW, fieldH);
		panelInfo.add(lblTenDangNhap);

		txtTenDangNhap = new JTextField();
		txtTenDangNhap.setBounds(fieldX2, row2Y, fieldW, fieldH);
		panelInfo.add(txtTenDangNhap);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
		lblMatKhau.setBounds(labelX1, row3Y, labelW, fieldH);
		panelInfo.add(lblMatKhau);

		txtMatKhau = new JPasswordField();
		txtMatKhau.setBounds(fieldX1, row3Y, fieldW, fieldH);
		panelInfo.add(txtMatKhau);


        // --- Search Panel ---
		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		panelSearch.setBounds(510, 210, 455, 34);
		add(panelSearch);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên nhân viên cần tìm");
		txtTimKiem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                    searchNhanVien();
	                }
			}
		});
		txtTimKiem.setBounds(0, 7, 274, 25);
		panelSearch.add(txtTimKiem);
		txtTimKiem.setColumns(10);

		// --- Button Creation without Helper Method, with Icons ---
		btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTimKiem.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-search-20.png"))); // Added Icon
		btnTimKiem.addActionListener(e -> searchNhanVien());
		btnTimKiem.setBounds(285, 5, 117, 27);
		panelSearch.add(btnTimKiem);

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(1, 0, 15, 0)); // Use 15 gap

		btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-add-20.png"))); // Added Icon
		btnThem.addActionListener(e -> enterAddMode());
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-edit-20.png"))); // Added Icon
		btnSua.addActionListener(e -> enterEditMode());
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-delete-20.png"))); // Added Icon
		btnXoa.addActionListener(e -> deleteSelectedNhanVien());
		panelChucNang.add(btnXoa);

	 	btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-save-20.png"))); // Added Icon
		btnLuu.addActionListener(e -> saveNhanVien());
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-cancel-20.png"))); // Added Icon
		btnHuy.addActionListener(e -> cancelEditMode());
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(NhanVienUI.class.getResource("/icons/icons8-exit-20.png"))); // Added Icon
		btnThoat.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đóng tab Nhân Viên?", "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                     window.dispose();
                }
            } else {
                System.exit(0);
            }
       });
		panelChucNang.add(btnThoat);

		loadTableData(NhanVienDAO.readAll());
		setInitialState();
	}

	private void populateFieldsFromSelectedRow(int selectedRow) {
		if (selectedRow != -1) {
            if(selectedRow < tableModel.getRowCount()) {
                String maNhanVien = tableModel.getValueAt(selectedRow, 0).toString();
                NhanVien nv = NhanVienDAO.findById(maNhanVien);

                if (nv != null) {
                    txtMaNhanVien.setText(nv.getMaNhanVien());
                    txtTenNhanVien.setText(nv.getTenNhanVien());
                    txtChucVu.setText(nv.getChucVu());
                    txtTenDangNhap.setText(nv.getTenDangNhap());
                    txtMatKhau.setText(""); // Never populate password
                } else {
                     showError("Không tìm thấy chi tiết nhân viên.", null);
                     clearFields();
                }
            } else {
                 clearFields();
            }
		}
	}

	private void clearFields() {
		txtMaNhanVien.setText("");
		txtTenNhanVien.setText("");
		txtChucVu.setText("");
		txtTenDangNhap.setText("");
		txtMatKhau.setText("");
		txtTimKiem.setText("");
        if (currentState == EditState.ADDING) {
             txtMaNhanVien.setText("(Tự động)");
             txtChucVu.setText("Nhân viên");
        }
	}

	private void setFieldsEditable(boolean isEditable, boolean isMaEditable) {
		txtMaNhanVien.setEditable(isMaEditable);
		txtTenNhanVien.setEditable(isEditable);
		txtChucVu.setEditable(isEditable);
		txtTenDangNhap.setEditable(isEditable);
		txtMatKhau.setEditable(isEditable);

        txtMaNhanVien.setBackground(isMaEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtTenNhanVien.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtChucVu.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtTenDangNhap.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtMatKhau.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
	}

	private void updateButtonStates() {
		boolean isIdle = (currentState == EditState.IDLE);
		boolean rowSelected = (table.getSelectedRow() != -1);

		btnThem.setEnabled(isIdle);
		btnSua.setEnabled(isIdle && rowSelected);
		btnXoa.setEnabled(isIdle && rowSelected);
		btnLuu.setEnabled(!isIdle);
		btnHuy.setEnabled(!isIdle);
		btnThoat.setEnabled(isIdle);
        btnTimKiem.setEnabled(isIdle);
        txtTimKiem.setEnabled(isIdle);

        table.setEnabled(isIdle);
        setFieldsEditable(!isIdle, currentState == EditState.ADDING && false);
	}

	private void setInitialState() {
        currentState = EditState.IDLE;
        clearFields();
        table.clearSelection();
        updateButtonStates();
    }


    private void searchNhanVien() {
        String keyword = txtTimKiem.getText().trim();
        List<NhanVien> results;
        if (keyword.isEmpty()) {
            results = NhanVienDAO.readAll();
        } else {
            results = NhanVienDAO.searchByName(keyword);
        }
        loadTableData(results);
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
		}
		setInitialState();
	}

	private void enterAddMode() {
		currentState = EditState.ADDING;
		table.clearSelection();
		clearFields();
		updateButtonStates();
		txtTenNhanVien.requestFocusInWindow();
	}

	private void enterEditMode() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một nhân viên để sửa.", table);
			return;
		}
		currentState = EditState.EDITING;
		updateButtonStates();
		txtTenNhanVien.requestFocusInWindow();
	}

	private void deleteSelectedNhanVien() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một nhân viên để xóa.", table);
			return;
		}

		String maNhanVien = tableModel.getValueAt(selectedRow, 0).toString();
		String tenNhanVien = tableModel.getValueAt(selectedRow, 1).toString();

		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa nhân viên:\nMã: " + maNhanVien + "\nTên: " + tenNhanVien + "?",
				"Xác nhận xóa Nhân Viên",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (choice == JOptionPane.YES_OPTION) {
			try {
                boolean success = NhanVienDAO.delete(maNhanVien);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData(NhanVienDAO.readAll());
                } else {
                    showError("Xóa nhân viên thất bại. Nhân viên có thể đang liên kết với dữ liệu khác (vd: Hóa Đơn).", null);
                }
            } catch (Exception ex) {
                 showError("Lỗi hệ thống khi xóa nhân viên.", ex);
            }
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
        if (chucVu.isEmpty()) { showValidationError("Chức vụ không được để trống.", txtChucVu); Arrays.fill(passwordChars, ' '); return; }
        if (tenDN.isEmpty()) { showValidationError("Tên đăng nhập không được để trống.", txtTenDangNhap); Arrays.fill(passwordChars, ' '); return; }

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
        }
    }

    private void showError(String message, Exception ex) {
         String detailedMessage = message;
         if (ex != null) {
             detailedMessage += "\nChi tiết lỗi: " + ex.getMessage();
             ex.printStackTrace();
             System.err.println(message + "\n" + ex.getMessage());
         }
         JOptionPane.showMessageDialog(this, detailedMessage, "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
    }
}
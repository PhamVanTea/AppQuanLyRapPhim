package ui;

import dao.KhachHangDAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class KhachHangUI extends JPanel {
	private JTable table;
	private JTextField txtMaKhachHang;
	private JTextField txtTenKhachHang;
	private JTextField txtSDT;
	private JTextField txtEmail;
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

	public KhachHangUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Khách Hàng", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 215, 927, 367);
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

		JPanel panelInfo = new JPanel();
		panelInfo.setLayout(null);
		panelInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Khách Hàng", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelInfo.setBounds(38, 15, 927, 146);
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

		JLabel lblMaKhachHang = new JLabel("Mã khách hàng:");
		lblMaKhachHang.setBounds(labelX1, row1Y, labelW, fieldH);
		panelInfo.add(lblMaKhachHang);

		txtMaKhachHang = new JTextField();
		txtMaKhachHang.setEditable(false);
        txtMaKhachHang.setBackground(Color.LIGHT_GRAY);
		txtMaKhachHang.setBounds(fieldX1, row1Y, fieldW, fieldH);
		panelInfo.add(txtMaKhachHang);

		JLabel lblTenKhachHang = new JLabel("Tên khách hàng:");
		lblTenKhachHang.setBounds(labelX2, row1Y, labelW, fieldH);
		panelInfo.add(lblTenKhachHang);

		txtTenKhachHang = new JTextField();
		txtTenKhachHang.setBounds(fieldX2, row1Y, fieldW, fieldH);
		panelInfo.add(txtTenKhachHang);

        JLabel lblSDT = new JLabel("Số điện thoại");
		lblSDT.setBounds(labelX1, row2Y, labelW, fieldH);
		panelInfo.add(lblSDT);

		txtSDT = new JTextField();
		txtSDT.setBounds(fieldX1, row2Y, fieldW, fieldH);
		panelInfo.add(txtSDT);

        JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(labelX2, row2Y, labelW, fieldH);
		panelInfo.add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(fieldX2, row2Y, fieldW, fieldH);
		panelInfo.add(txtEmail);

		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		panelSearch.setBounds(510, 171, 455, 34);
		add(panelSearch);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên khách hàng cần tìm");
		txtTimKiem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                    searchKhachHang();
	                }
			}
		});
		txtTimKiem.setBounds(0, 7, 274, 25);
		panelSearch.add(txtTimKiem);
		txtTimKiem.setColumns(10);

		// --- Button Creation without Helper Method ---
		btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTimKiem.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-search-20.png")));
		btnTimKiem.addActionListener(e -> searchKhachHang());
		btnTimKiem.setBounds(285, 5, 117, 27);
		panelSearch.add(btnTimKiem);

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(1, 0, 15, 0));

		btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-add-20.png")));
		btnThem.addActionListener(e -> enterAddMode());
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-edit-20.png")));
		btnSua.addActionListener(e -> enterEditMode());
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-delete-20.png")));
		btnXoa.addActionListener(e -> deleteSelectedKhachHang());
		panelChucNang.add(btnXoa);

	 	btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-save-20.png")));
		btnLuu.addActionListener(e -> saveKhachHang());
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-cancel-20.png")));
		btnHuy.addActionListener(e -> cancelEditMode());
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(KhachHangUI.class.getResource("/icons/icons8-exit-20.png")));
		btnThoat.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn đóng tab Khách Hàng?", "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                     window.dispose();
                }
            } else {
                System.exit(0);
            }
       });
		panelChucNang.add(btnThoat);

		loadTableData(KhachHangDAO.readAll());
		setInitialState();
	}

	private void populateFieldsFromSelectedRow(int selectedRow) {
		if (selectedRow != -1) {
             if (selectedRow < tableModel.getRowCount()) {
                String maKhachHang = tableModel.getValueAt(selectedRow, 0).toString();
                KhachHang kh = KhachHangDAO.findById(maKhachHang);
                if (kh != null) {
                    txtMaKhachHang.setText(kh.getMaKhachHang());
                    txtTenKhachHang.setText(kh.getTenKhachHang());
                    txtSDT.setText(kh.getSDT());
                    txtEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
                } else {
                     showError("Không tìm thấy chi tiết khách hàng.", null);
                     clearFields();
                }
             } else {
                 clearFields();
             }
		}
	}

	private void clearFields() {
		txtMaKhachHang.setText("");
		txtTenKhachHang.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		txtTimKiem.setText("");
        if (currentState == EditState.ADDING) {
             txtMaKhachHang.setText("(Tự động)");
        }
	}

	private void setFieldsEditable(boolean isEditable, boolean isMaEditable) {
		txtMaKhachHang.setEditable(isMaEditable);
		txtTenKhachHang.setEditable(isEditable);
		txtSDT.setEditable(isEditable);
		txtEmail.setEditable(isEditable);
        txtMaKhachHang.setBackground(isMaEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtTenKhachHang.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtSDT.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtEmail.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
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

    private void searchKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        List<KhachHang> results;
        if (keyword.isEmpty()) {
            results = KhachHangDAO.readAll();
        } else {
            results = KhachHangDAO.searchByName(keyword);
        }
        loadTableData(results);
    }

    private void loadTableData(List<KhachHang> list) {
		if (tableModel.getColumnCount() == 0) {
            tableModel.setColumnIdentifiers(new Object[]{
                "Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Email"
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(120);
            table.getColumnModel().getColumn(3).setPreferredWidth(200);
        }
		tableModel.setRowCount(0);
		if (list != null) {
			for (KhachHang kh : list) {
				tableModel.addRow(new Object[]{
	                kh.getMaKhachHang(),
	                kh.getTenKhachHang(),
	                kh.getSDT(),
	                kh.getEmail()
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
		txtTenKhachHang.requestFocusInWindow();
	}

	private void enterEditMode() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một khách hàng để sửa.", table);
			return;
		}
		currentState = EditState.EDITING;
		updateButtonStates();
		txtTenKhachHang.requestFocusInWindow();
	}

	private void deleteSelectedKhachHang() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một khách hàng để xóa.", table);
			return;
		}
		String maKhachHang = tableModel.getValueAt(selectedRow, 0).toString();
		String tenKhachHang = tableModel.getValueAt(selectedRow, 1).toString();
		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa khách hàng:\nMã: " + maKhachHang + "\nTên: " + tenKhachHang + "?",
				"Xác nhận xóa Khách Hàng",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			try {
                boolean success = KhachHangDAO.delete(maKhachHang);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData(KhachHangDAO.readAll());
                } else {
                    showError("Xóa khách hàng thất bại. Khách hàng có thể đang liên kết với dữ liệu khác (vd: Hóa Đơn).", null);
                }
            } catch (Exception ex) {
                 showError("Lỗi hệ thống khi xóa khách hàng.", ex);
            }
		}
	}

	private void saveKhachHang() {
		String ma;
		String ten = txtTenKhachHang.getText().trim();
		String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();
		if (ten.isEmpty()) { 
			showValidationError("Tên khách hàng không được để trống.", txtTenKhachHang); 
			throw new RuntimeException("Lỗi: Chưa nhập tên khách hàng.");
//			System.err.println("Tên khách hàng không được để trống.");
//		return; 
		}
		//Thêm biểu thức chính quy ktra nhập Tên
		if (!ten.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
		    showValidationError("Tên khách hàng chỉ chứa chữ cái, phải viết hoa chữ cái đầu mỗi từ, cho phép khoảng trắng. Ví dụ: Nguyễn Văn A", txtTenKhachHang);
		    throw new RuntimeException("Lỗi: Tên khách hàng nhập không hợp lệ!");
//		    System.err.println("Tên khách hàng chỉ chứa chữ cái, phải viết hoa chữ cái đầu mỗi từ, cho phép khoảng trắng. Ví dụ: Nguyễn Văn A");
////		    txtTenKhachHang.requestFocus();
//		    return;
		}
        if (sdt.isEmpty()) { showValidationError("Số điện thoại không được để trống.", txtSDT); 
        throw new RuntimeException("Lỗi: Chưa nhập số điện thoại");
//        return; 
        }
//        if (!sdt.matches("^\\+?[0-9. ()-]{8,}$")) {
//             showValidationError("Số điện thoại không hợp lệ.", txtSDT); return;
//        }
        //Thay đổi biểu thức chính quy kiểm tra nhập vào sdt
        if (!sdt.matches("^(02|03|05|07|08|09)\\d{8,9}$")) {
            showValidationError("Số điện thoại không hợp lệ. Phải bắt đầu bằng các đầu số hiện hành, và có 10 hoặc 11 chữ số.", txtSDT);
            throw new RuntimeException("Lỗi: Số điện thoại nhập vào không hợp lệ/ sai định dạng");
//            System.err.println("Số điện thoại không hợp lệ. Phải bắt đầu bằng các đầu số hiện hành, và có 10 hoặc 11 chữ số.");
//            return;
        }

        if (!email.isEmpty() && !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showValidationError("Địa chỉ email không hợp lệ (ví dụ: example@gmail.com).", txtEmail);
            throw new RuntimeException("Lỗi: Địa chỉ email trống hoặc nhập không hợp lệ");
//		    System.err.println("Địa chỉ email không hợp lệ (ví dụ: example@gmail.com).");
//            return;
        }

        KhachHang kh;
		boolean success = false;
		String successMessage = "";
		String errorMessage = "";
		try {
			if (currentState == EditState.ADDING) {
                ma = KhachHangDAO.generateMaKhachHang();
                kh = new KhachHang(ma, ten, sdt, email);
				success = KhachHangDAO.create(kh);
				successMessage = "Thêm khách hàng thành công!";
				errorMessage = "Thêm khách hàng thất bại. Mã, SĐT hoặc Email có thể đã tồn tại.";

			} else if (currentState == EditState.EDITING) {
                ma = txtMaKhachHang.getText().trim();
                 if (ma.isEmpty() || ma.equals("(Tự động)")) {
                     showError("Lỗi: Không xác định được Mã Khách hàng để cập nhật.", null);
                     return;
                 }
                kh = new KhachHang(ma, ten, sdt, email);
				success = KhachHangDAO.update(kh);
				successMessage = "Cập nhật khách hàng thành công!";
				errorMessage = "Cập nhật khách hàng thất bại. SĐT hoặc Email có thể bị trùng.";
			} else {
                 return;
            }

			if (success) {
				JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
				loadTableData(KhachHangDAO.readAll());
			} else {
				showValidationError(errorMessage, (currentState == EditState.ADDING) ? txtSDT : txtTenKhachHang);
                 if (currentState == EditState.ADDING && !success) {
                      txtMaKhachHang.setText("(Tự động)");
                 }
			}
		} catch (Exception ex) {
            showError("Đã xảy ra lỗi hệ thống trong quá trình lưu.", ex);
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
				if (componentToFocus instanceof JTextField) {
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
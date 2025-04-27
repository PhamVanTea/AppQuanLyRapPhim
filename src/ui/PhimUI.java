package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Window;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.PhimDAO;
import dao.TheLoaiDAO;
import entity.Phim;
import entity.TheLoai;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension; // Added import

public class PhimUI extends JPanel {
	private JTable tablePhim;
	private JTextField txtMaPhim;
	private JTextField txtTenPhim;
	private JTextField txtDaoDien;
	private JTextField txtDienVien;
	private JComboBox<TheLoai> comboBoxTheLoai;
	private JTextField txtThoiLuong;
	private JTextField txtXepHang;
	private JTextField txtMoTa;
	private DefaultTableModel tableModelPhim;

	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLuu;
	private JButton btnHuy;
	private JButton btnThoat;
	private JButton btnTimKiem;
	private JTextField txtTimKiem;

	private List<TheLoai> danhSachTheLoai;

	private enum EditState {
		IDLE, ADDING, EDITING
	}

	private EditState currentState = EditState.IDLE;

	public PhimUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680)); 

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Phim", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 275, 927, 307);
		add(panelDanhSach);
		panelDanhSach.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelDanhSach.add(scrollPane, BorderLayout.CENTER);

		tableModelPhim = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
        tableModelPhim.setColumnIdentifiers(new Object[]{
            "Mã Phim", "Tên Phim", "Đạo Diễn", "Diễn Viên", "Thể Loại", "Thời Lượng", "Xếp Hạng", "Mô Tả"
        });

		tablePhim = new JTable(tableModelPhim);
		tablePhim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhim.setRowHeight(25); // Added Row Height
        tablePhim.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12)); // Style Header
        tablePhim.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Style Content

		tablePhim.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && currentState == EditState.IDLE) {
					int selectedRow = tablePhim.getSelectedRow();
					if (selectedRow != -1) {
						populateFieldsFromSelectedRow(selectedRow);
						// btnSua.setEnabled(true); // Let updateButtonStates handle
						// btnXoa.setEnabled(true); // Let updateButtonStates handle
					} else {
						clearFields();
                        // btnSua.setEnabled(false); // Let updateButtonStates handle
                        // btnXoa.setEnabled(false); // Let updateButtonStates handle
					}
                    updateButtonStates(); // Always update on selection change when idle
				}
			}
		});
		scrollPane.setViewportView(tablePhim);

		JPanel panelThongTin = new JPanel();
		panelThongTin.setLayout(null);
		panelThongTin.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Phim", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelThongTin.setBounds(38, 21, 927, 196);
		add(panelThongTin);

        int row1Y = 25;
        int fieldWidth = 280;
		JLabel lblMaPhim = new JLabel("Mã phim");
		lblMaPhim.setBounds(25, row1Y, 80, 25);
		panelThongTin.add(lblMaPhim);
		txtMaPhim = new JTextField();
		txtMaPhim.setEditable(false);
        txtMaPhim.setBackground(Color.LIGHT_GRAY); // Màu xám- để biết ô không thể sửa
		txtMaPhim.setBounds(110, row1Y, fieldWidth, 25);
		panelThongTin.add(txtMaPhim);
		txtMaPhim.setColumns(10);

		JLabel lblTenPhim = new JLabel("Tên phim");
		lblTenPhim.setBounds(442, row1Y, 80, 25);
		panelThongTin.add(lblTenPhim);
		txtTenPhim = new JTextField();
		txtTenPhim.setBounds(580, row1Y, fieldWidth + 40, 25);
		panelThongTin.add(txtTenPhim);
		txtTenPhim.setColumns(10);

        int row2Y = row1Y + 40;
		JLabel lblDaoDien = new JLabel("Đạo diễn");
		lblDaoDien.setBounds(25, row2Y, 80, 25);
		panelThongTin.add(lblDaoDien);
		txtDaoDien = new JTextField();
		txtDaoDien.setBounds(110, row2Y, fieldWidth, 25);
		panelThongTin.add(txtDaoDien);
		txtDaoDien.setColumns(10);

		JLabel lblDienVien = new JLabel("Diễn viên");
		lblDienVien.setBounds(442, row2Y, 80, 25);
		panelThongTin.add(lblDienVien);
		txtDienVien = new JTextField();
		txtDienVien.setBounds(580, row2Y, fieldWidth + 40, 25);
		panelThongTin.add(txtDienVien);
		txtDienVien.setColumns(10);

		int row3Y = row2Y + 40;
		JLabel lblTheLoai = new JLabel("Thể loại");
		lblTheLoai.setBounds(25, row3Y, 80, 25);
		panelThongTin.add(lblTheLoai);
		comboBoxTheLoai = new JComboBox<>();
		comboBoxTheLoai.setBounds(110, row3Y, fieldWidth, 25);
		panelThongTin.add(comboBoxTheLoai);

		JLabel lblThoiLuong = new JLabel("Thời lượng (phút)");
		lblThoiLuong.setBounds(442, row3Y, 120, 25);
		panelThongTin.add(lblThoiLuong);
		txtThoiLuong = new JTextField();
		txtThoiLuong.setBounds(580, row3Y, fieldWidth + 40, 25);
		panelThongTin.add(txtThoiLuong);
		txtThoiLuong.setColumns(10);

        int row4Y = row3Y + 40;
		JLabel lblXepHang = new JLabel("Xếp hạng");
		lblXepHang.setBounds(25, row4Y, 80, 25);
		panelThongTin.add(lblXepHang);
		txtXepHang = new JTextField();
		txtXepHang.setBounds(110, row4Y, fieldWidth, 25);
		panelThongTin.add(txtXepHang);
		txtXepHang.setColumns(10);

		JLabel lblMoTa = new JLabel("Mô tả");
		lblMoTa.setBounds(442, row4Y, 80, 25);
		panelThongTin.add(lblMoTa);
		txtMoTa = new JTextField();
		txtMoTa.setBounds(580, row4Y, fieldWidth + 40, 25);
		panelThongTin.add(txtMoTa);
		txtMoTa.setColumns(10);

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(1, 0, 10, 0));

		// --- Button Creation with Icons & Style (Without Helper) ---
		btnThem = new JButton("Thêm");
		btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThem.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-add-20.png")));
		btnThem.addActionListener(e -> enterAddMode());
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
		btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSua.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-edit-20.png")));
		btnSua.addActionListener(e -> enterEditMode());
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
		btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnXoa.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-delete-20.png")));
		btnXoa.addActionListener(e -> deleteSelectedPhim());
		panelChucNang.add(btnXoa);

		btnLuu = new JButton("Lưu");
		btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLuu.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-save-20.png")));
		btnLuu.addActionListener(e -> savePhim());
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
		btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHuy.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-cancel-20.png")));
		btnHuy.addActionListener(e -> cancelEditMode());
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
		btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnThoat.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-exit-20.png")));
		btnThoat.addActionListener(e -> {
             int confirm = JOptionPane.showConfirmDialog(
                    PhimUI.this,
                    "Bạn có chắc chắn muốn thoát?",
                    "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION
                );
             if (confirm == JOptionPane.YES_OPTION) {
                 Window win = SwingUtilities.getWindowAncestor(this);
                 if (win != null) {
                    win.dispose();
                 } else {
                     System.exit(0);
                 }
             }
        });
		panelChucNang.add(btnThoat);

		JPanel panelTimKiem = new JPanel();
		panelTimKiem.setLayout(null);
		panelTimKiem.setBounds(509, 231, 455, 34);
		add(panelTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên phim cần tìm kiếm");
		txtTimKiem.setBounds(10, 7, 296, 21);
		panelTimKiem.add(txtTimKiem);
		txtTimKiem.setColumns(10);
		txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPhim();
                }
            }
        });

		btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTimKiem.setIcon(new ImageIcon(PhimUI.class.getResource("/icons/icons8-search-20.png")));
		btnTimKiem.addActionListener(e -> searchPhim());
		btnTimKiem.setBounds(316, 3, 117, 26);
		panelTimKiem.add(btnTimKiem);

		loadTheLoaiComboBox();
		loadPhimTableData(PhimDAO.readAll());
		setInitialState();
	}

	private void loadTheLoaiComboBox() {
        danhSachTheLoai = TheLoaiDAO.readAll();
        DefaultComboBoxModel<TheLoai> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(null); // Placeholder for prompt
        if (danhSachTheLoai != null && !danhSachTheLoai.isEmpty()) {
            for (TheLoai tl : danhSachTheLoai) {
                comboBoxModel.addElement(tl);
            }
        } else {
             System.err.println("Không thể tải danh sách thể loại.");
        }
        comboBoxTheLoai.setModel(comboBoxModel);
        comboBoxTheLoai.setRenderer(new DefaultListCellRenderer() {
             @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TheLoai) {
                    setText(((TheLoai) value).getTenTheLoai());
                } else if (value == null && index == -1) { // Hiển thị mục đã chọn
                    setText("-- Chọn Thể Loại --");
                } else if (value == null) { // For dropdown list item
                     setText("-- Chọn Thể Loại --");
                }
                return this;
            }
        });
         if (comboBoxModel.getSize() > 0) {
             comboBoxTheLoai.setSelectedIndex(0); // Ensure prompt is selected initially
         }
	}

    private TheLoai findTheLoaiInList(String maTheLoai) {
        if (maTheLoai == null || danhSachTheLoai == null) {
            return null;
        }
        for (TheLoai tl : danhSachTheLoai) {
            if (tl != null && maTheLoai.equals(tl.getMaTheLoai())) {
                return tl;
            }
        }
        return null;
    }

	private void populateFieldsFromSelectedRow(int selectedRow) {
		if (selectedRow != -1 && currentState == EditState.IDLE) {
             if (selectedRow < tableModelPhim.getRowCount()){
                String maPhim = tableModelPhim.getValueAt(selectedRow, 0).toString();
                Phim selectedPhim = PhimDAO.findById(maPhim);

                if (selectedPhim != null) {
                    txtMaPhim.setText(selectedPhim.getMaPhim());
                    txtTenPhim.setText(selectedPhim.getTenPhim());
                    txtDaoDien.setText(selectedPhim.getDaoDien() != null ? selectedPhim.getDaoDien() : "");
                    txtDienVien.setText(selectedPhim.getDienVien() != null ? selectedPhim.getDienVien() : "");
                    txtThoiLuong.setText(String.valueOf(selectedPhim.getThoiLuong()));
                    txtXepHang.setText(selectedPhim.getXepHang() != null ? selectedPhim.getXepHang() : "");
                    txtMoTa.setText(selectedPhim.getMoTa() != null ? selectedPhim.getMoTa() : "");
                    TheLoai tlOfPhim = selectedPhim.getTheLoai();
                    if (tlOfPhim != null) {
                         TheLoai itemToSelect = findTheLoaiInList(tlOfPhim.getMaTheLoai());
                         if (itemToSelect != null) {
                             comboBoxTheLoai.setSelectedItem(itemToSelect);
                         } else {
                             System.err.println("Thể loại '" + tlOfPhim.getTenTheLoai() + "' của phim không có trong ComboBox.");
                             comboBoxTheLoai.setSelectedIndex(0);
                         }
                    } else {
                        comboBoxTheLoai.setSelectedIndex(0);
                    }
                } else {
                    showError("Không tìm thấy thông tin chi tiết cho phim đã chọn.", null);
                    clearFields();
                }
             } else {
                 clearFields();
             }
		}
	}

	private void clearFields() {
		txtMaPhim.setText("");
		txtTenPhim.setText("");
		txtDaoDien.setText("");
        txtDienVien.setText("");
        txtThoiLuong.setText("");
        txtXepHang.setText("");
        txtMoTa.setText("");
        if (comboBoxTheLoai.getItemCount() > 0) {
            comboBoxTheLoai.setSelectedIndex(0);
        } else {
             comboBoxTheLoai.setSelectedIndex(-1);
        }
        txtTimKiem.setText("");
         if (currentState == EditState.ADDING) {
             txtMaPhim.setText("(Tự động)");
        }
	}

	private void setFieldsEditable(boolean isEditable, boolean isMaEditable) {
		txtMaPhim.setEditable(false); // MaPhim không để ng dùng chỉnh sửa
		txtTenPhim.setEditable(isEditable);
		txtDaoDien.setEditable(isEditable);
        txtDienVien.setEditable(isEditable);
        comboBoxTheLoai.setEnabled(isEditable);
        txtThoiLuong.setEditable(isEditable);
        txtXepHang.setEditable(isEditable);
        txtMoTa.setEditable(isEditable);

        txtMaPhim.setBackground(Color.LIGHT_GRAY); // Always gray
        txtTenPhim.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtDaoDien.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtDienVien.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        //comboBox không thay đổi background dựa trên trạng thái được bật như này
        txtThoiLuong.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtXepHang.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtMoTa.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
	}

	private void updateButtonStates() {
		boolean isIdle = (currentState == EditState.IDLE);
		boolean rowSelected = (tablePhim.getSelectedRow() != -1);

		btnThem.setEnabled(isIdle);
		btnSua.setEnabled(isIdle && rowSelected);
		btnXoa.setEnabled(isIdle && rowSelected);
		btnLuu.setEnabled(!isIdle);
		btnHuy.setEnabled(!isIdle);
		btnThoat.setEnabled(isIdle);
		btnTimKiem.setEnabled(isIdle);
		txtTimKiem.setEnabled(isIdle);
        tablePhim.setEnabled(isIdle);
        setFieldsEditable(!isIdle, false); // MaPhim k được chỉnh (tự phát sinh)
	}

	private void setInitialState() {
        currentState = EditState.IDLE;
        clearFields();
        tablePhim.clearSelection();
        updateButtonStates();
    }

	private void loadPhimTableData(List<Phim> list) {
        tableModelPhim.setRowCount(0);
		if (list != null) {
            for (Phim phim : list) {
                String tenTheLoai = "N/A";
                if (phim.getTheLoai() != null && phim.getTheLoai().getTenTheLoai() != null) {
                    tenTheLoai = phim.getTheLoai().getTenTheLoai();
                }
                tableModelPhim.addRow(new Object[]{
                    phim.getMaPhim(),
                    phim.getTenPhim(),
                    phim.getDaoDien(),
                    phim.getDienVien(),
                    tenTheLoai,
                    phim.getThoiLuong(),
                    phim.getXepHang(),
                    phim.getMoTa()
                });
            }
        }
        setInitialState();
	}

	private void enterAddMode() {
		currentState = EditState.ADDING;
		tablePhim.clearSelection();
		clearFields();
        updateButtonStates(); // This makes fields editable
		txtTenPhim.requestFocusInWindow(); // Focus on the first editable field
	}

	private void enterEditMode() {
		int selectedRow = tablePhim.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một phim để sửa.", tablePhim);
			return;
		}
		currentState = EditState.EDITING;
		updateButtonStates(); // This makes fields editable
		txtTenPhim.requestFocusInWindow();
	}

	private void deleteSelectedPhim() {
		int selectedRow = tablePhim.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một phim để xóa.", tablePhim);
			return;
		}
		String maPhim = tableModelPhim.getValueAt(selectedRow, 0).toString();
		String tenPhim = tableModelPhim.getValueAt(selectedRow, 1).toString();
		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa phim:\nMã: " + maPhim + "\nTên: " + tenPhim + "?\n(Lưu ý: Thao tác này không thể hoàn tác và có thể ảnh hưởng đến các suất chiếu liên quan)",
				"Xác nhận xóa phim",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			try {
                boolean success = PhimDAO.delete(maPhim);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadPhimTableData(PhimDAO.readAll());
                } else {
                    showError("Xóa phim thất bại. Vui lòng kiểm tra xem phim có đang được sử dụng trong suất chiếu nào không.", null);
                }
            } catch (Exception ex) {
                 showError("Lỗi hệ thống khi xóa phim.", ex);
            }
		}
	}

	private void savePhim() {
	    String ma;
	    String ten = txtTenPhim.getText().trim();
	    String daoDien = txtDaoDien.getText().trim();
	    String dienVien = txtDienVien.getText().trim();
	    Object selectedItem = comboBoxTheLoai.getSelectedItem();
	    String thoiLuongStr = txtThoiLuong.getText().trim();
	    String xepHang = txtXepHang.getText().trim();
	    String moTa = txtMoTa.getText().trim();

	    if (ten.isEmpty()) {
	    	showValidationError("Tên phim không để trống", txtTenPhim);
	    	throw new RuntimeException("Lỗi: Chưa nhập tên phim");
	    }
	    // Kiểm tra tên phim bằng regex
//	    if (!ten.matches("^[\\p{L}0-9 ]+$")) { 
	    if (!ten.matches("^[A-ZÀ-Ỹ][\\p{L}0-9 :_.,'\"!?()\\-]*$")) { 
	        showValidationError("Tên phim không hợp lệ. Tên phim phải bắt đầu bằng chữ in hoa, cho phép chữ cái, số, và một số ký tự đặc biệt", txtTenPhim); 
	        throw new RuntimeException("Tên phim không hợp lệ.");
//	        return; 
	    }
	    
	    if (daoDien.isEmpty()) {
	    	showValidationError("Không để trống đạo diễn", txtDaoDien);
	    	throw new RuntimeException("Lỗi: Chưa nhập đạo diễn của phim");
	    }
	    
	    if (!daoDien.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
	        showValidationError("Tên đạo diễn phải viết hoa chữ cái đầu mỗi từ, cho phép khoảng trắng.", txtDaoDien);
	        throw new RuntimeException("Lỗi: Tên đạo diễn nhập không hợp lệ!");
	    }
	    
	    if (dienVien.isEmpty()) {
	    	showValidationError("Không để trống diễn viên", txtDienVien);
	    	throw new RuntimeException("Lỗi: Chưa nhập diễn viên");
	    }
	    
//	    if (!dienVien.matches("^[A-ZÀ-Ỹ][a-zà-ỹ]*(\\s[A-ZÀ-Ỹ][a-zà-ỹ]*)*$")) {
	    if (!dienVien.matches("^[a-zA-ZÀ-Ỹà-ỹ .]+(,\\s*[a-zA-ZÀ-Ỹà-ỹ .]+)*(\\s*\\.\\.\\.)?$")) {
	        showValidationError("Tên diễn viên chỉ chứa chữ cái, phải viết hoa chữ cái đầu mỗi từ, cho phép khoảng trắng.", txtDienVien);
	        throw new RuntimeException("Lỗi: Tên diễn viên nhập không hợp lệ!");
	    }
	    
	    if (thoiLuongStr.isEmpty()) {
	        showValidationError("Thời lượng không được để trống.", txtThoiLuong);
	        throw new RuntimeException("Lỗi: Chưa nhập thời lượng.");
	    }
	    
	    int thoiLuong = Integer.parseInt(thoiLuongStr);
	    if (thoiLuong <= 0) {
	        showValidationError("Thời lượng phải lớn hơn 0.", txtThoiLuong);
	        throw new RuntimeException("Thời lượng phải lớn hơn 0.");
//	        return;
	    }
	    
	    
	    TheLoai selectedTheLoai = null;
	    if (selectedItem instanceof TheLoai) {
	        selectedTheLoai = (TheLoai) selectedItem;
	    } else {
	        showValidationError("Vui lòng chọn một thể loại hợp lệ.", comboBoxTheLoai); 
	        throw new RuntimeException("Vui lòng chọn một thể loại hợp lệ.");
//	        return;
	    }

	    // Kiểm tra thời lượng bằng regex
	    if (!thoiLuongStr.matches("^\\d+$")) { 
	        showValidationError("Thời lượng phải là một số nguyên dương.", txtThoiLuong);
	        throw new RuntimeException("Thời lượng phải là một số nguyên dương.");
//	        return; 
	    }



	    Phim phim;
	    boolean success = false;
	    String successMessage = "";
	    String errorMessage = "";

	    try {
	        if (currentState == EditState.ADDING) {
	            ma = PhimDAO.generateMaPhim(); // Generate ID using DAO
	            phim = new Phim(ma, ten, daoDien, dienVien, selectedTheLoai, thoiLuong, xepHang, moTa);
	            success = PhimDAO.create(phim);
	            successMessage = "Thêm phim thành công!";
	            errorMessage = "Thêm phim thất bại. Mã phim có thể đã tồn tại.";
	        } else if (currentState == EditState.EDITING) {
	            ma = txtMaPhim.getText().trim(); // Get existing ID from non-editable field
	            if (ma.isEmpty() || ma.equals("(Tự động)")) {
	                showError("Lỗi: Không xác định được Mã Phim để cập nhật.", null);
	                return;
	            }
	            phim = new Phim(ma, ten, daoDien, dienVien, selectedTheLoai, thoiLuong, xepHang, moTa);
	            success = PhimDAO.update(phim);
	            successMessage = "Cập nhật phim thành công!";
	            errorMessage = "Cập nhật phim thất bại.";
	        } else {
	            return;
	        }

	        if (success) {
	            JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
	            loadPhimTableData(PhimDAO.readAll());
	        } else {
	            showValidationError(errorMessage, txtTenPhim);
	            if (currentState == EditState.ADDING) {
	                txtMaPhim.setText("(Tự động)");
	            }
	        }
	    } catch (Exception ex) {
	        showError("Đã xảy ra lỗi hệ thống trong quá trình lưu.", ex);
	    }
	}


	private void cancelEditMode() {
        setInitialState();
        loadPhimTableData(PhimDAO.readAll()); // Reload all data on cancel
    }

    private void searchPhim() {
        String keyword = txtTimKiem.getText().trim();
        List<Phim> results;
        if (keyword.isEmpty()) {
            results = PhimDAO.readAll();
        } else {
            results = PhimDAO.searchByTitle(keyword);
            if (results.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Không tìm thấy phim nào với tên chứa '" + keyword + "'.", "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        loadPhimTableData(results);
        // Keep initial state logic within loadPhimTableData -> setInitialState
    }

    private void showValidationError(String message, Component componentToFocus) {
		JOptionPane.showMessageDialog(this, message, "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
		if (componentToFocus != null) {
			SwingUtilities.invokeLater(() -> {
				componentToFocus.requestFocusInWindow();
				if (componentToFocus instanceof JTextField) {
					((JTextField) componentToFocus).selectAll();
				} else if (componentToFocus instanceof JComboBox) {
                    ((JComboBox<?>) componentToFocus).showPopup();
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
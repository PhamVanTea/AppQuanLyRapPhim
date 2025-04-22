package ui;

import java.awt.Color;
import java.awt.Component; // Added import
import java.awt.Cursor; // Added import
import java.awt.Font; // Added import
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import dao.PhongChieuDAO;
import entity.PhongChieu;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension; // Added import

public class PhongChieuUI extends JPanel {
	private JTable tablePhongChieu;
	private JTextField txtMaPhongChieu;
	private JTextField txtTenPhongChieu;
	private JTextField txtSoGhe;
	private DefaultTableModel tableModelPhongChieu;
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLuu;
	private JButton btnHuy;
	private JButton btnThoat;
	private JButton btnTimKiem;
	private JTextField txtTimKiem;
	private enum EditState { IDLE, ADDING, EDITING }
	private EditState currentState = EditState.IDLE;

	public PhongChieuUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680)); // Added Size

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Phòng chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 214, 927, 368);
		add(panelDanhSach);
		panelDanhSach.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelDanhSach.add(scrollPane, BorderLayout.CENTER);

		tableModelPhongChieu = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
        tableModelPhongChieu.setColumnIdentifiers(new Object[]{
            "Mã Phòng Chiếu", "Tên Phòng Chiếu", "Số Ghế"
        });

		tablePhongChieu = new JTable(tableModelPhongChieu);
		tablePhongChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePhongChieu.setRowHeight(25); // Added Size
        tablePhongChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12)); // Added Font
        tablePhongChieu.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Added Font

		tablePhongChieu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && currentState == EditState.IDLE) {
					int selectedRow = tablePhongChieu.getSelectedRow();
					if (selectedRow != -1) {
						populateFieldsFromSelectedRow(selectedRow);
					} else {
						clearFields();
					}
                    updateButtonStates(); // Update buttons after selection handling
				}
			}
		});
		scrollPane.setViewportView(tablePhongChieu);

		JPanel panelThongTin = new JPanel();
		panelThongTin.setLayout(null);
		panelThongTin.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Phòng chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelThongTin.setBounds(38, 21, 927, 142);
		add(panelThongTin);

        int fieldWidth = 295;
        int labelWidth = 110; // Adjusted to fit "Mã phòng chiếu"

		JLabel lblMaPhongChieu = new JLabel("Mã phòng chiếu");
		lblMaPhongChieu.setBounds(25, 35, labelWidth, 25);
		panelThongTin.add(lblMaPhongChieu);
		txtMaPhongChieu = new JTextField();
		txtMaPhongChieu.setEditable(false);
        txtMaPhongChieu.setBackground(Color.LIGHT_GRAY);
		txtMaPhongChieu.setBounds(140, 35, fieldWidth, 25);
		panelThongTin.add(txtMaPhongChieu);
		txtMaPhongChieu.setColumns(10);

		JLabel lblTenPhongChieu = new JLabel("Tên phòng chiếu");
		lblTenPhongChieu.setBounds(476, 35, labelWidth, 25); // Adjusted label width
		panelThongTin.add(lblTenPhongChieu);
		txtTenPhongChieu = new JTextField();
		txtTenPhongChieu.setBounds(600, 35, fieldWidth, 25); // Adjusted x position
		panelThongTin.add(txtTenPhongChieu);
		txtTenPhongChieu.setColumns(10);

		JLabel lblSoGhe = new JLabel("Số ghế");
		lblSoGhe.setBounds(25, 80, labelWidth, 25);
		panelThongTin.add(lblSoGhe);
		txtSoGhe = new JTextField();
		txtSoGhe.setBounds(140, 80, fieldWidth, 25);
		panelThongTin.add(txtSoGhe);
		txtSoGhe.setColumns(10);

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(1, 0, 15, 0)); // Increased gap

		btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-add-20.png")));
		btnThem.addActionListener(e -> enterAddMode());
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-edit-20.png")));
		btnSua.addActionListener(e -> enterEditMode());
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-delete-20.png")));
		btnXoa.addActionListener(e -> deleteSelectedPhongChieu());
		panelChucNang.add(btnXoa);

		btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-save-20.png")));
		btnLuu.addActionListener(e -> savePhongChieu());
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-cancel-20.png")));
		btnHuy.addActionListener(e -> cancelEditMode());
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-exit-20.png")));
		btnThoat.addActionListener(e -> {
             int confirm = JOptionPane.showConfirmDialog(
                    PhongChieuUI.this,
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
		panelTimKiem.setBounds(503, 170, 455, 34);
		add(panelTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên phòng chiếu để tìm kiếm");
		txtTimKiem.setColumns(10);
		txtTimKiem.setBounds(10, 7, 296, 21);
		panelTimKiem.add(txtTimKiem);

		btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTimKiem.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-search-20.png")));
		btnTimKiem.addActionListener(e -> searchPhongChieu());
		btnTimKiem.setBounds(316, 3, 117, 26);
		panelTimKiem.add(btnTimKiem);

		txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPhongChieu();
                }
            }
        });

		loadPhongChieuTableData(PhongChieuDAO.readAll());
		setInitialState();
	}

	private void populateFieldsFromSelectedRow(int selectedRow) {
		if (selectedRow != -1 && currentState == EditState.IDLE) {
            if (selectedRow < tableModelPhongChieu.getRowCount()) { // Check bounds
                String maPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 0).toString();
                String tenPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 1).toString();
                String soGhe = tableModelPhongChieu.getValueAt(selectedRow, 2).toString();

                txtMaPhongChieu.setText(maPhongChieu);
                txtTenPhongChieu.setText(tenPhongChieu);
                txtSoGhe.setText(soGhe);
            } else {
                clearFields();
            }
		}
	}

	private void clearFields() {
		txtMaPhongChieu.setText("");
		txtTenPhongChieu.setText("");
		txtSoGhe.setText("");
		txtTimKiem.setText("");
        if (currentState == EditState.ADDING) {
            txtMaPhongChieu.setText("(Tự động)");
        }
	}

	private void setFieldsEditable(boolean isEditable, boolean isMaEditable_IGNORED) {
		txtMaPhongChieu.setEditable(false); // MaPhong never editable
		txtTenPhongChieu.setEditable(isEditable);
		txtSoGhe.setEditable(isEditable);

        txtMaPhongChieu.setBackground(Color.LIGHT_GRAY); // Always non-editable look
        txtTenPhongChieu.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtSoGhe.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
	}

	private void updateButtonStates() {
		boolean isIdle = (currentState == EditState.IDLE);
		boolean rowSelected = (tablePhongChieu.getSelectedRow() != -1);

		btnThem.setEnabled(isIdle);
		btnSua.setEnabled(isIdle && rowSelected);
		btnXoa.setEnabled(isIdle && rowSelected);
		btnLuu.setEnabled(!isIdle);
		btnHuy.setEnabled(!isIdle);
		btnThoat.setEnabled(isIdle);
		btnTimKiem.setEnabled(isIdle);
		txtTimKiem.setEnabled(isIdle);

        tablePhongChieu.setEnabled(isIdle);
        setFieldsEditable(!isIdle, false); // MaPhong never editable

        if (!isIdle) {
             tablePhongChieu.clearSelection();
        }
	}

	private void setInitialState() {
        currentState = EditState.IDLE;
        clearFields();
        tablePhongChieu.clearSelection();
        updateButtonStates();
    }

	private void loadPhongChieuTableData(List<PhongChieu> list) {
        tableModelPhongChieu.setRowCount(0);
		if (list != null) {
			for (PhongChieu pc : list) {
				tableModelPhongChieu.addRow(new Object[]{
	                pc.getMaPhong(),
	                pc.getTenPhong(),
	                pc.getSoGhe()
	            });
			}
		}
		setInitialState(); // Reset state after loading
	}

	private void enterAddMode() {
		currentState = EditState.ADDING;
		tablePhongChieu.clearSelection();
		clearFields();
        updateButtonStates(); // Makes fields editable
		txtTenPhongChieu.requestFocusInWindow(); // Focus on TenPhong
	}

	private void enterEditMode() {
		int selectedRow = tablePhongChieu.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một phòng chiếu để sửa.", tablePhongChieu);
			return;
		}
		currentState = EditState.EDITING;
		updateButtonStates(); // Makes fields editable
		txtTenPhongChieu.requestFocusInWindow();
	}

	private void deleteSelectedPhongChieu() {
		int selectedRow = tablePhongChieu.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một phòng chiếu để xóa.", tablePhongChieu);
			return;
		}
		String maPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 0).toString();
		String tenPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 1).toString();
		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa phòng chiếu:\nMã: " + maPhongChieu + "\nTên: " + tenPhongChieu + "?\n(Lưu ý: Hành động này không thể hoàn tác và có thể ảnh hưởng đến lịch chiếu và ghế!)",
				"Xác nhận xóa phòng chiếu",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			try {
                boolean success = PhongChieuDAO.delete(maPhongChieu);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadPhongChieuTableData(PhongChieuDAO.readAll());
                } else {
                    showError("Xóa phòng chiếu thất bại. Vui lòng kiểm tra xem phòng có đang được sử dụng không.", null);
                }
            } catch(Exception ex) {
                 showError("Lỗi hệ thống khi xóa phòng chiếu.", ex);
            }
		}
	}

	private void savePhongChieu() {
        String ma;
		String ten = txtTenPhongChieu.getText().trim();
		String soGheStr = txtSoGhe.getText().trim();

		if (ten.isEmpty()) { 
			showValidationError("Tên phòng chiếu không được để trống.", txtTenPhongChieu); 
			System.err.println("");
		return; 
		}
		if (soGheStr.isEmpty()) { showValidationError("Số ghế không được để trống.", txtSoGhe); return; }

        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) { 
        	showValidationError("Số ghế phải là một số nguyên dương hợp lệ.", txtSoGhe); 
        	System.err.println("");
        return; 
        }

		PhongChieu phongChieu;
		boolean success = false;
		String successMessage = "";
        String errorMessage = "";

		try {
			if (currentState == EditState.ADDING) {
                ma = PhongChieuDAO.generateMaPhongChieu(); // Generate ID
                phongChieu = new PhongChieu(ma, ten, soGhe);
				success = PhongChieuDAO.create(phongChieu);
				successMessage = "Thêm phòng chiếu thành công!";
                errorMessage = "Thêm phòng chiếu thất bại. Mã phòng có thể đã tồn tại.";
			} else if (currentState == EditState.EDITING) {
                 ma = txtMaPhongChieu.getText().trim(); // Get existing ID
                 if(ma.isEmpty() || ma.equals("(Tự động)")) {
                    showError("Lỗi: Không xác định được Mã phòng để cập nhật.", null);
                    return;
                 }
                 phongChieu = new PhongChieu(ma, ten, soGhe);
				 success = PhongChieuDAO.update(phongChieu);
				 successMessage = "Cập nhật phòng chiếu thành công!";
                 errorMessage = "Cập nhật phòng chiếu thất bại.";
			} else {
                return; // Should not happen
            }

			if (success) {
				JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
				loadPhongChieuTableData(PhongChieuDAO.readAll());
			} else {
                 showValidationError(errorMessage, txtTenPhongChieu);
                 if (currentState == EditState.ADDING) {
                     txtMaPhongChieu.setText("(Tự động)");
                 }
			}
		} catch (Exception ex) {
            showError("Đã xảy ra lỗi không mong muốn khi lưu.", ex);
		}
	}

	private void cancelEditMode() {
        setInitialState();
        loadPhongChieuTableData(PhongChieuDAO.readAll());
    }

	private void searchPhongChieu() {
		String keyword = txtTimKiem.getText().trim();
		List<PhongChieu> results;
		if (keyword.isEmpty()) {
			results = PhongChieuDAO.readAll();
		} else {
			results = PhongChieuDAO.searchByName(keyword);
			if (results.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy phòng chiếu nào có tên chứa '" + keyword + "'", "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		loadPhongChieuTableData(results);
		// setInitialState is called by loadPhongChieuTableData
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
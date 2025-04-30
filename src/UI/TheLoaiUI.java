package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import DAO.TheLoaiDAO;
import Entity.TheLoai;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class TheLoaiUI extends JPanel implements ActionListener {
	private JTable table;
	private JTextField txtMaTheLoai;
	private JTextField txtTenTheLoai;
	private JTextField txtMoTa;
	private DefaultTableModel tableModel;
	private JButton btnThem;
	private JButton btnSua;
	private JButton btnXoa;
	private JButton btnLuu;
	private JButton btnHuy;
	private JButton btnThoat;
	private JButton btnTimKiem;
	private enum EditState { IDLE, ADDING, EDITING }
	private EditState currentState = EditState.IDLE;
	private JTextField txtTimKiem;

	//Giao diện thể loại
	public TheLoaiUI() {
		setLayout(null);
		setPreferredSize(new Dimension(1000, 680));

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Thể loại phim", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 217, 927, 365);
		add(panelDanhSach);
		panelDanhSach.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelDanhSach.add(scrollPane);

		table = new JTable();
		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
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

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Thể loại phim", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(38, 21, 927, 142);
		add(panel_1);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setBounds(27, 33, 437, 34);
		panel_1.add(panel_1_1);

		JLabel lblNewLabel_1 = new JLabel("Mã thể loại");
		lblNewLabel_1.setBounds(23, 10, 99, 13);
		panel_1_1.add(lblNewLabel_1);

		txtMaTheLoai = new JTextField();
		txtMaTheLoai.setEditable(false);
		txtMaTheLoai.setBackground(Color.LIGHT_GRAY);
		txtMaTheLoai.setColumns(10);
		txtMaTheLoai.setBounds(132, 7, 295, 19);
		panel_1_1.add(txtMaTheLoai);

		JPanel panel_1_1_1 = new JPanel();
		panel_1_1_1.setLayout(null);
		panel_1_1_1.setBounds(474, 33, 430, 34);
		panel_1.add(panel_1_1_1);

		JLabel lblNewLabel_1_1 = new JLabel("Tên thể loại");
		lblNewLabel_1_1.setBounds(23, 10, 122, 13);
		panel_1_1_1.add(lblNewLabel_1_1);

		txtTenTheLoai = new JTextField();
		txtTenTheLoai.setEditable(false);
		txtTenTheLoai.setBackground(Color.LIGHT_GRAY);
		txtTenTheLoai.setColumns(10);
		txtTenTheLoai.setBounds(106, 7, 314, 19);
		panel_1_1_1.add(txtTenTheLoai);

		JPanel panel_1_1_3 = new JPanel();
		panel_1_1_3.setLayout(null);
		panel_1_1_3.setBounds(27, 77, 877, 34);
		panel_1.add(panel_1_1_3);

		JLabel lblNewLabel_1_3 = new JLabel("Mô tả");
		lblNewLabel_1_3.setBounds(23, 10, 99, 13);
		panel_1_1_3.add(lblNewLabel_1_3);

		txtMoTa = new JTextField();
		txtMoTa.setEditable(false);
		txtMoTa.setBackground(Color.LIGHT_GRAY);
		txtMoTa.setColumns(10);
		txtMoTa.setBounds(132, 7, 735, 19);
		panel_1_1_3.add(txtMoTa);

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(1, 0, 10, 0));

		btnThem = new JButton("Thêm");
		btnThem.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-add-20.png")));
//		btnThem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				enterAddMode();
//			}
//		});
		btnThem.addActionListener(this);
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
		btnSua.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-edit-20.png")));
		btnSua.setEnabled(false);
//		btnSua.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				enterEditMode();
//			}
//		});
		btnSua.addActionListener(this);
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
		btnXoa.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-delete-20.png")));
		btnXoa.setEnabled(false);
//		btnXoa.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				deleteSelectedTheLoai();
//			}
//		});
		btnXoa.addActionListener(this);
		panelChucNang.add(btnXoa);

		btnLuu = new JButton("Lưu");
		btnLuu.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-save-20.png")));
		btnLuu.setEnabled(false);
//		btnLuu.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				saveTheLoai();
//			}
//		});
		btnLuu.addActionListener(this);
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
		btnHuy.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-cancel-20.png")));
		btnHuy.setEnabled(false);
//		btnHuy.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				cancelEditMode();
//			}
//		});
		btnHuy.addActionListener(this);
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
		btnThoat.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-exit-20.png")));
//		btnThoat.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Window window = SwingUtilities.getWindowAncestor(TheLoaiUI.this);
//				if (window != null) {
//					int confirm = JOptionPane.showConfirmDialog(TheLoaiUI.this,
//							"Bạn có chắc chắn muốn đóng tab Thể Loại?", "Xác nhận thoát",
//							JOptionPane.YES_NO_OPTION);
//					if (confirm == JOptionPane.YES_OPTION) {
//						window.dispose();
//					}
//				} else {
//					System.exit(0);
//				}
//			}
//		});
		btnThoat.addActionListener(this);
		panelChucNang.add(btnThoat);

		JPanel panel_1_1_1_1 = new JPanel();
		panel_1_1_1_1.setLayout(null);
		panel_1_1_1_1.setBounds(510, 173, 455, 34);
		add(panel_1_1_1_1);

		btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setIcon(new ImageIcon(TheLoaiUI.class.getResource("/icons/icons8-search-20.png")));
//		btnTimKiem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				searchTheLoai();
//			}
//		});
		btnTimKiem.addActionListener(this);
		btnTimKiem.setBounds(316, 3, 117, 26);
		panel_1_1_1_1.add(btnTimKiem);
		btnTimKiem.setEnabled(true);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên thể loại cần tìm kiếm");
		txtTimKiem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					searchTheLoai();
				}
			}
		});
		txtTimKiem.setBounds(32, 7, 274, 19);
		panel_1_1_1_1.add(txtTimKiem);
		txtTimKiem.setColumns(10);

		loadTableData(TheLoaiDAO.readAll());
		setInitialState();
	}

	//HT
	//Hiển thị thông tin thể loại được chọn từ dòng lên ô nhập
	private void populateFieldsFromSelectedRow(int selectedRow) {
		if (selectedRow != -1) {
			if (selectedRow < tableModel.getRowCount()) {
				String maTheLoai = tableModel.getValueAt(selectedRow, 0).toString();
				String tenTheLoai = tableModel.getValueAt(selectedRow, 1).toString();
				Object moTaObj = tableModel.getValueAt(selectedRow, 2);
				String moTa = (moTaObj != null) ? moTaObj.toString() : "";

				txtMaTheLoai.setText(maTheLoai);
				txtTenTheLoai.setText(tenTheLoai);
				txtMoTa.setText(moTa);
			} else {
				clearFields();
			}
		}
	}
	
	//HT
	//Xóa trắng (Xóa nd ô nhập liệu)
	private void clearFields() {
		txtMaTheLoai.setText("");
		txtTenTheLoai.setText("");
		txtMoTa.setText("");
		txtTimKiem.setText("");
		if (currentState == EditState.ADDING) {
			txtMaTheLoai.setText("(Tự động)");
		}
	}

	//HT
	//Cài đặt trạng thái có thể chỉnh sửa hay k cho các ô nhập
	private void setFieldsEditable(boolean isEditable, boolean isMaEditable_IGNORED) {
		txtMaTheLoai.setEditable(false);
		txtTenTheLoai.setEditable(isEditable);
		txtMoTa.setEditable(isEditable);
		txtMaTheLoai.setBackground(Color.LIGHT_GRAY);
		txtTenTheLoai.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
		txtMoTa.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
	}
	
	//HT
	//Cập nhật trạng thái các btn, trường nhập liệu theo trạng thái chỉnh sửa
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
		setFieldsEditable(!isIdle, false);
	}

	//HT
	//Thiết lập trạng thái ban đầu cho GDien
	private void setInitialState() {
		currentState = EditState.IDLE;
		clearFields();
		table.clearSelection();
		updateButtonStates();
	}

	private void searchTheLoai() {
		String keyword = txtTimKiem.getText().trim();
		List<TheLoai> results;
		if (keyword.isEmpty()) {
			results = TheLoaiDAO.readAll();
		} else {
			results = TheLoaiDAO.searchByName(keyword);
		}
		loadTableData(results);
	}

	//HT
	//Nạp dl vào bảng từ ds thể loại
	private void loadTableData(List<TheLoai> list) {
		if (tableModel.getColumnCount() == 0) {
			tableModel.setColumnIdentifiers(new Object[]{"Mã Thể Loại", "Tên Thể Loại", "Mô Tả"});
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(2).setPreferredWidth(400);
		}
		tableModel.setRowCount(0);
		if (list != null) {
			for (TheLoai theLoai : list) {
				tableModel.addRow(new Object[]{
					theLoai.getMaTheLoai(),
					theLoai.getTenTheLoai(),
					theLoai.getMoTa()
				});
			}
		}
		setInitialState();
	}

	//CS
	//Vào chế độ thêm mới thể loại
	private void enterAddMode() {
		currentState = EditState.ADDING;
		table.clearSelection();
		clearFields();
		updateButtonStates();
		txtTenTheLoai.requestFocusInWindow();
	}

	//CS
	//Vào chế độ chỉnh sửa tt
	private void enterEditMode() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một thể loại để sửa.", table);
			return;
		}
		currentState = EditState.EDITING;
		updateButtonStates();
		txtTenTheLoai.requestFocusInWindow();
	}

	//CS
	//Xóa thể loại được chọn
	private void deleteSelectedTheLoai() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một thể loại để xóa.", table);
			return;
		}
		String maTheLoai = tableModel.getValueAt(selectedRow, 0).toString();
		String tenTheLoai = tableModel.getValueAt(selectedRow, 1).toString();
		//Cảnh báo xóa
		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa thể loại:\nMã: " + maTheLoai + "\nTên: " + tenTheLoai + "?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			try {
				boolean success = TheLoaiDAO.delete(maTheLoai);
				if (success) {
					JOptionPane.showMessageDialog(this, "Xóa thể loại thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
					loadTableData(TheLoaiDAO.readAll());
				} else {
					showError("Xóa thể loại thất bại. Thể loại có thể đang được sử dụng bởi một Phim.", null);
				}
			} catch (Exception ex) {
				showError("Lỗi hệ thống khi xóa thể loại.", ex);
			}
		}
	}

	//CS
	//Lưu thể loại sau khi được thêm/chỉnh sửa
	private void saveTheLoai() {
		String ma;
		String ten = txtTenTheLoai.getText().trim();
		String moTa = txtMoTa.getText().trim();
		if (ten.isEmpty()) {
			showValidationError("Tên thể loại không được để trống.", txtTenTheLoai);
			throw new RuntimeException("Tên thể loại không được để trống.");
//			return;
		}
		
		//Mô tả có thể để trống
//		if (moTa.isEmpty()) {
//			showValidationError("Mô tả không được để trống.", txtTenTheLoai);
//			throw new RuntimeException("Mô tả không được để trống.");
////			return;
//		}
		TheLoai theLoai;
		boolean success = false;
		String successMessage = "";
		String errorMessage = "";
		try {
			if (currentState == EditState.ADDING) {
				ma = TheLoaiDAO.generateMaTheLoai();
				theLoai = new TheLoai(ma, ten, moTa);
				success = TheLoaiDAO.create(theLoai);
				successMessage = "Thêm thể loại thành công!";
				errorMessage = "Thêm thể loại thất bại. Mã thể loại có thể đã tồn tại hoặc có lỗi khác.";
			} else if (currentState == EditState.EDITING) {
				ma = txtMaTheLoai.getText().trim();
				if (ma.isEmpty() || ma.equals("(Tự động)")) {
					showError("Lỗi: Không xác định được Mã Thể Loại để cập nhật.", null);
					return;
				}
				theLoai = new TheLoai(ma, ten, moTa);
				success = TheLoaiDAO.update(theLoai);
				successMessage = "Cập nhật thể loại thành công!";
				errorMessage = "Cập nhật thể loại thất bại. Có lỗi xảy ra.";
			} else {
				return;
			}
			if (success) {
				JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
				loadTableData(TheLoaiDAO.readAll());
			} else {
				showValidationError(errorMessage, txtTenTheLoai);
				if (currentState == EditState.ADDING) {
					txtMaTheLoai.setText("(Tự động)");
				}
			}
		} catch (Exception ex) {
			showError("Đã xảy ra lỗi trong quá trình lưu.", ex);
		}
	}

	//Hủy thao tác lúc đang Thêm/Sửa
	private void cancelEditMode() {
		setInitialState();	//Quay lại trạng thái ban đầu
	}

	//HT lỗi
	//Hiển thị tb lỗi Dữ liệu k hợp lệ - Focus vào trường lỗi
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

	//HT Lỗi
	//Hiển thị tb lỗi hệ thống, kèm theo chi tiết ngoại lệ
	private void showError(String message, Exception ex) {
		String detailedMessage = message;
		if (ex != null) {
			detailedMessage += "\nChi tiết lỗi: " + ex.getMessage();
			ex.printStackTrace();
			System.err.println(message + "\n" + ex.getMessage());
		}
		JOptionPane.showMessageDialog(this, detailedMessage, "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
	}

	
	//Xử lý sk
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
	    if (o.equals(btnThem)) {
	        enterAddMode();
	    } else if (o.equals(btnSua)) {
	        enterEditMode();
	    } else if (o.equals(btnXoa)) {
	        deleteSelectedTheLoai();
	    } else if (o.equals(btnLuu)) {
	        saveTheLoai();
	    } else if (o.equals(btnHuy)) {
	        cancelEditMode();
	    } else if (o.equals(btnThoat)) {
	        Window window = SwingUtilities.getWindowAncestor(TheLoaiUI.this);
	        if (window != null) {
	            int confirm = JOptionPane.showConfirmDialog(TheLoaiUI.this,
	                    "Bạn có chắc chắn muốn đóng tab Thể Loại?", "Xác nhận thoát",
	                    JOptionPane.YES_NO_OPTION);
	            if (confirm == JOptionPane.YES_OPTION) {
	                window.dispose();
	            }
	        } else {
	            System.exit(0);
	        }
	    } else if (o.equals(btnTimKiem)) {
	        searchTheLoai();
	    }
		
	}
}
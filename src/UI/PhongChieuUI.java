package UI;

import java.awt.Color;
import java.awt.Component; // Added import
import java.awt.Cursor; // Added import
import java.awt.Font; // Added import
import java.awt.Window;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import DAO.PhongChieuDAO;
import Entity.PhongChieu;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension; // Added import

public class PhongChieuUI extends JPanel implements ActionListener {
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
	private EditState trangThaiHienTai = EditState.IDLE;

	//Giao diện Phòng Chiếu
	public PhongChieuUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Phòng chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 214, 927, 368);
		add(panelDanhSach);
		panelDanhSach.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelDanhSach.add(scrollPane, BorderLayout.CENTER);
		
		//mô hình dl cho bảng
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
				if (!e.getValueIsAdjusting() && trangThaiHienTai == EditState.IDLE) {
					int selectedRow = tablePhongChieu.getSelectedRow();
					if (selectedRow != -1) {
						capNhatFieldTuDongDuocChon(selectedRow);
					} else {
						clearFields();
					}
                    capNhatTrangThaiNut(); // Cập nhật trạng thái nút
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
		lblTenPhongChieu.setBounds(476, 35, labelWidth, 25); // Điều chỉnh label width
		panelThongTin.add(lblTenPhongChieu);
		txtTenPhongChieu = new JTextField();
		txtTenPhongChieu.setBounds(600, 35, fieldWidth, 25); // Chỉnh x position
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
		panelChucNang.setLayout(new GridLayout(1, 0, 15, 0)); // Tăng k/c các nút

		btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-add-20.png")));
        btnThem.addActionListener(this);
		panelChucNang.add(btnThem);

		btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-edit-20.png")));
        btnSua.addActionListener(this);
		panelChucNang.add(btnSua);

		btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-delete-20.png")));
        btnXoa.addActionListener(this);
		panelChucNang.add(btnXoa);

		btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-save-20.png")));
        btnLuu.addActionListener(this);
		panelChucNang.add(btnLuu);

		btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-cancel-20.png")));
		btnHuy.addActionListener(this);
		panelChucNang.add(btnHuy);

		btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-exit-20.png")));
        btnThoat.addActionListener(this);
		panelChucNang.add(btnThoat);

		JPanel panelTimKiem = new JPanel();
		panelTimKiem.setLayout(null);
		panelTimKiem.setBounds(503, 170, 455, 34);
		add(panelTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập tên phòng chiếu cần tìm kiếm");
		txtTimKiem.setColumns(10);
		txtTimKiem.setBounds(10, 7, 296, 21);
		panelTimKiem.add(txtTimKiem);

		btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTimKiem.setIcon(new ImageIcon(PhongChieuUI.class.getResource("/icons/icons8-search-20.png")));
//		btnTimKiem.addActionListener(e -> timPhongChieu());
        btnTimKiem.addActionListener(this);
		btnTimKiem.setBounds(316, 3, 117, 26);
		panelTimKiem.add(btnTimKiem);

		txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    timPhongChieu();
                }
            }
        });

		loadDuLieuBangPhongChieu(PhongChieuDAO.readAll());
		setInitialState();
	}

	//GD
	// Hiển thị thông tin phòng chiếu lên các ô khi chọn dòng trong bảng
	private void capNhatFieldTuDongDuocChon(int selectedRow) {
		if (selectedRow != -1 && trangThaiHienTai == EditState.IDLE) {
            if (selectedRow < tableModelPhongChieu.getRowCount()) { // Check selectedRow thuộc phạm vi hợp lệ của bảng
                String maPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 0).toString();	//lấy dl
                String tenPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 1).toString();
                String soGhe = tableModelPhongChieu.getValueAt(selectedRow, 2).toString();

                txtMaPhongChieu.setText(maPhongChieu);	//điền vào field trên GD
                txtTenPhongChieu.setText(tenPhongChieu);
                txtSoGhe.setText(soGhe);
            } else {
                clearFields();
            }
		}
	}

	//GD
	//Xóa nd các ô nhập liệu, ô tìm kiếm
	private void clearFields() {
		txtMaPhongChieu.setText("");
		txtTenPhongChieu.setText("");
		txtSoGhe.setText("");
		txtTimKiem.setText("");
        if (trangThaiHienTai == EditState.ADDING) {
            txtMaPhongChieu.setText("(Tự động)");
        }
	}

	//GD
	//Trạng thái chỉnh sửa các ô nhập
	private void setTrangThaiChinhSua(boolean isEditable, boolean isMaEditable_IGNORED) {
		txtMaPhongChieu.setEditable(false); // MaPhong không cho phép chỉnh sửa
		txtTenPhongChieu.setEditable(isEditable);
		txtSoGhe.setEditable(isEditable);

        txtMaPhongChieu.setBackground(Color.LIGHT_GRAY); // Hiển thị màu nền xám - k cho phép chỉnh sửa
        txtTenPhongChieu.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtSoGhe.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
	}

	//DK trạng thái
	//Cập nhật trạng thái các nút, trường nhập theo trạng thái hiện tại
	private void capNhatTrangThaiNut() {
		boolean isIdle = (trangThaiHienTai == EditState.IDLE);	//isIdle = true ở chế độ nghỉ, false khi ở chế độ thao tác (thêm, sửa, xóa).
		boolean rowSelected = (tablePhongChieu.getSelectedRow() != -1);

		btnThem.setEnabled(isIdle);
		btnSua.setEnabled(isIdle && rowSelected);	//nút sửa và xóa kích hoạt khi đang từ chế độ nghỉ và đã chọn 1 dòng
		btnXoa.setEnabled(isIdle && rowSelected);
		btnLuu.setEnabled(!isIdle);
		btnHuy.setEnabled(!isIdle);
		btnThoat.setEnabled(isIdle);
		btnTimKiem.setEnabled(isIdle);
		txtTimKiem.setEnabled(isIdle);
        tablePhongChieu.setEnabled(isIdle);
        setTrangThaiChinhSua(!isIdle, false); // MaPhong không chỉnh sửa

        if (!isIdle) {
             tablePhongChieu.clearSelection();
        }
	}

	//DK trạng thái
	//Thiết lập trạng thái ban đầu		//đặt về chế độ nghỉ, xóa dl trên field, bỏ chọn các dòng trong table, cập nhật lại nút
	private void setInitialState() {
        trangThaiHienTai = EditState.IDLE;	
        clearFields();
        tablePhongChieu.clearSelection();
        capNhatTrangThaiNut();
    }

	//DL Bảng
	//Nạp ds Phòng chiếu vào bảng
	private void loadDuLieuBangPhongChieu(List<PhongChieu> list) {
        tableModelPhongChieu.setRowCount(0);	//Đặt dòng về 0, xóa dl hiện có trong bảng
		if (list != null) {
			for (PhongChieu pc : list) {
				tableModelPhongChieu.addRow(new Object[]{
	                pc.getMaPhong(),
	                pc.getTenPhong(),
	                pc.getSoGhe()
	            });
			}
		}
		setInitialState(); // Đặt lại trạng thái
	}

	//Chức năng
	//Vào chế độ thêm mới
	private void vaoCheDoThem() {
		trangThaiHienTai = EditState.ADDING;
		tablePhongChieu.clearSelection();
		clearFields();
        capNhatTrangThaiNut(); // Cho phép chỉnh sửa các trường
		txtTenPhongChieu.requestFocusInWindow(); // Focus TenPhong
	}

	//Chức năng
	//Vào chế độ chỉnh sửa được chọn
	private void vaoCheDoChinhSua() {
		int selectedRow = tablePhongChieu.getSelectedRow();
		if (selectedRow == -1) {	//-1 : gtri mặc định khi k có dòng đc chọn
			showValidationError("Vui lòng chọn một phòng chiếu để sửa.", tablePhongChieu);
			return;
		}
		trangThaiHienTai = EditState.EDITING;
		capNhatTrangThaiNut(); // fields editable
		txtTenPhongChieu.requestFocusInWindow();
	}

	//Chức năng
	//Xóa phòng phiếu đang được chọn
	private void xoaPhongChieuDuocChon() {
		int selectedRow = tablePhongChieu.getSelectedRow();
		if (selectedRow == -1) {
			showValidationError("Vui lòng chọn một phòng chiếu để xóa.", tablePhongChieu);
			return;
		}
		String maPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 0).toString();	//lấy mã, tên của phòng được chọn (Cho thông báo)
		String tenPhongChieu = tableModelPhongChieu.getValueAt(selectedRow, 1).toString();
		//Xác nhận lại
		int choice = JOptionPane.showConfirmDialog(this,
				"Bạn có chắc chắn muốn xóa phòng chiếu:\nMã: " + maPhongChieu + "\nTên: " + tenPhongChieu + "?\n(Lưu ý: Hành động này không thể hoàn tác và có thể ảnh hưởng đến lịch chiếu và ghế!)",
				"Xác nhận xóa phòng chiếu",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
		if (choice == JOptionPane.YES_OPTION) {
			try {
                boolean success = PhongChieuDAO.xoa(maPhongChieu);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa phòng chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDuLieuBangPhongChieu(PhongChieuDAO.readAll());
                } else {
                    showError("Xóa phòng chiếu thất bại. Vui lòng kiểm tra xem phòng có đang được sử dụng không.", null);
                }
            } catch(Exception ex) {
                 showError("Lỗi hệ thống khi xóa phòng chiếu.", ex);
            }
		}
	}

	//Chức năng
	//Lưu dl khi thêm/sửa phòng chiếu
	private void savePhongChieu() {
        String ma;
		String ten = txtTenPhongChieu.getText().trim();
		String soGheStr = txtSoGhe.getText().trim();

		if (ten.isEmpty()) { 
			showValidationError("Tên phòng chiếu không được để trống.", txtTenPhongChieu); 
			throw new RuntimeException("Tên phòng chiếu không được để trống.");
//		return; 
		}
		if (!ten.matches("^[A-ZÀ-Ỹ][a-zà-ỹA-ZÀ-Ỹ0-9]*(\\s[A-ZÀ-Ỹ0-9][a-zà-ỹA-ZÀ-Ỹ0-9]*)*$")) {
			showValidationError("Tên phòng chiếu không hợp lệ. Mỗi từ bắt đầu bằng chữ cái in hoa, cho phép chữ số và khoảng trắng.",txtTenPhongChieu);
			//VD hợp lệ: Phòng
			throw new RuntimeException("Tên phòng chiếu nhập vào không hợp lệ.");
		}
		if (soGheStr.isEmpty()) { 
			showValidationError("Số ghế không được để trống.", txtSoGhe);
			throw new RuntimeException("Số ghế không được để trống.");
//		return;
		}

        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) { throw new NumberFormatException(); }
        } catch (NumberFormatException e) { 
        	showValidationError("Số ghế phải là một số nguyên dương hợp lệ.", txtSoGhe); 
        	throw new RuntimeException("Số ghế phải là một số nguyên dương hợp lệ.");
//        return; 
        }

		PhongChieu phongChieu;
		boolean success = false;
		String successMessage = "";
        String errorMessage = "";

		try {
			if (trangThaiHienTai == EditState.ADDING) {
                ma = PhongChieuDAO.generateMaPhongChieu(); // Generate ID
                phongChieu = new PhongChieu(ma, ten, soGhe);
				success = PhongChieuDAO.tao(phongChieu);
				successMessage = "Thêm phòng chiếu thành công!";
                errorMessage = "Thêm phòng chiếu thất bại. Mã phòng có thể đã tồn tại.";
			} else if (trangThaiHienTai == EditState.EDITING) {
                 ma = txtMaPhongChieu.getText().trim(); // Get existing ID
                 if(ma.isEmpty() || ma.equals("(Tự động)")) {
                    showError("Lỗi: Không xác định được Mã phòng để cập nhật.", null);
                    return;
                 }
                 phongChieu = new PhongChieu(ma, ten, soGhe);
				 success = PhongChieuDAO.capNhat(phongChieu);
				 successMessage = "Cập nhật phòng chiếu thành công!";
                 errorMessage = "Cập nhật phòng chiếu thất bại.";
			} else {
                return; 
            }

			if (success) {
				JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
				loadDuLieuBangPhongChieu(PhongChieuDAO.readAll());
			} else {
                 showValidationError(errorMessage, txtTenPhongChieu);
                 if (trangThaiHienTai == EditState.ADDING) {
                     txtMaPhongChieu.setText("(Tự động)");
                 }
			}
		} catch (Exception ex) {
            showError("Đã xảy ra lỗi không mong muốn khi lưu.", ex);
		}
	}

	//Chức năng
	//Hủy thao tác thêm,sửa. - Trở lại trạng thái ban đầu
	private void dongCheDoChinhSua() {
        setInitialState();
        loadDuLieuBangPhongChieu(PhongChieuDAO.readAll());
    }

	//Tìm kiếm
	//Tìm kiếm phòng chiếu theo tên
	private void timPhongChieu() {
		String keyword = txtTimKiem.getText().trim();
		List<PhongChieu> results;
		if (keyword.isEmpty()) {
			results = PhongChieuDAO.readAll();
		} else {
			results = PhongChieuDAO.timTheoTenPhongChieu(keyword);
			if (results.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy phòng chiếu nào có tên chứa '" + keyword + "'", "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		loadDuLieuBangPhongChieu(results);
		// setInitialState is called by loadDuLieuBangPhongChieu
	}

	//Hiển thị lỗi
	//Hiển thị lỗi nhập liệu - Focus lại ô lỗi
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

     //Hiển thị lỗi
     //Hiển thị lỗi hệ thống, kèm chi tiết ngoại lệ
	private void showError(String message, Exception ex) {
		String detailedMessage = message;
		if (ex != null) {
			detailedMessage += "\nChi tiết lỗi: " + ex.getMessage();
			ex.printStackTrace();
			System.err.println(message + "\n" + ex.getMessage());
		}
		JOptionPane.showMessageDialog(this, detailedMessage, "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
	}

	//Lắng nghe sk 
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

	    if (o.equals(btnThem)) {
	        vaoCheDoThem();
	    } else if (o.equals(btnSua)) {
	        vaoCheDoChinhSua();
	    } else if (o.equals(btnXoa)) {
	        xoaPhongChieuDuocChon();
	    } else if (o.equals(btnLuu)) {
	        savePhongChieu();
	    } else if (o.equals(btnHuy)) {
	        dongCheDoChinhSua();
	    } else if (o.equals(btnThoat)) {
	        int confirm = JOptionPane.showConfirmDialog(this,
	                "Bạn có chắc chắn muốn thoát?", "Xác nhận thoát",
	                JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            Window window = SwingUtilities.getWindowAncestor(this);
	            if (window != null) {
	                window.dispose();
	            }
	        }
	    } else if (o.equals(btnTimKiem)) {
	        timPhongChieu();
	    }
		
	}
}
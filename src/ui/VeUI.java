package ui;

import dao.*;
import entity.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VeUI extends JPanel {
	private JTable table;
	private JTextField txtTimKiem;
	private DefaultTableModel tableModel;
	private JButton btnTimKiem;
    private final DecimalFormat currencyFormatter = new DecimalFormat("#,##0 VNĐ");
    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM HH:mm");
    private final SimpleDateFormat dbDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public VeUI() {
		setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

		JPanel panelDanhSach = new JPanel();
		panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Vé Đã Bán", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDanhSach.setBounds(38, 60, 927, 586);
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
                // Chỉ xem
			}
		});
		scrollPane.setViewportView(table);

		JPanel panelSearch = new JPanel();
		panelSearch.setLayout(null);
		panelSearch.setBounds(38, 15, 927, 34);
		add(panelSearch);

		JLabel lblTimKiem = new JLabel("Tìm theo Mã HĐ:");
        lblTimKiem.setBounds(0, 7, 120, 25);
        panelSearch.add(lblTimKiem);

		txtTimKiem = new JTextField();
		txtTimKiem.setToolTipText("Nhập Mã Hóa Đơn cần tìm vé");
		txtTimKiem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                    searchVe();
	                }
			}
		});
		txtTimKiem.setBounds(130, 7, 274, 25);
		panelSearch.add(txtTimKiem);
		txtTimKiem.setColumns(10);

		btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTimKiem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTimKiem.setIcon(new ImageIcon(VeUI.class.getResource("/icons/icons8-search-20.png"))); // Added Icon
		btnTimKiem.addActionListener(e -> searchVe());
		btnTimKiem.setBounds(415, 5, 117, 27);
		panelSearch.add(btnTimKiem);

		loadTableData(VeDAO.readAll());
		setInitialState();
	}

    private void clearFields() {
		txtTimKiem.setText("");
        table.clearSelection();
	}

	private void setInitialState() {
        clearFields();
    }

    private void searchVe() {
        String keyword = txtTimKiem.getText().trim();
        List<Ve> results;
        if (keyword.isEmpty()) {
            results = VeDAO.readAll();
        } else {
            results = VeDAO.findByHoaDonId(keyword);
             if (results.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Không tìm thấy vé nào cho Mã Hóa Đơn: " + keyword, "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
                 System.err.println("Không tìm thấy vé nào cho Mã Hóa Đơn");
             }
        }
        loadTableData(results);
    }

    private void loadTableData(List<Ve> list) {
		if (tableModel.getColumnCount() == 0) {
            tableModel.setColumnIdentifiers(new Object[]{
                "Mã Vé", "Mã HĐ", "Tên Phim", "Phòng", "Ghế", "Thời Gian BD", "Giá Vé"
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(60);
            table.getColumnModel().getColumn(4).setPreferredWidth(60);
            table.getColumnModel().getColumn(5).setPreferredWidth(110);
            table.getColumnModel().getColumn(6).setPreferredWidth(90);
        }

		tableModel.setRowCount(0);

		if (list != null) {
			for (Ve ve : list) {
                String maHD = (ve.getHoaDon() != null) ? ve.getHoaDon().getMaHoaDon() : "N/A";
                String tenPhim = "N/A";
                String tenPhong = "N/A";
                String thoiGianBD = "N/A";
                if (ve.getSuatChieu() != null) {
                    thoiGianBD = formatDateTime(ve.getSuatChieu().getThoiGianBD());
                    if (ve.getSuatChieu().getPhim() != null) {
                        tenPhim = ve.getSuatChieu().getPhim().getTenPhim();
                    }
                    if (ve.getSuatChieu().getPhongChieu() != null) {
                        tenPhong = ve.getSuatChieu().getPhongChieu().getTenPhong();
                    }
                }
                String gheInfo = "N/A";
                if (ve.getGheNgoi() != null) {
                    gheInfo = ve.getGheNgoi().getHang() + ve.getGheNgoi().getSoGhe();
                }

				tableModel.addRow(new Object[]{
	                ve.getMaVe(),
	                maHD,
	                tenPhim,
                    tenPhong,
                    gheInfo,
                    thoiGianBD,
	                currencyFormatter.format(ve.getGiaVe())
	            });
			}
		}
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.clearSelection();
	}

    private String formatDateTime(String dbDateTime) {
        if (dbDateTime == null || dbDateTime.isEmpty()) {
            return "N/A";
        }
        try {
            Date date = dbDateTimeFormatter.parse(dbDateTime);
            return dateTimeFormatter.format(date);
        } catch (Exception e) {
            return dbDateTime;
        }
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
             System.err.println(message + (ex.getMessage() != null ? "\n" + ex.getMessage() : ""));
         }
         JOptionPane.showMessageDialog(this, detailedMessage, "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
    }
}
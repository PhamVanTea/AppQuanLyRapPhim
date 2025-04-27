package ui;

import dao.ThongKeDAO;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer; // Import for renderer

public class ThongKeUI extends JPanel {
    private JTable tableThongKe;
    private JDateChooser dateChooserTuNgay;
    private JDateChooser dateChooserDenNgay;
    private DefaultTableModel tableModelThongKe;
    private JButton btnThongKe;
    private final DecimalFormat currencyFormatter = new DecimalFormat("#,##0 VNĐ");

    public ThongKeUI() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

        JPanel panelKetQua = new JPanel();
        panelKetQua.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Kết quả Thống kê", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelKetQua.setBounds(38, 130, 927, 517);
        add(panelKetQua);
        panelKetQua.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panelKetQua.add(scrollPane);

        tableThongKe = new JTable();
        tableModelThongKe = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableThongKe.setModel(tableModelThongKe);
        tableThongKe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableThongKe.setRowHeight(25);
        tableThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableThongKe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        scrollPane.setViewportView(tableThongKe);

        JPanel panelLuaChon = new JPanel();
        panelLuaChon.setLayout(null);
        panelLuaChon.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chọn Khoảng Thời Gian", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelLuaChon.setBounds(38, 10, 927, 96);
        add(panelLuaChon);

        int labelW = 100;
        int fieldW1 = 247;
        int x1 = 30;
        int x2 = 140;
        int x3 = 397;
        int x4 = 507;
        int rowY = 38;

        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setBounds(x1, rowY, labelW, 25);
        panelLuaChon.add(lblTuNgay);

        dateChooserTuNgay = new JDateChooser();
        dateChooserTuNgay.setDateFormatString("dd/MM/yyyy");
        dateChooserTuNgay.setDate(new Date());
        dateChooserTuNgay.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Add cursor con trỏ chuột
        dateChooserTuNgay.setBounds(x2, rowY, fieldW1, 25);
        panelLuaChon.add(dateChooserTuNgay);

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setBounds(x3, rowY, labelW, 25);
        panelLuaChon.add(lblDenNgay);

        dateChooserDenNgay = new JDateChooser();
        dateChooserDenNgay.setDateFormatString("dd/MM/yyyy");
        dateChooserDenNgay.setDate(new Date());
        dateChooserDenNgay.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Add cursor con trỏ chuột
        dateChooserDenNgay.setBounds(x4, rowY, fieldW1, 25);
        panelLuaChon.add(dateChooserDenNgay);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThongKe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Add cursor con trỏ chuột
        btnThongKe.setIcon(new ImageIcon(ThongKeUI.class.getResource("/icons/icons8-statistics-20.png"))); // Thêm icon
        btnThongKe.setBounds(764, 29, 144, 43);
        panelLuaChon.add(btnThongKe);
        btnThongKe.addActionListener(e -> tinhVaHienThiThongKe());

        setInitialTableColumns();
    }

    private void setInitialTableColumns() {
         if (tableModelThongKe.getColumnCount() == 0) {
             tableModelThongKe.setColumnIdentifiers(new Object[]{"Loại Thống Kê", "Số Lượng / Giá Trị"});
             tableThongKe.getColumnModel().getColumn(0).setPreferredWidth(400);
             tableThongKe.getColumnModel().getColumn(1).setPreferredWidth(200);

             DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
             rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
             tableThongKe.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
         }
         tableModelThongKe.setRowCount(0);
    }

    private void tinhVaHienThiThongKe() {
        Date tuNgayDate = dateChooserTuNgay.getDate();
        Date denNgayDate = dateChooserDenNgay.getDate();

        if (tuNgayDate == null) {
            showValidationError("Vui lòng chọn 'Từ ngày'.", dateChooserTuNgay);
            System.err.println("Vui lòng chọn 'Từ ngày'.");
            return;
        }
        if (denNgayDate == null) {
            showValidationError("Vui lòng chọn 'Đến ngày'.", dateChooserDenNgay);
            System.err.println("Vui lòng chọn 'Đến ngày'.");
            return;
        }
        if (denNgayDate.before(tuNgayDate)) {
            showValidationError("'Đến ngày' không được trước 'Từ ngày'.", dateChooserDenNgay);
            System.err.println("'Đến ngày' không được trước 'Từ ngày'.");
            return;
        }

        LocalDate startDate = tuNgayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = denNgayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            BigDecimal tongDoanhThu = ThongKeDAO.calculateTongDoanhThu(startDate, endDate);
            int tongSoVe = ThongKeDAO.calculateTongSoVe(startDate, endDate);
            int tongSoKhachHang = ThongKeDAO.calculateTongSoKhachHang(startDate, endDate);
            int tongSoNhanVien = ThongKeDAO.calculateTongSoNhanVien(startDate, endDate);
            int tongSoPhim = ThongKeDAO.calculateTongSoPhim(startDate, endDate);
            int tongSoPhongChieu = ThongKeDAO.calculateTongSoPhongChieu(startDate, endDate);

            tableModelThongKe.setRowCount(0);

            tableModelThongKe.addRow(new Object[]{"Tổng Doanh Thu", currencyFormatter.format(tongDoanhThu)});
            tableModelThongKe.addRow(new Object[]{"Tổng Số Vé Bán Ra", tongSoVe});
            tableModelThongKe.addRow(new Object[]{"Số Lượng Khách Hàng Giao Dịch", tongSoKhachHang});
            tableModelThongKe.addRow(new Object[]{"Số Lượng Nhân Viên Lập Hóa Đơn", tongSoNhanVien});
            tableModelThongKe.addRow(new Object[]{"Số Lượng Phim Được Chiếu", tongSoPhim});
            tableModelThongKe.addRow(new Object[]{"Số Lượng Phòng Được Sử Dụng", tongSoPhongChieu});

        } catch (Exception ex) {
            showError("Đã xảy ra lỗi trong quá trình tính toán thống kê.", ex);
        }
    }

    private void showValidationError(String message, Component componentToFocus) {
        JOptionPane.showMessageDialog(this, message, "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        if (componentToFocus != null) {
            SwingUtilities.invokeLater(() -> {
                 componentToFocus.requestFocusInWindow();
                 if (componentToFocus instanceof JDateChooser) {
                     ((JDateChooser) componentToFocus).getCalendarButton().requestFocusInWindow();
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
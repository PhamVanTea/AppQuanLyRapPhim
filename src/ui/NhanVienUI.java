package ui;

import dao.NhanVienDAO;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class NhanVienUI extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;


    public NhanVienUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 680));

        // --- Search Panel ---
        JPanel panelTimKiem = new JPanel();
        panelTimKiem.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(panelTimKiem, BorderLayout.NORTH);


 

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
                "Mã NV", "Tên Nhân Viên", "Chức Vụ", "Tên Đăng Nhập", "Mật Khẩu"
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
    }


}

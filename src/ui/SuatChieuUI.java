package ui;

import dao.PhongChieuDAO;
import dao.PhimDAO;
import dao.SuatChieuDAO;
import entity.PhongChieu;
import entity.Phim;
import entity.SuatChieu;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SuatChieuUI extends JPanel implements ActionListener {
    private JTable table;
    private JTextField txtMaSuatChieu;
    private JComboBox<Phim> comboBoxPhim;
    private JComboBox<PhongChieu> comboBoxPhongChieu;
    private JTextField txtGia;
    private JDateChooser dateChooserThoiGianBD;
    private JTextField txtThoiGianBD_Time;
    private JTextField txtThoiGianKT_Display;

    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnThoat;

    private final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat DISPLAY_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat DAO_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SimpleDateFormat DATE_ONLY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("HH:mm");

    private enum EditState { IDLE, ADDING, EDITING }
    private EditState currentState = EditState.IDLE;
    private boolean isAdjustingFields = false;

    public SuatChieuUI() {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 680)); // Set preferred size

        JPanel panelDanhSach = new JPanel();
        panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Danh sách Suất chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelDanhSach.setBounds(38, 222, 927, 360);
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

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && currentState == EditState.IDLE) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    populateFieldsFromSelectedRow(selectedRow);
                } else {
                    clearFields();
                }
                updateButtonStates();
            }
        });
        scrollPane.setViewportView(table);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Suất chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_1.setBounds(38, 10, 927, 202);
        add(panel_1);

        int labelX1 = 30;
        int fieldX1 = 140;
        int labelX2 = 480;
        int fieldX2 = 600;
        int fieldW = 290;
        int labelW = 100;
        int fieldH = 25;
        int row1Y = 30;
        int row2Y = row1Y + 40;
        int row3Y = row2Y + 40;
        int row4Y = row3Y + 40;


        JLabel lblMaSuatChieu = new JLabel("Mã suất chiếu:");
        lblMaSuatChieu.setBounds(labelX1, row1Y, labelW, fieldH);
        panel_1.add(lblMaSuatChieu);
        txtMaSuatChieu = new JTextField();
        txtMaSuatChieu.setEditable(false);
        txtMaSuatChieu.setBackground(Color.LIGHT_GRAY);
        txtMaSuatChieu.setBounds(159, 30, 271, 25);
        panel_1.add(txtMaSuatChieu);

        JLabel lblPhim = new JLabel("Phim:");
        lblPhim.setBounds(labelX2, row1Y, labelW, fieldH);
        panel_1.add(lblPhim);
        comboBoxPhim = new JComboBox<>();
        comboBoxPhim.setBounds(fieldX2, row1Y, fieldW, fieldH);
        panel_1.add(comboBoxPhim);
        comboBoxPhim.addActionListener(this);

        JLabel lblPhong = new JLabel("Phòng:");
        lblPhong.setBounds(labelX1, row2Y, labelW, fieldH);
        panel_1.add(lblPhong);
        comboBoxPhongChieu = new JComboBox<>();
        comboBoxPhongChieu.setBounds(159, 70, 271, 25);
        panel_1.add(comboBoxPhongChieu);

        JLabel lblGia = new JLabel("Giá Vé (VNĐ):");
        lblGia.setBounds(labelX2, row2Y, labelW, fieldH);
        panel_1.add(lblGia);
        txtGia = new JTextField();
        txtGia.setBounds(fieldX2, row2Y, fieldW, fieldH);
        panel_1.add(txtGia);

        JLabel lblNgayBD = new JLabel("Ngày bắt đầu:");
        lblNgayBD.setBounds(labelX1, row3Y, labelW, fieldH);
        panel_1.add(lblNgayBD);
        dateChooserThoiGianBD = new JDateChooser();
        dateChooserThoiGianBD.setDateFormatString("dd/MM/yyyy");
        ((JTextField) dateChooserThoiGianBD.getDateEditor().getUiComponent()).setEditable(false);
        dateChooserThoiGianBD.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dateChooserThoiGianBD.setBounds(159, 110, 271, 25);
        panel_1.add(dateChooserThoiGianBD);

        JLabel lblGioBD = new JLabel("Giờ bắt đầu:");
        lblGioBD.setBounds(labelX2, row3Y, labelW, fieldH);
        panel_1.add(lblGioBD);
        txtThoiGianBD_Time = new JTextField();
        txtThoiGianBD_Time.setToolTipText("HH:mm (vd: 14:30)");
        txtThoiGianBD_Time.setBounds(fieldX2, row3Y, 100, fieldH); // Ngắn hơn vì chỉ nhập giờ
        panel_1.add(txtThoiGianBD_Time);

        JLabel lblGioKT = new JLabel("Kết thúc (dự kiến):");
        lblGioKT.setBounds(labelX1, row4Y, labelW + 30, fieldH);
        panel_1.add(lblGioKT);
        txtThoiGianKT_Display = new JTextField();
        txtThoiGianKT_Display.setEditable(false);
        txtThoiGianKT_Display.setBackground(Color.LIGHT_GRAY);
        txtThoiGianKT_Display.setBounds(159, 150, 271, 25); // Trải rộng chiều ngang hơn
        panel_1.add(txtThoiGianKT_Display);

        JPanel panelChucNang = new JPanel();
        panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelChucNang.setBounds(38, 592, 927, 64);
        add(panelChucNang);
        panelChucNang.setLayout(new GridLayout(1, 0, 15, 0)); // Tăng k/c giữa các nút

        btnThem = new JButton("Thêm");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-add-20.png")));
        btnThem.addActionListener(this);
        panelChucNang.add(btnThem);

        btnSua = new JButton("Sửa");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-edit-20.png")));
        btnSua.addActionListener(this);
        panelChucNang.add(btnSua);

        btnXoa = new JButton("Xóa");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-delete-20.png")));
        btnXoa.addActionListener(this);
        panelChucNang.add(btnXoa);

        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-save-20.png")));
        btnLuu.addActionListener(this);
        panelChucNang.add(btnLuu);

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-cancel-20.png")));
        btnHuy.addActionListener(this);
        panelChucNang.add(btnHuy);

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(SuatChieuUI.class.getResource("/icons/icons8-exit-20.png")));
        btnThoat.addActionListener(this);
        panelChucNang.add(btnThoat);

        addEventListeners();
        loadComboBoxData();
        setInitialState();
        handlePhimSelectionChange();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnThem)) {
            enterAddMode();
        } else if (o.equals(btnSua)) {
            enterEditMode();
        } else if (o.equals(btnXoa)) {
            deleteSelectedSuatChieu();
        } else if (o.equals(btnLuu)) {
            saveSuatChieu();
        } else if (o.equals(btnHuy)) {
            cancelEditMode();
        } else if (o.equals(btnThoat)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn thoát?", "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) { window.dispose(); }
            }
        } else if (o.equals(comboBoxPhim)) {
            if (!isAdjustingFields) {
                calculateAndDisplayEndTime();
                handlePhimSelectionChange();
            }
        }
    }

    private void addEventListeners() {
        txtThoiGianBD_Time.addActionListener(e -> { if(!isAdjustingFields) calculateAndDisplayEndTime(); });
        dateChooserThoiGianBD.addPropertyChangeListener("date", evt -> {
            if(!isAdjustingFields && "date".equals(evt.getPropertyName()))
                calculateAndDisplayEndTime();
        });
        txtThoiGianBD_Time.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) { if(!isAdjustingFields) calculateAndDisplayEndTime(); }
        });
    }

    private void loadComboBoxData() {
        loadPhimComboBox();
        loadPhongChieuComboBox();
    }

    private void loadPhimComboBox() {
        try {
            List<Phim> phimList = PhimDAO.readAll();
            DefaultComboBoxModel<Phim> model = new DefaultComboBoxModel<>();
            model.addElement(null);
            if (phimList != null) { for (Phim p : phimList) { model.addElement(p); } }
            comboBoxPhim.setModel(model);
            setComboBoxRenderer(comboBoxPhim, "-- Chọn Phim --", Phim::getTenPhim);
            if(model.getSize() > 1) comboBoxPhim.setSelectedIndex(1);
        } catch (Exception e) { handleDataLoadError("Lỗi tải danh sách phim", e); }
    }

    public void loadPhongChieuComboBox() {
        try {
            List<PhongChieu> phongList = PhongChieuDAO.readAll();
            DefaultComboBoxModel<PhongChieu> model = new DefaultComboBoxModel<>();
            model.addElement(null);
            if (phongList != null) { for (PhongChieu pc : phongList) { model.addElement(pc); } }
            comboBoxPhongChieu.setModel(model);
            setComboBoxRenderer(comboBoxPhongChieu, "-- Chọn Phòng --", PhongChieu::getTenPhong);
            if(model.getSize() > 1) comboBoxPhongChieu.setSelectedIndex(1);
        } catch (Exception e) { handleDataLoadError("Lỗi tải danh sách phòng chiếu", e); }
    }

    private <T> void setComboBoxRenderer(JComboBox<T> comboBox, String placeholder, java.util.function.Function<T, String> nameExtractor) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) { setText(placeholder); }
                else { setText(nameExtractor.apply((T)value)); }
                return this;
            }
        });
        if (comboBox.getSelectedIndex() == 0) {
            comboBox.setSelectedIndex(0);
        }
    }

    private void loadTableData(List<SuatChieu> list) {
        if (tableModel.getColumnCount() == 0) {
            tableModel.setColumnIdentifiers(new Object[]{
                    "Mã Suất", "Tên Phim", "Phòng", "Bắt Đầu", "Kết Thúc", "Giá (VNĐ)"
            });
            table.getColumnModel().getColumn(0).setPreferredWidth(80);
            table.getColumnModel().getColumn(1).setPreferredWidth(180);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(140);
            table.getColumnModel().getColumn(4).setPreferredWidth(140);
            table.getColumnModel().getColumn(5).setPreferredWidth(90);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        }
        tableModel.setRowCount(0);
        if (list != null) {
            for (SuatChieu sc : list) {
                tableModel.addRow(new Object[]{
                        sc.getMaSuatChieu(),
                        sc.getPhim() != null ? sc.getPhim().getTenPhim() : "Lỗi Phim",
                        sc.getPhongChieu() != null ? sc.getPhongChieu().getTenPhong() : "Lỗi Phòng",
                        formatDateTimeForDisplay(sc.getThoiGianBD()),
                        formatDateTimeForDisplay(sc.getThoiGianKetThuc()),
                        String.format("%,.0f", sc.getGia())
                });
            }
        }
        table.clearSelection();
        updateButtonStates();
    }

    private void handlePhimSelectionChange() {
        if (currentState != EditState.IDLE) { return; }

        Phim selectedPhim = (Phim) comboBoxPhim.getSelectedItem();
        List<SuatChieu> showtimes;
        if (selectedPhim != null && selectedPhim.getMaPhim() != null) {
            showtimes = SuatChieuDAO.searchByPhim(selectedPhim.getMaPhim());
        } else {
            showtimes = SuatChieuDAO.readAll();
        }
        loadTableData(showtimes);
        clearFields();
        updateButtonStates();
    }


    private void populateFieldsFromSelectedRow(int selectedRow) {
        if (selectedRow != -1) {
            if (selectedRow < tableModel.getRowCount()){ // Giới hạn k vượt quá số dòng trong bảng
                String maSuat = tableModel.getValueAt(selectedRow, 0).toString();
                SuatChieu selectedSC = SuatChieuDAO.findById(maSuat);
                if (selectedSC != null) {
                    isAdjustingFields = true;
                    try {
                        txtMaSuatChieu.setText(selectedSC.getMaSuatChieu());

                        selectComboBoxItem(comboBoxPhongChieu, selectedSC.getPhongChieu());
                        txtGia.setText(String.format("%.0f", selectedSC.getGia()));
                        Date startDate = parseDateTimeFromDAO(selectedSC.getThoiGianBD());
                        if (startDate != null) {
                            dateChooserThoiGianBD.setDate(startDate);
                            txtThoiGianBD_Time.setText(TIME_ONLY_FORMAT.format(startDate));
                        } else {
                            dateChooserThoiGianBD.setDate(null);
                            txtThoiGianBD_Time.setText("");
                        }
                        calculateAndDisplayEndTime();
                    } catch (Exception e) {
                        handleDataLoadError("Lỗi hiển thị chi tiết suất chiếu", e);
                        clearFields();
                    } finally {
                        isAdjustingFields = false;
                    }
                } else {
                    handleDataLoadError("Không tìm thấy suất chiếu với mã: " + maSuat, null);
                    clearFields();
                }
            } else {
                clearFields(); // Xóa nếu chỉ số nhập k hợp lệ
            }
        }
    }

    private <T> void selectComboBoxItem(JComboBox<T> comboBox, T itemToSelect) {
        if (itemToSelect == null) { comboBox.setSelectedIndex(0); return; }
        DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            T item = model.getElementAt(i);
            if (item != null && item.equals(itemToSelect)) { comboBox.setSelectedIndex(i); return; }
        }
        comboBox.setSelectedIndex(0);
    }

    private void clearFields() {
        isAdjustingFields = true;
        try {
            if (currentState == EditState.ADDING) {
                txtMaSuatChieu.setText("(Tự động tạo)");
            } else {
                txtMaSuatChieu.setText("");
            }

            txtGia.setText("");
            dateChooserThoiGianBD.setDate(null);
            txtThoiGianBD_Time.setText("");
            txtThoiGianKT_Display.setText("");

        } finally {
            isAdjustingFields = false;
        }
        calculateAndDisplayEndTime();
        updateButtonStates(); // Các nút cập nhật lại trạng thái
    }


    private void setFieldsEditable(boolean isEditable) {
        txtGia.setEditable(isEditable);
        txtThoiGianBD_Time.setEditable(isEditable);
        dateChooserThoiGianBD.setEnabled(isEditable);
        ((JTextField) dateChooserThoiGianBD.getDateEditor().getUiComponent()).setEditable(false);
        ((JTextField) dateChooserThoiGianBD.getDateEditor().getUiComponent()).setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);

        if (currentState == EditState.EDITING) {
            comboBoxPhim.setEnabled(false);
            comboBoxPhongChieu.setEnabled(false);
        } else if (currentState == EditState.ADDING) {
            comboBoxPhim.setEnabled(true);
            comboBoxPhongChieu.setEnabled(true);
        } else {
            comboBoxPhim.setEnabled(true);
            comboBoxPhongChieu.setEnabled(false);
        }
        txtMaSuatChieu.setEditable(false);
        txtThoiGianKT_Display.setEditable(false);
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
        setFieldsEditable(!isIdle);
        table.setEnabled(isIdle);
    }

    private void setInitialState() {
        currentState = EditState.IDLE;
        table.clearSelection();
        clearFields();
        updateButtonStates();
    }

    private void enterAddMode() {
        currentState = EditState.ADDING;
        table.clearSelection();
        clearFields();
        updateButtonStates();
        comboBoxPhim.requestFocusInWindow();
    }

    private void enterEditMode() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) { showValidationError("Vui lòng chọn suất chiếu để sửa.", table); return; }
        currentState = EditState.EDITING;
        updateButtonStates();
        txtGia.requestFocusInWindow();
    }

    private void cancelEditMode() {
        setInitialState();
        handlePhimSelectionChange();
    }

    private void deleteSelectedSuatChieu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) { showValidationError("Vui lòng chọn suất chiếu để xóa.", table); return; }
        String maSuat = tableModel.getValueAt(selectedRow, 0).toString();
        String tenPhim = tableModel.getValueAt(selectedRow, 1).toString();
        String thoiGian = tableModel.getValueAt(selectedRow, 3).toString();
        int choice = JOptionPane.showConfirmDialog(this,
                String.format("Bạn có chắc chắn muốn xóa suất chiếu sau?\n\nMã: %s\nPhim: %s\nThời gian bắt đầu: %s\n\n(Hành động này không thể hoàn tác)", maSuat, tenPhim, thoiGian),
                "Xác nhận xóa Suất chiếu",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = SuatChieuDAO.delete(maSuat);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa suất chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    handlePhimSelectionChange();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Xóa suất chiếu thất bại.\nCó thể suất chiếu này đã có vé được bán hoặc có lỗi xảy ra.",
                            "Lỗi Xóa", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) { handleDataLoadError("Lỗi hệ thống khi xóa suất chiếu", ex); }
        }
    }

    private void saveSuatChieu() {
        Object phimItem = comboBoxPhim.getSelectedItem();
        Object phongItem = comboBoxPhongChieu.getSelectedItem();
        Date startDateOnly = dateChooserThoiGianBD.getDate();
        String startTimeStr = txtThoiGianBD_Time.getText().trim();
        String giaStr = txtGia.getText().trim().replace(",", "");
        String currentMaSuat = (currentState == EditState.EDITING) ? txtMaSuatChieu.getText().trim() : null;

        if (!(phimItem instanceof Phim)) {
            showValidationError("Vui lòng chọn một Phim.", comboBoxPhim);
            throw new RuntimeException("Lỗi: Chưa chọn phim.");
        }
        if (!(phongItem instanceof PhongChieu)) {
            showValidationError("Vui lòng chọn một Phòng Chiếu.", comboBoxPhongChieu);
            throw new RuntimeException("Lỗi: Chưa chọn phòng chiếu.");
        }
        if (giaStr.isEmpty()) {
            showValidationError("Giá vé không được để trống.", txtGia);
            throw new RuntimeException("Lỗi: Giá vé trống.");
        }

        float gia;
        try {
            gia = Float.parseFloat(giaStr);
            if (gia < 0) {
                showValidationError("Giá vé phải là một số không âm.", txtGia);
                throw new RuntimeException("Lỗi: Giá vé âm.");
            }
        } catch (NumberFormatException e) {
            showValidationError("Giá vé không hợp lệ. Vui lòng nhập một số.", txtGia);
            throw new RuntimeException("Lỗi: Giá vé không hợp lệ.");
        }

        if (startDateOnly == null) {
            showValidationError("Vui lòng chọn Ngày bắt đầu.", dateChooserThoiGianBD);
            throw new RuntimeException("Lỗi: Chưa chọn ngày bắt đầu.");
        }

        if (startTimeStr.isEmpty()) {
            showValidationError("Chưa nhập thời gian bắt đầu", txtThoiGianBD_Time);
            throw new RuntimeException("Lỗi: Chưa nhập thời gian bắt đầu.");
        }

        if (!startTimeStr.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
            showValidationError("Giờ bắt đầu không hợp lệ. Phải có định dạng HH:mm (VD: 09:30 hoặc 14:05).", txtThoiGianBD_Time);
            throw new RuntimeException("Lỗi: Giờ bắt đầu không hợp lệ.");
        }


        Phim selectedPhim = (Phim) phimItem;
        PhongChieu selectedPhong = (PhongChieu) phongItem;

        Date startDateTime;
        Date endDateTime;

        try {
            String datePart = DATE_ONLY_FORMAT.format(startDateOnly);
            String dateTimeStr = datePart + " " + startTimeStr + ":00";
            startDateTime = DAO_DATE_TIME_FORMAT.parse(dateTimeStr);
            if (selectedPhim.getThoiLuong() <= 0) {
                showValidationError("Phim '" + selectedPhim.getTenPhim() + "' có thời lượng không hợp lệ (" + selectedPhim.getThoiLuong() + " phút). Không thể tính giờ kết thúc.", comboBoxPhim);
                throw new RuntimeException("Lỗi: Thời lượng phim không hợp lệ.");
            }
            endDateTime = calculateEndDate(startDateTime, selectedPhim);
            if (endDateTime == null) {
                showValidationError("Không thể tính toán thời gian kết thúc suất chiếu.", txtThoiGianBD_Time);
                throw new RuntimeException("Lỗi: Không thể tính giờ kết thúc.");
            }
        } catch (Exception e) {
            showValidationError("Định dạng Ngày hoặc Giờ bắt đầu không hợp lệ. Không thể xử lý.", txtThoiGianBD_Time);
            throw new RuntimeException("Lỗi: Định dạng ngày hoặc giờ bắt đầu không hợp lệ.");
        }

        String thoiGianBDStrDAO = DAO_DATE_TIME_FORMAT.format(startDateTime);
        String thoiGianKTStrDAO = DAO_DATE_TIME_FORMAT.format(endDateTime);

        try {
            if (SuatChieuDAO.hasOverlap(selectedPhong.getMaPhong(), thoiGianBDStrDAO, thoiGianKTStrDAO, currentMaSuat)) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi: Suất chiếu này bị trùng lịch với một suất chiếu khác trong cùng phòng (" + selectedPhong.getTenPhong() + ").\n" +
                                "Vui lòng chọn thời gian hoặc phòng khác.",
                        "Lỗi Trùng Lịch Chiếu", JOptionPane.ERROR_MESSAGE);
                System.err.println("Lỗi: Trùng lịch chiếu.");
                dateChooserThoiGianBD.requestFocusInWindow();
                return;
            }
        } catch (Exception e) {
            handleDataLoadError("Lỗi khi kiểm tra trùng lịch chiếu", e);
            return;
        }

        SuatChieu suatChieu;
        boolean success = false;
        String successMessage = "";
        String errorMessage = "";

        try {
            if (currentState == EditState.ADDING) {
                String newMaSuat = SuatChieuDAO.generateMaSuatChieu();
                suatChieu = new SuatChieu(newMaSuat, selectedPhim, selectedPhong, gia, thoiGianBDStrDAO, thoiGianKTStrDAO);
                success = SuatChieuDAO.create(suatChieu);
                successMessage = "Thêm suất chiếu thành công!";
                errorMessage = "Thêm suất chiếu thất bại.";
            } else {
                if (currentMaSuat == null || currentMaSuat.isEmpty() || currentMaSuat.equals("(Tự động tạo)")) {
                    showValidationError("Lỗi: Không xác định được Mã Suất Chiếu để cập nhật.", txtMaSuatChieu);
                    System.err.println("Lỗi: Không xác định được mã suất chiếu.");
                    return;
                }
                suatChieu = new SuatChieu(currentMaSuat, selectedPhim, selectedPhong, gia, thoiGianBDStrDAO, thoiGianKTStrDAO);
                success = SuatChieuDAO.update(suatChieu);
                successMessage = "Cập nhật suất chiếu thành công!";
                errorMessage = "Cập nhật suất chiếu thất bại.";
            }

            if (success) {
                JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                setInitialState();
                handlePhimSelectionChange();
            } else {
                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi Lưu", JOptionPane.ERROR_MESSAGE);
                System.err.println(errorMessage);
            }
        } catch (Exception ex) {
            handleDataLoadError("Lỗi hệ thống khi lưu suất chiếu", ex);
        }
    }

    private Date calculateEndDate(Date startDate, Phim phim) {
        if (startDate == null || phim == null || phim.getThoiLuong() <= 0) { return null; }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.MINUTE, phim.getThoiLuong());
            return cal.getTime();
        } catch (Exception e) {
            System.err.println("Error calculating end date: " + e.getMessage());
            return null;
        }
    }

    private void calculateAndDisplayEndTime() {
        if (isAdjustingFields) return;
        Date startDateOnly = dateChooserThoiGianBD.getDate();
        String startTimeStr = txtThoiGianBD_Time.getText().trim();
        Object phimItem = comboBoxPhim.getSelectedItem();
        Date startDateTime = null;
        Date endDate = null;

        if (startDateOnly != null && startTimeStr.matches("^([01]\\d|2[0-3]):([0-5]\\d)$") && phimItem instanceof Phim) {
            Phim selectedPhim = (Phim) phimItem;
            if (selectedPhim.getThoiLuong() > 0) {
                try {
                    String datePart = DATE_ONLY_FORMAT.format(startDateOnly);
                    String dateTimeStr = datePart + " " + startTimeStr + ":00";
                    startDateTime = DAO_DATE_TIME_FORMAT.parse(dateTimeStr);
                    endDate = calculateEndDate(startDateTime, selectedPhim);
                } catch (ParseException e) {
                    endDate = null;
                    System.err.println("Parse error during end time calculation: "+ e.getMessage());
                }
            }
        }

        if (endDate != null) {
            txtThoiGianKT_Display.setText(DISPLAY_DATE_FORMAT.format(endDate) + " " + DISPLAY_TIME_FORMAT.format(endDate));
        } else {
            txtThoiGianKT_Display.setText("");
        }
    }

    private String formatDateTimeForDisplay(String daoDateTimeString) {
        if (daoDateTimeString == null || daoDateTimeString.isEmpty()) { return "N/A"; }
        try {
            Date date = DAO_DATE_TIME_FORMAT.parse(daoDateTimeString);
            return DISPLAY_DATE_FORMAT.format(date) + " " + DISPLAY_TIME_FORMAT.format(date);
        } catch (ParseException e) {
            System.err.println("Error formatting date for display: " + daoDateTimeString + " - " + e.getMessage());
            return daoDateTimeString;
        }
    }

    private Date parseDateTimeFromDAO(String daoDateTimeString) {
        if (daoDateTimeString == null || daoDateTimeString.isEmpty()) { return null; }
        try { return DAO_DATE_TIME_FORMAT.parse(daoDateTimeString); }
        catch (ParseException e) {
            System.err.println("Error parsing date from DAO: " + daoDateTimeString + " - " + e.getMessage());
            return null;
        }
    }

    private void showValidationError(String message, Component componentToFocus) {
        JOptionPane.showMessageDialog(this, message, "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        if (componentToFocus != null) {
            SwingUtilities.invokeLater(() -> {
                componentToFocus.requestFocusInWindow();
                if (componentToFocus instanceof JTextField) { ((JTextField) componentToFocus).selectAll(); }
                if (componentToFocus instanceof JComboBox) { ((JComboBox<?>)componentToFocus).showPopup();}
                if (componentToFocus instanceof JDateChooser) {
                    Component editor = ((JDateChooser) componentToFocus).getDateEditor().getUiComponent();
                    if (editor != null) editor.requestFocusInWindow();
                }
            });
        }
    }

    private void handleDataLoadError(String context, Exception e) {
        String errorMessage = context;
        if (e != null) {
            e.printStackTrace();
            if(e.getMessage() != null && !e.getMessage().isEmpty()) {
                errorMessage += ":\n" + e.getMessage();
            }
        } else {
            errorMessage += ". Lỗi không xác định.";
        }
        System.err.println("Error: " + context + (e != null ? " - " + e : ""));
        JOptionPane.showMessageDialog(this, errorMessage, "Lỗi Hệ Thống", JOptionPane.ERROR_MESSAGE);
    }
}

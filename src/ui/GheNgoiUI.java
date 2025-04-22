package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import dao.GheNgoiDAO;
import dao.PhongChieuDAO;
import entity.GheNgoi;
import entity.PhongChieu;

public class GheNgoiUI extends JPanel {
    private JTextField txtMaGhe;
    private JTextField txtHang;
    private JTextField txtSoGhe;
    private JComboBox<PhongChieu> comboBoxPhong;
    private JComboBox<String> comboBoxTrangThai;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLuu;
    private JButton btnHuy;
    private JButton btnThoat;
    private JPanel panelSeatGrid;
    private Map<String, JButton> seatButtonMap;
    private enum EditState { IDLE, ADDING, EDITING }
    private EditState currentState = EditState.IDLE;
    private GheNgoi selectedGheNgoi = null;
    private JButton selectedSeatButton = null;
    private final String[] TRANG_THAI_OPTIONS = {"Trống", "Đã đặt", "Bảo trì"};
    private final Color COLOR_TRONG = new Color(0, 153, 0);
    private final Color COLOR_DA_DAT = new Color(204, 0, 0);
    private final Color COLOR_BAO_TRI = Color.GRAY;
    private final Color COLOR_SELECTED = Color.ORANGE;

    public GheNgoiUI() {
        seatButtonMap = new HashMap<>();
        setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

        JPanel panelDanhSach = new JPanel();
        panelDanhSach.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Sơ đồ Ghế ngồi", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelDanhSach.setBounds(38, 212, 927, 370);
        add(panelDanhSach);
        panelDanhSach.setLayout(new BorderLayout(0, 0));

        panelSeatGrid = new JPanel();
        panelSeatGrid.setBackground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(panelSeatGrid);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelDanhSach.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInput = new JPanel();
        panelInput.setLayout(null);
        panelInput.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông tin Ghế ngồi", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelInput.setBounds(38, 21, 927, 181);
        add(panelInput);

        int labelWidth = 100;
        int fieldWidth = 295;
        int yGap = 40;
        int row1Y = 30;

        JLabel lblPhongChieu = new JLabel("Chọn Phòng");
        lblPhongChieu.setBounds(25, row1Y, labelWidth, 25);
        panelInput.add(lblPhongChieu);

        comboBoxPhong = new JComboBox<>();
        comboBoxPhong.setBounds(130, row1Y, fieldWidth, 25);
        panelInput.add(comboBoxPhong);
        comboBoxPhong.addActionListener(e -> handlePhongChieuSelectionChange());

        JLabel lblMaGhe = new JLabel("Mã ghế");
        lblMaGhe.setBounds(470, row1Y, labelWidth, 25);
        panelInput.add(lblMaGhe);

        txtMaGhe = new JTextField();
        txtMaGhe.setEditable(false);
        txtMaGhe.setBackground(Color.LIGHT_GRAY);
        txtMaGhe.setBounds(570, row1Y, fieldWidth, 25);
        panelInput.add(txtMaGhe);
        txtMaGhe.setColumns(10);

        int row2Y = row1Y + yGap;
        JLabel lblHang = new JLabel("Hàng");
        lblHang.setBounds(25, row2Y, labelWidth, 25);
        panelInput.add(lblHang);

        txtHang = new JTextField();
        txtHang.setEditable(false);
        txtHang.setBackground(Color.LIGHT_GRAY);
        txtHang.setBounds(130, row2Y, fieldWidth, 25);
        panelInput.add(txtHang);
        txtHang.setColumns(1);

        JLabel lblSoGhe = new JLabel("Số ghế");
        lblSoGhe.setBounds(470, row2Y, labelWidth, 25);
        panelInput.add(lblSoGhe);

        txtSoGhe = new JTextField();
        txtSoGhe.setEditable(false);
        txtSoGhe.setBackground(Color.LIGHT_GRAY);
        txtSoGhe.setBounds(570, row2Y, fieldWidth, 25);
        panelInput.add(txtSoGhe);
        txtSoGhe.setColumns(10);

        int row3Y = row2Y + yGap;
        JLabel lblTrangThai = new JLabel("Trạng thái");
        lblTrangThai.setBounds(25, row3Y, labelWidth, 25);
        panelInput.add(lblTrangThai);

        comboBoxTrangThai = new JComboBox<>(TRANG_THAI_OPTIONS);
        comboBoxTrangThai.setEnabled(false);
        comboBoxTrangThai.setBackground(Color.LIGHT_GRAY);
        comboBoxTrangThai.setBounds(130, row3Y, fieldWidth, 25);
        panelInput.add(comboBoxTrangThai);

        JPanel panelChucNang = new JPanel();
        panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelChucNang.setBounds(38, 592, 927, 64);
        add(panelChucNang);
        panelChucNang.setLayout(new GridLayout(1, 0, 10, 0));

        btnThem = new JButton("Thêm Ghế");
        btnThem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThem.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-add-20.png")));
        btnThem.addActionListener(e -> enterAddMode());
        panelChucNang.add(btnThem);

        btnSua = new JButton("Sửa TT Ghế");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-edit-20.png")));
        btnSua.setEnabled(false);
        btnSua.addActionListener(e -> enterEditMode());
        panelChucNang.add(btnSua);

        btnXoa = new JButton("Xóa Ghế");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-delete-20.png")));
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(e -> deleteSelectedGheNgoi());
        panelChucNang.add(btnXoa);

        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-save-20.png")));
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(e -> saveGheNgoi());
        panelChucNang.add(btnLuu);

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-cancel-20.png")));
        btnHuy.setEnabled(false);
        btnHuy.addActionListener(e -> cancelEditMode());
        panelChucNang.add(btnHuy);

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-exit-20.png")));
        btnThoat.addActionListener(e -> {
             int confirm = JOptionPane.showConfirmDialog(
                    GheNgoiUI.this,
                    "Bạn có chắc chắn muốn thoát?",
                    "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION
                );
             if (confirm == JOptionPane.YES_OPTION) {
                 Window window = SwingUtilities.getWindowAncestor(this);
                 if (window != null) {
                     window.dispose();
                 } else {
                     System.exit(0);
                 }
             }
        });
        panelChucNang.add(btnThoat);

        loadPhongChieuComboBox();
        setInitialState();
        if (comboBoxPhong.getItemCount() > 0) {
            comboBoxPhong.setSelectedIndex(0);
             handlePhongChieuSelectionChange();
        } else {
            renderSeatGrid(new ArrayList<>());
        }
    }

    private void loadPhongChieuComboBox() {
        try {
            List<PhongChieu> phongChieuList = PhongChieuDAO.readAll();
            DefaultComboBoxModel<PhongChieu> model = new DefaultComboBoxModel<>();
            if (phongChieuList != null && !phongChieuList.isEmpty()) {
                for (PhongChieu pc : phongChieuList) {
                    model.addElement(pc);
                }
            } else {
                model.addElement(null);
                System.err.println("Không tìm thấy phòng chiếu nào.");
            }
            comboBoxPhong.setModel(model);
            comboBoxPhong.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof PhongChieu) {
                        setText(((PhongChieu) value).getTenPhong());
                    } else {
                        setText("Chọn phòng...");
                    }
                    return this;
                }
            });
        } catch (Exception e) {
             System.err.println("Lỗi tải danh sách phòng chiếu: " + e.getMessage());
             e.printStackTrace();
             JOptionPane.showMessageDialog(this, "Lỗi tải danh sách phòng chiếu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

     private void handlePhongChieuSelectionChange() {
         if (currentState != EditState.IDLE) {
             int choice = JOptionPane.showConfirmDialog(this,
                     "Bạn đang trong quá trình chỉnh sửa. Thay đổi phòng sẽ hủy các thay đổi hiện tại. Tiếp tục?",
                     "Xác nhận", JOptionPane.YES_NO_OPTION);
             if (choice == JOptionPane.NO_OPTION) {
                 // Revert combo box selection to the room of the currently selected/edited seat
                 if (selectedGheNgoi != null && selectedGheNgoi.getPhongChieu() != null) {
                    comboBoxPhong.setSelectedItem(selectedGheNgoi.getPhongChieu());
                 }
                 return; // Stop processing the change
             } else {
                 // User chose YES, proceed after cancelling edits
                 cancelEditMode(); // This will set state to IDLE, clear fields, etc.
             }
         }

         PhongChieu selectedPhong = (PhongChieu) comboBoxPhong.getSelectedItem();
         if (selectedPhong != null) {
             List<GheNgoi> seats = GheNgoiDAO.searchByPhongChieu(selectedPhong.getMaPhong());
             renderSeatGrid(seats);
         } else {
             renderSeatGrid(new ArrayList<>());
         }
         clearFields();
         deselectSeatButton(); // Also clears selectedGheNgoi
         updateButtonStates(); // Update buttons (Sua/Xoa should be disabled)
     }

     private void renderSeatGrid(List<GheNgoi> seats) {
         panelSeatGrid.removeAll();
         seatButtonMap.clear();
         deselectSeatButton(); // Clear selection state

         if (seats == null || seats.isEmpty()) {
             panelSeatGrid.setLayout(new BorderLayout());
             panelSeatGrid.add(new JLabel("Không có ghế nào trong phòng này hoặc không tải được.", SwingConstants.CENTER), BorderLayout.CENTER);
             panelSeatGrid.revalidate();
             panelSeatGrid.repaint();
             return;
         }

         Collections.sort(seats, Comparator.comparing(GheNgoi::getHang)
                                         .thenComparingInt(GheNgoi::getSoGhe));

         char maxRow = 'A';
         int maxCol = 0;
         for (GheNgoi seat : seats) {
            if (seat.getHang() != null && !seat.getHang().isEmpty()) { // Add null check for hang
                if (seat.getHang().charAt(0) > maxRow) {
                    maxRow = seat.getHang().charAt(0);
                }
            }
             if (seat.getSoGhe() > maxCol) {
                 maxCol = seat.getSoGhe();
             }
         }
         int numRows = maxRow - 'A' + 1;
         int numCols = maxCol;
         if (numRows <= 0 || numCols <= 0) { // Handle cases with invalid seat data
            panelSeatGrid.setLayout(new BorderLayout());
            panelSeatGrid.add(new JLabel("Dữ liệu ghế không hợp lệ để tạo sơ đồ.", SwingConstants.CENTER), BorderLayout.CENTER);
            panelSeatGrid.revalidate();
            panelSeatGrid.repaint();
            return;
         }


         panelSeatGrid.setLayout(new GridLayout(numRows, numCols, 5, 5));

         JButton[][] seatGrid = new JButton[numRows][numCols];

         ActionListener seatClickListener = e -> {
             if (currentState != EditState.IDLE) return;
             JButton clickedButton = (JButton) e.getSource();
             String maGhe = clickedButton.getActionCommand();

             GheNgoi newlySelectedGhe = GheNgoiDAO.findById(maGhe);

             if (newlySelectedGhe != null) {
                 // If the clicked seat is already the selected one, deselect it
                 if (newlySelectedGhe.equals(selectedGheNgoi)) {
                     deselectSeatButton();
                     clearFields();
                 } else {
                     // Otherwise, select the new seat
                     selectedGheNgoi = newlySelectedGhe; // Update the selected object FIRST
                     populateFieldsFromGheNgoi(selectedGheNgoi);
                     updateSelectedSeatButton(clickedButton); // Then update visual selection
                 }
             } else {
                 JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chi tiết cho ghế này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 deselectSeatButton(); // Clear selection state if error
                 clearFields();
             }
             updateButtonStates(); // Update buttons based on the new selection state
         };


         for (GheNgoi seat : seats) {
             if (seat.getHang() == null || seat.getHang().isEmpty()) continue; // Skip seats with invalid row

             int rowIndex = seat.getHang().charAt(0) - 'A';
             int colIndex = seat.getSoGhe() - 1;

             if (rowIndex >= 0 && rowIndex < numRows && colIndex >= 0 && colIndex < numCols) {
                 JButton seatButton = new JButton(seat.getHang() + seat.getSoGhe());
                 seatButton.setActionCommand(seat.getMaGhe());
                 seatButton.setToolTipText("Trạng thái: " + seat.getTrangThai());
                 seatButton.setBackground(getSeatColor(seat.getTrangThai()));
                 seatButton.setForeground(Color.WHITE);
                 seatButton.setOpaque(true);
                 seatButton.setBorderPainted(false);
                 seatButton.setFont(new Font("Arial", Font.BOLD, 10));
                 seatButton.setMargin(new Insets(2, 2, 2, 2));
                 seatButton.setPreferredSize(new Dimension(60, 40));
                 seatButton.addActionListener(seatClickListener);

                 seatGrid[rowIndex][colIndex] = seatButton;
                 seatButtonMap.put(seat.getMaGhe(), seatButton);
             }
         }

         for (int r = 0; r < numRows; r++) {
             for (int c = 0; c < numCols; c++) {
                 if (seatGrid[r][c] != null) {
                     panelSeatGrid.add(seatGrid[r][c]);
                 } else {
                     panelSeatGrid.add(new JLabel(""));
                 }
             }
         }

         panelSeatGrid.revalidate();
         panelSeatGrid.repaint();
     }

    private Color getSeatColor(String trangThai) {
        if (trangThai == null) return Color.DARK_GRAY;
        switch (trangThai) {
            case "Trống": return COLOR_TRONG;
            case "Đã đặt": return COLOR_DA_DAT;
            case "Bảo trì": return COLOR_BAO_TRI;
            default: return Color.DARK_GRAY;
        }
    }

    private void updateSelectedSeatButton(JButton newlySelected) {
        // Only deselect the OLD button if it's different from the new one
        if (selectedSeatButton != null && selectedSeatButton != newlySelected) {
             selectedSeatButton.setBorderPainted(false);
        }
        selectedSeatButton = newlySelected; // Update the reference
        if (selectedSeatButton != null) {
            selectedSeatButton.setBorder(new LineBorder(COLOR_SELECTED, 3));
            selectedSeatButton.setBorderPainted(true);
        }
    }

    // --- Modified deselectSeatButton ---
    private void deselectSeatButton() {
        // Only remove border from the currently selected button visually
        if (selectedSeatButton != null) {
            selectedSeatButton.setBorderPainted(false);
        }
        // Set both references to null
        selectedSeatButton = null;
        selectedGheNgoi = null;
    }


    private void populateFieldsFromGheNgoi(GheNgoi gheNgoi) {
        if (gheNgoi != null) {
            txtMaGhe.setText(gheNgoi.getMaGhe());
            txtHang.setText(gheNgoi.getHang());
            txtSoGhe.setText(String.valueOf(gheNgoi.getSoGhe()));
            comboBoxTrangThai.setSelectedItem(gheNgoi.getTrangThai());
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        txtMaGhe.setText("");
        txtHang.setText("");
        txtSoGhe.setText("");
        if (comboBoxTrangThai.getItemCount() > 0) {
            comboBoxTrangThai.setSelectedIndex(0);
        }
        // Don't clear selectedGheNgoi here - let deselectSeatButton handle it
    }

    private void setFieldsEditable(boolean isEditable) {
        txtHang.setEditable(isEditable);
        txtSoGhe.setEditable(isEditable);
        comboBoxTrangThai.setEnabled(isEditable);
        comboBoxPhong.setEnabled(!isEditable);

        txtMaGhe.setBackground(Color.LIGHT_GRAY);
        txtHang.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        txtSoGhe.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        comboBoxTrangThai.setBackground(isEditable ? Color.WHITE : Color.LIGHT_GRAY);
        comboBoxPhong.setBackground(!isEditable ? Color.WHITE : Color.LIGHT_GRAY);
    }

    private void updateButtonStates() {
        boolean isIdle = (currentState == EditState.IDLE);
        boolean seatSelected = (selectedGheNgoi != null);

        btnThem.setEnabled(isIdle);
        btnSua.setEnabled(isIdle && seatSelected);
        btnXoa.setEnabled(isIdle && seatSelected);
        btnLuu.setEnabled(!isIdle);
        btnHuy.setEnabled(!isIdle);
        btnThoat.setEnabled(isIdle);

        setFieldsEditable(!isIdle);
    }

    private void setInitialState() {
        currentState = EditState.IDLE;
        deselectSeatButton(); // This now also sets selectedGheNgoi to null
        clearFields();
        updateButtonStates();
    }

    private void enterAddMode() {
        PhongChieu selectedPhong = (PhongChieu) comboBoxPhong.getSelectedItem();
        if (selectedPhong == null) {
            showValidationError("Vui lòng chọn phòng chiếu trước khi thêm ghế.", comboBoxPhong);
            return;
        }
        currentState = EditState.ADDING;
        deselectSeatButton();
        clearFields();
        txtMaGhe.setText("(Tự động tạo)");
        updateButtonStates();
        txtHang.requestFocus();
    }

    private void enterEditMode() {
        if (selectedGheNgoi == null) {
            showValidationError("Vui lòng chọn một ghế để sửa.", panelSeatGrid);
            return;
        }
        currentState = EditState.EDITING;
        updateButtonStates();
        txtHang.requestFocus();
    }

    private void deleteSelectedGheNgoi() {
        if (selectedGheNgoi == null) {
            showValidationError("Vui lòng chọn một ghế để xóa.", panelSeatGrid);
            return;
        }
        String maGhe = selectedGheNgoi.getMaGhe();
        String viTri = selectedGheNgoi.getHang() + selectedGheNgoi.getSoGhe();
        String tenPhong = selectedGheNgoi.getPhongChieu() != null ? selectedGheNgoi.getPhongChieu().getTenPhong() : "N/A";
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa Ghế ngồi:\nMã: " + maGhe + "\nPhòng: " + tenPhong + "\nVị trí: " + viTri + "?",
                "Xác nhận xóa Ghế ngồi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = GheNgoiDAO.delete(maGhe);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa ghế ngồi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    handlePhongChieuSelectionChange(); // Reload grid calls setInitialState
                } else {
                    showError("Xóa ghế ngồi thất bại. Ghế có thể đang được tham chiếu trong Vé.", null);
                }
            } catch (Exception ex) {
                 showError("Lỗi khi xóa ghế.", ex);
            }
        }
    }

    private void saveGheNgoi() {
        PhongChieu selectedPhong = (PhongChieu) comboBoxPhong.getSelectedItem();
        String hangInput = txtHang.getText().trim().toUpperCase();
        String soGheStr = txtSoGhe.getText().trim();
        String trangThai = (String) comboBoxTrangThai.getSelectedItem();
        String currentMaGhe = txtMaGhe.getText().trim();

        if (selectedPhong == null) { showValidationError("Lỗi: Phòng chiếu không hợp lệ.", comboBoxPhong); return; }
        if (hangInput.isEmpty() || hangInput.length() != 1 || !Character.isLetter(hangInput.charAt(0))) { showValidationError("Hàng ghế phải là một chữ cái (A-Z).", txtHang); return; }
        if (soGheStr.isEmpty()) { showValidationError("Số ghế không được để trống.", txtSoGhe); return; }
        if (trangThai == null || trangThai.isEmpty()) { 
        	showValidationError("Vui lòng chọn trạng thái.", comboBoxTrangThai); 
        	System.err.println("");
        return; 
        }

        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) { 
        	showValidationError("Số ghế phải là một số nguyên dương.", txtSoGhe); 
        	System.err.println("");
        return; 
        }

        GheNgoi gheNgoi;
        boolean success = false;
        String successMessage = "";
        String errorMessage = "";
        String generatedMaGhe = selectedPhong.getMaPhong() + "-" + hangInput + String.format("%02d", soGhe);

        try {
            if (currentState == EditState.ADDING) {
                 GheNgoi existingSeat = GheNgoiDAO.findById(generatedMaGhe);
                 if (existingSeat != null) {
                    showValidationError("Ghế tại vị trí '" + hangInput + soGhe + "' trong phòng '" + selectedPhong.getTenPhong() + "' đã tồn tại (Mã: " + generatedMaGhe + ").", txtHang);
                    return;
                 }
                gheNgoi = new GheNgoi(generatedMaGhe, selectedPhong, hangInput, soGhe, trangThai);
                success = GheNgoiDAO.create(gheNgoi);
                successMessage = "Thêm ghế ngồi thành công!";
                errorMessage = "Thêm ghế ngồi thất bại.";

            } else {
                 if (selectedGheNgoi == null || currentMaGhe.equals("(Tự động tạo)") || currentMaGhe.isEmpty()) {
                     showError("Lỗi: Không xác định được ghế cần cập nhật.", null);
                     return;
                 }
                 GheNgoi conflictingSeat = GheNgoiDAO.findById(generatedMaGhe);
                 if (conflictingSeat != null && !conflictingSeat.getMaGhe().equals(currentMaGhe)) {
                     showValidationError("Vị trí ghế '" + hangInput + soGhe + "' trong phòng '" + selectedPhong.getTenPhong() + "' đã tồn tại cho một ghế khác.", txtHang);
                     return;
                 }
                 gheNgoi = new GheNgoi(currentMaGhe, selectedPhong, hangInput, soGhe, trangThai);
                 success = GheNgoiDAO.update(gheNgoi);
                 successMessage = "Cập nhật ghế ngồi thành công!";
                 errorMessage = "Cập nhật ghế ngồi thất bại.";
            }

            if (success) {
                JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                handlePhongChieuSelectionChange();
            } else {
                 showValidationError(errorMessage, currentState == EditState.ADDING ? txtHang : comboBoxTrangThai);
                 if(currentState == EditState.ADDING && !success) {
                     txtMaGhe.setText("(Tự động tạo)");
                 }
            }
        } catch (Exception ex) {
            showError("Lỗi khi lưu ghế.", ex);
        }
    }

    private void cancelEditMode() {
        setInitialState(); // This clears object and visual selection now
        // No need to re-select button visually, user will click again if needed.
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
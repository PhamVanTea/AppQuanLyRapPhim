package UI;

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

import DAO.GheNgoiDAO;
import DAO.PhongChieuDAO;
import Entity.GheNgoi;
import Entity.PhongChieu;

import javax.swing.border.LineBorder;

public class GheNgoiUI extends JPanel implements ActionListener {
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
    private Map<String, JButton> seatButtonMap; // Map lưu trữ ghế tương ứng với nút
    private enum EditState { IDLE, ADDING, EDITING }
    private EditState currentState = EditState.IDLE;
    private GheNgoi selectedGheNgoi = null;	//theo dõi ghế đang được chọn
    private JButton nutGheDangChon = null; //theo dõi ghế đang được chọn
    private final String[] TRANG_THAI_OPTIONS = {"Trống", "Đã đặt", "Bảo trì"};
    private final Color COLOR_TRONG = new Color(0, 153, 0);
    private final Color COLOR_DA_DAT = new Color(204, 0, 0);
    private final Color COLOR_BAO_TRI = Color.GRAY;
    private final Color COLOR_SELECTED = Color.ORANGE;

    //GD QL Ghế ngồi
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
        JScrollPane scrollPane = new JScrollPane(panelSeatGrid);	//bọc trong JScroll
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
        comboBoxPhong.addActionListener(e -> capNhatThayDoiPhongChieu());  //sự kiện khi chọn một mục trong combobox ->khi xảy ra sk - capNhatThayDoiPhongChieu() được gọi

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
//        btnThem.addActionListener(e -> enterAddMode());
        btnThem.addActionListener(this);
        panelChucNang.add(btnThem);

        btnSua = new JButton("Sửa TT Ghế");
        btnSua.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSua.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSua.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-edit-20.png")));
        btnSua.setEnabled(false);
//        btnSua.addActionListener(e -> enterEditMode());
        btnSua.addActionListener(this);
        panelChucNang.add(btnSua);

        btnXoa = new JButton("Xóa Ghế");
        btnXoa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXoa.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-delete-20.png")));
        btnXoa.setEnabled(false);
//        btnXoa.addActionListener(e -> deleteSelectedGheNgoi());
        btnXoa.addActionListener(this);
        panelChucNang.add(btnXoa);

        btnLuu = new JButton("Lưu");
        btnLuu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLuu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLuu.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-save-20.png")));
        btnLuu.setEnabled(false);
//        btnLuu.addActionListener(e -> saveGheNgoi());
        btnLuu.addActionListener(this);
        panelChucNang.add(btnLuu);

        btnHuy = new JButton("Hủy");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuy.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-cancel-20.png")));
        btnHuy.setEnabled(false);
//        btnHuy.addActionListener(e -> cancelEditMode());
        btnHuy.addActionListener(this);
        panelChucNang.add(btnHuy);

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(GheNgoiUI.class.getResource("/icons/icons8-exit-20.png")));
//        btnThoat.addActionListener(e -> {
//             int confirm = JOptionPane.showConfirmDialog(
//                    GheNgoiUI.this,
//                    "Bạn có chắc chắn muốn thoát?",
//                    "Xác nhận thoát",
//                    JOptionPane.YES_NO_OPTION
//                );
//             if (confirm == JOptionPane.YES_OPTION) {
//                 Window window = SwingUtilities.getWindowAncestor(this);
//                 if (window != null) {
//                     window.dispose();
//                 } else {
//                     System.exit(0);
//                 }
//             }
//        });
        btnThoat.addActionListener(this);
        panelChucNang.add(btnThoat);

        loadPhongChieuComboBox();
        setInitialState();
        if (comboBoxPhong.getItemCount() > 0) {
            comboBoxPhong.setSelectedIndex(0);
             capNhatThayDoiPhongChieu();
        } else {
            renderSeatGrid(new ArrayList<>());
        }
    }

    //Dl - Phòng Chiếu
    //Nạp ds Phòng chiếu vào COMBOBOX
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

    //DL- Phòng Chiếu
    //Ktra trạng thái , load ghế mới - Khi chọn phòng chiếu khác
     private void capNhatThayDoiPhongChieu() {	//PT lấy ds ghế (List<GheNgoi>) theo (maPhong) -> gọi GheNgoiDAO.searchByPhongChieu(selectedPhong.getMaPhong())
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
             List<GheNgoi> seats = GheNgoiDAO.timTheoMaPhongChieu(selectedPhong.getMaPhong());
             renderSeatGrid(seats);
         } else {
             renderSeatGrid(new ArrayList<>());
         }
         clearFields();
         boChon(); // Also clears selectedGheNgoi
         updateButtonStates(); // Update buttons (Sua/Xoa should be disabled)
     }

     //SĐ Ghế ngồi
     //Tạo lưới ghế ngồi (dựa trên dl Ghế của Phòng Chiếu)
     private void renderSeatGrid(List<GheNgoi> seats) {
         panelSeatGrid.removeAll();
         seatButtonMap.clear();
         boChon(); // Clear selection state

         if (seats == null || seats.isEmpty()) {	//xử lý TH không có dl ghế
             panelSeatGrid.setLayout(new BorderLayout());
             panelSeatGrid.add(new JLabel("Không có ghế nào trong phòng này hoặc không tải được.", SwingConstants.CENTER), BorderLayout.CENTER);
             panelSeatGrid.revalidate();	//refresh cập nhật lại layout
             panelSeatGrid.repaint();
             return;
         }
         
         //sx ds ghế - getHang theo chữ cái - getSoghe trong cùng hàng xếp theo số tăng dần
         Collections.sort(seats, Comparator.comparing(GheNgoi::getHang).thenComparingInt(GheNgoi::getSoGhe));

         char maxRow = 'A'; //lấy A là gtri nhỏ nhất để so sánh sx
         int maxCol = 0;
         
         for (GheNgoi seat : seats) {	//Vòng lặp tìm gtri lớn nhất
            if (seat.getHang() != null && !seat.getHang().isEmpty()) { 
                if (seat.getHang().charAt(0) > maxRow) {
                    maxRow = seat.getHang().charAt(0);
                }
            }
             if (seat.getSoGhe() > maxCol) {
                 maxCol = seat.getSoGhe();
             }
         }
         
         //Tính số hàng, số cột
         int numRows = maxRow - 'A' + 1;
         int numCols = maxCol;
         
         
         if (numRows <= 0 || numCols <= 0) { 	//TH giá trị hàng, cột k hợp lệ
            panelSeatGrid.setLayout(new BorderLayout());
            panelSeatGrid.add(new JLabel("Dữ liệu ghế không hợp lệ để tạo sơ đồ.", SwingConstants.CENTER), BorderLayout.CENTER);	//swingconstants căn giữa VB
            panelSeatGrid.revalidate();
            panelSeatGrid.repaint();
            return;
         }


         panelSeatGrid.setLayout(new GridLayout(numRows, numCols, 5, 5)); //Tạo sơ đồ ghế với GridLayout -(số hàng(hang), số cột(maxCol), k/c nang 5, k/c dọc 5)

         JButton[][] seatGrid = new JButton[numRows][numCols];

         ActionListener seatClickListener = e -> {	//xử lý sk nhấn chuột vào nút ghế 
             if (currentState != EditState.IDLE) return;	//trạng thái hiện tại ở chế độ thêm/sửa -> k xử lý sk clik chuột
             JButton clickedButton = (JButton) e.getSource();
             String maGhe = clickedButton.getActionCommand();	//lấy mã ghế khi nhấn

             GheNgoi gheMoiDuocChon = GheNgoiDAO.timTheoMaGhe(maGhe);

             if (gheMoiDuocChon != null) {	//tìm thấy ghế 
                 if (gheMoiDuocChon.equals(selectedGheNgoi)) {
                     boChon();
                     clearFields();
                 } else {
                     
                     selectedGheNgoi = gheMoiDuocChon; // Update the selected object FIRST
                     dienThongTinTuGhe(selectedGheNgoi);
                     capNhatNutGheChon(clickedButton); // cap nhat mau...
                 }
             } else {	//khi null
                 JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin chi tiết cho ghế này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 boChon(); // Clear nếu error
                 clearFields();
             }
             updateButtonStates(); // Update buttons based on the new selection state
         };

         
         	//TẠO NÚT GHẾ
         for (GheNgoi seat : seats) {
             if (seat.getHang() == null || seat.getHang().isEmpty()) continue; // ktra nếu rỗng -> bỏ qua bằng continue
             //Tính chỉ số trang mảng
             int rowIndex = seat.getHang().charAt(0) - 'A';		//chỉ số hàng
             int colIndex = seat.getSoGhe() - 1;		//chỉ số cột

             if (rowIndex >= 0 && rowIndex < numRows && colIndex >= 0 && colIndex < numCols) {	//ktra chỉ số có nằm trong giới hạn kích thước lưới
                 JButton seatButton = new JButton(seat.getHang() + seat.getSoGhe());	//Tạo button Nút ghế Hàng+Cột
                 seatButton.setActionCommand(seat.getMaGhe());	//đặt ActionCommand vào mã ghế để nhận diện khi nhấp
                 seatButton.setToolTipText("Trạng thái: " + seat.getTrangThai());
                 seatButton.setBackground(datColorTrangThai(seat.getTrangThai()));
                 seatButton.setForeground(Color.WHITE);
                 seatButton.setOpaque(true);
                 seatButton.setBorderPainted(false);	//viền mặc định - false
                 seatButton.setFont(new Font("Arial", Font.BOLD, 10));
                 seatButton.setMargin(new Insets(2, 2, 2, 2));
                 seatButton.setPreferredSize(new Dimension(60, 40));	//đặt kích thước mặc định cho ô ghế
                 seatButton.addActionListener(seatClickListener);	//xử lý sk nhấp chuột

                 seatGrid[rowIndex][colIndex] = seatButton;		//Đặt nút vào vd tương ứng trong mản - VD: B3-seatGrid[1][2]
                 seatButtonMap.put(seat.getMaGhe(), seatButton);
             }
         }

         	//Thêm các nút vô panel để hiển thị lên GD
         for (int r = 0; r < numRows; r++) {
             for (int c = 0; c < numCols; c++) {
                 if (seatGrid[r][c] != null) {
                     panelSeatGrid.add(seatGrid[r][c]);
                 } else {
                     panelSeatGrid.add(new JLabel(""));	//nếu k có nút, thêm jlabel rỗng giữ cấu trúc lưới
                 }
             }
         }
         	//refresh gd
         panelSeatGrid.revalidate();
         panelSeatGrid.repaint();
     }


    //SĐ ghế ngồi
    //Chọn ghế: cập nhật thông tin hiển thị và trạng thái giao diện
    private void capNhatNutGheChon(JButton newlySelected) {
        // Only deselect the OLD button if it's different from the new one
        if (nutGheDangChon != null && nutGheDangChon != newlySelected) {
             nutGheDangChon.setBorderPainted(false);	//bỏ viền
        }
        nutGheDangChon = newlySelected; // Update the reference
        if (nutGheDangChon != null) {
            nutGheDangChon.setBorder(new LineBorder(COLOR_SELECTED, 3));	//viền màu 3px
            nutGheDangChon.setBorderPainted(true);	//hiển thị viền
        }
    }

    //SĐ ghế ngồi
    //Bỏ chọn ghế đang chọn
    private void boChon() {
        // Only remove border from the currently selected button visually
        if (nutGheDangChon != null) {
            nutGheDangChon.setBorderPainted(false);
        }
        // Set both references to null
        nutGheDangChon = null;
        selectedGheNgoi = null;
    }

    //SĐ ghế ngồi
    // Gán dữ liệu từ đối tượng ghế vào các ô nhập liệu
    private void dienThongTinTuGhe(GheNgoi gheNgoi) {
        if (gheNgoi != null) {
            txtMaGhe.setText(gheNgoi.getMaGhe());
            txtHang.setText(gheNgoi.getHang());
            txtSoGhe.setText(String.valueOf(gheNgoi.getSoGhe()));
            comboBoxTrangThai.setSelectedItem(gheNgoi.getTrangThai());
        } else {
            clearFields();
        }
    }
    
    private Color datColorTrangThai(String trangThai) {
        if (trangThai == null) return Color.DARK_GRAY;
        switch (trangThai) {		//Combobox trạng thái ghế: Trống, đã đặt, bảo trì
            case "Trống": return COLOR_TRONG;
            case "Đã đặt": return COLOR_DA_DAT;
            case "Bảo trì": return COLOR_BAO_TRI;
            default: return Color.DARK_GRAY;
        }
    }

    private void clearFields() {
        txtMaGhe.setText("");
        txtHang.setText("");
        txtSoGhe.setText("");
        if (comboBoxTrangThai.getItemCount() > 0) {
            comboBoxTrangThai.setSelectedIndex(0);
        }
        // Don't clear selectedGheNgoi here - let boChon handle it
    }

    //Chức năng trạng thái
    // Cho phép nhập dữ liệu (khi thêm/sửa)
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

    //Chức năng trạng thái
    // Cập nhật trạng thái các nút và khả năng chỉnh sửa của các ô
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

    //Chức năng trạng thái
    //Đặt lại trạng thái ban đầu
    private void setInitialState() {
        currentState = EditState.IDLE;
        boChon(); //  set selectedGheNgoi null
        clearFields();
        updateButtonStates();
    }

    //Chức năng
    //Thêm ghế mới
    private void enterAddMode() {
        PhongChieu selectedPhong = (PhongChieu) comboBoxPhong.getSelectedItem();
        if (selectedPhong == null) {
            showValidationError("Vui lòng chọn phòng chiếu trước khi thêm ghế.", comboBoxPhong);
            return;
        }
        currentState = EditState.ADDING;
        boChon();
        clearFields();
        txtMaGhe.setText("(Tự động tạo)");
        updateButtonStates();
        txtHang.requestFocus();
    }

    //Chức năng
    //Sửa thông tin ghế đã chọn
    private void enterEditMode() {
        if (selectedGheNgoi == null) {
            showValidationError("Vui lòng chọn một ghế để sửa.", panelSeatGrid); //panelSeatGrid chứa sơ đồ ghế
            return;
        }
        currentState = EditState.EDITING;
        updateButtonStates();
        txtHang.requestFocus();
    }

    //Chức năng
    //Xóa ghế đang chọn
    private void deleteSelectedGheNgoi() {
        if (selectedGheNgoi == null) {
            showValidationError("Vui lòng chọn một ghế để xóa.", panelSeatGrid);
            return;
        }
        String maGhe = selectedGheNgoi.getMaGhe();
        String viTri = selectedGheNgoi.getHang() + selectedGheNgoi.getSoGhe();
        String tenPhong = selectedGheNgoi.getPhongChieu() != null ? selectedGheNgoi.getPhongChieu().getTenPhong() : "N/A";
        //Xác nhận lại
        int choice = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa Ghế ngồi:\nMã: " + maGhe + "\nPhòng: " + tenPhong + "\nVị trí: " + viTri + "?",
                "Xác nhận xóa Ghế ngồi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                boolean success = GheNgoiDAO.xoa(maGhe);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Xóa ghế ngồi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    capNhatThayDoiPhongChieu(); // Reload grid calls setInitialState
                } else {
                    showError("Xóa ghế ngồi thất bại. Ghế có thể đang được tham chiếu trong Vé.", null);
                }
            } catch (Exception ex) {
                 showError("Lỗi khi xóa ghế.", ex);
            }
        }
    }

    //Chức năng
    //Lưu dl khi thêm/sửa ghế (kèm kiểm tra trùng mã, định dạng, trạng thái)
    private void saveGheNgoi() {
        PhongChieu selectedPhong = (PhongChieu) comboBoxPhong.getSelectedItem();
        String hangInput = txtHang.getText().trim().toUpperCase();
        String soGheStr = txtSoGhe.getText().trim();
        String trangThai = (String) comboBoxTrangThai.getSelectedItem();
        String currentMaGhe = txtMaGhe.getText().trim();

        if (selectedPhong == null) { showValidationError("Lỗi: Phòng chiếu không hợp lệ.", comboBoxPhong); return; }
//        if (hangInput.isEmpty() || hangInput.length() != 1 || !Character.isLetter(hangInput.charAt(0))) { 
//        	showValidationError("Hàng ghế phải là một chữ cái (A-Z).", txtHang); 
//        	throw new RuntimeException("Lỗi: Chưa nhập Hàng ghế / Nhập sai định dạng");
////        	return; 
//        	}
        if (hangInput.isEmpty()) {
        	showValidationError("Hàng ghế không được để trống", txtHang);
        	throw new RuntimeException("Lỗi: Chưa nhập hàng ghế!");
        }
        
        if (!hangInput.matches("^[A-Z]{1,2}$")) {
        	//hangInPut . toUpperCase() tự động chuyển từ in thường sang từ in hoa khi nhập
        	showValidationError("Hàng ghế chỉ cho phép 1 hoặc 2 chữ cái in hoa không dấu liên tiếp", txtHang);
        	throw new RuntimeException("Lỗi: Hàng ghế nhập không đúng định dạng");
        }
        
        if (soGheStr.isEmpty()) {
        	showValidationError("Số ghế không được để trống.", txtSoGhe); 
//        	return; 
        	throw new RuntimeException("Lỗi: Chưa nhập số ghế!");
        	}
        
        if (trangThai == null || trangThai.isEmpty()) { 
        	showValidationError("Vui lòng chọn trạng thái.", comboBoxTrangThai); 
//        	System.err.println("");
//        return; 
        	throw new RuntimeException("Lỗi: Chưa chọn trạng thái ghế trong ComboBox");
        }

        int soGhe;
        try {
            soGhe = Integer.parseInt(soGheStr);
            if (soGhe <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) { 
        	showValidationError("Số ghế phải là một số nguyên dương.", txtSoGhe); 
//        	System.err.println("");
//        return; 
        	throw new RuntimeException("Lỗi: Số ghế nhập vào không hợp lệ!");
        }

        GheNgoi gheNgoi;
        boolean success = false;
        String successMessage = "";
        String errorMessage = "";
        String generatedMaGhe = selectedPhong.getMaPhong() + "-" + hangInput + String.format("%02d", soGhe);

        try {
            if (currentState == EditState.ADDING) {
                 GheNgoi existingSeat = GheNgoiDAO.timTheoMaGhe(generatedMaGhe);
                 if (existingSeat != null) {
                    showValidationError("Ghế tại vị trí '" + hangInput + soGhe + "' trong phòng '" + selectedPhong.getTenPhong() + "' đã tồn tại (Mã: " + generatedMaGhe + ").", txtHang);
                    return;
                 }
                gheNgoi = new GheNgoi(generatedMaGhe, selectedPhong, hangInput, soGhe, trangThai);
                success = GheNgoiDAO.tao(gheNgoi);
                successMessage = "Thêm ghế ngồi thành công!";
                errorMessage = "Thêm ghế ngồi thất bại.";

            } else {
                 if (selectedGheNgoi == null || currentMaGhe.equals("(Tự động tạo)") || currentMaGhe.isEmpty()) {
                     showError("Lỗi: Không xác định được ghế cần cập nhật.", null);
                     return;
                 }
                 GheNgoi conflictingSeat = GheNgoiDAO.timTheoMaGhe(generatedMaGhe);
                 if (conflictingSeat != null && !conflictingSeat.getMaGhe().equals(currentMaGhe)) {
                     showValidationError("Vị trí ghế '" + hangInput + soGhe + "' trong phòng '" + selectedPhong.getTenPhong() + "' đã tồn tại cho một ghế khác.", txtHang);
                     return;
                 }
                 gheNgoi = new GheNgoi(currentMaGhe, selectedPhong, hangInput, soGhe, trangThai);
                 success = GheNgoiDAO.capNhat(gheNgoi);
                 successMessage = "Cập nhật ghế ngồi thành công!";
                 errorMessage = "Cập nhật ghế ngồi thất bại.";
            }

            if (success) {
                JOptionPane.showMessageDialog(this, successMessage, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                capNhatThayDoiPhongChieu();
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

    //Chức năng
    // Hủy thao tác thêm/sửa và quay lại trạng thái ban đầu
    private void cancelEditMode() {
        setInitialState(); //xóa dtuong và hủy chọn hiển thị hiện tại
//        hủy chế độ chỉnh sửa, giao diện về trạng thái ban đầu - không cần phải thao tác lại trên các nút chức năng.
    }

    
    //HT Lỗi
    //Hiển thị tb lỗi nhập liệu - Focus lại ô lỗi
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

     //HT Lỗi
     //Hiển thị lỗi Hệ thống
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
	        deleteSelectedGheNgoi();
	    } else if (o.equals(btnLuu)) {
	        saveGheNgoi();
	    } else if (o.equals(btnHuy)) {
	        cancelEditMode();
	    } else if (o.equals(btnThoat)) {
	        int confirm = JOptionPane.showConfirmDialog(this,
	                "Bạn có chắc chắn muốn thoát?", "Xác nhận thoát",
	                JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            Window window = SwingUtilities.getWindowAncestor(this);
	            if (window != null) {
	                window.dispose();
	            } else {
	                System.exit(0);
	            }
	        }
	    }
		
	}
}
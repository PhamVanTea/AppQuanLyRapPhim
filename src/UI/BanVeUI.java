package UI;

import session.StaticVariable;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

//thư viện pdf
import com.lowagie.text.Chunk; //tạo văn bản 
import com.lowagie.text.Document; //tài liệu pdf
import com.lowagie.text.DocumentException; 
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell; //tạo bảng trong pdf
import com.lowagie.text.pdf.PdfPTable; //tạo bảng trong pdf
import com.lowagie.text.pdf.PdfWriter; //ghi nội dung vào pdf

//đoạn code sử dụng iText để xây dựng file PDF hóa đơn bán vé.
//nội dung PDF gồm tiêu đề, thông tin hóa đơn, chi tiết vé, tổng tiền và chân trang.
//font tiếng Việt được xử lý bằng font DejaVuSans được nhúng vào PDF.
//file PDF được lưu ra ổ đĩa và tự động mở sau khi tạo thành công.
//việc tạo PDF được tích hợp chặt chẽ trong quy trình đặt vé, giúp người dùng có thể in hoặc lưu hóa đơn nhanh chóng.

import DAO.*;
import Entity.*;
//JPanel là một thành phần (component) trong thư viện Swing dùng để nhóm nhiều thành phần con
//(JButton, JTable, JTextField, v.v.) lại với nhau. Bạn có thể xem nó như một "khối xây dựng giao diện".
// Lớp BanVeUI kế thừa từ JPanel và implements ActionListener
public class BanVeUI extends JPanel implements ActionListener {
    // Khai báo các thành phần giao diện và biến cần thiết
    private JTable tableSuatChieu;
    private JTextField txtMaNhanVien;
    private JTextField txtTongTien;
    private DefaultTableModel tableModelSuatChieu;
    private JButton btnDatVe;
    private JButton btnHuyChon;
    private JButton btnThoat;
    private JPanel panelSeatGrid;
    private Map<String, JButton> seatButtonMap;
    private List<GheNgoi> selectedSeatsList;
    private SuatChieu selectedSuatChieu = null;
    private KhachHang selectedKhachHang = null;
    private NhanVien currentNhanVien = (StaticVariable.nhanVien != null) ? StaticVariable.nhanVien : new NhanVien("NV000", "N/A");

    // Khai báo các màu sắc và định dạng
    private final Color COLOR_TRONG = new Color(0, 153, 0);
    private final Color COLOR_DA_DAT =  Color.RED;
    private final Color COLOR_BAO_TRI = Color.GRAY;
    private final Color COLOR_SELECTED = new Color(255, 153, 0);
    private final Color COLOR_GHE_THUONG = Color.GREEN;
    private final Color COLOR_GHE_VIP = new Color(255, 193, 37);
    private final DecimalFormat currencyFormatter = new DecimalFormat("#,##0 VNĐ");
    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat fileDateTimeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private final SimpleDateFormat dbDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static com.lowagie.text.Font fontTitle;
    private static com.lowagie.text.Font fontHeader;
    private static com.lowagie.text.Font fontNormal;
    private static com.lowagie.text.Font fontBold;
    private static String FONT_PATH = "/fonts/DejaVuSans.ttf"; //đoạn code tải font tiếng việt để đảm bảo PDF hiển thị tiếng Việt đúng.
    private JTextField txtKhachHang;

    // Khối static để tải font cho PDF
    static {
        try {
            URL fontUrl = BanVeUI.class.getResource(FONT_PATH);
            String fontPath = fontUrl.getPath();
            BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // Tạo các kiểu font từ BaseFont đã load
            fontTitle = new com.lowagie.text.Font(bf, 18, Font.BOLD);
            fontHeader = new com.lowagie.text.Font(bf, 12, Font.BOLD);
            fontNormal = new com.lowagie.text.Font(bf, 11, Font.PLAIN);
            fontBold = new com.lowagie.text.Font(bf, 11, Font.BOLD);
            System.out.println("Font " + FONT_PATH + " loaded successfully for PDF.");

        } catch (DocumentException | IOException e) {
            System.err.println("!!! Lỗi nghiêm trọng: Không thể load font Tiếng Việt cho PDF tại: " + FONT_PATH);
            System.err.println("!!! PDF sẽ không hiển thị đúng tiếng Việt.");
            e.printStackTrace();

            fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD);
            fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD);
            fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.PLAIN);
            fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
        }
    }

    // Khởi tạo giao diện bán vé
    public BanVeUI() {
        seatButtonMap = new HashMap<>();
        selectedSeatsList = new ArrayList<>();

        setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

        // Tạo bảng danh sách suất chiếu
        JPanel panelDanhSach_1 = new JPanel();
        panelDanhSach_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chọn Suất Chiếu Hôm Nay", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelDanhSach_1.setBounds(38, 15, 927, 140);
        add(panelDanhSach_1);
        panelDanhSach_1.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        panelDanhSach_1.add(scrollPane_1, BorderLayout.CENTER);

        tableSuatChieu = new JTable();
        tableModelSuatChieu = new DefaultTableModel() {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tableSuatChieu.setModel(tableModelSuatChieu);
        tableSuatChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSuatChieu.setRowHeight(25);
        tableSuatChieu.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableSuatChieu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scrollPane_1.setViewportView(tableSuatChieu);

        tableSuatChieu.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    handleSuatChieuSelectionChange();
                }
            }
        });

        // Tạo sơ đồ ghế ngồi
        JPanel panelSeatDisplay = new JPanel();
        panelSeatDisplay.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Sơ đồ Ghế Ngồi", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelSeatDisplay.setBounds(38, 165, 927, 270);
        add(panelSeatDisplay);
        panelSeatDisplay.setLayout(new BorderLayout(0, 0));

        panelSeatGrid = new JPanel();
        panelSeatGrid.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPaneSeats = new JScrollPane(panelSeatGrid);
        scrollPaneSeats.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneSeats.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panelSeatDisplay.add(scrollPaneSeats, BorderLayout.CENTER);

        // Tạo panel nhập thông tin đặt vé
        JPanel panelInput = new JPanel();
        panelInput.setLayout(null);
        panelInput.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Thông Tin Đặt Vé", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelInput.setBounds(38, 445, 927, 130);
        add(panelInput);

        int labelX1 = 30;
        int fieldX1 = 140;
        int labelX2 = 480;
        int fieldX2 = 600;
        int fieldW = 290;
        int labelW = 100;
        int fieldH = 25;
        int row1Y = 30;
        int row2Y = row1Y + 40;

        JLabel lblKhachHang = new JLabel("Khách hàng:");
        lblKhachHang.setBounds(labelX1, row1Y, labelW, fieldH);
        panelInput.add(lblKhachHang);

        JLabel lblMaNhanVien = new JLabel("Nhân viên:");
        lblMaNhanVien.setBounds(labelX2, row1Y, labelW, fieldH);
        panelInput.add(lblMaNhanVien);

        txtMaNhanVien = new JTextField();
        txtMaNhanVien.setEditable(false);
        txtMaNhanVien.setBackground(Color.LIGHT_GRAY);
        txtMaNhanVien.setBounds(fieldX2, row1Y, fieldW, fieldH);
        txtMaNhanVien.setText(StaticVariable.nhanVien != null ? StaticVariable.nhanVien.getTenNhanVien() : "N/A");
        panelInput.add(txtMaNhanVien);

        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongTien.setBounds(labelX1, row2Y, labelW, fieldH);
        panelInput.add(lblTongTien);

        txtTongTien = new JTextField("0 VNĐ");
        txtTongTien.setEditable(false);
        txtTongTien.setBackground(Color.LIGHT_GRAY);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 13));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setBounds(fieldX1, row2Y, fieldW, fieldH);
        panelInput.add(txtTongTien);

        txtKhachHang = new JTextField();
        txtKhachHang.setBounds(140, 30, 290, 25);
        panelInput.add(txtKhachHang);
        txtKhachHang.setColumns(10);

        // Tạo panel chức năng
        JPanel panelChucNang = new JPanel();
        panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelChucNang.setBounds(38, 592, 927, 64);
        add(panelChucNang);
        panelChucNang.setLayout(new GridLayout(0, 3, 15, 0));

        btnDatVe = new JButton("Xác Nhận & Xuất PDF");
        btnDatVe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDatVe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDatVe.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-print-20.png")));
        btnDatVe.addActionListener(this); // Sử dụng this thay vì lambda
        btnDatVe.setEnabled(false);
        panelChucNang.add(btnDatVe);

        btnHuyChon = new JButton("Hủy Chọn Ghế");
        btnHuyChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuyChon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuyChon.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-cancel-20.png")));
        btnHuyChon.addActionListener(this); // Sử dụng this thay vì lambda
        btnHuyChon.setEnabled(false);
        panelChucNang.add(btnHuyChon);

        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-exit-20.png")));
        btnThoat.addActionListener(this); // Sử dụng this thay vì lambda
        panelChucNang.add(btnThoat);

        // Tải dữ liệu suất chiếu và thiết lập trạng thái ban đầu
        loadSuatChieuTable();
        setInitialState();
        setupAutoCompleteForTextField();
    }

    // Triển khai phương thức actionPerformed từ ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnDatVe)) {
            processTicketSaleAndPrint();
        } else if (o.equals(btnHuyChon)) {
            clearSelectionAndPrice();
        } else if (o.equals(btnThoat)) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc chắn muốn thoát khỏi màn hình Bán Vé?", "Xác nhận thoát",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    window.dispose();
                }
            } else {
                System.exit(0);
            }
        } else if (o instanceof JButton) {
            // Xử lý khi người dùng nhấn vào nút ghế
            JButton clickedButton = (JButton) o;
            if (seatButtonMap.containsValue(clickedButton)) {
                handleSeatButtonClick(clickedButton);
            }
        }
    }

    // Xử lý sự kiện khi nhấn vào nút ghế
    private void handleSeatButtonClick(JButton clickedButton) {
        String maGhe = clickedButton.getActionCommand();
        GheNgoi ghe = GheNgoiDAO.findById(maGhe);

        if (ghe != null) {
            if (selectedSeatsList.contains(ghe)) {
                selectedSeatsList.remove(ghe);
                boolean isMaintenance = "Bảo trì".equalsIgnoreCase(ghe.getTrangThai());
                if (isMaintenance) {
                    clickedButton.setBackground(COLOR_BAO_TRI);
                } else {
                    clickedButton.setBackground(COLOR_GHE_THUONG);
                    clickedButton.setBorder(UIManager.getBorder("Button.border"));
                }
            } else {
                selectedSeatsList.add(ghe);
                clickedButton.setBackground(COLOR_SELECTED);
                clickedButton.setBorder(new LineBorder(Color.BLACK, 2));
            }
            updateTotalPrice();
            updateButtonStates();
        } else {
            showError("Lỗi: Không tìm thấy thông tin ghế " + maGhe, null);
        }
    }

    // Lấy thông tin khách hàng từ text
    private KhachHang getKhachHangFromText() {
        String input = txtKhachHang.getText().trim();

        List<KhachHang> khList = KhachHangDAO.readAll();
        for (KhachHang kh : khList) {
            String display = kh.getTenKhachHang() + " - " + kh.getSDT();
            if (display.equalsIgnoreCase(input)) {
                return kh;
            }
        }
        return null;
    }

    // Thiết lập tự động hoàn thành cho trường khách hàng
    public void setupAutoCompleteForTextField() {
        List<KhachHang> khList = KhachHangDAO.readAll();

        // Xóa toàn bộ KeyListener cũ để tránh bị chồng
        for (KeyListener listener : txtKhachHang.getKeyListeners()) {
            txtKhachHang.removeKeyListener(listener);
        }

        txtKhachHang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = txtKhachHang.getText().trim().toLowerCase();

                    List<KhachHang> matched = khList.stream()
                            .filter(kh -> kh.getTenKhachHang().toLowerCase().contains(input)
                                    || kh.getSDT().toLowerCase().contains(input))
                            .collect(Collectors.toList());

                    if (matched.size() == 1) {
                        KhachHang selected = matched.get(0);
                        txtKhachHang.setText(selected.getTenKhachHang() + " - " + selected.getSDT());
                        // TODO: Gán selected vào biến xử lý tiếp
                    } else if (matched.size() > 1) {
                        JPopupMenu suggestionMenu = new JPopupMenu();

                        for (KhachHang kh : matched) {
                            JMenuItem item = new JMenuItem(kh.getTenKhachHang() + " - " + kh.getSDT());
                            item.addActionListener(ev -> {
                                txtKhachHang.setText(kh.getTenKhachHang() + " - " + kh.getSDT());
                                // TODO: Gán kh vào logic xử lý tiếp
                            });
                            suggestionMenu.add(item);
                        }

                        suggestionMenu.show(txtKhachHang, 0, txtKhachHang.getHeight());
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng phù hợp.");
                    }
                }
            }
        });
    }

    // Tải dữ liệu vào bảng suất chiếu
    public void loadSuatChieuTable() {
        try {
            List<SuatChieu> scList = SuatChieuDAO.getFutureShowtimes();
            if (tableModelSuatChieu.getColumnCount() == 0) {
                tableModelSuatChieu.setColumnIdentifiers(new Object[]{
                        "Mã SC", "Tên Phim", "Phòng", "Bắt Đầu", "Kết Thúc", "Giá Vé"
                });
                tableSuatChieu.getColumnModel().getColumn(0).setPreferredWidth(60);
                tableSuatChieu.getColumnModel().getColumn(1).setPreferredWidth(250);
                tableSuatChieu.getColumnModel().getColumn(2).setPreferredWidth(80);
                tableSuatChieu.getColumnModel().getColumn(3).setPreferredWidth(100);
                tableSuatChieu.getColumnModel().getColumn(4).setPreferredWidth(100);
                tableSuatChieu.getColumnModel().getColumn(5).setPreferredWidth(90);
            }
            tableModelSuatChieu.setRowCount(0);

            for (SuatChieu sc : scList) {
                tableModelSuatChieu.addRow(new Object[]{
                        sc.getMaSuatChieu(),
                        sc.getPhim() != null ? sc.getPhim().getTenPhim() : "N/A",
                        sc.getPhongChieu() != null ? sc.getPhongChieu().getTenPhong() : "N/A",
                        formatDateTime(sc.getThoiGianBD()),
                        formatDateTime(sc.getThoiGianKetThuc()),
                        currencyFormatter.format(sc.getGia())
                });
            }
        } catch (Exception e) {
            showError("Lỗi tải danh sách suất chiếu.", e);
        }
    }

    // Xử lý khi thay đổi lựa chọn suất chiếu
    private void handleSuatChieuSelectionChange() {
        int selectedRow = tableSuatChieu.getSelectedRow();
        selectedSeatsList.clear();
        updateTotalPrice();
        renderSeatGrid(new ArrayList<>(), new HashSet<>());

        if (selectedRow != -1) {
            try {
                String maSuatChieu = tableModelSuatChieu.getValueAt(tableSuatChieu.convertRowIndexToModel(selectedRow), 0).toString();
                selectedSuatChieu = SuatChieuDAO.findById(maSuatChieu);

                if (selectedSuatChieu != null && selectedSuatChieu.getPhongChieu() != null) {
                    String maPhong = selectedSuatChieu.getPhongChieu().getMaPhong();
                    List<GheNgoi> allSeatsInRoom = GheNgoiDAO.searchByPhongChieu(maPhong);
                    List<Ve> bookedTicketsForShowtime = VeDAO.findBySuatChieuId(selectedSuatChieu.getMaSuatChieu());

                    Set<String> bookedSeatIds = new HashSet<>();
                    for (Ve ve : bookedTicketsForShowtime) {
                        if (ve.getGheNgoi() != null) {
                            bookedSeatIds.add(ve.getGheNgoi().getMaGhe());
                        }
                    }
                    renderSeatGrid(allSeatsInRoom, bookedSeatIds);
                } else {
                    showError("Không tìm thấy thông tin suất chiếu hoặc phòng chiếu.", null);
                    selectedSuatChieu = null;
                }
            } catch (Exception e) {
                showError("Lỗi khi tải sơ đồ ghế.", e);
                selectedSuatChieu = null;
            }
        } else {
            selectedSuatChieu = null;
        }
        updateButtonStates();
    }

    // Vẽ sơ đồ ghế ngồi
    private void renderSeatGrid(List<GheNgoi> allSeats, Set<String> bookedSeatIds) {
        panelSeatGrid.removeAll();
        seatButtonMap.clear();

        if (allSeats.isEmpty()) {
            panelSeatGrid.setLayout(new FlowLayout());
            panelSeatGrid.add(new JLabel("Vui lòng chọn suất chiếu để xem sơ đồ ghế."));
            panelSeatGrid.revalidate();
            panelSeatGrid.repaint();
            return;
        }

        Collections.sort(allSeats, Comparator.comparing(GheNgoi::getHang).thenComparingInt(GheNgoi::getSoGhe));

        int maxRow = 0;
        int maxCol = 0;
        char maxHangChar = 'A';
        if (!allSeats.isEmpty()) {
            for (GheNgoi ghe : allSeats) {
                if (ghe.getHang() != null && !ghe.getHang().isEmpty()) {
                    if (ghe.getHang().charAt(0) > maxHangChar) {
                        maxHangChar = ghe.getHang().charAt(0);
                    }
                }
                if (ghe.getSoGhe() > maxCol) {
                    maxCol = ghe.getSoGhe();
                }
            }
            maxRow = maxHangChar - 'A' + 1;
        } else {
            maxRow = 20;
            maxCol = 20;
        }

        if (maxRow <= 0 || maxCol <= 0) {
            panelSeatGrid.setLayout(new FlowLayout());
            panelSeatGrid.add(new JLabel("Không có dữ liệu ghế hợp lệ cho phòng này."));
            panelSeatGrid.revalidate();
            panelSeatGrid.repaint();
            return;
        }

        panelSeatGrid.setLayout(new GridLayout(20, 20, 5, 5));
        Map<Point, GheNgoi> seatPositions = new HashMap<>();
        for (GheNgoi ghe : allSeats) {
            if (ghe.getHang() != null && !ghe.getHang().isEmpty()){
                int row = ghe.getHang().charAt(0) - 'A';
                int col = ghe.getSoGhe() - 1;
                if (row >= 0 && col >=0) {
                    seatPositions.put(new Point(col, row), ghe);
                }
            }
        }

        for (int r = 0; r < maxRow; r++) {
            for (int c = 0; c < maxCol; c++) {
                GheNgoi ghe = seatPositions.get(new Point(c, r));
                if (ghe != null) {
                    JButton seatButton = new JButton(ghe.getHang() + ghe.getSoGhe());
                    seatButton.setFont(new Font("Arial", Font.BOLD, 10));
                    seatButton.setMargin(new Insets(2, 2, 2, 2));
                    seatButton.setActionCommand(ghe.getMaGhe());
                    seatButton.setOpaque(true);
                    seatButton.setBorderPainted(true);

                    boolean isBooked = bookedSeatIds.contains(ghe.getMaGhe());
                    boolean isMaintenance = "Bảo trì".equalsIgnoreCase(ghe.getTrangThai());

                    if (isBooked) {
                        seatButton.setBackground(COLOR_DA_DAT);
                        seatButton.setForeground(Color.BLACK);
                        seatButton.setEnabled(false);
                    } else if (isMaintenance) {
                        seatButton.setBackground(COLOR_BAO_TRI);
                        seatButton.setForeground(Color.BLACK);
                        seatButton.setEnabled(false);
                        seatButton.setText(ghe.getHang() + ghe.getSoGhe() + "X");
                    } else {
                        seatButton.setBackground(COLOR_GHE_THUONG);
                        seatButton.setForeground(Color.BLACK);
                        seatButton.setEnabled(true);
                        seatButton.addActionListener(this); // Sử dụng this thay vì lambda
                    }

                    seatButtonMap.put(ghe.getMaGhe(), seatButton);
                    panelSeatGrid.add(seatButton);
                } else {
                    panelSeatGrid.add(new JLabel(""));
                }
            }
        }

        panelSeatGrid.revalidate();
        panelSeatGrid.repaint();
    }

    // Cập nhật tổng tiền
    private void updateTotalPrice() {
        if (selectedSuatChieu != null && !selectedSeatsList.isEmpty()) {
            float giaVe = selectedSuatChieu.getGia();
            float total = giaVe * selectedSeatsList.size();
            txtTongTien.setText(currencyFormatter.format(total));
        } else {
            txtTongTien.setText(currencyFormatter.format(0));
        }
    }

    // Xóa lựa chọn ghế và cập nhật giá
    private void clearSelectionAndPrice() {
        for(GheNgoi ghe : selectedSeatsList) {
            JButton btn = seatButtonMap.get(ghe.getMaGhe());
            if (btn != null && btn.isEnabled()) {
                btn.setBackground(COLOR_GHE_THUONG);
                btn.setBorder(UIManager.getBorder("Button.border"));
            }
        }
        selectedSeatsList.clear();
        updateTotalPrice();
        updateButtonStates();
    }

    // Xử lý bán vé và in hóa đơn
    private void processTicketSaleAndPrint() {
        // Xác định khách hàng từ trường nhập liệu
        selectedKhachHang = getKhachHangFromText();

        // Kiểm tra các điều kiện trước khi tiếp tục
        if (selectedSuatChieu == null) {
            showValidationError("Vui lòng chọn một suất chiếu.", tableSuatChieu);
            System.err.println("Vui lòng chọn một suất chiếu.");
            return;
        }
        if (selectedKhachHang == null) {
            showValidationError("Vui lòng chọn khách hàng.", txtKhachHang);
            throw new RuntimeException("Vui lòng chọn khách hàng.");
        }
        if (selectedSeatsList.isEmpty()) {
            showValidationError("Vui lòng chọn ít nhất một ghế.", panelSeatGrid);
            throw new RuntimeException("Vui lòng chọn ít nhất một ghế.");
        }
        if (StaticVariable.nhanVien == null) {
            showError("Lỗi: Không xác định được nhân viên đang đăng nhập.", null);
            return;
        }

        // Tạo hóa đơn và danh sách vé
        String maHoaDon = "HD" + System.currentTimeMillis() % 1000000;
        float tongTien = selectedSuatChieu.getGia() * selectedSeatsList.size();
        String ngayLap = dbDateTimeFormatter.format(new Date());
        String trangThaiHD = "Đã thanh toán";

        HoaDon hoaDon = new HoaDon(maHoaDon, StaticVariable.nhanVien, selectedKhachHang, tongTien, ngayLap, trangThaiHD);
        List<Ve> veList = new ArrayList<>();

        boolean transactionSuccess = false;

        try {
            boolean hoaDonCreated = HoaDonDAO.create(hoaDon);

            if (!hoaDonCreated) {
                throw new SQLException("Không thể tạo hóa đơn.");
            }

            float giaVe = selectedSuatChieu.getGia();
            for (GheNgoi ghe : selectedSeatsList) {
                String maVe = "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                Ve ve = new Ve(maVe, hoaDon, selectedSuatChieu, giaVe, ghe);
                boolean veCreated = VeDAO.create(ve);
                if (!veCreated) {
                    throw new SQLException("Không thể tạo vé cho ghế: " + ghe.getMaGhe());
                }
                veList.add(ve);
            }

            transactionSuccess = true;

        } catch (SQLException e) {
            showError("Lỗi giao dịch khi đặt vé: " + e.getMessage(), e);
        }

        // Nếu giao dịch thành công, tự động lưu file PDF vào ổ D
        if (transactionSuccess) {
            String suggestedFileName = "HoaDon_" + maHoaDon + "_" + fileDateTimeFormatter.format(new Date()) + ".pdf";
            File fileToSave = new File("D:\\" + suggestedFileName); // Đường dẫn lưu mặc định ở ổ D

            // Ghi hóa đơn ra file PDF
            if (writeInvoiceToPdf(fileToSave, hoaDon, veList)) {
                JOptionPane.showMessageDialog(this,
                        "Đặt vé thành công! Hóa đơn PDF đã được lưu vào:\n" + fileToSave.getAbsolutePath(),
                        "Đặt Vé Thành Công & Đã Lưu PDF",
                        JOptionPane.INFORMATION_MESSAGE);
                // Mở file PDF
                try {
                    Desktop.getDesktop().open(fileToSave);
                } catch (IOException e) {
                    showError("Lỗi khi mở file PDF: " + e.getMessage(), e);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Đặt vé thành công nhưng đã xảy ra lỗi khi lưu hóa đơn PDF.\nHóa đơn ID: " + maHoaDon,
                        "Đặt Vé Thành Công - Lỗi Lưu PDF",
                        JOptionPane.WARNING_MESSAGE);
            }
            handleSuatChieuSelectionChange();
            clearSelectionAndPrice();
        }
    }

    // Ghi hóa đơn ra file PDF
    private boolean writeInvoiceToPdf(File file, HoaDon hoaDon, List<Ve> veList) {
        Document document = new Document(PageSize.A5); // Sử dụng kích thước A5 cho hóa đơn nhỏ
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE); // Thêm dòng trống

            // Bảng thông tin (cấu trúc hơn so với đoạn văn bản)
            PdfPTable infoTable = new PdfPTable(2); // 2 cột
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 3}); // Độ rộng cột (Nhãn, Giá trị)
            infoTable.getDefaultCell().setBorder(Rectangle.OUT_BOTTOM); // Không có viền cho ô

            addInfoRow(infoTable, "Mã Hóa Đơn:", hoaDon.getMaHoaDon());
            addInfoRow(infoTable, "Ngày Lập:", dateTimeFormatter.format(new Date()));
            addInfoRow(infoTable, "Nhân viên:", (hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNhanVien() : "N/A"));
            addInfoRow(infoTable, "Khách hàng:", (hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKhachHang() : "Khách lẻ"));
            if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getSDT() != null) {
                addInfoRow(infoTable, "SĐT Khách:", hoaDon.getKhachHang().getSDT());
            }
            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // Tiêu đề chi tiết vé
            Paragraph detailsHeader = new Paragraph("----- CHI TIẾT VÉ -----", fontHeader);
            detailsHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(detailsHeader);
            document.add(Chunk.NEWLINE);

            if (!veList.isEmpty() && veList.get(0).getSuatChieu() != null) {
                SuatChieu sc = veList.get(0).getSuatChieu();
                PdfPTable ticketInfoTable = new PdfPTable(2);
                ticketInfoTable.setWidthPercentage(100);
                ticketInfoTable.setWidths(new float[]{1, 3});
                ticketInfoTable.getDefaultCell().setBorder(Rectangle.OUT_BOTTOM);

                addInfoRow(ticketInfoTable, "Phim:", (sc.getPhim() != null ? sc.getPhim().getTenPhim() : "N/A"));
                addInfoRow(ticketInfoTable, "Phòng:", (sc.getPhongChieu() != null ? sc.getPhongChieu().getTenPhong() : "N/A"));
                addInfoRow(ticketInfoTable, "Suất chiếu:", formatDateTime(sc.getThoiGianBD()));

                List<String> seatCodes = new ArrayList<>();
                for(Ve ve : veList) {
                    if(ve.getGheNgoi() != null) {
                        seatCodes.add(ve.getGheNgoi().getHang() + ve.getGheNgoi().getSoGhe());
                    }
                }
                addInfoRow(ticketInfoTable, "Ghế đã đặt:", String.join(", ", seatCodes));
                addInfoRow(ticketInfoTable, "Số lượng vé:", String.valueOf(veList.size()));
                addInfoRow(ticketInfoTable, "Đơn giá:", currencyFormatter.format(veList.get(0).getGiaVe()));
                document.add(ticketInfoTable);

            } else {
                document.add(new Paragraph("(Không có thông tin chi tiết vé)", fontNormal));
            }

            document.add(Chunk.NEWLINE);

            // Tổng tiền
            Paragraph total = new Paragraph("TỔNG TIỀN:   " + currencyFormatter.format(hoaDon.getTongTien()), fontHeader);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            document.add(Chunk.NEWLINE);

            // Chân trang
            Paragraph footer = new Paragraph("Cảm ơn quý khách! Hẹn gặp lại!", fontNormal);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            writer.close();
            return true;

        } catch (DocumentException | IOException e) {
            showError("Lỗi khi tạo file PDF hóa đơn: " + e.getMessage(), e);
            return false;
        }
    }

    // Helper để thêm hàng vào bảng thông tin trong PDF
    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, fontBold));
        labelCell.setBorder(Rectangle.OUT_BOTTOM);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", fontNormal));
        valueCell.setBorder(Rectangle.OUT_BOTTOM);
        table.addCell(valueCell);
    }

    // Thiết lập trạng thái ban đầu
    private void setInitialState() {
        selectedKhachHang = null;
        selectedSuatChieu = null;
        selectedSeatsList.clear();
        txtKhachHang.setText("");
        txtTongTien.setText(currencyFormatter.format(0));
        renderSeatGrid(new ArrayList<>(), new HashSet<>());
        updateButtonStates();
    }

    // Cập nhật trạng thái nút
    private void updateButtonStates() {
        boolean showtimeSelected = (selectedSuatChieu != null);
        boolean seatsSelected = !selectedSeatsList.isEmpty();
        btnDatVe.setEnabled(showtimeSelected && seatsSelected);
        btnHuyChon.setEnabled(showtimeSelected && seatsSelected);
        btnThoat.setEnabled(true);
    }

    // Định dạng thời gian từ chuỗi
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

    // Hiển thị thông báo lỗi khi dữ liệu không hợp lệ
    private void showValidationError(String message, Component componentToFocus) {
        JOptionPane.showMessageDialog(this, message, "Dữ liệu không hợp lệ", JOptionPane.WARNING_MESSAGE);
        if (componentToFocus != null) {
            SwingUtilities.invokeLater(() -> {
                componentToFocus.requestFocusInWindow();
            });
        }
    }

    // Hiển thị thông báo lỗi hệ thống
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


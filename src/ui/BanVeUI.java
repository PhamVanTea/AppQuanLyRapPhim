package ui;

import dao.*;
import entity.*;
import session.StaticVariable; // Assuming this holds logged-in user

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter; // Import for File Chooser
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File; // Import File
import java.io.FileOutputStream; // Import for PDF output stream
import java.io.IOException; // Import for file IO exception
import java.net.URL;
import java.sql.Connection; // For Transaction
import java.sql.SQLException; // For Transaction
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

import com.lowagie.text.Chunk;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont; 
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import connect.DbConnect;

public class BanVeUI extends JPanel {
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
    private static String FONT_PATH = "/fonts/DejaVuSans.ttf"; 
    private JTextField txtKhachHang;

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
             System.err.println("Không thể load font Tiếng Việt cho PDF tại: " + FONT_PATH);
             System.err.println("PDF sẽ không hiển thị đúng tiếng Việt.");
             e.printStackTrace();
           
             fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD);
             fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD);
             fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.PLAIN);
             fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD);
        }
    }

    public BanVeUI() {
        seatButtonMap = new HashMap<>();
        selectedSeatsList = new ArrayList<>();

        setLayout(null);
        setPreferredSize(new Dimension(1000, 680));

		JPanel panelDanhSach_1 = new JPanel();
		panelDanhSach_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Chọn Suất Chiếu", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
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

		JPanel panelChucNang = new JPanel();
		panelChucNang.setBorder(new TitledBorder(null, "Chức năng", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelChucNang.setBounds(38, 592, 927, 64);
		add(panelChucNang);
		panelChucNang.setLayout(new GridLayout(0, 3, 15, 0));

		btnDatVe = new JButton("Xác Nhận & Xuất PDF");
        btnDatVe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDatVe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDatVe.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-print-20.png"))); // Use PDF icon
		btnDatVe.addActionListener(e -> processTicketSaleAndPrint());
        btnDatVe.setEnabled(false);
		panelChucNang.add(btnDatVe);

        btnHuyChon = new JButton("Hủy Chọn Ghế");
        btnHuyChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuyChon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHuyChon.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-cancel-20.png")));
        btnHuyChon.addActionListener(e -> clearSelectionAndPrice());
        btnHuyChon.setEnabled(false);
		panelChucNang.add(btnHuyChon);

		btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnThoat.setIcon(new ImageIcon(BanVeUI.class.getResource("/icons/icons8-exit-20.png")));
		btnThoat.addActionListener(e -> {
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
        });
		panelChucNang.add(btnThoat);

  
        loadSuatChieuTable();
		setInitialState();
		setupAutoCompleteForTextField();
	}
    
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

    
    private void setupAutoCompleteForTextField() {
        List<KhachHang> khList = KhachHangDAO.readAll();

        txtKhachHang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = txtKhachHang.getText().trim().toLowerCase();

                    List<KhachHang> matched = khList.stream()
                            .filter(kh -> kh.getTenKhachHang().toLowerCase().contains(input) || kh.getSDT().toLowerCase().contains(input))
                            .collect(Collectors.toList());

                    if (matched.size() == 1) {
                        // Chọn khách hàng đầu tiên khớp
                        KhachHang selected = matched.get(0);
                        txtKhachHang.setText(selected.getTenKhachHang() + " - " + selected.getSDT());

                        // TODO: Có thể gán selected vào 1 biến hoặc dùng cho logic tiếp theo
                    } else if (matched.size() > 1) {
                        JPopupMenu suggestionMenu = new JPopupMenu();

                        for (KhachHang kh : matched) {
                            JMenuItem item = new JMenuItem(kh.getTenKhachHang() + " - " + kh.getSDT());
                            item.addActionListener(ev -> {
                                txtKhachHang.setText(kh.getTenKhachHang() + " - " + kh.getSDT());

                                // TODO: Gán kh cho logic xử lý tiếp
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
                          seatButton.addActionListener(this::handleSeatSelection);
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

    private void handleSeatSelection(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
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

    private void updateTotalPrice() {
        if (selectedSuatChieu != null && !selectedSeatsList.isEmpty()) {
            float giaVe = selectedSuatChieu.getGia();
            float total = giaVe * selectedSeatsList.size();
            txtTongTien.setText(currencyFormatter.format(total));
        } else {
            txtTongTien.setText(currencyFormatter.format(0));
        }
    }

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

    private void processTicketSaleAndPrint() {
    	selectedKhachHang = getKhachHangFromText();
        if (selectedSuatChieu == null) { showValidationError("Vui lòng chọn một suất chiếu.", tableSuatChieu); return; }
        if (selectedKhachHang == null) { showValidationError("Vui lòng chọn khách hàng.", txtKhachHang); return; }
        if (selectedSeatsList.isEmpty()) { showValidationError("Vui lòng chọn ít nhất một ghế.", panelSeatGrid); return; }
        if (StaticVariable.nhanVien == null) { showError("Lỗi: Không xác định được nhân viên đang đăng nhập.", null); return; }

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
        } finally {
        }


        if (transactionSuccess) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu hóa đơn PDF");
            String suggestedFileName = "HoaDon_" + maHoaDon + "_" + fileDateTimeFormatter.format(new Date()) + ".pdf";
            fileChooser.setSelectedFile(new File(suggestedFileName));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Documents (*.pdf)", "pdf");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".pdf");
                }

                if(writeInvoiceToPdf(fileToSave, hoaDon, veList)) {
                     JOptionPane.showMessageDialog(this,
                        "Đặt vé thành công! Hóa đơn PDF đã được lưu vào:\n" + fileToSave.getAbsolutePath(),
                        "Đặt Vé Thành Công & Đã Lưu PDF",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(this,
                        "Đặt vé thành công nhưng đã xảy ra lỗi khi lưu hóa đơn PDF.\nHóa đơn ID: " + maHoaDon,
                        "Đặt Vé Thành Công - Lỗi Lưu PDF",
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                 JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công cho hóa đơn " + maHoaDon + "! (File PDF chưa được lưu)",
                    "Đặt Vé Thành Công",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            handleSuatChieuSelectionChange();
            clearSelectionAndPrice();
        }
    }

    private boolean writeInvoiceToPdf(File file, HoaDon hoaDon, List<Ve> veList) {
        Document document = new Document(PageSize.A5); // Use A5 for a smaller receipt size
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Title
            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE); // Add empty line

            // Info Table (more structured than paragraphs)
            PdfPTable infoTable = new PdfPTable(2); // 2 columns
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 3}); // Column widths (Label, Value)
            infoTable.getDefaultCell().setBorder(Rectangle.OUT_BOTTOM); // No borders for cells

            addInfoRow(infoTable, "Mã Hóa Đơn:", hoaDon.getMaHoaDon());
            addInfoRow(infoTable, "Ngày Lập:", dateTimeFormatter.format(new Date()));
            addInfoRow(infoTable, "Nhân viên:", (hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNhanVien() : "N/A"));
            addInfoRow(infoTable, "Khách hàng:", (hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getTenKhachHang() : "Khách lẻ"));
            if (hoaDon.getKhachHang() != null && hoaDon.getKhachHang().getSDT() != null) {
                addInfoRow(infoTable, "SĐT Khách:", hoaDon.getKhachHang().getSDT());
            }
            document.add(infoTable);
            document.add(Chunk.NEWLINE);

            // Ticket Details Header
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

            // Total Amount
            Paragraph total = new Paragraph("TỔNG TIỀN:   " + currencyFormatter.format(hoaDon.getTongTien()), fontHeader);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);
            document.add(Chunk.NEWLINE);

            // Footer
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

    // Helper to add rows to info tables in PDF
    private void addInfoRow(PdfPTable table, String label, String value) {
         PdfPCell labelCell = new PdfPCell(new Phrase(label, fontBold));
         labelCell.setBorder(Rectangle.OUT_BOTTOM);
         table.addCell(labelCell);

         PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", fontNormal));
         valueCell.setBorder(Rectangle.OUT_BOTTOM);
         table.addCell(valueCell);
    }


    private void setInitialState() {
    	selectedKhachHang = null;
        selectedSuatChieu = null;
        selectedSeatsList.clear();
        txtKhachHang.setText("");
        txtTongTien.setText(currencyFormatter.format(0));
        renderSeatGrid(new ArrayList<>(), new HashSet<>());
        updateButtonStates();
    }

	private void updateButtonStates() {
		boolean showtimeSelected = (selectedSuatChieu != null);
        boolean seatsSelected = !selectedSeatsList.isEmpty();
        btnDatVe.setEnabled(showtimeSelected && seatsSelected);
        btnHuyChon.setEnabled(showtimeSelected && seatsSelected);
        btnThoat.setEnabled(true);
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
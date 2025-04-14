package ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Cursor; // Import Cursor
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList; // Import ArrayList
import java.util.List; // Import List

import dao.NhanVienDAO;
import entity.NhanVien;
import session.StaticVariable; // Assuming StaticVariable holds logged-in user info

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelContainer; // Panel to display different UIs
	private JPanel panelMenuButtons; // Panel holding the menu buttons

	// Declare UI panels as members
	private TheLoaiUI theLoaiUI;
	private PhimUI phimUI;
	private PhongChieuUI phongChieuUI;
	private GheNgoiUI gheNgoiUI;
	private SuatChieuUI suatChieuUI;
	private NhanVienUI nhanVienUI;
	private KhachHangUI khachHangUI;
	private BanVeUI banVeUI;
	private VeUI veUI;
	private ThongKeUI thongKeUI;

    // Keep track of the currently selected button
    private JButton selectedButton = null;
    private final Color defaultButtonColor = new Color(51, 51, 51);
    private final Color selectedButtonColor = new Color(255, 153, 0); // Orange

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                     // Simulate login for testing - REMOVE this in production
                     // StaticVariable.nhanVien = new NhanVien("NV01", "Admin Test", "Quản lý", "admin", "hashed_pw");
                     if (StaticVariable.nhanVien == null) {
                         // If not logged in (e.g., running MainUI directly), show login first
                         DangNhapUI login = new DangNhapUI();
                         login.setLocationRelativeTo(null);
                         login.setVisible(true);
                     } else {
                        // If already logged in (from DangNhapUI), show main frame
                        MainUI frame = new MainUI();
                        frame.setLocationRelativeTo(null); // Center frame
                        frame.setVisible(true);
                     }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUI() {
        if (StaticVariable.nhanVien == null) {
            // Prevent opening if not logged in - show error or redirect to login
            JOptionPane.showMessageDialog(null, "Lỗi: Không có thông tin nhân viên đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            // Optionally open login screen again here
            // DangNhapUI login = new DangNhapUI(); login.setVisible(true);
            dispose(); // Close this empty frame
            return; // Stop constructor execution
        }

        setTitle("Quản Lý Rạp Chiếu Phim"); // Set a proper title
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelSidebar = new JPanel(); // Renamed for clarity
		panelSidebar.setBackground(new Color(51, 51, 51));
		panelSidebar.setPreferredSize(new Dimension(220, 10)); // Slightly wider sidebar
		contentPane.add(panelSidebar, BorderLayout.WEST);
		panelSidebar.setLayout(new BorderLayout(0, 0));

		JPanel panelUserInfo = new JPanel(); // Renamed for clarity
		panelUserInfo.setPreferredSize(new Dimension(10, 100));
		panelUserInfo.setBackground(new Color(51, 51, 51));
		panelSidebar.add(panelUserInfo, BorderLayout.NORTH);
        panelUserInfo.setLayout(null); // Use absolute for simple labels

        // Add an icon/placeholder for user avatar (optional)
        JLabel lblAvatar = new JLabel("");
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setIcon(new ImageIcon(MainUI.class.getResource("/icons/icons8-avatar-64.png"))); // Example avatar icon
        lblAvatar.setBounds(10, 5, 200, 64);
        panelUserInfo.add(lblAvatar);

		JPanel panelUserInfoText = new JPanel(); // Panel for text below avatar
		panelUserInfoText.setBounds(10, 70, 200, 30); // Position below avatar
		panelUserInfoText.setBackground(new Color(51, 51, 51));
		panelUserInfo.add(panelUserInfoText);
		panelUserInfoText.setLayout(new GridLayout(2, 1, 0, 2)); // 2 rows for name and role

		JLabel lblTenNhanVien = new JLabel(StaticVariable.nhanVien.getTenNhanVien());
		lblTenNhanVien.setHorizontalAlignment(SwingConstants.CENTER);
		lblTenNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Adjusted font
		lblTenNhanVien.setForeground(Color.WHITE);
		panelUserInfoText.add(lblTenNhanVien);

		JLabel lblChucVu = new JLabel(StaticVariable.nhanVien.getChucVu());
		lblChucVu.setHorizontalAlignment(SwingConstants.CENTER);
		lblChucVu.setFont(new Font("Segoe UI", Font.ITALIC, 12)); // Adjusted font
		lblChucVu.setForeground(new Color(200, 200, 200)); // Lighter gray for role
		panelUserInfoText.add(lblChucVu);
		
		

		panelMenuButtons = new JPanel(); // Assign to class member
		panelMenuButtons.setBackground(new Color(51, 51, 51));
		panelSidebar.add(panelMenuButtons, BorderLayout.CENTER);
        // Use BoxLayout for vertical arrangement and better spacing control
        panelMenuButtons.setLayout(new BoxLayout(panelMenuButtons, BoxLayout.Y_AXIS));
        panelMenuButtons.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add some vertical padding


		// --- Create Menu Buttons (without helper) ---
        // Create a list to hold buttons for easier management if needed later
        List<JButton> menuButtons = new ArrayList<>();

		JButton btnBanVe = createMenuButton("Bán Vé", "/icons/icons8-movie-ticket-25.png", e -> hienThiBanVe());
        menuButtons.add(btnBanVe);

		JButton btnSuatChieu = new JButton("Quản lý Suất chiếu");
        configureMenuButton(btnSuatChieu, "/icons/icons8-time-25.png", e -> hienThiSuatChieu());
        menuButtons.add(btnSuatChieu);

		JButton btnPhim = new JButton("Quản lý Phim");
        configureMenuButton(btnPhim, "/icons/icons8-movie-25.png", e -> hienThiPhim());
        menuButtons.add(btnPhim);

		JButton btnTheLoai = new JButton("Quản lý Thể loại");
        configureMenuButton(btnTheLoai, "/icons/icons8-category-25.png", e -> hienThiTheLoai());
		menuButtons.add(btnTheLoai);

		JButton btnPhongChieu = new JButton("Quản lý Phòng chiếu");
        configureMenuButton(btnPhongChieu, "/icons/icons8-room-25.png", e -> hienThiPhongChieu());
		menuButtons.add(btnPhongChieu);

		JButton btnGhe = new JButton("Quản lý Ghế");
        configureMenuButton(btnGhe, "/icons/icons8-chair-25.png", e -> hienThiGheNgoi());
		menuButtons.add(btnGhe);

		JButton btnKhachHang = new JButton("Quản lý Khách hàng");
        configureMenuButton(btnKhachHang, "/icons/icons8-customer-25.png", e -> hienThiKhachHang());
		menuButtons.add(btnKhachHang);

		JButton btnNhanVien = new JButton("Quản lý Nhân viên");
        configureMenuButton(btnNhanVien, "/icons/icons8-account-25.png", e -> hienThiNhanVien());
		menuButtons.add(btnNhanVien);

		JButton btnVe = new JButton("Lịch sử Vé");
        configureMenuButton(btnVe, "/icons/icons8-history-25.png", e -> hienThiVe());
        menuButtons.add(btnVe);

		JButton btnThongKe = new JButton("Thống kê");
        configureMenuButton(btnThongKe, "/icons/icons8-statistics-25.png", e -> hienThiThongKe());
		menuButtons.add(btnThongKe);

        // Add buttons to the panel
        for (JButton btn : menuButtons) {
            panelMenuButtons.add(btn);
            panelMenuButtons.add(Box.createRigidArea(new Dimension(0, 5))); // Add small gap between buttons
        }


		panelContainer = new JPanel();
		panelContainer.setBackground(Color.WHITE); // Set background for the content area
		contentPane.add(panelContainer, BorderLayout.CENTER);
		panelContainer.setLayout(new BorderLayout(0, 0));

        // Optionally display a default panel initially
        // hienThiBanVe(); // Show BanVe UI by default
        // Or show a welcome panel
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Rạp Chiếu Phim!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);
        panelContainer.add(welcomePanel, BorderLayout.CENTER);

	}

    // Helper method to configure common button properties
    private void configureMenuButton(JButton button, String iconPath, ActionListener action) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Use Segoe UI
		button.setForeground(Color.WHITE);
		button.setBackground(defaultButtonColor);
        button.setOpaque(true); // Needed for background color
		button.setBorderPainted(false);
		button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(15); // Gap between icon and text
        button.setFocusPainted(false); // Remove focus border
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Control height with BoxLayout
        button.setPreferredSize(new Dimension(200, 40)); // Set preferred height
        button.setMinimumSize(new Dimension(180, 40)); // Set minimum height

		try { // Add try-catch for icon loading
        	button.setIcon(new ImageIcon(MainUI.class.getResource(iconPath)));
		} catch (Exception e) {
			System.err.println("Error loading icon: " + iconPath);
		}

        // Add the common action listener for highlighting and specific action
        button.addActionListener(e -> {
            handleMenuButtonClick(button); // Handle highlighting
            action.actionPerformed(e); // Perform the specific action
        });
    }

     // Helper method to create and configure buttons (alternative)
     private JButton createMenuButton(String text, String iconPath, ActionListener action) {
         JButton button = new JButton(text);
         configureMenuButton(button, iconPath, action);
         return button;
     }


    // Method to handle button highlighting
    private void handleMenuButtonClick(JButton clickedButton) {
        // Reset previous button's color
        if (selectedButton != null) {
            selectedButton.setBackground(defaultButtonColor);
            selectedButton.setForeground(Color.WHITE); // Reset text color if needed
            selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Reset font style
        }
        // Set new button's color
        clickedButton.setBackground(selectedButtonColor); // Orange highlight
        clickedButton.setForeground(Color.BLACK); // Change text color on highlight
        clickedButton.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Slightly larger font on highlight

        selectedButton = clickedButton; // Update reference
    }


    // --- Methods to display different UI panels ---
	private void displayPanel(JPanel panel) {
		panelContainer.removeAll();
		panelContainer.add(panel, BorderLayout.CENTER);
		panelContainer.revalidate();
		panelContainer.repaint();
	}

	private void hienThiTheLoai() {
		if (theLoaiUI == null) {
			theLoaiUI = new TheLoaiUI();
		}
		displayPanel(theLoaiUI);
	}
	private void hienThiPhim() {
		if (phimUI == null) {
			phimUI = new PhimUI();
		}
		displayPanel(phimUI);
	}
	private void hienThiPhongChieu() {
		if (phongChieuUI == null) {
			phongChieuUI = new PhongChieuUI();
		}
		displayPanel(phongChieuUI);
	}
	private void hienThiGheNgoi() {
		if (gheNgoiUI == null) {
			gheNgoiUI = new GheNgoiUI();
		}
		displayPanel(gheNgoiUI);
	}
	private void hienThiSuatChieu() {
		if (suatChieuUI == null) {
			suatChieuUI = new SuatChieuUI();
		}
		
		displayPanel(suatChieuUI);
	}
	private void hienThiNhanVien() {
		if (nhanVienUI == null) {
			nhanVienUI = new NhanVienUI();
		}
		displayPanel(nhanVienUI);
	}
	private void hienThiKhachHang() {
		if (khachHangUI == null) {
			khachHangUI = new KhachHangUI();
		}
		displayPanel(khachHangUI);
	}
	private void hienThiBanVe() {
		if (banVeUI == null) {
			banVeUI = new BanVeUI();
		}
		banVeUI.loadSuatChieuTable();
		displayPanel(banVeUI);
	}
	private void hienThiVe() {
		if (veUI == null) {
			veUI = new VeUI();
		}
		displayPanel(veUI);
	}
	private void hienThiThongKe() {
		if (thongKeUI == null) {
			thongKeUI = new ThongKeUI();
		}
		displayPanel(thongKeUI);
	}
}
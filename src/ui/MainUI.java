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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import dao.NhanVienDAO;
import dao.SuatChieuDAO;
import entity.NhanVien;
import session.StaticVariable;

public class MainUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelContainer;
	private JPanel panelMenuButtons;

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

	private JButton selectedButton = null;
	private final Color defaultButtonColor = new Color(51, 51, 51);
	private final Color selectedButtonColor = new Color(255, 153, 0);
    private final Color logoutButtonColor = new Color(204, 0, 0);
    private final Color logoutButtonClickedColor = new Color(180, 0, 0);


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (StaticVariable.nhanVien == null) {
						DangNhapUI login = new DangNhapUI();
						login.setLocationRelativeTo(null);
						login.setVisible(true);
					} else {
						MainUI frame = new MainUI();
						frame.setLocationRelativeTo(null);
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
			JOptionPane.showMessageDialog(null, "Lỗi: Không có thông tin nhân viên đăng nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			dispose();
            EventQueue.invokeLater(() -> {
                DangNhapUI login = new DangNhapUI();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
			return;
		}

		setTitle("Quản Lý Rạp Chiếu Phim");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 720);
        setMinimumSize(new Dimension(1000, 600));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelSidebar = new JPanel();
		panelSidebar.setBackground(new Color(51, 51, 51));
		panelSidebar.setPreferredSize(new Dimension(220, 10));
		contentPane.add(panelSidebar, BorderLayout.WEST);
		panelSidebar.setLayout(new BorderLayout(0, 0));

		JPanel panelUserInfo = new JPanel();
		panelUserInfo.setPreferredSize(new Dimension(10, 110));
		panelUserInfo.setBackground(new Color(51, 51, 51));
		panelSidebar.add(panelUserInfo, BorderLayout.NORTH);
		panelUserInfo.setLayout(null);

		JLabel lblAvatar = new JLabel("");
		lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			lblAvatar.setIcon(new ImageIcon(MainUI.class.getResource("/icons/icons8-avatar-64.png")));
		} catch (NullPointerException e) {
			System.err.println("Avatar icon not found. Check path /icons/icons8-avatar-64.png");
			lblAvatar.setText("[Avatar]");
			lblAvatar.setForeground(Color.WHITE);
		}
		lblAvatar.setBounds(10, 5, 200, 64);
		panelUserInfo.add(lblAvatar);

		JPanel panelUserInfoText = new JPanel();
		panelUserInfoText.setBounds(10, 70, 200, 40);
		panelUserInfoText.setBackground(new Color(51, 51, 51));
		panelUserInfo.add(panelUserInfoText);
		panelUserInfoText.setLayout(new GridLayout(2, 1, 0, 2));

		JLabel lblTenNhanVien = new JLabel(StaticVariable.nhanVien.getTenNhanVien());
		lblTenNhanVien.setHorizontalAlignment(SwingConstants.CENTER);
		lblTenNhanVien.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblTenNhanVien.setForeground(Color.WHITE);
		panelUserInfoText.add(lblTenNhanVien);

		JLabel lblChucVu = new JLabel(StaticVariable.nhanVien.getChucVu());
		lblChucVu.setHorizontalAlignment(SwingConstants.CENTER);
		lblChucVu.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		lblChucVu.setForeground(new Color(200, 200, 200));
		panelUserInfoText.add(lblChucVu);

		panelMenuButtons = new JPanel();
		panelMenuButtons.setBackground(new Color(51, 51, 51));
		panelSidebar.add(panelMenuButtons, BorderLayout.CENTER);
		panelMenuButtons.setLayout(new BoxLayout(panelMenuButtons, BoxLayout.Y_AXIS));
		panelMenuButtons.setBorder(new EmptyBorder(10, 5, 10, 5));

		List<JButton> menuButtons = new ArrayList<>();

		JButton btnBanVe = createMenuButton("Bán Vé", "/icons/icons8-movie-ticket-25.png", e -> hienThiBanVe());
		menuButtons.add(btnBanVe);

		JButton btnSuatChieu = createMenuButton("Quản lý Suất chiếu", "/icons/icons8-time-25.png", e -> hienThiSuatChieu());
		menuButtons.add(btnSuatChieu);

		JButton btnPhim = createMenuButton("Quản lý Phim", "/icons/icons8-movie-25.png", e -> hienThiPhim());
		menuButtons.add(btnPhim);

		JButton btnTheLoai = createMenuButton("Quản lý Thể loại", "/icons/icons8-category-25.png", e -> hienThiTheLoai());
		menuButtons.add(btnTheLoai);

		JButton btnPhongChieu = createMenuButton("Quản lý Phòng chiếu", "/icons/icons8-room-25.png", e -> hienThiPhongChieu());
		menuButtons.add(btnPhongChieu);

		JButton btnGhe = createMenuButton("Quản lý Ghế", "/icons/icons8-chair-25.png", e -> hienThiGheNgoi());
		menuButtons.add(btnGhe);

		JButton btnKhachHang = createMenuButton("Quản lý Khách hàng", "/icons/icons8-customer-25.png", e -> hienThiKhachHang());
		menuButtons.add(btnKhachHang);

		JButton btnNhanVien = createMenuButton("Quản lý Nhân viên", "/icons/icons8-account-25.png", e -> hienThiNhanVien());
		menuButtons.add(btnNhanVien);

		JButton btnVe = createMenuButton("Lịch sử Vé", "/icons/icons8-history-25.png", e -> hienThiVe());
		menuButtons.add(btnVe);

		JButton btnThongKe = createMenuButton("Thống kê", "/icons/icons8-statistics-25.png", e -> hienThiThongKe());
		menuButtons.add(btnThongKe);

		for (JButton btn : menuButtons) {
			panelMenuButtons.add(btn);
			panelMenuButtons.add(Box.createRigidArea(new Dimension(0, 5)));
		}

        panelMenuButtons.add(Box.createVerticalGlue());

        JButton btnDangXuat = new JButton("Đăng xuất");
        configureMenuButton(btnDangXuat, "/icons/icons8-logout-25.png", null); // Pass null action initially
        btnDangXuat.setBackground(logoutButtonColor);
        // Add specific action listener for logout that doesn't rely on the general one
        btnDangXuat.addActionListener(e -> {
             handleMenuButtonClick(btnDangXuat); // Still handle visual selection
             dangXuat();
        });
        panelMenuButtons.add(btnDangXuat);
        panelMenuButtons.add(Box.createRigidArea(new Dimension(0, 5)));


		panelContainer = new JPanel();
		panelContainer.setBackground(Color.WHITE);
		contentPane.add(panelContainer, BorderLayout.CENTER);
		panelContainer.setLayout(new BorderLayout(0, 0));

		hienThiBanVe();
		handleMenuButtonClick(btnBanVe);

	}

	private void configureMenuButton(JButton button, String iconPath, ActionListener action) {
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(defaultButtonColor);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setIconTextGap(15);
		button.setFocusPainted(false);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		button.setPreferredSize(new Dimension(210, 40)); // Fixed preferred width based on sidebar
		button.setMinimumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

		try {
            if (!iconPath.startsWith("/")) {
                iconPath = "/" + iconPath;
            }
			button.setIcon(new ImageIcon(MainUI.class.getResource(iconPath)));
		} catch (Exception e) {
			System.err.println("Error loading icon: " + iconPath + " for button " + button.getText());
            button.setText(" " + button.getText());
		}

        // Add the main action listener only if an action is provided
		if (action != null) {
            button.addActionListener(e -> {
                handleMenuButtonClick(button);
                action.actionPerformed(e);
            });
        }
	}

	private JButton createMenuButton(String text, String iconPath, ActionListener action) {
		JButton button = new JButton(text);
		configureMenuButton(button, iconPath, action);
		return button;
	}

	private void handleMenuButtonClick(JButton clickedButton) {
        boolean isLogoutClicked = clickedButton.getText().equals("Đăng xuất");

        // Reset the previously selected button
		if (selectedButton != null && selectedButton != clickedButton) {
             boolean isPreviousLogout = selectedButton.getText().equals("Đăng xuất");
             if (isPreviousLogout) {
                selectedButton.setBackground(logoutButtonColor); // Reset logout to its normal color
             } else {
                selectedButton.setBackground(defaultButtonColor); // Reset normal button
             }
             selectedButton.setForeground(Color.WHITE);
             selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		}

        // Highlight the newly clicked button
        if (isLogoutClicked) {
            clickedButton.setBackground(logoutButtonClickedColor); // Special color for clicked logout
            clickedButton.setForeground(Color.WHITE);
            clickedButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        } else {
            clickedButton.setBackground(selectedButtonColor); // Normal selection color
            clickedButton.setForeground(Color.BLACK);
            clickedButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        }

		selectedButton = clickedButton;
	}

	private void displayPanel(JPanel panel) {
		panelContainer.removeAll();
		panelContainer.add(panel, BorderLayout.CENTER);
		panelContainer.revalidate();
		panelContainer.repaint();
	}

    private void dangXuat() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            StaticVariable.nhanVien = null;
            this.dispose();

            EventQueue.invokeLater(() -> {
                DangNhapUI login = new DangNhapUI();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            });
        } else {
             if (selectedButton != null && selectedButton.getText().equals("Đăng xuất")) {
                 selectedButton.setBackground(logoutButtonColor);
                 selectedButton.setForeground(Color.WHITE);
                 selectedButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
             }
        }
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
		suatChieuUI.loadPhongChieuComboBox();
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
		banVeUI.setupAutoCompleteForTextField();
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
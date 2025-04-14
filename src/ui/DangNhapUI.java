package ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;
import javax.swing.ImageIcon; // Import ImageIcon
import java.awt.Color; // Import Color
import java.awt.Cursor; // Import Cursor
import java.awt.Dimension;

import dao.NhanVienDAO;
import entity.NhanVien;
import session.StaticVariable;

public class DangNhapUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtTenDangNhap;
	private JPasswordField txtMatKhau;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					DangNhapUI frame = new DangNhapUI();
                    frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DangNhapUI() {
		setTitle("Đăng Nhập Hệ Thống Rạp Chiếu Phim");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300); // Slightly larger bounds
		setResizable(false); // Prevent resizing
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 248, 255)); // AliceBlue background
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // Add padding
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setForeground(new Color(0, 102, 204)); // Darker blue title
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Larger, modern font
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(10, 20, 416, 40); // Adjusted bounds
		contentPane.add(lblTitle);

		// --- Username Panel ---
		JPanel panelUser = new JPanel();
		panelUser.setBackground(new Color(240, 248, 255)); // Match content pane background
		panelUser.setBounds(45, 80, 346, 30); // Centered position
		contentPane.add(panelUser);
		panelUser.setLayout(null);

		JLabel lblUsernameIcon = new JLabel("");
		lblUsernameIcon.setIcon(new ImageIcon(DangNhapUI.class.getResource("/icons/icons8-user-20.png"))); // User icon
		lblUsernameIcon.setBounds(0, 5, 20, 20); // Position icon
		panelUser.add(lblUsernameIcon);

		JLabel lblUsername = new JLabel("Tên đăng nhập:");
		lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblUsername.setBounds(30, 5, 110, 20); // Position label next to icon
		panelUser.add(lblUsername);

		txtTenDangNhap = new JTextField();
		txtTenDangNhap.setText("admin");
		txtTenDangNhap.setHorizontalAlignment(SwingConstants.LEFT);
		txtTenDangNhap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtTenDangNhap.setBounds(150, 5, 196, 25); // Position text field
		panelUser.add(txtTenDangNhap);
		txtTenDangNhap.setColumns(10);

		// --- Password Panel ---
		JPanel panelPassword = new JPanel();
		panelPassword.setBackground(new Color(240, 248, 255)); // Match content pane background
		panelPassword.setLayout(null);
		panelPassword.setBounds(45, 120, 346, 30); // Position below username
		contentPane.add(panelPassword);

		JLabel lblPasswordIcon = new JLabel("");
		lblPasswordIcon.setIcon(new ImageIcon(DangNhapUI.class.getResource("/icons/icons8-password-20.png"))); // Password icon
		lblPasswordIcon.setBounds(0, 5, 20, 20);
		panelPassword.add(lblPasswordIcon);

		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPassword.setBounds(30, 5, 110, 20);
		panelPassword.add(lblPassword);

		txtMatKhau = new JPasswordField();
		txtMatKhau.setText("123456");
		txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtMatKhau.setBounds(150, 5, 196, 25);
		panelPassword.add(txtMatKhau);

		// --- Button Panel ---
		JPanel panelButtons = new JPanel();
		panelButtons.setBackground(new Color(240, 248, 255)); // Match background
		panelButtons.setBounds(10, 175, 416, 45); // Position buttons below password
		contentPane.add(panelButtons);
        FlowLayout fl_panelButtons = new FlowLayout(FlowLayout.CENTER, 20, 5); // Center buttons with gap
		panelButtons.setLayout(fl_panelButtons);

		JButton btnDangNhap = new JButton("Đăng nhập");
		btnDangNhap.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor
		btnDangNhap.setForeground(Color.BLACK);
		btnDangNhap.setBackground(new Color(0, 128, 128)); // Teal background
		btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnDangNhap.setIcon(new ImageIcon(DangNhapUI.class.getResource("/icons/icons8-login-20.png"))); // Login icon
        btnDangNhap.setPreferredSize(new Dimension(150, 35)); // Set preferred size
		btnDangNhap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dangNhap();
			}
		});
		panelButtons.add(btnDangNhap);

		JButton btnThoat = new JButton("Thoát");
		btnThoat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor
		btnThoat.setForeground(Color.BLACK);
		btnThoat.setBackground(new Color(192, 57, 43)); // Red background
		btnThoat.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnThoat.setIcon(new ImageIcon(DangNhapUI.class.getResource("/icons/icons8-exit-20.png"))); // Exit icon
        btnThoat.setPreferredSize(new Dimension(150, 35)); // Set preferred size
        btnThoat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(DangNhapUI.this,
                    "Bạn có chắc chắn muốn thoát?", "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
		panelButtons.add(btnThoat);

        txtMatKhau.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dangNhap();
                }
            }
        });

        txtTenDangNhap.addKeyListener(new KeyAdapter() {
             @Override
            public void keyPressed(KeyEvent e) {
                 if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtMatKhau.requestFocusInWindow();
                 }
            }
        });
	}

	private void dangNhap() {
		String tenDangNhap = txtTenDangNhap.getText().trim();
		String matKhau = new String(txtMatKhau.getPassword()).trim();

		if (tenDangNhap.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập Tên đăng nhập!", "Lỗi", JOptionPane.WARNING_MESSAGE);
			txtTenDangNhap.requestFocus();
			return;
		}
		if (matKhau.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập Mật khẩu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
			txtMatKhau.requestFocus();
			return;
		}

		NhanVien loggedInUser = NhanVienDAO.login(tenDangNhap, matKhau);

		if (loggedInUser != null) {
			JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nChào mừng " + loggedInUser.getTenNhanVien() + "!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            StaticVariable.nhanVien = loggedInUser;
			MainUI mainApp = new MainUI();
            mainApp.setLocationRelativeTo(null);
			mainApp.setVisible(true);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc Mật khẩu không đúng!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.setText("");
            txtTenDangNhap.requestFocus();
            txtTenDangNhap.selectAll();
		}
	}
}
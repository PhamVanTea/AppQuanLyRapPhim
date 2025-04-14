package main;

import java.awt.EventQueue;

import javax.swing.UIManager;

import ui.DangNhapUI;

public class Main {
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
}
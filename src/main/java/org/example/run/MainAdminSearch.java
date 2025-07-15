package org.example.run;

import org.example.view.AdminSearchView;

import javax.swing.*;

public class MainAdminSearch {
    public static void main(String[] args) {
        // Cài giao diện FlatLaf nếu muốn đẹp hơn (tuỳ chọn)
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            System.out.println("Không thể set LookAndFeel: " + e.getMessage());
        }

        // Tạo JFrame chứa panel tìm kiếm
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý tìm phòng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null); // center
            frame.setContentPane(AdminSearchView.createSearchPanel());
            frame.setVisible(true);
        });
    }
}

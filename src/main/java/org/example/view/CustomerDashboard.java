package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomerDashboard {
    public static JPanel createUserHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Tiêu đề chào mừng
        JLabel titleLabel = new JLabel("Chào mừng bạn đến với hệ thống quản lý khách sạn");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Panel trung tâm gồm ảnh và mô tả
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(Color.WHITE);

        // Ảnh nền hoặc logo
        try {
            ImageIcon icon = new ImageIcon("hotel.jpg"); // ảnh cần có trong thư mục project
            Image scaledImage = icon.getImage().getScaledInstance(400, 280, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            centerPanel.add(imageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            JLabel imageLabel = new JLabel("[Ảnh không hiển thị được]");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            centerPanel.add(imageLabel, BorderLayout.WEST);
        }

        // Mô tả dịch vụ
        JTextArea infoText = new JTextArea();
        infoText.setText("\nHệ thống giúp bạn:\n\n- Tra cứu thông tin phòng dễ dàng\n- Đặt phòng nhanh chóng\n- Theo dõi lịch sử và thông báo\n- Hỗ trợ thanh toán an toàn\n\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi!");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoText.setEditable(false);
        infoText.setWrapStyleWord(true);
        infoText.setLineWrap(true);
        infoText.setOpaque(false);
        infoText.setMargin(new Insets(10, 10, 10, 10));

        centerPanel.add(infoText, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }
}

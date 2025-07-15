//package org.example.view;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.kordamp.ikonli.materialdesign.MaterialDesign;
//import org.kordamp.ikonli.swing.FontIcon;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.function.Supplier;
//
//public class Test17 extends JFrame {
//    private Color backgroundColor;
//    private Color hoverColor;
//    private Color pressedColor;
//    private Color selectedTextColor;
//    private Color normalTextColor;
//
//    private JButton selectedButton = null;
//    private JPanel mainContentPanel;
//
//    private static final int LEFT_PADDING = 65;
//
//    public Test17() {
//        setTitle("Quản lý khách sạn");
//        setSize(1200, 800);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        // Màu giao diện
//        backgroundColor = UIManager.getColor("Panel.background");
//        hoverColor = backgroundColor.brighter();
//        pressedColor = new Color(220, 230, 255);
//        selectedTextColor = new Color(0, 120, 215);
//        normalTextColor = Color.DARK_GRAY;
//
//        JPanel rootPanel = new JPanel(new BorderLayout());
//        setContentPane(rootPanel);
//
//        // Menu bên trái
//        JPanel drawerMenu = new JPanel();
//        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
//        drawerMenu.setBackground(backgroundColor);
//        drawerMenu.setPreferredSize(new Dimension(250, 800));
//        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        // Nút Admin (không tương tác)
//        JButton accountBtn = createNavButton("account-circle", "DiuLyh612", true);
//        accountBtn.setEnabled(false);
//        drawerMenu.add(accountBtn);
//
//        drawerMenu.add(Box.createVerticalStrut(20));
//        drawerMenu.add(createNavButton("home", "Trang chủ", false, Test21::createUserHomePanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false, Test18::createSearchPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//        drawerMenu.add(createNavButton("clipboard-check", "Thông báo", false, Test20::createUserNotificationPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//        drawerMenu.add(createNavButton("calendar-check", "Lịch sử", false, Test19::createPaymentHistoryPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//        drawerMenu.add(Box.createVerticalGlue());
//
//        drawerMenu.add(createNavButton("logout", "Đăng xuất", false));
//
//        // Khu vực chính hiển thị nội dung
//        mainContentPanel = new JPanel(new BorderLayout());
//        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        mainContentPanel.setBackground(Color.WHITE);
//
//        rootPanel.add(drawerMenu, BorderLayout.WEST);
//        rootPanel.add(mainContentPanel, BorderLayout.CENTER);
//
//        // Mặc định mở "Trang chủ"
//        mainContentPanel.add(Test21.createUserHomePanel(), BorderLayout.CENTER);
//        mainContentPanel.revalidate();
//        mainContentPanel.repaint();
//
//        // Đánh dấu nút "Trang chủ" là đã chọn
//        for (Component comp : drawerMenu.getComponents()) {
//            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
//                selectButton(btn);
//                break;
//            }
//        }
//    }
//
//    private JButton createNavButton(String iconName, String text, boolean centerAligned) {
//        return createNavButton(iconName, text, centerAligned, null);
//    }
//
//    private JButton createNavButton(String iconName, String text, boolean centerAligned, Supplier<JPanel> panelSupplier) {
//        JButton button = new JButton(text);
//        button.setFocusPainted(false);
//        button.setBackground(backgroundColor);
//        button.setForeground(normalTextColor);
//        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
//        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        button.setOpaque(true);
//        button.setMaximumSize(new Dimension(250, 40));
//
//        MaterialDesign mdiIcon;
//        try {
//            mdiIcon = MaterialDesign.valueOf("MDI_" + iconName.toUpperCase().replace("-", "_"));
//        } catch (IllegalArgumentException e) {
//            mdiIcon = MaterialDesign.MDI_HELP_CIRCLE_OUTLINE;
//        }
//
//        FontIcon icon = FontIcon.of(mdiIcon, 20);
//        icon.setIconColor(normalTextColor);
//        button.setIcon(icon);
//        button.setIconTextGap(10);
//
//        if (centerAligned) {
//            button.setHorizontalAlignment(SwingConstants.CENTER);
//            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
//            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//        } else {
//            button.setHorizontalAlignment(SwingConstants.LEFT);
//            button.setBorder(BorderFactory.createEmptyBorder(10, LEFT_PADDING, 10, 0));
//        }
//
//        if (button.isEnabled()) {
//            button.addMouseListener(new MouseAdapter() {
//                public void mouseEntered(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(hoverColor);
//                }
//
//                public void mouseExited(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(backgroundColor);
//                }
//
//                public void mousePressed(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(pressedColor);
//                }
//
//                public void mouseReleased(MouseEvent e) {
//                    if (button != selectedButton) button.setBackground(hoverColor);
//                }
//            });
//
//            button.addActionListener(e -> {
//                selectButton(button);
//                if (panelSupplier != null) {
//                    mainContentPanel.removeAll();
//                    mainContentPanel.add(panelSupplier.get(), BorderLayout.CENTER);
//                    mainContentPanel.revalidate();
//                    mainContentPanel.repaint();
//                }
//            });
//        }
//
//        return button;
//    }
//
//    private void selectButton(JButton button) {
//        if (selectedButton != null) {
//            selectedButton.setBackground(backgroundColor);
//            selectedButton.setForeground(normalTextColor);
//            ((FontIcon) selectedButton.getIcon()).setIconColor(normalTextColor);
//        }
//        button.setBackground(backgroundColor);
//        button.setForeground(selectedTextColor);
//        ((FontIcon) button.getIcon()).setIconColor(selectedTextColor);
//        selectedButton = button;
//    }
//
//    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        SwingUtilities.invokeLater(() -> new Test17().setVisible(true));
//    }
//}

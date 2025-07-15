
package org.example.view;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

public class CustomerContainerView extends JPanel {
    private MainFrameView mainFrame;
    private String username;

    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color selectedTextColor;
    private Color normalTextColor;

    private JButton selectedButton = null;
    private JPanel mainContentPanel;
    private static final int LEFT_PADDING = 65;

    public CustomerContainerView(MainFrameView mainFrame, String username) {
        this.mainFrame = mainFrame;
        this.username = username;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));

        backgroundColor = UIManager.getColor("Panel.background");
        hoverColor = backgroundColor.brighter();
        pressedColor = new Color(220, 230, 255);
        selectedTextColor = new Color(0, 120, 215);
        normalTextColor = Color.DARK_GRAY;

        JPanel drawerMenu = new JPanel();
        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
        drawerMenu.setBackground(backgroundColor);
        drawerMenu.setPreferredSize(new Dimension(250, 800));
        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Avatar / username
        JButton accountBtn = createNavButton("account-circle", username, true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);

        drawerMenu.add(Box.createVerticalStrut(20));

        // === Menu ===
        drawerMenu.add(createNavButton("home", "Trang chủ", false, CustomerDashboardView::createUserHomePanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false,
                () -> CustomerSearchView.createSearchPanel(mainFrame)));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("clipboard-check", "Thông báo", false,
                NotificationView::createUserNotificationPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("calendar-check", "Lịch sử", false,
                HistoryView::createPaymentHistoryPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        // === Logout ===
        drawerMenu.add(Box.createVerticalGlue());
        drawerMenu.add(createNavButton("logout", "Đăng xuất", false, () -> {
            mainFrame.showLoginPanel();
            return new JPanel();
        }));

        // === Nội dung chính (panel phải) ===
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContentPanel.setBackground(Color.WHITE);

        add(drawerMenu, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        // Mặc định: Trang chủ
        mainContentPanel.add(CustomerDashboardView.createUserHomePanel(), BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();

        // Chọn mặc định "Trang chủ"
        for (Component comp : drawerMenu.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
                selectButton(btn);
                break;
            }
        }
    }

    private JButton createNavButton(String iconName, String text, boolean centerAligned) {
        return createNavButton(iconName, text, centerAligned, null);
    }

    private JButton createNavButton(String iconName, String text, boolean centerAligned, Supplier<JPanel> panelSupplier) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(normalTextColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(250, 40));

        // Icon
        MaterialDesign mdiIcon;
        try {
            mdiIcon = MaterialDesign.valueOf("MDI_" + iconName.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            mdiIcon = MaterialDesign.MDI_HELP_CIRCLE_OUTLINE;
        }

        FontIcon icon = FontIcon.of(mdiIcon, 20);
        icon.setIconColor(normalTextColor);
        button.setIcon(icon);
        button.setIconTextGap(10);

        if (centerAligned) {
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        } else {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorder(BorderFactory.createEmptyBorder(10, LEFT_PADDING, 10, 0));
        }

        // Mouse hover & click
        if (button.isEnabled()) {
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (button != selectedButton) button.setBackground(hoverColor);
                }

                public void mouseExited(MouseEvent e) {
                    if (button != selectedButton) button.setBackground(backgroundColor);
                }

                public void mousePressed(MouseEvent e) {
                    if (button != selectedButton) button.setBackground(pressedColor);
                }

                public void mouseReleased(MouseEvent e) {
                    if (button != selectedButton) button.setBackground(hoverColor);
                }
            });

            button.addActionListener(e -> {
                selectButton(button);
                if (panelSupplier != null) {
                    mainContentPanel.removeAll();
                    mainContentPanel.add(panelSupplier.get(), BorderLayout.CENTER);
                    mainContentPanel.revalidate();
                    mainContentPanel.repaint();
                }
            });
        }

        return button;
    }

    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(backgroundColor);
            selectedButton.setForeground(normalTextColor);
            ((FontIcon) selectedButton.getIcon()).setIconColor(normalTextColor);
        }
        button.setBackground(backgroundColor);
        button.setForeground(selectedTextColor);
        ((FontIcon) button.getIcon()).setIconColor(selectedTextColor);
        selectedButton = button;
    }

    // 📌 Cho phép lớp khác cập nhật nội dung động bên phải
    public void setDynamicContent(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void loadData(String username) {
        this.username = username;
        // nếu cần load thêm dữ liệu người dùng
    }

    public void resetToDefault() {
        setDynamicContent(CustomerDashboardView.createUserHomePanel());
    }

    public void setMainContent(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

}

package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

public class CustomerContainerView extends JPanel {
    private final MainFrameView mainFrame;
    private final String username;

    private final Color backgroundColor;
    private final Color hoverColor;
    private final Color pressedColor;
    private final Color selectedTextColor;
    private final Color normalTextColor;

    private JButton selectedButton = null;
    private JPanel mainContentPanel;
    private JPanel drawerMenu;
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

        drawerMenu = new JPanel();
        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
        drawerMenu.setBackground(backgroundColor);
        drawerMenu.setPreferredSize(new Dimension(250, 800));
        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton accountBtn = createNavButton("\uD83D\uDD75\u200D", username, true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);
        drawerMenu.add(Box.createVerticalStrut(20));

        drawerMenu.add(createNavButton("\uD83C\uDFE0", "Trang chủ", false, () -> CustomerDashboardView.createUserHomePanel()));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("\uD83D\uDD0D", "Tìm kiếm", false,
                () -> CustomerSearchView.createSearchPanel(mainFrame)));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("\uD83D\uDECE\uFE0F", "Thông báo", false,
                () -> {
                    String currentUsername = mainFrame.getLoggedInUsername() != null ? mainFrame.getLoggedInUsername() : username;
                    return NotificationView.createUserNotificationPanel(currentUsername);
                }));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("\uD83D\uDCCB", "Lịch sử", false,
                () -> {
                    String currentUsername = mainFrame.getLoggedInUsername() != null ? mainFrame.getLoggedInUsername() : username;
                    return HistoryView.createPaymentHistoryPanel(currentUsername);
                }));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(Box.createVerticalGlue());

        // Nút Đăng xuất xử lý riêng
        JButton logoutButton = createNavButton("\uD83D\uDD12", "Đăng xuất", false);
        logoutButton.addActionListener(e -> handleLogout());
        drawerMenu.add(logoutButton);

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContentPanel.setBackground(Color.WHITE);

        add(drawerMenu, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        setDynamicContent(CustomerDashboardView.createUserHomePanel());

        setSelectedMenu("Trang chủ");
    }

    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
            mainFrame.showLoginPanel();
            setDynamicContent(new JPanel());
        }
        // Không làm gì nếu chọn "Không"
    }

    private JButton createNavButton(String unicodeIcon, String text, boolean centerAligned) {
        return createNavButton(unicodeIcon, text, centerAligned, null);
    }

    private JButton createNavButton(String unicodeIcon, String text, boolean centerAligned, Supplier<JPanel> panelSupplier) {
        JButton button = new JButton(unicodeIcon + " " + text);
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(normalTextColor);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(250, 40));

        if (centerAligned) {
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        } else {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorder(BorderFactory.createEmptyBorder(10, LEFT_PADDING, 10, 0));
        }

        if (button.isEnabled() && panelSupplier != null) {
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
                setDynamicContent(panelSupplier.get());
            });
        }

        return button;
    }

    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(backgroundColor);
            selectedButton.setForeground(normalTextColor);
        }
        button.setBackground(backgroundColor);
        button.setForeground(selectedTextColor);
        selectedButton = button;
    }

    public void setDynamicContent(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void resetToDefault() {
        setDynamicContent(CustomerDashboardView.createUserHomePanel());
        setSelectedMenu("Trang chủ");
    }

    public void setMainContent(JPanel panel) {
        setDynamicContent(panel);
    }

    public void setSelectedMenu(String menuName) {
        for (Component comp : drawerMenu.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals(menuName)) {
                selectButton(btn);
                break;
            }
        }
    }

    public void loadData(String username) {
        setDynamicContent(CustomerDashboardView.createUserHomePanel());
    }
}
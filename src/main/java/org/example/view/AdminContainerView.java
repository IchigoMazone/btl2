//package org.example.view;
//
//import org.kordamp.ikonli.materialdesign.MaterialDesign;
//import org.kordamp.ikonli.swing.FontIcon;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.function.Supplier;
//
//public class AdminContainerView extends JPanel {
//    private final MainFrameView mainFrame;
//
//    private final Color backgroundColor;
//    private final Color hoverColor;
//    private final Color pressedColor;
//    private final Color selectedTextColor;
//    private final Color normalTextColor;
//
//    private JButton selectedButton = null;
//    private final JPanel mainContentPanel;
//    private final JPanel drawerMenu;
//    private static final int LEFT_PADDING = 65;
//
//    public AdminContainerView(MainFrameView mainFrame) {
//        this.mainFrame = mainFrame;
//
//        setLayout(new BorderLayout());
//        setPreferredSize(new Dimension(1200, 800));
//
//        backgroundColor = UIManager.getColor("Panel.background");
//        hoverColor = backgroundColor.brighter();
//        pressedColor = new Color(220, 230, 255);
//        selectedTextColor = new Color(0, 120, 215);
//        normalTextColor = Color.DARK_GRAY;
//
//        drawerMenu = new JPanel();
//        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
//        drawerMenu.setBackground(backgroundColor);
//        drawerMenu.setPreferredSize(new Dimension(250, 800));
//        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        // Avatar / admin label
//        JButton accountBtn = createNavButton("account-circle", "Admin", true);
//        accountBtn.setEnabled(false);
//        drawerMenu.add(accountBtn);
//        drawerMenu.add(Box.createVerticalStrut(20));
//
//        // === Menu trái ===
//        drawerMenu.add(createNavButton("home", "Trang chủ", false, AdminDashboardView::createDashboardPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false,
//                () -> AdminSearchView.createSearchPanel(mainFrame)));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("calendar-check", "Đặt phòng", false,
//                BookingView::createBookingPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("bed-outline", "Phòng nghỉ", false,
//                RoomView::createRoomSearchPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("account-multiple", "Khách hàng", false,
//                CustomerView::createCustomerSearchPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("clipboard-check", "Yêu cầu", false,
//                RequestView::createNotificationPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("chart-bar", "Thống kê", false,
//                StatisticalView::createDashboardPanel));
//
//        drawerMenu.add(Box.createVerticalGlue());
//
//        drawerMenu.add(createNavButton("logout", "Đăng xuất", false, () -> {
//            mainFrame.showLoginPanel();
//            return new JPanel();
//        }));
//
//        mainContentPanel = new JPanel(new BorderLayout());
//        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        mainContentPanel.setBackground(Color.WHITE);
//
//        add(drawerMenu, BorderLayout.WEST);
//        add(mainContentPanel, BorderLayout.CENTER);
//
//        // Mặc định hiển thị Trang chủ
//        setDynamicContent(AdminDashboardView.createDashboardPanel());
//
//        // Chọn "Trang chủ"
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
//                    setDynamicContent(panelSupplier.get());
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
//    public void setDynamicContent(JPanel panel) {
//        mainContentPanel.removeAll();
//        mainContentPanel.add(panel, BorderLayout.CENTER);
//        mainContentPanel.revalidate();
//        mainContentPanel.repaint();
//    }
//
//    public void resetToDefault() {
//        setDynamicContent(AdminDashboardView.createDashboardPanel());
//
//        for (Component comp : drawerMenu.getComponents()) {
//            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
//                selectButton(btn);
//                break;
//            }
//        }
//    }
//
//    public void setSelectedMenu(String menuName) {
//        for (Component comp : drawerMenu.getComponents()) {
//            if (comp instanceof JButton btn && btn.getText().equals(menuName)) {
//                selectButton(btn);
//                break;
//            }
//        }
//    }
//
//
//    public void setMainContent(JPanel panel) {
//        setDynamicContent(panel);
//    }
//
//    public void loadData() {
//        // Có thể dùng để load thông tin admin nếu sau này mở rộng
//    }
//}


//
//
//package org.example.view;
//
//import org.kordamp.ikonli.materialdesign.MaterialDesign;
//import org.kordamp.ikonli.swing.FontIcon;
//import org.example.controller.CustomerController;
//import org.example.service.BookingService;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.function.Supplier;
//
//public class AdminContainerView extends JPanel {
//    private final MainFrameView mainFrame;
//    private final Color backgroundColor;
//    private final Color hoverColor;
//    private final Color pressedColor;
//    private final Color selectedTextColor;
//    private final Color normalTextColor;
//
//    private JButton selectedButton = null;
//    private final JPanel mainContentPanel;
//    private final JPanel drawerMenu;
//    private static final int LEFT_PADDING = 65;
//    private CustomerController controller = new CustomerController(new BookingService());
//
//
//    public AdminContainerView(MainFrameView mainFrame) {
//        this.mainFrame = mainFrame;
//
//        setLayout(new BorderLayout());
//        setPreferredSize(new Dimension(1200, 800));
//
//        backgroundColor = UIManager.getColor("Panel.background");
//        hoverColor = backgroundColor.brighter();
//        pressedColor = new Color(220, 230, 255);
//        selectedTextColor = new Color(0, 120, 215);
//        normalTextColor = Color.DARK_GRAY;
//
//        drawerMenu = new JPanel();
//        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
//        drawerMenu.setBackground(backgroundColor);
//        drawerMenu.setPreferredSize(new Dimension(250, 800));
//        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
//
//        JButton accountBtn = createNavButton("account-circle", "Admin", true);
//        accountBtn.setEnabled(false);
//        drawerMenu.add(accountBtn);
//        drawerMenu.add(Box.createVerticalStrut(20));
//
//        drawerMenu.add(createNavButton("home", "Trang chủ", false, AdminDashboardView::createDashboardPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false,
//                () -> AdminSearchView.createSearchPanel(mainFrame)));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("calendar-check", "Đặt phòng", false,
//                BookingView::createBookingPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("bed-outline", "Phòng nghỉ", false,
//                RoomView::createRoomSearchPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
////        drawerMenu.add(createNavButton("account-multiple", "Khách hàng", false,
////                CustomerView::createCustomerSearchPanel(controller));
//
//        drawerMenu.add(createNavButton("account-multiple", "Khách hàng", false,
//                () -> CustomerView.createCustomerSearchPanel(controller)
//        ));
//
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("clipboard-check", "Yêu cầu", false,
//                RequestView::createNotificationPanel));
//        drawerMenu.add(Box.createVerticalStrut(8));
//
//        drawerMenu.add(createNavButton("chart-bar", "Thống kê", false,
//                StatisticalView::createDashboardPanel));
//
//        drawerMenu.add(Box.createVerticalGlue());
//
//        drawerMenu.add(createNavButton("logout", "Đăng xuất", false, () -> {
//            mainFrame.showLoginPanel();
//            return new JPanel();
//        }));
//
//        mainContentPanel = new JPanel(new BorderLayout());
//        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        mainContentPanel.setBackground(Color.WHITE);
//
//        add(drawerMenu, BorderLayout.WEST);
//        add(mainContentPanel, BorderLayout.CENTER);
//
//        setDynamicContent(AdminDashboardView.createDashboardPanel());
//
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
//                    setDynamicContent(panelSupplier.get());
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
//    public void setDynamicContent(JPanel panel) {
//        mainContentPanel.removeAll();
//        mainContentPanel.add(panel, BorderLayout.CENTER);
//        mainContentPanel.revalidate();
//        mainContentPanel.repaint();
//    }
//
//    public void resetToDefault() {
//        setDynamicContent(AdminDashboardView.createDashboardPanel());
//
//        for (Component comp : drawerMenu.getComponents()) {
//            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
//                selectButton(btn);
//                break;
//            }
//        }
//    }
//
//    public void setSelectedMenu(String menuName) {
//        for (Component comp : drawerMenu.getComponents()) {
//            if (comp instanceof JButton btn && btn.getText().equals(menuName)) {
//                selectButton(btn);
//                break;
//            }
//        }
//    }
//
//
//    public void setMainContent(JPanel panel) {
//        setDynamicContent(panel);
//    }
//}


package org.example.view;

import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import org.example.controller.CustomerController;
import org.example.controller.RequestController;
import org.example.entity.Request;
import org.example.service.BookingService;
import org.example.action.CheckRequest;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;

public class AdminContainerView extends JPanel {
    private final MainFrameView mainFrame;
    private final Color backgroundColor;
    private final Color hoverColor;
    private final Color pressedColor;
    private final Color selectedTextColor;
    private final Color normalTextColor;

    private JButton selectedButton = null;
    private final JPanel mainContentPanel;
    private final JPanel drawerMenu;
    private static final int LEFT_PADDING = 65;
    private CustomerController customerController = new CustomerController(new BookingService());
    private RequestController requestController = new RequestController(); // Added RequestController

    public AdminContainerView(MainFrameView mainFrame) {
        this.mainFrame = mainFrame;

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

        JButton accountBtn = createNavButton("account-circle", "Admin", true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);
        drawerMenu.add(Box.createVerticalStrut(20));

        drawerMenu.add(createNavButton("home", "Trang chủ", false, AdminDashboardView::createDashboardPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false,
                () -> AdminSearchView.createSearchPanel(mainFrame)));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("calendar-check", "Đặt phòng", false,
                BookingView::createBookingPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("bed-outline", "Phòng nghỉ", false,
                RoomView::createRoomSearchPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("account-multiple", "Khách hàng", false,
                () -> CustomerView.createCustomerSearchPanel(customerController)));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("clipboard-check", "Yêu cầu", false, this::createRequestNotificationPanel));
        drawerMenu.add(Box.createVerticalStrut(8));

        drawerMenu.add(createNavButton("chart-bar", "Thống kê", false,
                StatisticalView::createDashboardPanel));

        drawerMenu.add(Box.createVerticalGlue());

        drawerMenu.add(createNavButton("logout", "Đăng xuất", false, () -> {
            mainFrame.showLoginPanel();
            return new JPanel();
        }));

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContentPanel.setBackground(Color.WHITE);

        add(drawerMenu, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        setDynamicContent(AdminDashboardView.createDashboardPanel());

        for (Component comp : drawerMenu.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
                selectButton(btn);
                break;
            }
        }
    }

    private JPanel createRequestNotificationPanel() {
        // Fetch filtered requests
        List<Request> filteredRequests = requestController.getFilteredRequests();

        // Create table model
        String[] columns = {"Thông báo"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        // Load data into the model
        requestController.loadTableData(filteredRequests, model);

        // Create renderer and editor
        RequestView.ButtonLikeRenderer renderer = new RequestView.ButtonLikeRenderer();
        JTable table = new JTable(model); // Create table for editor
        CheckRequest editor = new CheckRequest(new JCheckBox(), model, filteredRequests, table, requestController);

        // Create the notification panel
        return RequestView.createNotificationPanel(filteredRequests, model, renderer, editor);
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
                    setDynamicContent(panelSupplier.get());
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

    public void setDynamicContent(JPanel panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    public void resetToDefault() {
        setDynamicContent(AdminDashboardView.createDashboardPanel());

        for (Component comp : drawerMenu.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals("Trang chủ")) {
                selectButton(btn);
                break;
            }
        }
    }

    public void setSelectedMenu(String menuName) {
        for (Component comp : drawerMenu.getComponents()) {
            if (comp instanceof JButton btn && btn.getText().equals(menuName)) {
                selectButton(btn);
                break;
            }
        }
    }

    public void setMainContent(JPanel panel) {
        setDynamicContent(panel);
    }
}
import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Test2 extends JFrame {

    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color selectedTextColor;
    private Color normalTextColor;

    private JButton selectedButton = null;

    private static final int LEFT_PADDING = 65;

    // Components ngày giờ + loại phòng
    private DatePicker dpCheckIn, dpCheckOut;
    private JComboBox<Integer> cbCheckInHour, cbCheckInMinute, cbCheckOutHour, cbCheckOutMinute;
    private JComboBox<String> cbLoaiPhong;
    private JTable table;
    private DefaultTableModel tableModel;

    public Test2() {
        setTitle("Ứng dụng tìm kiếm phòng với Drawer Menu");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Font Arial toàn bộ UI
        setUIFont(new javax.swing.plaf.FontUIResource("Arial", Font.PLAIN, 14));

        backgroundColor = UIManager.getColor("Panel.background");
        hoverColor = backgroundColor.brighter();
        pressedColor = new Color(220, 230, 255);
        selectedTextColor = new Color(0, 120, 215);
        normalTextColor = Color.DARK_GRAY;

        JPanel drawerMenu = createDrawerMenu();
        JPanel searchPanel = createSearchPanel();
        searchPanel.setPreferredSize(new Dimension(950, 800));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(drawerMenu, BorderLayout.WEST);
        getContentPane().add(searchPanel, BorderLayout.CENTER);
    }

    private JPanel createDrawerMenu() {
        JPanel drawerMenu = new JPanel();
        drawerMenu.setLayout(new BoxLayout(drawerMenu, BoxLayout.Y_AXIS));
        drawerMenu.setBackground(backgroundColor);
        drawerMenu.setPreferredSize(new Dimension(250, 800));
        drawerMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton accountBtn = createNavButton("account-circle", "DieuLynh612", true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);

        drawerMenu.add(Box.createVerticalStrut(20));

        drawerMenu.add(createNavButton("home", "Trang chủ", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("clipboard-check", "Thông báo", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("calendar-check", "Đặt phòng", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(Box.createVerticalGlue());
        drawerMenu.add(createNavButton("logout", "Đăng xuất", false));

        // Mặc định chọn Trang chủ
        selectButton((JButton) drawerMenu.getComponent(0 + 2)); // Vì có accountBtn + 1 khoảng cách + Trang chủ là nút thứ 2

        return drawerMenu;
    }

    private JButton createNavButton(String iconName, String text, boolean centerAligned) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(normalTextColor);
        button.setFont(new Font("Arial", Font.PLAIN, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(250, 40));
        button.setPreferredSize(new Dimension(250, 40));
        button.setMinimumSize(new Dimension(250, 40));

        MaterialDesign mdiIcon;
        try {
            mdiIcon = MaterialDesign.valueOf(toConstantName(iconName));
        } catch (IllegalArgumentException e) {
            mdiIcon = MaterialDesign.MDI_HELP_CIRCLE_OUTLINE;
        }
        FontIcon icon = FontIcon.of(mdiIcon, 20);
        icon.setIconColor(normalTextColor);
        button.setIcon(icon);
        button.setIconTextGap(10);

        if(centerAligned){
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Arial", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        } else {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorder(BorderFactory.createEmptyBorder(10, LEFT_PADDING, 10, 0));
        }

        if (button.isEnabled()) {
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(hoverColor);
                    }
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(backgroundColor);
                    }
                }
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(pressedColor);
                    }
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(hoverColor);
                    }
                }
            });

            button.addActionListener(e -> selectButton(button));
        }

        return button;
    }

    private String toConstantName(String iconName) {
        return "MDI_" + iconName.toUpperCase().replace("-", "_");
    }

    private void selectButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(backgroundColor);
            selectedButton.setForeground(normalTextColor);
            ((FontIcon) selectedButton.getIcon()).setIconColor(normalTextColor);
            selectedButton.repaint();
        }
        button.setBackground(backgroundColor);
        button.setForeground(selectedTextColor);
        ((FontIcon) button.getIcon()).setIconColor(selectedTextColor);
        button.repaint();
        selectedButton = button;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Tạo DatePicker cho Ngày đến và Ngày đi
        DatePickerSettings dateSettingsCheckIn = new DatePickerSettings();
        dateSettingsCheckIn.setFormatForDatesCommonEra("dd-MM-yyyy");
        dpCheckIn = new DatePicker(dateSettingsCheckIn);

        DatePickerSettings dateSettingsCheckOut = new DatePickerSettings();
        dateSettingsCheckOut.setFormatForDatesCommonEra("dd-MM-yyyy");
        dpCheckOut = new DatePicker(dateSettingsCheckOut);

        Integer[] hours = new Integer[24];
        for (int i = 0; i < 24; i++) hours[i] = i;
        Integer[] minutes = new Integer[60];
        for (int i = 0; i < 60; i++) minutes[i] = i;

        cbCheckInHour = new JComboBox<>(hours);
        cbCheckInMinute = new JComboBox<>(minutes);
        cbCheckOutHour = new JComboBox<>(hours);
        cbCheckOutMinute = new JComboBox<>(minutes);

        Dimension comboSize = new Dimension(60, cbCheckInHour.getPreferredSize().height);
        cbCheckInHour.setPreferredSize(comboSize);
        cbCheckInMinute.setPreferredSize(comboSize);
        cbCheckOutHour.setPreferredSize(comboSize);
        cbCheckOutMinute.setPreferredSize(comboSize);

        cbLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"});

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 1: Ngày đến + giờ + phút
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Ngày đến:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dpCheckIn, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cbCheckInHour, gbc);
        gbc.gridx = 4;
        formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5;
        formPanel.add(cbCheckInMinute, gbc);

        // Hàng 1 tiếp: Loại phòng lùi phải
        gbc.gridx = 7;
        gbc.weightx = 0.5;  // để đẩy sang phải
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Loại phòng:"), gbc);

        gbc.gridx = 8;
        gbc.weightx = 0;
        formPanel.add(cbLoaiPhong, gbc);

        // Hàng 2: Ngày đi + giờ + phút lùi trái
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        gbc.gridx = 1;
        formPanel.add(dpCheckOut, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Giờ:"), gbc);
        gbc.gridx = 3;
        formPanel.add(cbCheckOutHour, gbc);
        gbc.gridx = 4;
        formPanel.add(new JLabel("Phút:"), gbc);
        gbc.gridx = 5;
        formPanel.add(cbCheckOutMinute, gbc);

        // Nút Tìm kiếm và Đặt phòng
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JButton btnDatPhong = new JButton("Đặt phòng");
        btnPanel.add(btnTimKiem);
        btnPanel.add(btnDatPhong);

        // Bảng kết quả
        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Hàm set font Arial toàn bộ UI
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new Test2().setVisible(true);
        });
    }
}

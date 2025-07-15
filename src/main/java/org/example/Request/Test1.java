package org.example.Request;

import com.formdev.flatlaf.FlatLightLaf;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Test1 extends JFrame {

    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color selectedTextColor;
    private Color normalTextColor;

    private JButton selectedButton = null;

    private static final int LEFT_PADDING = 65;

    // Component bên phải (panel tìm kiếm giao diện)
    private JTextField tfCheckIn, tfCheckOut;
    private JComboBox<String> cbLoaiPhong;
    private JTable table;
    private DefaultTableModel tableModel;

    public Test1() {
        setTitle("Ứng dụng tìm kiếm phòng với Drawer Menu");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        backgroundColor = UIManager.getColor("Panel.background");
        hoverColor = backgroundColor.brighter();
        pressedColor = new Color(220, 230, 255);
        selectedTextColor = new Color(0, 120, 215);
        normalTextColor = Color.DARK_GRAY;

        // Phần drawer menu bên trái
        JPanel drawerMenu = createDrawerMenu();

        // Phần giao diện tìm kiếm bên phải (chỉ giữ giao diện, không logic)
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

        JButton accountBtn = createNavButton("account-circle", "Admin", true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);

        drawerMenu.add(Box.createVerticalStrut(20));

        drawerMenu.add(createNavButton("home", "Trang chủ", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("magnify", "Tìm kiếm", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("calendar-check", "Đặt phòng", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("bed-outline", "Phòng nghỉ", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("account-multiple", "Khách hàng", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("clipboard-check", "Yêu cầu", false));
        drawerMenu.add(Box.createVerticalStrut(8));
        drawerMenu.add(createNavButton("chart-bar", "Thống kê", false));

        drawerMenu.add(Box.createVerticalGlue());

        drawerMenu.add(createNavButton("logout", "Đăng xuất", false));

        return drawerMenu;
    }

    private JButton createNavButton(String iconName, String text, boolean centerAligned) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(normalTextColor);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        } else {
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setBorder(BorderFactory.createEmptyBorder(10, LEFT_PADDING, 10, 0));
        }

        if (button.isEnabled()) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(hoverColor);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(backgroundColor);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (button != selectedButton) {
                        button.setBackground(pressedColor);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
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

        // Phần form thông tin tìm kiếm
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Thông tin tìm kiếm", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Ngày đến
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Ngày đến:"), gbc);
        tfCheckIn = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(tfCheckIn, gbc);

        // Ngày đi
        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày đi:"), gbc);
        tfCheckOut = new JTextField(10);
        gbc.gridx = 3;
        formPanel.add(tfCheckOut, gbc);

        // Loại phòng
        gbc.gridx = 4;
        formPanel.add(new JLabel("Loại phòng:"), gbc);
        cbLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"});
        gbc.gridx = 5;
        formPanel.add(cbLoaiPhong, gbc);

        // Bảng kết quả
        String[] columns = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        // Nút bấm
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JButton btnDatPhong = new JButton("Đặt phòng");
        btnPanel.add(btnTimKiem);
        btnPanel.add(btnDatPhong);

        // Không xử lý sự kiện tìm kiếm hay đặt phòng vì bạn muốn giao diện trống
        // btnTimKiem.addActionListener(...);
        // btnDatPhong.addActionListener(...);

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Test1 frame = new Test1();
            frame.setVisible(true);
        });
    }
}

import com.formdev.flatlaf.FlatLightLaf;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color selectedTextColor;
    private Color normalTextColor;

    private JButton selectedButton = null;

    // Khoảng cách căn trái chuẩn (lấy theo "Quản lý phòng")
    private static final int LEFT_PADDING = 65;

    public MainFrame() {
        setTitle("Drawer Menu tràn full viền");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        // Nút "Admin" – căn giữa hẳn, không tương tác
        JButton accountBtn = createNavButton("account-circle", "Admin", true);
        accountBtn.setEnabled(false);
        drawerMenu.add(accountBtn);

        drawerMenu.add(Box.createVerticalStrut(20));

        // Các nút chức năng chính - căn trái theo LEFT_PADDING
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

        // Nút đăng xuất - căn trái theo LEFT_PADDING
        drawerMenu.add(createNavButton("logout", "Đăng xuất", false));

        JPanel mainContent = new JPanel();
        mainContent.setBackground(Color.WHITE);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(drawerMenu, BorderLayout.WEST);
        getContentPane().add(mainContent, BorderLayout.CENTER);
    }

    /**
     * Tạo nút với icon và text
     * @param iconName tên icon trong MaterialDesign
     * @param text text hiển thị trên nút
     * @param centerAligned nếu true thì căn giữa toàn bộ (icon + text)
     *                      nếu false thì căn trái theo LEFT_PADDING (đồng nhất theo chuẩn quản lý phòng)
     * @return JButton
     */
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
            // Nút "Admin" căn giữa toàn bộ nội dung (icon + text)
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        } else {
            // Các nút khác căn trái theo LEFT_PADDING, tức phần icon+text bắt đầu cách trái nút LEFT_PADDING px
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}

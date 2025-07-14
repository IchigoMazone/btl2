import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Test16 extends JFrame {
    private JTable table;
    private JComboBox<String> searchTypeCombo;
    private JComboBox<String> statusCombo;
    private JTextField keywordField;
    private DefaultTableModel model;

    public Test16() {
        setTitle("Quản lý đặt phòng khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 800);
        setLocationRelativeTo(null);
        FlatLightLaf.setup();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        mainPanel.add(createSearchPanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createTablePanel());
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setBorder(new TitledBorder("Tìm kiếm"));

        searchTypeCombo = new JComboBox<>(new String[]{"Tài khoản", "Họ tên", "Số điện thoại", "Gmail"});
        keywordField = new JTextField(20);

        statusCombo = new JComboBox<>(new String[]{"Tất cả", "Đã đặt", "Đã check-in"});

        panel.add(new JLabel("Tìm theo:"));
        panel.add(searchTypeCombo);
        panel.add(keywordField);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(statusCombo);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
                "Mã đơn", "Tài khoản", "Họ tên", "SĐT", "Gmail",
                "Phòng", "Loại phòng", "Ngày đến", "Ngày đi",
                "Trạng thái", "Ghi chú", "Dịch vụ kèm", "Tổng tiền"
        };

        Object[][] data = {
                {"001", "user123", "Nguyễn A", "0901234567", "a@gmail.com", "P101", "Đơn", "12/07", "14/07", "Đã đặt", "Không", "Không", "2.000.000"},
                {"002", "user456", "Trần B", "0902345678", "b@gmail.com", "P102", "Đôi", "13/07", "15/07", "Đã check-in", "Không", "Không", "3.500.000"},
                {"003", "user789", "Lê C", "0903456789", "c@gmail.com", "P103", "Gia đình", "10/07", "12/07", "Đã check-out", "Không", "Không", "5.000.000"}
        };

        model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Căn giữa tiêu đề cột
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        // ✅ Cập nhật chiều rộng từng cột
        int[] widths = {150, 150, 250, 150, 250, 100, 150, 100, 100, 150, 300, 300, 100};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        scrollPane.setBorder(new TitledBorder("Danh sách đơn đặt phòng"));
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton searchBtn = new JButton("Tìm kiếm");
        JButton editBtn = new JButton("Thay đổi");
        JButton updateBtn = new JButton("Cập nhật"); // ✅ Sửa tên nút

        panel.add(searchBtn);
        panel.add(editBtn);
        panel.add(updateBtn);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Test16().setVisible(true));
    }
}

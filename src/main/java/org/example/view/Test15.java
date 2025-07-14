import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Test15 {

    public static JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(Color.WHITE);

        panel.add(createSearchPanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createTablePanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButtonPanel());

        return panel;
    }

    private static JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setBorder(new TitledBorder("Tìm kiếm"));
        panel.setBackground(UIManager.getColor("Panel.background"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{
                "Tài khoản", "Họ tên", "Số điện thoại", "Gmail"
        });
        JTextField keywordField = new JTextField(18);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{
                "Tất cả", "Đã đặt", "Đã check-in"
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tìm theo:"), gbc);

        gbc.gridx = 1;
        panel.add(searchTypeCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Từ khóa:"), gbc);

        gbc.gridx = 3;
        panel.add(keywordField, gbc);

        gbc.gridx = 4;
        panel.add(new JLabel("Trạng thái:"), gbc);

        gbc.gridx = 5;
        panel.add(statusCombo, gbc);

        return panel;
    }

    private static JScrollPane createTablePanel() {
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

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

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

    private static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton searchBtn = new JButton("Tìm kiếm");
        JButton editBtn = new JButton("Thay đổi");
        JButton updateBtn = new JButton("Cập nhật");

        panel.add(searchBtn);
        panel.add(editBtn);
        panel.add(updateBtn);

        return panel;
    }
}

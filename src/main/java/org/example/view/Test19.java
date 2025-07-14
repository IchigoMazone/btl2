import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Test19 {

    // Tạo panel hiển thị lịch sử thanh toán đã hoàn tất của user
    public static JPanel createPaymentHistoryPanel() {
        // Tên người dùng cần lọc, ví dụ từ session hiện tại hoặc hardcoded tạm thời
        String username = "Nguyen Van A";

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Dữ liệu mẫu
        Object[][] data = {
                {"PMTF7D81", "BK20250708", "Nguyen Van A", "2500.0", "Credit Card", "Đã thanh toán", "2025-07-13T18:06:44"},
                {"PMTF7D82", "BK20250709", "Nguyen Van A", "1800.0", "Banking", "Đã thanh toán", "2025-07-12T14:23:10"},
                {"PMTF7D83", "BK20250710", "Nguyen Van A", "3000.0", "Momo", "Đã hủy", "2025-07-11T10:45:00"},
                {"PMTF7D84", "BK20250711", "Nguyen Van B", "2000.0", "Credit Card", "Đã thanh toán", "2025-07-09T17:15:32"}
        };

        String[] columns = {
                "Mã thanh toán", "Mã đặt phòng", "Họ tên", "Số tiền (VNĐ)",
                "Phương thức", "Trạng thái", "Thời gian thanh toán"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Lọc chỉ lấy lịch sử đã thanh toán của người dùng đó
        for (Object[] row : data) {
            String fullName = row[2].toString().toLowerCase();
            String status = row[5].toString();
            if (fullName.contains(username.toLowerCase()) && status.equals("Đã thanh toán")) {
                model.addRow(row);
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Cho phép cuộn ngang

        // Cố định độ rộng từng cột để lấp đầy bảng
        int[] columnWidths = {130, 130, 150, 130, 120, 120, 200};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Lịch sử thanh toán"));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NotificationTableUI extends JFrame {

    public NotificationTableUI() {
        setTitle("Danh sách thông báo đặt phòng");
        setSize(950, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Sample data
        Object[][] data = {
                {"Nguyễn Văn A", "10/07/2025", "12/07/2025", "Deluxe", "Xem"},
                {"Trần Thị B", "11/07/2025", "13/07/2025", "Standard", "Xem"},
        };

        String[] columnNames = {"Khách hàng", "Check-in", "Check-out", "Loại phòng", "Chi tiết"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.getColumn("Chi tiết").setCellRenderer(new ButtonRenderer());
        table.getColumn("Chi tiết").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    // Renderer cho nút
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Xem");
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Xem");
            return this;
        }
    }

    // Editor xử lý sự kiện click nút
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Xem");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            button.addActionListener((ActionEvent e) -> {
                fireEditingStopped();
                showDetailDialog(selectedRow);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            clicked = true;
            return button;
        }

        public Object getCellEditorValue() {
            clicked = false;
            return "Xem";
        }

        private void showDetailDialog(int row) {
            // Đây là popup hiển thị chi tiết — bạn có thể lấy thông tin từ model nếu cần
            JOptionPane.showMessageDialog(button,
                    "Chi tiết đơn đặt phòng cho khách hàng dòng " + (row + 1),
                    "Chi tiết đơn", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("FlatLaf không được tải.");
        }

        SwingUtilities.invokeLater(() -> {
            new NotificationTableUI().setVisible(true);
        });
    }
}

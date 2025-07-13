import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BookingStatusManager extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public BookingStatusManager() {
        setTitle("Quản lý trạng thái đặt phòng");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        String[] columnNames = {"Phòng", "Trạng thái", "Hành động"};
        Object[][] data = {
                {"Phòng 101", "Available", "Thay đổi"},
                {"Phòng 102", "Confirmed", "Thay đổi"},
                {"Phòng 103", "Checked-in", "Thay đổi"},
                {"Phòng 104", "Maintenance", "Thay đổi"}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cho chỉnh cột Hành động
            }
        };

        table = new JTable(tableModel);
        table.getColumn("Hành động").setCellRenderer(new ButtonRenderer());
        table.getColumn("Hành động").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void changeStatus(int row) {
        String current = (String) tableModel.getValueAt(row, 1);
        String[] statuses = {"Available", "Pending", "Confirmed", "Checked-in", "Checked-out", "Cancelled", "Maintenance"};

        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Chọn trạng thái mới:",
                "Thay đổi trạng thái",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statuses,
                current
        );

        if (newStatus != null && !newStatus.equals(current)) {
            tableModel.setValueAt(newStatus, row, 1);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // FlatLaf theme
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new BookingStatusManager().setVisible(true));
    }

    // --- Button Renderer (hiển thị nút trong bảng)
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Thay đổi");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    // --- Button Editor (xử lý khi click nút)
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;
        private BookingStatusManager manager;

        public ButtonEditor(JCheckBox checkBox, BookingStatusManager manager) {
            super(checkBox);
            this.manager = manager;
            button = new JButton("Thay đổi");
            button.addActionListener((ActionEvent e) -> manager.changeStatus(row));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Thay đổi";
        }
    }
}

package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RequestView {

    public static JPanel createNotificationPanel() {
        Color backgroundColor = UIManager.getColor("Panel.background");

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        container.setBackground(Color.WHITE);

        String[] columns = {"Thông báo"};
        Object[][] data = {
                {" Nguyễn Văn A đã đặt phòng lúc 14:20"},
                {" Trần Thị B đã huỷ đặt phòng lúc 15:05"},
                {" Lê Văn C đã đặt phòng lúc 16:30"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int col) {
                return true;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setCellRenderer(new ButtonLikeRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(new ButtonLikeEditor(new JCheckBox(), model));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Thông báo hệ thống"));

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
        public ButtonLikeRenderer() {
            styleButton(this);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());

            // Nếu là đã đọc thì đổi màu text xám
            if (value != null && value.toString().contains("[Đã đọc]")) {
                setForeground(Color.GRAY);
            } else {
                setForeground(new Color(33, 99, 255));
            }

            return this;
        }
    }

    static class ButtonLikeEditor extends DefaultCellEditor {
        private final JButton button;
        private int selectedRow;
        private final DefaultTableModel model;

        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model) {
            super(checkBox);
            this.model = model;
            button = new JButton();
            styleButton(button);

            button.addActionListener(e -> {
                fireEditingStopped();
                showNotificationDialog(selectedRow);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            button.setText(value == null ? "" : value.toString());
            return button;
        }

        public Object getCellEditorValue() {
            return button.getText();
        }

        private void showNotificationDialog(int row) {
            String message = (String) model.getValueAt(row, 0);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
            dialog.setSize(400, 240);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel content = new JPanel(new BorderLayout());
            content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
            JTextArea area = new JTextArea(message);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            area.setBackground(dialog.getBackground());

            content.add(area, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnQuayLai = new JButton("Quay lại");
            JButton btnCancel = new JButton("Hủy");
            JButton btnConfirm = new JButton("Xác nhận");

            styleDialogButton(btnQuayLai, new Color(120, 120, 120));  // xám
            styleDialogButton(btnCancel, new Color(200, 55, 60));     // đỏ
            styleDialogButton(btnConfirm, new Color(0, 153, 76));     // xanh lá

            btnQuayLai.addActionListener(e -> {
                String current = (String) model.getValueAt(row, 0);
                if (!current.contains("[Đã đọc]")) {
                    model.setValueAt(current + " [Đã đọc]", row, 0);
                }
                dialog.dispose();
            });

            btnCancel.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(dialog,
                        "Bạn có chắc chắn muốn hủy thao tác không?",
                        "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(dialog, "Bạn đã hủy thao tác.");
                    dialog.dispose();
                }
            });

            btnConfirm.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(dialog,
                        "Bạn có chắc chắn muốn xác nhận thông báo này?",
                        "Xác nhận thông báo", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(dialog, "Bạn đã xác nhận thông báo.");
                    dialog.dispose();
                }
            });

            buttons.add(btnQuayLai);
            buttons.add(btnCancel);
            buttons.add(btnConfirm);

            dialog.add(content, BorderLayout.CENTER);
            dialog.add(buttons, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setForeground(new Color(33, 99, 255));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMargin(new Insets(5, 10, 5, 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setOpaque(true);
                button.setBackground(new Color(230, 240, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setOpaque(false);
                button.setBackground(null);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 220, 250));
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 240, 255));
            }
        });
    }

    private static void styleDialogButton(JButton btn, Color bgColor) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }
}

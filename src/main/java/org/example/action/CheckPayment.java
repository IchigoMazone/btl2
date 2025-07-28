package org.example.action;

import org.example.controller.HistoryController;
import org.example.view.HistoryView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.border.EmptyBorder;

import java.text.SimpleDateFormat;

public class CheckPayment extends DefaultCellEditor {
    private final JButton button;
    private String label;
    private int selectedRow;
    private final DefaultTableModel model;
    private final Object[][] data;
    private final String username;
    private final HistoryController controller;

    public CheckPayment(JCheckBox checkBox, DefaultTableModel model, Object[][] data, String username, HistoryController controller) {
        super(checkBox);
        this.model = model;
        this.data = data;
        this.username = username;
        this.controller = controller;
        button = new JButton();
        HistoryView.styleButton(button);

        button.addActionListener(e -> {
            fireEditingStopped();
            showNotificationDialog(selectedRow);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = value == null ? "" : value.toString();
        selectedRow = row;
        button.setText(label);
        button.setForeground(label.contains("[Đã đọc]") ? Color.GRAY : new Color(33, 99, 255));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    private void showNotificationDialog(int row) {
        Object[] rowData = findRowData(row);
        if (rowData == null) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy dữ liệu thanh toán",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String bookingId = rowData[1].toString();
        String paymentId = rowData[0].toString();

        JDialog dialog = new JDialog((Frame) null, "Chi tiết thanh toán", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(15, 20, 10, 20));

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String formattedPaidAt, formattedCheckIn, formattedCheckOut;
        try {
            formattedPaidAt = outputFormat.format(inputFormat.parse(rowData[9].toString()));
            formattedCheckIn = outputFormat.format(inputFormat.parse(rowData[10].toString()));
            formattedCheckOut = outputFormat.format(inputFormat.parse(rowData[11].toString()));
        } catch (Exception e) {
            formattedPaidAt = rowData[9].toString();
            formattedCheckIn = rowData[10].toString();
            formattedCheckOut = rowData[11].toString();
            e.printStackTrace();
        }

        String notificationMessage = String.format("Chi tiết thanh toán");
        String details = String.format(
                "<html>" +
                        "<b>%s</b><br><br>" +
                        "Mã thanh toán: %s<br>" +
                        "Mã đặt phòng: %s<br><br>" +
                        "Họ và tên: %s<br>" +
                        "Tên người dùng: %s<br><br>" +
                        "Phòng: %s<br><br>" +
                        "Số tiền: %s VNĐ<br>" +
                        "Phương thức: %s<br>" +
                        "Mã giao dịch: %s<br><br>" +
                        "Thời gian thanh toán: %s<br>" +
                        "Nhận phòng: %s<br>" +
                        "Trả phòng: %s" +
                        "</html>",
                notificationMessage, rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5],
                rowData[6], rowData[7], formattedPaidAt, formattedCheckIn, formattedCheckOut
        );

        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(details);
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(content.getBackground());

        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setBorder(BorderFactory.createEmptyBorder());
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        content.add(textScrollPane);
        content.add(Box.createVerticalGlue());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");

        btnClose.addActionListener(e -> {
            if (!controller.isBookingRead(bookingId)) {
                controller.markBookingAsRead(bookingId);
                String current = (String) model.getValueAt(row, 0);
                if (!current.contains("[Đã đọc]")) {
                    model.setValueAt(current + " [Đã đọc]", row, 0);
                    try {
                        controller.updatePaymentStatus("payments.xml", paymentId, "Đã đọc");
                        Object[][] newData = controller.parseXMLData("payments.xml");
                        controller.loadTableData(newData, username, model);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật trạng thái: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            dialog.dispose();
        });

        buttons.add(btnClose);

        dialog.add(content, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private Object[] findRowData(int row) {
        int currentRow = -1;
        for (Object[] rowData : data) {
            String userName = rowData[3].toString().toLowerCase();
            String status = rowData[8].toString();
            if (userName.equalsIgnoreCase(username) && (status.equals("Đã thanh toán") || status.equals("Đã đọc"))) {
                currentRow++;
                if (currentRow == row) {
                    return rowData;
                }
            }
        }
        return null;
    }
}
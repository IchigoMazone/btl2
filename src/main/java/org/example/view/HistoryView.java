
package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.example.service.PaymentService;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class HistoryView {

    private static final Set<String> readBookings = new HashSet<>();
    private static final String[] TABLE_COLUMNS = {"Lịch sử thanh toán"};
    private static final String TABLE_TITLE = "Lịch sử thanh toán";
    private static final String ERROR_MESSAGE = "Không có lịch sử thanh toán";

    public static JPanel createPaymentHistoryPanel(String username) {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(0, 0, 0, 0));

        Object[][] data = parseXMLData("payments.xml");

        DefaultTableModel model = new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return true;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setToolTipText("Danh sách lịch sử thanh toán");

        loadTableData(data, username, model);

        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(
                new ButtonLikeEditor(new JCheckBox(), model, data, username)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(TABLE_TITLE));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private static Object[][] parseXMLData(String fileName) {
        try {
            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                return new Object[0][0];
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList paymentList = doc.getElementsByTagName("Payment");
            Object[][] data = new Object[paymentList.getLength()][12];

            for (int i = 0; i < paymentList.getLength(); i++) {
                Element payment = (Element) paymentList.item(i);
                data[i][0] = payment.getElementsByTagName("paymentId").item(0).getTextContent();
                data[i][1] = payment.getElementsByTagName("bookingId").item(0).getTextContent();
                data[i][2] = payment.getElementsByTagName("fullName").item(0).getTextContent();
                data[i][3] = payment.getElementsByTagName("userName").item(0).getTextContent();
                data[i][4] = payment.getElementsByTagName("room").item(0).getTextContent();
                data[i][5] = String.format("%,.0f", Double.parseDouble(payment.getElementsByTagName("amount").item(0).getTextContent()));
                data[i][6] = payment.getElementsByTagName("method").item(0).getTextContent();
                data[i][7] = payment.getElementsByTagName("code").item(0).getTextContent();
                data[i][8] = payment.getElementsByTagName("status").item(0).getTextContent();
                data[i][9] = payment.getElementsByTagName("paidAt").item(0).getTextContent();
                data[i][10] = payment.getElementsByTagName("checkIn").item(0).getTextContent();
                data[i][11] = payment.getElementsByTagName("checkOut").item(0).getTextContent();
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    private static void loadTableData(Object[][] data, String username, DefaultTableModel model) {
        model.setRowCount(0);
        boolean hasData = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Arrays.sort(data, Comparator.comparing(row -> {
            try {
                return LocalDateTime.parse((String) row[9], formatter);
            } catch (Exception e) {
                return LocalDateTime.now();
            }
        }));

        for (Object[] row : data) {
            String userName = row[3].toString().toLowerCase();
            String status = row[8].toString();
            if (userName.equalsIgnoreCase(username) && (status.equals("Đã thanh toán") || status.equals("Đã đọc"))) {
                String bookingId = row[1].toString();
                String timeString = row[9].toString();
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String formattedPaidAt = outputFormat.format(inputFormat.parse(timeString));
                    String notificationMessage = String.format(" Đơn đặt phòng %s đã được thanh toán lúc %s", bookingId, formattedPaidAt);
                    boolean isRead = readBookings.contains(bookingId) || status.equals("Đã đọc");
                    if (isRead) {
                        notificationMessage += " [Đã đọc]";
                    }
                    model.addRow(new Object[]{notificationMessage});
                    hasData = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasData) {
            model.addRow(new Object[]{ERROR_MESSAGE});
        }
    }

    static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
        public ButtonLikeRenderer() {
            styleButton(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());
            setForeground(value != null && value.toString().contains("[Đã đọc]")
                    ? Color.GRAY : new Color(33, 99, 255));
            return this;
        }
    }

    static class ButtonLikeEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private int selectedRow;
        private final DefaultTableModel model;
        private final Object[][] data;
        private final String username;

        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, Object[][] data, String username) {
            super(checkBox);
            this.model = model;
            this.data = data;
            this.username = username;
            button = new JButton();
            styleButton(button);

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
                if (!readBookings.contains(bookingId)) {
                    readBookings.add(bookingId);
                    String current = (String) model.getValueAt(row, 0);
                    if (!current.contains("[Đã đọc]")) {
                        model.setValueAt(current + " [Đã đọc]", row, 0);
                        try {
                            PaymentService.updatePaymentStatus("payments.xml", paymentId, "Đã đọc");
                            // Refresh table data to reflect updated status
                            Object[][] newData = parseXMLData("payments.xml");
                            loadTableData(newData, username, model);
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

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setForeground(new Color(33, 99, 255));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMargin(new Insets(5, 15, 5, 15));
        button.setToolTipText("Nhấn để xem chi tiết thanh toán");

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setOpaque(true);
                button.setBackground(new Color(230, 240, 255));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setOpaque(false);
                button.setBackground(null);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                button.setBackground(new Color(200, 220, 250));
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                button.setBackground(new Color(230, 240, 255));
            }
        });
    }
}





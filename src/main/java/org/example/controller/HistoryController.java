package org.example.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class HistoryController {
    private static final String XML_PATH = "payments.xml";
    private static final Set<String> readBookings = new HashSet<>();
    private final String username;
    private Object[][] paymentData;

    public HistoryController(String username) {
        this.username = username;
        this.paymentData = parseXMLData();
    }

    public Object[][] getPaymentData() {
        return paymentData;
    }

    public boolean isValidPayment(Object[] row) {
        String userName = row[3].toString().toLowerCase();
        String status = row[8].toString();
        return userName.equalsIgnoreCase(username) && status.equals("Đã thanh toán");
    }

    public boolean isBookingRead(String bookingId) {
        return readBookings.contains(bookingId);
    }

    public String formatPaymentMessage(Object[] row) {
        String bookingId = row[1].toString();
        String timeString = row[9].toString();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String formattedPaidAt = outputFormat.format(inputFormat.parse(timeString));
            return String.format("Đơn đặt phòng %s đã được thanh toán lúc %s", bookingId, formattedPaidAt);
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("Đơn đặt phòng %s đã được thanh toán lúc %s", bookingId, timeString);
        }
    }

    public void showNotificationDialog(JButton button, String bookingId) {
        Object[] row = getRowByBookingId(bookingId);
        if (row == null) return;

        JDialog dialog = new JDialog((Frame) null, "Chi tiết thanh toán", true);
        dialog.setLayout(new BorderLayout(10, 5));
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(5, 10, 5, 10));

        String formattedPaidAt, formattedCheckIn, formattedCheckOut;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            formattedPaidAt = outputFormat.format(inputFormat.parse(row[9].toString()));
            formattedCheckIn = outputFormat.format(inputFormat.parse(row[10].toString()));
            formattedCheckOut = outputFormat.format(inputFormat.parse(row[11].toString()));
        } catch (Exception e) {
            formattedPaidAt = row[9].toString();
            formattedCheckIn = row[10].toString();
            formattedCheckOut = row[11].toString();
            e.printStackTrace();
        }

        String notificationMessage = String.format("Chi tiết thanh toán", bookingId, formattedPaidAt);
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
                notificationMessage, row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], formattedPaidAt, formattedCheckIn, formattedCheckOut
        );

        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(details);
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(content.getBackground());
        textPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setBorder(BorderFactory.createEmptyBorder());
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textScrollPane.setPreferredSize(new Dimension(550, 400));

        content.add(textScrollPane);
        content.add(Box.createVerticalGlue());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Đóng");
        styleDialogButton(btnClose, new Color(0, 120, 215));

        btnClose.addActionListener(e -> {
            if (!readBookings.contains(bookingId)) {
                readBookings.add(bookingId);
                button.setForeground(Color.GRAY);
                String currentText = button.getText().replace(" [Đã đọc]", "");
                button.setText(currentText + " [Đã đọc]");
                FontMetrics fm = button.getFontMetrics(button.getFont());
                int maxWidth = 400;
                int lineHeight = fm.getHeight();
                int lines = (int) Math.ceil((double) fm.stringWidth(currentText + " [Đã đọc]") / maxWidth);
                int buttonHeight = lines * lineHeight + 10;
                button.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
                button.setPreferredSize(new Dimension(maxWidth, buttonHeight));
                button.getParent().revalidate();
                button.getParent().repaint();
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

    private Object[][] parseXMLData() {
        try {
            File xmlFile = new File(XML_PATH);
            if (!xmlFile.exists()) {
                System.err.println("File XML not found: " + XML_PATH);
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
                data[i][5] = payment.getElementsByTagName("amount").item(0).getTextContent();
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

    private Object[] getRowByBookingId(String bookingId) {
        for (Object[] row : paymentData) {
            if (row[1].toString().equals(bookingId)) {
                return row;
            }
        }
        return null;
    }

    private void styleDialogButton(JButton btn, Color bgColor) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }
}
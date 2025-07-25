

package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class HistoryView {

    private static final Set<String> readBookings = new HashSet<>();

    // Tạo panel hiển thị lịch sử thanh toán của người dùng
    public static JPanel createPaymentHistoryPanel(String username) {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Đọc dữ liệu từ file XML
        Object[][] data = parseXMLData("payments.xml");

        // Panel chứa các nút thông báo
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(new TitledBorder("Lịch sử thanh toán"));
        buttonPanel.setBackground(Color.WHITE);

        // Tạo các nút từ dữ liệu thanh toán
        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            String userName = row[3].toString().toLowerCase();
            String status = row[8].toString();
            if (userName.equalsIgnoreCase(username) && status.equals("Đã thanh toán")) {
                String bookingId = row[1].toString();
                String timeString = row[9].toString();
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String formattedPaidAt = outputFormat.format(inputFormat.parse(timeString));
                    String notificationMessage = String.format(" Đơn đặt phòng %s đã được thanh toán lúc %s", bookingId, formattedPaidAt);
                    boolean isRead = readBookings.contains(bookingId);

                    JButton button = createStyledButton(notificationMessage, bookingId, isRead, row);
                    buttonPanel.add(button);
                    if (i < data.length - 1) buttonPanel.add(Box.createVerticalStrut(5));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Thêm scroll pane để cuộn dọc nếu có nhiều nút
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    // Hàm parse dữ liệu từ file XML
    private static Object[][] parseXMLData(String fileName) {
        try {
            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                System.err.println("File XML not found: " + fileName);
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

    private static JButton createStyledButton(String text, String bookingId, boolean isRead, Object[] row) {
        String htmlText = "<html>" + text + (isRead ? " [Đã đọc]" : "") + "</html>";
        JButton button = new JButton(htmlText);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Font và màu
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(isRead ? Color.GRAY : new Color(33, 99, 255));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // Khôi phục viền xám cho nút
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                new EmptyBorder(5, 5, 5, 20)
        ));

        // Tính toán kích thước nút
        FontMetrics fm = button.getFontMetrics(button.getFont());
        int maxWidth = 400;
        int lineHeight = fm.getHeight();
        int lines = (int) Math.ceil((double) fm.stringWidth(text + (isRead ? " [Đã đọc]" : "")) / maxWidth);
        int buttonHeight = lines * lineHeight + 10;

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonHeight));
        button.setPreferredSize(new Dimension(maxWidth, buttonHeight));

        // Thêm hành động khi nhấn nút
        button.addActionListener(e -> showNotificationDialog(button, bookingId, row));

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!isRead) button.setBackground(new Color(230, 240, 255));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!isRead) button.setBackground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (!isRead) button.setBackground(new Color(200, 220, 250));
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                if (!isRead) button.setBackground(new Color(230, 240, 255));
            }
        });

        return button;
    }

    // Hiển thị hộp thoại thông báo với thông báo ở đầu và các chi tiết còn lại
    private static void showNotificationDialog(JButton button, String bookingId, Object[] row) {
        JDialog dialog = new JDialog((Frame) null, "Chi tiết thanh toán", true);
        dialog.setLayout(new BorderLayout(10, 5)); // Giảm gap dọc để gần nút Đóng hơn

        // Thêm viền xám cho toàn bộ dialog
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));

        // Sử dụng JPanel để chứa JTextPane
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Format thời gian
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String formattedPaidAt, formattedCheckIn, formattedCheckOut;
        try {
            formattedPaidAt = outputFormat.format(inputFormat.parse(row[9].toString()));
            formattedCheckIn = outputFormat.format(inputFormat.parse(row[10].toString()));
            formattedCheckOut = outputFormat.format(inputFormat.parse(row[11].toString()));
        } catch (Exception e) {
            formattedPaidAt = row[9].toString();
            formattedCheckIn = row[10].toString();
            formattedCheckOut = row[11].toString();
            e.printStackTrace();
        }

        // Tạo thông báo làm tiêu đề
        String notificationMessage = String.format("Chi  tiet thanh toan", bookingId, formattedPaidAt);
        // Tạo chuỗi chứa các chi tiết, bỏ trường Trạng thái
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

        // Thêm JScrollPane cho textPane
        // Không dùng JScrollPane, hoặc nếu muốn giữ để tránh lỗi layout thì ẩn thanh cuộn:
        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền xanh
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textScrollPane.setPreferredSize(null); // Cho phép layout tự co giãn theo nội dung

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
                // Cập nhật kích thước nút
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

        // Đặt kích thước dialog lớn hơn
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void styleDialogButton(JButton btn, Color bgColor) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }
}



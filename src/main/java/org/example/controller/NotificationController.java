//package org.example.controller;
//
//import org.example.entity.Booking;
//import org.example.entity.Notification;
//import org.example.entity.NotificationXML;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.service.BookingService;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.utils.FileUtils;
//import org.example.view.NotificationView;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder; // Added import for EmptyBorder
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.time.format.DateTimeFormatter;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class NotificationController {
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
//    private static final String XML_PATH = "notifications.xml";
//    private static final String REQUESTS_XML_PATH = "requests.xml";
//    private static final String ERROR_MESSAGE = "Không có thông báo";
//
//    private final String username;
//    private List<Notification> notifications;
//
//    public NotificationController(String username) {
//        this.username = username;
//        loadNotifications();
//    }
//
//    public boolean hasNotifications() {
//        return notifications != null && !notifications.isEmpty();
//    }
//
//    public void loadTableData(DefaultTableModel model) {
//        model.setRowCount(0);
//        boolean hasData = false;
//        for (Notification n : notifications) {
//            String msg = formatNotificationMessage(n);
//            model.addRow(new Object[]{msg});
//            hasData = true;
//        }
//        if (!hasData) {
//            model.addRow(new Object[]{ERROR_MESSAGE});
//        }
//    }
//
//    public void showNotificationDialog(int row, DefaultTableModel model) {
//        if (row < 0 || notifications == null || row >= notifications.size()) {
//            return;
//        }
//        Notification notification = notifications.get(row);
//        String message = (String) model.getValueAt(row, 0);
//
//        JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
//        dialog.setSize(400, 200);
//        dialog.setLocationRelativeTo(null);
//        dialog.setLayout(new BorderLayout(10, 10));
//
//        JPanel content = new JPanel(new BorderLayout());
//        content.setBorder(new EmptyBorder(15, 20, 10, 20));
//        JTextArea area = new JTextArea(message);
//        area.setLineWrap(true);
//        area.setWrapStyleWord(true);
//        area.setEditable(false);
//        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        area.setBackground(dialog.getBackground());
//
//        content.add(area, BorderLayout.CENTER);
//
//        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton btnClose = new JButton("Đóng");
//        styleDialogButton(btnClose, new Color(100, 100, 100));
//
//        btnClose.addActionListener(e -> handleCloseDialog(row, model, notification, dialog));
//
//        if ("Yêu cầu duyệt".equalsIgnoreCase(notification.getContent())) {
//            RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
//            if (requestXML != null) {
//                Request request = requestXML.getRequests().stream()
//                        .filter(r -> r.getRequestId().equals(notification.getRequestId()))
//                        .findFirst()
//                        .orElse(null);
//                if (request != null && ("Gửi yêu cầu".equalsIgnoreCase(request.getStatus()) || "Đã đọc".equalsIgnoreCase(request.getStatus()))) {
//                    JButton btnCancel = new JButton("Hủy");
//                    styleDialogButton(btnCancel, new Color(255, 100, 100));
//                    btnCancel.addActionListener(e -> handleCancelRequest(row, model, notification, dialog));
//                    buttons.add(btnCancel);
//                }
//            }
//        }
//
//        if ("Đã được duyệt".equalsIgnoreCase(notification.getContent())) {
//            Booking booking = BookingService.findBookingById(notification.getBookingId());
//            if (booking != null && booking.getCheckIn() != null) {
//                long hoursUntilCheckIn = ChronoUnit.HOURS.between(notification.getTime(), booking.getCheckIn());
//                if (hoursUntilCheckIn >= 5) {
//                    RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
//                    if (requestXML != null) {
//                        Request request = requestXML.getRequests().stream()
//                                .filter(r -> r.getRequestId().equals(notification.getRequestId()))
//                                .findFirst()
//                                .orElse(null);
//                        boolean hasCancellationRequest = notifications.stream()
//                                .anyMatch(n -> n.getRequestId().equals(notification.getRequestId()) &&
//                                        "Gửi yêu cầu hủy".equalsIgnoreCase(n.getContent()));
//                        if (!hasCancellationRequest) {
//                            JButton btnRequestCancel = new JButton("Yêu cầu hủy");
//                            styleDialogButton(btnRequestCancel, new Color(255, 165, 0));
//                            btnRequestCancel.addActionListener(e -> handleRequestCancel(row, model, notification, dialog));
//                            buttons.add(btnRequestCancel);
//                        }
//                    }
//                }
//            }
//        }
//
//        buttons.add(btnClose);
//
//        dialog.add(content, BorderLayout.CENTER);
//        dialog.add(buttons, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//
//    private void loadNotifications() {
//        NotificationXML notificationXML = FileUtils.readFromFile(XML_PATH, NotificationXML.class);
//        if (notificationXML == null || notificationXML.getNotifications() == null) {
//            notifications = new ArrayList<>();
//        } else {
//            notifications = notificationXML.getNotifications().stream()
//                    .filter(n -> username.equalsIgnoreCase(n.getUserName()) &&
//                            ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
//                    .collect(Collectors.toList());
//        }
//    }
//
//    private String formatNotificationMessage(Notification n) {
//        String msg;
//        if ("Đã được duyệt".equalsIgnoreCase(n.getContent())) {
//            msg = String.format(" %s đã được đặt lúc %s",
//                    n.getBookingId(), n.getTime().format(TIME_FORMATTER));
//        } else if ("Đã bị hủy".equalsIgnoreCase(n.getContent()) && !n.getBookingId().equals("BK00000001")) {
//            msg = String.format(" %s đã được hủy lúc %s",
//                    n.getBookingId(), n.getTime().format(TIME_FORMATTER));
//        } else if ("Không được hủy".equalsIgnoreCase(n.getContent())) {
//            msg = String.format(" %s không được hủy lúc %s",
//                    n.getBookingId(), n.getTime().format(TIME_FORMATTER));
//        } else {
//            String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
//            msg = String.format(" %s %s lúc %s",
//                    id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
//        }
//        if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
//            msg += " [Đã đọc]";
//        }
//        return msg;
//    }
//
//    private void handleCloseDialog(int row, DefaultTableModel model, Notification notification, JDialog dialog) {
//        String current = (String) model.getValueAt(row, 0);
//        if (!current.contains("[Đã đọc]")) {
//            model.setValueAt(current + " [Đã đọc]", row, 0);
//            NotificationService.updateStatus(
//                    notification.getBookingId(),
//                    notification.getRequestId(),
//                    notification.getUserName(),
//                    notification.getContent(),
//                    "Đã đọc"
//            );
//            loadNotifications();
//            loadTableData(model);
//        }
//        dialog.dispose();
//    }
//
//    private void handleCancelRequest(int row, DefaultTableModel model, Notification notification, JDialog dialog) {
//        RequestService.updateStatus(notification.getRequestId(), "Đã từ chối");
//        NotificationService.createNotification(
//                notification.getBookingId(),
//                notification.getRequestId(),
//                notification.getUserName(),
//                "Đã bị hủy",
//                "Đã gửi"
//        );
//        JOptionPane.showMessageDialog(dialog, "Yêu cầu đã bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        dialog.dispose();
//        loadNotifications();
//        loadTableData(model);
//    }
//
//    private void handleRequestCancel(int row, DefaultTableModel model, Notification notification, JDialog dialog) {
//        RequestService.updateStatus(notification.getRequestId(), "Gửi yêu cầu hủy");
//        NotificationService.createNotification(
//                notification.getBookingId(),
//                notification.getRequestId(),
//                notification.getUserName(),
//                "Gửi yêu cầu hủy",
//                "Đã gửi"
//        );
//        JOptionPane.showMessageDialog(dialog, "Yêu cầu hủy đã được gửi.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//        dialog.dispose();
//        loadNotifications();
//        loadTableData(model);
//    }
//
//    private void styleDialogButton(JButton btn, Color bgColor) {
//        btn.setForeground(Color.WHITE);
//        btn.setFocusPainted(false);
//        btn.setBackground(bgColor);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(110, 36));
//    }
//}
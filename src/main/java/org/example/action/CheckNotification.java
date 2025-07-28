package org.example.action;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.example.entity.Booking;
import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.utils.FileUtils;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.service.RequestService;
import org.example.controller.NotificationController;

public class CheckNotification {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String ERROR_MESSAGE = "Không có thông báo";
    private static final String REQUESTS_XML_PATH = "requests.xml";

    public static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
        model.setRowCount(0);
        boolean hasData = false;
        for (Notification n : notifications) {
            String msg;
            if ("Đã được duyệt".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s đã được đặt lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Đã bị hủy".equalsIgnoreCase(n.getContent()) && !n.getBookingId().equals("BK00000001")) {
                msg = String.format(" %s đã bị hủy lúc %s",
                        n.getRequestId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Không được hủy".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s không được hủy lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Đã được hủy".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s đã được hủy lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Check-out".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s đã check-out lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Vắng mặt".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s đã bị hủy do vắng mặt lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else if ("Đã thanh toán".equalsIgnoreCase(n.getContent())) {
                msg = String.format(" %s đã được thanh toán lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else {
                String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
                msg = String.format(" %s %s lúc %s",
                        id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
            }
            if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
                msg += " [Đã đọc]";
            }
            model.addRow(new Object[]{msg});
            hasData = true;
        }
        if (!hasData) {
            model.addRow(new Object[]{ERROR_MESSAGE});
        }
    }

    public static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
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

    public static class ButtonLikeEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private int selectedRow;
        private final DefaultTableModel model;
        private final List<Notification> notifications;
        private final NotificationController controller;

        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Notification> notifications, NotificationController controller) {
            super(checkBox);
            this.model = model;
            this.notifications = notifications;
            this.controller = controller;
            button = new JButton();

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
            if (row < 0 || notifications == null || row >= notifications.size()) {
                return;
            }
            Notification notification = notifications.get(row);
            String message = (String) model.getValueAt(row, 0);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
            dialog.setSize(400, 200);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout(10, 10));

            JPanel content = new JPanel(new BorderLayout());
            content.setBorder(new EmptyBorder(15, 20, 10, 20));
            JTextArea area = new JTextArea(message);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            area.setBackground(dialog.getBackground());

            content.add(area, BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnClose = new JButton("Đóng");

            btnClose.addActionListener(e -> {
                String current = (String) model.getValueAt(row, 0);
                if (!current.contains("[Đã đọc]")) {
                    model.setValueAt(current + " [Đã đọc]", row, 0);
                    controller.markNotificationAsRead(notification);
                    controller.reloadNotifications(notifications, notification.getUserName());
                    loadTableData(notifications, model);
                }
                dialog.dispose();
            });

            if ("Yêu cầu duyệt".equalsIgnoreCase(notification.getContent())) {
                RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
                if (requestXML != null) {
                    Request request = requestXML.getRequests().stream()
                            .filter(r -> r.getRequestId().equals(notification.getRequestId()))
                            .findFirst()
                            .orElse(null);
                    if (request != null && ("Gửi yêu cầu".equalsIgnoreCase(request.getStatus()) || "Đã đọc".equalsIgnoreCase(request.getStatus()))) {
                        JButton btnCancel = new JButton("Hủy");
                        btnCancel.addActionListener(e -> {
                            RequestService.updateStatus(notification.getRequestId(), "Đã từ chối");
                            NotificationService.createNotification(
                                    notification.getBookingId(),
                                    notification.getRequestId(),
                                    notification.getUserName(),
                                    "Đã bị hủy",
                                    "Đã gửi"
                            );
                            JOptionPane.showMessageDialog(dialog, "Yêu cầu đã bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            controller.reloadNotifications(notifications, notification.getUserName());
                            loadTableData(notifications, model);
                        });
                        buttons.add(btnCancel);
                    }
                }
            }

            if ("Đã được duyệt".equalsIgnoreCase(notification.getContent())) {
                Booking booking = BookingService.findBookingById(notification.getBookingId());
                if (booking != null && booking.getCheckIn() != null) {
                    long hoursUntilCheckIn = ChronoUnit.HOURS.between(notification.getTime(), booking.getCheckIn());
                    if (hoursUntilCheckIn >= 2) {
                        RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
                        if (requestXML != null) {
                            Request request = requestXML.getRequests().stream()
                                    .filter(r -> r.getRequestId().equals(notification.getRequestId()))
                                    .findFirst()
                                    .orElse(null);
                            boolean hasCancellationRequest = notifications.stream()
                                    .anyMatch(n -> n.getRequestId().equals(notification.getRequestId()) &&
                                            "Gửi yêu cầu hủy".equalsIgnoreCase(n.getContent()));
                            if (!hasCancellationRequest) {
                                JButton btnRequestCancel = new JButton("Yêu cầu hủy");
                                btnRequestCancel.addActionListener(e -> {
                                    RequestService.updateStatus(notification.getRequestId(), "Gửi yêu cầu hủy");
                                    NotificationService.createNotification(
                                            notification.getBookingId(),
                                            notification.getRequestId(),
                                            notification.getUserName(),
                                            "Gửi yêu cầu hủy",
                                            "Đã gửi"
                                    );
                                    JOptionPane.showMessageDialog(dialog, "Yêu cầu hủy đã được gửi.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                    dialog.dispose();
                                    controller.reloadNotifications(notifications, notification.getUserName());
                                    loadTableData(notifications, model);
                                });
                                buttons.add(btnRequestCancel);
                            }
                        }
                    }
                }
            }

            buttons.add(btnClose);

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
        button.setMargin(new Insets(5, 15, 5, 15));
        button.setToolTipText("Nhấn để xem chi tiết thông báo");

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
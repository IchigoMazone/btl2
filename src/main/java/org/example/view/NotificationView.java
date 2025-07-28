
package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.example.entity.Booking;
import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.utils.FileUtils;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.service.RequestService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationView {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String XML_PATH = "notifications.xml";
    private static final String REQUESTS_XML_PATH = "requests.xml";
    private static final String[] TABLE_COLUMNS = {"Thông báo cá nhân"};
    private static final String NOTIFICATION_TITLE = "Thông báo của bạn";
    private static final String ERROR_MESSAGE = "Không có thông báo";

    public static JPanel createUserNotificationPanel(String username) {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(0, 0, 0, 0));

        NotificationXML notificationXML = FileUtils.readFromFile(XML_PATH, NotificationXML.class);
        if (notificationXML == null || notificationXML.getNotifications() == null || notificationXML.getNotifications().isEmpty()) {
            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
            return container;
        }

        List<Notification> filtered = new ArrayList<>(notificationXML.getNotifications().stream()
                .filter(n -> username.equalsIgnoreCase(n.getUserName()) &&
                        ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                .collect(Collectors.toList()));

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
        table.setToolTipText("Danh sách thông báo cá nhân");

        loadTableData(filtered, model);

        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(
                new ButtonLikeEditor(new JCheckBox(), model, filtered, XML_PATH)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
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
        private final List<Notification> notifications;
        private final String xmlPath;

        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Notification> notifications, String xmlPath) {
            super(checkBox);
            this.model = model;
            this.notifications = notifications;
            this.xmlPath = xmlPath;
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
                    NotificationService.updateStatus(
                            notification.getBookingId(),
                            notification.getRequestId(),
                            notification.getUserName(),
                            notification.getContent(),
                            "Đã đọc"
                    );
                    NotificationXML updatedXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
                    if (updatedXML != null) {
                        notifications.clear();
                        notifications.addAll(updatedXML.getNotifications().stream()
                                .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()) &&
                                        ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                                .collect(Collectors.toList()));
                        loadTableData(notifications, model);
                    }
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
                            NotificationXML updatedXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
                            if (updatedXML != null) {
                                notifications.clear();
                                notifications.addAll(updatedXML.getNotifications().stream()
                                        .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()) &&
                                                ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                                        .collect(Collectors.toList()));
                                loadTableData(notifications, model);
                            }
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
                                    NotificationXML updatedXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
                                    if (updatedXML != null) {
                                        notifications.clear();
                                        notifications.addAll(updatedXML.getNotifications().stream()
                                                .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()) &&
                                                        ("Đã gửi".equalsIgnoreCase(n.getStatus()) || "Đã đọc".equalsIgnoreCase(n.getStatus())))
                                                .collect(Collectors.toList()));
                                        loadTableData(notifications, model);
                                    }
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

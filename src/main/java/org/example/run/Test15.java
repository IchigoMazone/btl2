//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.entity.Notification;
//import org.example.entity.NotificationXML;
//import org.example.utils.FileUtils;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Test15 {
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
//    private static final String XML_PATH = "notifications.xml";
//    private static final String[] TABLE_COLUMNS = {"Thông báo cá nhân"};
//    private static final String NOTIFICATION_TITLE = "Thông báo của bạn";
//    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu thông báo!";
//
//    public static JPanel createNotificationPanel(String xmlPath, String targetUsername) {
//        JPanel container = new JPanel(new BorderLayout(10, 10));
//        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        container.setBackground(Color.WHITE);
//
//        NotificationXML notificationXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
//        if (notificationXML == null) {
//            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
//            return container;
//        }
//
//        List<Notification> allNotifications = notificationXML.getNotifications();
//        List<Notification> filtered = new ArrayList<>(allNotifications.stream()
//                .filter(n -> targetUsername.equalsIgnoreCase(n.getUserName()))
//                .collect(Collectors.toList()));
//
//        DefaultTableModel model = new DefaultTableModel(TABLE_COLUMNS, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return true;
//            }
//        };
//
//        JTable table = new JTable(model);
//        table.setRowHeight(38);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());
//
//        loadTableData(filtered, model);
//
//        table.getColumnModel().getColumn(0).setCellEditor(
//                new ButtonLikeEditor(new JCheckBox(), model)
//        );
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
//
//        container.add(scrollPane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
//        model.setRowCount(0);
//        for (Notification n : notifications) {
//            String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
//            String msg = String.format("%s %s lúc %s",
//                    id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
//            if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
//                msg += " [Đã đọc]";
//            }
//            model.addRow(new Object[]{msg});
//        }
//    }
//
//    public static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
//        public ButtonLikeRenderer() {
//            styleButton(this);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                                                       boolean isSelected, boolean hasFocus,
//                                                       int row, int column) {
//            setText(value == null ? "" : value.toString());
//            if (value != null && value.toString().contains("[Đã đọc]")) {
//                setForeground(Color.GRAY);
//            } else {
//                setForeground(new Color(33, 99, 255));
//            }
//            return this;
//        }
//    }
//
//    public static class ButtonLikeEditor extends DefaultCellEditor {
//        private final JButton button;
//        private int selectedRow;
//        private final DefaultTableModel model;
//
//        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model) {
//            super(checkBox);
//            this.model = model;
//            button = new JButton();
//            styleButton(button);
//
//            button.addActionListener(e -> {
//                fireEditingStopped();
//                showNotificationDialog(selectedRow);
//            });
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            selectedRow = row;
//            button.setText(value == null ? "" : value.toString());
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return button.getText();
//        }
//
//        private void showNotificationDialog(int row) {
//            String message = (String) model.getValueAt(row, 0);
//
//            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
//            dialog.setSize(400, 200);
//            dialog.setLocationRelativeTo(null);
//            dialog.setLayout(new BorderLayout(10, 10));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
//            JTextArea area = new JTextArea(message);
//            area.setLineWrap(true);
//            area.setWrapStyleWord(true);
//            area.setEditable(false);
//            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            area.setBackground(dialog.getBackground());
//
//            content.add(area, BorderLayout.CENTER);
//
//            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            JButton btnClose = new JButton("Đóng");
//            styleDialogButton(btnClose, new Color(100, 100, 100));
//
//            btnClose.addActionListener(e -> {
//                String current = (String) model.getValueAt(row, 0);
//                if (!current.contains("[Đã đọc]")) {
//                    model.setValueAt(current + " [Đã đọc]", row, 0);
//                }
//                dialog.dispose();
//            });
//
//            buttons.add(btnClose);
//
//            dialog.add(content, BorderLayout.CENTER);
//            dialog.add(buttons, BorderLayout.SOUTH);
//            dialog.setVisible(true);
//        }
//    }
//
//    private static void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setContentAreaFilled(false);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        button.setForeground(new Color(33, 99, 255));
//        button.setHorizontalAlignment(SwingConstants.LEFT);
//        button.setMargin(new Insets(5, 10, 5, 10));
//
//        button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent evt) {
//                button.setOpaque(true);
//                button.setBackground(new Color(230, 240, 255));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent evt) {
//                button.setOpaque(false);
//                button.setBackground(null);
//            }
//
//            @Override
//            public void mousePressed(MouseEvent evt) {
//                button.setBackground(new Color(200, 220, 250));
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent evt) {
//                button.setBackground(new Color(230, 240, 255));
//            }
//        });
//    }
//
//    private static void styleDialogButton(JButton btn, Color bgColor) {
//        btn.setForeground(Color.WHITE);
//        btn.setFocusPainted(false);
//        btn.setBackground(bgColor);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(110, 36));
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Apply FlatLightLaf theme
//                UIManager.setLookAndFeel(new FlatLightLaf());
//            } catch (Exception ex) {
//                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
//            }
//
//            JFrame frame = new JFrame("📢 Thông báo của người dùng");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 500);
//            frame.setContentPane(createNotificationPanel(XML_PATH, "husky123"));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}

//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.entity.Notification;
//import org.example.entity.NotificationXML;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.utils.FileUtils;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Test15 {
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
//    private static final String XML_PATH = "notifications.xml";
//    private static final String REQUESTS_XML_PATH = "requests.xml";
//    private static final String[] TABLE_COLUMNS = {"Thông báo cá nhân"};
//    private static final String NOTIFICATION_TITLE = "Thông báo của bạn";
//    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu thông báo!";
//
//    public static JPanel createNotificationPanel(String xmlPath, String targetUsername) {
//        JPanel container = new JPanel(new BorderLayout(10, 10));
//        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        container.setBackground(Color.WHITE);
//
//        NotificationXML notificationXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
//        if (notificationXML == null) {
//            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
//            return container;
//        }
//
//        List<Notification> allNotifications = notificationXML.getNotifications();
//        List<Notification> filtered = new ArrayList<>(allNotifications.stream()
//                .filter(n -> targetUsername.equalsIgnoreCase(n.getUserName()))
//                .collect(Collectors.toList()));
//
//        DefaultTableModel model = new DefaultTableModel(TABLE_COLUMNS, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return true;
//            }
//        };
//
//        JTable table = new JTable(model);
//        table.setRowHeight(38);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());
//
//        loadTableData(filtered, model);
//
//        table.getColumnModel().getColumn(0).setCellEditor(
//                new ButtonLikeEditor(new JCheckBox(), model, filtered, xmlPath)
//        );
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
//
//        container.add(scrollPane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
//        model.setRowCount(0);
//        for (Notification n : notifications) {
//            String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
//            String msg = String.format("%s %s lúc %s",
//                    id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
//            if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
//                msg += " [Đã đọc]";
//            }
//            model.addRow(new Object[]{msg});
//        }
//    }
//
//    public static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
//        public ButtonLikeRenderer() {
//            styleButton(this);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                                                       boolean isSelected, boolean hasFocus,
//                                                       int row, int column) {
//            setText(value == null ? "" : value.toString());
//            if (value != null && value.toString().contains("[Đã đọc]")) {
//                setForeground(Color.GRAY);
//            } else {
//                setForeground(new Color(33, 99, 255));
//            }
//            return this;
//        }
//    }
//
//    public static class ButtonLikeEditor extends DefaultCellEditor {
//        private final JButton button;
//        private int selectedRow;
//        private final DefaultTableModel model;
//        private final List<Notification> notifications;
//        private final String xmlPath;
//
//        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Notification> notifications, String xmlPath) {
//            super(checkBox);
//            this.model = model;
//            this.notifications = notifications;
//            this.xmlPath = xmlPath;
//            button = new JButton();
//            styleButton(button);
//
//            button.addActionListener(e -> {
//                fireEditingStopped();
//                showNotificationDialog(selectedRow);
//            });
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            selectedRow = row;
//            button.setText(value == null ? "" : value.toString());
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return button.getText();
//        }
//
//        private void showNotificationDialog(int row) {
//            if (row < 0 || row >= notifications.size()) {
//                return;
//            }
//            Notification notification = notifications.get(row);
//            String message = (String) model.getValueAt(row, 0);
//
//            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
//            dialog.setSize(400, 200);
//            dialog.setLocationRelativeTo(null);
//            dialog.setLayout(new BorderLayout(10, 10));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
//            JTextArea area = new JTextArea(message);
//            area.setLineWrap(true);
//            area.setWrapStyleWord(true);
//            area.setEditable(false);
//            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            area.setBackground(dialog.getBackground());
//
//            content.add(area, BorderLayout.CENTER);
//
//            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            JButton btnClose = new JButton("Đóng");
//            styleDialogButton(btnClose, new Color(100, 100, 100));
//
//            btnClose.addActionListener(e -> {
//                String current = (String) model.getValueAt(row, 0);
//                if (!current.contains("[Đã đọc]")) {
//                    model.setValueAt(current + " [Đã đọc]", row, 0);
//                }
//                dialog.dispose();
//            });
//
//            // Add "Hủy" button for notifications with content "Yêu cầu duyệt" and request status "Gửi yêu cầu" or "Đã đọc"
//            if ("Yêu cầu duyệt".equalsIgnoreCase(notification.getContent())) {
//                RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
//                if (requestXML != null) {
//                    Request request = requestXML.getRequests().stream()
//                            .filter(r -> r.getRequestId().equals(notification.getRequestId()))
//                            .findFirst()
//                            .orElse(null);
//                    if (request != null && ("Gửi yêu cầu".equalsIgnoreCase(request.getStatus()) || "Đã đọc".equalsIgnoreCase(request.getStatus()))) {
//                        JButton btnCancel = new JButton("Hủy");
//                        styleDialogButton(btnCancel, new Color(255, 100, 100));
//                        btnCancel.addActionListener(e -> {
//                            // Update request status to "Đã từ chối"
//                            RequestService.updateStatus(notification.getRequestId(), "Đã từ chối");
//                            // Create new notification
//                            NotificationService.createNotification(
//                                    notification.getBookingId(),
//                                    notification.getRequestId(),
//                                    notification.getUserName(),
//                                    "Đã bị hủy",
//                                    "Đã gửi"
//                            );
//                            JOptionPane.showMessageDialog(dialog, "Yêu cầu đã bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                            dialog.dispose();
//                            // Refresh table
//                            NotificationXML updatedXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
//                            if (updatedXML != null) {
//                                notifications.clear();
//                                notifications.addAll(updatedXML.getNotifications().stream()
//                                        .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()))
//                                        .collect(Collectors.toList()));
//                                loadTableData(notifications, model);
//                            }
//                        });
//                        buttons.add(btnCancel);
//                    }
//                }
//            }
//
//            buttons.add(btnClose);
//
//            dialog.add(content, BorderLayout.CENTER);
//            dialog.add(buttons, BorderLayout.SOUTH);
//            dialog.setVisible(true);
//        }
//    }
//
//    private static void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setContentAreaFilled(false);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        button.setForeground(new Color(33, 99, 255));
//        button.setHorizontalAlignment(SwingConstants.LEFT);
//        button.setMargin(new Insets(5, 10, 5, 10));
//
//        button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent evt) {
//                button.setOpaque(true);
//                button.setBackground(new Color(230, 240, 255));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent evt) {
//                button.setOpaque(false);
//                button.setBackground(null);
//            }
//
//            @Override
//            public void mousePressed(MouseEvent evt) {
//                button.setBackground(new Color(200, 220, 250));
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent evt) {
//                button.setBackground(new Color(230, 240, 255));
//            }
//        });
//    }
//
//    private static void styleDialogButton(JButton btn, Color bgColor) {
//        btn.setForeground(Color.WHITE);
//        btn.setFocusPainted(false);
//        btn.setBackground(bgColor);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(110, 36));
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Apply FlatLightLaf theme
//                UIManager.setLookAndFeel(new FlatLightLaf());
//            } catch (Exception ex) {
//                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
//            }
//
//            JFrame frame = new JFrame("📢 Thông báo của người dùng");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 500);
//            frame.setContentPane(createNotificationPanel(XML_PATH, "husky123"));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}


//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.entity.Notification;
//import org.example.entity.NotificationXML;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.utils.FileUtils;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Test15 {
//    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
//    private static final String XML_PATH = "notifications.xml";
//    private static final String REQUESTS_XML_PATH = "requests.xml";
//    private static final String[] TABLE_COLUMNS = {"Thông báo cá nhân"};
//    private static final String NOTIFICATION_TITLE = "Thông báo của bạn";
//    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu thông báo!";
//
//    public static JPanel createNotificationPanel(String xmlPath, String targetUsername) {
//        JPanel container = new JPanel(new BorderLayout(10, 10));
//        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        container.setBackground(Color.WHITE);
//
//        NotificationXML notificationXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
//        if (notificationXML == null) {
//            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
//            return container;
//        }
//
//        List<Notification> allNotifications = notificationXML.getNotifications();
//        List<Notification> filtered = new ArrayList<>(allNotifications.stream()
//                .filter(n -> targetUsername.equalsIgnoreCase(n.getUserName()))
//                .collect(Collectors.toList()));
//
//        DefaultTableModel model = new DefaultTableModel(TABLE_COLUMNS, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return true;
//            }
//        };
//
//        JTable table = new JTable(model);
//        table.setRowHeight(38);
//        table.getTableHeader().setReorderingAllowed(false);
//        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());
//
//        loadTableData(filtered, model);
//
//        table.getColumnModel().getColumn(0).setCellEditor(
//                new ButtonLikeEditor(new JCheckBox(), model, filtered, xmlPath)
//        );
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
//
//        container.add(scrollPane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
//        model.setRowCount(0);
//        for (Notification n : notifications) {
//            String msg;
//            if ("Đã được duyệt".equalsIgnoreCase(n.getContent())) {
//                msg = String.format("%s đã được đặt lúc %s",
//                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
//            } else {
//                String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
//                msg = String.format("%s %s lúc %s",
//                        id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
//            }
//            if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
//                msg += " [Đã đọc]";
//            }
//            model.addRow(new Object[]{msg});
//        }
//    }
//
//    public static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
//        public ButtonLikeRenderer() {
//            styleButton(this);
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                                                       boolean isSelected, boolean hasFocus,
//                                                       int row, int column) {
//            setText(value == null ? "" : value.toString());
//            if (value != null && value.toString().contains("[Đã đọc]")) {
//                setForeground(Color.GRAY);
//            } else {
//                setForeground(new Color(33, 99, 255));
//            }
//            return this;
//        }
//    }
//
//    public static class ButtonLikeEditor extends DefaultCellEditor {
//        private final JButton button;
//        private int selectedRow;
//        private final DefaultTableModel model;
//        private final List<Notification> notifications;
//        private final String xmlPath;
//
//        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Notification> notifications, String xmlPath) {
//            super(checkBox);
//            this.model = model;
//            this.notifications = notifications;
//            this.xmlPath = xmlPath;
//            button = new JButton();
//            styleButton(button);
//
//            button.addActionListener(e -> {
//                fireEditingStopped();
//                showNotificationDialog(selectedRow);
//            });
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            selectedRow = row;
//            button.setText(value == null ? "" : value.toString());
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return button.getText();
//        }
//
//        private void showNotificationDialog(int row) {
//            if (row < 0 || row >= notifications.size()) {
//                return;
//            }
//            Notification notification = notifications.get(row);
//            String message = (String) model.getValueAt(row, 0);
//
//            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
//            dialog.setSize(400, 200);
//            dialog.setLocationRelativeTo(null);
//            dialog.setLayout(new BorderLayout(10, 10));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
//            JTextArea area = new JTextArea(message);
//            area.setLineWrap(true);
//            area.setWrapStyleWord(true);
//            area.setEditable(false);
//            area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            area.setBackground(dialog.getBackground());
//
//            content.add(area, BorderLayout.CENTER);
//
//            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            JButton btnClose = new JButton("Đóng");
//            styleDialogButton(btnClose, new Color(100, 100, 100));
//
//            btnClose.addActionListener(e -> {
//                String current = (String) model.getValueAt(row, 0);
//                if (!current.contains("[Đã đọc]")) {
//                    model.setValueAt(current + " [Đã đọc]", row, 0);
//                }
//                dialog.dispose();
//            });
//
//            // Add "Hủy" button for notifications with content "Yêu cầu duyệt" and request status "Gửi yêu cầu" or "Đã đọc"
//            if ("Yêu cầu duyệt".equalsIgnoreCase(notification.getContent())) {
//                RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
//                if (requestXML != null) {
//                    Request request = requestXML.getRequests().stream()
//                            .filter(r -> r.getRequestId().equals(notification.getRequestId()))
//                            .findFirst()
//                            .orElse(null);
//                    if (request != null && ("Gửi yêu cầu".equalsIgnoreCase(request.getStatus()) || "Đã đọc".equalsIgnoreCase(request.getStatus()))) {
//                        JButton btnCancel = new JButton("Hủy");
//                        styleDialogButton(btnCancel, new Color(255, 100, 100));
//                        btnCancel.addActionListener(e -> {
//                            // Update request status to "Đã từ chối"
//                            RequestService.updateStatus(notification.getRequestId(), "Đã từ chối");
//                            // Create new notification
//                            NotificationService.createNotification(
//                                    notification.getBookingId(),
//                                    notification.getRequestId(),
//                                    notification.getUserName(),
//                                    "Đã bị hủy",
//                                    "Đã gửi"
//                            );
//                            JOptionPane.showMessageDialog(dialog, "Yêu cầu đã bị hủy.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                            dialog.dispose();
//                            // Refresh table
//                            NotificationXML updatedXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
//                            if (updatedXML != null) {
//                                notifications.clear();
//                                notifications.addAll(updatedXML.getNotifications().stream()
//                                        .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()))
//                                        .collect(Collectors.toList()));
//                                loadTableData(notifications, model);
//                            }
//                        });
//                        buttons.add(btnCancel);
//                    }
//                }
//            }
//
//            buttons.add(btnClose);
//
//            dialog.add(content, BorderLayout.CENTER);
//            dialog.add(buttons, BorderLayout.SOUTH);
//            dialog.setVisible(true);
//        }
//    }
//
//    private static void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        button.setContentAreaFilled(false);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        button.setForeground(new Color(33, 99, 255));
//        button.setHorizontalAlignment(SwingConstants.LEFT);
//        button.setMargin(new Insets(5, 10, 5, 10));
//
//        button.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent evt) {
//                button.setOpaque(true);
//                button.setBackground(new Color(230, 240, 255));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent evt) {
//                button.setOpaque(false);
//                button.setBackground(null);
//            }
//
//            @Override
//            public void mousePressed(MouseEvent evt) {
//                button.setBackground(new Color(200, 220, 250));
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent evt) {
//                button.setBackground(new Color(230, 240, 255));
//            }
//        });
//    }
//
//    private static void styleDialogButton(JButton btn, Color bgColor) {
//        btn.setForeground(Color.WHITE);
//        btn.setFocusPainted(false);
//        btn.setBackground(bgColor);
//        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        btn.setPreferredSize(new Dimension(110, 36));
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Apply FlatLightLaf theme
//                UIManager.setLookAndFeel(new FlatLightLaf());
//            } catch (Exception ex) {
//                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
//            }
//
//            JFrame frame = new JFrame("📢 Thông báo của người dùng");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 500);
//            frame.setContentPane(createNotificationPanel(XML_PATH, "husky123"));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}

package org.example.run;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.entity.Booking;
import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.service.RequestService;
import org.example.utils.FileUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test15 {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String XML_PATH = "notifications.xml";
    private static final String REQUESTS_XML_PATH = "requests.xml";
    private static final String[] TABLE_COLUMNS = {"Thông báo cá nhân"};
    private static final String NOTIFICATION_TITLE = "Thông báo của bạn";
    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu thông báo!";

    public static JPanel createNotificationPanel(String xmlPath, String targetUsername) {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.setBackground(Color.WHITE);

        NotificationXML notificationXML = FileUtils.readFromFile(xmlPath, NotificationXML.class);
        if (notificationXML == null) {
            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
            return container;
        }

        List<Notification> allNotifications = notificationXML.getNotifications();
        List<Notification> filtered = new ArrayList<>(allNotifications.stream()
                .filter(n -> targetUsername.equalsIgnoreCase(n.getUserName()))
                .collect(Collectors.toList()));

        DefaultTableModel model = new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultRenderer(Object.class, new ButtonLikeRenderer());

        loadTableData(filtered, model);

        table.getColumnModel().getColumn(0).setCellEditor(
                new ButtonLikeEditor(new JCheckBox(), model, filtered, xmlPath)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private static void loadTableData(List<Notification> notifications, DefaultTableModel model) {
        model.setRowCount(0);
        for (Notification n : notifications) {
            String msg;
            if ("Đã được duyệt".equalsIgnoreCase(n.getContent())) {
                msg = String.format("%s đã được đặt lúc %s",
                        n.getBookingId(), n.getTime().format(TIME_FORMATTER));
            } else {
                String id = n.getBookingId().equals("BK00000001") ? n.getRequestId() : n.getBookingId();
                msg = String.format("%s %s lúc %s",
                        id, n.getContent().toLowerCase(), n.getTime().format(TIME_FORMATTER));
            }
            if ("Đã đọc".equalsIgnoreCase(n.getStatus())) {
                msg += " [Đã đọc]";
            }
            model.addRow(new Object[]{msg});
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
            if (value != null && value.toString().contains("[Đã đọc]")) {
                setForeground(Color.GRAY);
            } else {
                setForeground(new Color(33, 99, 255));
            }
            return this;
        }
    }

    public static class ButtonLikeEditor extends DefaultCellEditor {
        private final JButton button;
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
            styleButton(button);

            button.addActionListener(e -> {
                fireEditingStopped();
                showNotificationDialog(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            button.setText(value == null ? "" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }

        private void showNotificationDialog(int row) {
            if (row < 0 || row >= notifications.size()) {
                return;
            }
            Notification notification = notifications.get(row);
            String message = (String) model.getValueAt(row, 0);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết thông báo", true);
            dialog.setSize(400, 200);
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
            JButton btnClose = new JButton("Đóng");
            styleDialogButton(btnClose, new Color(100, 100, 100));

            btnClose.addActionListener(e -> {
                String current = (String) model.getValueAt(row, 0);
                if (!current.contains("[Đã đọc]")) {
                    model.setValueAt(current + " [Đã đọc]", row, 0);
                }
                dialog.dispose();
            });

            // Add "Hủy" button for "Yêu cầu duyệt" notifications with request status "Gửi yêu cầu" or "Đã đọc"
            if ("Yêu cầu duyệt".equalsIgnoreCase(notification.getContent())) {
                RequestXML requestXML = FileUtils.readFromFile(REQUESTS_XML_PATH, RequestXML.class);
                if (requestXML != null) {
                    Request request = requestXML.getRequests().stream()
                            .filter(r -> r.getRequestId().equals(notification.getRequestId()))
                            .findFirst()
                            .orElse(null);
                    if (request != null && ("Gửi yêu cầu".equalsIgnoreCase(request.getStatus()) || "Đã đọc".equalsIgnoreCase(request.getStatus()))) {
                        JButton btnCancel = new JButton("Hủy");
                        styleDialogButton(btnCancel, new Color(255, 100, 100));
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
                                        .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()))
                                        .collect(Collectors.toList()));
                                loadTableData(notifications, model);
                            }
                        });
                        buttons.add(btnCancel);
                    }
                }
            }

            // Add "Yêu cầu hủy" button for "Đã được duyệt" notifications if notification time is 5 hours before check-in
            if ("Đã được duyệt".equalsIgnoreCase(notification.getContent())) {
                Booking booking = BookingService.getBookingById(notification.getBookingId());
                if (booking != null && booking.getCheckIn() != null) {
                    long hoursUntilCheckIn = ChronoUnit.HOURS.between(notification.getTime(), booking.getCheckIn());
                    if (hoursUntilCheckIn >= 5) {
                        JButton btnRequestCancel = new JButton("Yêu cầu hủy");
                        styleDialogButton(btnRequestCancel, new Color(255, 165, 0));
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
                                        .filter(n -> notification.getUserName().equalsIgnoreCase(n.getUserName()))
                                        .collect(Collectors.toList()));
                                loadTableData(notifications, model);
                            }
                        });
                        buttons.add(btnRequestCancel);
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
        button.setMargin(new Insets(5, 10, 5, 10));

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

    private static void styleDialogButton(JButton btn, Color bgColor) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Apply FlatLightLaf theme
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ex) {
                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
            }

            JFrame frame = new JFrame("📢 Thông báo của người dùng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setContentPane(createNotificationPanel(XML_PATH, "husky123"));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
//
//
//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.entity.Person;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.service.RequestService;
//import org.example.utils.FileUtils;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class R {
//    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
//    private static final DateTimeFormatter DETAILED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    private static final String XML_PATH = "requests.xml";
//    private static final String[] TABLE_COLUMNS = {"Thông báo"};
//    private static final String NOTIFICATION_TITLE = "Thông báo đặt phòng mới";
//    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu yêu cầu!";
//
//    public static JPanel createNotificationPanel(String xmlPath) {
//        JPanel container = new JPanel(new BorderLayout(10, 10));
//        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        container.setBackground(Color.WHITE);
//
//        RequestXML requestXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
//        if (requestXML == null) {
//            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
//            return container;
//        }
//
//        List<Request> allRequests = requestXML.getRequests();
//        List<Request> filtered = new ArrayList<>(allRequests.stream()
//                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) || "Đã đọc".equalsIgnoreCase(r.getStatus()))
//                .toList());
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
//                new ButtonLikeEditor(new JCheckBox(), model, filtered, table, xmlPath)
//        );
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
//
//        container.add(scrollPane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private static void loadTableData(List<Request> requests, DefaultTableModel model) {
//        model.setRowCount(0);
//        for (Request r : requests) {
//            String msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s",
//                    Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                    r.getSubmittedAt().format(DATE_TIME_FORMATTER));
//            if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                msg += " [Đã đọc]";
//            }
//            model.addRow(new Object[]{msg});
//        }
//    }
//
//    static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
//        public ButtonLikeRenderer() {
//            setFocusPainted(false);
//            setCursor(new Cursor(Cursor.HAND_CURSOR));
//            setContentAreaFilled(false);
//            setBorder(BorderFactory.createLineBorder(Color.GRAY));
//            setHorizontalAlignment(SwingConstants.LEFT);
//            setMargin(new Insets(5, 10, 5, 10));
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                       boolean hasFocus, int row, int column) {
//            setText(value == null ? "" : value.toString());
//            return this;
//        }
//    }
//
//    static class ButtonLikeEditor extends DefaultCellEditor {
//        private final JButton button;
//        private final DefaultTableModel model;
//        private final List<Request> requests;
//        private final JTable table;
//        private final String xmlPath;
//        private int selectedRow;
//        private String currentValue;
//
//        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Request> requests, JTable table, String xmlPath) {
//            super(checkBox);
//            this.model = model;
//            this.requests = requests;
//            this.table = table;
//            this.xmlPath = xmlPath;
//
//            button = new JButton();
//            button.setFocusPainted(false);
//            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            button.setContentAreaFilled(false);
//            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//            button.setHorizontalAlignment(SwingConstants.LEFT);
//            button.setMargin(new Insets(5, 10, 5, 10));
//
//            button.addActionListener(e -> {
//                if (selectedRow >= 0 && selectedRow < requests.size()) {
//                    // Stop editing before showing dialog to prevent conflicts
//                    if (table.isEditing()) {
//                        table.getCellEditor().stopCellEditing();
//                    }
//                    Request r = requests.get(selectedRow);
//                    showDetailDialog(r);
//                }
//            });
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            selectedRow = row;
//            currentValue = value == null ? "" : value.toString();
//            button.setText(currentValue);
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return currentValue;
//        }
//
//        private void reloadTable() {
//            RequestXML updatedXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
//            if (updatedXML == null) {
//                JOptionPane.showMessageDialog(null, ERROR_MESSAGE, "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            List<Request> updated = new ArrayList<>(updatedXML.getRequests().stream()
//                    .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) || "Đã đọc".equalsIgnoreCase(r.getStatus()))
//                    .toList());
//            requests.clear();
//            requests.addAll(updated);
//            loadTableData(requests, model);
//        }
//
//        private void showDetailDialog(Request r) {
//            List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
//            Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
//            int soNguoi = people.size();
//
//            StringBuilder danhSach = new StringBuilder();
//            for (Person p : people) {
//                danhSach.append("- ").append(p.getName())
//                        .append(" [").append(p.getLoaiGiayTo()).append(": ").append(p.getMaGiayTo()).append("]\n");
//            }
//
//            String chiTiet = String.format("""
//                    THÔNG TIN ĐẶT PHÒNG
//
//                    Người dùng: %s
//                    Người đại diện: %s
//                    CCCD: %s
//                    Gmail: %s
//                    SĐT: %s
//
//                    Số người: %d
//
//                    Danh sách khách:
//                    %s
//                    Phòng: %s
//                    Giá: %,.0f VND
//
//                    Check-in: %s
//                    Check-out: %s
//                    Tạo yêu cầu: %s
//                    Trạng thái: %s
//                    """,
//                    Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                    daiDien.getName(), daiDien.getMaGiayTo(),
//                    Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
//                    Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
//                    soNguoi, danhSach,
//                    Objects.requireNonNullElse(r.getRoomId(), "Không rõ"), r.getAmount(),
//                    r.getCheckIn().format(DETAILED_DATE_FORMATTER),
//                    r.getCheckOut().format(DETAILED_DATE_FORMATTER),
//                    r.getSubmittedAt().format(DETAILED_DATE_FORMATTER),
//                    r.getStatus()
//            );
//
//            JTextArea textArea = new JTextArea(chiTiet);
//            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setMargin(new Insets(10, 10, 10, 10));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.add(new JScrollPane(textArea), BorderLayout.CENTER);
//
//            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            JButton back = new JButton("Quay lại");
//            JButton cancel = new JButton("Hủy");
//            JButton confirm = new JButton("Xác nhận");
//
//            back.addActionListener(e -> {
//                if (!"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    RequestService.updateStatus(r.getRequestId(), "Đã đọc");
//                    r.setStatus("Đã đọc");
//                    // Update the current row's display text if the row still exists
//                    if (selectedRow < model.getRowCount()) {
//                        String msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s [Đã đọc]",
//                                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                                r.getSubmittedAt().format(DATE_TIME_FORMATTER));
//                        model.setValueAt(msg, selectedRow, 0);
//                    }
//                }
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            cancel.addActionListener(e -> {
//                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) && !"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể hủy.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                RequestService.updateStatus(r.getRequestId(), "Đã từ chối");
//                JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            confirm.addActionListener(e -> {
//                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) && !"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể xác nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                RequestService.updateStatus(r.getRequestId(), "Đã duyệt");
//                JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            buttons.add(back);
//            buttons.add(cancel);
//            buttons.add(confirm);
//            content.add(buttons, BorderLayout.SOUTH);
//
//            JDialog dialog = new JDialog((Frame) null, "Chi tiết yêu cầu", true);
//            dialog.setContentPane(content);
//            dialog.setSize(550, 460);
//            dialog.setLocationRelativeTo(null);
//            dialog.setVisible(true);
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(new FlatLightLaf());
//            } catch (Exception ex) {
//                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
//            }
//
//            JFrame frame = new JFrame("📢 Yêu cầu đặt phòng");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 500);
//            frame.setContentPane(createNotificationPanel(XML_PATH));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}


//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.entity.Person;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.service.RequestService;
//import org.example.service.BookingService;
//import org.example.utils.FileUtils;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableCellRenderer;
//import java.awt.*;
//import java.time.format.DateTimeFormatter;
//import org.example.service.NotificationService;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class R {
//    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
//    private static final DateTimeFormatter DETAILED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//    private static final String XML_PATH = "requests.xml";
//    private static final String BOOKINGS_XML_PATH = "bookings.xml";
//    private static final String[] TABLE_COLUMNS = {"Thông báo"};
//    private static final String NOTIFICATION_TITLE = "Thông báo đặt phòng mới";
//    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu yêu cầu!";
//
//    public static JPanel createNotificationPanel(String xmlPath) {
//        JPanel container = new JPanel(new BorderLayout(10, 10));
//        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        container.setBackground(Color.WHITE);
//
//        RequestXML requestXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
//        if (requestXML == null) {
//            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
//            return container;
//        }
//
//        List<Request> allRequests = requestXML.getRequests();
//        List<Request> filtered = new ArrayList<>(allRequests.stream()
//                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) || "Đã đọc".equalsIgnoreCase(r.getStatus()))
//                .toList());
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
//                new ButtonLikeEditor(new JCheckBox(), model, filtered, table, xmlPath)
//        );
//
//        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));
//
//        container.add(scrollPane, BorderLayout.CENTER);
//        return container;
//    }
//
//    private static void loadTableData(List<Request> requests, DefaultTableModel model) {
//        model.setRowCount(0);
//        for (Request r : requests) {
//            String msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s",
//                    Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                    r.getSubmittedAt().format(DATE_TIME_FORMATTER));
//            if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                msg += " [Đã đọc]";
//            }
//            model.addRow(new Object[]{msg});
//        }
//    }
//
//    static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
//        public ButtonLikeRenderer() {
//            setFocusPainted(false);
//            setCursor(new Cursor(Cursor.HAND_CURSOR));
//            setContentAreaFilled(false);
//            setBorder(BorderFactory.createLineBorder(Color.GRAY));
//            setHorizontalAlignment(SwingConstants.LEFT);
//            setMargin(new Insets(5, 10, 5, 10));
//        }
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                       boolean hasFocus, int row, int column) {
//            setText(value == null ? "" : value.toString());
//            return this;
//        }
//    }
//
//    static class ButtonLikeEditor extends DefaultCellEditor {
//        private final JButton button;
//        private final DefaultTableModel model;
//        private final List<Request> requests;
//        private final JTable table;
//        private final String xmlPath;
//        private int selectedRow;
//        private String currentValue;
//
//        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Request> requests, JTable table, String xmlPath) {
//            super(checkBox);
//            this.model = model;
//            this.requests = requests;
//            this.table = table;
//            this.xmlPath = xmlPath;
//
//            button = new JButton();
//            button.setFocusPainted(false);
//            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            button.setContentAreaFilled(false);
//            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//            button.setHorizontalAlignment(SwingConstants.LEFT);
//            button.setMargin(new Insets(5, 10, 5, 10));
//
//            button.addActionListener(e -> {
//                if (selectedRow >= 0 && selectedRow < requests.size()) {
//                    // Stop editing before showing dialog to prevent conflicts
//                    if (table.isEditing()) {
//                        table.getCellEditor().stopCellEditing();
//                    }
//                    Request r = requests.get(selectedRow);
//                    showDetailDialog(r);
//                }
//            });
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            selectedRow = row;
//            currentValue = value == null ? "" : value.toString();
//            button.setText(currentValue);
//            return button;
//        }
//
//        @Override
//        public Object getCellEditorValue() {
//            return currentValue;
//        }
//
//        private void reloadTable() {
//            RequestXML updatedXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
//            if (updatedXML == null) {
//                JOptionPane.showMessageDialog(null, ERROR_MESSAGE, "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//            List<Request> updated = new ArrayList<>(updatedXML.getRequests().stream()
//                    .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) || "Đã đọc".equalsIgnoreCase(r.getStatus()))
//                    .toList());
//            requests.clear();
//            requests.addAll(updated);
//            loadTableData(requests, model);
//        }
//
//        private void showDetailDialog(Request r) {
//            List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
//            Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
//            int soNguoi = people.size();
//
//            StringBuilder danhSach = new StringBuilder();
//            for (Person p : people) {
//                danhSach.append("- ").append(p.getName())
//                        .append(" [").append(p.getLoaiGiayTo()).append(": ").append(p.getMaGiayTo()).append("]\n");
//            }
//
//            String chiTiet = String.format("""
//                    THÔNG TIN ĐẶT PHÒNG
//
//                    Người dùng: %s
//                    Người đại diện: %s
//                    CCCD: %s
//                    Gmail: %s
//                    SĐT: %s
//
//                    Số người: %d
//
//                    Danh sách khách:
//                    %s
//                    Phòng: %s
//                    Giá: %,.0f VND
//
//                    Check-in: %s
//                    Check-out: %s
//                    Tạo yêu cầu: %s
//                    Trạng thái: %s
//                    """,
//                    Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                    daiDien.getName(), daiDien.getMaGiayTo(),
//                    Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
//                    Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
//                    soNguoi, danhSach,
//                    Objects.requireNonNullElse(r.getRoomId(), "Không rõ"), r.getAmount(),
//                    r.getCheckIn().format(DETAILED_DATE_FORMATTER),
//                    r.getCheckOut().format(DETAILED_DATE_FORMATTER),
//                    r.getSubmittedAt().format(DETAILED_DATE_FORMATTER),
//                    r.getStatus()
//            );
//
//            JTextArea textArea = new JTextArea(chiTiet);
//            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//            textArea.setEditable(false);
//            textArea.setMargin(new Insets(10, 10, 10, 10));
//
//            JPanel content = new JPanel(new BorderLayout());
//            content.add(new JScrollPane(textArea), BorderLayout.CENTER);
//
//            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            JButton back = new JButton("Quay lại");
//            JButton cancel = new JButton("Hủy");
//            JButton confirm = new JButton("Xác nhận");
//
//            back.addActionListener(e -> {
//                if (!"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    RequestService.updateStatus(r.getRequestId(), "Đã đọc");
//                    r.setStatus("Đã đọc");
//                    // Update the current row's display text if the row still exists
//                    if (selectedRow < model.getRowCount()) {
//                        String msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s [Đã đọc]",
//                                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                                r.getSubmittedAt().format(DATE_TIME_FORMATTER));
//                        model.setValueAt(msg, selectedRow, 0);
//                    }
//                }
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            cancel.addActionListener(e -> {
//                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) && !"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể hủy.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                RequestService.updateStatus(r.getRequestId(), "Đã từ chối");
//                NotificationService.createNotification(
//                        "BK00000001", r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                      "Đã bị hủy", "Đã gửi"
//                );
//                JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            confirm.addActionListener(e -> {
//                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) && !"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể xác nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                // Update request status to "Đã duyệt"
//                RequestService.updateStatus(r.getRequestId(), "Đã duyệt");
//                // Create booking
//                try {
//                    BookingService.createBooking(
//                            BOOKINGS_XML_PATH,
//                            generateBookingId(), // Replace with actual bookingId generation logic
//                            r.getRequestId(),
//                            Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                            daiDien.getName(),
//                            Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
//                            Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
//                            Objects.requireNonNullElse(r.getRoomId(), "Không rõ"),
//                            r.getCheckIn(),
//                            r.getCheckOut(),
//                            r.getAmount(),
//                            r.getPersons() != null ? r.getPersons() : new ArrayList<>() // Pass List<Person> instead of String
//                    );
//                    JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu và tạo booking thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Lỗi khi tạo booking: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//
//                RequestService.updateStatus(r.getRequestId(), "Đã được duyệt");
//                NotificationService.createNotification(
//                        generateBookingId(), r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                        "Đã được duyệt", "Đã gửi"
//                );
//                SwingUtilities.getWindowAncestor(content).dispose();
//                // Defer table reload to avoid conflicts with editor
//                SwingUtilities.invokeLater(this::reloadTable);
//            });
//
//            buttons.add(back);
//            buttons.add(cancel);
//            buttons.add(confirm);
//            content.add(buttons, BorderLayout.SOUTH);
//
//            JDialog dialog = new JDialog((Frame) null, "Chi tiết yêu cầu", true);
//            dialog.setContentPane(content);
//            dialog.setSize(550, 460);
//            dialog.setLocationRelativeTo(null);
//            dialog.setVisible(true);
//        }
//
//        // Placeholder method for generating bookingId (replace with actual logic)
//        private String generateBookingId() {
//            // TODO: Implement actual bookingId generation logic (e.g., UUID, incrementing ID, etc.)
//            return "BOOKING_" + System.currentTimeMillis();
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(new FlatLightLaf());
//            } catch (Exception ex) {
//                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
//            }
//
//            JFrame frame = new JFrame("📢 Yêu cầu đặt phòng");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 500);
//            frame.setContentPane(createNotificationPanel(XML_PATH));
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}


package org.example.run;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.entity.Notification;
import org.example.entity.NotificationXML;
import org.example.entity.Person;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.service.RequestService;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.utils.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class R {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private static final DateTimeFormatter DETAILED_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String XML_PATH = "requests.xml";
    private static final String BOOKINGS_XML_PATH = "bookings.xml";
    private static final String NOTIFICATIONS_XML_PATH = "notifications.xml";
    private static final String[] TABLE_COLUMNS = {"Thông báo"};
    private static final String NOTIFICATION_TITLE = "Thông báo đặt phòng mới";
    private static final String ERROR_MESSAGE = "Không thể đọc dữ liệu yêu cầu!";

    public static JPanel createNotificationPanel(String xmlPath) {
        JPanel container = new JPanel(new BorderLayout(10, 10));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.setBackground(Color.WHITE);

        RequestXML requestXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
        if (requestXML == null) {
            container.add(new JLabel(ERROR_MESSAGE), BorderLayout.CENTER);
            return container;
        }

        List<Request> allRequests = requestXML.getRequests();
        List<Request> filtered = new ArrayList<>(allRequests.stream()
                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) ||
                        "Đã đọc".equalsIgnoreCase(r.getStatus()) ||
                        "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus()))
                .toList());

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
                new ButtonLikeEditor(new JCheckBox(), model, filtered, table, xmlPath)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(NOTIFICATION_TITLE));

        container.add(scrollPane, BorderLayout.CENTER);
        return container;
    }

    private static void loadTableData(List<Request> requests, DefaultTableModel model) {
        model.setRowCount(0);
        for (Request r : requests) {
            String msg;
            if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                msg = String.format("%s gửi yêu cầu hủy phòng lúc %s",
                        Objects.requireNonNullElse(r.getRequestId(), "Không rõ"),
                        r.getSubmittedAt().format(DATE_TIME_FORMATTER));
            } else {
                msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s",
                        Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                        r.getSubmittedAt().format(DATE_TIME_FORMATTER));
                if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
                    msg += " [Đã đọc]";
                }
            }
            model.addRow(new Object[]{msg});
        }
    }

    static class ButtonLikeRenderer extends JButton implements TableCellRenderer {
        public ButtonLikeRenderer() {
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setContentAreaFilled(false);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setHorizontalAlignment(SwingConstants.LEFT);
            setMargin(new Insets(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    static class ButtonLikeEditor extends DefaultCellEditor {
        private final JButton button;
        private final DefaultTableModel model;
        private final List<Request> requests;
        private final JTable table;
        private final String xmlPath;
        private int selectedRow;
        private String currentValue;

        public ButtonLikeEditor(JCheckBox checkBox, DefaultTableModel model, List<Request> requests, JTable table, String xmlPath) {
            super(checkBox);
            this.model = model;
            this.requests = requests;
            this.table = table;
            this.xmlPath = xmlPath;

            button = new JButton();
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setMargin(new Insets(5, 10, 5, 10));

            button.addActionListener(e -> {
                if (selectedRow >= 0 && selectedRow < requests.size()) {
                    if (table.isEditing()) {
                        table.getCellEditor().stopCellEditing();
                    }
                    Request r = requests.get(selectedRow);
                    showDetailDialog(r);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            currentValue = value == null ? "" : value.toString();
            button.setText(currentValue);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }

        private void reloadTable() {
            RequestXML updatedXML = FileUtils.readFromFile(xmlPath, RequestXML.class);
            if (updatedXML == null) {
                JOptionPane.showMessageDialog(null, ERROR_MESSAGE, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Request> updated = new ArrayList<>(updatedXML.getRequests().stream()
                    .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) ||
                            "Đã đọc".equalsIgnoreCase(r.getStatus()) ||
                            "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus()))
                    .toList());
            requests.clear();
            requests.addAll(updated);
            loadTableData(requests, model);
        }

        private String getBookingIdForCancelRequest(String requestId) {
            NotificationXML notificationXML = FileUtils.readFromFile(NOTIFICATIONS_XML_PATH, NotificationXML.class);
            if (notificationXML == null || notificationXML.getNotifications() == null) {
                return null;
            }
            return notificationXML.getNotifications().stream()
                    .filter(n -> "Đã được duyệt".equalsIgnoreCase(n.getContent()) &&
                            n.getRequestId().equals(requestId))
                    .map(Notification::getBookingId)
                    .findFirst()
                    .orElse(null);
        }

        private void showDetailDialog(Request r) {
            List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
            Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
            int soNguoi = people.size();

            StringBuilder danhSach = new StringBuilder();
            for (Person p : people) {
                danhSach.append("- ").append(p.getName())
                        .append(" [").append(p.getLoaiGiayTo()).append(": ").append(p.getMaGiayTo()).append("]\n");
            }

            String chiTiet = String.format("""
                    THÔNG TIN ĐẶT PHÒNG

                    Người dùng: %s
                    Người đại diện: %s
                    CCCD: %s
                    Gmail: %s
                    SĐT: %s

                    Số người: %d

                    Danh sách khách:
                    %s
                    Phòng: %s
                    Giá: %,.0f VND

                    Check-in: %s
                    Check-out: %s
                    Tạo yêu cầu: %s
                    Trạng thái: %s
                    """,
                    Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                    daiDien.getName(), daiDien.getMaGiayTo(),
                    Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
                    Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
                    soNguoi, danhSach,
                    Objects.requireNonNullElse(r.getRoomId(), "Không rõ"), r.getAmount(),
                    r.getCheckIn().format(DETAILED_DATE_FORMATTER),
                    r.getCheckOut().format(DETAILED_DATE_FORMATTER),
                    r.getSubmittedAt().format(DETAILED_DATE_FORMATTER),
                    r.getStatus()
            );

            JTextArea textArea = new JTextArea(chiTiet);
            textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textArea.setEditable(false);
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JPanel content = new JPanel(new BorderLayout());
            content.add(new JScrollPane(textArea), BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton back = new JButton("Quay lại");
            JButton cancel = new JButton("Hủy");
            JButton confirm = new JButton("Xác nhận");

            back.addActionListener(e -> {
                if (!"Đã đọc".equalsIgnoreCase(r.getStatus())) {
                    RequestService.updateStatus(r.getRequestId(), "Đã đọc");
                    r.setStatus("Đã đọc");
                    if (selectedRow < model.getRowCount()) {
                        String msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s [Đã đọc]",
                                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                r.getSubmittedAt().format(DATE_TIME_FORMATTER));
                        model.setValueAt(msg, selectedRow, 0);
                    }
                }
                SwingUtilities.getWindowAncestor(content).dispose();
                SwingUtilities.invokeLater(this::reloadTable);
            });

            cancel.addActionListener(e -> {
                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
                        !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
                        !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể hủy.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String bookingId = "BK00000001";
                if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    bookingId = getBookingIdForCancelRequest(r.getRequestId());
                    if (bookingId == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                RequestService.updateStatus(r.getRequestId(), "Đã bị hủy");
                NotificationService.createNotification(
                        bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                        "Đã bị hủy", "Đã gửi"
                );
                JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(content).dispose();
                SwingUtilities.invokeLater(this::reloadTable);
            });

            confirm.addActionListener(e -> {
                if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
                        !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
                        !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể xác nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                    String bookingId = getBookingIdForCancelRequest(r.getRequestId());
                    if (bookingId == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để xác nhận hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    RequestService.updateStatus(r.getRequestId(), "Đã bị hủy");
                    BookingService.updateBookingStatus(BOOKINGS_XML_PATH, bookingId, "Đã bị hủy");
                    NotificationService.createNotification(
                            bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                            "Đã bị hủy", "Đã gửi"
                    );
                    JOptionPane.showMessageDialog(null, "Bạn đã xác nhận hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String newBookingId = generateBookingId();
                    RequestService.updateStatus(r.getRequestId(), "Đã được duyệt");
                    try {
                        BookingService.createBooking(
                                BOOKINGS_XML_PATH,
                                newBookingId,
                                r.getRequestId(),
                                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                daiDien.getName(),
                                Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
                                Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
                                Objects.requireNonNullElse(r.getRoomId(), "Không rõ"),
                                r.getCheckIn(),
                                r.getCheckOut(),
                                r.getAmount(),
                                r.getPersons() != null ? r.getPersons() : new ArrayList<>()
                        );
                        NotificationService.createNotification(
                                newBookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
                                "Đã được duyệt", "Đã gửi"
                        );
                        JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu và tạo booking thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tạo booking: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
                SwingUtilities.getWindowAncestor(content).dispose();
                SwingUtilities.invokeLater(this::reloadTable);
            });

            buttons.add(back);
            buttons.add(cancel);
            buttons.add(confirm);
            content.add(buttons, BorderLayout.SOUTH);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết yêu cầu", true);
            dialog.setContentPane(content);
            dialog.setSize(550, 460);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }

        private String generateBookingId() {
            return "BOOKING_" + System.currentTimeMillis();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ex) {
                System.err.println("Không thể áp dụng FlatLaf theme: " + ex.getMessage());
            }

            JFrame frame = new JFrame("📢 Yêu cầu đặt phòng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setContentPane(createNotificationPanel(XML_PATH));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
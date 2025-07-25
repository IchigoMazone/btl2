//
//package org.example.view;
//
//import org.example.service.BookingService;
//import org.example.service.PaymentService;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import org.w3c.dom.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.TitledBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableColumn;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.awt.*;
//import java.io.File;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//
//public class BookingView {
//
//    private static DefaultTableModel model;
//    private static JTable table;
//    private static JTextField keywordField;
//    private static JComboBox<String> searchTypeCombo;
//    private static JComboBox<String> statusCombo;
//
//    public static JPanel createBookingPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
//        panel.setBackground(Color.WHITE);
//
//        panel.add(createSearchPanel());
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(createTablePanel());
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(createButtonPanel());
//
//        return panel;
//    }
//
//    private static JPanel createSearchPanel() {
//        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
//        panel.setBorder(new TitledBorder("Tìm kiếm"));
//        panel.setBackground(UIManager.getColor("Panel.background"));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 15, 10);
//        gbc.anchor = GridBagConstraints.WEST;
//
//        searchTypeCombo = new JComboBox<>(new String[]{
//                "Tất cả", "Mã đơn", "Tài khoản", "Họ tên", "SĐT", "Gmail"
//        });
//        keywordField = new JTextField(18);
//
//        statusCombo = new JComboBox<>(new String[]{
//                "Tất cả", "Đã đặt", "Check-in"
//        });
//
//        gbc.gridx = 0; gbc.gridy = 0;
//        panel.add(new JLabel("Tìm theo:"), gbc);
//
//        gbc.gridx = 1;
//        panel.add(searchTypeCombo, gbc);
//
//        gbc.gridx = 2;
//        panel.add(new JLabel("Từ khóa:"), gbc);
//
//        gbc.gridx = 3;
//        panel.add(keywordField, gbc);
//
//        gbc.gridx = 4;
//        panel.add(new JLabel("Trạng thái:"), gbc);
//
//        gbc.gridx = 5;
//        panel.add(statusCombo, gbc);
//
//        return panel;
//    }
//
//    private static JScrollPane createTablePanel() {
//        String[] columns = {
//                "Mã đơn", "Tài khoản", "Họ tên", "SĐT", "Gmail",
//                "Phòng", "Ngày đến", "Ngày đi", "Tổng tiền", "Trạng thái"
//        };
//
//        model = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int col) {
//                return false;
//            }
//        };
//
//        table = new JTable(model);
//        table.setRowHeight(30);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//
//        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
//        center.setHorizontalAlignment(JLabel.CENTER);
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(center);
//        }
//
//        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
//                .setHorizontalAlignment(SwingConstants.CENTER);
//
//        int[] widths = {150, 150, 250, 150, 250, 100, 150, 150, 150, 120};
//        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
//            TableColumn column = table.getColumnModel().getColumn(i);
//            column.setPreferredWidth(widths[i]);
//        }
//
//        loadBookingsFromXML("bookings.xml");
//
//        JScrollPane scrollPane = new JScrollPane(table,
//                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
//                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setPreferredSize(new Dimension(1000, 500));
//        scrollPane.setBorder(new TitledBorder("Danh sách đơn đặt phòng"));
//
//        return scrollPane;
//    }
//
//    private static JPanel createButtonPanel() {
//        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
//
//        JButton searchBtn = new JButton("Tìm kiếm");
//        JButton updateBtn = new JButton("Cập nhật");
//
//        searchBtn.addActionListener(e -> loadBookingsFromXML("bookings.xml"));
//        updateBtn.addActionListener(e -> handleUpdateAction());
//
//        panel.add(searchBtn);
//        panel.add(updateBtn);
//
//        return panel;
//    }
//
//    private static void handleUpdateAction() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Vui lòng chọn một đơn trong bảng!");
//            return;
//        }
//        String status = model.getValueAt(selectedRow, 9).toString();
//
//        if (status.equals("Đã đặt")) {
//            showConfirmDialog(selectedRow);
//        } else if (status.equals("Check-in")) {
//            showPaymentDialog(selectedRow, table);
//        }
//    }
//
//    private static void showConfirmDialog(int row) {
//        JDialog dialog = new JDialog((Frame) null, "Xác nhận nhận phòng", true);
//        dialog.setLayout(new BorderLayout(10, 10));
//        dialog.setSize(600, 400);
//        dialog.setLocationRelativeTo(null);
//
//        String bookingId = table.getValueAt(row, 0).toString();
//        String info = getBookingDetails(row);
//
//        JTextArea infoArea = new JTextArea(info);
//        infoArea.setEditable(false);
//        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
//        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn đặt"));
//        infoArea.setBackground(new Color(248, 248, 248));
//
//        JPanel infoPanel = new JPanel(new BorderLayout());
//        infoPanel.add(infoArea, BorderLayout.CENTER);
//        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        JButton confirmBtn = new JButton("Xác nhận");
//        JButton cancelBtn = new JButton("Quay lại");
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        buttonPanel.add(confirmBtn);
//        buttonPanel.add(cancelBtn);
//
//        confirmBtn.addActionListener(e -> {
//            table.setValueAt("Check-in", row, getColumnIndex("Trạng thái"));
//            System.out.println("Đã xác nhận Check-in cho đơn: " + bookingId);
//            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-in");
//            dialog.dispose();
//        });
//
//        cancelBtn.addActionListener(e -> dialog.dispose());
//
//        dialog.add(infoPanel, BorderLayout.CENTER);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//
//    private static void showPaymentDialog(int row, JTable table) {
//        JDialog dialog = new JDialog((Frame) null, "Thanh toán", true);
//        dialog.setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//
//        JLabel paymentLabel = new JLabel("Phương thức thanh toán:");
//        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ tín dụng"});
//        JTextField codeField = new JTextField(10);
//        codeField.setText("0000000X");
//
//        paymentCombo.addActionListener(e -> {
//            if (paymentCombo.getSelectedItem().equals("Tiền mặt")) {
//                codeField.setText("0000000X");
//                codeField.setEditable(false);
//            } else {
//                codeField.setText("");
//                codeField.setEditable(true);
//            }
//        });
//
//        JButton confirmBtn = new JButton("Xác nhận");
//        JButton cancelBtn = new JButton("Quay lại");
//
//        confirmBtn.addActionListener(e -> {
//            DefaultTableModel model = (DefaultTableModel) table.getModel();
//            String bookingId = model.getValueAt(row, getColumnIndex(table, "Mã đơn")).toString();
//
//            // Cập nhật trạng thái trên bảng
//            int statusCol = getColumnIndex(table, "Trạng thái");
//            if (statusCol != -1) {
//                model.setValueAt("Check-out", row, statusCol);
//            }
//
//            // Ghi trạng thái vào file XML
//            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-out");
//
//            // Lấy thông tin
//            String fullName = model.getValueAt(row, getColumnIndex(table, "Họ tên")).toString();
//            String userName = model.getValueAt(row, getColumnIndex(table, "Tài khoản")).toString();
//            String room = model.getValueAt(row, getColumnIndex(table, "Phòng")).toString();
//            double amount = Double.parseDouble(model.getValueAt(row, getColumnIndex(table, "Tổng tiền")).toString());
//            String method = paymentCombo.getSelectedItem().toString();
//            String code = codeField.getText();
//
//            // Parse thời gian
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"); // ✅ sửa lại formatter
//            LocalDateTime checkIn = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đến")).toString(), formatter);
//            LocalDateTime checkOut = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đi")).toString(), formatter);
//
//            PaymentService.createPayment(
//                    bookingId,
//                    fullName,
//                    userName,
//                    room,
//                    amount,
//                    method,
//                    code,
//                    checkIn,
//                    checkOut
//            );
//
//            dialog.dispose();
//        });
//
//        cancelBtn.addActionListener(e -> dialog.dispose());
//
//        // Add components
//        gbc.gridx = 0; gbc.gridy = 0;
//        dialog.add(paymentLabel, gbc);
//        gbc.gridx = 1;
//        dialog.add(paymentCombo, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 1;
//        dialog.add(new JLabel("Mã thanh toán:"), gbc);
//        gbc.gridx = 1;
//        dialog.add(codeField, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 2;
//        dialog.add(confirmBtn, gbc);
//        gbc.gridx = 1;
//        dialog.add(cancelBtn, gbc);
//
//        // Hiển thị thông tin đơn
//        JTextArea infoArea = new JTextArea(getBookingDetails(row));
//        infoArea.setEditable(false);
//        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn"));
//
//        gbc.gridwidth = 2;
//        gbc.gridx = 0; gbc.gridy = 3;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        dialog.add(infoArea, gbc);
//
//        dialog.pack();
//        dialog.setLocationRelativeTo(null);
//        dialog.setVisible(true);
//    }
//
//
//    // Hàm hỗ trợ tìm chỉ số cột theo tên
//    private static int getColumnIndex(JTable table, String columnName) {
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//
//
//
//
//    private static String getBookingDetails(int row) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Mã đơn       : ").append(model.getValueAt(row, 0)).append("\n");
//        sb.append("Tài khoản    : ").append(model.getValueAt(row, 1)).append("\n");
//        sb.append("Họ tên       : ").append(model.getValueAt(row, 2)).append("\n");
//        sb.append("SĐT          : ").append(model.getValueAt(row, 3)).append("\n");
//        sb.append("Gmail        : ").append(model.getValueAt(row, 4)).append("\n");
//        sb.append("Phòng        : ").append(model.getValueAt(row, 5)).append("\n");
//        sb.append("Ngày đến     : ").append(model.getValueAt(row, 6)).append("\n");
//        sb.append("Ngày đi      : ").append(model.getValueAt(row, 7)).append("\n");
//        sb.append("Tổng tiền    : ").append(model.getValueAt(row, 8)).append("\n");
//        sb.append("Trạng thái   : ").append(model.getValueAt(row, 9)).append("\n");
//        return sb.toString();
//    }
//
//    private static int getColumnIndex(String columnName) {
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private static void loadBookingsFromXML(String fileName) {
//        model.setRowCount(0);
//        try {
//            File xmlFile = new File(fileName);
//            if (!xmlFile.exists()) return;
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(xmlFile);
//            doc.getDocumentElement().normalize();
//
//            NodeList nList = doc.getElementsByTagName("Booking");
//
//            String keyword = keywordField.getText().trim().toLowerCase();
//            String type = (String) searchTypeCombo.getSelectedItem();
//            String statusFilter = (String) statusCombo.getSelectedItem();
//
//            List<Element> elements = new ArrayList<>();
//            for (int i = 0; i < nList.getLength(); i++) {
//                elements.add((Element) nList.item(i));
//            }
//
//            elements.sort(Comparator.comparing(e -> {
//                try {
//                    return new SimpleDateFormat("yyyy-MM-dd").parse(getTagValue(e, "checkIn"));
//                } catch (ParseException ex) {
//                    return new Date(0);
//                }
//            }));
//
//            for (Element e : elements) {
//                String[] rowData = {
//                        getTagValue(e, "bookingId"),
//                        getTagValue(e, "userName"),
//                        getTagValue(e, "fullName"),
//                        getTagValue(e, "phone"),
//                        getTagValue(e, "email"),
//                        getTagValue(e, "roomId"),
//                        getTagValue(e, "checkIn"),
//                        getTagValue(e, "checkOut"),
//                        getTagValue(e, "amount"),
//                        getTagValue(e, "status")
//                };
//
//                boolean matches = keyword.isEmpty();
//                if (!matches) {
//                    switch (type) {
//                        case "Mã đơn": matches = rowData[0].toLowerCase().contains(keyword); break;
//                        case "Tài khoản": matches = rowData[1].toLowerCase().contains(keyword); break;
//                        case "Họ tên": matches = rowData[2].toLowerCase().contains(keyword); break;
//                        case "SĐT": matches = rowData[3].toLowerCase().contains(keyword); break;
//                        case "Gmail": matches = rowData[4].toLowerCase().contains(keyword); break;
//                        case "Tất cả":
//                            matches = Arrays.stream(rowData).anyMatch(s -> s.toLowerCase().contains(keyword));
//                            break;
//                    }
//                }
//
//                // Bỏ qua nếu trạng thái là "Check-out"
//                if (rowData[9].equalsIgnoreCase("Check-out")
//                ||  rowData[9].equalsIgnoreCase("Đã bị hủy")) {
//                    continue;
//                }
//
//                boolean statusMatch = statusFilter.equals("Tất cả") || rowData[9].equalsIgnoreCase(statusFilter);
//
//
//                if (matches && statusMatch) {
//                    model.addRow(rowData);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String getTagValue(Element element, String tagName) {
//        NodeList list = element.getElementsByTagName(tagName);
//        if (list != null && list.getLength() > 0 && list.item(0) != null) {
//            return list.item(0).getTextContent();
//        }
//        return "";
//    }
//}



package org.example.view;

import org.example.controller.BookingController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class BookingView {

    private static DefaultTableModel model;
    private static JTable table;
    private static JTextField keywordField;
    private static JComboBox<String> searchTypeCombo;
    private static JComboBox<String> statusCombo;

    public static JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setBackground(Color.WHITE);

        BookingController controller = new BookingController();

        panel.add(createSearchPanel(controller));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createTablePanel(controller));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButtonPanel(controller));

        return panel;
    }

    private static JPanel createSearchPanel(BookingController controller) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setBorder(new TitledBorder("Tìm kiếm"));
        panel.setBackground(UIManager.getColor("Panel.background"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        searchTypeCombo = new JComboBox<>(new String[]{
                "Tất cả", "Mã đơn", "Tài khoản", "Họ tên", "SĐT", "Gmail"
        });
        keywordField = new JTextField(18);
        statusCombo = new JComboBox<>(new String[]{
                "Tất cả", "Đã đặt", "Check-in"
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tìm theo:"), gbc);

        gbc.gridx = 1;
        panel.add(searchTypeCombo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Từ khóa:"), gbc);

        gbc.gridx = 3;
        panel.add(keywordField, gbc);

        gbc.gridx = 4;
        panel.add(new JLabel("Trạng thái:"), gbc);

        gbc.gridx = 5;
        panel.add(statusCombo, gbc);

        return panel;
    }

    private static JScrollPane createTablePanel(BookingController controller) {
        String[] columns = {
                "Mã đơn", "Tài khoản", "Họ tên", "SĐT", "Gmail",
                "Phòng", "Ngày đến", "Ngày đi", "Tổng tiền", "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        int[] widths = {150, 150, 250, 150, 250, 100, 150, 150, 150, 120};
        for (int i = 0; i < table.getColumnCount(); i++) {
            final int index = i; // Fix for effectively final
            table.getColumnModel().getColumn(index).setCellRenderer(center);
            if (index < widths.length) {
                TableColumn column = table.getColumnModel().getColumn(index);
                column.setPreferredWidth(widths[index]);
            }
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        controller.loadBookings("bookings.xml");

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        scrollPane.setBorder(new TitledBorder("Danh sách đơn đặt phòng"));

        return scrollPane;
    }

    private static JPanel createButtonPanel(BookingController controller) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton searchBtn = new JButton("Tìm kiếm");
        JButton updateBtn = new JButton("Cập nhật");

        searchBtn.addActionListener(e -> controller.loadBookings("bookings.xml"));
        updateBtn.addActionListener(e -> controller.handleUpdate());

        panel.add(searchBtn);
        panel.add(updateBtn);

        return panel;
    }

    // Getters for controller to access UI components
    public static DefaultTableModel getModel() {
        return model;
    }

    public static JTable getTable() {
        return table;
    }

    public static JTextField getKeywordField() {
        return keywordField;
    }

    public static JComboBox<String> getSearchTypeCombo() {
        return searchTypeCombo;
    }

    public static JComboBox<String> getStatusCombo() {
        return statusCombo;
    }
}
package org.example.view;

import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.service.PaymentService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import org.w3c.dom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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

        panel.add(createSearchPanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createTablePanel());
        panel.add(Box.createVerticalStrut(10));
        panel.add(createButtonPanel());

        return panel;
    }

    private static JPanel createSearchPanel() {
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
                "Tất cả", "Đã đặt", "Check-in", "Chờ thanh toán"
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

    private static JScrollPane createTablePanel() {
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
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        int[] widths = {150, 150, 250, 150, 250, 100, 150, 150, 150, 120};
        for (int i = 0; i < widths.length && i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }

        loadBookingsFromXML("bookings.xml", false); // Tải dữ liệu ban đầu, không hiển thị thông báo

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 500));
        scrollPane.setBorder(new TitledBorder("Danh sách đơn đặt phòng"));

        return scrollPane;
    }

    private static JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton searchBtn = new JButton("Tìm kiếm");
        JButton updateBtn = new JButton("Cập nhật");

        searchBtn.addActionListener(e -> loadBookingsFromXML("bookings.xml", true)); // Tìm kiếm với thông báo
        updateBtn.addActionListener(e -> handleUpdateAction());

        panel.add(searchBtn);
        panel.add(updateBtn);

        return panel;
    }

    private static void handleUpdateAction() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một đơn trong bảng!");
            return;
        }
        String status = model.getValueAt(selectedRow, 9).toString();

        if (status.equals("Đã đặt")) {
            showConfirmDialog(selectedRow);
        } else if (status.equals("Check-in")) {
            showPaymentDialog(selectedRow, table);
        } else if (status.equals("Chờ thanh toán")) {
            showChoThanhToan(selectedRow, table);
        }
    }

    private static void showConfirmDialog(int row) {
        JDialog dialog = new JDialog((Frame) null, "Xác nhận nhận phòng", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(null);

        String bookingId = table.getValueAt(row, 0).toString();
        String info = getBookingDetails(row);

        JTextArea infoArea = new JTextArea(info);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn đặt"));
        infoArea.setBackground(new Color(248, 248, 248));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoArea, BorderLayout.CENTER);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton confirmBtn = new JButton("Xác nhận");
        JButton cancelBtn = new JButton("Quay lại");
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        confirmBtn.addActionListener(e -> {
            String checkInStr = table.getValueAt(row, getColumnIndex(table, "Ngày đến")).toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime checkInTime = LocalDateTime.parse(checkInStr, formatter);
            LocalDateTime now = LocalDateTime.now();
            long hoursUntilCheckIn = ChronoUnit.HOURS.between(now, checkInTime);

            if (hoursUntilCheckIn > 1) {
                JOptionPane.showMessageDialog(dialog,
                        "Không thể check-in! Thời gian check-in phải trong vòng 1 giờ từ hiện tại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            table.setValueAt("Check-in", row, getColumnIndex(table, "Trạng thái"));
            System.out.println("Đã xác nhận Check-in cho đơn: " + bookingId);
            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-in");
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private static void showPaymentDialog(int row, JTable table) {
        JDialog dialog = new JDialog((Frame) null, "Thanh toán", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel paymentLabel = new JLabel("Phương thức thanh toán:");
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ tín dụng"});
        JTextField codeField = new JTextField(10);
        codeField.setText("Giao dịch trực tiếp");

        paymentCombo.addActionListener(e -> {
            if (paymentCombo.getSelectedItem().equals("Tiền mặt")) {
                codeField.setText("Giao dịch trực tiếp");
                codeField.setEditable(false);
            } else {
                codeField.setText("");
                codeField.setEditable(true);
            }
        });

        JButton confirmBtn = new JButton("Xác nhận");
        JButton cancelBtn = new JButton("Quay lại");

        confirmBtn.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String bookingId = model.getValueAt(row, getColumnIndex(table, "Mã đơn")).toString();
            String userName = model.getValueAt(row, getColumnIndex(table, "Tài khoản")).toString();
            String method = paymentCombo.getSelectedItem().toString();
            String code = codeField.getText().trim();

            if (method.equals("Thẻ tín dụng")) {
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Mã thanh toán không được để trống khi chọn Thẻ tín dụng",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!code.matches("^[a-zA-Z0-9]{6,20}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã thanh toán phải là mã ngân hàng hợp lệ (chữ và số, 6-20 ký tự)!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int statusCol = getColumnIndex(table, "Trạng thái");
            if (statusCol != -1) {
                model.setValueAt("Check-out", row, statusCol);
            }

            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-out");
            NotificationService.createNotification(
                    bookingId,
                    "Đơn đặt trực tiếp",
                    userName,
                    "Check-out",
                    "Đã gửi"
            );

            String fullName = model.getValueAt(row, getColumnIndex(table, "Họ tên")).toString();
            String room = model.getValueAt(row, getColumnIndex(table, "Phòng")).toString();
            double amount = Double.parseDouble(model.getValueAt(row, getColumnIndex(table, "Tổng tiền")).toString().replace(",", ""));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime checkIn = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đến")).toString(), formatter);
            LocalDateTime checkOut = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đi")).toString(), formatter);

            PaymentService.createPayment(
                    bookingId,
                    fullName,
                    userName,
                    room,
                    amount,
                    method,
                    code,
                    checkIn,
                    checkOut
            );

            NotificationService.createNotification(
                    bookingId,
                    "Đơn đặt trực tiếp",
                    userName,
                    "Đã thanh toán",
                    "Đã gửi"
            );

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(paymentLabel, gbc);
        gbc.gridx = 1;
        dialog.add(paymentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Mã thanh toán:"), gbc);
        gbc.gridx = 1;
        dialog.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(confirmBtn, gbc);
        gbc.gridx = 1;
        dialog.add(cancelBtn, gbc);

        JTextArea infoArea = new JTextArea(getBookingDetails(row));
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn"));

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(infoArea, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static void showChoThanhToan(int row, JTable table) {
        JDialog dialog = new JDialog((Frame) null, "Thanh toán", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel paymentLabel = new JLabel("Phương thức thanh toán:");
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ tín dụng"});
        JTextField codeField = new JTextField(10);
        codeField.setText("Giao dịch trực tiếp");

        paymentCombo.addActionListener(e -> {
            if (paymentCombo.getSelectedItem().equals("Tiền mặt")) {
                codeField.setText("Giao dịch trực tiếp");
                codeField.setEditable(false);
            } else {
                codeField.setText("");
                codeField.setEditable(true);
            }
        });

        JButton confirmBtn = new JButton("Xác nhận");
        JButton cancelBtn = new JButton("Quay lại");

        confirmBtn.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            String bookingId = model.getValueAt(row, getColumnIndex(table, "Mã đơn")).toString();
            String userName = model.getValueAt(row, getColumnIndex(table, "Tài khoản")).toString();
            String method = paymentCombo.getSelectedItem().toString();
            String code = codeField.getText().trim();

            if (method.equals("Thẻ tín dụng")) {
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Mã thanh toán không được để trống khi chọn Thẻ tín dụng",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!code.matches("^[a-zA-Z0-9]{6,20}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã thanh toán phải là mã ngân hàng hợp lệ (chữ và số, 6-20 ký tự)!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int statusCol = getColumnIndex(table, "Trạng thái");
            if (statusCol != -1) {
                model.setValueAt("Check-out", row, statusCol);
            }

            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-out");
            PaymentService.createPayment(
                    bookingId,
                    model.getValueAt(row, getColumnIndex(table, "Họ tên")).toString(),
                    userName,
                    model.getValueAt(row, getColumnIndex(table, "Phòng")).toString(),
                    Double.parseDouble(model.getValueAt(row, getColumnIndex(table, "Tổng tiền")).toString().replace(",", "")),
                    method,
                    code,
                    LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đến")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                    LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đi")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            );

            NotificationService.createNotification(
                    bookingId,
                    "Đơn đặt trực tiếp",
                    userName,
                    "Đã thanh toán",
                    "Đã gửi"
            );

            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(paymentLabel, gbc);
        gbc.gridx = 1;
        dialog.add(paymentCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Mã thanh toán:"), gbc);
        gbc.gridx = 1;
        dialog.add(codeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(confirmBtn, gbc);
        gbc.gridx = 1;
        dialog.add(cancelBtn, gbc);

        JTextArea infoArea = new JTextArea(getBookingDetails(row));
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn"));

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(infoArea, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private static int getColumnIndex(JTable table, String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private static String getBookingDetails(int row) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mã đơn       : ").append(model.getValueAt(row, 0)).append("\n");
        sb.append("Tài khoản    : ").append(model.getValueAt(row, 1)).append("\n");
        sb.append("Họ tên       : ").append(model.getValueAt(row, 2)).append("\n");
        sb.append("SĐT          : ").append(model.getValueAt(row, 3)).append("\n");
        sb.append("Gmail        : ").append(model.getValueAt(row, 4)).append("\n");
        sb.append("Phòng        : ").append(model.getValueAt(row, 5)).append("\n");
        sb.append("Ngày đến     : ").append(model.getValueAt(row, 6)).append("\n");
        sb.append("Ngày đi      : ").append(model.getValueAt(row, 7)).append("\n");
        sb.append("Tổng tiền    : ").append(model.getValueAt(row, 8)).append("\n");
        sb.append("Trạng thái   : ").append(model.getValueAt(row, 9)).append("\n");
        return sb.toString();
    }

    private static int getColumnIndex(String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private static void loadBookingsFromXML(String fileName, boolean showMessages) {
        model.setRowCount(0);
        try {
            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                if (showMessages) {
                    JOptionPane.showMessageDialog(null,
                            "Không thể đọc dữ liệu đơn đặt phòng!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
                return;
            }

            String keyword = keywordField.getText().trim();
            String type = (String) searchTypeCombo.getSelectedItem();
            String statusFilter = (String) statusCombo.getSelectedItem();

            if (showMessages && keyword.isEmpty() && !type.equals("Tất cả")) {
                JOptionPane.showMessageDialog(null,
                        "Vui lòng nhập từ khóa tìm kiếm!",
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (showMessages && !keyword.isEmpty()) {
                if (type.equals("SĐT") && !keyword.matches("0\\d{9}")) {
                    JOptionPane.showMessageDialog(null,
                            "Số điện thoại phải chứa đúng 10 chữ số và bắt đầu bằng 0!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (type.equals("Gmail") && !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Email không đúng định dạng!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (type.equals("Mã đơn") && !keyword.matches("BK[A-Za-z0-9]{2,}")) {
                    JOptionPane.showMessageDialog(null,
                            "Mã đơn phải bắt đầu bằng 'BK' và chứa ít nhất 2 chữ cái hoặc số tiếp theo!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (type.equals("Tài khoản") && !keyword.matches("[a-z0-9]{8,}")) {
                    JOptionPane.showMessageDialog(null,
                            "Tài khoản phải chứa ít nhất 8 chữ cái thường hoặc số!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (type.equals("Họ tên") && keyword.length() < 2) {
                    JOptionPane.showMessageDialog(null,
                            "Họ tên phải chứa ít nhất 2 ký tự!",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (type.equals("Tất cả")) {
                    if (!keyword.matches("0\\d{9}") &&
                            !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") &&
                            !keyword.matches("[A-Za-z0-9]{2,}") &&
                            keyword.length() < 2) {
                        JOptionPane.showMessageDialog(null,
                                "Từ khóa phải là số điện thoại (10 chữ số bắt đầu bằng 0), email hợp lệ, hoặc ít nhất 2 ký tự!",
                                "Lỗi",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            String searchKeyword = keyword.toLowerCase();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Booking");

            List<Element> elements = new ArrayList<>();
            for (int i = 0; i < nList.getLength(); i++) {
                elements.add((Element) nList.item(i));
            }

            elements.sort(Comparator.comparing(e -> {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(getTagValue(e, "checkIn"));
                } catch (ParseException ex) {
                    return new Date(0);
                }
            }));

            boolean hasResults = false;

            for (Element e : elements) {
                String amountStr = getTagValue(e, "amount");
                double amount = amountStr.isEmpty() ? 0.0 : Double.parseDouble(amountStr);
                String[] rowData = {
                        getTagValue(e, "bookingId"),
                        getTagValue(e, "userName"),
                        getTagValue(e, "fullName"),
                        getTagValue(e, "phone"),
                        getTagValue(e, "email"),
                        getTagValue(e, "roomId"),
                        getTagValue(e, "checkIn"),
                        getTagValue(e, "checkOut"),
                        String.format("%,.0f", amount),
                        getTagValue(e, "status")
                };

                // Lọc các trạng thái không mong muốn trong mọi trường hợp
                if (rowData[9].equalsIgnoreCase("Check-out") ||
                        rowData[9].equalsIgnoreCase("Đã bị hủy") ||
                        rowData[9].equalsIgnoreCase("Vắng mặt")) {
                    continue;
                }

                boolean matches = showMessages ? (type.equals("Tất cả") && keyword.isEmpty()) : true;
                if (!matches) {
                    switch (type) {
                        case "Mã đơn":
                            matches = rowData[0].toLowerCase().contains(searchKeyword);
                            break;
                        case "Tài khoản":
                            matches = rowData[1].toLowerCase().contains(searchKeyword);
                            break;
                        case "Họ tên":
                            matches = rowData[2].toLowerCase().contains(searchKeyword);
                            break;
                        case "SĐT":
                            matches = rowData[3].toLowerCase().contains(searchKeyword);
                            break;
                        case "Gmail":
                            matches = rowData[4].toLowerCase().contains(searchKeyword);
                            break;
                        case "Tất cả":
                            matches = Arrays.stream(rowData).anyMatch(s -> s.toLowerCase().contains(searchKeyword));
                            break;
                    }
                }

                boolean statusMatch = statusFilter.equals("Tất cả") || rowData[9].equalsIgnoreCase(statusFilter);

                if (matches && statusMatch) {
                    model.addRow(rowData);
                    hasResults = true;
                }
            }

            if (showMessages && !hasResults) {
                JOptionPane.showMessageDialog(null,
                        "Không tìm thấy đơn đặt phòng phù hợp với từ khóa!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (showMessages) {
                JOptionPane.showMessageDialog(null,
                        "Lỗi khi đọc dữ liệu đơn đặt phòng: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static String getTagValue(Element element, String tagName) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0 && list.item(0) != null) {
            return list.item(0).getTextContent();
        }
        return "";
    }
}

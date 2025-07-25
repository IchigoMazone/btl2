//package org.example.controller;
//
//import org.example.service.BookingService;
//import org.example.service.PaymentService;
//import org.example.view.BookingView;
//import org.w3c.dom.*;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.Date; // Added import for Date
//import java.util.List;
//import java.io.File;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//public class BookingController {
//
//    public void loadBookings(String fileName) {
//        DefaultTableModel model = BookingView.getModel();
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
//            String keyword = BookingView.getKeywordField().getText().trim().toLowerCase();
//            String type = (String) BookingView.getSearchTypeCombo().getSelectedItem();
//            String statusFilter = (String) BookingView.getStatusCombo().getSelectedItem();
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
//                if (rowData[9].equalsIgnoreCase("Check-out") || rowData[9].equalsIgnoreCase("Đã bị hủy")) {
//                    continue;
//                }
//
//                boolean statusMatch = statusFilter.equals("Tất cả") || rowData[9].equalsIgnoreCase(statusFilter);
//
//                if (matches && statusMatch) {
//                    model.addRow(rowData);
//                }
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu từ XML!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    public void handleUpdate() {
//        JTable table = BookingView.getTable();
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Vui lòng chọn một đơn trong bảng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        String status = BookingView.getModel().getValueAt(selectedRow, 9).toString();
//
//        if (status.equals("Đã đặt")) {
//            showConfirmDialog(selectedRow);
//        } else if (status.equals("Check-in")) {
//            showPaymentDialog(selectedRow);
//        }
//    }
//
//    private void showConfirmDialog(int row) {
//        JDialog dialog = new JDialog((Frame) null, "Xác nhận nhận phòng", true);
//        dialog.setLayout(new BorderLayout(10, 10));
//        dialog.setSize(600, 400);
//        dialog.setLocationRelativeTo(null);
//
//        String bookingId = BookingView.getTable().getValueAt(row, 0).toString();
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
//        confirmBtn.addActionListener(e -> {
//            BookingView.getTable().setValueAt("Check-in", row, getColumnIndex("Trạng thái"));
//            BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-in");
//            JOptionPane.showMessageDialog(dialog, "Xác nhận Check-in thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//            dialog.dispose();
//        });
//
//        cancelBtn.addActionListener(e -> dialog.dispose());
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
//        buttonPanel.add(confirmBtn);
//        buttonPanel.add(cancelBtn);
//
//        dialog.add(infoPanel, BorderLayout.CENTER);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//        dialog.setVisible(true);
//    }
//
//    private void showPaymentDialog(int row) {
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
//        paymentCombo.addActionListener(e -> handlePaymentMethodChange(paymentCombo, codeField));
//
//        JButton confirmBtn = new JButton("Xác nhận");
//        JButton cancelBtn = new JButton("Quay lại");
//
//        confirmBtn.addActionListener(e -> handlePaymentConfirmation(row, paymentCombo, codeField, dialog));
//
//        cancelBtn.addActionListener(e -> dialog.dispose());
//
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
//    private void handlePaymentMethodChange(JComboBox<String> paymentCombo, JTextField codeField) {
//        if (paymentCombo.getSelectedItem().equals("Tiền mặt")) {
//            codeField.setText("0000000X");
//            codeField.setEditable(false);
//        } else {
//            codeField.setText("");
//            codeField.setEditable(true);
//        }
//    }
//
//    private void handlePaymentConfirmation(int row, JComboBox<String> paymentCombo, JTextField codeField, JDialog dialog) {
//        DefaultTableModel model = BookingView.getModel();
//        JTable table = BookingView.getTable();
//        String bookingId = model.getValueAt(row, getColumnIndex(table, "Mã đơn")).toString();
//
//        int statusCol = getColumnIndex(table, "Trạng thái");
//        if (statusCol != -1) {
//            model.setValueAt("Check-out", row, statusCol);
//        }
//
//        BookingService.updateBookingStatus("bookings.xml", bookingId, "Check-out");
//
//        String fullName = model.getValueAt(row, getColumnIndex(table, "Họ tên")).toString();
//        String userName = model.getValueAt(row, getColumnIndex(table, "Tài khoản")).toString();
//        String room = model.getValueAt(row, getColumnIndex(table, "Phòng")).toString();
//        double amount;
//        try {
//            amount = Double.parseDouble(model.getValueAt(row, getColumnIndex(table, "Tổng tiền")).toString());
//        } catch (NumberFormatException ex) {
//            JOptionPane.showMessageDialog(dialog, "Lỗi định dạng tổng tiền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//        String method = paymentCombo.getSelectedItem().toString();
//        String code = codeField.getText().trim();
//
//        if (code.isEmpty() && !method.equals("Tiền mặt")) {
//            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập mã thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
//        LocalDateTime checkIn;
//        LocalDateTime checkOut;
//        try {
//            checkIn = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đến")).toString(), formatter);
//            checkOut = LocalDateTime.parse(model.getValueAt(row, getColumnIndex(table, "Ngày đi")).toString(), formatter);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(dialog, "Lỗi định dạng ngày giờ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        PaymentService.createPayment(
//                bookingId, fullName, userName, room, amount, method, code, checkIn, checkOut
//        );
//
//        JOptionPane.showMessageDialog(dialog, "Thanh toán thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//        dialog.dispose();
//    }
//
//    private String getBookingDetails(int row) {
//        DefaultTableModel model = BookingView.getModel();
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
//    private int getColumnIndex(JTable table, String columnName) {
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private int getColumnIndex(String columnName) {
//        JTable table = BookingView.getTable();
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    private String getTagValue(Element element, String tagName) {
//        NodeList list = element.getElementsByTagName(tagName);
//        if (list != null && list.getLength() > 0 && list.item(0) != null) {
//            return list.item(0).getTextContent();
//        }
//        return "";
//    }
//}
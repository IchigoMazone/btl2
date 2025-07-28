package org.example.action;

import org.example.controller.BookingController;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.service.PaymentService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.util.Date; // Added import for Date
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CheckBooking {
    private BookingController controller;
    private static final String XML_FILE = "bookings.xml";

    public CheckBooking(BookingController controller) {
        this.controller = controller;
        handleSearch(); // Initialize table with data
    }

    public void handleSearch() {
        DefaultTableModel model = controller.getView().getModel();
        model.setRowCount(0);
        try {
            File xmlFile = new File(XML_FILE);
            if (!xmlFile.exists()) {
                JOptionPane.showMessageDialog(controller.getPanel(),
                        "Không thể đọc dữ liệu đơn đặt phòng!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String keyword = controller.getView().getKeywordField().getText().trim();
            String type = (String) controller.getView().getSearchTypeCombo().getSelectedItem();
            String statusFilter = (String) controller.getView().getStatusCombo().getSelectedItem();

            String errorMessage = validateInput(keyword, type);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(controller.getPanel(),
                        errorMessage,
                        "Lỗi",
                        JOptionPane.WARNING_MESSAGE);
                return;
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

                if (rowData[9].equalsIgnoreCase("Check-out") ||
                        rowData[9].equalsIgnoreCase("Đã bị hủy") ||
                        rowData[9].equalsIgnoreCase("Vắng mặt")) {
                    continue;
                }

                boolean matches = type.equals("Tất cả") && keyword.isEmpty();
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

            if (!hasResults) {
                String message = String.format(
                        "Không tìm thấy đơn đặt phòng phù hợp với từ khóa '%s' (Tìm theo: %s, Trạng thái: %s).",
                        keyword.isEmpty() ? "Không có từ khóa" : keyword,
                        type,
                        statusFilter
                );
                JOptionPane.showMessageDialog(controller.getPanel(),
                        message,
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(controller.getPanel(),
                    "Lỗi khi đọc dữ liệu đơn đặt phòng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleUpdate() {
        int selectedRow = controller.getView().getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(controller.getPanel(),
                    "Vui lòng chọn một đơn trong bảng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String status = controller.getView().getModel().getValueAt(selectedRow, 9).toString();

        if (status.equals("Đã đặt")) {
            showConfirmDialog(selectedRow);
        } else if (status.equals("Check-in")) {
            showPaymentDialog(selectedRow);
        } else if (status.equals("Chờ thanh toán")) {
            showChoThanhToan(selectedRow);
        }
    }

    private void showConfirmDialog(int row) {
        JDialog dialog = new JDialog((Frame) null, "Xác nhận nhận phòng", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(null);

        String bookingId = controller.getView().getTable().getValueAt(row, 0).toString();
        String info = getBookingDetails(row);

        JTextArea infoArea = new JTextArea(info);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        infoArea.setBorder(BorderFactory.createTitledBorder("Thông tin đơn đặt"));
        infoArea.setBackground(new Color(248, 248, 248));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(infoArea, BorderLayout.CENTER);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton confirmBtn = new JButton("Xác nhận");
        JButton cancelBtn = new JButton("Quay lại");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmBtn);
        buttonPanel.add(cancelBtn);

        confirmBtn.addActionListener(e -> {
            String checkInStr = controller.getView().getTable().getValueAt(row, getColumnIndex("Ngày đến")).toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime checkInTime = LocalDateTime.parse(checkInStr, formatter);
            LocalDateTime now = LocalDateTime.now();
            long hoursUntilCheckIn = ChronoUnit.HOURS.between(now, checkInTime);

            if (hoursUntilCheckIn > 1) {
                JOptionPane.showMessageDialog(dialog,
                        "Không thể check-in! Thời gian check-in phải trong vòng 1 giờ từ hiện tại.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            controller.getView().getTable().setValueAt("Check-in", row, getColumnIndex("Trạng thái"));
            System.out.println("Đã xác nhận Check-in cho đơn: " + bookingId);
            BookingService.updateBookingStatus(XML_FILE, bookingId, "Check-in");
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(infoPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showPaymentDialog(int row) {
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
            DefaultTableModel model = controller.getView().getModel();
            String bookingId = model.getValueAt(row, getColumnIndex("Mã đơn")).toString();
            String userName = model.getValueAt(row, getColumnIndex("Tài khoản")).toString();
            String method = paymentCombo.getSelectedItem().toString();
            String code = codeField.getText().trim();

            if (method.equals("Thẻ tín dụng")) {
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Mã thanh toán không được để trống khi chọn Thẻ tín dụng",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!code.matches("^[a-zA-Z0-9]{6,20}$")) {
                    JOptionPane.showMessageDialog(dialog,
                            "Mã thanh toán phải là mã ngân hàng hợp lệ (chữ và số, 6-20 ký tự)!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int statusCol = getColumnIndex("Trạng thái");
            if (statusCol != -1) {
                model.setValueAt("Check-out", row, statusCol);
            }

            BookingService.updateBookingStatus(XML_FILE, bookingId, "Check-out");
            NotificationService.createNotification(
                    bookingId,
                    "",
                    userName,
                    "Check-out",
                    "Đã gửi"
            );

            String fullName = model.getValueAt(row, getColumnIndex("Họ tên")).toString();
            String room = model.getValueAt(row, getColumnIndex("Phòng")).toString();
            double amount = Double.parseDouble(model.getValueAt(row, getColumnIndex("Tổng tiền")).toString().replace(",", ""));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime checkIn = LocalDateTime.parse(model.getValueAt(row, getColumnIndex("Ngày đến")).toString(), formatter);
            LocalDateTime checkOut = LocalDateTime.parse(model.getValueAt(row, getColumnIndex("Ngày đi")).toString(), formatter);

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
                    "",
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

    private void showChoThanhToan(int row) {
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
            DefaultTableModel model = controller.getView().getModel();
            String bookingId = model.getValueAt(row, getColumnIndex("Mã đơn")).toString();
            String userName = model.getValueAt(row, getColumnIndex("Tài khoản")).toString();
            String method = paymentCombo.getSelectedItem().toString();
            String code = codeField.getText().trim();

            if (method.equals("Thẻ tín dụng")) {
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "Mã thanh toán không được để trống khi chọn Thẻ tín dụng",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!code.matches("^[a-zA-Z0-9]{6,20}$")) {
                    JOptionPane.showMessageDialog(dialog,
                            "Mã thanh toán phải là mã ngân hàng hợp lệ (chữ và số, 6-20 ký tự)!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int statusCol = getColumnIndex("Trạng thái");
            if (statusCol != -1) {
                model.setValueAt("Check-out", row, statusCol);
            }

            BookingService.updateBookingStatus(XML_FILE, bookingId, "Check-out");
            PaymentService.createPayment(
                    bookingId,
                    model.getValueAt(row, getColumnIndex("Họ tên")).toString(),
                    userName,
                    model.getValueAt(row, getColumnIndex("Phòng")).toString(),
                    Double.parseDouble(model.getValueAt(row, getColumnIndex("Tổng tiền")).toString().replace(",", "")),
                    method,
                    code,
                    LocalDateTime.parse(model.getValueAt(row, getColumnIndex("Ngày đến")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                    LocalDateTime.parse(model.getValueAt(row, getColumnIndex("Ngày đi")).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            );

            NotificationService.createNotification(
                    bookingId,
                    "",
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

    private String validateInput(String keyword, String type) {
        if (keyword.isEmpty() && !type.equals("Tất cả")) {
            return "Vui lòng nhập từ khóa tìm kiếm!";
        }
        if (type.equals("SĐT") && !keyword.matches("0\\d{9}")) {
            return "Số điện thoại phải chứa đúng 10 chữ số và bắt đầu bằng 0!";
        } else if (type.equals("Gmail") && !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Email không đúng định dạng!";
        } else if (type.equals("Mã đơn") && !keyword.matches("BK[A-Za-z0-9]{2,}")) {
            return "Mã đơn phải bắt đầu bằng 'BK' và chứa ít nhất 2 chữ cái hoặc số tiếp theo!";
        } else if (type.equals("Tài khoản") && !keyword.matches("[a-z0-9]{8,}")) {
            return "Tài khoản phải chứa ít nhất 8 chữ cái thường hoặc số!";
        } else if (type.equals("Họ tên") && keyword.length() < 2) {
            return "Họ tên phải chứa ít nhất 2 ký tự!";
        } else if (type.equals("Tất cả") && !keyword.isEmpty()) {
            if (!keyword.matches("0\\d{9}") &&
                    !keyword.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") &&
                    !keyword.matches("[A-Za-z0-9]{2,}") &&
                    keyword.length() < 2) {
                return "Từ khóa phải là số điện thoại (10 chữ số bắt đầu bằng 0), email hợp lệ, hoặc ít nhất 2 ký tự!";
            }
        }
        return null;
    }

    private String getBookingDetails(int row) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mã đơn       : ").append(controller.getView().getModel().getValueAt(row, 0)).append("\n");
        sb.append("Tài khoản    : ").append(controller.getView().getModel().getValueAt(row, 1)).append("\n");
        sb.append("Họ tên       : ").append(controller.getView().getModel().getValueAt(row, 2)).append("\n");
        sb.append("SĐT          : ").append(controller.getView().getModel().getValueAt(row, 3)).append("\n");
        sb.append("Gmail        : ").append(controller.getView().getModel().getValueAt(row, 4)).append("\n");
        sb.append("Phòng        : ").append(controller.getView().getModel().getValueAt(row, 5)).append("\n");
        sb.append("Ngày đến     : ").append(controller.getView().getModel().getValueAt(row, 6)).append("\n");
        sb.append("Ngày đi      : ").append(controller.getView().getModel().getValueAt(row, 7)).append("\n");
        sb.append("Tổng tiền    : ").append(controller.getView().getModel().getValueAt(row, 8)).append("\n");
        sb.append("Trạng thái   : ").append(controller.getView().getModel().getValueAt(row, 9)).append("\n");
        return sb.toString();
    }

    private int getColumnIndex(String columnName) {
        for (int i = 0; i < controller.getView().getTable().getColumnCount(); i++) {
            if (controller.getView().getTable().getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private String getTagValue(Element element, String tagName) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0 && list.item(0) != null) {
            return list.item(0).getTextContent();
        }
        return "";
    }
}
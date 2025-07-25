//package org.example.controller;
//
//import org.example.entity.*;
//import org.example.service.BookingService;
//import org.example.service.NotificationService;
//import org.example.service.RequestService;
//import org.example.utils.FileUtils;
//import org.example.view.RequestView;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class RequestController {
//    private static final String XML_PATH = "requests.xml";
//    private static final String BOOKINGS_XML_PATH = "bookings.xml";
//    private static final String NOTIFICATIONS_XML_PATH = "notifications.xml";
//    private final DefaultTableModel tableModel;
//    private final JTable table;
//    private final DateTimeFormatter dateTimeFormatter;
//    private final DateTimeFormatter detailedDateFormatter;
//    private final String errorMessage;
//    private final List<Request> requests;
//
//    public RequestController() {
//        this.tableModel = RequestView.getTableModel();
//        this.table = RequestView.getTable();
//        this.dateTimeFormatter = RequestView.getDateTimeFormatter();
//        this.detailedDateFormatter = RequestView.getDetailedDateFormatter();
//        this.errorMessage = RequestView.getErrorMessage();
//        this.requests = new ArrayList<>();
//    }
//
//    public void loadTableData() {
//        RequestXML requestXML = FileUtils.readFromFile(XML_PATH, RequestXML.class);
//        if (requestXML == null) {
//            JOptionPane.showMessageDialog(null, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        requests.clear();
//        requests.addAll(requestXML.getRequests().stream()
//                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) ||
//                        "Đã đọc".equalsIgnoreCase(r.getStatus()) ||
//                        "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus()))
//                .toList());
//
//        tableModel.setRowCount(0);
//        for (Request r : requests) {
//            String msg;
//            if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
//                msg = String.format(" %s gửi yêu cầu hủy phòng lúc %s",
//                        Objects.requireNonNullElse(r.getRequestId(), "Không rõ"),
//                        r.getSubmittedAt().format(dateTimeFormatter));
//            } else {
//                msg = String.format(" %s đã gửi yêu cầu đặt phòng lúc %s",
//                        Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                        r.getSubmittedAt().format(dateTimeFormatter));
//                if ("Đã đọc".equalsIgnoreCase(r.getStatus())) {
//                    msg += " [Đã đọc]";
//                }
//            }
//            tableModel.addRow(new Object[]{msg});
//        }
//    }
//
//    public void handleTableButtonClick(int selectedRow) {
//        if (selectedRow >= 0 && selectedRow < requests.size()) {
//            Request request = requests.get(selectedRow);
//            RequestView.showDetailDialog(request, this, selectedRow);
//        }
//    }
//
//    public String formatRequestDetails(Request r) {
//        List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
//        Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
//        int soNguoi = people.size();
//
//        StringBuilder danhSach = new StringBuilder();
//        for (Person p : people) {
//            danhSach.append("- ").append(p.getFullName())
//                    .append(" [").append(p.getDocumentType()).append(": ").append(p.getDocumentCode()).append("]\n");
//        }
//
//        return String.format("""
//                THÔNG TIN ĐẶT PHÒNG
//
//                Người dùng: %s
//                Người đại diện: %s
//                CCCD: %s
//                Gmail: %s
//                SĐT: %s
//
//                Số người: %d
//
//                Danh sách khách:
//                %s
//                Phòng: %s
//                Giá: %,.0f VND
//
//                Check-in: %s
//                Check-out: %s
//                Tạo yêu cầu: %s
//                Trạng thái: %s
//                """,
//                Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                daiDien.getFullName(), daiDien.getDocumentCode(),
//                Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
//                Objects.requireNonNullElse(r.getPhone(), "none"),
//                soNguoi, danhSach,
//                Objects.requireNonNullElse(r.getRoomId(), "Không rõ"), r.getAmount(),
//                r.getCheckIn().format(detailedDateFormatter),
//                r.getCheckOut().format(detailedDateFormatter),
//                r.getSubmittedAt().format(detailedDateFormatter),
//                r.getStatus());
//    }
//
//    public void handleQuayLai(Request r, int selectedRow, JPanel content) {
//        if (!"Đã đọc".equalsIgnoreCase(r.getStatus())) {
//            RequestService.updateStatus(r.getRequestId(), "Đã đọc");
//            r.setStatus("Đã đọc");
//            if (selectedRow < tableModel.getRowCount()) {
//                String msg = String.format("%s đã gửi yêu cầu đặt phòng lúc %s [Đã đọc]",
//                        Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                        r.getSubmittedAt().format(dateTimeFormatter));
//                tableModel.setValueAt(msg, selectedRow, 0);
//            }
//        }
//        SwingUtilities.getWindowAncestor(content).dispose();
//        SwingUtilities.invokeLater(this::loadTableData);
//    }
//
//    public void handleCancel(Request r, int selectedRow, JPanel content) {
//        if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
//                !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
//                !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
//            JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể hủy.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        int choice = JOptionPane.showConfirmDialog(content,
//                "Bạn có chắc chắn muốn hủy yêu cầu này?",
//                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
//        if (choice == JOptionPane.YES_OPTION) {
//            String bookingId = "BK00000001";
//            if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
//                bookingId = getBookingIdForCancelRequest(r.getRequestId());
//                if (bookingId == null) {
//                    JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }
//            RequestService.updateStatus(r.getRequestId(), "Đã bị hủy");
//            NotificationService.createNotification(
//                    bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                    "Không được hủy", "Đã gửi"
//            );
//            JOptionPane.showMessageDialog(null, "Bạn đã hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            SwingUtilities.getWindowAncestor(content).dispose();
//            SwingUtilities.invokeLater(this::loadTableData);
//        }
//    }
//
//    public void handleConfirm(Request r, int selectedRow, JPanel content) {
//        if (!"Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) &&
//                !"Đã đọc".equalsIgnoreCase(r.getStatus()) &&
//                !"Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
//            JOptionPane.showMessageDialog(null, "Yêu cầu đã xử lý. Không thể xác nhận.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        int choice = JOptionPane.showConfirmDialog(content,
//                "Bạn có chắc chắn muốn xác nhận yêu cầu này?",
//                "Xác nhận thông báo", JOptionPane.YES_NO_OPTION);
//        if (choice == JOptionPane.YES_OPTION) {
//            if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
//                String bookingId = getBookingIdForCancelRequest(r.getRequestId());
//                if (bookingId == null) {
//                    JOptionPane.showMessageDialog(null, "Không tìm thấy booking liên quan để xác nhận hủy.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                RequestService.updateStatus(r.getRequestId(), "Đã bị hủy");
//                BookingService.updateBookingStatus(BOOKINGS_XML_PATH, bookingId, "Đã bị hủy");
//                NotificationService.createNotification(
//                        bookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                        "Đã bị hủy", "Đã gửi"
//                );
//                JOptionPane.showMessageDialog(null, "Bạn đã xác nhận hủy yêu cầu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                String newBookingId = generateBookingId();
//                RequestService.updateStatus(r.getRequestId(), "Đã được duyệt");
//                try {
//                    List<Person> people = r.getPersons() != null ? r.getPersons() : new ArrayList<>();
//                    Person daiDien = people.isEmpty() ? new Person("Không rõ", "", "") : people.get(0);
//                    BookingService.createBooking(
//                            BOOKINGS_XML_PATH,
//                            newBookingId,
//                            r.getRequestId(),
//                            Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                            daiDien.getFullName(),
//                            Objects.requireNonNullElse(r.getEmail(), "Không rõ"),
//                            Objects.requireNonNullElse(r.getPhone(), "Không rõ"),
//                            Objects.requireNonNullElse(r.getRoomId(), "Không rõ"),
//                            r.getCheckIn(),
//                            r.getCheckOut(),
//                            r.getAmount(),
//                            people
//                    );
//                    NotificationService.createNotification(
//                            newBookingId, r.getRequestId(), Objects.requireNonNullElse(r.getUserName(), "Không rõ"),
//                            "Đã được duyệt", "Đã gửi"
//                    );
//                    JOptionPane.showMessageDialog(null, "Bạn đã xác nhận yêu cầu và tạo booking thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Lỗi khi tạo booking: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//            SwingUtilities.getWindowAncestor(content).dispose();
//            SwingUtilities.invokeLater(this::loadTableData);
//        }
//    }
//
//    private String getBookingIdForCancelRequest(String requestId) {
//        NotificationXML notificationXML = FileUtils.readFromFile(NOTIFICATIONS_XML_PATH, NotificationXML.class);
//        if (notificationXML == null || notificationXML.getNotifications() == null) {
//            return null;
//        }
//        return notificationXML.getNotifications().stream()
//                .filter(n -> "Đã được duyệt".equalsIgnoreCase(n.getContent()) &&
//                        n.getRequestId().equals(requestId))
//                .map(Notification::getBookingId)
//                .findFirst()
//                .orElse(null);
//    }
//
//    private String generateBookingId() {
//        return "BK" + System.currentTimeMillis();
//    }
//}
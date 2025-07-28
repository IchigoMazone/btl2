//package org.example.controller;
//
//import java.util.List;
//import org.example.entity.Request;
//import org.example.entity.RequestXML;
//import org.example.utils.FileUtils;
//import java.util.stream.Collectors;
//
//public class RequestController {
//    private static final String XML_PATH = "requests.xml";
//
//    // Đọc tất cả request không lọc status
//    public List<Request> getAllRequests() {
//        RequestXML requestXML = FileUtils.readFromFile(XML_PATH, RequestXML.class);
//        if (requestXML == null || requestXML.getRequests() == null) return null;
//        return requestXML.getRequests().stream()
//                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus())
//                        || "Đã đọc".equalsIgnoreCase(r.getStatus())
//                        || "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus()))
//                .collect(Collectors.toList());
//    }
//}

package org.example.controller;

import org.example.entity.*;
import org.example.service.RequestService;
import org.example.service.BookingService;
import org.example.service.NotificationService;
import org.example.utils.FileUtils;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private static final String XML_PATH = "requests.xml";
    private static final String NOTIFICATIONS_XML_PATH = "notifications.xml";
    private static final String BOOKINGS_XML_PATH = "bookings.xml";

    public List<Request> getFilteredRequests() {
        RequestXML requestXML = FileUtils.readFromFile(XML_PATH, RequestXML.class);
        if (requestXML == null) {
            return null;
        }
        return new ArrayList<>(requestXML.getRequests().stream()
                .filter(r -> "Gửi yêu cầu".equalsIgnoreCase(r.getStatus()) ||
                        "Đã đọc".equalsIgnoreCase(r.getStatus()) ||
                        "Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus()))
                .toList());
    }

    public void loadTableData(List<Request> requests, DefaultTableModel model) {
        model.setRowCount(0);
        for (Request r : requests) {
            String msg;
            if ("Gửi yêu cầu hủy".equalsIgnoreCase(r.getStatus())) {
                msg = String.format(" %s gửi yêu cầu hủy phòng lúc %s",
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

    public String getBookingIdForCancelRequest(String requestId) {
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

    public String generateBookingId() {
        return "BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ((int)(Math.random() * 99999) + 10000);
    }

    public void updateRequestStatus(String requestId, String status) {
        RequestService.updateStatus(requestId, status);
    }

    public void createBooking(String bookingId, Request request, String representativeName, List<Person> persons) throws Exception {
        BookingService.createBooking(
                BOOKINGS_XML_PATH,
                bookingId,
                request.getRequestId(),
                Objects.requireNonNullElse(request.getUserName(), "Không rõ"),
                representativeName,
                Objects.requireNonNullElse(request.getEmail(), "Không rõ"),
                Objects.requireNonNullElse(request.getPhone(), "Không rõ"),
                Objects.requireNonNullElse(request.getRoomId(), "Không rõ"),
                request.getCheckIn(),
                request.getCheckOut(),
                request.getAmount(),
                persons
        );
    }

    public void updateBookingStatus(String bookingId, String status) {
        BookingService.updateBookingStatus(BOOKINGS_XML_PATH, bookingId, status);
    }

    public void createNotification(String bookingId, String requestId, String userName, String content, String status) {
        NotificationService.createNotification(bookingId, requestId, userName, content, status);
    }
}
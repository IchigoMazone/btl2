package org.example.Test;


import java.time.LocalDateTime;
import java.util.*;

public class SetupRequest {

    private static final String FILE_PATH = "requests.xml";

    // -------------------- TẠO YÊU CẦU MỚI --------------------
    public static void createRequest(String userName, String fullName, String email, String phone, String cccd,
                                     String roomId, LocalDateTime checkIn, LocalDateTime checkOut, double amount) {

        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) {
            requestXML = new RequestXML();
        }

        List<Request> requests = requestXML.getRequests();

        Request request = new Request();
        request.setId(generateRequestId());
        request.setUserName(userName);
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhone(phone);
        request.setCccd(cccd);
        request.setRoomId(roomId);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setAmount(amount); // ✅ Gán tổng tiền

        // Lịch sử gửi yêu cầu
        LocalDateTime now = LocalDateTime.now();
        History history = new History();
        history.setType("Gửi yêu cầu");
        history.setBy(userName);
        history.setTime(now);
        request.getHistoryList().add(history);

        // Cập nhật submittedAt và status từ lịch sử
        request.setSubmittedAt(now);
        request.setStatus(convertActionToStatus("Gửi yêu cầu"));

        requests.add(request);
        requestXML.setRequests(requests);
        XMLUtil.writeToFile(FILE_PATH, requestXML);

        System.out.println("✅ Đã gửi yêu cầu thành công: " + request.getId());
    }

    // -------------------- ADMIN DUYỆT / TỪ CHỐI --------------------
    public static void reviewRequest(String requestId, String admin, boolean approved) {
        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) return;

        List<Request> requests = requestXML.getRequests();
        boolean found = false;

        for (Request req : requests) {
            if (req.getId().equals(requestId)) {
                String type = approved ? "Duyệt yêu cầu" : "Từ chối yêu cầu";

                // Thêm lịch sử
                History history = new History();
                history.setType(type);
                history.setBy(admin);
                history.setTime(LocalDateTime.now());
                req.getHistoryList().add(history);

                // Cập nhật submittedAt và status
                updateStatusAndSubmittedAtFromHistory(req);

                found = true;
                break;
            }
        }

        if (found) {
            XMLUtil.writeToFile(FILE_PATH, requestXML);
            System.out.println((approved ? "✅ Đã duyệt" : "❌ Đã từ chối") + " yêu cầu: " + requestId);
        } else {
            System.out.println("⚠️ Không tìm thấy yêu cầu: " + requestId);
        }
    }

    // -------------------- CẬP NHẬT TRẠNG THÁI KHÁC --------------------
    public static void updateStatus(String requestId, String actionType, String by) {
        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) return;

        for (Request req : requestXML.getRequests()) {
            if (req.getId().equals(requestId)) {
                History history = new History();
                history.setType(actionType);
                history.setBy(by);
                history.setTime(LocalDateTime.now());
                req.getHistoryList().add(history);

                // Cập nhật submittedAt và status
                updateStatusAndSubmittedAtFromHistory(req);

                XMLUtil.writeToFile(FILE_PATH, requestXML);
                System.out.println("✅ Đã cập nhật trạng thái: " + actionType + " cho " + requestId);
                return;
            }
        }

        System.out.println("❌ Không tìm thấy yêu cầu với ID: " + requestId);
    }

    // -------------------- HỖ TRỢ: cập nhật trạng thái + submittedAt từ history mới nhất --------------------
    private static void updateStatusAndSubmittedAtFromHistory(Request request) {
        List<History> histories = request.getHistoryList();
        if (!histories.isEmpty()) {
            History latest = histories.get(histories.size() - 1);
            request.setSubmittedAt(latest.getTime());
            request.setStatus(convertActionToStatus(latest.getType()));
        }
    }

    // -------------------- TẠO ID --------------------
    private static String generateRequestId() {
        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    // -------------------- CHUYỂN TÊN HÀNH ĐỘNG -> TRẠNG THÁI --------------------
    private static String convertActionToStatus(String action) {
        return switch (action) {
            case "Gửi yêu cầu" -> "Chờ duyệt";
            case "Duyệt yêu cầu" -> "Đã duyệt";
            case "Từ chối yêu cầu" -> "Đã từ chối";
            case "Yêu cầu hủy" -> "Yêu cầu hủy";
            case "Hủy bởi admin" -> "Đã hủy";
            case "Xác nhận no-show" -> "No-show";
            case "Check-in" -> "Đã nhận phòng";
            case "Check-out" -> "Đã trả phòng";
            default -> "Không xác định";
        };
    }
}

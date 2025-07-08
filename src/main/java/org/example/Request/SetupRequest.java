package org.example.Request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SetupRequest {

    private static final String FILE_PATH = "requests.xml";

    public static void createRequest(String userName, String fullName, String email, String phone, String roomId,
                                     LocalDateTime checkIn, LocalDateTime checkOut, double amount,
                                     List<Person> persons) {

        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) {
            requestXML = new RequestXML();
        }

        List<Request> requests = requestXML.getRequests();

        Request request = new Request();
        String requestId = generateRequestId();
        request.setRequestId(requestId);
        request.setUserName(userName);
        request.setFullName(fullName);
        request.setEmail(email);
        request.setPhone(phone);
        request.setRoomId(roomId);
        request.setCheckIn(checkIn);
        request.setCheckOut(checkOut);
        request.setAmount(amount);
        request.setPersons(persons);

        LocalDateTime now = LocalDateTime.now();
        request.addRequestHistory("Gửi yêu cầu", now);

        request.setSubmittedAt(now);
        request.setStatus(convertActionToStatus("Gửi yêu cầu"));

        requests.add(request);
        requestXML.setRequests(requests);
        XMLUtil.writeToFile(FILE_PATH, requestXML);

        System.out.println("✅ Đã gửi yêu cầu thành công: " + requestId);
    }

    public static void reviewRequest(String requestId, boolean approved) {
        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) return;

        List<Request> requests = requestXML.getRequests();
        boolean found = false;

        for (Request req : requests) {
            if (req.getRequestId().equals(requestId)) {
                String status = approved ? "Duyệt yêu cầu" : "Từ chối yêu cầu";
                req.addRequestHistory(status, LocalDateTime.now());
                updateStatusAndSubmittedAt(req);
                found = true;
                break;
            }
        }

        if (found) {
            XMLUtil.writeToFile(FILE_PATH, requestXML);
            System.out.println((approved ? "✅ Đã duyệt " : "❌ Đã từ chối ") + "yêu cầu: " + requestId);
        } else {
            System.out.println("⚠️ Không tìm thấy yêu cầu: " + requestId);
        }
    }

    public static void updateStatus(String requestId, String action) {
        RequestXML requestXML = XMLUtil.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) return;

        for (Request req : requestXML.getRequests()) {
            if (req.getRequestId().equals(requestId)) {
                req.addRequestHistory(action, LocalDateTime.now());
                updateStatusAndSubmittedAt(req);
                XMLUtil.writeToFile(FILE_PATH, requestXML);
                System.out.println("✅ Đã cập nhật trạng thái: " + action + " cho yêu cầu " + requestId);
                return;
            }
        }
        System.out.println("❌ Không tìm thấy yêu cầu với ID: " + requestId);
    }

    private static void updateStatusAndSubmittedAt(Request request) {
        List<HistoryEntry> histories = request.getHistory();
        if (!histories.isEmpty()) {
            HistoryEntry latest = histories.get(histories.size() - 1);
            request.setSubmittedAt(latest.getTimestamp());
            request.setStatus(convertActionToStatus(latest.getStatus()));
        }
    }

    private static String generateRequestId() {
        return "REQ" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

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

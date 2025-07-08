package org.example.Test;

import java.time.LocalDateTime;

public class Test2 {
    public static void main(String[] args) {
        // 1. Gửi yêu cầu mới
        SetupRequest.createRequest(
                "user01",
                "Nguyễn Văn A",
                "vana@example.com",
                "0912345678",
                "123456789012",
                "P101",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                2000000
        );

        // 2. Đọc lại danh sách yêu cầu từ file
        RequestXML requestXML = XMLUtil.readFromFile("requests.xml", RequestXML.class);

        if (requestXML == null || requestXML.getRequests().isEmpty()) {
            System.out.println("❌ Không tìm thấy yêu cầu nào để test.");
            return;
        }

        // 3. Lấy yêu cầu mới nhất (vừa gửi)
        Request request = requestXML.getRequests().get(requestXML.getRequests().size() - 1);
        String requestId = request.getRequestId();
        System.out.println("🆔 Đang test với request ID: " + requestId);

        // 4. Admin duyệt yêu cầu
        SetupRequest.reviewRequest(requestId, "admin01", true);

        // 5. Cập nhật trạng thái: Check-in
        SetupRequest.updateStatus(requestId, "Check-in", "admin01");

        // 6. Cập nhật trạng thái: Check-out
        SetupRequest.updateStatus(requestId, "Check-out", "admin01");

        // 7. Test từ chối yêu cầu khác (nếu cần tạo thêm)
        SetupRequest.createRequest(
                "user02",
                "Trần Thị B",
                "tranb@example.com",
                "0987654321",
                "987654321098",
                "P102",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(5),
                2500000
        );

        Request newRequest = XMLUtil.readFromFile("requests.xml", RequestXML.class)
                .getRequests().getLast();
        String newRequestId = newRequest.getRequestId();
        SetupRequest.reviewRequest(newRequestId, "admin02", false); // từ chối

        // 8. In kết thúc
        System.out.println("\n✅ Test hoàn tất.");
    }
}

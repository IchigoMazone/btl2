package org.example.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Test2 {

    public static void main(String[] args) {

        // Tạo danh sách khách (persons)
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Nguyen Van A", "123456789"));
        persons.add(new Person("Tran Thi B", "987654321"));

        // Tạo yêu cầu
        SetupRequest.createRequest(
                "user1",
                "Nguyen Van A",
                "user1@example.com",
                "0123456789",
                "R101",
                LocalDateTime.of(2025, 7, 10, 14, 0),
                LocalDateTime.of(2025, 7, 15, 12, 0),
                1500.0,
                persons
        );

        // Lấy requestId để test tiếp (ở thực tế bạn cần lấy từ file hoặc trả về)
        String requestId = getLastRequestId();

        // Duyệt yêu cầu
        SetupRequest.reviewRequest(requestId, true);

        // Cập nhật trạng thái check-in
        SetupRequest.updateStatus(requestId, "Check-in");

        // Cập nhật trạng thái check-out
        SetupRequest.updateStatus(requestId, "Check-out");
    }

    // Giả định đọc file lấy requestId cuối cùng (cách đơn giản)
    private static String getLastRequestId() {
        RequestXML requestXML = XMLUtil.readFromFile("requests.xml", RequestXML.class);
        if (requestXML == null || requestXML.getRequests().isEmpty()) return null;
        return requestXML.getRequests().get(requestXML.getRequests().size() - 1).getRequestId();
    }
}

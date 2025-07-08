package org.example.run;

import org.example.entity.Person;
import org.example.entity.RequestXML;
import org.example.service.RequestService;
import org.example.utils.FileUtils;

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
        RequestService.createRequest(
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

        // Lấy requestId để test tiếp
        String requestId = getFirstRequestId();

        // Duyệt yêu cầu
        RequestService.reviewRequest(requestId, true);

        // Cập nhật trạng thái check-in
        RequestService.updateStatus(requestId, "Check-in");

        // Cập nhật trạng thái check-out
        RequestService.updateStatus(requestId, "Check-out");
    }

    // Giả định đọc file lấy requestId đầu tiên
    private static String getFirstRequestId() {
        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        if (requestXML == null || requestXML.getRequests().isEmpty()) return null;
        return requestXML.getRequests().get(0).getRequestId();  // Lấy phần tử đầu tiên
    }

}


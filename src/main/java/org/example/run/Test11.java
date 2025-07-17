package org.example.run;

import org.example.entity.Request;
import org.example.service.RequestCheckerService;

import java.util.List;

public class Test11 {
    public static void main(String[] args) {
        String path = "requests.xml";  // 🔁 Cập nhật đúng đường dẫn tới file XML của bạn
        RequestCheckerService checker = new RequestCheckerService(path);

        List<Request> validRequests = checker.getValidRequestsForApproval();

        if (validRequests.isEmpty()) {
            System.out.println("⛔ Không có yêu cầu nào còn hiệu lực để duyệt (sau 2 giờ).");
        } else {
            System.out.println("✅ Danh sách yêu cầu còn hiệu lực để duyệt:");
            for (Request r : validRequests) {
                System.out.printf("- Mã yêu cầu: %s | User: %s | Check-in: %s | Phòng: %s\n",
                        r.getRequestId(), r.getUserName(), r.getCheckIn(), r.getRoomId());
            }
        }
    }
}

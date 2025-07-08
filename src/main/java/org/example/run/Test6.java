package org.example.run;

import org.example.service.RequestCheckerService;
import org.example.entity.Request;
import java.util.List;

public class Test6 {
    public static void main(String[] args) {
        String requestPath = "requests.xml";
        String user = "ivy";

        RequestCheckerService checker = new RequestCheckerService(requestPath);

        // 1. Lấy tất cả yêu cầu của user
        List<Request> allRequests = checker.getRequestsByUser(user);
        System.out.println("Tất cả yêu cầu của user " + user + ":");
        for (Request r : allRequests) {
            System.out.println("- ID: " + r.getRequestId() + " | Status: " + r.getStatus());
        }

        // 2. Lấy các yêu cầu đang ở trạng thái "Chờ duyệt"
        List<Request> pendingRequests = checker.getRequestsByUserAndStatus(user, "Chờ duyệt");
        System.out.println("\nYêu cầu đang 'Chờ duyệt':");
        for (Request r : pendingRequests) {
            System.out.println("- ID: " + r.getRequestId());
        }
    }
}

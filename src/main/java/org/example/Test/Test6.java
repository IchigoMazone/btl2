package org.example.Test;


import java.util.List;

public class Test6 {
    public static void main(String[] args) {
        String requestPath = "requests.xml";
        String user = "ivy";

        RequestCheckerService checker = new RequestCheckerService(requestPath);

        // 1. Lấy tất cả yêu cầu
        List<Request> allRequests = checker.getRequestsByUser(user);
        System.out.println("📋 Tất cả yêu cầu của user " + user + ":");
        allRequests.forEach(r -> System.out.println("- ID: " + r.getId() + " | Status: " + r.getStatus()));

        // 2. Lọc yêu cầu đang chờ duyệt
        List<Request> pendingRequests = checker.getRequestsByUserAndStatus(user, "Chờ duyệt");
        System.out.println("\n⏳ Yêu cầu 'Chờ duyệt':");
        pendingRequests.forEach(r -> System.out.println("- " + r.getId()));
    }
}


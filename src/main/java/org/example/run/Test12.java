package org.example.run;

import org.example.service.RequestPrinterService;

public class Test12 {
    public static void main(String[] args) {
        // Đường dẫn tới file requests.xml
        String filePath = "requests.xml";

        // In ra tất cả yêu cầu đặt phòng
        RequestPrinterService.printAllRequests(filePath);
    }
}

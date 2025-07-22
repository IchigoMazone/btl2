package org.example.service;

import org.example.entity.Person;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.utils.FileUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RequestPrinterService {

    public static void printAllRequests(String xmlFilePath) {
        RequestXML requestXML = FileUtils.readFromFile(xmlFilePath, RequestXML.class);
        if (requestXML == null || requestXML.getRequests().isEmpty()) {
            System.out.println("⚠️ Không có yêu cầu nào được tìm thấy trong file.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Request r : requestXML.getRequests()) {
            List<Person> guests = r.getPersons();
            if (guests.isEmpty()) continue;

            Person daiDien = guests.get(0); // người đại diện là người đầu tiên
            int soNguoi = guests.size();

            StringBuilder danhSachKhach = new StringBuilder();
            for (Person p : guests) {
                danhSachKhach
                        .append("- ")
                        .append(p.getFullName())
                        .append(" [").append(p.getDocumentType()).append(": ").append(p.getDocumentCode()).append("]\n");
            }

            String output = String.format("""
                    -------------------------------
                    🛎️  THÔNG TIN ĐẶT PHÒNG
                    -------------------------------
                    Người dùng: %s
                    Người đại diện: %s
                    CCCD: %s
                    Gmail: %s
                    SĐT: %s
                    
                    Số người: %d
                    
                    Danh sách khách:
                    %s
                    Phòng: %s
                    Giá: %,.0f VND
                    
                    Check-in: %s
                    Check-out: %s
                    Tạo yêu cầu: %s
                    Trạng thái: Đã gửi yêu cầu
                    """,
                    r.getUserName(),
                    daiDien.getFullName(),
                    daiDien.getDocumentCode(),
                    r.getEmail(),
                    r.getPhone(),
                    soNguoi,
                    danhSachKhach.toString(),
                    r.getRoomId(),
                    r.getAmount(),
                    r.getCheckIn().format(formatter),
                    r.getCheckOut().format(formatter),
                    r.getSubmittedAt().format(formatter)
            );

            System.out.println(output);
        }
    }
}

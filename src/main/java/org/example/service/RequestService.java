

package org.example.service;

import org.example.entity.Person;
import org.example.entity.Request;
import org.example.entity.RequestXML;
import org.example.entity.HistoryEntry;
import org.example.utils.FileUtils;

import java.time.LocalDateTime;
import java.util.List;

public class RequestService {

    private static final String FILE_PATH = "requests.xml";

    public static void createRequest(String requestId, String userName, String fullName, String email, String phone, String roomId,
                                     LocalDateTime checkIn, LocalDateTime checkOut, double amount,
                                     List<Person> persons) {

        RequestXML requestXML = FileUtils.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) {
            requestXML = new RequestXML();
        }

        List<Request> requests = requestXML.getRequests();

        Request request = new Request();
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
        request.setStatus("Gửi yêu cầu");

        requests.add(0, request);
        requestXML.setRequests(requests);
        FileUtils.writeToFile(FILE_PATH, requestXML);

        System.out.println("Đã gửi yêu cầu thành công: " + requestId);
    }


    public static void updateStatus(String requestId, String action) {
        RequestXML requestXML = FileUtils.readFromFile(FILE_PATH, RequestXML.class);
        if (requestXML == null) return;

        for (Request req : requestXML.getRequests()) {
            if (req.getRequestId().equals(requestId)) {
                req.addRequestHistory(action, LocalDateTime.now());
                updateStatusAndSubmittedAt(req);
                FileUtils.writeToFile(FILE_PATH, requestXML);
                System.out.println("Đã cập nhật trạng thái: " + action + " cho yêu cầu " + requestId);
                return;
            }
        }
        System.out.println("Không tìm thấy yêu cầu với ID: " + requestId);
    }


    private static void updateStatusAndSubmittedAt(Request request) {
        List<HistoryEntry> histories = request.getHistory();
        if (!histories.isEmpty()) {
            HistoryEntry latest = histories.get(histories.size() - 1);
            request.setSubmittedAt(latest.getTimestamp());
            request.setStatus(latest.getStatus());
        }
    }
}

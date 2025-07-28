
package org.example.service;

import org.example.utils.FileUtils;
import org.example.entity.RequestXML;
import org.example.entity.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestCheckerService {
    private final String requestFilePath;

    public RequestCheckerService(String requestFilePath) {
        this.requestFilePath = requestFilePath;
    }

    public List<Request> getRequestsByUser(String userName) {
        RequestXML requestXML = FileUtils.readFromFile(requestFilePath, RequestXML.class);
        if (requestXML == null) return new ArrayList<>();

        return requestXML.getRequests().stream()
                .filter(r -> r.getUserName().equalsIgnoreCase(userName))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestsByUserAndStatus(String userName, String status) {
        return getRequestsByUser(userName).stream()
                .filter(r -> r.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}

package org.example.Request;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {

    private String requestId;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String roomId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkIn;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkOut;

    private double amount;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime submittedAt;

    private String status;

    @XmlElement(name = "Person")
    private List<Person> persons = new ArrayList<>();

    @XmlElement(name = "History")
    private List<HistoryEntry> history = new ArrayList<>();

    public Request() {
    }

    public Request(String requestId, String userName, String fullName, String email, String phone, String roomId,
                   LocalDateTime checkIn, LocalDateTime checkOut, double amount, LocalDateTime submittedAt,
                   String status, List<Person> persons, List<HistoryEntry> history) {
        this.requestId = requestId;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.amount = amount;
        this.submittedAt = submittedAt;
        this.status = status;
        if (persons != null) this.persons = persons;
        if (history != null) this.history = history;
    }

    // Getters & setters...

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public LocalDateTime getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }
    public LocalDateTime getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<Person> getPersons() {
        return persons;
    }
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    public List<HistoryEntry> getHistory() {
        return history;
    }
    public void setHistory(List<HistoryEntry> history) {
        this.history = history;
    }

    // Thêm lịch sử trạng thái
    public void addRequestHistory(String status, LocalDateTime timestamp) {
        if (history == null) {
            history = new ArrayList<>();
        }
        HistoryEntry entry = new HistoryEntry(timestamp, status);
        history.add(entry);
    }
}

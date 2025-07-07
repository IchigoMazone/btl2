package org.example.Test;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class Request {
    private String id;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String cccd;
    private String roomId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkIn;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime checkOut;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime submittedAt;

    private String status;
    private double amount;

    @XmlElementWrapper(name = "historyList")
    @XmlElement(name = "history")
    private List<History> historyList = new ArrayList<>();

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }

    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public List<History> getHistoryList() { return historyList; }
    public void setHistoryList(List<History> historyList) { this.historyList = historyList; }
}

package org.example.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfo extends User {
    private String fullName;
    private String email;
    private String phoneNumber;

    public UserInfo() {}

    public UserInfo(
            String fullName,
            String userName,
            String password,
            String email,
            String phoneNumber
    ) {
        super(userName, password);
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
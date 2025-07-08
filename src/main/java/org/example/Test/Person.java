package org.example.Test;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Person {

    private String fullName;
    private String cccd;

    // === Constructor không tham số (bắt buộc cho JAXB) ===
    public Person() {}

    // === Constructor có tham số ===
    public Person(String fullName, String cccd) {
        this.fullName = fullName;
        this.cccd = cccd;
    }

    // === Getter & Setter ===
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}

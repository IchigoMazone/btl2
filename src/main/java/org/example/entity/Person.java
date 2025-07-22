//
//package org.example.entity;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class Person {
//    private String name;         // Họ tên
//    private String loaiGiayTo;   // "CCCD", "Hộ chiếu", hoặc "Không có"
//    private String maGiayTo;     // Mã giấy tờ (nếu có)
//
//    public Person() {
//    }
//
//    public Person(String name, String loaiGiayTo, String maGiayTo) {
//        this.name = name;
//        this.loaiGiayTo = loaiGiayTo;
//        this.maGiayTo = maGiayTo;
//    }
//
//    // === Getter & Setter ===
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLoaiGiayTo() {
//        return loaiGiayTo;
//    }
//
//    public void setLoaiGiayTo(String loaiGiayTo) {
//        this.loaiGiayTo = loaiGiayTo;
//    }
//
//    public String getMaGiayTo() {
//        return maGiayTo;
//    }
//
//    public void setMaGiayTo(String maGiayTo) {
//        this.maGiayTo = maGiayTo;
//    }
//
//    @Override
//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", loaiGiayTo='" + loaiGiayTo + '\'' +
//                ", maGiayTo='" + maGiayTo + '\'' +
//                '}';
//    }
//}

package org.example.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    private String fullName;     // Full name
    private String documentType; // "ID card", "Passport", or "None"
    private String documentCode; // Document code (if any)

    public Person() {
    }

    public Person(String fullName, String documentType, String documentCode) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentCode = documentCode;
    }

    // === Getter & Setter ===

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    @Override
    public String toString() {
        return "Person{" +
                "fullName='" + fullName + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentCode='" + documentCode + '\'' +
                '}';
    }
}

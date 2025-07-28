package org.example.controller;

import org.example.service.PaymentService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class HistoryController {
    private static final Set<String> readBookings = new HashSet<>();
    private static final String[] TABLE_COLUMNS = {"Lịch sử thanh toán"};
    private static final String ERROR_MESSAGE = "Không có lịch sử thanh toán";

    public Object[][] parseXMLData(String fileName) {
        try {
            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                return new Object[0][0];
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList paymentList = doc.getElementsByTagName("Payment");
            Object[][] data = new Object[paymentList.getLength()][12];

            for (int i = 0; i < paymentList.getLength(); i++) {
                Element payment = (Element) paymentList.item(i);
                data[i][0] = payment.getElementsByTagName("paymentId").item(0).getTextContent();
                data[i][1] = payment.getElementsByTagName("bookingId").item(0).getTextContent();
                data[i][2] = payment.getElementsByTagName("fullName").item(0).getTextContent();
                data[i][3] = payment.getElementsByTagName("userName").item(0).getTextContent();
                data[i][4] = payment.getElementsByTagName("room").item(0).getTextContent();
                data[i][5] = String.format("%,.0f", Double.parseDouble(payment.getElementsByTagName("amount").item(0).getTextContent()));
                data[i][6] = payment.getElementsByTagName("method").item(0).getTextContent();
                data[i][7] = payment.getElementsByTagName("code").item(0).getTextContent();
                data[i][8] = payment.getElementsByTagName("status").item(0).getTextContent();
                data[i][9] = payment.getElementsByTagName("paidAt").item(0).getTextContent();
                data[i][10] = payment.getElementsByTagName("checkIn").item(0).getTextContent();
                data[i][11] = payment.getElementsByTagName("checkOut").item(0).getTextContent();
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[0][0];
        }
    }

    public void loadTableData(Object[][] data, String username, DefaultTableModel model) {
        model.setRowCount(0);
        boolean hasData = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        Arrays.sort(data, Comparator.comparing(row -> {
            try {
                return LocalDateTime.parse((String) row[9], formatter);
            } catch (Exception e) {
                return LocalDateTime.now();
            }
        }));

        for (Object[] row : data) {
            String userName = row[3].toString().toLowerCase();
            String status = row[8].toString();
            if (userName.equalsIgnoreCase(username) && (status.equals("Đã thanh toán") || status.equals("Đã đọc"))) {
                String bookingId = row[1].toString();
                String timeString = row[9].toString();
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    String formattedPaidAt = outputFormat.format(inputFormat.parse(timeString));
                    String notificationMessage = String.format(" Đơn đặt phòng %s đã được thanh toán lúc %s", bookingId, formattedPaidAt);
                    boolean isRead = readBookings.contains(bookingId) || status.equals("Đã đọc");
                    if (isRead) {
                        notificationMessage += " [Đã đọc]";
                    }
                    model.addRow(new Object[]{notificationMessage});
                    hasData = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!hasData) {
            model.addRow(new Object[]{ERROR_MESSAGE});
        }
    }

    public void updatePaymentStatus(String fileName, String paymentId, String status) throws Exception {
        PaymentService.updatePaymentStatus(fileName, paymentId, status);
    }

    public boolean isBookingRead(String bookingId) {
        return readBookings.contains(bookingId);
    }

    public void markBookingAsRead(String bookingId) {
        readBookings.add(bookingId);
    }
}
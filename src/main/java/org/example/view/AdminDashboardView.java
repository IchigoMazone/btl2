////package org.example.view;
////
////import org.example.service.RoomFinderService;
////import org.kordamp.ikonli.materialdesign.MaterialDesign;
////import org.kordamp.ikonli.swing.FontIcon;
////import java.time.LocalDateTime;
////import java.time.format.DateTimeFormatter;    // để định dạng ngày giờ
////import java.time.Duration;                   // để tính khoảng thời gian giữa 2 thời điểm
////import java.time.temporal.ChronoUnit;        // để so sánh đơn vị như giờ, phút
////
////
////import javax.swing.*;
////import java.awt.*;
////
////public class AdminDashboardView {
////    public static JPanel createDashboardPanel() {
////        // Panel chính chứa Grid và viền
////        JPanel mainContent = new JPanel(new BorderLayout());
////        mainContent.setPreferredSize(new Dimension(950, 800));
////        mainContent.setBackground(UIManager.getColor("Panel.background"));
////        mainContent.setBorder(BorderFactory.createTitledBorder(
////                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
////                "Thông tin"
////        ));
////
////        // Lưới 2x4
////        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
////        grid.setBackground(new Color(245, 245, 245));
////        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
////
////
////        int soPhongTrong = RoomFinderService.countAvailableRoomsAt(LocalDateTime.now());
////        String strPhongTrong = String.valueOf(soPhongTrong);
////
////
////        // Thêm các thẻ thống kê
////        grid.add(createStatCard(MaterialDesign.MDI_HOTEL, "Phòng trống", strPhongTrong, new Color(76, 175, 80)));
////        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_MULTIPLE, "Khách hiện tại", "8", new Color(33, 150, 243)));
////        grid.add(createStatCard(MaterialDesign.MDI_CASH_MULTIPLE, "Doanh thu hôm nay", "3,500,000 VNĐ", new Color(255, 152, 0)));
////        grid.add(createStatCard(MaterialDesign.MDI_CALENDAR_CHECK, "Đơn đặt hôm nay", "5", new Color(156, 39, 176)));
////        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_CHECK, "Phòng đang hoạt động", "10", new Color(0, 200, 83)));
////        grid.add(createStatCard(MaterialDesign.MDI_LOGIN, "Check-in hôm nay", "4", new Color(0, 188, 212)));
////        grid.add(createStatCard(MaterialDesign.MDI_LOGOUT, "Check-out hôm nay", "3", new Color(255, 87, 34)));
////        grid.add(createStatCard(MaterialDesign.MDI_EMAIL, "Yêu cầu hiện tại", "6", new Color(121, 85, 72)));
////
////        mainContent.add(grid, BorderLayout.CENTER);
////
////        // Wrapper dùng GridBagLayout để căn giữa mainContent
////        JPanel wrapper = new JPanel(new GridBagLayout());
////        wrapper.setBackground(Color.WHITE);
////        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // lề phải 10
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.gridx = 0;
////        gbc.gridy = 0;
////        gbc.fill = GridBagConstraints.BOTH;
////        gbc.weightx = 1.0;
////        gbc.weighty = 1.0;
////
////        wrapper.add(mainContent, gbc);
////        return wrapper;
////    }
////
////    private static JPanel createStatCard(MaterialDesign iconCode, String label, String value, Color accentColor) {
////        JPanel card = new JPanel();
////        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
////        card.setBackground(Color.WHITE);
////        card.putClientProperty("JComponent.roundRect", true);
////        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
////
////        JLabel iconLabel = new JLabel(FontIcon.of(iconCode, 36, accentColor));
////        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
////
////        JLabel titleLabel = new JLabel(label);
////        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
////        titleLabel.setForeground(new Color(60, 60, 60));
////        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
////
////        JLabel valueLabel = new JLabel(value);
////        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
////        valueLabel.setForeground(accentColor);
////        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
////
////        card.add(iconLabel);
////        card.add(Box.createVerticalStrut(8));
////        card.add(titleLabel);
////        card.add(valueLabel);
////
////        return card;
////    }
////}
////
////
//
//
//package org.example.view;
//
//import org.example.service.RoomFinderService;
//import org.kordamp.ikonli.materialdesign.MaterialDesign;
//import org.kordamp.ikonli.swing.FontIcon;
//import org.example.entity.BookingXML;
//import javax.swing.*;
//import java.awt.*;
//import java.time.LocalDateTime;
//
//public class AdminDashboardView {
//    public static JPanel createDashboardPanel() {
//        // Panel chính chứa Grid và viền
//        JPanel mainContent = new JPanel(new BorderLayout());
//        mainContent.setPreferredSize(new Dimension(950, 800));
//        mainContent.setBackground(UIManager.getColor("Panel.background"));
//        mainContent.setBorder(BorderFactory.createTitledBorder(
//                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
//                "Thông tin"
//        ));
//
//        // Lưới 2x4
//        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
//        grid.setBackground(new Color(245, 245, 245));
//        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
//
//        // Lấy số phòng trống tại thời điểm hiện tại
//        int soPhongTrong = RoomFinderService.countAvailableRoomsAt(LocalDateTime.now());
//        String strPhongTrong = String.valueOf(soPhongTrong);
//
//        int soPhongHoatDong = RoomFinderService.countActiveRoomsAt(LocalDateTime.now());
//        String strHoatDong = String.valueOf(soPhongHoatDong);
//
//        int soKhach = RoomFinderService.getNumberOfActiveCustomersNow(BookingXML.getBookings());
//
//        // Thêm các thẻ thống kê
//        grid.add(createStatCard(MaterialDesign.MDI_HOTEL, "Phòng trống", strPhongTrong, new Color(76, 175, 80)));
//        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_MULTIPLE, "Khách hiện tại", "8", new Color(33, 150, 243)));
//        grid.add(createStatCard(MaterialDesign.MDI_CASH_MULTIPLE, "Doanh thu hôm nay", "3,500,000 VNĐ", new Color(255, 152, 0)));
//        grid.add(createStatCard(MaterialDesign.MDI_CALENDAR_CHECK, "Đơn đặt hôm nay", "5", new Color(156, 39, 176)));
//        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_CHECK, "Phòng đang hoạt động", strHoatDong, new Color(0, 200, 83)));
//        grid.add(createStatCard(MaterialDesign.MDI_LOGIN, "Check-in hôm nay", "4", new Color(0, 188, 212)));
//        grid.add(createStatCard(MaterialDesign.MDI_LOGOUT, "Check-out hôm nay", "3", new Color(255, 87, 34)));
//        grid.add(createStatCard(MaterialDesign.MDI_EMAIL, "Yêu cầu hiện tại", "6", new Color(121, 85, 72)));
//
//        mainContent.add(grid, BorderLayout.CENTER);
//
//        // Wrapper dùng GridBagLayout để căn giữa mainContent
//        JPanel wrapper = new JPanel(new GridBagLayout());
//        wrapper.setBackground(Color.WHITE);
//        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weightx = 1.0;
//        gbc.weighty = 1.0;
//
//        wrapper.add(mainContent, gbc);
//        return wrapper;
//    }
//
//    private static JPanel createStatCard(MaterialDesign iconCode, String label, String value, Color accentColor) {
//        JPanel card = new JPanel();
//        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
//        card.setBackground(Color.WHITE);
//        card.putClientProperty("JComponent.roundRect", true);
//        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        JLabel iconLabel = new JLabel(FontIcon.of(iconCode, 36, accentColor));
//        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel titleLabel = new JLabel(label);
//        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
//        titleLabel.setForeground(new Color(60, 60, 60));
//        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel valueLabel = new JLabel(value);
//        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
//        valueLabel.setForeground(accentColor);
//        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        card.add(iconLabel);
//        card.add(Box.createVerticalStrut(8));
//        card.add(titleLabel);
//        card.add(valueLabel);
//
//        return card;
//    }
//}


package org.example.view;

import org.example.entity.BookingXML;
import org.example.service.RoomFinderService;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import org.kordamp.ikonli.swing.FontIcon;
import org.example.utils.FileUtils;
import org.example.entity.RequestXML;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class AdminDashboardView {
    public static JPanel createDashboardPanel() {
        // Panel chính chứa Grid và viền
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setPreferredSize(new Dimension(950, 800));
        mainContent.setBackground(UIManager.getColor("Panel.background"));
        mainContent.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                "Thông tin"
        ));

        // Lưới 2x4
        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBackground(new Color(245, 245, 245));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Lấy dữ liệu động từ các service
        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        int soPhongTrong = RoomFinderService.countAvailableRoomsAt(LocalDateTime.now());
        int soPhongHoatDong = RoomFinderService.countActiveRoomsAt(LocalDateTime.now());
        int soKhach = RoomFinderService.getNumberOfActiveCustomersNow(bookingXML);
        int countIn = RoomFinderService.countTodayCheckinRooms(bookingXML);
        int countOu = RoomFinderService.countTodayCheckoutRooms(bookingXML);
        int countX = RoomFinderService.countTodayCreatedBookings(bookingXML);
        int countY = RoomFinderService.countPendingRequests(requestXML);

        // Chuyển về chuỗi
        String strPhongTrong = String.valueOf(soPhongTrong);
        String strHoatDong = String.valueOf(soPhongHoatDong);
        String strSoKhach = String.valueOf(soKhach);
        String strHomNayIn = String.valueOf(countIn);
        String strHomNayOu = String.valueOf(countOu);
        String doanhThu = RoomFinderService.getFormattedTodayRevenue(bookingXML);
        String numberCreate = String.valueOf(countX);
        String yeucau = String.valueOf(countY);

        // Thêm các thẻ thống kê
        grid.add(createStatCard(MaterialDesign.MDI_HOTEL, "Phòng trống", strPhongTrong, new Color(76, 175, 80)));
        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_MULTIPLE, "Khách hiện tại", strSoKhach, new Color(33, 150, 243)));
        grid.add(createStatCard(MaterialDesign.MDI_CASH_MULTIPLE, "Doanh thu hôm nay", doanhThu, new Color(255, 152, 0)));
        grid.add(createStatCard(MaterialDesign.MDI_CALENDAR_CHECK, "Đơn đặt hôm nay", numberCreate, new Color(156, 39, 176)));
        grid.add(createStatCard(MaterialDesign.MDI_ACCOUNT_CHECK, "Phòng đang hoạt động", strHoatDong, new Color(0, 200, 83)));
        grid.add(createStatCard(MaterialDesign.MDI_LOGIN, "Check-in hôm nay", strHomNayIn, new Color(0, 188, 212)));
        grid.add(createStatCard(MaterialDesign.MDI_LOGOUT, "Check-out hôm nay", strHomNayOu, new Color(255, 87, 34)));
        grid.add(createStatCard(MaterialDesign.MDI_EMAIL, "Yêu cầu hiện tại", yeucau, new Color(121, 85, 72)));

        mainContent.add(grid, BorderLayout.CENTER);

        // Wrapper dùng GridBagLayout để căn giữa mainContent
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        wrapper.add(mainContent, gbc);
        return wrapper;
    }

    private static JPanel createStatCard(MaterialDesign iconCode, String label, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.putClientProperty("JComponent.roundRect", true);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(FontIcon.of(iconCode, 36, accentColor));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }
}


package org.example.view;

import org.example.entity.BookingXML;
import org.example.service.RoomFinderService;
import org.example.utils.FileUtils;
import org.example.entity.RequestXML;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class AdminDashboardView {
    public static JPanel createDashboardPanel() {

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setPreferredSize(new Dimension(950, 800));
        mainContent.setBackground(UIManager.getColor("Panel.background"));
        mainContent.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                "Thông tin"
        ));

        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBackground(new Color(245, 245, 245));
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        RequestXML requestXML = FileUtils.readFromFile("requests.xml", RequestXML.class);
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        int soPhongTrong = RoomFinderService.countAvailableRoomsAt(LocalDateTime.now());
        int soPhongHoatDong = RoomFinderService.countActiveRoomsAt(LocalDateTime.now());
        int soKhach = RoomFinderService.getNumberOfActiveCustomersNow(bookingXML);
        int countIn = RoomFinderService.countTodayCheckinRooms(bookingXML);
        int countOu = RoomFinderService.countTodayCheckoutRooms(bookingXML);
        int countX = RoomFinderService.countTodayCreatedBookings(bookingXML);
        int countY = RoomFinderService.countPendingRequests(requestXML);

        String strPhongTrong = String.valueOf(soPhongTrong);
        String strHoatDong = String.valueOf(soPhongHoatDong);
        String strSoKhach = String.valueOf(soKhach);
        String strHomNayIn = String.valueOf(countIn);
        String strHomNayOu = String.valueOf(countOu);
        String doanhThu = RoomFinderService.getFormattedTodayRevenue(bookingXML);
        String numberCreate = String.valueOf(countX);
        String yeucau = String.valueOf(countY);

        grid.add(createStatCard("\uD83D\uDECF", "Phòng trống", strPhongTrong, new Color(76, 175, 80)));
        grid.add(createStatCard("\uD83D\uDCF7", "Khách hiện tại", strSoKhach, new Color(33, 150, 243)));
        grid.add(createStatCard("\uD83D\uDCB0", "Doanh thu hôm nay", doanhThu, new Color(255, 152, 0)));
        grid.add(createStatCard("\uD83D\uDD52", "Đơn đặt hôm nay", numberCreate, new Color(156, 39, 176)));
        grid.add(createStatCard("\uD83D\uDCFA", "Phòng đang hoạt động", strHoatDong, new Color(0, 200, 83)));
        grid.add(createStatCard("\u27A1", "Check-in hôm nay", strHomNayIn, new Color(0, 188, 212)));
        grid.add(createStatCard("\u2B05", "Check-out hôm nay", strHomNayOu, new Color(255, 87, 34)));
        grid.add(createStatCard("\u2709\uFE0F", "Yêu cầu hiện tại", yeucau, new Color(121, 85, 72)));

        mainContent.add(grid, BorderLayout.CENTER);

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

    private static JPanel createStatCard(String unicodeIcon, String label, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.putClientProperty("JComponent.roundRect", true);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(unicodeIcon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setForeground(accentColor);
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
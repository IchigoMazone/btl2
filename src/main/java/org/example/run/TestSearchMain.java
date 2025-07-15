//package org.example.run;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import org.example.view.CustomerSearchView;
//
//import javax.swing.*;
//
//public class TestSearchMain {
//    public static void main(String[] args) {
//        // Cài giao diện FlatLaf
//        try {
//            UIManager.setLookAndFeel(new FlatLightLaf());
//        } catch (Exception ex) {
//            System.err.println("❌ Không thể load FlatLaf.");
//        }
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("🔍 Tìm kiếm phòng khách sạn");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(950, 800); // Kích thước chuẩn
//            frame.setLocationRelativeTo(null); // Căn giữa màn hình
//
//            JPanel searchPanel = CustomerSearchView.createSearchPanel();
//            frame.setContentPane(searchPanel);
//
//            frame.setVisible(true);
//        });
//    }
//}

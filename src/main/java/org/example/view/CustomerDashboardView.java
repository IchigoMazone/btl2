////package org.example.view;
////
////import javax.swing.*;
////import javax.swing.border.EmptyBorder;
////import java.awt.*;
////
////public class CustomerDashboardView {
////    public static JPanel createUserHomePanel() {
////        JPanel panel = new JPanel(new BorderLayout());
////        panel.setBackground(Color.WHITE);
////        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
////
////        // Tiêu đề chào mừng
////        JLabel titleLabel = new JLabel("Chào mừng bạn đến với hệ thống quản lý khách sạn");
////        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
////        titleLabel.setForeground(new Color(0, 102, 204));
////        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
////        panel.add(titleLabel, BorderLayout.NORTH);
////
////        // Panel trung tâm gồm ảnh và mô tả
////        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
////        centerPanel.setBackground(Color.WHITE);
////
////        // Ảnh nền hoặc logo
////        try {
////            ImageIcon icon = new ImageIcon("hotel.jpg"); // ảnh cần có trong thư mục project
////            Image scaledImage = icon.getImage().getScaledInstance(400, 280, Image.SCALE_SMOOTH);
////            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
////            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
////            centerPanel.add(imageLabel, BorderLayout.WEST);
////        } catch (Exception e) {
////            JLabel imageLabel = new JLabel("[Ảnh không hiển thị được]");
////            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
////            centerPanel.add(imageLabel, BorderLayout.WEST);
////        }
////
////        // Mô tả dịch vụ
////        JTextArea infoText = new JTextArea();
////        infoText.setText("\nHệ thống giúp bạn:\n\n- Tra cứu thông tin phòng dễ dàng\n- Đặt phòng nhanh chóng\n- Theo dõi lịch sử và thông báo\n- Hỗ trợ thanh toán an toàn\n\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi!");
////        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
////        infoText.setEditable(false);
////        infoText.setWrapStyleWord(true);
////        infoText.setLineWrap(true);
////        infoText.setOpaque(false);
////        infoText.setMargin(new Insets(10, 10, 10, 10));
////
////        centerPanel.add(infoText, BorderLayout.CENTER);
////        panel.add(centerPanel, BorderLayout.CENTER);
////
////        return panel;
////    }
////}
//
//
//package org.example.view;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//
//public class CustomerDashboardView extends JPanel {
//    private static final String[] IMAGE_PATHS = {
//            "/image/phonggym.jpg",
//            "/image/hoboi.jpg",
//            "/image/nhahang.jpg", // Kiểm tra ảnh này
//            "/image/thangmay.jpg",
//            "/image/giatui.jpg",
//            "/image/duadonsanbay.jpg",
//            "/image/buasang.jpg",
//            "/image/ketsat.jpg"
//    };
//    private static final String[] AMENITIES = {
//            "<html>Phòng tập thể hình trang bị đầy đủ –<br>Đáp ứng nhu cầu rèn luyện sức khỏe mỗi ngày.</html>",
//            "<html>Hồ bơi rộng rãi, thoáng mát –<br>Không gian lý tưởng để thư giãn và tái tạo năng lượng.</html>",
//            "<html>Nhà hàng sang trọng –<br>Phục vụ ẩm thực phong phú trong không gian đẳng cấp.</html>",
//            "<html>Hệ thống thang máy hiện đại –<br>Đảm bảo sự thuận tiện và an toàn trong di chuyển.</html>",
//            "<html>Dịch vụ giặt ủi chuyên nghiệp –<br>Đem lại sự chỉn chu và thoải mái cho quý khách.</html>",
//            "<html>Dịch vụ đưa đón sân bay –<br>Hỗ trợ di chuyển nhanh chóng, tận tâm và đúng giờ.</html>",
//            "<html>Bữa sáng được phục vụ miễn phí –<br>Khởi đầu ngày mới với sự chu đáo và tiết kiệm.</html>",
//            "<html>Két an toàn trong phòng –<br>Giải pháp bảo vệ tài sản cá nhân một cách an tâm tuyệt đối.</html>"
//    };
//    private int currentImageIndex = 0;
//    private BufferedImage[] images;
//    private JLabel imageLabel;
//    private JPanel amenitiesPanel;
//    private JPanel dotPanel;
//    private Timer timer;
//
//    private static class CircularDot extends JLabel {
//        private Color color;
//
//        public CircularDot(Color color) {
//            this.color = color;
//            setPreferredSize(new Dimension(12, 12));
//            setOpaque(false);
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2d.setColor(color);
//            g2d.fillOval(0, 0, getWidth(), getHeight());
//            g2d.dispose();
//        }
//    }
//
//    public CustomerDashboardView() {
//        super(new BorderLayout());
//        setBorder(new EmptyBorder(0, 20, 20, 20));
//        images = new BufferedImage[IMAGE_PATHS.length];
//        loadInitialImage(); // Tải ảnh đầu tiên (phonggym.jpg)
//        initUI();
//        startTimer();
//        updateBackground();
//    }
//
//    private void loadInitialImage() {
//        long startTime = System.currentTimeMillis();
//        var url = getClass().getResource(IMAGE_PATHS[0]);
//        System.out.println("Checking resource: " + IMAGE_PATHS[0] + " -> " + url);
//        if (url != null) {
//            try {
//                images[0] = ImageIO.read(url);
//                if (images[0] == null) {
//                    System.err.println("Invalid image data at: " + IMAGE_PATHS[0]);
//                } else {
//                    System.out.println("Loaded image: " + IMAGE_PATHS[0] + " with dimensions " + images[0].getWidth() + "x" + images[0].getHeight());
//                }
//            } catch (IOException e) {
//                System.err.println("Error loading image at " + IMAGE_PATHS[0] + ": " + e.getMessage());
//            }
//        } else {
//            System.err.println("Resource not found: " + IMAGE_PATHS[0] + ". Please place it in src/main/java/image/");
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("Initial image loading time: " + (endTime - startTime) + " ms");
//    }
//
//    private void loadImage(int index) {
//        if (images[index] == null) {
//            var url = getClass().getResource(IMAGE_PATHS[index]);
//            System.out.println("Checking resource: " + IMAGE_PATHS[index] + " -> " + url);
//            if (url != null) {
//                try {
//                    images[index] = ImageIO.read(url);
//                    if (images[index] == null) {
//                        System.err.println("Invalid image data at: " + IMAGE_PATHS[index]);
//                    } else {
//                        System.out.println("Loaded image: " + IMAGE_PATHS[index] + " with dimensions " + images[index].getWidth() + "x" + images[index].getHeight());
//                    }
//                } catch (IOException e) {
//                    System.err.println("Error loading image at " + IMAGE_PATHS[index] + ": " + e.getMessage());
//                }
//            } else {
//                System.err.println("Resource not found: " + IMAGE_PATHS[index] + ". Please place it in src/main/java/image/");
//            }
//        }
//    }
//
//    private void updateBackground() {
//        if (images[currentImageIndex] != null) {
//            repaint();
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g.create();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        if (images[currentImageIndex] != null) {
//            g2d.drawImage(images[currentImageIndex], 0, 0, getWidth(), getHeight(), null); // Phủ kín toàn bộ
//        } else {
//            g2d.setColor(Color.LIGHT_GRAY);
//            g2d.fillRect(0, 0, getWidth(), getHeight());
//        }
//        g2d.dispose();
//    }
//
//    private void initUI() {
//        setLayout(new BorderLayout());
//
//        JPanel centerPanel = new JPanel(new GridBagLayout());
//        centerPanel.setOpaque(false);
//
//        amenitiesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
//        amenitiesPanel.setOpaque(false);
//        JLabel amenitiesTitle = new JLabel("Tiện ích khách sạn");
//        amenitiesTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
//        amenitiesTitle.setForeground(Color.WHITE);
//        amenitiesPanel.add(amenitiesTitle);
//        JLabel amenityLabel = new JLabel(AMENITIES[currentImageIndex]);
//        amenityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        amenityLabel.setForeground(Color.WHITE);
//        amenitiesPanel.add(amenityLabel);
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.weightx = 0.5;
//        gbc.insets = new Insets(0, 0, 0, 20);
//        centerPanel.add(amenitiesPanel, gbc);
//
//        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, -5, 0));
//        imagePanel.setOpaque(false);
//        imagePanel.setPreferredSize(new Dimension(255, 355)); // Kích thước bao quanh ảnh
//        imageLabel = new JLabel();
//        imageLabel.setPreferredSize(new Dimension(250, 350)); // Ảnh cố định 200x300
//        if (images[currentImageIndex] != null) {
//            Image scaledImage = images[currentImageIndex].getScaledInstance(250, 350, Image.SCALE_SMOOTH);
//            imageLabel.setIcon(new ImageIcon(scaledImage));
//            imageLabel.setText(""); // Xóa text khi có ảnh
//        } else {
//            imageLabel.setText("[Ảnh không hiển thị]");
//        }
//
//        JPanel imageWrapper = new JPanel(new GridBagLayout());
//        imageWrapper.setOpaque(false);
//        GridBagConstraints imageGbc = new GridBagConstraints();
//        imageGbc.anchor = GridBagConstraints.CENTER;
//        imageWrapper.add(imageLabel, imageGbc);
//        imagePanel.add(imageWrapper);
//
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        gbc.anchor = GridBagConstraints.EAST;
//        gbc.weightx = 0.0;
//        centerPanel.add(imagePanel, gbc);
//
//        dotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
//        dotPanel.setOpaque(false);
//        dotPanel.setPreferredSize(new Dimension(150, 20));
//        updateDots();
//
//        gbc = new GridBagConstraints();
//        gbc.gridx = 1;
//        gbc.gridy = 1;
//        gbc.anchor = GridBagConstraints.NORTH;
//        centerPanel.add(dotPanel, gbc);
//
//        add(centerPanel, BorderLayout.CENTER);
//    }
//
//    private void updateDots() {
//        dotPanel.removeAll();
//        int expectedDots = IMAGE_PATHS.length;
//        for (int i = 0; i < expectedDots; i++) {
//            CircularDot dot = new CircularDot(i == currentImageIndex ? new Color(0, 102, 204) : Color.GRAY);
//            int index = i;
//            dot.addMouseListener(new java.awt.event.MouseAdapter() {
//                @Override
//                public void mouseClicked(java.awt.event.MouseEvent e) {
//                    currentImageIndex = index;
//                    loadImage(currentImageIndex); // Tải ảnh khi chuyển
//                    updateImage();
//                    updateAmenities();
//                    updateBackground();
//                    updateDots();
//                }
//            });
//            dotPanel.add(dot);
//        }
//        dotPanel.revalidate();
//        dotPanel.repaint();
//    }
//
//    private void updateImage() {
//        if (images[currentImageIndex] != null) {
//            Image scaledImage = images[currentImageIndex].getScaledInstance(250, 350, Image.SCALE_SMOOTH);
//            imageLabel.setIcon(new ImageIcon(scaledImage));
//            imageLabel.setText(""); // Xóa text khi có ảnh
//        } else {
//            imageLabel.setIcon(null); // Xóa icon khi không có ảnh
//            imageLabel.setText("[Ảnh không hiển thị]");
//        }
//        revalidate();
//        repaint();
//    }
//
//    private void updateAmenities() {
//        amenitiesPanel.remove(1);
//        JLabel amenityLabel = new JLabel(AMENITIES[currentImageIndex]);
//        amenityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        amenityLabel.setForeground(Color.WHITE);
//        amenitiesPanel.add(amenityLabel, 1);
//        amenitiesPanel.revalidate();
//        amenitiesPanel.repaint();
//    }
//
//    private void startTimer() {
//        timer = new Timer(10000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                currentImageIndex = (currentImageIndex + 1) % IMAGE_PATHS.length;
//                loadImage(currentImageIndex); // Tải ảnh khi chuyển
//                updateImage();
//                updateAmenities();
//                updateBackground();
//                updateDots();
//            }
//        });
//        timer.start();
//    }
//
//    public void cleanup() {
//        if (timer != null) {
//            timer.stop();
//        }
//    }
//
//    public static JPanel createUserHomePanel() {
//        return new CustomerDashboardView();
//    }
//}

package org.example.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CustomerDashboardView extends JPanel {
    private static final String[] IMAGE_PATHS = {
            "/image/phonggym.jpg",
            "/image/hoboi.jpg",
            "/image/nhahang.jpg",
            "/image/thangmay.jpg",
            "/image/giatui.jpg",
            "/image/duadonsanbay.jpg",
            "/image/buasang.jpg",
            "/image/ketsat.jpg"
    };
    private static final String[] AMENITIES = {
            "<html>Phòng tập thể hình trang bị đầy đủ –<br>Đáp ứng nhu cầu rèn luyện sức khỏe mỗi ngày.</html>",
            "<html>Hồ bơi rộng rãi, thoáng mát –<br>Không gian lý tưởng để thư giãn và tái tạo năng lượng.</html>",
            "<html>Nhà hàng sang trọng –<br>Phục vụ ẩm thực phong phú trong không gian đẳng cấp.</html>",
            "<html>Hệ thống thang máy hiện đại –<br>Đảm bảo sự thuận tiện và an toàn trong di chuyển.</html>",
            "<html>Dịch vụ giặt ủi chuyên nghiệp –<br>Đem lại sự chỉn chu và thoải mái cho quý khách.</html>",
            "<html>Dịch vụ đưa đón sân bay –<br>Hỗ trợ di chuyển nhanh chóng, tận tâm và đúng giờ.</html>",
            "<html>Bữa sáng được phục vụ miễn phí –<br>Khởi đầu ngày mới với sự chu đáo và tiết kiệm.</html>",
            "<html>Két an toàn trong phòng –<br>Giải pháp bảo vệ tài sản cá nhân một cách an tâm tuyệt đối.</html>"
    };
    private int currentImageIndex = 0;
    private BufferedImage[] images;
    private JLabel imageLabel;
    private JPanel amenitiesPanel;
    private JPanel dotPanel;
    private Timer timer;

    private static class CircularDot extends JLabel {
        private Color color;

        public CircularDot(Color color) {
            this.color = color;
            setPreferredSize(new Dimension(12, 12));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

    public CustomerDashboardView() {
        super(new BorderLayout());
        setBorder(new EmptyBorder(0, 20, 20, 20));
        images = new BufferedImage[IMAGE_PATHS.length];
        loadInitialImage();
        initUI();
        startTimer();
        updateBackground();
    }

    private void loadInitialImage() {
        var url = getClass().getResource(IMAGE_PATHS[0]);
        if (url != null) {
            try {
                images[0] = ImageIO.read(url);
            } catch (IOException ignored) {
                // Không in lỗi, để trống để sử dụng placeholder nếu cần
            }
        }
    }

    private void loadImage(int index) {
        if (images[index] == null) {
            var url = getClass().getResource(IMAGE_PATHS[index]);
            if (url != null) {
                try {
                    images[index] = ImageIO.read(url);
                } catch (IOException ignored) {
                    // Không in lỗi
                }
            }
        }
    }

    private void updateBackground() {
        if (images[currentImageIndex] != null) {
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (images[currentImageIndex] != null) {
            g2d.drawImage(images[currentImageIndex], 0, 0, getWidth(), getHeight(), null);
        } else {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        amenitiesPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        amenitiesPanel.setOpaque(false);
        JLabel amenitiesTitle = new JLabel("Tiện ích khách sạn");
        amenitiesTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        amenitiesTitle.setForeground(Color.WHITE);
        amenitiesPanel.add(amenitiesTitle);
        JLabel amenityLabel = new JLabel(AMENITIES[currentImageIndex]);
        amenityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amenityLabel.setForeground(Color.WHITE);
        amenitiesPanel.add(amenityLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 0, 20);
        centerPanel.add(amenitiesPanel, gbc);

        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, -5, 0));
        imagePanel.setOpaque(false);
        imagePanel.setPreferredSize(new Dimension(255, 355));
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 350));
        if (images[currentImageIndex] != null) {
            Image scaledImage = images[currentImageIndex].getScaledInstance(250, 350, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
        } else {
            imageLabel.setText("[Ảnh không hiển thị]");
        }

        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.setOpaque(false);
        GridBagConstraints imageGbc = new GridBagConstraints();
        imageGbc.anchor = GridBagConstraints.CENTER;
        imageWrapper.add(imageLabel, imageGbc);
        imagePanel.add(imageWrapper);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0;
        centerPanel.add(imagePanel, gbc);

        dotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        dotPanel.setOpaque(false);
        dotPanel.setPreferredSize(new Dimension(150, 20));
        updateDots();

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        centerPanel.add(dotPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void updateDots() {
        dotPanel.removeAll();
        for (int i = 0; i < IMAGE_PATHS.length; i++) {
            CircularDot dot = new CircularDot(i == currentImageIndex ? new Color(0, 102, 204) : Color.GRAY);
            int index = i;
            dot.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    currentImageIndex = index;
                    loadImage(currentImageIndex);
                    updateImage();
                    updateAmenities();
                    updateBackground();
                    updateDots();
                }
            });
            dotPanel.add(dot);
        }
        dotPanel.revalidate();
        dotPanel.repaint();
    }

    private void updateImage() {
        if (images[currentImageIndex] != null) {
            Image scaledImage = images[currentImageIndex].getScaledInstance(250, 350, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("[Ảnh không hiển thị]");
        }
        revalidate();
        repaint();
    }

    private void updateAmenities() {
        amenitiesPanel.remove(1);
        JLabel amenityLabel = new JLabel(AMENITIES[currentImageIndex]);
        amenityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amenityLabel.setForeground(Color.WHITE);
        amenitiesPanel.add(amenityLabel, 1);
        amenitiesPanel.revalidate();
        amenitiesPanel.repaint();
    }

    private void startTimer() {
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex = (currentImageIndex + 1) % IMAGE_PATHS.length;
                loadImage(currentImageIndex);
                updateImage();
                updateAmenities();
                updateBackground();
                updateDots();
            }
        });
        timer.start();
    }

    public void cleanup() {
        if (timer != null) {
            timer.stop();
        }
    }

    public static JPanel createUserHomePanel() {
        return new CustomerDashboardView();
    }
}
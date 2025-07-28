

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
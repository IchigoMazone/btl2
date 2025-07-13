
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Giả lập class Room đơn giản
class Room {
    private String roomId;
    private String description;
    private String type;
    private int price;

    public Room(String roomId, String description, String type, int price) {
        this.roomId = roomId;
        this.description = description;
        this.type = type;
        this.price = price;
    }

    public String getRoomId() { return roomId; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public int getPrice() { return price; }
}

// Giả lập RoomFinderService đơn giản, không đọc file XML thực
class RoomFinderService {
    private List<Room> rooms = new ArrayList<>();

    public RoomFinderService(String roomPath, String bookingPath) {
        // Giả lập dữ liệu rooms
        rooms.add(new Room("R001", "Phòng đơn có cửa sổ nhỏ", "Phòng đơn", 300000));
        rooms.add(new Room("R006", "Phòng đôi có ban công nhỏ", "Phòng đôi", 450000));
        rooms.add(new Room("R011", "Phòng gia đình có 2 giường", "Phòng gia đình", 600000));
        rooms.add(new Room("R016", "Phòng đặc biệt VIP", "Phòng đặc biệt", 800000));
    }

    // Tìm phòng theo thời gian và loại phòng
    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, String roomType) {
        // Ở đây chỉ filter theo loại phòng vì giả lập, không xét booking thật
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (roomType == null || roomType.isEmpty() || r.getType().equals(roomType)) {
                result.add(r);
            }
        }
        return result;
    }
}

public class RoomSearchUI extends JFrame {

    private JTable table;
    private JTextField tfCheckIn, tfCheckOut;
    private JComboBox<String> cbLoaiPhong;
    private DefaultTableModel tableModel;

    // Thay đổi định dạng ngày sang dd-MM-yyyy
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private RoomFinderService finderService;

    public RoomSearchUI() {
        setTitle("Tìm kiếm phòng");
        setSize(950, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Khởi tạo RoomFinderService giả lập
        finderService = new RoomFinderService("rooms.xml", "bookings.xml");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Panel thông tin tìm kiếm ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin tìm kiếm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Ngày đến
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Ngày đến (dd-MM-yyyy):"), gbc);
        tfCheckIn = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(tfCheckIn, gbc);

        // Ngày đi
        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày đi (dd-MM-yyyy):"), gbc);
        tfCheckOut = new JTextField(10);
        gbc.gridx = 3;
        formPanel.add(tfCheckOut, gbc);

        // Loại phòng
        gbc.gridx = 4;
        formPanel.add(new JLabel("Loại phòng:"), gbc);
        cbLoaiPhong = new JComboBox<>(new String[]{"Tất cả", "Phòng đơn", "Phòng đôi", "Phòng đặc biệt", "Phòng gia đình"});
        gbc.gridx = 5;
        formPanel.add(cbLoaiPhong, gbc);

        // --- Bảng kết quả ---
        String[] columnNames = {"Mã phòng", "Mô tả", "Loại phòng", "Giá tiền (VNĐ)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Kết quả tìm kiếm"));

        // --- Nút bấm ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnTimKiem = new JButton("Tìm kiếm");
        JButton btnDatPhong = new JButton("Đặt phòng");

        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnDatPhong);

        btnTimKiem.addActionListener(this::performSearch);
        btnDatPhong.addActionListener(e -> datPhong());

        // Gộp các thành phần vào giao diện chính
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private void performSearch(ActionEvent e) {
        String checkInStr = tfCheckIn.getText().trim();
        String checkOutStr = tfCheckOut.getText().trim();
        String loaiPhong = (String) cbLoaiPhong.getSelectedItem();

        System.out.println("Ngày đến nhập: " + checkInStr);
        System.out.println("Ngày đi nhập: " + checkOutStr);

        try {
            LocalDate checkInDate = LocalDate.parse(checkInStr, dtf);
            LocalDate checkOutDate = LocalDate.parse(checkOutStr, dtf);

            if (checkOutDate.isBefore(checkInDate)) {
                JOptionPane.showMessageDialog(this, "Ngày đi phải sau ngày đến!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime checkIn = checkInDate.atStartOfDay();
            LocalDateTime checkOut = checkOutDate.atTime(23, 59, 59);

            List<Room> results = finderService.findAvailableRooms(
                    checkIn,
                    checkOut,
                    loaiPhong.equals("Tất cả") ? null : loaiPhong
            );

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phòng phù hợp.", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }

            loadTableData(results);

        } catch (Exception ex) {
            ex.printStackTrace();  // In lỗi để debug
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày đúng định dạng (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void datPhong() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng để đặt.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String maPhong = (String) tableModel.getValueAt(row, 0);
        String moTa = (String) tableModel.getValueAt(row, 1);
        String loaiPhong = (String) tableModel.getValueAt(row, 2);
        String gia = (String) tableModel.getValueAt(row, 3);

        String msg = String.format("Đặt phòng:\n- Mã: %s\n- Mô tả: %s\n- Loại: %s\n- Giá: %s", maPhong, moTa, loaiPhong, gia);
        JOptionPane.showMessageDialog(this, msg, "Xác nhận đặt phòng", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadTableData(List<Room> rooms) {
        tableModel.setRowCount(0);
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomId(),
                    r.getDescription(),
                    r.getType(),
                    String.format("%,d", r.getPrice())
            });
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new RoomSearchUI().setVisible(true));
    }
}
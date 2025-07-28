
package org.example.service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.example.entity.*;
import org.example.utils.FileUtils;
import org.example.entity.Booking;
import org.example.entity.Person;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingService {
    private static final String BOOKINGS_XML_PATH = "bookings.xml";
    private static final String FILE_PATH = "bookings.xml";

    public void addPerson(String bookingId, Person person) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        booking.getPersons().add(person);

        FileUtils.writeToFile(FILE_PATH, bookingXML);
    }

    public void updatePerson(String bookingId, String oldDocumentCode, String oldFullName, Person updatedPerson) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        List<Person> persons = booking.getPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getDocumentCode().equals(oldDocumentCode) && p.getFullName().equals(oldFullName)) {
                persons.set(i, updatedPerson);
                FileUtils.writeToFile(FILE_PATH, bookingXML);
                return;
            }
        }

        throw new RuntimeException("Không tìm thấy người cần sửa.");
    }

    public void deletePerson(String bookingId, String documentCode, String fullName) {
        BookingXML bookingXML = FileUtils.readFromFile(FILE_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            throw new RuntimeException("Không thể đọc dữ liệu booking.");
        }

        Booking booking = bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bookingId: " + bookingId));

        boolean removed = booking.getPersons().removeIf(
                p -> p.getDocumentCode().equals(documentCode) && p.getFullName().equals(fullName)
        );

        if (!removed) {
            throw new RuntimeException("Không tìm thấy người cần xóa.");
        }

        FileUtils.writeToFile(FILE_PATH, bookingXML);
    }

    public static void createBooking(String filePath,
                                     String bookingId,
                                     String requestId,
                                     String userName,
                                     String fullName,
                                     String email,
                                     String phone,
                                     String roomId,
                                     LocalDateTime checkIn,
                                     LocalDateTime checkOut,
                                     double amount,
                                     List<Person> persons) {

        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null) {
            bookingXML = new BookingXML();
        }

        List<Booking> bookings = bookingXML.getBookings();
        if (bookings == null) {
            bookings = new ArrayList<>();
        }

        LocalDateTime now = LocalDateTime.now();

        List<HistoryEntry> historyList = new ArrayList<>();
        historyList.add(new HistoryEntry(now, "Đã tạo booking"));

        Booking newBooking = new Booking(
                bookingId,
                requestId,
                userName,
                fullName,
                email,
                phone,
                roomId,
                checkIn,
                checkOut,
                amount,
                now,
                "Đã đặt",
                persons,
                historyList
        );

        bookings.add(0, newBooking);
        bookingXML.setBookings(bookings);
        FileUtils.writeToFile(filePath, bookingXML);

        System.out.println("Tạo booking thành công: " + bookingId);
    }

    public static void updateBookingStatus(String filePath, String bookingId, String newStatus) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (Booking booking : bookingXML.getBookings()) {
            if (booking.getBookingId().equals(bookingId)) {
                booking.setStatus(newStatus);
                booking.addBookingHistory(newStatus, LocalDateTime.now());
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile(filePath, bookingXML);
            System.out.println("Cập nhật trạng thái thành công: " + bookingId);
        } else {
            System.out.println("Không tìm thấy booking với ID: " + bookingId);
        }
    }

    public static void listCurrentGuests(LocalDateTime currentTime, String filePath) {
        BookingXML bookingXML = FileUtils.readFromFile(filePath, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        List<Booking> currentBookings = bookingXML.getBookings().stream()
                .filter(b -> !b.getCheckIn().isAfter(currentTime) && !b.getCheckOut().isBefore(currentTime))
                .toList();

        if (currentBookings.isEmpty()) {
            System.out.println("Không có khách nào đang lưu trú vào thời điểm " + currentTime);
            return;
        }

        System.out.println("Danh sách khách đang lưu trú tại " + currentTime + ":");
        for (Booking booking : currentBookings) {
            String phone = booking.getPhone();
            String email = booking.getEmail();
            String account = booking.getUserName();

            List<Person> persons = booking.getPersons();
            for (Person person : persons) {
                String documentType = person.getDocumentType();
                String documentCode = person.getDocumentCode();

                System.out.println("- " + person.getFullName()
                        + " | Tài khoản: " + (account != null ? account : "Không có")
                        + " | SĐT đại diện: " + phone
                        + " | Email đại diện: " + email
                        + " | Giấy tờ: " + documentType
                        + " | Mã giấy tờ: " + (documentCode != null ? documentCode : "Không có")
                        + " | Phòng: " + booking.getRoomId()
                        + " | Check-in: " + booking.getCheckIn()
                        + " | Check-out: " + booking.getCheckOut());
            }
        }
    }

    public void updateBooking(Booking updatedBooking) {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            System.out.println("Không có dữ liệu booking.");
            return;
        }

        boolean updated = false;
        for (int i = 0; i < bookingXML.getBookings().size(); i++) {
            Booking booking = bookingXML.getBookings().get(i);
            if (booking.getBookingId().equals(updatedBooking.getBookingId())) {
                bookingXML.getBookings().set(i, updatedBooking);
                updated = true;
                break;
            }
        }

        if (updated) {
            FileUtils.writeToFile("bookings.xml", bookingXML);
            System.out.println("Cập nhật booking thành công: " + updatedBooking.getBookingId());
        } else {
            System.out.println("Không tìm thấy booking với ID: " + updatedBooking.getBookingId());
        }
    }

    public List<Booking> getAllBookings() {
        BookingXML bookingXML = FileUtils.readFromFile("bookings.xml", BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            return new ArrayList<>();
        }
        return bookingXML.getBookings();
    }


    public static void checkBookingStatus(String bookingFilePath, String roomFilePath) {
        BookingXML bookingXML = FileUtils.readFromFile(bookingFilePath, BookingXML.class);
        RoomXML roomXML = FileUtils.readFromFile(roomFilePath, RoomXML.class);

        if (roomXML == null || roomXML.getRooms() == null || roomXML.getRooms().isEmpty()) {
            System.out.println("Không có dữ liệu phòng.");
            return;
        }

        List<Room> rooms = roomXML.getRooms();
        List<Booking> bookings = bookingXML != null && bookingXML.getBookings() != null ? bookingXML.getBookings() : new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Room room : rooms) {
            boolean isActive = false;

            for (Booking booking : bookings) {
                if (booking.getRoomId().equals(room.getRoomId())
                        && "Check-in".equalsIgnoreCase(booking.getStatus())
                        && !now.isBefore(booking.getCheckIn())
                        && !now.isAfter(booking.getCheckOut())) {
                    isActive = true;
                    break;
                }
            }

            System.out.println("Phòng " + room.getRoomId() + ": " + (isActive ? "Đang hoạt động" : "Không hoạt động"));
        }
    }

    public static Booking findBookingById(String bookingId) {
        BookingXML bookingXML = FileUtils.readFromFile(BOOKINGS_XML_PATH, BookingXML.class);
        if (bookingXML == null || bookingXML.getBookings() == null) {
            return null;
        }
        return bookingXML.getBookings().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }


    public static double getAmount(LocalDateTime checkIn, LocalDateTime checkOut, double giaNiemYet) {
        if (checkOut.isBefore(checkIn)) {
            return 0;
        }

        long totalNights = ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());

        if (checkOut.getHour() >= 12) {
            totalNights += 1;
        }

        if (totalNights == 0) {
            totalNights = 1;
        }

        return totalNights * giaNiemYet;
    }

    public int countPersonsByBookingId(String bookingId) throws Exception {
        File xmlFile = new File("bookings.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(BookingXML.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        BookingXML bookingsWrapper = (BookingXML) unmarshaller.unmarshal(xmlFile);
        List<Booking> bookings = bookingsWrapper.getBookings();

        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking.getNumberOfGuests();
            }
        }
        return 0;
    }


    public void deletePersonByRow(int rowIndex, String bookingId) throws Exception {
        int personCount = countPersonsByBookingId(bookingId);
        if (personCount <= 1) {
            throw new Exception("Không thể xóa: Phòng phải có ít nhất một người.");
        }

        File xmlFile = new File("bookings.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        removeWhitespaceNodes(doc.getDocumentElement());

        NodeList bookingList = doc.getElementsByTagName("Booking");
        Element targetBooking = null;
        for (int i = 0; i < bookingList.getLength(); i++) {
            Element bookingElement = (Element) bookingList.item(i);
            String currentBookingId = bookingElement.getElementsByTagName("bookingId").item(0).getTextContent();
            if (currentBookingId.equals(bookingId)) {
                targetBooking = bookingElement;
                break;
            }
        }

        if (targetBooking == null) {
            throw new Exception("Không tìm thấy Booking với bookingId: " + bookingId);
        }

        NodeList personList = targetBooking.getElementsByTagName("Person");
        if (rowIndex < 0 || rowIndex >= personList.getLength()) {
            throw new Exception("Chỉ số dòng không hợp lệ: " + rowIndex);
        }

        Node personNode = personList.item(rowIndex);
        personNode.getParentNode().removeChild(personNode);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

    private void removeWhitespaceNodes(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.hasChildNodes()) {
                removeWhitespaceNodes(child);
            }
        }
    }
}
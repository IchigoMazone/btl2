package org.example.service;

import org.example.utils.FileUtils;
import org.example.entity.RoomXML;
import org.example.entity.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomService {

    private final String filePath;

    public RoomService(String filePath) {
        this.filePath = filePath;
    }

    public List<Room> getAllRooms() {
        RoomXML roomXML = FileUtils.readFromFile(filePath, RoomXML.class);
        return roomXML != null ? roomXML.getRooms() : new ArrayList<>();
    }

    public void saveRooms(List<Room> rooms) {
        RoomXML roomXML = new RoomXML();
        roomXML.setRooms(rooms);
        FileUtils.writeToFile(filePath, roomXML);
    }

    public Room findRoomById(String roomId) {
        return getAllRooms().stream()
                .filter(r -> r.getRoomId().equals(roomId))
                .findFirst()
                .orElse(null);
    }

    public List<Room> searchByKeyword(String keyword) {
        return getAllRooms().stream()
                .filter(r -> r.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void addRoom(Room room) {
        List<Room> rooms = getAllRooms();
        rooms.add(room);
        saveRooms(rooms);
    }

    public void deleteRoom(String roomId) {
        List<Room> rooms = getAllRooms();
        rooms.removeIf(r -> r.getRoomId().equals(roomId));
        saveRooms(rooms);
    }

    public void updateRoom(Room updatedRoom) {
        List<Room> rooms = getAllRooms();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomId().equals(updatedRoom.getRoomId())) {
                rooms.set(i, updatedRoom);
                break;
            }
        }
        saveRooms(rooms);
    }
}


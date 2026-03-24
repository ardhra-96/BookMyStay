import java.util.*;

public class RoomAllocationService {

    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        // Check availability
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.get(roomType) <= 0) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Store globally
        allocatedRoomIds.add(roomId);

        // Store by type
        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory (VERY IMPORTANT)
        availability.put(roomType, availability.get(roomType) - 1);

        // Confirm booking
        System.out.println(
                "Booking confirmed for Guest: "
                        + reservation.getGuestName()
                        + ", Room ID: "
                        + roomId
        );
    }

    private String generateRoomId(String roomType) {

        int count = assignedRoomsByType
                .getOrDefault(roomType, new HashSet<>())
                .size() + 1;

        return roomType + "-" + count;
    }
}

public class Main {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue (from UC5)
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Single"));
        queue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Allocation service
        RoomAllocationService allocationService = new RoomAllocationService();

        // Process queue (FIFO)
        while (queue.hasPendingRequests()) {
            Reservation r = queue.getNextRequest();
            allocationService.allocateRoom(r, inventory);
        }
    }
}
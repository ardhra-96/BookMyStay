public class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

public class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type (case sensitive as per question)
        if (!roomType.equals("Single") &&
                !roomType.equals("Double") &&
                !roomType.equals("Suite")) {

            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate availability
        if (!inventory.hasAvailableRoom(roomType)) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Get input
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(name, roomType, inventory);

            // If valid → proceed
            bookingQueue.addRequest(new Reservation(name, roomType));
            System.out.println("Booking request added successfully.");

        } catch (InvalidBookingException e) {

            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());

        } finally {
            scanner.close();
        }
    }
}
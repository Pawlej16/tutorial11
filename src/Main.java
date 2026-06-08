
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ReservationService service = initializeService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==============================================");
        System.out.println("   Welcome to MediaLab Equipment Reservation  ");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> listStudents(service);
                case "2" -> listEquipment(service);
                case "3" -> createReservation(service, scanner);
                case "4" -> returnEquipment(service, scanner);
                case "5" -> service.printActiveReservations();
                case "6" -> service.printReport();
                case "7" -> searchEquipment(service, scanner);
                case "8" -> listAvailableEquipment(service);
                case "9" -> listEquipmentSortedByPrice(service);
                case "0" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Unknown option. Please enter a number from the menu.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("----------- MENU -----------");
        System.out.println("1. List students");
        System.out.println("2. List all equipment");
        System.out.println("3. Create reservation");
        System.out.println("4. Return equipment");
        System.out.println("5. Active reservations");
        System.out.println("6. Report (completed + revenue)");
        System.out.println("7. Search equipment by name");
        System.out.println("8. Show only available equipment");
        System.out.println("9. Equipment sorted by price");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private static void listStudents(ReservationService service) {
        System.out.println("\n========== STUDENTS ==========");
        for (Student s : service.getStudents()) {
            System.out.println(s);
        }
        System.out.println("==============================");
    }

    private static void listEquipment(ReservationService service) {
        System.out.println("\n========== EQUIPMENT ==========");
        for (Equipment eq : service.getAllEquipment()) {
            System.out.println(eq.getDisplayText());
        }
        System.out.println("================================");
    }

    private static void listAvailableEquipment(ReservationService service) {
        System.out.println("\n========== AVAILABLE EQUIPMENT ==========");
        List<Equipment> available = service.findAvailableEquipment();
        if (available.isEmpty()) {
            System.out.println("No equipment available at the moment.");
        } else {
            for (Equipment eq : available) {
                System.out.println(eq.getDisplayText());
            }
        }
        System.out.println("=========================================");
    }

    private static void listEquipmentSortedByPrice(ReservationService service) {
        System.out.println("\n========== EQUIPMENT SORTED BY PRICE ==========");
        for (Equipment eq : service.getEquipmentSortedByPrice()) {
            System.out.println(eq.getDisplayText());
        }
        System.out.println("================================================");
    }

    private static void createReservation(ReservationService service, Scanner scanner) {
        System.out.println("\n--- Create Reservation ---");
        System.out.print("Enter student id: ");
        String studentId = scanner.nextLine().trim();

        System.out.print("Enter equipment id: ");
        String equipmentId = scanner.nextLine().trim();

        System.out.print("Enter number of days (1-14): ");
        String daysInput = scanner.nextLine().trim();

        int days;
        try {
            days = Integer.parseInt(daysInput);
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + daysInput + "' is not a valid number.");
            return;
        }

        try {
            Reservation reservation = service.createReservation(studentId, equipmentId, days);
            double cost = reservation.calculateTotalCost(new LoyaltyDiscountPolicy());
            boolean discountApplied = reservation.getStudent().getLoyaltyPoints() >= 100;

            System.out.println();
            System.out.println("Reservation " + reservation.getId() + " created successfully.");
            System.out.println("Equipment: " + reservation.getEquipment().getName());
            System.out.printf("Daily price: %.2f PLN%n", reservation.getEquipment().calculateDailyPrice());
            System.out.printf("Days: %d%n", reservation.getDays());
            if (discountApplied) {
                System.out.println("Loyalty discount applied: 10%");
            }
            System.out.printf("Total cost: %.2f PLN%n", cost);
            System.out.println("Status: " + reservation.getStatus());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnEquipment(ReservationService service, Scanner scanner) {
        System.out.println("\n--- Return Equipment ---");
        System.out.print("Enter reservation id: ");
        String reservationId = scanner.nextLine().trim();

        try {
            service.returnEquipment(reservationId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchEquipment(ReservationService service, Scanner scanner) {
        System.out.print("Search equipment (enter part of the name): ");
        String query = scanner.nextLine().trim();

        List<Equipment> results = service.findEquipmentByName(query);
        System.out.println("\n--- Search results for: '" + query + "' ---");
        if (results.isEmpty()) {
            System.out.println("No equipment found matching '" + query + "'.");
        } else {
            for (Equipment eq : results) {
                System.out.println(eq.getDisplayText());
            }
        }
    }

    private static ReservationService initializeService() {
        List<Student> students = new ArrayList<>();
        students.add(new Student("S001", "Anna Kowalska", "12c", 120));
        students.add(new Student("S002", "Marek Nowak", "12c", 40));
        students.add(new Student("S003", "Julia Zielinska", "13a", 0));

        List<Equipment> equipment = new ArrayList<>();
        equipment.add(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80.0, 32, true));
        equipment.add(new LaptopSet("E002", "Dell XPS Demo", 100.0, 16, false));
        equipment.add(new CameraKit("E003", "Sony Content Kit", 90.0, 3, true));
        equipment.add(new CameraKit("E004", "Canon Interview Kit", 70.0, 1, true));

        DiscountPolicy discountPolicy = new LoyaltyDiscountPolicy();
        return new ReservationService(students, equipment, discountPolicy);
    }
}

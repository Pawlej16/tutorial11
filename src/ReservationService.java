
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final List<Student> students;
    private final List<Equipment> equipment;
    private final List<Reservation> reservations;
    private final DiscountPolicy discountPolicy;
    private int reservationCounter = 1;

    public ReservationService(List<Student> students, List<Equipment> equipment, DiscountPolicy discountPolicy) {
        this.students = students;
        this.equipment = equipment;
        this.reservations = new ArrayList<>();
        this.discountPolicy = discountPolicy;
    }

    public Reservation createReservation(String studentId, String equipmentId, int days) {
        Student student = findStudentById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id '" + studentId + "' not found."));

        Equipment eq = findEquipmentById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment with id '" + equipmentId + "' not found."));

        if (!eq.isAvailable()) {
            throw new IllegalStateException("Equipment " + equipmentId + " is not available.");
        }

        if (days < 1 || days > 14) {
            throw new IllegalArgumentException("Number of days must be between 1 and 14.");
        }

        String reservationId = String.format("R%03d", reservationCounter++);
        Reservation reservation = new Reservation(reservationId, student, eq, days);
        eq.setAvailable(false);
        reservations.add(reservation);
        return reservation;
    }

    public void returnEquipment(String reservationId) {
        Reservation reservation = findReservationById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation with id '" + reservationId + "' not found."));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation " + reservationId + " is not active (status: " + reservation.getStatus() + ").");
        }

        reservation.setStatus(ReservationStatus.RETURNED);
        reservation.getEquipment().setAvailable(true);

        double totalCost = reservation.calculateTotalCost(discountPolicy);
        int pointsEarned = (int) (totalCost / 10.0);
        reservation.getStudent().addLoyaltyPoints(pointsEarned);

        System.out.println("Equipment returned successfully.");
        System.out.printf("The student %s received %d loyalty points.%n",
                reservation.getStudent().getFullName(), pointsEarned);
    }

    public List<Equipment> findAvailableEquipment() {
        List<Equipment> available = new ArrayList<>();
        for (Equipment eq : equipment) {
            if (eq.isAvailable()) available.add(eq);
        }
        return available;
    }

    public List<Equipment> findEquipmentByName(String namePart) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment eq : equipment) {
            if (eq.getName().toLowerCase().contains(namePart.toLowerCase())) {
                result.add(eq);
            }
        }
        return result;
    }

    public List<Equipment> getEquipmentSortedByPrice() {
        List<Equipment> sorted = new ArrayList<>(equipment);
        sorted.sort(Comparator.comparingDouble(Equipment::calculateDailyPrice));
        return sorted;
    }

    public void printReport() {
        System.out.println("\n========== REPORT: COMPLETED RESERVATIONS ==========");
        List<Reservation> returned = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.RETURNED) returned.add(r);
        }

        if (returned.isEmpty()) {
            System.out.println("No completed reservations.");
        } else {
            for (Reservation r : returned) {
                System.out.println(r.getDisplayText());
            }
        }

        double totalRevenue = returned.stream()
                .mapToDouble(r -> r.calculateTotalCost(discountPolicy))
                .sum();
        System.out.printf("%nTotal revenue: %.2f PLN%n", totalRevenue);

        students.stream()
                .max(Comparator.comparingInt(Student::getLoyaltyPoints))
                .ifPresent(s -> System.out.printf("Top loyalty student: %s (%d points)%n",
                        s.getFullName(), s.getLoyaltyPoints()));
        System.out.println("=====================================================");
    }

    public void printActiveReservations() {
        System.out.println("========== ACTIVE RESERVATIONS ==========");
        boolean found = false;
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.ACTIVE) {
                System.out.println(r.getDisplayText());
                found = true;
            }
        }
        if (!found) System.out.println("No active reservations.");
        System.out.println("==========================================");
    }

    public List<Student> getStudents() { return students; }
    public List<Equipment> getAllEquipment() { return equipment; }
    public List<Reservation> getAllReservations() { return reservations; }

    private Optional<Student> findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
    }

    private Optional<Equipment> findEquipmentById(String id) {
        return equipment.stream().filter(e -> e.getId().equalsIgnoreCase(id)).findFirst();
    }

    private Optional<Reservation> findReservationById(String id) {
        return reservations.stream().filter(r -> r.getId().equalsIgnoreCase(id)).findFirst();
    }
}

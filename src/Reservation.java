
public class Reservation implements Displayable {
    private final String id;
    private final Student student;
    private final Equipment equipment;
    private final int days;
    private ReservationStatus status;

    public Reservation(String id, Student student, Equipment equipment, int days) {
        this.id = id;
        this.student = student;
        this.equipment = equipment;
        this.days = days;
        this.status = ReservationStatus.ACTIVE;
    }

    public String getId() { return id; }
    public Student getStudent() { return student; }
    public Equipment getEquipment() { return equipment; }
    public int getDays() { return days; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public double calculateTotalCost(DiscountPolicy discountPolicy) {
        double priceBeforeDiscount = equipment.calculateDailyPrice() * days;
        return discountPolicy.applyDiscount(student, priceBeforeDiscount);
    }

    @Override
    public String getDisplayText() {
        return String.format("[%s] Student: %-20s | Equipment: %-30s | Days: %2d | Status: %-9s | Cost: %.2f PLN",
                id, student.getFullName(), equipment.getName(),
                days, status, calculateTotalCost(new LoyaltyDiscountPolicy()));
    }
}

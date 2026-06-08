
public class LoyaltyDiscountPolicy implements DiscountPolicy {
    private static final int POINTS_THRESHOLD = 100;
    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public double applyDiscount(Student student, double price) {
        if (student.getLoyaltyPoints() >= POINTS_THRESHOLD) {
            return price * (1.0 - DISCOUNT_RATE);
        }
        return price;
    }

    public boolean isEligible(Student student) {
        return student.getLoyaltyPoints() >= POINTS_THRESHOLD;
    }
}

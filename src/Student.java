
public class Student {
    private final String id;
    private final String fullName;
    private final String groupName;
    private int loyaltyPoints;

    public Student(String id, String fullName, String groupName, int loyaltyPoints) {
        this.id = id;
        this.fullName = fullName;
        this.groupName = groupName;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getGroupName() { return groupName; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + fullName + " (group: " + groupName + ", loyalty points: " + loyaltyPoints + ")";
    }

}


public abstract class Equipment implements Displayable {
    private final String id;
    private final String name;
    private final double baseDailyPrice;
    private boolean available;

    public Equipment(String id, String name, double baseDailyPrice) {
        this.id = id;
        this.name = name;
        this.baseDailyPrice = baseDailyPrice;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseDailyPrice() {
        return baseDailyPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateDailyPrice();

    public abstract String getDetails();

    public abstract String getTypeName();

    @Override
    public String getDisplayText() {
        return "[" + id + "] "
                + name + " | "
                + "Type: " + getTypeName() + " | "
                + "Price: " + String.format("%.2f", calculateDailyPrice()) + " PLN/day | "
                + (available ? "AVAILABLE" : "UNAVAILABLE") + " | "
                + getDetails();
    }
}

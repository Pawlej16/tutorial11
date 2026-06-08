
public class LaptopSet extends Equipment {
    private final int ramGb;
    private final boolean hasDockingStation;

    public LaptopSet(String id, String name, double baseDailyPrice, int ramGb, boolean hasDockingStation) {
        super(id, name, baseDailyPrice);
        this.ramGb = ramGb;
        this.hasDockingStation = hasDockingStation;
    }

    public int getRamGb() { return ramGb; }
    public boolean hasDockingStation() { return hasDockingStation; }

    @Override
    public double calculateDailyPrice() {
        double price = getBaseDailyPrice();
        if (hasDockingStation) price += 15.0;
        if (ramGb >= 32) price += 25.0;
        return price;
    }

    @Override
    public String getDetails() {
        return ramGb + " GB RAM, " + (hasDockingStation ? "docking station" : "no docking station");
    }


    @Override
    public String getTypeName() {

        return "LaptopSet";
    }
}

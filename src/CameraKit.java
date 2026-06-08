
public class CameraKit extends Equipment {
    private final int lensCount;
    private final boolean hasTripod;

    public CameraKit(String id, String name, double baseDailyPrice, int lensCount, boolean hasTripod) {
        super(id, name, baseDailyPrice);
        this.lensCount = lensCount;
        this.hasTripod = hasTripod;
    }

    public int getLensCount() { return lensCount; }
    public boolean hasTripod() { return hasTripod; }

    @Override
    public double calculateDailyPrice() {
        double price = getBaseDailyPrice();
        price += lensCount * 10.0;
        if (hasTripod) price += 15.0;
        return price;
    }

    @Override
    public String getDetails() {
        return lensCount + " lens" + (lensCount > 1 ? "es" : "") + ", "
                + (hasTripod ? "tripod" : "no tripod");
    }


    @Override
    public String getTypeName() {

        return "CameraKit";
    }
}

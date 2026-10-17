import java.math.BigDecimal;
public class AirConditionerRemote extends TeachingDevice {
    public AirConditionerRemote(String id, String name, int yearInUse, BigDecimal price) {
        super(id, name, yearInUse, price, 2, 6, new BigDecimal("10000"));
    }

    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPrice().multiply(new BigDecimal("0.05"));   // 5% giá mua
    }
}

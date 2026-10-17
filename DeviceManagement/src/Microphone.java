/*NGUYEN THANH DAT - 202419040 */

import java.math.BigDecimal;

public class Microphone extends TeachingDevice {
    public Microphone(String id, String name, int yearInUse, BigDecimal price) {
        super(id, name, yearInUse, price, 1, 4, new BigDecimal("20000"));
    }

    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPrice().multiply(new BigDecimal("0.10"));   
    }
}

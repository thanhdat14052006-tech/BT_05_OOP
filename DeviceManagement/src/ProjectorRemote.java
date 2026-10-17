/*NGUYEN THANH DAT - 202419040 */

import java.math.BigDecimal;

public class ProjectorRemote extends TeachingDevice {
    public ProjectorRemote(String id, String name, int yearInUse, BigDecimal price) {
        super(id, name, yearInUse, price, 3, 8, new BigDecimal("10000"));
    }

    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPrice().multiply(new BigDecimal("0.08"));  
    }
}

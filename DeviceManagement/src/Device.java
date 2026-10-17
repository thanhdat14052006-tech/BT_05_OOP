/*NGUYEN THANH DAT - 202419040 */

import java.math.BigDecimal;
import java.time.Year;

public abstract class Device {
    private final String deviceId;     
    private String name;
    private int yearInUse;
    private BigDecimal price;
    private DeviceStatus status;

    protected Device(String deviceId, String name, int yearInUse, BigDecimal price) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }
        this.deviceId = deviceId;
        setName(name);
        setYearInUse(yearInUse);
        setPrice(price);
        this.status = DeviceStatus.AVAILABLE;
    }

    public String getDeviceId() { return deviceId; }
    public String getName() { return name; }
    public int getYearInUse() { return yearInUse; }
    public BigDecimal getPrice() { return price; }
    public DeviceStatus getStatus() { return status; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên thiết bị không được rỗng.");
        }
        this.name = name;
    }

    public void setYearInUse(int yearInUse) {
        if (yearInUse > Year.now().getValue()) {
            throw new IllegalArgumentException("Năm đưa vào sử dụng không được lớn hơn năm hiện tại.");
        }
        this.yearInUse = yearInUse;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá mua phải lớn hơn 0.");
        }
        this.price = price;
    }

    protected void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public void sendToMaintenance() { this.status = DeviceStatus.UNDER_MAINTENANCE; }

    public int getAge() {
        return Year.now().getValue() - yearInUse;
    }

    public abstract BigDecimal calculateAnnualMaintenanceCost();

    @Override
    public String toString() {
        return String.format("[%s] %s | năm %d | giá %s | trạng thái: %s",
                deviceId, name, yearInUse, price.toPlainString(), status);
    }
}

/*NGUYEN THANH DAT - 202419040 */

import java.math.BigDecimal;

public abstract class TeachingDevice extends Device {
    private final int maxBorrowDays;   
    private final int maxPeriods;       
    private final BigDecimal lateFeePerDay;

    protected TeachingDevice(String deviceId, String name, int yearInUse, BigDecimal price,
                             int maxBorrowDays, int maxPeriods, BigDecimal lateFeePerDay) {
        super(deviceId, name, yearInUse, price);
        if (maxBorrowDays <= 0 || maxPeriods <= 0) {
            throw new IllegalArgumentException("Giới hạn ngày/tiết mượn phải lớn hơn 0.");
        }
        this.maxBorrowDays = maxBorrowDays;
        this.maxPeriods = maxPeriods;
        this.lateFeePerDay = lateFeePerDay;
    }

    public int getMaxBorrowDays() { return maxBorrowDays; }
    public int getMaxPeriods() { return maxPeriods; }
    public BigDecimal getLateFeePerDay() { return lateFeePerDay; }

    public void markAsBorrowed() {
        if (getStatus() != DeviceStatus.AVAILABLE) {
            throw new IllegalStateException("Chỉ thiết bị Available mới được chuyển sang Borrowed.");
        }
        setStatus(DeviceStatus.BORROWED);
    }

    public void markAsReturned() {
        if (getStatus() != DeviceStatus.BORROWED) {
            throw new IllegalStateException("Chỉ thiết bị Borrowed mới được chuyển về Available.");
        }
        setStatus(DeviceStatus.AVAILABLE);
    }
}

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowItem {
    public static final int MIN_PERIOD = 1;
    public static final int MAX_PERIOD = 12;

    private final TeachingDevice device;      
    private final LocalDate borrowedDate;
    private final LocalDate dueDate;
    private int fromPeriod;
    private int toPeriod;
    private LocalDate returnedDate;          

    public BorrowItem(TeachingDevice device, LocalDate borrowedDate, LocalDate dueDate,
                      int fromPeriod, int toPeriod) {
        if (device == null) throw new IllegalArgumentException("Thiết bị không được null.");
        if (borrowedDate == null || dueDate == null) throw new IllegalArgumentException("Ngày mượn/hạn trả không được null.");
        if (dueDate.isBefore(borrowedDate)) {
            throw new IllegalArgumentException("Hạn trả phải sau hoặc bằng ngày mượn.");
        }
        this.device = device;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        setPeriods(fromPeriod, toPeriod);
    }

    public TeachingDevice getDevice() { return device; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public int getFromPeriod() { return fromPeriod; }
    public int getToPeriod() { return toPeriod; }
    public LocalDate getReturnedDate() { return returnedDate; }
    public boolean isReturned() { return returnedDate != null; }

    /** Đặt cả 2 tiết cùng lúc để kiểm tra from <= to một cách nhất quán. */
    public void setPeriods(int fromPeriod, int toPeriod) {
        if (fromPeriod < MIN_PERIOD || toPeriod > MAX_PERIOD || fromPeriod > toPeriod) {
            throw new IllegalArgumentException("Tiết mượn không hợp lệ (từ " + MIN_PERIOD + " đến " + MAX_PERIOD
                    + ", tiết đầu <= tiết cuối).");
        }
        this.fromPeriod = fromPeriod;
        this.toPeriod = toPeriod;
    }

    public int getPeriodCount() { return toPeriod - fromPeriod + 1; }

    public void returnDevice(LocalDate returnedDate) throws BorrowingException {
        if (returnedDate == null) throw new IllegalArgumentException("Ngày trả không được null.");
        if (isReturned()) throw new DeviceAlreadyReturnedException(device.getDeviceId());
        if (returnedDate.isBefore(borrowedDate)) {
            throw new InvalidReturnDateException(device.getDeviceId(), returnedDate, borrowedDate);
        }
        this.returnedDate = returnedDate;
    }

    public long getLateDays() {
        if (!isReturned() || !returnedDate.isAfter(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, returnedDate);
    }

    public boolean isLate() { return getLateDays() > 0; }

    public BigDecimal calculateLateFee() {
        return device.getLateFeePerDay().multiply(BigDecimal.valueOf(getLateDays()));
    }

    @Override
    public String toString() {
        return String.format("%s | tiết %d-%d | mượn %s | hạn %s | trả %s",
                device.getName(), fromPeriod, toPeriod, borrowedDate, dueDate,
                isReturned() ? returnedDate : "chưa trả");
    }
}

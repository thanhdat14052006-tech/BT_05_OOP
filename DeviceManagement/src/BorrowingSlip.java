import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorrowingSlip {
    private final String slipId;
    private final IBorrower borrower;          
    private final String teachingRoom;
    private final LocalDate borrowedDate;
    private final List<BorrowItem> items = new ArrayList<>();

    public BorrowingSlip(String slipId, IBorrower borrower, String teachingRoom, LocalDate borrowedDate) {
        if (slipId == null || slipId.isBlank()) throw new IllegalArgumentException("Mã phiếu không hợp lệ.");
        if (borrower == null) throw new IllegalArgumentException("Người mượn không được null.");
        if (teachingRoom == null || teachingRoom.isBlank()) throw new IllegalArgumentException("Phòng dạy không hợp lệ.");
        if (borrowedDate == null) throw new IllegalArgumentException("Ngày mượn không được null.");
        this.slipId = slipId;
        this.borrower = borrower;
        this.teachingRoom = teachingRoom;
        this.borrowedDate = borrowedDate;
    }

    public String getSlipId() { return slipId; }
    public IBorrower getBorrower() { return borrower; }
    public String getTeachingRoom() { return teachingRoom; }
    public LocalDate getBorrowedDate() { return borrowedDate; }

    public List<BorrowItem> getItems() { return Collections.unmodifiableList(items); }

    public void addDevice(TeachingDevice device, int borrowDays) throws BorrowingException {
        int maxPeriods = (device == null) ? 1 : device.getMaxPeriods();
        addDevice(device, borrowDays, 1, maxPeriods);
    }

    public void addDevice(TeachingDevice device, int borrowDays, int fromPeriod, int toPeriod)
            throws BorrowingException {
        validateBorrowing(device, borrowDays, fromPeriod, toPeriod);

        BorrowItem item = new BorrowItem(device, borrowedDate, borrowedDate.plusDays(borrowDays),
                fromPeriod, toPeriod);
        device.markAsBorrowed();
        items.add(item);
    }

    private void validateBorrowing(TeachingDevice device, int borrowDays, int fromPeriod, int toPeriod)
            throws BorrowingException {
        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null.");
        }
        String id = device.getDeviceId();
        if (borrowDays <= 0) {
            throw new InvalidBorrowPeriodException("Số ngày mượn phải lớn hơn 0.", id, borrowDays,
                    device.getMaxBorrowDays());
        }
        if (findItem(id) != null) {
            throw new DuplicateDeviceException(id);
        }
        if (items.size() >= borrower.getMaximumDevices()) {
            throw new BorrowLimitExceededException(borrower.getBorrowerId(), borrower.getMaximumDevices());
        }
        if (device.getStatus() != DeviceStatus.AVAILABLE) {
            throw new DeviceUnavailableException(id, device.getStatus());
        }
        if (borrowDays > device.getMaxBorrowDays()) {
            throw new InvalidBorrowPeriodException("Số ngày mượn " + borrowDays + " vượt giới hạn "
                    + device.getMaxBorrowDays() + " ngày của thiết bị " + id, id, borrowDays,
                    device.getMaxBorrowDays());
        }
        int periods = toPeriod - fromPeriod + 1;
        if (fromPeriod < BorrowItem.MIN_PERIOD || toPeriod > BorrowItem.MAX_PERIOD || periods <= 0) {
            throw new InvalidBorrowPeriodException("Tiết mượn không hợp lệ: " + fromPeriod + "-" + toPeriod,
                    id, periods, device.getMaxPeriods());
        }
        if (periods > device.getMaxPeriods()) {
            throw new InvalidBorrowPeriodException("Số tiết mượn " + periods + " vượt giới hạn "
                    + device.getMaxPeriods() + " tiết của thiết bị " + id, id, periods, device.getMaxPeriods());
        }
    }

    public BigDecimal returnDevice(String deviceId, LocalDate returnedDate) throws BorrowingException {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }
        BorrowItem item = findItem(deviceId);
        if (item == null) {
            throw new DeviceNotInSlipException(deviceId);
        }
        if (item.isReturned()) {
            throw new DeviceAlreadyReturnedException(deviceId);
        }
        item.returnDevice(returnedDate);         
        item.getDevice().markAsReturned();      
        return item.calculateLateFee();
    }

    public BigDecimal calculateTotalLateFee() {
        BigDecimal total = BigDecimal.ZERO;
        for (BorrowItem item : items) {
            total = total.add(item.calculateLateFee());
        }
        return total;
    }

    public List<BorrowItem> getLateItems() {
        List<BorrowItem> result = new ArrayList<>();
        for (BorrowItem item : items) if (item.isLate()) result.add(item);
        return result;
    }

    public List<BorrowItem> getUnreturnedItems() {
        List<BorrowItem> result = new ArrayList<>();
        for (BorrowItem item : items) if (!item.isReturned()) result.add(item);
        return result;
    }

    private BorrowItem findItem(String deviceId) {
        for (BorrowItem item : items) {
            if (item.getDevice().getDeviceId().equals(deviceId)) return item;
        }
        return null;
    }
}

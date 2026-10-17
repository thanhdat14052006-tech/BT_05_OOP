/*NGUYEN THANH DAT - 202419040 */

public class Lecturer implements IBorrower {
    private final String borrowerId;
    private final String fullName;

    public Lecturer(String borrowerId, String fullName) {
        if (borrowerId == null || borrowerId.isBlank()) throw new IllegalArgumentException("Mã người mượn không hợp lệ.");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Họ tên không hợp lệ.");
        this.borrowerId = borrowerId;
        this.fullName = fullName;
    }

    @Override public String getBorrowerId() { return borrowerId; }
    @Override public String getFullName() { return fullName; }
    @Override public int getMaximumDevices() { return 3; }
    @Override public String toString() { return "Giảng viên " + fullName + " (" + borrowerId + ")"; }
}

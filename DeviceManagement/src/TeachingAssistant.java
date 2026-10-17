/*NGUYEN THANH DAT - 202419040 */

public class TeachingAssistant implements IBorrower {
    private final String borrowerId;
    private final String fullName;

    public TeachingAssistant(String borrowerId, String fullName) {
        if (borrowerId == null || borrowerId.isBlank()) throw new IllegalArgumentException("Mã người mượn không hợp lệ.");
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Họ tên không hợp lệ.");
        this.borrowerId = borrowerId;
        this.fullName = fullName;
    }

    @Override public String getBorrowerId() { return borrowerId; }
    @Override public String getFullName() { return fullName; }
    @Override public int getMaximumDevices() { return 2; }
    @Override public String toString() { return "Trợ giảng " + fullName + " (" + borrowerId + ")"; }
}

/*NGUYEN THANH DAT - 202419040 */

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Main {

    interface Action { void run() throws Exception; }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8)); // hiển thị tiếng Việt
        successFlow();
        exceptionFlow();
    }

    static void successFlow() {
        System.out.println("===== LUỒNG THÀNH CÔNG =====");
        LocalDate today = LocalDate.of(2026, 10, 18);

        IBorrower lecturer = new Lecturer("GV01", "Nguyễn Văn A");
        ProjectorRemote projector = new ProjectorRemote("PR01", "Điều khiển máy chiếu Epson", 2024, new BigDecimal("500000"));
        AirConditionerRemote ac = new AirConditionerRemote("AC01", "Điều khiển điều hòa Daikin", 2019, new BigDecimal("300000"));
        Microphone mic = new Microphone("MC01", "Micro không dây Shure", 2023, new BigDecimal("2000000"));

        ClassRoom room = new ClassRoom("P301", "Phòng D3-301", 60);
        room.addDevice(projector);
        room.addDevice(ac);
        room.addDevice(mic);
        System.out.println("Chi phí bảo trì dự kiến/năm của phòng: " + room.calculateAnnualMaintenanceCost());
        System.out.println("Thiết bị cần bảo trì: " + room.getDevicesRequiringMaintenance());

        BorrowingSlip slip = new BorrowingSlip("S001", lecturer, "D3-301", today);
        try {
            slip.addDevice(projector, 2);
            slip.addDevice(mic, 1);

            BigDecimal fee1 = slip.returnDevice("PR01", today.plusDays(2));   // đúng hạn
            BigDecimal fee2 = slip.returnDevice("MC01", today.plusDays(3));   // hạn là +1 ngày -> muộn 2 ngày
            System.out.println("Phí trả muộn điều khiển máy chiếu: " + fee1);
            System.out.println("Phí trả muộn micro: " + fee2);
            System.out.println("Tổng phí trả muộn: " + slip.calculateTotalLateFee());
        } catch (BorrowingException e) {
            System.out.println("Lỗi nghiệp vụ: " + e.getMessage());
        } finally {
            System.out.println("--- Báo cáo phiếu " + slip.getSlipId() + " ---");
            System.out.println("Các thiết bị trả muộn:");
            slip.getLateItems().forEach(i -> System.out.println("  " + i + " | muộn " + i.getLateDays() + " ngày"));
            System.out.println("Các thiết bị chưa trả:");
            slip.getUnreturnedItems().forEach(i -> System.out.println("  " + i));
            System.out.println("(AC01 chưa được thêm vào phiếu nên trạng thái vẫn là " + ac.getStatus() + ")");
        }
        System.out.println();
    }

    static void exceptionFlow() {
        System.out.println("===== LUỒNG NGOẠI LỆ =====");
        LocalDate today = LocalDate.of(2026, 10, 18);
        IBorrower lecturer = new Lecturer("GV01", "Nguyễn Văn A");
        IBorrower ta = new TeachingAssistant("TG01", "Trần Thị B");

        // 1. Mượn điều khiển điều hòa đang bảo trì
        runCase("1. Mượn điều khiển điều hòa đang UnderMaintenance", () -> {
            AirConditionerRemote ac = new AirConditionerRemote("AC02", "Điều khiển điều hòa", 2022, new BigDecimal("300000"));
            ac.sendToMaintenance();
            BorrowingSlip slip = new BorrowingSlip("S002", lecturer, "D3-301", today);
            try {
                slip.addDevice(ac, 1);
            } finally {
                System.out.println("   Trạng thái thiết bị sau lỗi: " + ac.getStatus()
                        + " | số item trong phiếu: " + slip.getItems().size());
            }
        });

        runCase("2. Trợ giảng mượn thiết bị thứ ba", () -> {
            BorrowingSlip slip = new BorrowingSlip("S003", ta, "D3-301", today);
            ProjectorRemote p = new ProjectorRemote("PR10", "Điều khiển máy chiếu", 2022, new BigDecimal("500000"));
            AirConditionerRemote a = new AirConditionerRemote("AC10", "Điều khiển điều hòa", 2022, new BigDecimal("300000"));
            Microphone m = new Microphone("MC10", "Micro", 2022, new BigDecimal("2000000"));
            slip.addDevice(p, 1);
            slip.addDevice(a, 1);
            try {
                slip.addDevice(m, 1);   // thiết bị thứ 3 -> lỗi
            } finally {
                System.out.println("   Micro vẫn là: " + m.getStatus() + " | số item: " + slip.getItems().size());
            }
        });

        runCase("3. Thêm cùng một micro hai lần", () -> {
            BorrowingSlip slip = new BorrowingSlip("S004", lecturer, "D3-301", today);
            Microphone m = new Microphone("MC20", "Micro", 2022, new BigDecimal("2000000"));
            slip.addDevice(m, 1);
            slip.addDevice(m, 1);
        });

        runCase("4. Mượn micro 3 ngày dù giới hạn là 1 ngày", () -> {
            BorrowingSlip slip = new BorrowingSlip("S005", lecturer, "D3-301", today);
            Microphone m = new Microphone("MC30", "Micro", 2022, new BigDecimal("2000000"));
            try {
                slip.addDevice(m, 3);
            } catch (InvalidBorrowPeriodException e) {
                System.out.println("   (chi tiết) mã=" + e.getDeviceId() + ", yêu cầu=" + e.getRequested()
                        + ", tối đa=" + e.getMaximum());
                throw e;
            } finally {
                System.out.println("   Micro vẫn là: " + m.getStatus());
            }
        });

        runCase("5. Trả một điều khiển máy chiếu không thuộc phiếu", () -> {
            BorrowingSlip slip = new BorrowingSlip("S006", lecturer, "D3-301", today);
            slip.addDevice(new Microphone("MC40", "Micro", 2022, new BigDecimal("2000000")), 1);
            slip.returnDevice("PR99", today);
        });

        runCase("6. Trả cùng một thiết bị hai lần", () -> {
            BorrowingSlip slip = new BorrowingSlip("S007", lecturer, "D3-301", today);
            ProjectorRemote p = new ProjectorRemote("PR50", "Điều khiển máy chiếu", 2022, new BigDecimal("500000"));
            slip.addDevice(p, 1);
            slip.returnDevice("PR50", today);
            slip.returnDevice("PR50", today);
        });

        runCase("7. Trả thiết bị với ngày trả trước ngày mượn", () -> {
            BorrowingSlip slip = new BorrowingSlip("S008", lecturer, "D3-301", today);
            ProjectorRemote p = new ProjectorRemote("PR60", "Điều khiển máy chiếu", 2022, new BigDecimal("500000"));
            slip.addDevice(p, 1);
            try {
                slip.returnDevice("PR60", today.minusDays(1));
            } finally {
                System.out.println("   Thiết bị vẫn là: " + p.getStatus() + " (không bị đổi sai)");
            }
        });
    }

    static void runCase(String title, Action action) {
        System.out.println(title);
        try {
            action.run();
            System.out.println("   -> Không có lỗi (không mong đợi!)");
        } catch (BorrowingException e) {
            System.out.println("   -> Bắt được " + e.getClass().getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   -> Lỗi khác " + e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            System.out.println("   (kết thúc tình huống)\n");
        }
    }
}

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * DieRoll: Class mô phỏng việc tung xúc xắc với số lượng và phần thưởng tùy chọn.
 */
public class DieRoll {
    private final int ndice;
    private final int nsides;
    private final int bonus;

    // Đối tượng Statistics để lưu trữ và tính toán thống kê
    private Statistics statistics = new Statistics();

    /**
     * Constructor để khởi tạo một đối tượng xúc xắc.
     * @param ndice Số lượng xúc xắc
     * @param nsides Số mặt trên mỗi xúc xắc
     * @param bonus Giá trị cộng thêm vào kết quả cuối cùng
     * @throws IllegalArgumentException nếu đầu vào không hợp lệ
     */
    public DieRoll(int ndice, int nsides, int bonus) {
        if (ndice <= 0) {
            throw new IllegalArgumentException("Số lượng xúc xắc phải lớn hơn 0.");
        }
        if (nsides <= 1) {
            throw new IllegalArgumentException("Số mặt của xúc xắc phải lớn hơn 1.");
        }
        
        this.ndice = ndice;
        this.nsides = nsides;
        this.bonus = bonus;
    }

    /**
     * Phương thức chung để tạo ra kết quả tung xúc xắc.
     * @return Một mảng các giá trị kết quả tung xúc xắc
     */
    private int[] rollDice() {
        return IntStream.range(0, ndice)
                        .map(i -> ThreadLocalRandom.current().nextInt(1, nsides + 1))
                        .toArray();
    }

    /**
     * Tung xúc xắc và trả về kết quả.
     * @return Đối tượng RollResult chứa kết quả tung và tổng điểm
     */
    public RollResult makeRoll() {
        RollResult result = new RollResult(bonus);
        int[] rolls = rollDice();
        
        // Thêm kết quả từng xúc xắc vào kết quả
        for (int roll : rolls) {
            result.addResult(roll);
        }

        // Cập nhật thống kê
        statistics.updateStats(rolls);

        return result;
    }

    /**
     * Lấy tổng điểm của tất cả các xúc xắc.
     * @return Tổng điểm của tất cả các xúc xắc cộng với phần thưởng
     */
    public int getTotalRoll() {
        return IntStream.of(rollDice()).sum() + bonus;
    }

    /**
     * Tính trung bình của tất cả các kết quả tung xúc xắc.
     * @return Trung bình điểm của tất cả các xúc xắc cộng với phần thưởng
     */
    public double getAverageRoll() {
        return (double) getTotalRoll() / ndice;
    }

    /**
     * Lấy thống kê kết quả tung xúc xắc.
     * @return Đối tượng Statistics chứa các thông tin thống kê
     */
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public String toString() {
        return String.format("%dd%d%s", ndice, nsides, 
            (bonus != 0 ? (bonus > 0 ? " cộng " : " trừ ") + Math.abs(bonus) : ""));
    }

    /**
     * Lớp Statistics lưu trữ và tính toán thống kê kết quả tung xúc xắc.
     */
    public static class Statistics {
        private Map<Integer, Integer> frequency = new HashMap<>();
        private int totalRolls = 0;
        private int totalValue = 0;

        /**
         * Cập nhật thống kê với kết quả tung xúc xắc.
         * @param rolls Mảng các giá trị kết quả tung xúc xắc
         */
        public void updateStats(int[] rolls) {
            for (int roll : rolls) {
                frequency.put(roll, frequency.getOrDefault(roll, 0) + 1);
            }
            totalRolls++;
            totalValue += IntStream.of(rolls).sum();
        }

        /**
         * Lấy số lần xuất hiện của mỗi giá trị.
         * @return Map chứa số lần xuất hiện của mỗi giá trị
         */
        public Map<Integer, Integer> getFrequency() {
            return frequency;
        }

        /**
         * Lấy tổng số lần tung xúc xắc.
         * @return Tổng số lần tung xúc xắc
         */
        public int getTotalRolls() {
            return totalRolls;
        }

        /**
         * Tính trung bình giá trị của các lần tung xúc xắc.
         * @return Trung bình giá trị
         */
        public double getAverageValue() {
            return totalRolls == 0 ? 0 : (double) totalValue / totalRolls;
        }

        /**
         * Hiển thị thống kê.
         * @return Chuỗi thống kê
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Thống kê kết quả tung xúc xắc:\n");
            sb.append("Tổng số lần tung: ").append(totalRolls).append("\n");
            sb.append("Trung bình giá trị: ").append(getAverageValue()).append("\n");
            sb.append("Tần suất xuất hiện của các giá trị: ").append(frequency).append("\n");
            return sb.toString();
        }
    }
}
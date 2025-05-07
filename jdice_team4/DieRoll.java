import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * DieRoll: Class mô phỏng việc tung xúc xắc với số lượng và phần thưởng tùy chọn.
 */
public class DieRoll {
    private final int ndice;
    private final int nsides;
    private final int bonus;

    /**
     * Constructor để khởi tạo một đối tượng xúc xắc.
     * @param ndice Số lượng xúc xắc
     * @param nsides Số mặt trên mỗi xúc xắc
     * @param bonus Giá trị cộng thêm vào kết quả cuối cùng
     * @throws IllegalArgumentException nếu đầu vào không hợp lệ
     */
    public DieRoll(int ndice, int nsides, int bonus) {
        // Kiểm tra đầu vào và hợp lý hóa
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
     * Tung xúc xắc và trả về kết quả.
     * @return Đối tượng RollResult chứa kết quả tung và tổng điểm
     */
    public RollResult makeRoll() {
        RollResult result = new RollResult(bonus);
        
        // Dùng IntStream để tối ưu hóa việc sinh kết quả tung xúc xắc
        IntStream.range(0, ndice).forEach(i -> result.addResult(ThreadLocalRandom.current().nextInt(1, nsides + 1)));
        
        return result;
    }

    /**
     * Lấy tổng điểm của tất cả các xúc xắc.
     * @return Tổng điểm của tất cả các xúc xắc cộng với phần thưởng
     */
    public int getTotalRoll() {
        return IntStream.range(0, ndice).map(i -> ThreadLocalRandom.current().nextInt(1, nsides + 1)).sum() + bonus;
    }

    /**
     * Tính trung bình của tất cả các kết quả tung xúc xắc.
     * @return Trung bình điểm của tất cả các xúc xắc cộng với phần thưởng
     */
    public double getAverageRoll() {
        return (double) getTotalRoll() / ndice;
    }

    @Override
    public String toString() {
        // Sử dụng String.format để tạo chuỗi dễ đọc hơn
        return String.format("%dd%d%s", ndice, nsides, (bonus != 0 ? (bonus > 0 ? "+" : "") + bonus : ""));
    }
}
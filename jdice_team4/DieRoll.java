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
        validateInput(ndice, nsides);
        this.ndice = ndice;
        this.nsides = nsides;
        this.bonus = bonus;
    }

    /**
     * Phương thức kiểm tra tính hợp lệ của đầu vào.
     * @param ndice Số lượng xúc xắc
     * @param nsides Số mặt của mỗi xúc xắc
     * @throws IllegalArgumentException nếu các giá trị không hợp lệ
     */
    private void validateInput(int ndice, int nsides) {
        if (ndice <= 0 || nsides <= 1) {
            throw new IllegalArgumentException("Số xúc xắc phải > 0 và số mặt phải > 1");
        }
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
        return makeRoll().getTotal() + bonus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ndice).append("d").append(nsides);
        
        if (bonus != 0) {
            sb.append(bonus > 0 ? "+" : "").append(bonus);
        }
        
        return sb.toString();
    }
}
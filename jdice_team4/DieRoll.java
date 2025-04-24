import java.util.concurrent.ThreadLocalRandom;

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
        if (ndice <= 0 || nsides <= 1) {
            throw new IllegalArgumentException("Số xúc xắc phải > 0 và số mặt > 1");
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
        for (int i = 0; i < ndice; i++) {
            int roll = ThreadLocalRandom.current().nextInt(1, nsides + 1);
            result.addResult(roll);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ndice).append("d").append(nsides);
        if (bonus > 0) {
            sb.append("+").append(bonus);
        } else if (bonus < 0) {
            sb.append(bonus); // bonus âm đã bao gồm dấu '-'
        }
        return sb.toString();
    }
}
/**
 * Represents the sum of two DieRoll operations.
 * Ensures that both rolls are valid and combines their results.
 * 
 * JDice: Java Dice Rolling Program
 * Licensed under GNU General Public License v2 or later.
 * 
 * @author Andrew D. Hilton
 */
public class DiceSum {

    private final DieRoll firstRoll;
    private final DieRoll secondRoll;

    /**
     * Constructs a DiceSum with two DieRolls.
     *
     * @param firstRoll the first die roll
     * @param secondRoll the second die roll
     * @throws IllegalArgumentException if either roll is null
     */
    public DiceSum(DieRoll firstRoll, DieRoll secondRoll) {
        if (firstRoll == null || secondRoll == null) {
            throw new IllegalArgumentException("DieRoll instances cannot be null.");
        }
        this.firstRoll = firstRoll;
        this.secondRoll = secondRoll;
    }

    /**
     * Executes both DieRolls and combines their results.
     *
     * @return the combined RollResult
     */
    public RollResult makeRoll() {
        return firstRoll.makeRoll().andThen(secondRoll.makeRoll());
    }

    /**
     * Returns a string representation of the DiceSum.
     *
     * @return formatted string with both die rolls
     */
    @Override
    public String toString() {
        return String.format("DiceSum[firstRoll=%s, secondRoll=%s]", firstRoll, secondRoll);
    }
}

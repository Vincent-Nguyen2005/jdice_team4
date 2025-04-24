import java.util.*;
/*
JDice: Java Dice Rolling Program
Copyright (C) 2006 Andrew D. Hilton  (adhilton@cis.upenn.edu)


This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

class DieRoll {

    protected int numberOfDice;
    protected int sides;
    protected int bonus;
    private static final Random rand = new Random();

    public DieRoll(int numberOfDice, int sides, int bonus) {
        this.numberOfDice = numberOfDice;
        this.sides = sides;
        this.bonus = bonus;
    }

    // Trả về kết quả tung xúc xắc
    public int makeRoll() {
        int result = bonus;
        for (int i = 0; i < numberOfDice; i++) {
            result += rand.nextInt(sides) + 1; // random từ 1 đến sides
        }
        return result;
    }

    public String toString() {
        return numberOfDice + "d" + sides + ((bonus >= 0) ? "+" : "") + bonus;
    }
}

class DiceSum extends DieRoll {

    private DieRoll a, b;

    public DiceSum(DieRoll a, DieRoll b) {
        super(0, 0, 0); // dummy values
        this.a = a;
        this.b = b;
    }

    @Override
    public int makeRoll() {
        return a.makeRoll() + b.makeRoll();
    }

    public String toString() {
        return "(" + a.toString() + " & " + b.toString() + ")";
    }
}

public class DiceParser {

    // [REFACTOR] Tối ưu class xử lý chuỗi: gom getInt() và readInt()
    private static class StringStream {
        StringBuffer buff;

        public StringStream(String s) {
            buff = new StringBuffer(s);
        }

        // Loại bỏ khoảng trắng đầu chuỗi
        private void munchWhiteSpace() {
            int index = 0;
            while (index < buff.length() && Character.isWhitespace(buff.charAt(index))) {
                index++;
            }
            buff.delete(0, index);
        }

        public boolean isEmpty() {
            munchWhiteSpace();
            return buff.length() == 0;
        }

        // [REFACTOR] Hợp nhất getInt() và readInt()
        public Integer getInt() {
            munchWhiteSpace();
            int index = 0;
            while (index < buff.length() && Character.isDigit(buff.charAt(index))) {
                index++;
            }
            if (index == 0) return null;
            try {
                int value = Integer.parseInt(buff.substring(0, index));
                buff.delete(0, index);
                return value;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public Integer readInt() {
            return getInt();
        }

        public Integer readSgnInt() {
            munchWhiteSpace();
            StringStream saved = save();

            if (checkAndEat("+")) {
                Integer val = readInt();
                if (val != null) return val;
                restore(saved);
                return null;
            }
            if (checkAndEat("-")) {
                Integer val = readInt();
                if (val != null) return -val;
                restore(saved);
                return null;
            }
            return readInt();
        }

        public boolean checkAndEat(String s) {
            munchWhiteSpace();
            if (buff.indexOf(s) == 0) {
                buff.delete(0, s.length());
                return true;
            }
            return false;
        }

        public StringStream save() {
            return new StringStream(buff.toString());
        }

        public void restore(StringStream ss) {
            this.buff = new StringBuffer(ss.buff);
        }

        public String toString() {
            return buff.toString();
        }
    }

    public static Vector<DieRoll> parseRoll(String s) {
        StringStream ss = new StringStream(s.toLowerCase());
        Vector<DieRoll> v = parseRollInner(ss, new Vector<>());
        return ss.isEmpty() ? v : null;
    }

    private static Vector<DieRoll> parseRollInner(StringStream ss, Vector<DieRoll> v) {
        Vector<DieRoll> r = parseXDice(ss);
        if (r == null) {
            return null;
        }
        v.addAll(r);
        if (ss.checkAndEat(";")) {
            return parseRollInner(ss, v);
        }
        return v;
    }

    private static Vector<DieRoll> parseXDice(StringStream ss) {
        StringStream saved = ss.save();
        Integer x = ss.getInt();
        int num = 1;

        if (x != null && ss.checkAndEat("x")) {
            num = x;
        } else {
            ss.restore(saved);
        }

        DieRoll dr = parseDice(ss);
        if (dr == null) {
            return null;
        }

        Vector<DieRoll> result = new Vector<>();
        for (int i = 0; i < num; i++) {
            result.add(dr);
        }
        return result;
    }

    private static DieRoll parseDice(StringStream ss) {
        return parseDTail(parseDiceInner(ss), ss);
    }

    private static DieRoll parseDiceInner(StringStream ss) {
        Integer first = ss.getInt();
        int ndice = (first != null) ? first : 1;

        if (!ss.checkAndEat("d")) {
            return null;
        }

        Integer dsides = ss.getInt();
        if (dsides == null) {
            return null;
        }

        Integer bonus = ss.readSgnInt();
        return new DieRoll(ndice, dsides, bonus != null ? bonus : 0);
    }

    private static DieRoll parseDTail(DieRoll d1, StringStream ss) {
        if (d1 == null) {
            return null;
        }
        if (ss.checkAndEat("&")) {
            DieRoll d2 = parseDice(ss);
            return parseDTail(new DiceSum(d1, d2), ss);
        }
        return d1;
    }

    // [CHỨC NĂNG MỚI] Ghi log nếu cú pháp không hợp lệ
    private static void test(String s) {
        Vector<DieRoll> v = parseRoll(s);
        if (v == null) {
            System.out.println("Failure: " + s);
            System.err.println("[LOG] Invalid syntax: " + s);
        } else {
            System.out.println("Results for: " + s);
            for (DieRoll dr : v) {
                System.out.println(dr + ": " + dr.makeRoll());
            }
        }
    }

    public static void main(String[] args) {
        test("d6");
        test("2d6");
        test("d6+5");
        test("4X3d8-5");
        test("12d10+5 & 4d6+2");
        test("d6 ; 2d4+3");
        test("4d6+3 ; 8d12 -15 ; 9d10 & 3d6 & 4d12 +17");
        test("4d6 + xyzzy"); // Invalid
        test("hi");          // Invalid
        test("4d4d4");       // Invalid
    }
}


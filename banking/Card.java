package banking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Card {
    private final static String start = "400000";
    private final long numberLimit = 1_000_000_000L;
    private static List<String> numberList = new ArrayList<>();
    private int balance;
    private String number;
    private String pin;

    public Card() {
        generateNumberAndPin();
    }

    public Card(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    private void generateNumberAndPin() {
        Random random = new Random(System.currentTimeMillis());
        long numberPart = Math.abs(random.nextLong()) % numberLimit;
        if (numberPart < 100_000_000L) {
            numberPart += 100_000_000L;
        }
        String startNumber = start + numberPart;
        applyLuhn(startNumber);
         while (numberList.contains(number) && !number.endsWith(String.valueOf(applyLuhn(startNumber)))) {
             startNumber = start + random.nextLong() % numberLimit;
             applyLuhn(startNumber);
         }

         int intPin = Math.abs(random.nextInt()) % 10000;
         if (intPin < 1000) {
             intPin += 1000;
         }
         pin = String.valueOf(intPin);
    }

    public int applyLuhn(String startNumber) {
        int[] intArr = startNumber.chars().map(ch -> Character.digit(ch, 10)).toArray();
        int lastIndex = intArr.length;
        int checksum = 0;
        //System.out.println("    " + number);

        for (int i = 0; i < lastIndex; i++ ) {
            if((i + 1) % 2 != 0) {
                intArr[i] *= 2;
            }
        }

        //System.out.println("*2: " +
        //        Arrays.stream(intArr).mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(" ")));

        for (int i = 0; i < lastIndex; i++) {
            if (intArr[i] > 9) {
                intArr[i] -= 9;
            }
        }

        //System.out.println("-9: " +
         //       Arrays.stream(intArr).mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(" ")));

        for (int i = 0; i < lastIndex; i++) {
            checksum += intArr[i];
        }
        //System.out.println();
        //System.out.println("raw checksum: " + checksum);
        checksum = checksum %10 == 0 ? 0 : 10 - checksum % 10;
        number = startNumber + checksum;
        //System.out.println("checksum: " + checksum);
        return checksum;
        //System.out.println(number);
        //
        //return number + checksum;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public static void main(String[] args) {
        Card card = new Card("4000003957282439", "1111");
        card.applyLuhn("400000395728243");
    }
}

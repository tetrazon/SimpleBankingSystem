package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
    private final static String start = "400000";
    private final long numberLimit = 1_000_000_000L;
    private static List<String> numberList = new ArrayList<>();
    private static int luhnLastIndex = 14;//1-15 digits to calculate 16th digit
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

    public Card(String number, String pin, int balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    private void generateNumberAndPin() {
        Random random = new Random(System.currentTimeMillis());
        long numberPart = Math.abs(random.nextLong()) % numberLimit;
        if (numberPart < 100_000_000L) {
            numberPart += 100_000_000L;
        }
        String startNumber = start + numberPart;
        number = applyLuhn(startNumber);
         while (numberList.contains(number) && !number.equals(applyLuhn(number))) { //number.charAt(number.length()-1) != applyLuhn(startNumber).charAt(startNumber.length()-1)
             startNumber = start + random.nextLong() % numberLimit;
             applyLuhn(startNumber);
         }

         int intPin = Math.abs(random.nextInt()) % 10000;
         if (intPin < 1000) {
             intPin += 1000;
         }
         pin = String.valueOf(intPin);
    }

    public static String applyLuhn(String startNumber) {
        if (startNumber.length() == 16){
            startNumber = startNumber.substring(0, startNumber.length() -1);
        }
        int[] intArr = startNumber.chars().map(ch -> Character.digit(ch, 10)).toArray();
        int checksum = 0;
        //System.out.println("    " + number);
        for (int i = 0; i < luhnLastIndex; i++ ) {
            if((i + 1) % 2 != 0) {
                intArr[i] *= 2;
            }
        }

        //System.out.println("*2: " +
        //        Arrays.stream(intArr).mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(" ")));

        for (int i = 0; i < luhnLastIndex; i++) {
            if (intArr[i] > 9) {
                intArr[i] -= 9;
            }
        }

        //System.out.println("-9: " +
         //       Arrays.stream(intArr).mapToObj(i -> String.valueOf(i)).collect(Collectors.joining(" ")));

        for (int i = 0; i < luhnLastIndex; i++) {
            checksum += intArr[i];
        }
        checksum = checksum %10 == 0 ? 0 : 10 - checksum % 10;
        //number = startNumber + checksum;
        return startNumber + checksum;
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
}

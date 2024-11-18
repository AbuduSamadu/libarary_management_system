package abudu.lms.library.utils;

import java.util.UUID;

public class ISBNGenerator {

    public static String generateISBN() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        return uuid + calculateCheckDigit(uuid);
    }

    private static int calculateCheckDigit(String isbn) {
        int sum = 0;
        for (int i = 0; i < isbn.length(); i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int mod = sum % 10;
        return (mod == 0) ? 0 : 10 - mod;
    }

    public static void main(String[] args) {
        System.out.println("Generated ISBN: " + generateISBN());
    }
}
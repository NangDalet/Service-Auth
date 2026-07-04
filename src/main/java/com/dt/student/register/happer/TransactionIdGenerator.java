package com.dt.student.register.happer;

import java.security.SecureRandom;

public class TransactionIdGenerator {

    // Characters to use in ID
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Length of the random part
    private static final int ID_LENGTH = 10;

    // SecureRandom for better randomness
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a transaction ID with prefix STD + 10 random alphanumeric characters
     * Example: STD4G7H2K9Q1
     */
    public static String generateTranId() {
        StringBuilder sb = new StringBuilder("STD");
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    // Test main method
    public static void main(String[] args) {
        System.out.println("Generated Transaction ID: " + generateTranId());
    }
}

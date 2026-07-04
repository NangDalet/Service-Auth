package com.dt.student.register.happer;

import java.time.LocalDate;

public class GenerateCode {
    public static String incrementNumericPart(String input) {
        // Use a regular expression to split the input into prefix and numeric parts
        String prefix = input.replaceAll("\\d+$", ""); // Extract prefix part
        String numericPart = input.substring(prefix.length()); // Extract numeric part

        // Convert numeric part to an integer
        int number = Integer.parseInt(numericPart);

        // Increment the number
        number += 1;

        // Determine the number of digits in the original numeric part
        int numericPartLength = numericPart.length();

        // Format the incremented number with leading zeros
        String incrementedNumericPart = String.format("%0" + numericPartLength + "d", number);

        // Concatenate the prefix and the incremented numeric part
        return prefix + incrementedNumericPart;
    }

    public static String generateReference(String input) {
        LocalDate currentDate = LocalDate.now();
        String yearLastTwoDigits = String.valueOf(currentDate.getYear()).substring(2);
        String result = yearLastTwoDigits + input + "000001";
        return result;
    }
}

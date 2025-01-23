package stringcalculator;

import java.util.Arrays;

public class StringCalculator {

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        return Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt)
                .sum();
    }
}

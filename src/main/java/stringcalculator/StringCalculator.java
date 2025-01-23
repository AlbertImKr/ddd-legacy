package stringcalculator;

import java.util.Arrays;

public class StringCalculator {

    private static final String DELIMITER = "[,:]";

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        return Arrays.stream(input.split(DELIMITER))
                .mapToInt(Integer::parseInt)
                .sum();
    }
}

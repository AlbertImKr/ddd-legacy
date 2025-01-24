package stringcalculator;

import java.util.Arrays;

public class StringCalculator {

    private static final StringCalculatorDelimiter DEFAULT_DELIMITER = new StringCalculatorDelimiter(",|:");
    private static final String CUSTOM_DELIMITER_PREFIX = "//";
    private static final String CUSTOM_DELIMITER_SUFFIX = "\n";
    private static final String CUSTOM_DELIMITER_REGEX = "^//.*\n.+";

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        var realInput = input;
        var delimiter = DEFAULT_DELIMITER;
        if (RegexCache.matches(CUSTOM_DELIMITER_REGEX, input)) {
            delimiter = DEFAULT_DELIMITER.add(getCustomDelimiter(input));
            var realInputStart = input.indexOf(CUSTOM_DELIMITER_SUFFIX) + 1;
            realInput = input.substring(realInputStart);
        }
        return Arrays.stream(realInput.split(delimiter.value()))
                .map(PositiveNumber::of)
                .mapToInt(PositiveNumber::value)
                .sum();
    }

    public static String getCustomDelimiter(String input) {
        return input.substring(CUSTOM_DELIMITER_PREFIX.length(), input.indexOf(CUSTOM_DELIMITER_SUFFIX));
    }
}

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
        if (isNullOrEmpty(input)) {
            return 0;
        }
        var body = extractBody(input);
        var delimiter = extractDelimiter(input);
        return sumNumbers(body, delimiter);
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }

    public static String extractBody(String input) {
        if (hasCustomDelimiter(input)) {
            int realInputStart = input.indexOf(CUSTOM_DELIMITER_SUFFIX) + 1;
            return input.substring(realInputStart);
        }
        return input;
    }

    public static StringCalculatorDelimiter extractDelimiter(String input) {
        if (hasCustomDelimiter(input)) {
            return DEFAULT_DELIMITER.add(getCustomDelimiter(input));
        }
        return DEFAULT_DELIMITER;
    }

    public static String getCustomDelimiter(String input) {
        return input.substring(CUSTOM_DELIMITER_PREFIX.length(), input.indexOf(CUSTOM_DELIMITER_SUFFIX));
    }

    public static int sumNumbers(String realInput, StringCalculatorDelimiter delimiter) {
        return Arrays.stream(realInput.split(delimiter.value()))
                .map(PositiveNumber::of)
                .mapToInt(PositiveNumber::value)
                .sum();
    }

    public static boolean hasCustomDelimiter(String input) {
        return RegexCache.matches(CUSTOM_DELIMITER_REGEX, input);
    }
}

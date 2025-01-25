package stringcalculator;

import java.util.Arrays;

public class StringCalculator {

    private static final StringCalculatorDelimiter BASE_DELIMITER = new StringCalculatorDelimiter(",|:");
    private static final String CUSTOM_DELIMITER_HEADER = "//";
    private static final String CUSTOM_DELIMITER_SUFFIX = "\n";
    private static final String CUSTOM_DELIMITER_INPUT_PATTERN = "^//.*\n.+";

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        if (StringHelper.isNullOrEmpty(input)) {
            return 0;
        }
        var body = extractNumbersData(input);
        var delimiter = extractDelimiterObject(input);
        return sumDelimitedNumbers(body, delimiter);
    }

    public static String extractNumbersData(String input) {
        if (containsCustomDelimiter(input)) {
            int realInputStart = input.indexOf(CUSTOM_DELIMITER_SUFFIX) + 1;
            return input.substring(realInputStart);
        }
        return input;
    }

    public static StringCalculatorDelimiter extractDelimiterObject(String input) {
        if (containsCustomDelimiter(input)) {
            return BASE_DELIMITER.add(extractCustomDelimiter(input));
        }
        return BASE_DELIMITER;
    }

    public static String extractCustomDelimiter(String input) {
        return input.substring(CUSTOM_DELIMITER_HEADER.length(), input.indexOf(CUSTOM_DELIMITER_SUFFIX));
    }

    public static int sumDelimitedNumbers(String realInput, StringCalculatorDelimiter delimiter) {
        return Arrays.stream(realInput.split(delimiter.value()))
                .map(PositiveNumber::of)
                .mapToInt(PositiveNumber::value)
                .sum();
    }

    public static boolean containsCustomDelimiter(String input) {
        return RegexCache.matches(CUSTOM_DELIMITER_INPUT_PATTERN, input);
    }
}

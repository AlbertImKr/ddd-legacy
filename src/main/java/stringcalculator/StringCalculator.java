package stringcalculator;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringCalculator {

    public static final String NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE = "음수 또는 숫자가 아닌 값은 입력할 수 없습니다.";
    private static final String DEFAULT_DELIMITER = "[,:]";
    private static final String CUSTOM_DELIMITER_PREFIX = "//";
    private static final String CUSTOM_DELIMITER_SUFFIX = "\n";
    private static final String CUSTOM_DELIMITER_REGEX = "^//.*\n.+";
    private static final String POSITIVE_NUMBER_REGEX = "^\\d*$";

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        var realInput = input;
        var delimiter = DEFAULT_DELIMITER;
        if (Pattern.matches(CUSTOM_DELIMITER_REGEX, input)) {
            delimiter = DEFAULT_DELIMITER + "|" + "[" + getCustomDelimiter(input) + "]";
            realInput = input.substring(input.indexOf(CUSTOM_DELIMITER_SUFFIX) + 1);
        }
        return Arrays.stream(realInput.split(delimiter))
                .mapToInt(StringCalculator::parsePositiveNumber)
                .sum();
    }

    public static String getCustomDelimiter(String input) {
        return input.substring(CUSTOM_DELIMITER_PREFIX.length(), input.indexOf(CUSTOM_DELIMITER_SUFFIX));
    }

    public static int parsePositiveNumber(String number) {
        if (!Pattern.matches(POSITIVE_NUMBER_REGEX, number)) {
            throw new RuntimeException(NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE);
        }
        return Integer.parseInt(number);
    }
}

package stringcalculator;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringCalculator {

    private static final String DEFAULT_DELIMITER = "[,:]";
    private static final String CUSTOM_DELIMITER_PREFIX = "//";
    private static final String CUSTOM_DELIMITER_SUFFIX = "\n";
    private static final String CUSTOM_DELIMITER_REGEX = "^//.*\n.+";

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        if (Pattern.matches(CUSTOM_DELIMITER_REGEX, input)) {
            var delimiter = DEFAULT_DELIMITER + "|" + getCustomDelimiter(input);
            var realInput = input.substring(input.indexOf(CUSTOM_DELIMITER_SUFFIX) + 1);
            return Arrays.stream(realInput.split(delimiter))
                    .mapToInt(Integer::parseInt)
                    .sum();
        }
        return Arrays.stream(input.split(DEFAULT_DELIMITER))
                .mapToInt(Integer::parseInt)
                .sum();
    }

    public static String getCustomDelimiter(String input) {
        return input.substring(CUSTOM_DELIMITER_PREFIX.length(), input.indexOf(CUSTOM_DELIMITER_SUFFIX));
    }
}

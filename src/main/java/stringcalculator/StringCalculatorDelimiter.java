package stringcalculator;

public record StringCalculatorDelimiter(String value) {

    private static final StringCalculatorDelimiter BASE_DELIMITER = new StringCalculatorDelimiter(",|:");
    private static final String CUSTOM_DELIMITER_HEADER = "//";
    private static final String CUSTOM_DELIMITER_SUFFIX = "\n";
    private static final String CUSTOM_DELIMITER_INPUT_PATTERN = "^//.*\n.+";

    public static boolean containsCustomDelimiter(String input) {
        return RegexCache.matches(CUSTOM_DELIMITER_INPUT_PATTERN, input);
    }

    public static String removeCustomDelimiter(String input) {
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

    public StringCalculatorDelimiter add(String newDelimiter) {
        return new StringCalculatorDelimiter(value + "|" + "[" + newDelimiter + "]");
    }
}

package stringcalculator;

import java.util.Arrays;

public class StringCalculator {

    private StringCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static int add(String input) {
        if (StringHelper.isNullOrEmpty(input)) {
            return 0;
        }
        var body = StringCalculatorDelimiter.removeCustomDelimiter(input);
        var delimiter = StringCalculatorDelimiter.extractDelimiterObject(input);
        return sumDelimitedNumbersData(body, delimiter);
    }

    public static int sumDelimitedNumbersData(String realInput, StringCalculatorDelimiter delimiter) {
        return Arrays.stream(realInput.split(delimiter.value()))
                .map(PositiveNumber::of)
                .mapToInt(PositiveNumber::value)
                .sum();
    }
}

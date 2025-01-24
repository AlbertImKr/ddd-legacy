package stringcalculator;

public class PositiveNumberParser {

    public static final String NEGATIVE_NUMBER_ERROR_MESSAGE = "음수는 입력할 수 없습니다.: (-%d)";
    public static final String NON_NUMBER_ERROR_MESSAGE = "숫자가 아닌 값은 입력할 수 없습니다.: (%s)";

    private PositiveNumberParser() {
        throw new IllegalStateException("Utility class");
    }

    public static int parsePositiveNumber(String number) {
        try {
            var parsedNumber = Integer.parseInt(number);
            if (parsedNumber < 0) {
                throw new RuntimeException(String.format(NEGATIVE_NUMBER_ERROR_MESSAGE, parsedNumber));
            }
            return parsedNumber;
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format(NON_NUMBER_ERROR_MESSAGE, number));
        }
    }
}

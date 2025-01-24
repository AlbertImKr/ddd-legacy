package stringcalculator;

public record PositiveNumber(int value) {

    public static final String NEGATIVE_NUMBER_ERROR_MESSAGE = "음수는 입력할 수 없습니다.: (-%d)";
    public static final String NON_NUMBER_ERROR_MESSAGE = "숫자가 아닌 값은 입력할 수 없습니다.: (%s)";

    public PositiveNumber {
        if (value < 0) {
            throw new RuntimeException(String.format(NEGATIVE_NUMBER_ERROR_MESSAGE, value));
        }
    }

    public static PositiveNumber of(String number) {
        try {
            return new PositiveNumber(Integer.parseInt(number));
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format(NON_NUMBER_ERROR_MESSAGE, number));
        }
    }
}

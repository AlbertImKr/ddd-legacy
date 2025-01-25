package stringcalculator;

public class StringHelper {

    private StringHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }
}

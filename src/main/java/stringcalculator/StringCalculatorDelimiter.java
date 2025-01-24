package stringcalculator;

public record StringCalculatorDelimiter(String value) {

    public StringCalculatorDelimiter add(String newDelimiter) {
        return new StringCalculatorDelimiter(value + "|" + "[" + newDelimiter + "]");
    }
}

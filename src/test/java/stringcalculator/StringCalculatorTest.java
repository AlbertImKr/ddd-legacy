package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringCalculatorTest {

    @DisplayName("모두 구분자로 형성한 문자열을 입력할 경우, 0을 반환한다.")
    @Test
    void if_input_string_contains_only_delimiters_then_return_zero() {
        // given
        String input = ",:";

        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isZero();
    }

    @DisplayName("커스텀 구분자는 '//'와 '\\n' 사이에 위치하며 커스텀 구분자를 추출한다.")
    @ParameterizedTest
    @ValueSource(strings = {";", "&", "."})
    void extract_custom_delimiter_between_double_slash_and_new_line(String delimiter) {
        // given
        String input = "//" + delimiter + "\n1" + delimiter + "2" + delimiter + "3";

        // when
        String customDelimiter = StringCalculator.extractCustomDelimiter(input);

        // then
        assertThat(customDelimiter).isEqualTo(delimiter);
    }

    @DisplayName("쉼표(,)와 콜론(:) 및 커스텀 구분자 기준으로 숫자를 분리하여 덧셈을 수행한다.")
    @ParameterizedTest
    @ValueSource(strings = {"//;\n1;2;3", "1,2:3", "1:2:3", "//&\n1&2:3", "//.\n1.2.3"})
    void split_numbers_by_comma_colon_and_custom_delimiter_and_sum(String input) {
        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

    @DisplayName("빈 문자열 또는 null을 입력할 경우, 0을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void if_input_string_is_empty_or_null_then_return_zero(String input) {
        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isZero();
    }

    @DisplayName("숫자 하나를 입력할 경우, 해당 숫자를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void if_input_string_contains_one_number_then_return_number(String input) {
        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(Integer.parseInt(input));
    }

    @DisplayName("입력한 문자열에서 숫자 데이터를 추출한다.")
    @ParameterizedTest
    @MethodSource("provideInputStringOfBody")
    void extract_numbers_data_from_input_string(String input, String expected) {
        // when
        String result = StringCalculator.extractNumbersData(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> provideInputStringOfBody() {
        return Stream.of(
                Arguments.of("1,2,3", "1,2,3"),
                Arguments.of("//;\n1;2;3", "1;2;3"),
                Arguments.of("1:2:3", "1:2:3"),
                Arguments.of("//&\n1&2:3", "1&2:3"),
                Arguments.of("//.\n1.2.3", "1.2.3")
        );
    }

    @DisplayName("입력한 문자열에서 커스텀 구분자가 포함되어 있는지 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"//;\n1;2;3", "//&\n1&2:3", "//.\n1.2.3"})
    void contains_custom_delimiter(String input) {
        // when
        boolean result = StringCalculator.containsCustomDelimiter(input);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("입력한 문자열에서 커스텀 구분자가 포함되어 있지 않은 경우, false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1,2,3", "1:2:3", "1,2:3"})
    void does_not_contain_custom_delimiter(String input) {
        // when
        boolean result = StringCalculator.containsCustomDelimiter(input);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("입력한 문자열에서 구분자 객체를 추출한다.")
    @ParameterizedTest
    @MethodSource("provideInputStringOfDefaultDelimiter")
    void extract_delimiter_object_from_input_string(String input, String expected) {
        // when
        StringCalculatorDelimiter result = StringCalculator.extractDelimiterObject(input);

        // then
        assertThat(result.value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideInputStringOfDefaultDelimiter() {
        return Stream.of(
                Arguments.of("1,2,3", ",|:"),
                Arguments.of("//;\n1;2;3", ",|:|[;]"),
                Arguments.of("1:2:3", ",|:"),
                Arguments.of("//&\n1&2:3", ",|:|[&]"),
                Arguments.of("//.\n1.2.3", ",|:|[.]")
        );
    }

    @DisplayName("숫자로 이루어진 문자열을 구분자로 나누어 합산한다.")
    @ParameterizedTest
    @MethodSource("provideInputStringOfSumNumbers")
    void sum_numbers_by_splitting_string_with_delimiter(String input, String delimiterStr, int expected) {
        // given
        StringCalculatorDelimiter delimiter = new StringCalculatorDelimiter(delimiterStr);

        // when
        int result = StringCalculator.sumDelimitedNumbers(input, delimiter);

        // then
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> provideInputStringOfSumNumbers() {
        return Stream.of(
                Arguments.of("1,2,3", ",|:", 6),
                Arguments.of("1;2;3", ",|:|[;]", 6),
                Arguments.of("1:2:3", ",|:", 6),
                Arguments.of("1&2:3", ",|:|[&]", 6),
                Arguments.of("1.2.3", ",|:|[.]", 6)
        );
    }
}

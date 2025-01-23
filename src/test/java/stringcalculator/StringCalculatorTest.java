package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    @ValueSource(strings = { ";", "&", "." })
    void extract_custom_delimiter_between_double_slash_and_new_line(String delimiter) {
        // given
        String input = "//" + delimiter + "\n1" + delimiter + "2" + delimiter + "3";

        // when
        String customDelimiter = StringCalculator.getCustomDelimiter(input);

        // then
        assertThat(customDelimiter).isEqualTo(delimiter);
    }

    @DisplayName("쉼표(,)와 콜론(:) 및 커스텀 구분자 기준으로 숫자를 분리하여 덧셈을 수행한다.")
    @ParameterizedTest
    @ValueSource(strings = { "//;\n1;2;3","1,2:3", "1:2:3", "//&\n1&2:3","//.\n1.2.3" })
    void split_numbers_by_comma_colon_and_custom_delimiter_and_sum(String input) {
        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

    @DisplayName("숫자 이외 및 음수의 경우 모두 RuntimeException이 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = { "1,2,-3", "1,2,a", "1,2,-3,a","//;\n1;2;-3","//;\n1;2;a","//;\n1;2;-3;a" })
    void if_input_string_contains_non_number_and_negative_number_then_throw_exception(String input) {
        // when, then
        assertThatThrownBy(() -> StringCalculator.add(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(StringCalculator.NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE);
    }

}

package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

    @DisplayName("쉼표(,)을 구분자로 하는 문자열을 입력할 경우, 쉼표를 기준으로 숫자를 분리하여 덧셈을 수행한다.")
    @Test
    void if_input_string_contains_comma_then_sum_numbers() {
        // given
        String input = "1,2,3";

        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

    @DisplayName("쉼표(,)와 콜론(:)을 구분자로 하는 문자열을 입력할 경우, 쉼표와 콜론을 기준으로 숫자를 분리하여 덧셈을 수행한다.")
    @Test
    void if_input_string_contains_comma_and_colon_then_sum_numbers() {
        // given
        String input = "1,2:3";

        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

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
    @Test
    void extract_custom_delimiter_between_double_slash_and_new_line() {
        // given
        String input = "//;\n1;2;3";

        // when
        String customDelimiter = StringCalculator.getCustomDelimiter(input);

        // then
        assertThat(customDelimiter).isEqualTo(";");
    }

    @DisplayName("커스텀 구분자를 사용하는 문자열을 입력할 경우, 커스텀 구분자도 포함하여 덧셈을 수행한다.")
    @Test
    void if_input_string_contains_custom_delimiter_then_sum_numbers() {
        // given
        String input = "//;\n1;2;3:4";

        // when
        int result = StringCalculator.add(input);

        // then
        assertThat(result).isEqualTo(10);
    }

    @DisplayName("숫자 이외의 값을 입력할 경우, RuntimeException이 발생한다.")
    @Test
    void if_input_string_contains_non_number_then_throw_exception() {
        // given
        String input = "1,2,a";

        // when, then
        assertThatThrownBy(() -> StringCalculator.add(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(StringCalculator.NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE);
    }

    @DisplayName("음수를 입력할 경우, RuntimeException이 발생한다.")
    @Test
    void if_input_string_contains_negative_number_then_throw_exception() {
        // given
        String input = "1,2,-3";

        // when, then
        assertThatThrownBy(() -> StringCalculator.add(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(StringCalculator.NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE);
    }

    @DisplayName("숫자 이외 및 음수의 경우 모두 RuntimeException이 발생한다.")
    @Test
    void if_input_string_contains_non_number_and_negative_number_then_throw_exception() {
        // given
        String input = "1,2,-3,a";

        // when, then
        assertThatThrownBy(() -> StringCalculator.add(input))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(StringCalculator.NEGATIVE_NUMBER_AND_NON_NUMBER_ERROR_MESSAGE);
    }

}

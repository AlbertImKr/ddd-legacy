package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

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
}

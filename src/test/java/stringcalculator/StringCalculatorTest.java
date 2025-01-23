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
}

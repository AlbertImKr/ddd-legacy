package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositiveNumberParserTest {

    @DisplayName("음수가 아닌 정수 문자열을 입력할 경우, 해당 정수를 반환한다.")
    @Test
    void parse_positive_number() {
        // given
        String number = "1";

        // when
        int result = PositiveNumberParser.parsePositiveNumber(number);

        // then
        assertThat(result).isEqualTo(1);
    }

    @DisplayName("음수 정수 문자열을 입력할 경우, RuntimeException이 발생한다.")
    @Test
    void parse_negative_number() {
        // given
        String number = "-1";

        // when, then
        assertThatThrownBy(() -> PositiveNumberParser.parsePositiveNumber(number))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(String.format(PositiveNumberParser.NEGATIVE_NUMBER_ERROR_MESSAGE, -1));
    }

    @DisplayName("숫자가 아닌 문자열을 입력할 경우, RuntimeException이 발생한다.")
    @Test
    void parse_non_number() {
        // given
        String number = "a";

        // when, then
        assertThatThrownBy(() -> PositiveNumberParser.parsePositiveNumber(number))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(String.format(PositiveNumberParser.NON_NUMBER_ERROR_MESSAGE, "a"));
    }
}

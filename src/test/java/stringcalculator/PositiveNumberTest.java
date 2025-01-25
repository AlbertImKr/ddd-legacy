package stringcalculator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PositiveNumberTest {

    @DisplayName("음수로 PositiveNumber 인스턴스를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3, -4, -5})
    void create_instance_if_value_is_negative_then_throw_exception(int value) {
        // when, then
        assertThatThrownBy(() -> new PositiveNumber(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(PositiveNumber.NEGATIVE_NUMBER_ERROR_MESSAGE, value));
    }

    @DisplayName("숫자가 아닌 문자열로 PositiveNumber 인스턴스를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c", "d", ",", ":", "1a", "a1;", "1:1", "1,1"})
    void create_instance_if_value_is_not_number_then_throw_exception(String value) {
        // when, then
        assertThatThrownBy(() -> PositiveNumber.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format(PositiveNumber.NON_NUMBER_ERROR_MESSAGE, value));
    }
}

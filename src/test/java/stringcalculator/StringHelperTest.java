package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringHelperTest {

    @DisplayName("입력한 문자열이 null 또는 빈 문자열일 경우, true를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void if_input_string_is_null_or_empty_then_return_true(String input) {
        // when
        boolean result = StringHelper.isNullOrEmpty(input);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("입력한 문자열이 null 또는 빈 문자열이 아닐 경우, false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1", "a", " ", "1,2,3"})
    void if_input_string_is_not_null_or_empty_then_return_false(String input) {
        // when
        boolean result = StringHelper.isNullOrEmpty(input);

        // then
        assertThat(result).isFalse();
    }
}

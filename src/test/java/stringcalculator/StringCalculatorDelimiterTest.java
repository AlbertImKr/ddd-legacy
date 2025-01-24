package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringCalculatorDelimiterTest {

    @DisplayName("새로운 구분자를 추가한다.")
    @Test
    void add_new_delimiter() {
        // given
        var delimiter = new StringCalculatorDelimiter(",|:");
        var expected = ",|:|[;]";

        // when
        var newDelimiter = delimiter.add(";");

        // then
        assertThat(newDelimiter.value()).isEqualTo(expected);
    }

}

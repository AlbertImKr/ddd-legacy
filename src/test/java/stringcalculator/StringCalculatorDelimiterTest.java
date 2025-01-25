package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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


    @DisplayName("입력한 문자열에서 숫자 데이터를 추출한다.")
    @ParameterizedTest
    @MethodSource("provideInputStringOfBody")
    void extract_numbers_data_from_input_string(String input, String expected) {
        // when
        String result = StringCalculatorDelimiter.removeCustomDelimiter(input);

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
        boolean result = StringCalculatorDelimiter.containsCustomDelimiter(input);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("입력한 문자열에서 커스텀 구분자가 포함되어 있지 않은 경우, false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1,2,3", "1:2:3", "1,2:3"})
    void does_not_contain_custom_delimiter(String input) {
        // when
        boolean result = StringCalculatorDelimiter.containsCustomDelimiter(input);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("입력한 문자열에서 구분자 객체를 추출한다.")
    @ParameterizedTest
    @MethodSource("provideInputStringOfDefaultDelimiter")
    void extract_delimiter_object_from_input_string(String input, String expected) {
        // when
        StringCalculatorDelimiter result = StringCalculatorDelimiter.extractDelimiterObject(input);

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

    @DisplayName("커스텀 구분자는 '//'와 '\\n' 사이에 위치하며 커스텀 구분자를 추출한다.")
    @ParameterizedTest
    @ValueSource(strings = {";", "&", "."})
    void extract_custom_delimiter_between_double_slash_and_new_line(String delimiter) {
        // given
        String input = "//" + delimiter + "\n1" + delimiter + "2" + delimiter + "3";

        // when
        String customDelimiter = StringCalculatorDelimiter.extractCustomDelimiter(input);

        // then
        assertThat(customDelimiter).isEqualTo(delimiter);
    }

}

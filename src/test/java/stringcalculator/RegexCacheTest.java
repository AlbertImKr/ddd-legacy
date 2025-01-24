package stringcalculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegexCacheTest {

    @DisplayName("Pattern 존재하면 getPattern 메서드는 Pattern을 반환한다.")
    @Test
    void get_pattern_if_pattern_is_not_exist() {
        // given
        String regex = ".*";

        // when
        var actual = RegexCache.getPattern(regex);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("Pattern 존재하면 getPattern 메서드는 동일한 Pattern을 반환한다.")
    @Test
    void get_pattern_if_pattern_is_exist() {
        // given
        String regex = ".*";

        // when
        var actual1 = RegexCache.getPattern(regex);
        var actual2 = RegexCache.getPattern(regex);

        // then
        assertThat(actual1).isSameAs(actual2);
    }

    @DisplayName("matches 메서드는 정규식에 매칭되는 경우 true를 반환한다.")
    @Test
    void matches() {
        // given
        String regex = "a.*";
        String input = "abc";

        // when
        var actual = RegexCache.matches(regex, input);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("matches 메서드는 정규식에 매칭되지 않는 경우 false를 반환한다.")
    @Test
    void not_matches() {
        // given
        String regex = "a.*";
        String input = "bcd";

        // when
        var actual = RegexCache.matches(regex, input);

        // then
        assertThat(actual).isFalse();
    }

}

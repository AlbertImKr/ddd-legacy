package racingcar;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CarTest {

    @DisplayName("자동차의 이름은 5글자를 초과하면 예외가 발생한다.")
    @Test
    void if_car_name_is_over_5_then_throw_exception() {
        // given
        var carName = "abcdef";

        // when,then
        assertThatThrownBy(() -> new Car(carName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자동차 이름은 5글자를 초과할 수 없습니다.");
    }
}

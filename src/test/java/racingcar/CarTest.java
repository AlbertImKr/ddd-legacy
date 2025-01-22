package racingcar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @DisplayName("자동차의 움직이는 조건은 4 이상이면 움직인다.")
    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8, 9})
    void if_car_move_condition_is_over_4_then_move(int condition) {
        // given
        var car = new Car("car");

        // when
        car.move(condition);

        // then
        assertThat(car.getPosition()).isEqualTo(1);
    }

    @DisplayName("자동차의 움직이는 조건은 4 미만이면 멈춘다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void if_car_move_condition_is_under_4_then_stop(int condition) {
        // given
        var car = new Car("car");

        // when
        car.move(condition);

        // then
        assertThat(car.getPosition()).isZero();
    }
}

package racingcar;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("자동차의 움직이는 조건은 true이면 움직인다.")
    @Test
    void if_car_move_condition_is_true_then_move() {
        // given
        var car = new Car("car");

        // when
        car.move(new ForwardStrategy());

        // then
        assertThat(car.getPosition()).isEqualTo(1);
    }

    @DisplayName("자동차의 움직이는 조건은 false이면 멈춘다.")
    @Test
    void if_car_move_condition_is_false_then_stop() {
        // given
        var car = new Car("car");

        // when
        car.move(new StopStrategy());

        // then
        assertThat(car.getPosition()).isZero();
    }
}

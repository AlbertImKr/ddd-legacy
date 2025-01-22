package racingcar;

import java.util.Random;

public class Car {

    private final String name;
    private int position = 0;

    public Car(String name) {
        if (name.length() > 5) {
            throw new IllegalArgumentException("자동차 이름은 5글자를 초과할 수 없습니다.");
        }
        this.name = name;
    }

    public void move(int condition) {
        if (condition >= 4) {
            position++;
        }
    }

    public void move() {
        var condition = new Random().nextInt(0, 10);
        move(condition);
    }

    public int getPosition() {
        return position;
    }
}

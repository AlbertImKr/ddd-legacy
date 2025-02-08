package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("메뉴 아이디 목록으로 메뉴 목록을 조회한다.")
    @Test
    void findAllByIdIn() {
        // given
        var menuGroup = new MenuGroup();
        menuGroup.setId(UUID.randomUUID());
        menuGroup.setName("치킨");

        entityManager.persist(menuGroup);

        var menu1 = createFixMenu("후라이드 치킨", 16000, menuGroup);
        var menu2 = createFixMenu("양념 치킨", 16000, menuGroup);
        var menu3 = createFixMenu("허니 콤보", 19500, menuGroup);

        entityManager.persist(menu1);
        entityManager.persist(menu2);
        entityManager.persist(menu3);

        entityManager.flush();

        // when
        var result = menuRepository.findAllByIdIn(
                List.of(menu1.getId(), menu2.getId()));

        // then
        assertThat(result).containsExactlyInAnyOrder(menu1, menu2);
    }

    private static @NotNull Menu createFixMenu(String name, int val, MenuGroup menuGroup) {
        var menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(val));
        menu.setDisplayed(true);
        menu.setMenuGroup(menuGroup);
        return menu;
    }
}

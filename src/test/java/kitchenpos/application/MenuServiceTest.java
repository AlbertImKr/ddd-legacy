package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.PurgomalumClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    PurgomalumClient purgomalumClient;

    @DisplayName("메뉴 생성")
    @Nested
    class MenuCreate {

        @DisplayName("메뉴의 가격이 null이먄 예외를 던진다.")
        @Test
        void if_price_is_null_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(null);

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 음수이면 예외를 던진다.")
        @Test
        void if_price_is_negative_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(-1));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 그룹이 없으면 예외를 던진다.")
        @Test
        void if_menu_group_is_null_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));
            request.setMenuGroupId(null);

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴의 메뉴 상품 목록이 없으면 예외를 던진다.")
        @Test
        void if_menu_products_is_null_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 상품 목록이 비어 있으면 예외를 던진다.")
        @Test
        void if_menu_products_is_empty_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            request.setMenuProducts(List.of());

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("각 메뉴 상품의 상품이 하나라도 없으면 예외를 던진다.")
        @Test
        void if_any_product_is_null_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var menuProduct1 = new MenuProduct();

            var productId1 = UUID.randomUUID();
            var product1 = new Product();
            menuProduct1.setProductId(productId1);
            menuProduct1.setProduct(product1);

            var menuProduct2 = new MenuProduct();

            var productId2 = UUID.randomUUID();
            menuProduct2.setProductId(productId2);

            request.setMenuProducts(List.of(menuProduct1, menuProduct2));

            given(productRepository.findAllByIdIn(List.of(productId1, productId2)))
                    .willReturn(List.of(menuProduct1.getProduct()));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품의 개수가 음수이면 예외를 던진다.")
        @Test
        void if_product_quantity_is_negative_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId = UUID.randomUUID();
            var product = new Product();
            product.setId(productId);

            var menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(-1);
            menuProduct.setProduct(product);

            request.setMenuProducts(List.of(menuProduct));

            given(productRepository.findAllByIdIn(List.of(productId)))
                    .willReturn(List.of(product));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품이 존재하지 않으면 예외를 던진다.")
        @Test
        void if_product_does_not_exist_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId = UUID.randomUUID();
            var product = new Product();
            product.setId(productId);

            var menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1);
            menuProduct.setProduct(product);

            request.setMenuProducts(List.of(menuProduct));

            given(productRepository.findAllByIdIn(List.of(productId)))
                    .willReturn(List.of(product));

            given(productRepository.findById(productId))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴의 가격이 상품의 가격과 개수의 곱의 합보다 크면 예외를 던진다.")
        @ParameterizedTest
        @CsvSource(value = {"3,1,1,1,1", "11,1,2,2,4"})
        void if_price_is_greater_than_sum_of_product_price_and_quantity_then_throw_exception(
                int menuPrice, int product1Price, int product1Quantity, int product2Price, int product2Quantity
        ) {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(menuPrice));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId1 = UUID.randomUUID();
            var product1 = new Product();
            product1.setId(productId1);
            product1.setPrice(BigDecimal.valueOf(product1Price));

            var productId2 = UUID.randomUUID();
            var product2 = new Product();
            product2.setId(productId2);
            product2.setPrice(BigDecimal.valueOf(product2Price));

            var menuProduct1 = new MenuProduct();
            menuProduct1.setProductId(productId1);
            menuProduct1.setQuantity(product1Quantity);
            menuProduct1.setProduct(product1);

            var menuProduct2 = new MenuProduct();
            menuProduct2.setProductId(productId2);
            menuProduct2.setQuantity(product2Quantity);
            menuProduct2.setProduct(product2);

            request.setMenuProducts(List.of(menuProduct1, menuProduct2));

            given(productRepository.findAllByIdIn(List.of(productId1, productId2)))
                    .willReturn(List.of(product1, product2));

            given(productRepository.findById(productId1))
                    .willReturn(Optional.of(product1));
            given(productRepository.findById(productId2))
                    .willReturn(Optional.of(product2));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 이름이 null이면 예외를 던진다.")
        @Test
        void if_name_is_null_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId = UUID.randomUUID();
            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.valueOf(1));

            var menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1);
            menuProduct.setProduct(product);

            request.setMenuProducts(List.of(menuProduct));

            given(productRepository.findAllByIdIn(List.of(productId)))
                    .willReturn(List.of(product));

            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 이름에 부적절한 단어가 포함되어 있으면 예외를 던진다.")
        @Test
        void if_name_contains_inappropriate_word_then_throw_exception() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));
            request.setName("bad word");

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId = UUID.randomUUID();
            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.valueOf(1));

            var menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1);
            menuProduct.setProduct(product);

            request.setMenuProducts(List.of(menuProduct));

            given(productRepository.findAllByIdIn(List.of(productId)))
                    .willReturn(List.of(product));

            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));

            given(purgomalumClient.containsProfanity(request.getName()))
                    .willReturn(true);

            // when
            assertThatThrownBy(() -> menuService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 생성 성공하면 메뉴를 반환한다.")
        @Test
        void if_menu_create_is_successful_then_return_menu() {
            // given
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));
            request.setName("good word");

            var menuGroupId = UUID.randomUUID();
            request.setMenuGroupId(menuGroupId);
            var menuGroup = new MenuGroup();
            menuGroup.setId(menuGroupId);

            given(menuGroupRepository.findById(menuGroupId))
                    .willReturn(Optional.of(menuGroup));

            var productId = UUID.randomUUID();
            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.valueOf(1));

            var menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(1);
            menuProduct.setProduct(product);

            request.setMenuProducts(List.of(menuProduct));

            given(productRepository.findAllByIdIn(List.of(productId)))
                    .willReturn(List.of(product));

            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));

            given(purgomalumClient.containsProfanity(request.getName()))
                    .willReturn(false);

            given(menuRepository.save(any(Menu.class)))
                    .will(invocation -> invocation.getArgument(0));

            // when
            var menu = menuService.create(request);

            // then
            assertThat(menu).isNotNull();
            Assertions.assertAll(
                    () -> assertThat(menu.getId()).isNotNull(),
                    () -> assertThat(menu.getName()).isEqualTo(request.getName()),
                    () -> assertThat(menu.getPrice()).isEqualTo(request.getPrice()),
                    () -> assertThat(menu.getMenuGroup()).isEqualTo(menuGroup),
                    () -> assertThat(menu.isDisplayed()).isFalse(),
                    () -> assertThat(menu.getMenuProducts()).hasSize(1)
            );
        }
    }

    @DisplayName("메뉴 가격 변경")
    @Nested
    class MenuChangePrice {

        @DisplayName("메뉴의 가격이 null이면 예외를 던진다.")
        @Test
        void if_price_is_null_then_throw_exception() {
            // given
            var menuId = UUID.randomUUID();
            var request = new Menu();
            request.setPrice(null);

            // when
            assertThatThrownBy(() -> menuService.changePrice(menuId, request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 음수이면 예외를 던진다.")
        @Test
        void if_price_is_negative_then_throw_exception() {
            // given
            var menuId = UUID.randomUUID();
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(-1));

            // when
            assertThatThrownBy(() -> menuService.changePrice(menuId, request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴가 존재하지 않으면 예외를 던진다.")
        @Test
        void if_menu_does_not_exist_then_throw_exception() {
            // given
            var menuId = UUID.randomUUID();
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(1));

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> menuService.changePrice(menuId, request))
                    // then
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴의 가격이 상품의 가격과 개수의 곱의 합보다 크면 예외를 던진다.")
        @ParameterizedTest
        @CsvSource(value = {"3,1,1,1,1", "11,1,2,2,4"})
        void if_price_is_greater_than_sum_of_product_price_and_quantity_then_throw_exception(
                int menuPrice, int product1Price, int product1Quantity, int product2Price, int product2Quantity
        ) {
            // given
            var menuId = UUID.randomUUID();
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(menuPrice));

            var product1 = new Product();
            product1.setPrice(BigDecimal.valueOf(product1Price));

            var product2 = new Product();
            product2.setPrice(BigDecimal.valueOf(product2Price));

            var menuProduct1 = new MenuProduct();
            menuProduct1.setProduct(product1);
            menuProduct1.setQuantity(product1Quantity);

            var menuProduct2 = new MenuProduct();
            menuProduct2.setProduct(product2);
            menuProduct2.setQuantity(product2Quantity);

            var menu = new Menu();
            menu.setId(menuId);
            menu.setPrice(BigDecimal.valueOf(1));
            menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.of(menu));

            // when
            assertThatThrownBy(() -> menuService.changePrice(menuId, request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격 변경 성공하면 메뉴를 반환한다.")
        @Test
        void if_menu_change_price_is_successful_then_return_menu() {
            // given
            var menuId = UUID.randomUUID();
            var request = new Menu();
            request.setPrice(BigDecimal.valueOf(0));

            var product = new Product();
            product.setPrice(BigDecimal.valueOf(1));

            var menuProduct = new MenuProduct();
            menuProduct.setQuantity(1);
            menuProduct.setProduct(new Product());
            menuProduct.setProduct(product);

            var menu = new Menu();
            menu.setId(menuId);
            menu.setPrice(BigDecimal.valueOf(2));
            menu.setMenuProducts(List.of(menuProduct));

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.of(menu));

            // when
            var changedMenu = menuService.changePrice(menuId, request);

            // then
            assertThat(changedMenu).isNotNull();
            assertThat(changedMenu.getPrice()).isEqualTo(request.getPrice());
        }
    }

    @DisplayName("메뉴 활성화")
    @Nested
    class MenuDisplay {

        @DisplayName("메뉴가 존재하지 않으면 예외를 던진다.")
        @Test
        void if_menu_does_not_exist_then_throw_exception() {
            // given
            var menuId = UUID.randomUUID();
            given(menuRepository.findById(menuId))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> menuService.display(menuId))
                    // then
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴의 가격이 상품의 가격과 개수의 곱의 합보다 크면 예외를 던진다.")
        @ParameterizedTest
        @CsvSource(value = {"3,1,1,1,1", "11,1,2,2,4"})
        void if_price_is_greater_than_sum_of_product_price_and_quantity_then_throw_exception(
                int menuPrice, int product1Price, int product1Quantity, int product2Price, int product2Quantity
        ) {
            // given
            var menuId = UUID.randomUUID();
            var menu = new Menu();
            menu.setId(menuId);
            menu.setPrice(BigDecimal.valueOf(menuPrice));

            var product1 = new Product();
            product1.setPrice(BigDecimal.valueOf(product1Price));

            var product2 = new Product();
            product2.setPrice(BigDecimal.valueOf(product2Price));

            var menuProduct1 = new MenuProduct();
            menuProduct1.setProduct(product1);
            menuProduct1.setQuantity(product1Quantity);

            var menuProduct2 = new MenuProduct();
            menuProduct2.setProduct(product2);
            menuProduct2.setQuantity(product2Quantity);

            menu.setMenuProducts(List.of(menuProduct1, menuProduct2));

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.of(menu));

            // when
            assertThatThrownBy(() -> menuService.display(menuId))
                    // then
                    .isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("메뉴 활성화 성공하면 메뉴를 반환한다.")
        @Test
        void if_menu_display_is_successful_then_return_menu() {
            // given
            var menuId = UUID.randomUUID();
            var menu = new Menu();
            menu.setId(menuId);
            menu.setDisplayed(false);
            menu.setPrice(BigDecimal.valueOf(0));
            menu.setMenuProducts(List.of());

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.of(menu));

            // when
            var displayedMenu = menuService.display(menuId);

            // then
            assertThat(displayedMenu).isNotNull();
            assertThat(displayedMenu.isDisplayed()).isTrue();
        }
    }

    @DisplayName("메뉴 비활성화")
    @Nested
    class MenuHide {

        @DisplayName("메뉴가 존재하지 않으면 예외를 던진다.")
        @Test
        void if_menu_does_not_exist_then_throw_exception() {
            // given
            var menuId = UUID.randomUUID();
            given(menuRepository.findById(menuId))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> menuService.hide(menuId))
                    // then
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("메뉴 비활성화 성공하면 메뉴를 반환한다.")
        @Test
        void if_menu_hide_is_successful_then_return_menu() {
            // given
            var menuId = UUID.randomUUID();
            var menu = new Menu();
            menu.setId(menuId);
            menu.setDisplayed(true);

            given(menuRepository.findById(menuId))
                    .willReturn(Optional.of(menu));

            // when
            var hiddenMenu = menuService.hide(menuId);

            // then
            assertThat(hiddenMenu).isNotNull();
            assertThat(hiddenMenu.isDisplayed()).isFalse();
        }
    }

    @DisplayName("메뉴 목록 조회")
    @Nested
    class MenuFindAll {

        @DisplayName("메뉴가 있으면 메뉴 목록을 반환한다.")
        @Test
        void if_menu_exists_then_return_menu_list() {
            // given
            var menu1 = new Menu();
            menu1.setId(UUID.randomUUID());
            var menu2 = new Menu();
            menu2.setId(UUID.randomUUID());

            given(menuRepository.findAll())
                    .willReturn(List.of(menu1, menu2));

            // when
            var menus = menuService.findAll();

            // then
            assertThat(menus).hasSize(2);
        }
    }
}


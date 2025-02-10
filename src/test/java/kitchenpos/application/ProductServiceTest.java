package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.Menu;
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
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private PurgomalumClient purgomalumClient;

    @DisplayName("상품 목록 조회")
    @Nested
    class ListProducts {

        @DisplayName("상품 목록을 조회한다.")
        @Test
        void if_success_then_return_products() {
            // given
            var product = new Product();
            product.setId(UUID.randomUUID());
            product.setName("상품");
            product.setPrice(BigDecimal.TEN);
            given(productRepository.findAll())
                    .willReturn(List.of(product));

            // when
            var products = productService.findAll();

            // then
            assertThat(products).isNotNull();
            Assertions.assertAll(
                    () -> assertThat(products).hasSize(1),
                    () -> assertThat(products.getFirst().getName()).isEqualTo("상품"),
                    () -> assertThat(products.getFirst().getPrice()).isEqualTo(BigDecimal.TEN)
            );
        }
    }

    @DisplayName("상품 생성")
    @Nested
    class CreateProduct {

        @DisplayName("상품의 가격은 0원 이하 일 때 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {0, -1})
        void if_price_is_less_than_or_equal_to_zero_then_throw_exception(final int price) {
            // given
            var request = new Product();
            request.setPrice(BigDecimal.valueOf(price));

            // when
            assertThatThrownBy(() -> productService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품의 가격은 null 일 때 예외가 발생한다.")
        @ParameterizedTest
        @NullSource
        void if_price_is_null_then_throw_exception(final BigDecimal price) {
            // given
            var request = new Product();
            request.setPrice(price);

            // when
            assertThatThrownBy(() -> productService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품의 이름이 null 일 때 예외가 발생한다.")
        @ParameterizedTest
        @NullSource
        void if_name_is_null_then_throw_exception(final String name) {
            // given
            var request = new Product();
            request.setPrice(BigDecimal.TEN);
            request.setName(name);

            // when
            assertThatThrownBy(() -> productService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품의 이름에 욕설이 포함되어 있을 때 예외가 발생한다.")
        @Test
        void if_name_contains_profanity_then_throw_exception() {
            // given
            var request = new Product();
            request.setPrice(BigDecimal.TEN);
            request.setName("욕설");
            given(purgomalumClient.containsProfanity("욕설"))
                    .willReturn(true);

            // when
            assertThatThrownBy(() -> productService.create(request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 생성 성공하면 저장된 상품을 반환한다.")
        @Test
        void if_success_then_return_saved_product() {
            // given
            var request = new Product();
            request.setPrice(BigDecimal.TEN);
            request.setName("상품");
            given(purgomalumClient.containsProfanity("상품"))
                    .willReturn(false);
            given(productRepository.save(any(Product.class)))
                    .will(invocation -> invocation.getArgument(0));

            // when
            var product = productService.create(request);

            // then
            assertThat(product).isNotNull();
            Assertions.assertAll(
                    () -> assertThat(product.getName()).isEqualTo("상품"),
                    () -> assertThat(product.getPrice()).isEqualTo(BigDecimal.TEN)
            );
        }
    }

    @DisplayName("상품 가격 변경")
    @Nested
    class ChangeProductPrice {

        @DisplayName("상품의 가격은 0원 미만 일 때 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2})
        void if_price_is_less_than_or_equal_to_zero_then_throw_exception(final int price) {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(BigDecimal.valueOf(price));

            // when
            assertThatThrownBy(() -> productService.changePrice(productId, request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품의 가격은 null 일 때 예외가 발생한다.")
        @ParameterizedTest
        @NullSource
        void if_price_is_null_then_throw_exception(final BigDecimal price) {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(price);

            // when
            assertThatThrownBy(() -> productService.changePrice(productId, request))
                    // then
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품이 존재하지 않을 때 예외가 발생한다.")
        @Test
        void if_product_does_not_exist_then_throw_exception() {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(BigDecimal.TEN);
            given(productRepository.findById(productId))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> productService.changePrice(productId, request))
                    // then
                    .isInstanceOf(java.util.NoSuchElementException.class);
        }

        @DisplayName("상품 가격 변경 성공하면 상품을 반환한다.")
        @Test
        void if_success_then_return_changed_product() {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(BigDecimal.TEN);
            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.ONE);
            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));
            given(menuRepository.findAllByProductId(productId))
                    .willReturn(List.of());

            // when
            var changedProduct = productService.changePrice(productId, request);

            // then
            assertThat(changedProduct).isNotNull();
            assertThat(changedProduct.getPrice()).isEqualTo(BigDecimal.TEN);
        }

        @DisplayName("상품의 가격을 변경하면 메뉴의 가격이 그 매뉴에 속한 상품의 가격의 합보다 작을 때 그 메뉴를 숨긴다.")
        @ParameterizedTest
        @CsvSource(value = {"1, 2, 3", "2, 3, 5", "3, 4, 7"})
        void if_menu_price_is_less_than_sum_of_product_price_then_hide_menu(
                final int productPrice, final int menuProductPrice, final int menuPrice
        ) {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(BigDecimal.valueOf(productPrice));

            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.valueOf(menuProductPrice));

            var menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(2L);

            var menu = new Menu();
            menu.setDisplayed(true);
            menu.setId(UUID.randomUUID());
            menu.setMenuProducts(List.of(menuProduct));
            menu.setPrice(BigDecimal.valueOf(menuPrice));

            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));
            given(menuRepository.findAllByProductId(productId))
                    .willReturn(List.of(menu));

            // when
            productService.changePrice(productId, request);

            // then
            assertThat(menu.isDisplayed()).isFalse();
        }

        @DisplayName("상품의 가격을 변경하면 메뉴의 가격이 그 매뉴에 속한 상품의 가격의 합보다 클 때 그 메뉴를 숨기지 않는다.")
        @ParameterizedTest
        @CsvSource(value = {"3, 2, 3", "4, 3, 5", "5, 4, 7"})
        void if_menu_price_is_greater_than_sum_of_product_price_then_not_hide_menu(
                final int productPrice, final int menuProductPrice, final int menuPrice
        ) {
            // given
            var productId = UUID.randomUUID();
            var request = new Product();
            request.setPrice(BigDecimal.valueOf(productPrice));

            var product = new Product();
            product.setId(productId);
            product.setPrice(BigDecimal.valueOf(menuProductPrice));

            var menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(2L);

            var menu = new Menu();
            menu.setDisplayed(true);
            menu.setId(UUID.randomUUID());
            menu.setMenuProducts(List.of(menuProduct));
            menu.setPrice(BigDecimal.valueOf(menuPrice));

            given(productRepository.findById(productId))
                    .willReturn(Optional.of(product));
            given(menuRepository.findAllByProductId(productId))
                    .willReturn(List.of(menu));

            // when
            productService.changePrice(productId, request);

            // then
            assertThat(menu.isDisplayed()).isTrue();
        }
    }
}

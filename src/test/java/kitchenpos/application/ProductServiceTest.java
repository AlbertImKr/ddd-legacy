package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
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

    @DisplayName("상품을 생성한다.")
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

        @DisplayName("상품을 성공하면 저장된 상품을 반환한다.")
        @Test
        void create_product_success_then_return_saved_product() {
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
}

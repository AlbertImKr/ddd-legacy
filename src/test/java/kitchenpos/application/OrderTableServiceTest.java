package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @InjectMocks
    OrderTableService orderTableService;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @DisplayName("주문 테이블 생성")
    @Nested
    class CreateOrderTable {

        @DisplayName("주문 테이블 이름이 null이거나 빈 문자열인 경우 예외를 던진다.")
        @ParameterizedTest
        @NullAndEmptySource
        void if_name_is_null_or_empty_then_throw_exception(String name) {
            // given
            var request = new OrderTable();
            request.setName(name);

            // when then
            assertThatThrownBy(() -> orderTableService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블 생성 성공하면 주문 테이블을 반환한다.")
        @Test
        void create_order_table() {
            // given
            var request = new OrderTable();
            request.setName("테이블");

            given(orderTableRepository.save(any()))
                    .will(invocation -> invocation.getArgument(0));

            // when
            var orderTable = orderTableService.create(request);

            // then
            assertThat(orderTable).isNotNull();
            Assertions.assertAll(
                    () -> assertThat(orderTable.getId()).isNotNull(),
                    () -> assertThat(orderTable.getName()).isEqualTo("테이블"),
                    () -> assertThat(orderTable.getNumberOfGuests()).isZero(),
                    () -> assertThat(orderTable.isOccupied()).isFalse()
            );
        }
    }

    @DisplayName("주문 테일블 사용")
    @Nested
    class SitOrderTable {

        @DisplayName("주문 테이블이 존재하지 않는 경우 예외를 던진다.")
        @Test
        void if_order_table_does_not_exist_then_throw_exception() {
            // given
            var orderTableId = UUID.randomUUID();

            given(orderTableRepository.findById(orderTableId))
                    .willReturn(Optional.empty());

            // when then
            assertThatThrownBy(() -> orderTableService.sit(orderTableId))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @DisplayName("주문 테이블을 사용하면 주문 테이블을 반환한다.")
        @Test
        void sit_order_table() {
            // given
            var orderTableId = UUID.randomUUID();
            var orderTable = new OrderTable();
            orderTable.setId(orderTableId);

            given(orderTableRepository.findById(orderTableId))
                    .willReturn(Optional.of(orderTable));

            // when
            var result = orderTableService.sit(orderTableId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.isOccupied()).isTrue();
        }
    }
}

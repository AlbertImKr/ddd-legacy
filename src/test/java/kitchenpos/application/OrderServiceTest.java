package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderType;
import kitchenpos.infra.KitchenridersClient;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    KitchenridersClient kitchenridersClient;

    private static @NotNull Menu createFixMenu(UUID id) {
        var menu = new Menu();
        menu.setId(id);
        return menu;
    }

    private static @NotNull Menu createFixMenu(UUID id, boolean displayed) {
        var menu = createFixMenu(id);
        menu.setDisplayed(displayed);
        return menu;
    }

    private static @NotNull Menu createFixMenu(UUID id, boolean displayed, long price) {
        Menu menu = createFixMenu(id, displayed);
        menu.setPrice(BigDecimal.valueOf(price));
        return menu;
    }

    private static @NotNull OrderLineItem createFixOrderLineItem(long quantity) {
        var orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    private static @NotNull OrderLineItem createFixOrderLineItem(long quantity, UUID id) {
        var orderLineItem = createFixOrderLineItem(quantity);
        orderLineItem.setMenuId(id);
        return orderLineItem;
    }

    private static @NotNull OrderLineItem createFixOrderLineItem(long quantity, UUID id, long price) {
        OrderLineItem orderLineItem = createFixOrderLineItem(quantity, id);
        orderLineItem.setPrice(BigDecimal.valueOf(price));
        return orderLineItem;
    }

    private static @NotNull OrderLineItem createFixOrderLineItem(long quantity, long price, Menu menu) {
        var orderLineItem = createFixOrderLineItem(quantity, menu.getId(), price);
        orderLineItem.setMenu(menu);
        return orderLineItem;
    }

    private static @NotNull Order createFixOrder(OrderType orderType) {
        var request = new Order();
        request.setType(orderType);
        return request;
    }

    private static @NotNull Order createFixOrder(OrderType orderType, List<OrderLineItem> orderLineItems) {
        var request = createFixOrder(orderType);
        request.setOrderLineItems(orderLineItems);
        return request;
    }

    private static @NotNull Order createFixOrder(
            OrderType orderType, List<OrderLineItem> orderLineItem, String deliveryAddress
    ) {
        var request = createFixOrder(orderType);
        request.setDeliveryAddress(deliveryAddress);
        request.setOrderLineItems(orderLineItem);
        return request;
    }

    private static @NotNull Order createFixOrder(UUID orderId, OrderStatus orderStatus) {
        var order = new Order();
        order.setId(orderId);
        order.setStatus(orderStatus);
        return order;
    }

    private static @NotNull Order createFixOrder(
            UUID orderId,
            OrderStatus orderStatus,
            OrderType orderType,
            String deliveryAddress,
            List<OrderLineItem> orderLineItems
    ) {
        var order = new Order();
        order.setId(orderId);
        order.setStatus(orderStatus);
        order.setType(orderType);
        order.setDeliveryAddress(deliveryAddress);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    @DisplayName("배달")
    @Nested
    class Delivery {

        @DisplayName("주문 생성")
        @Nested
        class Create {

            @DisplayName("주문 타입이 null인 경우 예외를 던진다.")
            @Test
            void if_type_is_null_then_throw_exception() {
                // given
                var request = new Order();

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 상세 항목 목록이 null인 경우 예외를 던진다.")
            @Test
            void if_order_line_items_is_null_then_throw_exception() {
                // given
                var request = createFixOrder(OrderType.DELIVERY);

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 상세 항목 목록이 비어있는 경우 예외를 던진다.")
            @Test
            void if_order_line_items_is_empty_then_throw_exception() {
                // given
                var request = createFixOrder(OrderType.DELIVERY, List.of());

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 상세 항목의 개수와 메뉴의 개수가 다른 경우 예외를 던진다.")
            @Test
            void if_order_line_items_size_is_not_equal_to_menu_size_then_throw_exception() {
                // given
                var request = createFixOrder(OrderType.DELIVERY, List.of(new OrderLineItem()));

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of());

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 상세 항목의 수량이 음수인 경우 예외를 던진다.")
            @Test
            void if_order_line_item_quantity_is_negative_then_throw_exception() {
                // given
                var orderLineItem = createFixOrderLineItem(-1L);

                var request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem));

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(new Menu()));

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 상세 항목의 메뉴가 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_line_item_menu_does_not_exist_then_throw_exception() {
                // given
                var menu = createFixMenu(UUID.randomUUID());

                var orderLineItem = createFixOrderLineItem(1L, menu.getId());

                Order request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem));

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(menu));
                given(menuRepository.findById(menu.getId()))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문 상세 항목의 메뉴가 미활성화된 경우 예외를 던진다.")
            @Test
            void if_order_line_item_menu_is_not_displayed_then_throw_exception() {
                // given
                var menu = createFixMenu(UUID.randomUUID());

                OrderLineItem orderLineItem = createFixOrderLineItem(1L, menu.getId());

                Order request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem));

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(menu));
                given(menuRepository.findById(menu.getId()))
                        .willReturn(Optional.of(menu));

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 상세 항목의 가격이 메뉴의 가격과 다른 경우 예외를 던진다.")
            @Test
            void if_order_line_item_price_is_not_equal_to_menu_price_then_throw_exception() {
                // given

                var menu = createFixMenu(UUID.randomUUID(), true, 1000L);

                var orderLineItem = createFixOrderLineItem(1L, menu.getId(), 2000L);

                Order request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem));

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(menu));
                given(menuRepository.findById(menu.getId()))
                        .willReturn(Optional.of(menu));

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("배달 주소가 null 혹은 빈 문자열인 경우 예외를 던진다.")
            @ParameterizedTest
            @NullAndEmptySource
            void if_delivery_address_is_null_or_empty_then_throw_exception(String deliveryAddress) {
                // given

                var menu = createFixMenu(UUID.randomUUID(), true, 1000L);

                var orderLineItem = createFixOrderLineItem(1L, menu.getId(), 1000L);

                var request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem), deliveryAddress);

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(menu));
                given(menuRepository.findById(menu.getId()))
                        .willReturn(Optional.of(menu));

                // when, then
                assertThatThrownBy(() -> orderService.create(request))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("주문 생성 성공하면 주문을 반환한다.")
            @Test
            void if_success_then_return_order() {
                // given
                var menu = createFixMenu(UUID.randomUUID(), true, 1000L);

                var orderLineItem = createFixOrderLineItem(1L, menu.getId(), 1000L);

                String deliveryAddress = "서울시 강남구";
                var request = createFixOrder(OrderType.DELIVERY, List.of(orderLineItem), deliveryAddress);

                given(menuRepository.findAllByIdIn(anyList()))
                        .willReturn(List.of(menu));
                given(menuRepository.findById(menu.getId()))
                        .willReturn(Optional.of(menu));

                given(orderRepository.save(any(Order.class)))
                        .will(invocation -> invocation.getArgument(0));

                // when
                var order = orderService.create(request);

                // then
                assertThat(order).isNotNull();
                Assertions.assertAll(
                        () -> assertThat(order.getType()).isEqualTo(OrderType.DELIVERY),
                        () -> assertThat(order.getStatus()).isEqualTo(OrderStatus.WAITING),
                        () -> assertThat(order.getOrderDateTime()).isNotNull(),
                        () -> assertThat(order.getOrderLineItems()).hasSize(1),
                        () -> assertThat(order.getDeliveryAddress()).isEqualTo(deliveryAddress)
                );
            }
        }

        @DisplayName("주문 접수")
        @Nested
        class Accept {

            @DisplayName("주문 아이디가 null인 경우 예외를 던진다.")
            @Test
            void if_order_id_is_null_then_throw_exception() {
                // when, then
                assertThatThrownBy(() -> orderService.accept(null))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문이 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_does_not_exist_then_throw_exception() {
                // given
                var orderId = UUID.randomUUID();

                given(orderRepository.findById(orderId))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.accept(orderId))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문 상태가 대기 상태가 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"ACCEPTED", "SERVED", "DELIVERING", "DELIVERED", "COMPLETED"})
            void if_order_status_is_not_waiting_then_throw_exception(OrderStatus orderStatus) {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, orderStatus);

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.accept(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 접수 성공하면 주문 상태를 접수로 변경되고 배달 업체에 배달 요청을 한다.")
            @Test
            void if_success_then_change_order_status_to_accepted_and_request_delivery() {
                // given
                long price = 1000L;
                var menu = createFixMenu(UUID.randomUUID(), true, price);

                var orderLineItem = createFixOrderLineItem(1L, price, menu);

                var orderId = UUID.randomUUID();
                var deliveryAddress = "서울시 강남구";
                var order = createFixOrder(
                        orderId,
                        OrderStatus.WAITING,
                        OrderType.DELIVERY,
                        deliveryAddress,
                        List.of(orderLineItem)
                );

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when
                var acceptedOrder = orderService.accept(orderId);

                // then
                assertThat(acceptedOrder).isNotNull();
                assertThat(acceptedOrder.getStatus()).isEqualTo(OrderStatus.ACCEPTED);
                verify(kitchenridersClient).requestDelivery(orderId, BigDecimal.valueOf(price), deliveryAddress);
            }
        }

        @DisplayName("주문 서빙")
        @Nested
        class Serve {

            @DisplayName("주문 아이디가 null인 경우 예외를 던진다.")
            @Test
            void if_order_id_is_null_then_throw_exception() {
                // when, then
                assertThatThrownBy(() -> orderService.serve(null))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문이 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_does_not_exist_then_throw_exception() {
                // given
                var orderId = UUID.randomUUID();

                given(orderRepository.findById(orderId))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.serve(orderId))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문 상태가 접수 상태가 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"WAITING", "SERVED", "DELIVERING", "DELIVERED", "COMPLETED"})
            void if_order_status_is_not_accepted_then_throw_exception(OrderStatus orderStatus) {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, orderStatus);

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.serve(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 서빙 성공하면 주문 상태를 서빙으로 변경한다.")
            @Test
            void if_success_then_change_order_status_to_served() {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, OrderStatus.ACCEPTED);

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when
                var servedOrder = orderService.serve(orderId);

                // then
                assertThat(servedOrder).isNotNull();
                assertThat(servedOrder.getStatus()).isEqualTo(OrderStatus.SERVED);
            }
        }

        @DisplayName("주문 배달 시작")
        @Nested
        class StartDelivery {

            @DisplayName("주문 아이디가 null인 경우 예외를 던진다.")
            @Test
            void if_order_id_is_null_then_throw_exception() {
                // when, then
                assertThatThrownBy(() -> orderService.startDelivery(null))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문이 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_does_not_exist_then_throw_exception() {
                // given
                var orderId = UUID.randomUUID();

                given(orderRepository.findById(orderId))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.startDelivery(orderId))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문 타입이 배달이 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"TAKEOUT", "EAT_IN"})
            void if_order_type_is_not_delivery_then_throw_exception(OrderType orderType) {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, OrderStatus.SERVED);
                order.setType(orderType);

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.startDelivery(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 상태가 서빙 상태가 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"WAITING", "ACCEPTED", "DELIVERING", "DELIVERED", "COMPLETED"})
            void if_order_status_is_not_served_then_throw_exception(OrderStatus orderStatus) {
                // given
                var orderId = UUID.randomUUID();

                var order = OrderServiceTest.createFixOrder(
                        orderId, orderStatus, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.startDelivery(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 배달 시작 성공하면 주문 상태를 배달 중으로 변경한다.")
            @Test
            void if_success_then_change_order_status_to_delivering() {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, OrderStatus.SERVED, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when
                var deliveringOrder = orderService.startDelivery(orderId);

                // then
                assertThat(deliveringOrder).isNotNull();
                assertThat(deliveringOrder.getStatus()).isEqualTo(OrderStatus.DELIVERING);
            }
        }

        @DisplayName("주문 배달 완료")
        @Nested
        class CompleteDelivery {

            @DisplayName("주문 아이디가 null인 경우 예외를 던진다.")
            @Test
            void if_order_id_is_null_then_throw_exception() {
                // when, then
                assertThatThrownBy(() -> orderService.completeDelivery(null))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문이 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_does_not_exist_then_throw_exception() {
                // given
                var orderId = UUID.randomUUID();

                given(orderRepository.findById(orderId))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.completeDelivery(orderId))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문 상태가 배달 중 상태가 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"WAITING", "ACCEPTED", "SERVED", "DELIVERED", "COMPLETED"})
            void if_order_status_is_not_delivering_then_throw_exception(OrderStatus orderStatus) {
                // given
                var orderId = UUID.randomUUID();

                var order = OrderServiceTest.createFixOrder(
                        orderId, orderStatus, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.completeDelivery(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 배달 완료 성공하면 주문 상태를 배달 완료로 변경한다.")
            @Test
            void if_success_then_change_order_status_to_delivered() {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, OrderStatus.DELIVERING, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when
                var deliveredOrder = orderService.completeDelivery(orderId);

                // then
                assertThat(deliveredOrder).isNotNull();
                assertThat(deliveredOrder.getStatus()).isEqualTo(OrderStatus.DELIVERED);
            }
        }

        @DisplayName("주문 완료")
        @Nested
        class Complete {

            @DisplayName("주문 아이디가 null인 경우 예외를 던진다.")
            @Test
            void if_order_id_is_null_then_throw_exception() {
                // when, then
                assertThatThrownBy(() -> orderService.complete(null))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("주문이 존재하지 않는 경우 예외를 던진다.")
            @Test
            void if_order_does_not_exist_then_throw_exception() {
                // given
                var orderId = UUID.randomUUID();

                given(orderRepository.findById(orderId))
                        .willReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> orderService.complete(orderId))
                        .isInstanceOf(NoSuchElementException.class);
            }

            @DisplayName("배달 완료 상태가 아닌 경우 예외를 던진다.")
            @ParameterizedTest
            @ValueSource(strings = {"WAITING", "ACCEPTED", "SERVED", "DELIVERING", "COMPLETED"})
            void if_order_status_is_not_delivered_then_throw_exception(OrderStatus orderStatus) {
                // given
                var orderId = UUID.randomUUID();

                var order = OrderServiceTest.createFixOrder(
                        orderId, orderStatus, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when, then
                assertThatThrownBy(() -> orderService.complete(orderId))
                        .isInstanceOf(IllegalStateException.class);
            }

            @DisplayName("주문 완료 성공하면 주문 상태를 완료로 변경한다.")
            @Test
            void if_success_then_change_order_status_to_completed() {
                // given
                var orderId = UUID.randomUUID();

                var order = createFixOrder(orderId, OrderStatus.DELIVERED, OrderType.DELIVERY, "서울시 강남구", List.of());

                given(orderRepository.findById(order.getId()))
                        .willReturn(Optional.of(order));

                // when
                var completedOrder = orderService.complete(orderId);

                // then
                assertThat(completedOrder).isNotNull();
                assertThat(completedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
            }
        }
    }
}

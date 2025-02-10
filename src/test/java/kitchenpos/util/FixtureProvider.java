package kitchenpos.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;
import org.jetbrains.annotations.NotNull;

public class FixtureProvider {

    public static @NotNull OrderLineItem createFixOrderLineItem(long quantity) {
        var orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static @NotNull OrderLineItem createFixOrderLineItem(long quantity, UUID id) {
        var orderLineItem = createFixOrderLineItem(quantity);
        orderLineItem.setMenuId(id);
        return orderLineItem;
    }

    public static @NotNull OrderLineItem createFixOrderLineItem(long quantity, UUID id, long price) {
        OrderLineItem orderLineItem = createFixOrderLineItem(quantity, id);
        orderLineItem.setPrice(BigDecimal.valueOf(price));
        return orderLineItem;
    }

    public static @NotNull OrderLineItem createFixOrderLineItem(long quantity, long price, Menu menu) {
        var orderLineItem = createFixOrderLineItem(quantity, menu.getId(), price);
        orderLineItem.setMenu(menu);
        return orderLineItem;
    }

    public static @NotNull Order createFixOrder(OrderType orderType) {
        var request = new Order();
        request.setType(orderType);
        return request;
    }

    public static @NotNull Order createFixOrder(OrderType orderType, List<OrderLineItem> orderLineItems) {
        var request = createFixOrder(orderType);
        request.setOrderLineItems(orderLineItems);
        return request;
    }

    public static @NotNull Order createFixOrder(
            OrderType orderType, List<OrderLineItem> orderLineItem, String deliveryAddress
    ) {
        var request = createFixOrder(orderType);
        request.setDeliveryAddress(deliveryAddress);
        request.setOrderLineItems(orderLineItem);
        return request;
    }

    public static @NotNull Order createFixOrder(
            UUID orderId,
            OrderStatus orderStatus,
            OrderType orderType,
            OrderTable orderTable
    ) {
        var order = new Order();
        order.setId(orderId);
        order.setStatus(orderStatus);
        order.setType(orderType);
        order.setOrderTable(orderTable);
        return order;
    }

    public static @NotNull Order createFixOrder(UUID orderId, OrderStatus orderStatus) {
        var order = new Order();
        order.setId(orderId);
        order.setStatus(orderStatus);
        return order;
    }

    public static @NotNull Order createFixOrder(
            UUID orderId,
            OrderStatus orderStatus,
            OrderType orderType
    ) {
        var order = createFixOrder(orderId, orderStatus);
        order.setType(orderType);
        return order;
    }

    public static @NotNull Order createFixOrder(
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

    public static @NotNull Order createFixOrder(
            OrderType orderType,
            UUID orderTableId,
            List<OrderLineItem> orderLineItems
    ) {
        var request = createFixOrder(orderType);
        request.setOrderTableId(orderTableId);
        request.setOrderLineItems(orderLineItems);
        return request;
    }
}

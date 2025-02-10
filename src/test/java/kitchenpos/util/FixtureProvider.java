package kitchenpos.util;

import java.util.List;
import java.util.UUID;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderType;
import org.jetbrains.annotations.NotNull;

public class FixtureProvider {

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

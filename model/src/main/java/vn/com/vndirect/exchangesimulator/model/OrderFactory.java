package vn.com.vndirect.exchangesimulator.model;

import java.util.UUID;

public class OrderFactory {
    public static NewOrderSingle createNewOrder(String symbol, int quantity, double price) {
        NewOrderSingle order = new NewOrderSingle();
        order.setPrice(price);
        order.setOrderQty(quantity);
        order.setSymbol(symbol);
        order.setOrderId(UUID.randomUUID().toString());
        order.setSide('1');
        order.setClOrdID("124");
        return order;
    }
}

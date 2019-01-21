package org.romanchi.database.entities;

public class OrderItem {
    private Product product;
    private Order order;
    private Double orderItemQuantity;

    public OrderItem() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(Double orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product +
                ", order=" + order +
                ", orderItemQuantity=" + orderItemQuantity +
                '}';
    }
}

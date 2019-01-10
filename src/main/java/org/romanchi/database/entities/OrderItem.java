package org.romanchi.database.entities;

public class OrderItem {
    private Product product;
    private Order order;

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

    @Override
    public String toString() {
        return "OrderItem{" +
                "product=" + product +
                ", order=" + order +
                '}';
    }
}

package org.romanchi.database.entities;

public class Order {
    private long orderId;
    private User user;
    private Integer orderStatus;
    private Double summaryPrice;

    public Order() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getSummaryPrice() {
        return summaryPrice;
    }

    public void setSummaryPrice(Double summaryPrice) {
        this.summaryPrice = summaryPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", user=" + user +
                ", orderStatus=" + orderStatus +
                ", summaryPrice=" + summaryPrice +
                '}';
    }
}

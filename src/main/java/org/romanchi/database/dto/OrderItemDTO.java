package org.romanchi.database.dto;

public class OrderItemDTO {
    private long orderId;
    private Double summaryPrice;
    private Double orderItemQuantity;
    private long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImageSrc;


    public OrderItemDTO() {
    }

    public OrderItemDTO(long orderId, Double summaryPrice, Double orderItemQuantity, long productId, String productName, String productDescription, Double productPrice, String productImageSrc) {
        this.orderId = orderId;
        this.summaryPrice = summaryPrice;
        this.orderItemQuantity = orderItemQuantity;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImageSrc = productImageSrc;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Double getSummaryPrice() {
        return summaryPrice;
    }

    public void setSummaryPrice(Double summaryPrice) {
        this.summaryPrice = summaryPrice;
    }

    public Double getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(Double orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageSrc() {
        return productImageSrc;
    }

    public void setProductImageSrc(String productImageSrc) {
        this.productImageSrc = productImageSrc;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}

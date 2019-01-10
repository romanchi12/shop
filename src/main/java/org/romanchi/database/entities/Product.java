package org.romanchi.database.entities;

public class Product {
    private Long productId;
    private Warehouse WarehouseItem;
    private Category category;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImgSrc;

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Warehouse getWarehouseItem() {
        return WarehouseItem;
    }

    public void setWarehouseItem(Warehouse warehouseItem) {
        WarehouseItem = warehouseItem;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public String getProductImgSrc() {
        return productImgSrc;
    }

    public void setProductImgSrc(String productImgSrc) {
        this.productImgSrc = productImgSrc;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", WarehouseItem=" + WarehouseItem +
                ", category=" + category +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", productImgSrc='" + productImgSrc + '\'' +
                '}';
    }
}

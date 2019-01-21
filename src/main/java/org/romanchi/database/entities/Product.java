package org.romanchi.database.entities;

public class Product {
    private long productId;
    private WarehouseItem WarehouseItem;
    private Category category;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImgSrc;

    public Product() {
    }

    public Product(long productId, org.romanchi.database.entities.WarehouseItem warehouseItem, Category category, String productName, String productDescription, Double productPrice, String productImgSrc) {
        this.productId = productId;
        WarehouseItem = warehouseItem;
        this.category = category;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImgSrc = productImgSrc;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public WarehouseItem getWarehouseItem() {
        return WarehouseItem;
    }

    public void setWarehouseItem(WarehouseItem warehouseItem) {
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

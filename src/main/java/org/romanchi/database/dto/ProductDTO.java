package org.romanchi.database.dto;

import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.WarehouseItem;

import java.util.Objects;

public class ProductDTO {
    private long productId;
    private long wareHouserItemId;
    private Integer warehouseItemQuantity;
    private long categoryId;
    private String categoryName;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImgSrc;


    public ProductDTO(long productId, long wareHouserItemId, Integer warehouseItemQuantity, long categoryId, String categoryName, String productName, String productDescription, Double productPrice, String productImgSrc) {
        this.productId = productId;
        this.wareHouserItemId = wareHouserItemId;
        this.warehouseItemQuantity = warehouseItemQuantity;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
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

    public long getWareHouserItemId() {
        return wareHouserItemId;
    }

    public void setWareHouserItemId(long wareHouserItemId) {
        this.wareHouserItemId = wareHouserItemId;
    }

    public Integer getWarehouseItemQuantity() {
        return warehouseItemQuantity;
    }

    public void setWarehouseItemQuantity(Integer warehouseItemQuantity) {
        this.warehouseItemQuantity = warehouseItemQuantity;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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


}

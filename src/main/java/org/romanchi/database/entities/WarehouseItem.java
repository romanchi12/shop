package org.romanchi.database.entities;

public class WarehouseItem {
    private long warehouseItemId;
    private Integer warehouseItemQuantity;

    public WarehouseItem() {
    }

    public long getWarehouseItemId() {
        return warehouseItemId;
    }

    public void setWarehouseItemId(long warehouseItemId) {
        this.warehouseItemId = warehouseItemId;
    }

    public Integer getWarehouseItemQuantity() {
        return warehouseItemQuantity;
    }

    public void setWarehouseItemQuantity(Integer warehouseItemQuantity) {
        this.warehouseItemQuantity = warehouseItemQuantity;
    }

    @Override
    public String toString() {
        return "WarehouseItem{" +
                "warehouseItemId=" + warehouseItemId +
                ", warehouseItemQuantity='" + warehouseItemQuantity + '\'' +
                '}';
    }
}

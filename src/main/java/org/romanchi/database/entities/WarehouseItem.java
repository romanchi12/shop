package org.romanchi.database.entities;

public class WarehouseItem {
    private long warehouseItemId;
    private Double warehouseItemQuantity;

    public WarehouseItem() {
    }

    public long getWarehouseItemId() {
        return warehouseItemId;
    }

    public void setWarehouseItemId(long warehouseItemId) {
        this.warehouseItemId = warehouseItemId;
    }

    public Double getWarehouseItemQuantity() {
        return warehouseItemQuantity;
    }

    public void setWarehouseItemQuantity(Double warehouseItemQuantity) {
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

package org.romanchi.database.entities;

public class Warehouse {
    private Long warehouseItemId;
    private String warehouseItemQuantity;

    public Warehouse() {
    }

    public Long getWarehouseItemId() {
        return warehouseItemId;
    }

    public void setWarehouseItemId(Long warehouseItemId) {
        this.warehouseItemId = warehouseItemId;
    }

    public String getWarehouseItemQuantity() {
        return warehouseItemQuantity;
    }

    public void setWarehouseItemQuantity(String warehouseItemQuantity) {
        this.warehouseItemQuantity = warehouseItemQuantity;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseItemId=" + warehouseItemId +
                ", warehouseItemQuantity='" + warehouseItemQuantity + '\'' +
                '}';
    }
}

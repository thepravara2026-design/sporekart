package com.sporekart.operations.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.operations.copilot")
public class OperationsCopilotConfig {

    private String defaultWarehouse = "warehouse-main";
    private String defaultRegion = "all-india";
    private int lowStockThresholdDays = 14;
    private int criticalStockThresholdDays = 7;
    private int deadStockDays = 180;
    private String defaultShippingZone = "domestic";
    private double warehouseCapacityWarningPct = 85.0;
    private int fulfillmentSlaHours = 24;
    private int shippingSlaHours = 48;

    public String getDefaultWarehouse() { return defaultWarehouse; }
    public void setDefaultWarehouse(String defaultWarehouse) { this.defaultWarehouse = defaultWarehouse; }
    public String getDefaultRegion() { return defaultRegion; }
    public void setDefaultRegion(String defaultRegion) { this.defaultRegion = defaultRegion; }
    public int getLowStockThresholdDays() { return lowStockThresholdDays; }
    public void setLowStockThresholdDays(int lowStockThresholdDays) { this.lowStockThresholdDays = lowStockThresholdDays; }
    public int getCriticalStockThresholdDays() { return criticalStockThresholdDays; }
    public void setCriticalStockThresholdDays(int criticalStockThresholdDays) { this.criticalStockThresholdDays = criticalStockThresholdDays; }
    public int getDeadStockDays() { return deadStockDays; }
    public void setDeadStockDays(int deadStockDays) { this.deadStockDays = deadStockDays; }
    public String getDefaultShippingZone() { return defaultShippingZone; }
    public void setDefaultShippingZone(String defaultShippingZone) { this.defaultShippingZone = defaultShippingZone; }
    public double getWarehouseCapacityWarningPct() { return warehouseCapacityWarningPct; }
    public void setWarehouseCapacityWarningPct(double warehouseCapacityWarningPct) { this.warehouseCapacityWarningPct = warehouseCapacityWarningPct; }
    public int getFulfillmentSlaHours() { return fulfillmentSlaHours; }
    public void setFulfillmentSlaHours(int fulfillmentSlaHours) { this.fulfillmentSlaHours = fulfillmentSlaHours; }
    public int getShippingSlaHours() { return shippingSlaHours; }
    public void setShippingSlaHours(int shippingSlaHours) { this.shippingSlaHours = shippingSlaHours; }
}

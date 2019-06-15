package com.fazemeright.canteen_app_models.models;

//TODO: Remove class and its usages
public class OrderDetailItem {
    private String orderItemName, orderItemPrice, orderItemCategory,
            orderItemStatus;
    private int orederItemQuantity;

    public OrderDetailItem(String orderItemName, String orderItemPrice, int orederItemQuantity, String orderItemCategory, String orderItemStatus) {
        this.orderItemName = orderItemName;
        this.orderItemPrice = orderItemPrice;
        this.orederItemQuantity = orederItemQuantity;
        this.orderItemCategory = orderItemCategory;
        this.orderItemStatus = orderItemStatus;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public String getOrderItemPrice() {
        return orderItemPrice;
    }

    public int getOrederItemQuantity() {
        return orederItemQuantity;
    }

    public String getOrderItemCategory() {
        return orderItemCategory;
    }

    public String getOrderItemStatus() {
        return orderItemStatus;
    }
}

package com.example.getfood.models;

public class OrderListItem {
    private String orderID, orderTime, orderAmount;

    public OrderListItem(String orderID, String orderTime, String orderAmount) {
        this.orderID = orderID;
        this.orderTime = orderTime;
        this.orderAmount = orderAmount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }
}

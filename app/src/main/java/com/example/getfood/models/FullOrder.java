package com.example.getfood.models;

import java.io.Serializable;
import java.util.ArrayList;

public class FullOrder implements Serializable {
    private ArrayList<CartItem> cartItems;
    private String orderAmount;
    private String timeToDeliver;
    private String rollNo;
    private String orderId;
    private String orderStatus;
    private String displayID;

    public FullOrder(ArrayList<CartItem> cartItems, String orderAmount, String timeToDeliver, String rollNo, String orderId, String status) {
        this.cartItems = cartItems;
        this.orderAmount = orderAmount;
        this.timeToDeliver = timeToDeliver;
        this.rollNo = rollNo;
        this.orderId = orderId;
        this.orderStatus = status;
    }

    public FullOrder() {
    }

    public String getDisplayID() {
        return displayID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getTimeToDeliver() {
        return timeToDeliver;
    }

    public String getRollNo() {
        return rollNo;
    }
}

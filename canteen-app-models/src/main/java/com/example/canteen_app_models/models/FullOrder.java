package com.example.canteen_app_models.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * It is the complete order fetched directly from the FireBase Real-time database
 */
public class FullOrder implements Serializable {
    private ArrayList<CartItem> orderItems;
    private String orderAmount;
    private String timeToDeliver;
    private String rollNo;
    private String orderId;
    private String orderStatus;
    private String displayID;

    public FullOrder(ArrayList<CartItem> cartItems, String orderAmount, String timeToDeliver, String rollNo, String orderId, String status) {
        this.orderItems = cartItems;
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

    public ArrayList<CartItem> getOrderItems() {
        return orderItems;
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

    @NonNull
    @Override
    public String toString() {
        StringBuilder itemData = new StringBuilder();
        for (CartItem item : orderItems) {
            /*itemData = itemData.concat("\n\t\t\t Item Name: " + item.getItemName() + " Item Quantity: " + item.getItemQuantity()
                    + " Item Category: " + item.getItemCategory() + " Item Price: " + item.getItemPrice()
                    + " Item Status: " + item.getItemStatus() + " Item Rating: " + item.getItemRating());*/

            itemData.append(item.toString());

        }
        return "\n Display ID: " + displayID + " Order ID: " + orderId
                + "\n Order Amount: " + orderAmount + " Time to Deliver: " + timeToDeliver
                + "\n Roll No: " + rollNo + " Order Status: " + orderStatus
                + "\n Order Items Size: " + orderItems.size() + " Order Status: " + orderStatus
                + "\n Item Data : " + itemData.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullOrder fullOrder = (FullOrder) o;
        return Objects.equals(orderItems, fullOrder.orderItems) &&
                Objects.equals(orderAmount, fullOrder.orderAmount) &&
                Objects.equals(timeToDeliver, fullOrder.timeToDeliver) &&
                Objects.equals(rollNo, fullOrder.rollNo) &&
                Objects.equals(orderId, fullOrder.orderId) &&
                Objects.equals(orderStatus, fullOrder.orderStatus) &&
                Objects.equals(displayID, fullOrder.displayID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItems, orderAmount, timeToDeliver, rollNo, orderId, orderStatus, displayID);
    }
}

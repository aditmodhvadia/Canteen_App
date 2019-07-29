package com.example.getfood.data;

import com.fazemeright.canteen_app_models.models.CartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {

    private static ArrayList<CartItem> cartItems;
    private static DataManager dataManager;

    public static synchronized DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
            cartItems = new ArrayList<>();
        }
        return dataManager;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        DataManager.cartItems = cartItems;
    }

    public int getCartSize() {
        if (cartItems != null) {
            return cartItems.size();
        } else {
            return 0;
        }
    }

    public int getCartTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total = total + Integer.parseInt(item.getItemPrice()) * item.getItemQuantity();
        }
        return total;
    }

    public void increaseCartItemQuantity(int position) {
        cartItems.get(position).increaseQuantity();
    }

    public void sortCartItems() {
        Collections.sort(cartItems, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem o1, CartItem o2) {
                return o1.getItemCategory().length() - o2.getItemCategory().length();
            }
        });
    }

    public void clearCartItems() {
        cartItems.clear();
    }
}

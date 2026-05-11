package org.example.model;

public class Order {

    private final String orderId;
    private final String restaurantId;
    private final String foodItemId;


    public Order(String orderId, String restaurantId, String foodItemId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.foodItemId = foodItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getFoodItemId() {
        return foodItemId;
    }
}

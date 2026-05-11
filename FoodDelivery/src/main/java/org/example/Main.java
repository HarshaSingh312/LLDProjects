package org.example;

public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        sol.init(new Helper05());

        sol.orderFood("order-0", "restaurant-0", "food-1");
        sol.rateOrder("order-0", 3);

        sol.orderFood("order-1", "restaurant-2", "food-0");
        sol.rateOrder("order-1", 1);

        sol.orderFood("order-2", "restaurant-1", "food-0");
        sol.rateOrder("order-2", 3);

        sol.orderFood("order-3", "restaurant-2", "food-0");
        sol.rateOrder("order-3", 5);

        sol.orderFood("order-4", "restaurant-0", "food-0");
        sol.rateOrder("order-4", 3);

        sol.orderFood("order-5", "restaurant-1", "food-0");
        sol.rateOrder("order-5", 4);

        sol.orderFood("order-6", "restaurant-0", "food-1");
        sol.rateOrder("order-6", 2);

        sol.orderFood("order-7", "restaurant-0", "food-1");
        sol.rateOrder("order-7", 2);

        sol.orderFood("order-8", "restaurant-1", "food-0");
        sol.rateOrder("order-8", 2);

        sol.orderFood("order-9", "restaurant-1", "food-0");
        sol.rateOrder("order-9", 4);

        System.out.println("getTopRestaurantsByFood('food-0'): " + sol.getTopRestaurantsByFood("food-0"));
        System.out.println("Expected: [restaurant-1, restaurant-0, restaurant-2]");

        System.out.println();

        System.out.println("getTopRestaurantsByFood('food-1'): " + sol.getTopRestaurantsByFood("food-1"));
        System.out.println("Expected: [restaurant-0, restaurant-2]");

        System.out.println();

        System.out.println("getTopRatedRestaurants(): " + sol.getTopRatedRestaurants());
        System.out.println("Expected: [restaurant-1, restaurant-2, restaurant-0]");
    }
}

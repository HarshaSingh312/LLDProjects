// ****** It's better to write code in your local code editor and paste it back here *********

import java.util.*;

class Order {
    private final String orderId;
    private final String restaurantId;
    private final String foodItemId;

    public Order(String orderId, String restaurantId, String foodItemId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.foodItemId = foodItemId;
    }

    public String getOrderId() { return orderId; }
    public String getRestaurantId() { return restaurantId; }
    public String getFoodItemId() { return foodItemId; }
}

class Restaurant {
    private final String name;
    private final HashMap<String, int[]> foodRating; // foodItemId -> [sum, count]
    private int ratingSum = 0;
    private int ratingCount = 0;

    public Restaurant(String name) {
        this.name = name;
        this.foodRating = new HashMap<>();
    }

    public double getRating() {
        return ratingCount == 0 ? 0 : (double) ratingSum / ratingCount;
    }

    public void addRating(String foodItemId, int rating) {
        int[] cur = foodRating.getOrDefault(foodItemId, new int[]{0, 0});
        cur[0] += rating;
        cur[1]++;
        foodRating.put(foodItemId, cur);
        ratingSum += rating;
        ratingCount++;
    }

    public double getFoodRating(String food) {
        if (!foodRating.containsKey(food)) return 0;
        int[] cur = foodRating.get(food);
        return (double) cur[0] / cur[1];
    }
}

class ResurantRating {
    String name;
    double rating;

    public ResurantRating(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() { return name; }
    public double getRating() { return rating; }
}

public class Solution implements Q05RestaurantRatingInterface {
    private Helper05 helper;
    HashMap<String, Restaurant> restaurants = new HashMap<>();
    HashMap<String, Order> orders = new HashMap<>();

    public Solution(){}

    public void init(Helper05 helper){
        this.helper=helper;
    }

    public void orderFood(String orderId, String restaurantId, String foodItemId) {
        restaurants.putIfAbsent(restaurantId, new Restaurant(restaurantId));
        orders.put(orderId, new Order(orderId, restaurantId, foodItemId));
    }

    /**
     * when you(customer) are rating an order e.g giving 4 stars to an orders
     * then it means you are assigning 4 stars to both the food item
     * in that restaurant as well as 4 stars to the overall restaurant rating.
     * - rating ranges from 1 to 5, 5 is best, 1 is worst
     */
    public void rateOrder(String orderId, int rating) {
        Restaurant restaurant = restaurants.get(orders.get(orderId).getRestaurantId());
        restaurant.addRating(orders.get(orderId).getFoodItemId(), rating);
    }

    /**
     * - Fetches a list of top 20 restaurants
     * - unrated restaurants will be at the bottom of list.
     * - restaurants are sorted in descending order on average ratings
     * of the food item and then based on restaurant id lexicographically
     * - ratings are rounded down to 1 decimal point,
     *  i.e. 4.05, 4.08, 4.11, 4.12, 4.14 all become 4.1,
     *    4.15, 4.19, 4.22, 4.24 all become 4.2
     * - e.g. 'food-item-1':  veg burger is rated 4.3 in restaurant-4
     * and 4.6 in restaurant-6 then we will return ['restaurant-6', 'restaurant-4']
     */
    public List<String> getTopRestaurantsByFood(String foodItemId) {
        List<ResurantRating> rated = new ArrayList<>();
        List<String> unrated = new ArrayList<>();
        for (String restaurant : restaurants.keySet()) {
            double rate = restaurants.get(restaurant).getFoodRating(foodItemId);
            if (rate > 0) {
                rated.add(new ResurantRating(restaurant, Math.round(rate * 10) / 10.0));
            } else {
                unrated.add(restaurant);
            }
        }
        rated.sort((a, b) -> {
            int cmp = Double.compare(b.getRating(), a.getRating());
            return cmp != 0 ? cmp : a.getName().compareTo(b.getName());
        });
        Collections.sort(unrated);
        List<String> result = new ArrayList<>();
        for (ResurantRating r : rated) result.add(r.getName());
//        result.addAll(unrated);
        return result.size() > 20 ? result.subList(0, 20) : result;
    }

    /**
     * - Here we are talking about restaurant's overall rating and NOT food item's rating.
     */
    public List<String> getTopRatedRestaurants() {
        List<ResurantRating> rated = new ArrayList<>();
        List<String> unrated = new ArrayList<>();
        for (String restaurant : restaurants.keySet()) {
            double rate = restaurants.get(restaurant).getRating();
            if (rate > 0) {
                rated.add(new ResurantRating(restaurant, Math.round(rate * 10) / 10.0));
            } else {
                unrated.add(restaurant);
            }
        }
        rated.sort((a, b) -> {
            int cmp = Double.compare(b.getRating(), a.getRating());
            return cmp != 0 ? cmp : a.getName().compareTo(b.getName());
        });
        Collections.sort(unrated);
        List<String> result = new ArrayList<>();
        for (ResurantRating r : rated) result.add(r.getName());
//        result.addAll(unrated);
        return result.size() > 20 ? result.subList(0, 20) : result;
    }
}

/**
interface Q05RestaurantRatingInterface {
    void init(Helper05 helper);
    void orderFood(String orderId, String restaurantId, String foodItemId);
    void rateOrder(String orderId, int rating);
    List<String> getTopRestaurantsByFood(String foodItemId);
    List<String> getTopRatedRestaurants();
}

class Helper05 {
    void print(String s){System.out.print(s);}
    void println(String s){System.out.println(s);}
}
*/

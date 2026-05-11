package org.example.model;

import java.util.HashMap;

public class Restaurant {

    private final String name;
    private final HashMap<String, Rate> foodRating;

    public float getRating() {
        return rating;
    }

    private float rating = 0;
    private float ratingNumbers = 0;

    public Restaurant(String name) {
        this.name = name;
        this.foodRating = new HashMap<>();
    }

    public void addRating(String foodItemId, int rating) {
        Rate currentRating = foodRating.getOrDefault(foodItemId, new Rate(0, 0));
        float currFoodRating = currentRating.getRate();
        int currFoodRatingUsers = currentRating.getUsersCount();
        float newFoodRating = ((currFoodRating * currFoodRatingUsers) + rating)/ (currFoodRatingUsers + 1);
        currentRating.setRate(newFoodRating);
        currentRating.setUsersCount(currFoodRatingUsers + 1);
        foodRating.put(foodItemId, currentRating);

        this.rating = ((this.rating * ratingNumbers) + rating)/(ratingNumbers + 1);
        this.ratingNumbers++;
    }

    public HashMap<String, Rate> getFoodRating() {
        return foodRating;
    }

    public float getFoodRating(String food) {
        if (!foodRating.containsKey(food)) return 0;
        return foodRating.get(food).getRate();
    }
}

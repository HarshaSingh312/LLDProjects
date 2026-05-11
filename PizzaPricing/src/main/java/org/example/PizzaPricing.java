package org.example;

import org.example.model.Topping;

import java.util.Objects;

import static org.example.ToppingFactory.getToppingInstance;

public class PizzaPricing {

    private int basePrice;
    private float taxPercentage;
    private String size;
    private boolean canBeAdded = true;
    private boolean cheeseBustAlreadyAdded = false;

    public PizzaPricing(int basePrice, int taxPercentage, String size) {
        this.basePrice = basePrice;
        this.taxPercentage = taxPercentage;
        this.size = size;
    }

    public boolean addTopping(String topping, int servingsCount) {
        Topping topping1 = getToppingInstance(topping);
        if (Objects.isNull(topping1)) return false;
        if (("mushroom".equals(topping) || "cheeseburst".equals(topping))) {
            if (canBeAdded) {
                canBeAdded = false;
            } else {
                return false;
            }
        }
        if ("cheeseburst".equals(topping) && !cheeseBustAlreadyAdded) {
            taxPercentage = (taxPercentage * 130) /100;
            cheeseBustAlreadyAdded = true;
        }
        basePrice += topping1.addPrice() * servingsCount;
        return true;
    }

    public int getFinalPrice() {
        System.out.println(basePrice + " " + taxPercentage);
        float taxValue = (float) (basePrice * (100 + taxPercentage)) / 100 ;
        System.out.println("price " + taxValue);
        float updatedValue = (float) (taxValue + 0.5);
        if ((int) taxValue != (int) updatedValue) {
            return (int) (updatedValue);
        }
        return (int) (taxValue);
    }
}

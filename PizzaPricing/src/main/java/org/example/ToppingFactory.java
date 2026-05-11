package org.example;

import org.example.model.*;

public class ToppingFactory {

    public static Topping getToppingInstance(String topping) {
        switch (topping) {
            case "cheeseburst": return new Cheeseburst();
            case "corn": return new Corn();
            case "onion": return new Onion();
            case "capsicum": return new Capsicum();
            case "pineapple": return new Pineapple();
            case "mushroom": return new Mushroom();
            default: return null;
        }
    }
}

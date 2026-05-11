package org.example;

import org.example.service.ShoppingCart;

import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                "coca-cola-pack,8,12",
                "juice-box,5,10"
        ));

        System.out.println(cart.addItem("coca-cola-pack", 5)); // "SUCCESS"
//        System.out.println(cart.addItem("juice-box", 3));
        System.out.println(cart.addItem("coca-cola-pack", 1)); // "SUCCESS"
        System.out.println(cart.addItem("coca-cola-pack", 2)); // "SUCCESS"

        List<String> view = cart.viewCart();
        System.out.println(view);
// view = ["coca-cola-pack,2", "juice-box,3"]  (sorted by itemId)

        System.out.println(cart.checkout());
// total = 2*8 + 3*5 = 31
// cart is now empty; cart.viewCart() returns []

//        ShoppingCart cart2 = new ShoppingCart(Arrays.asList(
//                "laptop,900,1"
//        ));
//
//        System.out.println(cart2.addItem("laptop", 2));
//// r1 = "OUT OF STOCK"
//// cart.viewCart() = []  (still empty)
//
//        ShoppingCart cart3 = new ShoppingCart(Arrays.asList(
//                "book,50,2"
//        ));
//
//        System.out.println(cart3.addItem("pen", 1));
//// r1 = "UNAVAILABLE"
//// cart.viewCart() = []  (cart still empty)
//
//        System.out.println(cart.checkout());
// total = -1  (cannot checkout empty cart)
    }
}

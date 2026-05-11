package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        PizzaPricing p = new PizzaPricing(500, 15, "medium");

//        Compute final price
        System.out.println(p.getFinalPrice());  // 407

//        Calculation: base=200; toppings=100 + 2×30=160; subtotal=360;
//        tax=13% of 360=46.8; final=406.8 → round half up ⇒ 407

        PizzaPricing q = new PizzaPricing(350, 8, "medium");

//        Add toppings
        System.out.println(q.addTopping("mushroom", 2));      // true (2×40=80)
        System.out.println(q.addTopping("cheeseburst", 1));   // false (blocked by mutual exclusion)

//        Compute final price
        System.out.println(q.getFinalPrice());    // 464

//        Calculation: base=350; toppings=80; subtotal=430;
//        tax=8% of 430=34.4; final=464.4 → round half up ⇒ 464

    }
}

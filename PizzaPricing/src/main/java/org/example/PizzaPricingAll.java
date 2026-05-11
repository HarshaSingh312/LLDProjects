package org.example;

import java.util.*;

// -------------------- CORE --------------------
interface Pizza {
    int getPrice();
    Set<ToppingType> getAppliedToppings();
}

// Base pizza
class BasePizza implements Pizza {
    private final int basePrice;

    public BasePizza(int basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public int getPrice() {
        return basePrice;
    }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        return new HashSet<>();
    }
}

// -------------------- DECORATOR --------------------
abstract class ToppingDecorator implements Pizza {
    protected Pizza pizza;

    public ToppingDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        return pizza.getAppliedToppings();
    }
}

// -------------------- TOPPINGS --------------------
enum ToppingType {
    CHEESEBURST, CORN, ONION, CAPSICUM, PINEAPPLE, MUSHROOM
}

class CheeseBurst extends ToppingDecorator {

    public CheeseBurst(Pizza pizza) {
        super(pizza);
    }

    @Override
    public int getPrice() {
        return pizza.getPrice() + 100;
    }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        Set<ToppingType> set = new HashSet<>(pizza.getAppliedToppings());
        set.add(ToppingType.CHEESEBURST);
        return set;
    }
}

class Mushroom extends ToppingDecorator {
    public Mushroom(Pizza pizza) { super(pizza); }

    @Override
    public int getPrice() {
        return pizza.getPrice() + 40;
    }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        Set<ToppingType> set = new HashSet<>(pizza.getAppliedToppings());
        set.add(ToppingType.MUSHROOM);
        return set;
    }
}

class Corn extends ToppingDecorator {
    public Corn(Pizza pizza) { super(pizza); }

    @Override
    public int getPrice() { return pizza.getPrice() + 50; }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        Set<ToppingType> set = new HashSet<>(pizza.getAppliedToppings());
        set.add(ToppingType.CORN);
        return set;
    }
}

class Onion extends ToppingDecorator {
    public Onion(Pizza pizza) { super(pizza); }

    @Override
    public int getPrice() { return pizza.getPrice() + 30; }

    @Override
    public Set<ToppingType> getAppliedToppings() {
        Set<ToppingType> set = new HashSet<>(pizza.getAppliedToppings());
        set.add(ToppingType.ONION);
        return set;
    }
}

// -------------------- FACTORY --------------------
class ToppingFactory {
    public static Pizza addTopping(Pizza pizza, ToppingType type) {
        switch (type) {
            case CHEESEBURST: return new CheeseBurst(pizza);
            case MUSHROOM: return new Mushroom(pizza);
            case CORN: return new Corn(pizza);
            case ONION: return new Onion(pizza);
            default: throw new IllegalArgumentException("Invalid topping");
        }
    }
}

// -------------------- RULE ENGINE --------------------
interface ToppingRule {
    boolean validate(Set<ToppingType> applied, ToppingType newTopping);
}

class CheeseBurstMushroomRule implements ToppingRule {
    @Override
    public boolean validate(Set<ToppingType> applied, ToppingType newTopping) {
        if (newTopping == ToppingType.CHEESEBURST && applied.contains(ToppingType.MUSHROOM))
            return false;
        if (newTopping == ToppingType.MUSHROOM && applied.contains(ToppingType.CHEESEBURST))
            return false;
        return true;
    }
}

class RuleEngine {
    private final List<ToppingRule> rules = List.of(
            new CheeseBurstMushroomRule()
    );

    public boolean validate(Set<ToppingType> applied, ToppingType newTopping) {
        for (ToppingRule rule : rules) {
            if (!rule.validate(applied, newTopping)) return false;
        }
        return true;
    }
}

// -------------------- TAX STRATEGY --------------------
interface TaxStrategy {
    int applyTax(int price);
}

class DefaultTaxStrategy implements TaxStrategy {
    private final int taxPercent;

    public DefaultTaxStrategy(int taxPercent) {
        this.taxPercent = taxPercent;
    }

    @Override
    public int applyTax(int price) {
        double finalPrice = price * (1 + taxPercent / 100.0);
        return (int) Math.round(finalPrice);
    }
}

// -------------------- SERVICE --------------------
class PizzaService {
    private Pizza pizza;
    private final RuleEngine ruleEngine;
    private final TaxStrategy taxStrategy;

    public PizzaService(int basePrice, TaxStrategy taxStrategy) {
        this.pizza = new BasePizza(basePrice);
        this.ruleEngine = new RuleEngine();
        this.taxStrategy = taxStrategy;
    }

    public boolean addTopping(ToppingType type) {
        if (!ruleEngine.validate(pizza.getAppliedToppings(), type)) {
            return false;
        }
        pizza = ToppingFactory.addTopping(pizza, type);
        return true;
    }

    public int getFinalPrice() {
        return taxStrategy.applyTax(pizza.getPrice());
    }
}

// -------------------- MAIN --------------------
public class Main {
    public static void main(String[] args) {
        PizzaService service = new PizzaService(200, new DefaultTaxStrategy(10));

        service.addTopping(ToppingType.CORN);
        service.addTopping(ToppingType.ONION);
        service.addTopping(ToppingType.CHEESEBURST);

        System.out.println(service.getFinalPrice());
    }
}
package org.example.model;

import java.util.HashMap;
import java.util.Objects;

public class Inventory {
    private HashMap<String, Product> products;

    public void addInventory(int productId, String sellerId, int delta) {
        String productSeller = productId + "-" + sellerId;
        Product product = products.getOrDefault(productSeller, null);
        if (Objects.isNull(product)) {
            product = new Product(productId, sellerId, delta);
        } else {
            product.setCount(delta);
        }
        products.put(productSeller, product);
    }

    public void reduceInventory(int productId, String sellerId, int delta) {
        String productSeller = productId + "-" + sellerId;
        Product product = products.get(productSeller);
        product.setCount(delta * -1);
        products.put(productSeller, product);
    }

    public int getInventory(int productId, String sellerId) {
        String productSeller = productId + "-" + sellerId;
        if (!products.containsKey(productSeller)) return 0;
        Product product = products.get(productSeller);
        return product.getCount();
    }



}

package org.example;

import java.util.*;

public class Solution1 {

    static class Seller {
        private String id;
        private List<String> pincodes;
        private List<String> paymentModes;

        public Seller(String id, List<String> pincodes, List<String> paymentModes) {
            this.id = id;
            this.pincodes = pincodes;
            this.paymentModes = paymentModes;
        }
    }

    static class Product {
        private int name;
        private String seller;
        private int count;

        public Product(int name, String seller, int count) {
            this.name = name;
            this.seller = seller;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count += count;
        }
    }

    static class Inventory {
        private HashMap<String, Product> products;

        private void addInventory(int productId, String sellerId, int delta) {
            String productSeller = productId + "-" + sellerId;
            Product product = products.getOrDefault(productSeller, null);
            if (Objects.isNull(product)) {
                product = new Product(productId, sellerId, delta);
            } else {
                product.setCount(delta);
            }
        }
    }

    static class Order {
        private String orderId;
        private String destinationPincode;
        private String sellerId;
        private int productId;
        private int productCount;
        private String paymentMode;

        public Order(String orderId, String destinationPincode, String sellerId, int productId, int productCount, String paymentMode) {
            this.orderId = orderId;
            this.destinationPincode = destinationPincode;
            this.sellerId = sellerId;
            this.productId = productId;
            this.productCount = productCount;
            this.paymentMode = paymentMode;
        }
    }
}

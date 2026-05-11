package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

class Product {

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

 class Order {

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

    class Seller {
         private String id;

         public List<String> getPincodes() {
             return pincodes;
         }

         public List<String> getPaymentModes() {
             return paymentModes;
         }


         private List<String> pincodes;
         private List<String> paymentModes;

         public Seller(String id, List<String> pincodes, List<String> paymentModes) {
             this.id = id;
             this.pincodes = pincodes;
             this.paymentModes = paymentModes;
         }
     }

     class Inventory {
         private ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

         public void addInventory(int productId, String sellerId, int delta) {
             String productSeller = productId + "-" + sellerId;
             products.compute(productSeller, (k,v) -> {
                 if (Objects.isNull(v)) return new Product(productId, sellerId, delta);
                 v.setCount(delta);
                 return v;
             });
         }

         public void reduceInventory(int productId, String sellerId, int delta) {
             String productSeller = productId + "-" + sellerId;
             products.computeIfPresent(productSeller, (k, v) -> {
                 v.setCount(delta * -1);
                 return v;
             });
         }

         public int getInventory(int productId, String sellerId) {
             String productSeller = productId + "-" + sellerId;
             if (!products.containsKey(productSeller)) return 0;
             Product product = products.get(productSeller);
             return product.getCount();
         }
     }


     // use helper.print("") or helper.println("") for printing logs else logs will not be visible.
public class Solution implements Q04EcommerceOrdersInterface {
    private Helper04 helper;
    HashMap<String, Seller> sellers = new HashMap<>();
    Inventory inventory = new Inventory();
    HashMap<String, Order> orders = new HashMap<>();
    ReentrantLock lock = new ReentrantLock();

    public Solution(){}

    public void init(Helper04 helper, int productsCount){
        this.helper=helper;
        // helper.println("e-commerce orders module initialized");
    }

    //paymentModes it will be always one of  "cash", "upi", "netbanking", "debit card" and "credit card"
    public void createSeller(String sellerId, List<String> serviceablePincodes, List<String> paymentModes) {
        sellers.put(sellerId, new Seller(sellerId, serviceablePincodes, paymentModes));
    }

    //returns "inventory added", "product doesn't exist", "seller doesn't exist"
    public void addInventory(int productId, String sellerId, int delta) {
        if (!sellers.containsKey(sellerId)) return;
        inventory.addInventory(productId, sellerId, delta);
    }

    //if the product or seller doesn't exist then returns 0
    public int getInventory(int productId, String sellerId) {
        return inventory.getInventory(productId, sellerId);
    }

    // returns (in that order) : "order placed" or "pincode unserviceable"
    // or "payment mode not supported" or "insufficient product inventory"
    public String createOrder(String orderId, String destinationPincode, String sellerId,
                              int productId, int productCount, String paymentMode) {
        // Validate seller
        if (!sellers.containsKey(sellerId)) return "seller doesn't exist";
        // Validate payment
        Seller seller = sellers.get(sellerId);
        if (!seller.getPaymentModes().contains(paymentMode)) return "payment mode not supported";
        // Validate Pincode
        if (!seller.getPincodes().contains(destinationPincode)) return "pincode unserviceable";
        // Validate product count
        try {
            lock.lock();
            int count = getInventory(productId, sellerId);
            if (productCount > count) return "insufficient product inventory";
            // Reduce inventory
            inventory.reduceInventory(productId, sellerId, productCount);
            // Place order
            Order order = new Order(orderId, destinationPincode, sellerId, productId, productCount, paymentMode);
            orders.put(orderId, order);
            return "order placed";
        } finally {
            lock.unlock();
        }
    }

}

// uncomment below code in case you are using your local ide like intellij, eclipse etc and
// comment it back again back when you are pasting completed solution in the online CodeZym editor.
// if you don't comment it back, you will get "java.lang.AssertionError: java.lang.LinkageError"
// This will help avoid unwanted compilation errors and get method autocomplete in your local code editor.
///*
interface Q04EcommerceOrdersInterface {

    public void init(Helper04 helper, int productsCount);

    void createSeller(String sellerId, List<String> serviceablePincodes, List<String> paymentModes);

    void addInventory(int productId, String sellerId, int delta);

    int getInventory(int productId, String sellerId);

    String createOrder(String orderId, String destinationPincode, String sellerId,
                       int productId, int productCount, String paymentMode);

}

class Helper04 {
    void print(String s){System.out.print(s);}
    void println(String s){System.out.println(s);}
}
//*/

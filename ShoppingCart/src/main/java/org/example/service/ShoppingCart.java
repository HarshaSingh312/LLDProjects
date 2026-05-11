package org.example.service;

import java.util.*;
import java.util.stream.Collectors;

class utils {

    public static List<Item> getItemFromString(List<String> items) {
        List<Item> updatedItems = new ArrayList<>();
        for (String items1: items) {
            String[] itemParts = items1.split(",");
            updatedItems.add(new Item(itemParts[0], Integer.parseInt(itemParts[2]), Integer.parseInt(itemParts[1])));
        }
        return updatedItems;
    }
}

class Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String id;

    public int getCount() {
        return count;
    }

    public void addCount(int count) {
//        System.out.println("AddCount " + this.count);
        this.count += count;
//        System.out.println("AddCount " + this.count);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int count;
    private int price;

    public Item(String id, int count, int price) {
        this.id = id;
        this.count = count;
        this.price = price;
    }


}


class ItemService {
    HashMap<String, Item> itemDB = new HashMap<>();

    public HashMap<String, Item> getItemDB() {
        return itemDB;
    }

    public void loadData(List<Item> items) {
        for (Item item: items) {
//            System.out.println(item.getId());
            itemDB.put(item.getId(), item);
        }
    }

    public boolean isItemPresent(String itemId) {
        return itemDB.containsKey(itemId);
    }

    public Item getItem(String itemId) {
        return itemDB.get(itemId);
    }

    public int getItemCount(String itemId) {
        return itemDB.get(itemId).getCount();
    }

    public void setItemCount(String itemId, int count) {
        Item item = itemDB.get(itemId);
        item.addCount(count);
        itemDB.put(itemId, item);
//        System.out.println("updated item " + itemDB.get(itemId).getCount());
    }
}


public class ShoppingCart {

    ItemService itemService;
    List<Item> cartItems = new ArrayList<>();

    public ShoppingCart(List<String> items) {
        itemService = new ItemService();
        itemService.loadData(utils.getItemFromString(items));
    }

    public String addItem(String itemId, int count) {
        if (!itemService.isItemPresent(itemId)) return "UNAVAILABLE";
//        System.out.println("Item count " + itemId + " : " + itemService.getItemCount(itemId));
        if (itemService.getItemCount(itemId) < count) return "OUT OF STOCK";
        itemService.setItemCount(itemId, count * -1);
//        System.out.println("Item count " + itemId + " : " + itemService.getItemCount(itemId));
        Item dbItem = itemService.getItem(itemId);
        List<Item> existingItems = cartItems.stream().filter(item -> Objects.equals(item.getId(), itemId)).collect(Collectors.toList());
        Item existingItem;
        if (!existingItems.isEmpty()) {
            existingItem = existingItems.get(0);
            existingItem.addCount(count);
        } else {
            cartItems.add(new Item(dbItem.getId(), count, dbItem.getPrice()));
        }
        return "SUCCESS";
    }

    public List<String> viewCart() {
        cartItems.sort(Comparator.comparing(Item::getId));
        List<String> cartItems = new ArrayList<>();
        for (Item item: this.cartItems) {
            cartItems.add(item.getId() + "," + item.getCount());
        }
        return cartItems;
    }

    public int checkout() {
        int count = 0;
        for (Item item: this.cartItems) {
//            System.out.println(item.getId() + ", " + item.getPrice());
           count += item.getCount() * item.getPrice();
        }
        cartItems = new ArrayList<>();
        return count == 0? -1: count;
    }
}
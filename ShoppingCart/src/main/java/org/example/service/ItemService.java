//package org.example.service;
//
//import org.example.model.Item;
//
//import java.util.HashMap;
//import java.util.List;
//
//public class ItemService {
//    HashMap<String, Item> itemDB = new HashMap<>();
//
//    public HashMap<String, Item> getItemDB() {
//        return itemDB;
//    }
//
//    public void loadData(List<Item> items) {
//        for (Item item: items) {
//            System.out.println(item.getId());
//            itemDB.put(item.getId(), item);
//        }
//    }
//
//    public boolean isItemPresent(String itemId) {
//        return itemDB.containsKey(itemId);
//    }
//
//    public Item getItem(String itemId) {
//        return itemDB.get(itemId);
//    }
//
//    public int getItemCount(String itemId) {
//        return itemDB.get(itemId).getCount();
//    }
//
//    public void setItemCount(String itemId, int count) {
//        itemDB.get(itemId).addCount(count);
//    }
//
//}

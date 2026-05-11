package org.example;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Sequential Tests ===");
//        sequentialTests();

        System.out.println("\n=== Multithreaded Tests ===");
        multithreadedTests();
    }

    static void sequentialTests() {
        Solution solution = new Solution();
        Helper04 helper = new Helper04();
        solution.init(helper, 10);

        // createSeller
        solution.createSeller("seller-1", List.of("452001", "452002", "452003"), List.of("cash", "upi", "netbanking"));
        solution.createSeller("seller-2", List.of("452001", "452004"), List.of("debit card", "credit card"));
        System.out.println("Sellers created");

        // addInventory
        solution.addInventory(1, "seller-1", 50);
        solution.addInventory(2, "seller-1", 30);
        solution.addInventory(1, "seller-2", 20);
        solution.addInventory(3, "seller-unknown", 10); // seller doesn't exist
        System.out.println("Inventory added");

        // getInventory
        System.out.println("Inventory(1, seller-1): " + solution.getInventory(1, "seller-1")); // 50
        System.out.println("Inventory(2, seller-1): " + solution.getInventory(2, "seller-1")); // 30
        System.out.println("Inventory(1, seller-2): " + solution.getInventory(1, "seller-2")); // 20
        System.out.println("Inventory(99, seller-1): " + solution.getInventory(99, "seller-1")); // 0

        // createOrder - success
        System.out.println("Order1: " + solution.createOrder("order-1", "452001", "seller-1", 1, 5, "cash")); // order placed

        // createOrder - pincode unserviceable
        System.out.println("Order2: " + solution.createOrder("order-2", "999999", "seller-1", 1, 5, "cash")); // pincode unserviceable

        // createOrder - payment mode not supported
        System.out.println("Order3: " + solution.createOrder("order-3", "452001", "seller-1", 1, 5, "credit card")); // payment mode not supported

        // createOrder - insufficient inventory
        System.out.println("Order4: " + solution.createOrder("order-4", "452001", "seller-1", 1, 1000, "cash")); // insufficient product inventory

        // createOrder - seller doesn't exist
        System.out.println("Order5: " + solution.createOrder("order-5", "452001", "seller-unknown", 1, 1, "cash")); // seller doesn't exist

        // verify inventory reduced after order-1
        System.out.println("Inventory(1, seller-1) after order: " + solution.getInventory(1, "seller-1")); // 45
    }

    static void multithreadedTests() throws InterruptedException {
//        Solution solution = new Solution();
//        Helper04 helper = new Helper04();
//        solution.init(helper, 10);
//
//        solution.createSeller("seller-1", List.of("452001", "452002"), List.of("cash", "upi"));
//        solution.addInventory(1, "seller-1", 1000);
//
//        int threadCount = 10;
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        // Concurrent addInventory - 10 threads each adding 10 units
//        System.out.println("\n-- Concurrent addInventory --");
//        for (int i = 0; i < threadCount; i++) {
//            executor.submit(() -> {
//                solution.addInventory(2, "seller-1", 10);
//                latch.countDown();
//            });
//        }
//        latch.await();
//        System.out.println("Inventory(2, seller-1) expected ~100: " + solution.getInventory(2, "seller-1"));
//
//        // Concurrent createOrder - 10 threads each ordering 10 units from 1000 stock
//        System.out.println("\n-- Concurrent createOrder --");
//        CountDownLatch orderLatch = new CountDownLatch(threadCount);
//        for (int i = 0; i < threadCount; i++) {
//            final int idx = i;
//            executor.submit(() -> {
//                String result = solution.createOrder("mt-order-" + idx, "452001", "seller-1", 1, 10, "cash");
//                System.out.println("Thread-" + idx + " createOrder: " + result);
//                orderLatch.countDown();
//            });
//        }
//        orderLatch.await();
//        System.out.println("Inventory(1, seller-1) after concurrent orders expected ~900: " + solution.getInventory(1, "seller-1"));
//
//        // Concurrent getInventory - 10 threads reading simultaneously
//        System.out.println("\n-- Concurrent getInventory --");
//        CountDownLatch readLatch = new CountDownLatch(threadCount);
//        for (int i = 0; i < threadCount; i++) {
//            final int idx = i;
//            executor.submit(() -> {
//                int inv = solution.getInventory(1, "seller-1");
//                System.out.println("Thread-" + idx + " getInventory: " + inv);
//                readLatch.countDown();
//            });
//        }
//        readLatch.await();
//
//        executor.shutdown();
//        System.out.println("\nAll multithreaded tests completed.");

        // TOCTOU Race Condition: inventory sufficient at read time, but depleted by another thread before order placement
        toctuRaceConditionTest();
    }

    /**
     * Simulates TOCTOU (Time-of-Check-Time-of-Use) race condition:
     * - Only 10 units of product-5 exist for seller-1
     * - Thread A and Thread B both want to order 10 units
     * - Both threads read inventory at the same time (both see 10 — sufficient)
     * - Both threads then place the order
     * - Only one should succeed, but due to the race, both may succeed → inventory goes negative
     */
    static void toctuRaceConditionTest() throws InterruptedException {
        System.out.println("\n=== TOCTOU Race Condition Test ===");

        Solution solution = new Solution();
        solution.init(new Helper04(), 10);
        solution.createSeller("seller-1", List.of("452001"), List.of("cash"));
        solution.addInventory(5, "seller-1", 10); // only 10 units available

        // CyclicBarrier ensures both threads reach the same point before proceeding
        CyclicBarrier readBarrier = new CyclicBarrier(2);  // sync after inventory read
        CyclicBarrier orderBarrier = new CyclicBarrier(2);  // sync before placing order

        Runnable task = (Runnable) () -> {
            String threadName = Thread.currentThread().getName();
            try {
                // Step 1: Both threads read inventory at the same time
                int available = solution.getInventory(5, "seller-1");
                System.out.println(threadName + " read inventory: " + available);

                readBarrier.await(); // wait for both threads to finish reading

                // Step 2: Both threads now attempt to place order simultaneously
                orderBarrier.await(); // ensure both threads call createOrder at the same instant

                String result = solution.createOrder(
                        "toctou-" + threadName, "452001", "seller-1", 5, 10, "cash");
                System.out.println(threadName + " createOrder result: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        Thread threadA = new Thread(task, "Thread-A");
        Thread threadB = new Thread(task, "Thread-B");
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        int finalInventory = solution.getInventory(5, "seller-1");
        System.out.println("Final inventory(5, seller-1): " + finalInventory);
        if (finalInventory < 0) {
            System.out.println("BUG DETECTED: Inventory went negative! Race condition exploited.");
        } else {
            System.out.println("No race condition observed in this run (try running again).");
        }
    }
}

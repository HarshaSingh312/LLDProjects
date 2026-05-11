package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws Exception {
//        PaymentSystem ps = new PaymentSystem();
//
//        // Case 1: Exact invoice ID match
//        String result1 = ps.reconcile("PAY1,100.00,memo1:INV1", List.of("INV1,2024-01-15,100.00", "INV2,2024-01-10,200.00"), 0);
//        System.out.println("Case 1 (exact match): " + result1);
//
//        // Case 2: No exact match, amount match picks earliest due date
//        String result2 = ps.reconcile("PAY2,100.00,memo2:INV99", List.of("INV3,2024-03-01,100.00", "INV4,2024-01-01,100.00"), 0);
//        System.out.println("Case 2 (amount match, earliest date): " + result2);
//
//        // Case 3: No match at all
//        String result3 = ps.reconcile("PAY3,500.00,memo3:INV99", List.of("INV5,2024-01-01,100.00"), 0);
//        System.out.println("Case 3 (no match): " + result3);
//
//        // Case 4: Amount match with forgiveness
//        String result4 = ps.reconcile("PAY4,95.00,memo4:INV99", List.of("INV6,2024-02-01,100.00"), 5);
//        System.out.println("Case 4 (forgiveness): " + result4);
//
//        // Case 5: Amount outside forgiveness range
//        String result5 = ps.reconcile("PAY5,90.00,memo5:INV99", List.of("INV7,2024-02-01,100.00"), 5);
//        System.out.println("Case 5 (outside forgiveness): " + result5);

//        PaymentProcessing ps = new PaymentProcessing();
//        ps.process();

//        Thread2 thread2 = new Thread2();
//        Thread thread3 = new Thread(thread2);
//        thread3.setDaemon(true);
//        thread3.start();

        ExecutorService service = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i< 10; i++) {
            int finalI = i;
            futures.add(service.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("i " + finalI);
                return finalI;
            }));
        }

        futures.forEach(future -> {
            try {
                System.out.println("Result " + future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        service.shutdown();



//        Thread1 thread1 = new Thread1("Intresting");
//        thread1.start();

//        System.out.println("Executing on thread " + Thread.currentThread().getName());
    }
}

class Thread1 extends Thread {

    public Thread1(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("Executing on Thread1 " + Thread.currentThread().getName());
    }
}

class Thread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("i " + i);
        }

        System.out.println("Executing on Thread2 " + Thread.currentThread().getName());
    }
}

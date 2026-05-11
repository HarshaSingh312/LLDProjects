package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {

        CarRentalService carRentalService = new CarRentalService();

        carRentalService.addCar("KA01AB1234", 1200, 100, 10);

        System.out.println(carRentalService.bookCar("ORD-1", "KA01AB1234", "2025-08-28", "2025-08-30"));  // 3 days (28,29,30)
        // true

        carRentalService.startTrip("ORD-1", 5000);

        System.out.println(carRentalService.endTrip("ORD-1", 5250, "2025-08-29"));  // endDate earlier than tillDate → charge till 2025-08-30
//        days = 3
//        tripKms = 5250 - 5000 = 250
//        freeAllowance = 3 × 100 = 300
//        extraKms = max(0, 250 - 300) = 0
//        totalCost = (3 × 1200) + (0 × 10) = 3600
//→ returns 3600

        carRentalService.addCar("DL09CD4321", 1500, 120, 8);

        System.out.println(carRentalService.bookCar("ORD-2", "DL09CD4321", "2025-09-01", "2025-09-02"));  // 2 days (1,2)
//→ true

        carRentalService.startTrip("ORD-2", 20000);

        System.out.println(carRentalService.endTrip("ORD-2", 20550, "2025-09-04"));  // extended; effectiveEndDate = 2025-09-04
//        days = 1 + (2025-09-04 - 2025-09-01) = 4
//        tripKms = 20550 - 20000 = 550
//        freeAllowance = 4 × 120 = 480
//        extraKms = max(0, 550 - 480) = 70
//        totalCost = (4 × 1500) + (70 × 8) = 6000 + 560 = 6560
//→ returns 6560

        carRentalService.addCar("MH12EF9999", 1000, 80, 12);

        System.out.println(carRentalService.bookCar("ORD-3", "MH12EF9999", "2025-08-10", "2025-08-12"));  // 10,11,12
//→ true

        System.out.println(carRentalService.bookCar("ORD-4", "MH12EF9999", "2025-08-12", "2025-08-15")); // overlaps on 12 (inclusive)
//→ false
    }
}

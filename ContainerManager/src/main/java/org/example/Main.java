package org.example;

import java.util.List;

public class Main {
    static void main() {
        ContainerManager manager = new ContainerManager(List.of( "mA,8,16000",
                "mB,8,8000",
                "mC,4,32000"));

        System.out.println(manager.assignMachine(0, "c1", "img://a", 2, 2000));
        System.out.println(manager.assignMachine(0, "c2", "img://b", 6, 1000));
        System.out.println(manager.assignMachine(0, "c3", "img://c", 3, 1000));
        System.out.println(manager.assignMachine(1, "c4", "img://d", 2, 9000));
        System.out.println(manager.assignMachine(1, "c5", "img://e", 2, 25000));

        System.out.println(manager.stop("c4"));
        System.out.println(manager.assignMachine(1, "c5", "img://e", 2, 25000));
        System.out.println(manager.stop("c4"));
    }
}

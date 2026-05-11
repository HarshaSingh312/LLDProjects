package org.example;

import java.util.*;

// -------------------- MODELS --------------------

class Container {
    private final String id;
    private final String image;
    private final int cpu;
    private final int memory;
    private final String machineId;

    public Container(String id, String image, int cpu, int memory, String machineId) {
        this.id = id;
        this.image = image;
        this.cpu = cpu;
        this.memory = memory;
        this.machineId = machineId;
    }

    public String getId() { return id; }
    public int getCpu() { return cpu; }
    public int getMemory() { return memory; }
    public String getMachineId() { return machineId; }
}

class Machine {
    private final String id;
    private final int totalCpu;
    private final int totalMemory;

    private int freeCpu;
    private int freeMemory;

    public Machine(String id, int cpu, int memory) {
        this.id = id;
        this.totalCpu = cpu;
        this.totalMemory = memory;
        this.freeCpu = cpu;
        this.freeMemory = memory;
    }

    public String getId() { return id; }
    public int getFreeCpu() { return freeCpu; }
    public int getFreeMemory() { return freeMemory; }

    public boolean canFit(int cpu, int memory) {
        return freeCpu >= cpu && freeMemory >= memory;
    }

    public void allocate(int cpu, int memory) {
        if (!canFit(cpu, memory)) {
            throw new RuntimeException("Insufficient resources");
        }
        freeCpu -= cpu;
        freeMemory -= memory;
    }

    public void release(int cpu, int memory) {
        freeCpu += cpu;
        freeMemory += memory;
    }
}

// -------------------- STRATEGY --------------------

interface AllocationStrategy {
    Machine pick(TreeSet<Machine> machines, int cpu, int memory);
}

class MaxCpuStrategy implements AllocationStrategy {
    @Override
    public Machine pick(TreeSet<Machine> machines, int cpu, int memory) {
        for (Machine m : machines) {
            if (m.canFit(cpu, memory)) return m;
        }
        return null;
    }
}

class MaxMemoryStrategy implements AllocationStrategy {
    @Override
    public Machine pick(TreeSet<Machine> machines, int cpu, int memory) {
        for (Machine m : machines) {
            if (m.canFit(cpu, memory)) return m;
        }
        return null;
    }
}

// -------------------- MACHINE MANAGER --------------------

class MachineManager {

    private final Map<String, Machine> machineMap = new HashMap<>();

    // Sorted by CPU desc, tie -> id
    private final TreeSet<Machine> cpuOrdered = new TreeSet<>(
            Comparator.comparing(Machine::getFreeCpu).reversed()
                    .thenComparing(Machine::getId)
    );

    // Sorted by Memory desc
    private final TreeSet<Machine> memoryOrdered = new TreeSet<>(
            Comparator.comparing(Machine::getFreeMemory).reversed()
                    .thenComparing(Machine::getId)
    );

    public void addMachine(String id, int cpu, int memory) {
        Machine m = new Machine(id, cpu, memory);
        machineMap.put(id, m);
        cpuOrdered.add(m);
        memoryOrdered.add(m);
    }

    public Machine allocate(AllocationStrategy strategy, int cpu, int memory) {
        TreeSet<Machine> pool = (strategy instanceof MaxCpuStrategy) ? cpuOrdered : memoryOrdered;

        Machine selected = strategy.pick(pool, cpu, memory);
        if (selected == null) return null;

        // remove before update (important for TreeSet consistency)
        cpuOrdered.remove(selected);
        memoryOrdered.remove(selected);

        selected.allocate(cpu, memory);

        // reinsert updated
        cpuOrdered.add(selected);
        memoryOrdered.add(selected);

        return selected;
    }

    public void release(String machineId, int cpu, int memory) {
        Machine m = machineMap.get(machineId);

        cpuOrdered.remove(m);
        memoryOrdered.remove(m);

        m.release(cpu, memory);

        cpuOrdered.add(m);
        memoryOrdered.add(m);
    }
}

// -------------------- CONTAINER MANAGER --------------------

public class ContainerManager2 {

    private final MachineManager machineManager = new MachineManager();
    private final Map<String, Container> containers = new HashMap<>();

    public ContainerManager2(List<String> machines) {
        for (String m : machines) {
            String[] parts = m.split(",");
            machineManager.addMachine(
                    parts[0],
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])
            );
        }
    }

    public String startContainer(
            String containerId,
            String image,
            int cpu,
            int memory,
            AllocationStrategy strategy
    ) {
        Machine m = machineManager.allocate(strategy, cpu, memory);
        if (m == null) return null;

        Container c = new Container(containerId, image, cpu, memory, m.getId());
        containers.put(containerId, c);

        return m.getId();
    }

    public boolean stopContainer(String containerId) {
        Container c = containers.get(containerId);
        if (c == null) return false;

        machineManager.release(c.getMachineId(), c.getCpu(), c.getMemory());
        containers.remove(containerId);

        return true;
    }
}
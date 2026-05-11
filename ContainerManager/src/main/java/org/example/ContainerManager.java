package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class Container {
    private String containerName;
    private String imageUrl;
    private int cpuUnits;
    private int memMb;
    private String machineId;

    public Container(String containerName, String imageUrl, int cpuUnits, int memMb, String machineId) {
        this.containerName = containerName;
        this.imageUrl = imageUrl;
        this.cpuUnits = cpuUnits;
        this.memMb = memMb;
        this.machineId = machineId;
    }

    public String getContainerName() { return containerName; }
    public void setContainerName(String containerName) { this.containerName = containerName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getCpuUnits() { return cpuUnits; }
    public void setCpuUnits(int cpuUnits) { this.cpuUnits = cpuUnits; }
    public int getMemMb() { return memMb; }
    public void setMemMb(int memMb) { this.memMb = memMb; }
    public String getMachineId() { return machineId; }
}

class Machine {
    private String id;
    private int cpu;
    private int memeory;
    private int freeMemery;
    private int freeCPUUnit;

    public Machine(String id, int cpu, int memeory) {
        this.id = id;
        this.cpu = cpu;
        this.memeory = memeory;
        this.freeMemery = memeory;
        this.freeCPUUnit = cpu;
    }

    public String getId() { return id; }
    public int getFreeMemery() { return freeMemery; }
    public void addFreeMemery(int freeMemery) { this.freeMemery += freeMemery; }
    public int getFreeCPUUnit() { return freeCPUUnit; }
    public void addFreeCPUUnit(int freeCPUunit) { this.freeCPUUnit += freeCPUunit; }
}

interface MachineSelectionStrategy {
    Machine select(int cpu, int memory);
}

class CPUBasedSelectionStrategy implements MachineSelectionStrategy {
    MachineManager machineManager;

    public CPUBasedSelectionStrategy(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    @Override
    public Machine select(int cpu, int memory) {
        return machineManager.getMachineByMostAvailableCPU(cpu, memory);
    }
}

class MemeoryBasedSelectionStrategy implements MachineSelectionStrategy {
    MachineManager machineManager;

    public MemeoryBasedSelectionStrategy(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    @Override
    public Machine select(int cpu, int memory) {
        return machineManager.getMachineByMostAvailableMemory(cpu, memory);
    }
}

class CriteriaBasedSelectionFactory {
    public static MachineSelectionStrategy getInstance(int criteria, MachineManager machineManager) {
        switch (criteria) {
            case 0: return new CPUBasedSelectionStrategy(machineManager);
            case 1: return new MemeoryBasedSelectionStrategy(machineManager);
            default: return null;
        }
    }
}

class MachineManager {
    HashMap<String, Machine> machines = new HashMap<>();
    List<Machine> machinesList = new ArrayList<>();

    public void loadData(List<String> machines) {
        for (String machine : machines) {
            String[] parts = machine.split(",");
            Machine machine1 = new Machine(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            this.machines.put(parts[0], machine1);
            this.machinesList.add(machine1);
        }
    }

    public Machine getMachine(String machineId) {
        return machines.get(machineId);
    }

    public Machine getMachineByMostAvailableCPU(int cpu, int memory) {
        List<Machine> filteredMachines = getMachines(cpu, memory);
        filteredMachines.sort(Comparator.comparing(Machine::getFreeCPUUnit).reversed().thenComparing(Machine::getId));
        if (filteredMachines.isEmpty()) return null;
        return filteredMachines.get(0);
    }

    public Machine getMachineByMostAvailableMemory(int cpu, int memory) {
        List<Machine> filteredMachines = getMachines(cpu, memory);
        filteredMachines.sort(Comparator.comparing(Machine::getFreeMemery).reversed().thenComparing(Machine::getId));
        if (filteredMachines.isEmpty()) return null;
        return filteredMachines.get(0);
    }

    public void occupyMachine(String machineId, int cpu, int memory) {
        Machine m = getMachine(machineId);
        m.addFreeCPUUnit(-cpu);
        m.addFreeMemery(-memory);
    }

    public void removeOccupencyMachine(String machineId, int cpu, int memory) {
        Machine m = getMachine(machineId);
        m.addFreeCPUUnit(cpu);
        m.addFreeMemery(memory);
    }

    public List<Machine> getMachines(int cpu, int memory) {
        return machinesList.stream().filter(machine -> machine.getFreeCPUUnit() >= cpu)
                .filter(machine -> machine.getFreeMemery() >= memory).collect(Collectors.toList());
    }
}

public class ContainerManager {
    HashMap<String, Container> containerDb = new HashMap<>();
    MachineManager machineManager = new MachineManager();

    public ContainerManager(List<String> machines) {
        machineManager.loadData(machines);
    }

    public String assignMachine(int criteria, String containerName, String imageUrl, int cpuUnits, int memMb) {
        MachineSelectionStrategy machineSelectionStrategy = CriteriaBasedSelectionFactory.getInstance(criteria, machineManager);
        if (Objects.isNull(machineSelectionStrategy)) return "";
        Machine m = machineSelectionStrategy.select(cpuUnits, memMb);
        if (Objects.isNull(m)) return "";
        machineManager.occupyMachine(m.getId(), cpuUnits, memMb);
        containerDb.put(containerName, new Container(containerName, imageUrl, cpuUnits, memMb, m.getId()));
        return m.getId();
    }

    public boolean stop(String name) {
        Container container = containerDb.get(name);
        if (Objects.isNull(container)) return false;
        machineManager.removeOccupencyMachine(container.getMachineId(), container.getCpuUnits(), container.getMemMb());
        containerDb.remove(name);
        return true;
    }
}

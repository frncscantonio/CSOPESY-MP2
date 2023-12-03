// HI, THIS IDK IF THIS WORKS

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

class draft {
    private static final int MAX_INSTANCES = 5;     // Instances refer to...

    private static final Queue<Party> dungeonQueue = new LinkedList<>();

    static class Party {
        private final int tank;
        private final int healer;
        private final int dps;
        private final int clearTime;

        public Party(int tank, int healer, int dps, int clearTime) {
            this.tank = tank;
            this.healer = healer;
            this.dps = dps;
            this.clearTime = clearTime;
        }

        // Getters for party details
        public int getTank() {
            return tank;
        }

        public int getHealer() {
            return healer;
        }

        public int getDps() {
            return dps;
        }

        public int getClearTime() {
            return clearTime;
        }
    }

    static class DungeonInstance {
        private Party currentParty;

        public boolean isAvailable() {
            return currentParty == null;
        }

        public void assignParty(Party party) {
            currentParty = party;
        }

        public void clearInstance() {
            // Perform actions to clear the dungeon instance
            currentParty = null;
        }

        public Party getCurrentParty() {
            return currentParty;
        }

        // Other methods for managing the dungeon instance
    }

    static class DungeonManager {
        private static final DungeonInstance[] instances = new DungeonInstance[MAX_INSTANCES];

        static {
            for (int i = 0; i < MAX_INSTANCES; i++) {
                instances[i] = new DungeonInstance();
            }
        }

        public static synchronized void enqueueParty(Party party) {
            dungeonQueue.add(party);
            assignPartiesToInstances();
        }

        private static void assignPartiesToInstances() {
            for (DungeonInstance instance : instances) {
                if (instance.isAvailable() && !dungeonQueue.isEmpty()) {
                    Party party = dungeonQueue.poll();
                    instance.assignParty(party);

                    // Simulate dungeon clearing
                    new Thread(() -> {
                        try {
                            Thread.sleep(party.clearTime * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        instance.clearInstance();
                    }).start();
                }
            }
        }

        // Other methods for managing the dungeon queue
    }

    private static void displayInstanceStatus() {
        System.out.println("Current status of instances:");

        for (int i = 0; i < DungeonManager.instances.length; i++) {
            draft.DungeonInstance instance = DungeonManager.instances[i];
            String status = instance.isAvailable() ? "empty" : "active";
            System.out.println("Instance " + (i + 1) + ": " + status);
        }
    }

    private static void displaySummary() {
        System.out.println("Summary of parties served and total time served:");

        for (int i = 0; i < DungeonManager.instances.length; i++) {
            draft.DungeonInstance instance = DungeonManager.instances[i];
            if (instance.isAvailable()) {
                System.out.println("Instance " + (i + 1) + ": No parties served");
            } else {
                draft.Party party = instance.getCurrentParty();
                System.out.println("Instance " + (i + 1) + ": Served a party with " +
                        party.getTank() + " tanks, " +
                        party.getHealer() + " healers, " +
                        party.getDps() + " DPS. Total time served: " +
                        party.getClearTime() + " seconds");
            }
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Enter the maximum number of concurrent instances (n): ");
        int maxInstances = scanner.nextInt();

        // Example usage: Taking input for parties and enqueueing them
        System.out.print("Enter the number of parties to enqueue: ");
        int numParties = scanner.nextInt();

        for (int i = 0; i < numParties; i++) {
            System.out.println("Enter details for Party " + (i + 1) + ":");
            System.out.print("Number of tanks: ");
            int tanks = scanner.nextInt();
            System.out.print("Number of healers: ");
            int healers = scanner.nextInt();
            System.out.print("Number of DPS: ");
            int dps = scanner.nextInt();
            int clearTime = random.nextInt(15) + 1;

            draft.Party party = new draft.Party(tanks, healers, dps, clearTime);
            DungeonManager.enqueueParty(party);
        }

        // Output: Display current status of instances and summary
        displayInstanceStatus();
        displaySummary();
    }


}

// HI, THIS IDK IF THIS WORKS

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

class MP2 {
    private static final int MAX_INSTANCES = 5;

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

            MP2.Party party = new MP2.Party(tanks, healers, dps, clearTime);
            DungeonManager.enqueueParty(party);
        }
    }
}

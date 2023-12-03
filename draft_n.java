import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class draft_n {
    private static int maxInstances = 0;   // maximum number of instances that can be concurrently active
    private static int nTanks = 0;
    private static int nHealers= 0;
    private static int nDPS = 0;
    private static int t1 = 0;
    private static int t2 = 0;
    private static int nUpdate = 0;

    static class Dungeon {
        private Boolean isActive;
        private int nPartyServed;
        private int totalTime;

        public Dungeon() {
            this.isActive = false;
            this.nPartyServed = 0;
            this.totalTime = 0;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public int getNPartyServed() {
            return nPartyServed;
        }

        public int getTotalTime() {
            return totalTime;
        }


        public void emptyDungeon(Party party) {
            this.isActive = false;
            this.nPartyServed = this.nPartyServed + 1;
            this.totalTime = this.totalTime + party.getClearTime();
        }
    }

    static class Party {
        private int clearTime = 0;

        public Party() {
            Random random = new Random();
            this.clearTime = random.nextInt((t2 - t1) + 1) + t1;
        }

        public int getClearTime(){
            return clearTime;
        }
    }

    static class DungeonManager {
        private static final Dungeon[] dungeonThreads = new Dungeon[maxInstances];

        static {
            for (int i = 0; i < maxInstances; i++) {
                dungeonThreads[i] = new Dungeon();
            }
        }

        public static synchronized void enqueueParty() {
            for (Dungeon dungeon : dungeonThreads) {
                if (!dungeon.getActive() && nTanks >= 1 && nHealers >= 1 && nDPS >= 3) {
                    nTanks--;
                    nHealers--;
                    nDPS -= 3;

                    Party party = new Party();
                    assignPartyToDungeon(dungeon, party);
                }
            }

        }


        private static void assignPartyToDungeon(Dungeon dungeon, Party party) {
            dungeon.setActive(true);
            printStatuses(); // Print status when a party is assigned

            new Thread(() -> {
                try {
                    Thread.sleep(party.getClearTime() * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dungeon.emptyDungeon(party);
                printStatuses(); // Print status when dungeon becomes empty
                enqueueParty(); // Try to enqueue another party if there are enough players
            }).start();
        }


        public static synchronized void printStatuses() {
            long currentTimeInSeconds = System.currentTimeMillis() / 1000;
            nUpdate++;
            System.out.println("Update " + nUpdate + " at " + currentTimeInSeconds + " seconds:");
            for (int i = 0; i < dungeonThreads.length; i++) {
                System.out.println("Dungeon " + i + ": " + (dungeonThreads[i].getActive() ? "Active" : "Empty"));
            }
            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);
        }
        public static synchronized void checkForAvailableDungeon() {
            if (nTanks >= 1 && nHealers >= 1 && nDPS >= 3) {
                for (Dungeon dungeon : dungeonThreads) {
                    if (!dungeon.getActive()) {
                        enqueueParty();
                        break;
                    }
                }
            }
        }

        private static void printSummary() {
            System.out.println("SUMMARY:");

            for (Dungeon dungeon : dungeonThreads) {
                System.out.println("Dungeon " + dungeon + ":");
                System.out.println("   Parties Served: " + dungeon.getNPartyServed());
                System.out.println("   Total Time Served: " + dungeon.getTotalTime() + " seconds");
            }
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the maximum number of concurrent instances (n): ");
        maxInstances = scanner.nextInt();

        System.out.print("Number of tanks: ");
        nTanks = scanner.nextInt();

        System.out.print("Number of healers: ");
        nHealers = scanner.nextInt();

        System.out.print("Number of DPS: ");
        nDPS = scanner.nextInt();

        System.out.print("T1: ");
        t1 = scanner.nextInt();

        System.out.print("T1: ");
        t2 = scanner.nextInt();

        DungeonManager.printStatuses();
        DungeonManager.enqueueParty();
    }

}

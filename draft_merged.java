import java.util.Random;
import java.util.Scanner;

public class draft_merged {
    private static int maxInstances = 0;
    private static int nTanks = 0;
    private static int nHealers= 0;
    private static int nDPS = 0;
    private static int t1 = 0;
    private static int t2 = 0;
    private static int nUpdate = 0;
    private static boolean updatesRemoved = false;

    static class Dungeon {
        private Boolean isActive;
        private int nPartyServed;
        private int totalTime;
        private int currentPartyClearTime; // Added for tracking clear time

        public Dungeon() {
            this.isActive = false;
            this.nPartyServed = 0;
            this.totalTime = 0;
            this.currentPartyClearTime = 0;
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

        public int getCurrentPartyClearTime() {
            return currentPartyClearTime;
        }

        public void emptyDungeon(Party party) {
            this.isActive = false;
            this.nPartyServed = this.nPartyServed + 1;
            this.totalTime = this.totalTime + party.getClearTime();
        }

        public void setCurrentPartyClearTime(int clearTime) {
            this.currentPartyClearTime = clearTime;
        }

        public boolean isActive() {
            return isActive;
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
            dungeon.setCurrentPartyClearTime(party.getClearTime()); // Set clear time for active party
            printStatuses();

            new Thread(() -> {
                try {
                    Thread.sleep(party.getClearTime() * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dungeon.emptyDungeon(party);
                printStatuses();
                enqueueParty();
            }).start();
        }

        public static synchronized void printStatuses() {
            nUpdate++;
            System.out.println();
            System.out.println("Update " + nUpdate + ":");
            for (int i = 0; i < dungeonThreads.length; i++) {
                Dungeon dungeon = dungeonThreads[i];
                String status = dungeon.isActive() ? "Active" : "Empty";
                int clearTime = dungeon.isActive() ? dungeon.getCurrentPartyClearTime() : 0;
                System.out.println("Dungeon " + i + ": " + status + " (Clear Time: " + clearTime + "s)");
            }
            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);

            if (allDungeonsEmpty() && nUpdate != 1) {
                printSummary();
            }
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

        private static boolean allDungeonsEmpty() {
            for (Dungeon dungeon : dungeonThreads) {
                if (dungeon.isActive()) {
                    return false;
                }
            }
            return true;
        }

        private static void printSummary() {
            System.out.println();
            System.out.println("---------- SUMMARY ----------");

            for (int i = 0; i < dungeonThreads.length; i++) {
                Dungeon dungeon = dungeonThreads[i];
                System.out.println("Dungeon " + i + ":");
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

        System.out.print("T2: ");
        t2 = scanner.nextInt();

        DungeonManager.printStatuses();
        DungeonManager.enqueueParty();
    }
}

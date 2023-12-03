import java.util.Random;
import java.util.Scanner;

public class MP2 {
    private static int maxInstances = 0;
    private static int nTanks = 0;
    private static int nHealers= 0;
    private static int nDPS = 0;
    private static int t1 = 0;
    private static int t2 = 0;
    private static int nUpdate = 0;
    private static final String greenColor = "\u001B[32;1m";
    private static final String resetColor = "\u001B[0m";


    /**
     * Represents a Dungeon in the program.
     */
    static class Dungeon {
        private Boolean isActive;
        private int nPartyServed;
        private int totalTime;
        private int currentPartyClearTime;

        /**
         * Constructor to initialize Dungeon instance.
         */
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

        /**
         * Marks the dungeon as empty and updates statistics when a party finishes clearing.
         *
         * @param party The party that cleared the dungeon.
         */
        public void emptyDungeon(Party party) {
            this.isActive = false;
            this.nPartyServed = this.nPartyServed + 1;
            this.totalTime = this.totalTime + party.getClearTime();
        }

        /**
         * Sets the clear time for the active party in the dungeon.
         *
         * @param clearTime The clear time of the active party.
         */
        public void setCurrentPartyClearTime(int clearTime) {
            this.currentPartyClearTime = clearTime;
        }

        public boolean isActive() {
            return isActive;
        }
    }


    /**
     * Represents a Party in the program.
     */
    static class Party {
        private final int clearTime;

        /**
         * Constructor to initialize Party instance with a random clear time.
         */
        public Party() {
            Random random = new Random();
            this.clearTime = random.nextInt((t2 - t1) + 1) + t1;
        }

        public int getClearTime(){
            return clearTime;
        }
    }


    /**
     * Manages the dungeons and parties in the program.
     */
    static class DungeonManager {
        private static final Dungeon[] dungeonThreads = new Dungeon[maxInstances];

        static {
            for (int i = 0; i < maxInstances; i++) {
                dungeonThreads[i] = new Dungeon();
            }
        }

        /**
         * Enqueues a new party to an available dungeon.
         */
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

        /**
         * Assigns a party to a dungeon and starts a thread for the party to clear the dungeon.
         *
         * @param dungeon The dungeon to assign the party to.
         * @param party   The party to be assigned.
         */
        private static void assignPartyToDungeon(Dungeon dungeon, Party party) {
            dungeon.setActive(true);
            dungeon.setCurrentPartyClearTime(party.getClearTime());
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

        /**
         * Prints the current status of dungeons and player counts.
         */
        public static synchronized void printStatuses() {
            nUpdate++;
            System.out.println();
            String blueColor = "\u001B[34;1m";
            System.out.println(blueColor + "--- UPDATE " + nUpdate + " ---" + resetColor);
            for (int i = 0; i < dungeonThreads.length; i++) {
                Dungeon dungeon = dungeonThreads[i];
                String redColor = "\u001B[31;1m";
                String status = dungeon.isActive() ? greenColor + "Active" + resetColor : redColor + "Empty" + resetColor;
                int clearTime = dungeon.isActive() ? dungeon.getCurrentPartyClearTime() : 0;
                System.out.println("Dungeon " + i + ": " + status + " (Clear Time: " + clearTime + "s)");
            }
            System.out.println(blueColor +  "Player Counts:" + resetColor);
            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);

            if (allDungeonsEmpty() && nUpdate != 1) {
                printSummary();
            }
        }

        /**
         * Checks if all dungeons are empty.
         *
         * @return True if all dungeons are empty, otherwise false.
         */
        private static boolean allDungeonsEmpty() {
            for (Dungeon dungeon : dungeonThreads) {
                if (dungeon.isActive()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Prints the summary of each dungeon's performance.
         */
        private static void printSummary() {
            System.out.println();
            String cyanColor = "\u001B[36;1m";
            System.out.println(cyanColor + "---------- SUMMARY ----------" + resetColor);

            for (int i = 0; i < dungeonThreads.length; i++) {
                Dungeon dungeon = dungeonThreads[i];
                System.out.println();
                System.out.println(greenColor + "Dungeon " + i + ":" + resetColor);
                System.out.println("   Parties Served: " + dungeon.getNPartyServed());
                System.out.println("   Total Time Served: " + dungeon.getTotalTime() + " seconds");
            }
        }
    }

    /**
     * The main method to start the program and input initial parameters.
     */
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

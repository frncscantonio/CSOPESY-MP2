import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class draft3 {
    private static int maxInstances = 0;
    private static int nTanks = 0;
    private static int nHealers = 0;
    private static int nDPS = 0;
    private static int t1 = 0;
    private static int t2 = 0;
    private static int nUpdate = 0;
//    private static int currentTime = 0;

    static class Dungeon {
        private boolean isActive;
        private int nPartyServed;
        private int totalTime;
        private int currentPartyClearTime;

        public Dungeon() {
            this.isActive = false;
            this.nPartyServed = 0;
            this.totalTime = 0;
            this.currentPartyClearTime = 0;
        }

        public boolean isActive() {
            return isActive;
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

        public void startParty(Party party) {
            this.isActive = true;
            this.currentPartyClearTime = party.getClearTime();
            new Thread(() -> {
                try {
                    Thread.sleep(currentPartyClearTime * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.finishParty(party);
            }).start();
        }

        public void finishParty(Party party) {
            this.isActive = false;
            this.nPartyServed++;
            this.totalTime += party.getClearTime();
            DungeonManager.enqueueParty();
        }
    }

    static class Party {
        private int clearTime;

        public Party() {
            Random random = new Random();
            this.clearTime = random.nextInt((t2 - t1) + 1) + t1;
        }

        public int getClearTime() {
            return clearTime;
        }
    }

    static class DungeonManager {
        private static final Queue<Party> partyQueue = new LinkedList<>();
        private static final Dungeon[] dungeons = new Dungeon[maxInstances];

        static {
            for (int i = 0; i < maxInstances; i++) {
                dungeons[i] = new Dungeon();
            }
        }

        public static synchronized void enqueueParty() {
            nUpdate++;
//            currentTime++;

            boolean dungeonsChanged = false;

//            System.out.println("Update " + nUpdate + " (" + currentTime + "s):");
            System.out.println("Update " + nUpdate + ":");

            for (int i = 0; i < dungeons.length; i++) {
                Dungeon dungeon = dungeons[i];
                String status = dungeon.isActive() ? "active" : "empty";
                int clearTime = dungeon.isActive() ? dungeon.getCurrentPartyClearTime() : 0;
                System.out.println("Dungeon " + i + ": " + status + " (Clear Time: " + clearTime + "s)");
                if (dungeon.isActive()) {
                    dungeonsChanged = true;
                }
            }

            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);

            if (canFormParty()) {
                for (Dungeon dungeon : dungeons) {
                    if (canFormParty()) {
                        nTanks--;
                        nHealers--;
                        nDPS -= 3;
                        Party party = new Party();
                        dungeon.startParty(party);
                        dungeonsChanged = true;
                    } else {
                        break;
                    }
                }
            }

            if (allDungeonsEmpty()) {
                printSummary();
            }
        }

        private static boolean canFormParty() {
            return nTanks >= 1 && nHealers >= 1 && nDPS >= 3;
        }

        private static boolean allDungeonsEmpty() {
            for (Dungeon dungeon : dungeons) {
                if (dungeon.isActive()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void printSummary() {
        System.out.println("SUMMARY:");

        for (int i = 0; i < DungeonManager.dungeons.length; i++) {
            Dungeon dungeon = DungeonManager.dungeons[i];
            System.out.println("Dungeon " + i + ":");
            System.out.println("   Parties Served: " + dungeon.getNPartyServed());
            System.out.println("   Total Time Served: " + dungeon.getTotalTime() + " seconds");
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

        DungeonManager.enqueueParty();
    }
}

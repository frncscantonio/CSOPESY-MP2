import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class MP2 {
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

        public static synchronized void enqueueParty(){
            // PRINT CURRENT STATUS OF ALL
            nUpdate = nUpdate + 1;
            System.out.println("from enqueue: Update " + nUpdate + ":");
            for (int i = 0; i < DungeonManager.dungeonThreads.length; i++) {
                if (!DungeonManager.dungeonThreads[i].getActive()) {
                    System.out.println("Dungeon " + i + ": " +  DungeonManager.dungeonThreads[i].getActive());
                } else {
                    System.out.println("Dungeon " + i + ": " +  DungeonManager.dungeonThreads[i].getActive());
                }
            }
            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);

            if(nTanks >= 1 && nHealers >= 1 && nDPS >= 3) {
                nTanks = nTanks - 1;
                nHealers = nHealers - 1;
                nDPS = nDPS - 3;

                Party party = new Party();
                assignPartyToDungeon(party);
            } else {
                System.out.println("SUMMARY HERE");
            }
        }

        private static void assignPartyToDungeon(Party party) {

            // ASSIGN NEW PARTY TO AVAILABLE DUNGEON + PRINT CURRENT STATUS OF ALL
            nUpdate = nUpdate + 1;
            System.out.println("from assignParty: Update " + nUpdate + ":");
            for (int i = 0; i < dungeonThreads.length; i++) {
                if (!dungeonThreads[i].getActive()) {

                    System.out.println("Dungeon " + i + ": " +  dungeonThreads[i].getActive());

                    dungeonThreads[i].setActive(true);
                    int currentThreadFinal = i;
                    new Thread(() -> {
                        try {
                            Thread.sleep(party.clearTime * 1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dungeonThreads[currentThreadFinal].emptyDungeon(party);
                    }).start();
                } else {
                    System.out.println("Dungeon " + i + ": " +  dungeonThreads[i].getActive());
                }
            }
            System.out.println("Tanks: " + nTanks);
            System.out.println("Healers: " + nHealers);
            System.out.println("DPS: " + nDPS);
            enqueueParty();


//            for (Dungeon dungeonThread : dungeonThreads) {
//                if (!dungeonThread.getActive()) {
//                    dungeonThread.setActive(true);
//
//                    System.out.println(dungeonThread + "Active");
//
//                    new Thread(() -> {
//                        try {
//                            Thread.sleep(party.clearTime * 1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        dungeonThread.emptyDungeon(party);
//                    }).start();
//                }
//            }
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

        DungeonManager.enqueueParty();
    }

}

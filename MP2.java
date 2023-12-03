import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class MP2 {
    private int maxInstances = 0;   // maximum number of instances that can be concurrently active
    private int nTanks = 0;
    private int nHealers= 0;
    private int nDPS = 0;
    private static int t1 = 0;
    private static int t2 = 0;

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

        public void setNPartyServed(int nPartyServed) {
            this.nPartyServed = nPartyServed;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
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

    public void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the maximum number of concurrent instances (n): ");
        this.maxInstances = scanner.nextInt();

        System.out.print("Number of tanks: ");
        this.nTanks = scanner.nextInt();

        System.out.print("Number of healers: ");
        this.nHealers = scanner.nextInt();

        System.out.print("Number of DPS: ");
        this.nDPS = scanner.nextInt();

        System.out.print("T1: ");
        this.t1 = scanner.nextInt();

        System.out.print("T1: ");
        this.t2 = scanner.nextInt();

    }

}

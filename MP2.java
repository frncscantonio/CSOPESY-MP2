import java.util.LinkedList;
import java.util.Queue;

public class MP2 {
    private int maxParties = 0;   // maximum number of instances that can be concurrently active
    private static final Queue<Party> Queue = new LinkedList<>();  //

    static class Party {
        private final int tank;
        private final int healer;
        private final int dps;

        public Party(int tank, int healer, int dps) {
            this.tank = tank;
            this.healer = healer;
            this.dps = dps;
        }

    }

}

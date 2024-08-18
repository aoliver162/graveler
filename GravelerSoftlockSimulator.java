import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

class GravelerSoftlockSimulator {
    private static final long MAX_ATTEMPTS = 1000000000;
    private static final long TARGET_NUM = 177;
    private static final int MAX_TURNS = 231;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors()-1;

    public static void main(String[] args) throws Exception{
        System.out.println("Starting simulation of " + MAX_ATTEMPTS + " softlock escape attempts on "+THREAD_COUNT+" threads...");
        long start = System.currentTimeMillis();

        List<Long> sizes = Collections.nCopies(THREAD_COUNT, (MAX_ATTEMPTS / THREAD_COUNT + MAX_ATTEMPTS % THREAD_COUNT));
        long maxParalyzes = sizes.parallelStream().map(GravelerSoftlockSimulator::makeAttempts).max(Long::compare).get();

        long end = System.currentTimeMillis();
        System.out.println("Highest rolled number of paralysis procs: " + maxParalyzes + "\nTime elapsed: " + (end-start)/1000 + "s");
    }

    private static long makeAttempts(long numAttempts) {
        RandomGenerator rand = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create();
        long maxParalyzes = 0;
        int rolls = 0;
        while (maxParalyzes < TARGET_NUM && rolls++ < numAttempts) {
            maxParalyzes = Math.max(rand.ints(MAX_TURNS, 0, 4).filter(i -> i == 0).count(), maxParalyzes);
        }
        return maxParalyzes;
    }
}

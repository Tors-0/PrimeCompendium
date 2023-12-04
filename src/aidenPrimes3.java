import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
Prime Finder
Aiden Rouhani
Last Edit: 12/1/23
*/


public class aidenPrimes3 {
    private static int highestPerformanceMultiplier = 1;
    private static File settings = new File("./settings.txt");
    private static void initialization() throws IOException {
        if (settings.exists() && settings.isFile()){
            System.out.println("Identified existing system data...\n");

            FileReader read = new FileReader(settings);
            highestPerformanceMultiplier = read.read();
            read.close();
        } else {
            System.out.println("No data detected... optimizing for your system...\n");
            ArrayList<Long> trials = new ArrayList<>();

            for (int j = 1; j <= 4; j++) {
                for (int i = 1; i <= 4; i++) {
                    ArrayList<Long> interTrials = new ArrayList<>();
                    long startTime = System.nanoTime();
                    highestPerformanceMultiplier = j;
                    primeFinderOptimized(5_000_000);
                    interTrials.add(System.nanoTime() - startTime);

                    if (i == 4) {
                        trials.add((long) interTrials.stream()
                                .mapToDouble(d -> d)
                                .average()
                                .orElse(0.0));
                    }
                }
            }
            highestPerformanceMultiplier = trials.indexOf(Collections.min(trials))+1;
            System.out.println("Identified " + (highestPerformanceMultiplier*32768) + " bytes as the optimal L1 cache size for your system.");

            FileWriter write = new FileWriter(settings);
            write.write(highestPerformanceMultiplier);
            write.close();
        }
    }
    /*
    Main method.
    Creates scanner object, asks for user input, and then activates the
    primeFinder method.
    */
    public static void main(String[] args) throws IOException {
        initialization();
        Scanner input = new Scanner(System.in);

        System.out.println("PREMIER PRIMER by Aiden Rouhani");
        System.out.println("-------------------------------");

        System.out.println("How many primes would you like to find?");
        int primes = Integer.parseInt(input.nextLine().replace(",", ""));

        int algorithmChoice = 1;
        System.out.println("\nWould you like to use the SEGMENTED SIEVE (1) algorithm, the SINGULAR SIEVE algorithm (2) or the SIEVE-MULTITHREADED algorithm (3)?");
        System.out.println("   - [1] SEGMENTED SIEVE utilizes an optimized segmented Sieve of Eratosthenes algorithm.");
        System.out.println("   - [2] SINGULAR SIEVE utilizes an optimized Sieve of Eratosthenes algorithm.");
        System.out.println("   - [3] SINGULAR-SIEVE-MULTITHREADED utilizes the SIEVE algorithm, but with multithreading, making it faster at larger tasks but slower at smaller ones.");
        System.out.println("\n   (Input 4 if you'd like the system to determine the best algorithm for you.)");
        algorithmChoice = input.nextInt();
        input.nextLine();

        algorithmChoice = (algorithmChoice == 4 && primes < 10000) ? 2 : (algorithmChoice == 4) ? 1 : algorithmChoice;
        String algorithmChoiceString = (algorithmChoice == 1) ? "SEGMENTED SIEVE" : (algorithmChoice == 2) ? "SINGULAR SIEVE" : "SIEVE-MULTITHREADED";
        System.out.println("\nStarting prime finding process with " + algorithmChoiceString + " algorithm... \n");

        long startTime = System.nanoTime();
        long finalPrime = (algorithmChoice == 1) ? primeFinderOptimized(primes) : (algorithmChoice == 2) ? primeFinderSieve(primes) : primeFinderSieveMultithreaded(primes);
        double runningTime = (System.nanoTime() - startTime) / 1000000000.0;

        String formattedTime = (runningTime <= 1) ? ("~" + Math.round(runningTime*1000*100.0)/100.0 + "ms") : (runningTime <= 60) ? ("~" + Math.round(100.0*runningTime)/100.0 + "s") : ("~" + runningTime/60 + "min");
        System.out.println("The " + primes + "th Prime has been found, " + finalPrime + ", in " + formattedTime);

        BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true));
        writer.append(algorithmChoiceString).append(" | ").append(String.valueOf(primes)).append(" | ").append(formattedTime);
        writer.newLine();
        writer.flush();
        writer.close();
    }
    public static int primeFinderOptimized(int num) {

        int l1CacheSize = 32768 * highestPerformanceMultiplier;
        // Credit to Rae for formula translation
        int primes = (int)((Math.log(num) + Math.log(Math.log(num)) - 1 + ( (Math.log(Math.log(num)) - 2) / Math.log(num) )- (( Math.pow(Math.log(Math.log(num)),2) - 6 * Math.log(Math.log(num)) - 11 )/( 2 * Math.pow(Math.log(num),2) ))) * num);

        int sqrt = (int) Math.sqrt(primes);
        int sieveSize = Math.max(sqrt, l1CacheSize);

        int count = (primes < 2) ? 0 : 1;


        ArrayList<Integer> primeList = new ArrayList<>();
        ArrayList<Integer> multipleList = new ArrayList<>();

        boolean[] sieve = new boolean[sieveSize];
        boolean[] isPrime = new boolean[sqrt+1];

        //Arrays.fill(isPrime, 1);
        Arrays.fill(isPrime, true);

        int i = 3;
        int k = 3;
        int p = 3;


        for (int lowLimit = 0; lowLimit <= primes; lowLimit += sieveSize) {
            int highLimit = Math.min(lowLimit + sieveSize - 1, primes);
            Arrays.fill(sieve, true);

            for (; i*i <= highLimit; i+=2) {
                if (isPrime[i]) {
                    for (int j = i*i; j <= sqrt; j+=i) {
                        isPrime[j] = false;
                    }
                }
            }

            for (; k*k <= highLimit; k+=2) {
                if (isPrime[k]) {
                    primeList.add(k);
                    multipleList.add(k*k - lowLimit);
                }
            }

            for (int j = 0; j < primeList.size(); j++) {
                int f = multipleList.get(j);
                for (int n = primeList.get(j) *2; f < sieveSize; f += n) {
                    sieve[f] = false;
                }
                multipleList.set(j, f - sieveSize);
            }

            for (; p <= highLimit; p +=2) {
                if (sieve[p-lowLimit]) {
                    count++;
                }
                if (count == num) return p;
            }
    }

    return count;

}

    /*
    primeFinderSieveMultithreaded method.
    A duplicate of the optimized Sieve method, however utilizing multithreading proportionate to the computer's processing power.
    */
    public static long primeFinderSieveMultithreaded(int primes) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int primeCount = 0;
        int estimatedLimit = (primes <= 1000000) ? (int)(primes*Math.log(primes)*1.2+10) : (primes < 10000000) ? (int)(primes*Math.log(primes)*1.125) : (primes < 75000000) ? (int)(primes*Math.log(primes)*1.115) : (int)(primes*Math.log(primes)*1.11);

        boolean[] primedBools = new boolean[estimatedLimit];
        Arrays.fill(primedBools, true);

        for (int i = 2; i*i <= estimatedLimit; i++) {
            int tempi = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                if(primedBools[tempi]) {
                    for(int modifiedi = (tempi*tempi); modifiedi<estimatedLimit; modifiedi = modifiedi+tempi) {primedBools[modifiedi] = false;}
                }

            }, executorService);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();

        for (int i = 3; primeCount != primes-1; i++) {
            if (primedBools[i]) {
                primeCount++;
            }
            if (primeCount == primes -1) return i;

        }

        return (primes == 1) ? 2 : 3;
    }

    /*
    primeFinderSieve method.
    Uses the Sieve of Eratosthenes to eliminate non-primes (composites) in a
    boolean array.
    Then, transfers to an int array for better/simpler access.
    */
    public static long primeFinderSieve(int primes) {
        int primeCount = 0;
        int estimatedLimit = (primes <= 1000000) ? (int)(primes*Math.log(primes)*1.2+10) : (int)(primes*Math.log(primes)*1.125);
        boolean[] primedBools = new boolean[estimatedLimit];
        Arrays.fill(primedBools, true);
        for (int i = 2; i*i <= estimatedLimit; i++) {
            if(primedBools[i]) {
                for(int modifiedi = (i*i); modifiedi<estimatedLimit; modifiedi =
                        modifiedi+i) {primedBools[modifiedi] = false;}
            }
        }
        for (int i = 3; primeCount != primes-1; i++) {
            if(primedBools[i]) {
                primeCount++;
            }
            if (primeCount == primes-1) return i;
        }
        return (primes == 1) ? 2 : 3;
    }

    /*
    primeFinderStandard method.
    Finds the primes starting from the first 3, due to their anomalous behavior.
    Tries to divide each number by a stored list of primes up to its square root.
    */
    public static int primeFinderStandard(int primes) {
        if (primes == 1) return 2;
        int[] primeList = new int[primes+1];
        primeList[2] = 3;
        int mostRecentPrime = 3;
        int primeListLength = 2;
        for (int i = 2; i < primes; i++) {
            for (int add = 2; true; add+=2) {
                boolean hasBeenBroken = false;
                for (int ind = 2; (ind <= primeListLength && primeList[ind] *
                        primeList[ind] <= mostRecentPrime+add); ind++) {
                    if ((mostRecentPrime + add) % primeList[ind] == 0) {
                        hasBeenBroken = true;
                        break;
                    }
                }
                if (!hasBeenBroken) {
                    primeList[i+1] = mostRecentPrime+add;
                    primeListLength+=1;
                    mostRecentPrime += add;
                    break;

                }
            }
        }
        return mostRecentPrime;
    }
}

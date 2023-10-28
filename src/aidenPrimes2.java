import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Prime Finder
Aiden Rouhani
Last Edit: 10/26/23
*/
// Beginning primes: 2, 3, 5, 7, 11
public class aidenPrimes2 {
    /*
    Main method.
    Creates scanner object, asks for user input, and then activates the
    primeFinder method.
    */
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("PREMIER PRIMER by Aiden Rouhani");
        System.out.println("-------------------------------");

        System.out.println("How many primes would you like to find?");
        int primes = Integer.parseInt(input.nextLine().replace(",", ""));

        int algorithmChoice = 1;
        System.out.println("\nWould you like to use the STANDARD (1) algorithm, the SIEVE algorithm (2) or the SIEVE-MULTITHREADED algorithm (3)?");
        System.out.println("   - [1] STANDARD utilizes an optimized basic trial-division algorithm.");
        System.out.println("   - [2] SIEVE utilizes an optimized, multithreaded Sieve of Eratosthenes algorithm.");
        System.out.println("   - [3] SIEVE-MULTITHREADED utilizes the SIEVE algorithm, but with multithreading, making it faster at larger tasks but slower at smaller ones.");
        System.out.println("\n   (Input 4 if you'd like the system to determine the best algorithm for you.)");
        algorithmChoice = input.nextInt();
        input.nextLine();

        algorithmChoice = (algorithmChoice == 4 && primes <= 1000000) ? 2 : (algorithmChoice == 4) ? 3 : algorithmChoice;
        String algorithmChoiceString = (algorithmChoice == 1) ? "STANDARD" : (algorithmChoice == 2) ? "SIEVE" : "SIEVE-MULTITHREADED";
        System.out.println("\nStarting prime finding process with " + algorithmChoiceString + " algorithm... \n");

        long startTime = System.nanoTime();
        long finalPrime = (algorithmChoice == 1) ? primeFinderStandard(primes) : (algorithmChoice == 2) ? primeFinderSieve(primes) : primeFinderSieveMultithreaded(primes);
        double time = System.nanoTime() - startTime; // added by rae to procure millisecond times, no effect on calculation speed
        double runningTime = time / 1000000000.0;
        time /= 1_000_000;

        String formattedTime = (runningTime <= 1) ? ("~" + Math.round(runningTime*1000*100.0)/100.0 + "ms") : (runningTime <= 60) ? ("~" + Math.round(100.0*runningTime)/100.0 + "s") : ("~" + runningTime/60 + "min");
        System.out.println("The " + primes + "th Prime has been found, " + finalPrime + ", in " + formattedTime);
        System.out.println("millitime = " + time);

        BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true));
        writer.append(algorithmChoiceString).append(" | ").append(String.valueOf(primes)).append(" | ").append(formattedTime);
        writer.newLine();
        writer.flush();
        writer.close();
    }
    /*
    primeFinderSieveMultithreaded method.
    A duplicate of the optimized Sieve method, however utilizing multithreading proportionate to the computer's processing power.
    */
    public static long primeFinderSieveMultithreaded(int primes) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int estimatedLimit = (primes <= 1000000) ? (int)(primes*Math.log(primes)*1.2+10) : (primes < 10000000) ? (int)(primes*Math.log(primes)*1.125) : (primes < 75000000) ? (int)(primes*Math.log(primes)*1.115) : (int)(primes*Math.log(primes)*1.11);

        int[] primeList = new int[primes];
        primeList[0] = 2;

        //(int)(primes*1.1)+10
        boolean[] primedBools = new boolean[estimatedLimit];
        Arrays.fill(primedBools, true);

        int sqrtEstimatedLimit = (int)Math.sqrt(estimatedLimit)+1;
        for (int i = 3; i <= sqrtEstimatedLimit; i+=2) {
            int tempi = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                if(primedBools[tempi]) {
                    for(int modifiedi = (tempi*tempi); modifiedi<estimatedLimit; modifiedi += 2*tempi) {primedBools[modifiedi] = false;}
                }

            }, executorService);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        int primeCount = 1;
        for (int i = 3; primeCount != primes; i += 2) {

                if (primedBools[i]) {
                    primeList[primeCount] = i;
                    //if (primeCount == primes) break;
                    primeCount++;
                }


        }


        executorService.shutdown();

        return (primes == 1) ? 2 : primeList[primes-1];
    }

    /*
    primeFinderSieve method.
    Uses the Sieve of Eratosthenes to eliminate non-primes (composites) in a
    boolean array.
    Then, transfers to an int array for better/simpler access.
    */
    public static long primeFinderSieve(int primes) {
        int estimatedLimit = (primes <= 1000000) ? (int)(primes*Math.log(primes)*1.2+10) : (int)(primes*Math.log(primes)*1.125);
        int[] primeList = new int[primes+1];
        primeList[0] = 2;
        // (int)(primes*1.1)+10]
        boolean[] primedBools = new boolean[estimatedLimit];
        Arrays.fill(primedBools, true);
        for (int i = 3; i*i <= estimatedLimit; i+=2) {
            if(primedBools[i]) {
                for(int modifiedi = (i*i); modifiedi<estimatedLimit; modifiedi =
                        modifiedi+i) {primedBools[modifiedi] = false;}
            }
        }
        int primeCount = 1;
        for (int i = 3; i< primedBools.length && primeCount != primes; i+=2) {
            if(primedBools[i]) {
                primeList[primeCount] = i;
                primeCount++;
            }
        }
        return (primes == 1) ? 2 : primeList[primes-1];
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

import java.util.Arrays;
import java.util.Scanner;

/*
Prime Finder
Aiden Rouhani
Last Edit: 10/26/23
 */

// Beginning primes: 2, 3, 5, 7, 11



public class aidenPrimes1 {
    /*
     Main method.
     Creates scanner object, asks for user input, and then activates the primeFinder method.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("PRIME FINDER STARTED");
        System.out.println("How many primes would you like to find?");
        int primes = Integer.parseInt(input.nextLine().replace(",", ""));


        int algorithmChoice = 1;
        System.out.println("Would you like to use the STANDARD (1) algorithm or the SIEVE algorithm (2)?");
        algorithmChoice = input.nextInt();
        input.nextLine();

        System.out.println("\n Starting prime finding process... \n");
        long startTime = System.nanoTime();
        long finalPrime = (algorithmChoice == 1) ? primeFinderStandard(primes) : primeFinderSieve(primes);
        double runningTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.println("The " + primes + "th Prime has been found: " + finalPrime + " in " + ((runningTime <= 60) ? runningTime : runningTime/60) + ((runningTime <= 60) ? " seconds" : " minutes"));
    }

    /*
    primeFinderSieve method.
    Uses the Sieve of Eratosthenes to eliminate non-primes (composites) in a boolean array.
    Then, transfers to an int array for better/simpler access.
     */
    public static long primeFinderSieve(int primes) {
        int primeCount = 0;
        int estimatedLimit = (primes <= 1000000) ? (int)(primes*Math.log(primes)*1.2+10) : (int)(primes*Math.log(primes)*1.125);
        int[] primeList = new int[estimatedLimit];
        boolean[] primedBools = new boolean[estimatedLimit];

        Arrays.fill(primedBools, true); // start by assuming all numbers are prime

        for (int i = 2; i*i <= estimatedLimit; i++) {
            // starting from 2, check every number until the sqrt of the estimated limit
            if(primedBools[i]) { // if that number is prime
                for(int modifiedi = (i*i); modifiedi<estimatedLimit; modifiedi = modifiedi+i) {
                    // starting from i^2, check multiples of i and set their values to false
                    primedBools[modifiedi] = false;
                }
            }
        }


        for (int i = 3; i< primedBools.length; i++) { // run through the whole list of booleans
            if(primedBools[i]) { // check if they're prime
                primeList[primeCount] = i; // if they are, add them to the list of primes
                primeCount++; // add one to the number of primes we've found
            }
        }

        return (primes == 1) ? 2 : primeList[primes-2];
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



                for (int ind = 2; (ind <= primeListLength && primeList[ind] * primeList[ind] <= mostRecentPrime+add); ind++) {
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
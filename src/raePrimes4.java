import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class raePrimes4 {
    static Scanner scanny = new Scanner(System.in);

    /**
     * We use n/64 to get the index of the int x is within. Then use bitwise operators to divide x by 2, take the mod of
     * x and 32, which gives us the index within the int at x/64. Then place a 1 at that index
     * @param primeBooleans our "boolean" array of integers
     * @param x the index in our "boolean" array
     */
    private static void makeComposite(int[] primeBooleans, int x) {
        primeBooleans[x/64] |= (1 << ((x >> 1) & 31));
    }
    /**
     * We use n/64 to get the index of the int x is within. Then use bitwise operators to divide x by 2, take the mod of
     * x and 32, which gives us the index within the int at x/64. Then bitwise AND the binary at that bit with 1 and
     * compare that output with 1 (the binary representation of {@link Boolean#TRUE})
     * @param primeBools our "boolean" array of integers
     * @param x the index in the "boolean" array
     * @return true is x is composite, false if prime
     */
    private static boolean isPrime(int[] primeBools, int x) {
        return (primeBools[x/64] & (1 << ((x >> 1) & 31))) == 0;
    }

    /**
     * Revamped algorithm using bitwise functionality to optimize processing time and memory density
     * @param n An int <i>n</i>
     * @return The <i>n</i>th prime number
     */
    public static long primeSieve(int n) {
        int estimatedLimit = (int)((Math.log(n) + Math.log(Math.log(n)) - 1 + ( (Math.log(Math.log(n)) - 2) / Math.log(n) )
                - (( Math.pow(Math.log(Math.log(n)),2) - 6 * Math.log(Math.log(n)) - 11 )/( 2 * Math.pow(Math.log(n),2) ))) * n);
        /*
         * We're using each individual bit of an int (32 bit) array as a boolean, and only storing odd numbers, so we can
         * cut the array size down to n/64. +1 for OBOE protection
         */
        int[] isPrimeBools = new int[estimatedLimit/64 + 1];
        Arrays.fill(isPrimeBools, 0000);
        for (int i = 3; i * i <= estimatedLimit; i+=2) {
            if (isPrime(isPrimeBools, i)) {
                for (int j = i*i, k = i << 1; j < estimatedLimit; j += k) {
                    makeComposite(isPrimeBools, j);
                }
            }
        }
        int finalPrime = 1;
        for (int foundPrimes = 1; foundPrimes <= n; finalPrime += 2) {
            if (isPrime(isPrimeBools, finalPrime)) {
                foundPrimes++;
            }
        }
        return n == 1 ? 2 : finalPrime-2;
    }
    public static void main(String[] args) {
        System.out.print(
                "Which prime do you wish to find?\nWARNING: " +
                "This may take several minutes depending on your computer! : ");
        String in = scanny.nextLine().replaceAll(",","");
        System.out.print("Preprocessing... ");
        int n = Integer.parseInt(in);
        long prime;

        System.out.print("Done!\nSeeking...");
        double start = System.nanoTime();
        if (n < 1) throw new IllegalArgumentException("Invalid input: " + n + "th prime number does not exist");
        if (n == 1) {
            prime = 2;
        }
        else {
            prime = primeSieve(n);
        }
        double end = System.nanoTime();

        System.out.print(" Done!\nPost-processing... ");
        String run = nsToTime(end - start);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String primed = formatter.format(prime);
        System.out.print("Done!\n");
        System.out.printf("\nFound the %sth prime (%s) in ~%s", formatter.format(n), primed, run);
    }
    // format methods (computer data -> human-readable data) below
    /**
     * Formats a nanosecond time into a human-readable form
     * @param ns double number of nanoseconds
     * @return String containing formatted timescale with 2 decimal digits
     */
    public static String nsToTime(double ns) {
        String suffix = " ns";
        if (ns >= 1000) {
            double us = ns / 1000;
            suffix = " µs";
            if (us >= 1000) {
                double ms =  us / 1000;
                suffix = " ms";
                if (ms >= 1000) {
                    double s = ms / 1000;
                    String out = "(" + ms + "ms)";
                    suffix = " s";
                    if (s >= 60) {
                        s = Math.floor(s * 100) / 100;
                        double min = s / 60;
                        min = Math.floor(min * 100) / 100;
                        return "" + min + " minutes " + out;
                    } else {
                        s = Math.floor(s * 100) / 100;
                        return "" + s + suffix + " " + out;
                    }
                } else {
                    ms = Math.floor(ms * 100) / 100;
                    return "" + ms + suffix;
                }
            } else {
                us = Math.floor(us * 100) / 100;
                return "" + us + suffix;
            }
        } else {
            ns = Math.floor(ns * 100) / 100;
            return "" + ns + suffix;
        }
    }
}

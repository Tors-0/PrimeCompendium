import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class raePrimes1 {
    /**
     funny enough, since this can store Integer.MAX_VALUE numbers, the limit to this program is actually the int value stored,
     as the 2,147,483,647 prime exceeds the integer limit. if we were to change this to an array of long instead, we
     could store the entire first 2,147,483,647 prime numbers, and it would only take 17,179,869,192 bytes of memory.
     That's 16 GiB of RAM! (or double the average laptops total memory)
     */
    static int[] primeList;
    static Scanner scanny = new Scanner(System.in);
    static long evals = 0;
    public static long nthPrime(long seek, int foundPrimes, int start) {
        // check every number 1 at a time until it finds n primes, has been changed to only check odd numbers to
        // increase speed, which contributed an additional 24.1% speed-up to the seeking process
        long result = -1;
        long mod = seek;
        for (int i = start; foundPrimes < seek; i+=2) {
            evals++;
            // only checks for factors from 1 to sqrt(n) instead of 1 to n
            // provides exponential speed increase over fastPrimeCheck\
            if (isPrime(i, foundPrimes)) {
                foundPrimes++;
                primeList[foundPrimes -1] = i;
                result = i;
            }
            if (i % mod <= 2) {
                System.out.print(".");
            }
        }
        return result;
    }
    public static boolean isPrime(int i, int foundPrimes) {
        double max = Math.sqrt(i) + 1;
            for (int j = 0; primeList[j] != 0 && primeList[j] <= max; j++) {
                evals++;
                if (i % primeList[j] == 0) {
                    return false;
                }
            }
        return true;
    }
    public static void main(String[] args) {
        System.out.print(
                "Which prime do you wish to find?\nWARNING: " +
                "This may take several minutes depending on your computer! : ");
        String in = scanny.nextLine().replaceAll(",","");
        System.out.print("Preprocessing... ");
        int n = Integer.parseInt(in);
        primeList = new int[n+1];
        long prime;

        System.out.print("Done!\nSeeking");
        double start = System.nanoTime();
        if (n < 1) throw new IllegalArgumentException("Invalid input: " + n + "th prime number does not exist");
        if (n == 1) {
            prime = 2;
        }
        else {
            primeList[0] = 2;
            prime = nthPrime(n,1,3);
        }
        double end = System.nanoTime();

        System.out.print(" Done!\nPost-processing... ");
        String run = nsToTime(end - start);
        String evaluated = numToMil(evals);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String primed = formatter.format(prime);
        System.out.print("Done!\n");
        System.out.println("\nFound the " + formatter.format(n) + "th prime (" + primed + ") in ~" + run);
        System.out.println("Evaluated ~" + evaluated + " factors/numbers during runtime!");
    }
    // format methods (computer data -> human-readable data)

    /**
     * Parses very large numbers into smaller ones with suffixes
     * @param n a very large number
     * @return String containing formatted number
     */
    public static String numToMil(double n) {
        int count = 0;
        while (n >= 1000) {
            if (count >= 10) break;
            n /= 1000.0;
            count++;
        }
        n = (double) Math.round(n * 100) / 100;
        if (count == 0) {
            return "" + n;
        } else if (count == 1) {
            return n + "k";
        } else if (count == 2) {
            return n + "m";
        } else if (count == 3) {
            return n + "b";
        } else if (count == 4) {
            return n + "q";
        } else if (count == 5) {
            return n + "Q";
        } else if (count == 6) {
            return n + "s";
        } else if (count == 7) {
            return n + "S";
        } else if (count == 8) {
            return n + "o";
        } else if (count == 9) {
            return n + "n";
        } else if (count == 10) {
            return n + "d";
        }
        return "error";
    }

    /**
     * Formats a nanosecond time into a human-readable form
     * @param ns double number of nanoseconds
     * @return String containing formatted timescale with 2 decimal digits
     */
    public static String nsToTime(double ns) {
        String suffix = " ns";
        if (ns >= 1000) {
            double us = ns / 1000;
            suffix = " \u00b5s";
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

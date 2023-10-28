import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class raePrimes4 {
    static Scanner scanny = new Scanner(System.in);
    static void makeComposite(int[] primeBools, int x) {
        primeBools[x/64] |= (1 << ((x >> 1) & 31));
    }
    public static int isComposite(int[] primeBools, int x) {
        return (primeBools[x/64] & (1 << ((x >> 1) & 31)));
    }
    public static long primeSieve(int n) {
        int estimatedLimit = (int)((Math.log(n) + Math.log(Math.log(n)) - 1 + ( (Math.log(Math.log(n)) - 2) / Math.log(n) )
                - (( Math.pow(Math.log(Math.log(n)),2) - 6 * Math.log(Math.log(n)) - 11 )/( 2 * Math.pow(Math.log(n),2) ))) * n);
        int[] isPrimeBools = new int[estimatedLimit/64 + 1];
        Arrays.fill(isPrimeBools, 0000);
        for (int i = 3; i * i <= estimatedLimit; i+=2) {
            if (isComposite(isPrimeBools, i) == 0) {
                for (int j = i*i, k = i << 1; j < estimatedLimit; j += k) {
                    makeComposite(isPrimeBools, j);
                }
            }
        }
        int finalPrime = 1;
        for (int foundPrimes = 1; foundPrimes <= n; finalPrime += 2) {
            if (isComposite(isPrimeBools, finalPrime) == 0) {
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

import java.util.Scanner;
import java.util.ArrayList;
public class maggiePrimes1 {
    public static void main(String[] args){
        //ask the user which prime to find
        Scanner myScanner = new Scanner(System.in);
        System.out.println("What prime do you want?");
        String input = myScanner.nextLine();
        //remove any commas in the number entered
        while (input.contains(",")){
            input = input.substring(0, input.indexOf(",")) + input.substring(input.indexOf(",") + 1);
        }
        //assign the inputted number (sans commas) to n
        int n = Integer.parseInt(input);
        //okay let's do this
        System.out.println("Searching...");
        long startTime = System.nanoTime();
        //make an arraylist to hold primes as we find them, and add 2 to this list
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        /* until we have enough primes, we go through every other number (starting with 3)
        and check if it's prime. if it is prime, we add it to the arraylist.
        we're skipping even numbers because those are obviously not prime. */
        int i = 3;
        while (primes.size() < n){
            if (isPrime(i, primes)){
                primes.add(i);
            }
            i += 2;
        }
        //find the time it took to finish and convert to a readable amount
        long endTime = System.nanoTime();
        long time = endTime - startTime;
        long timecopy = time;
        String timeResult;
        //if less than a second, convert to milliseconds
        if (time < 1000000000){
            time /= 1000000;
            timeResult = time + "ms";
        }
        //otherwise convert to seconds
        else {
            time /= 1000000000;
            //if more than 60 s, convert to minutes and seconds
            if (time > 60){
                int s = (int)time % 60;
                int m = ((int)time - s) / 60;
                timeResult = m + "m" + s + "s";
            }
            else {
                timeResult = time + "s";
            }
        }
        //print the nth prime
        System.out.println("The " + n + "th prime is " + primes.get(n - 1));
        System.out.println("It took " + timeResult + " to calculate.");
        // added by rae to fetch ms values
        System.out.println("added by rae: time in ms = " + (Math.round(timecopy/10000f) / 100f));
    }

    /* method to check if a number is prime, given the number and an arraylist of all primes
    less than the number we're checking
    */
    public static boolean isPrime(int num, ArrayList<Integer> primes){
        /*for every prime in primes (the arraylist of all primes less than num), we check
        if this number (num) is divisible by that prime. if it is, we return false - num is
        not prime. if we make it through all the primes and num isn't divisible by any of
        them, we return true - num is prime. we only need to check primes (not every number
        less than this number) because every number can be broken down into prime factors.
        see the fundamental theorem of arithmetic.
        we only need to check primes less than half of num, because any number greater than
        half of it will have to be multiplied by a number less than half of it.
        */
        int half = num/2;
        for (int prime: primes){
            if (prime > half){
                break;
            }
            if (num % prime == 0){
                return false;
            }
        }
        return true;
    }
}

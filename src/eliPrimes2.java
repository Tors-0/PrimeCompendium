import java.util.Scanner;
public class eliPrimes2 {
    public static void main(String[] args) {

        System.out.println("what number prime do you want to find? (As in the _th prime)");

        Scanner input = new Scanner(System.in);
        int total = input.nextInt();
        input.nextLine();

        long startTime = System.nanoTime();

        int[] primeBag = new int[total];

        primeBag[0] = 2;
        if(total >= 2){primeBag[1] = 3;}
        if(total >= 3){primeBag[2] = 5;}
        if(total >= 4){primeBag[3] = 7;}
        if(total >= 5){primeBag[4] = 11;}

        int number = 13;
        int NAS = 5;

        for (int i = 0; i < total - 5; i++) {

            int x = 1;

            while (true) {
                if (primeBag[x] * 2 > primeBag[NAS - 1]) {
                    primeBag[NAS] = number;
                    NAS++;
                    break;
                } else if (number % primeBag[x] != 0) {
                    x++;
                } else if (number % primeBag[x] == 0) {
                    number += 2;
                    x = 1;
                }
            }
            int percentDone = (int)(((double)(NAS-1) /(double)total)*100);
            System.out.print("\r" + "last prime found = " + number + " | " + percentDone + "% Done");
            if (total - 6 == i){System.out.print("\rDone!");}
            number += 2;
        }
        long endTime = System.nanoTime();
        System.out.println("\nThe " + total + "th prime number is " + primeBag[total - 1]);
        System.out.printf("Time taken is %s milliseconds",(double) Math.round(((double) (endTime-startTime)/1_000_000) * 100) / 100);
        System.out.printf(" or %s seconds",(double) Math.round(((double) (endTime-startTime)/1_000_000) * 100)/100000);
    }
}

//SAME PROGRAM AS BEFORE, JUST MORE OPTIMIZED (WHICH STILL PRODUCES AWFUL TIMES)
//IT'S FOR THIS REASON THAT I COULDN'T BE BOTHERED TO DO COMMENTS,

//THIS IS DEFINITELY THE LAST TIME I USE THIS TYPE OF ALGORITHM



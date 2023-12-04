import java.util.Scanner;

public class eliPrimes1 {
    public static void main(String[] args) {
        System.out.println("what number prime do you want to find? (As in the _th prime)");
        //ASKS USER FOR WHATEVER PRIME THEY WANT TO FIND

        Scanner input = new Scanner(System.in);
        int total = input.nextInt();
        input.nextLine();
        //RETRIEVES USER INPUT AND PUTS IT IN A total VARIABLE
        long startTime = System.nanoTime(); // added by rae
        int[] primeBag = new int[total];
        primeBag[0] = 2;
        //primeBag IS AN ARRAY THAT STORES ALL THE PRIME NUMBERS THE PROGRAM FINDS UP UNTIL WHATEVER PRIME THE USER WANTS
        //IT'S ALSO AN ESSENTIAL PART OF THE PRIME TESTER, WHICH IS WHY IT STARTS WITH 2 AS ITS FIRST VALUE, AS 2 IS THE FIRST PRIME NUMBER

        int progress = 0;
        int number = 3;
        int NAS = 1;
        //progress IS HOW MANY SPOTS ARE TAKEN IN THE ARRAY
        //number IS AN INTEGER THAT COUNTS EVERY ODD NUMBER STARTING FROM 3, WHICH IS THEN CHECKED TO SEE IF IT'S PRIME, AND ADDED TO THE ARRAY
        //NAS IS SHORT FOR NEXT AVAILABLE SPOT (IN THE ARRAY)

        System.out.print(primeBag[0] + ", ");
        //SINCE THE PROGRAM ONLY PRINTS THE PRIMES IT FINDS, AND 2 IS GIVEN TO IT, I DECIDED TO PRINT 2 JUST FOR CONSISTENCY

        for (int i = 0; i < total - 1; i++) {
        //LOOP THAT RUNS FOR HOWEVER MANY MORE SPOT'S IN THE ARRAY NEED TO BE FILLED

            int x = progress;
            //SET'S INTEGER x TO HOWEVER MANY INDEXES HAVE BEEN FILLED, AS THE PROGRAM WILL ONLY USE THOSE INDEXES TO FIND PRIMES

            while(true){
            //THIS IS THE ACTUAL PRIME FINDER, WHICH WORKS BY DIVIDING NEW NUMBERS BY ALL THE PRIME NUMBERS IT'S PREVIOUSLY FOUND (WHICH ARE STORED IN primeBag)

                if(x < 0) {
                    primeBag[NAS] = number;
                    //System.out.print(primeBag[NAS] + ", ");
                    NAS++;
                    progress++;
                    break;
                }
//              IF x IS LESS THAN 0, IT MEANS number HAS BEEN DIVIDED BY ALL THE INDEXES IN primeBag, AND IS 100% A PRIME NUMBER
//              THE NEWLY FOUND PRIME NUMBER IS THEN ADDED TO primeBag, THE NAS INTEGER MOVES TO THE NEXT INDEX, AND THE PROGRESS VARIABLE GOES UP BY ONE

//              NOTE: I KNOW "NAS" AND "progress" COULD PROBABLY BE COMBINED INTO ONE VARIABLE FOR OPTIMIZATION, BUT I DON'T FEEL LIKE CHANGING IT RIGHT NOW


                else if (number%primeBag[x] != 0) {
                        x--;
                } else if  (number%primeBag[x] == 0) {
                        number += 2;
                        x = progress;
                }
                //STARTS WITH ONE INDEX, CHECKS IF number IS DIVISIBLE BY THAT INDEX,
                //IF IT IS, THE x VARIABLE RESETS, AND number IS GIVEN THE NEXT ODD NUMBER TO BE RAN THROUGH THE CHECKER AGAIN
                //IF IT'S NOT, IT MOVES ON TO THE NEXT ONE UNTIL ALL THE INDEXES HAVE BEEN CHECKED

            }
        }
        long endTime = System.nanoTime(); // added by rae
        System.out.println();
        System.out.println("The" + total + "th prime number is " + primeBag[total - 1]);
        System.out.printf("Time taken is %s milliseconds",(double) Math.round(((double) (endTime-startTime)/1_000_000) * 100) / 100);
        //PRINTS THE FINAL PRIME NUMBER
    }
}

//MIGHT REVISIT IN THE FUTURE, THERE'S DEFINITELY A LOT OF ROOM FOR OPTIMIZATION
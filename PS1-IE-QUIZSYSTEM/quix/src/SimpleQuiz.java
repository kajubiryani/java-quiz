import java.util.Scanner;
public class SimpleQuiz{
    static Scanner sc = new Scanner(System.in);

    //rulesfunction
    static void showrules(){
        System.out.println("\\n============= RULES =============");
        System.out.println("~ The test consists of 3 stages.");
        System.out.println("~ Each stage has 5 multiple-choice questions.");
        System.out.println("~ Each question has 4 options (A, B, C, D).");
        System.out.println("~ Type A/B/C/D to answer.");
        System.out.println("~ Points per correct answer:");
        System.out.println("   - Stage 1: 1 point");
        System.out.println("   - Stage 2: 2 points");
        System.out.println("   - Stage 3: 3 points");
        System.out.println("~ Options are shuffled every time.");
        System.out.println("~ Total maximum score = 30 points.");
        System.out.println("=================================\n");
        System.out.println("PRESS ENTER TO CONTINUE...");
        sc.nextLine();
    }

    //center logic
    static int runstage(String[][] data, String stagename, int points){
        shufflequestions(data);

        System.out.println("\n=================================");
        System.out.println(stagename);
        System.out.println("=================================");

        int score=0;
        for (int i=0; i<data.length; i++){
            System.out.println("\nQ" + (i + 1) + ": " + data[i][0]);

            String[] options = {data[i][1], data[i][2], data[i][3], data[i][4]};
            String correct = data[i][5];
            shuffleoptions(options);

            System.out.println("A] " + options[0]);
            System.out.println("B] " + options[1]);
            System.out.println("C] " + options[2]);
            System.out.println("D] " + options[3]);

            System.out.println("Your Answer: ");
            char ans = sc.next().toUpperCase().charAt(0);

            int index= ans - 'A';

            if (index >= 0 && index < 4 && options[index].equals(correct)){
                score += points;
                System.out.println("Correct! +" + points + "Points");
            } else {
                System.out.println("Wrong! Correct Answer: " + correct);
            }
        }
        System.out.println("Score in " + stagename + ": " + score);
        return score;
    }

    //questionshuffle
    static void shufflequestions(String[][] data){
        for (int i=0; i<data.length; i++){
            int j = (int)(Math.random()*data.length);

            String[] temp = data[i];
            data[i] = data[j];
            data[j] = temp;
        }
    }

    //optionshuffle
    static void shuffleoptions(String[] options){
        for (int i = 0; i<options.length; i++){
            int j = (int)(Math.random()*options.length);
            String temp = options[i];
            options[i] = options[j];
            options[j] = temp;
        }
    }

    //scorefeedback
    static void showresult(int totalscore) {
        System.out.println("\n=================================");
        System.out.println("FINAL SCORE: " + totalscore + "/30");

        if (totalscore < 10)
            System.out.println("Performance: Needs Improvement");
        else if (totalscore < 20)
            System.out.println("Performance: Average");
        else if (totalscore < 30)
            System.out.println("Performance: Great Job");
        else
            System.out.println("Performance: Perfect Score!");

        System.out.println("=================================");
    }

    //stage 1 set
    static String[][] getstage1() {
        return new String[][] {
                {"Solve for x: 3x + 12 = 45.", "9", "11", "13", "15", "11"},
                {"A jacket is on sale for 20% off. If the sale price is $80, what was the original price?", "$90", "$96", "$100", "$120", "$100"},
                {"A train travels at 60 km/h for 2 hours and then 90 km/h for the next 3 hours. What is the average speed?", "75 km/h", "78 km/h", "80 km/h", "82 km/h", "78 km/h"},
                {"If 5 machines produce 5 widgets in 5 minutes, how long for 100 machines to produce 100 widgets?", "1 minute", "5 minutes", "20 minutes", "100 minutes", "5 minutes"},
                {"A bat and ball cost $1.10. The bat costs $1.00 more than the ball. How much does the ball cost?", "$0.05", "$0.10", "$0.15", "$1.00", "$0.05"}
        };
    }

    //stage 2 set
    static String[][] getstage2() {
        return new String[][] {
                {"Complete the sequence: 2, 6, 12, 20, 30, ?", "36", "40", "42", "48", "42"},
                {"If WATER is written as XBUFS, how is BLOOD written in that same code?", "AMNNC", "CMPPE", "CPPPE", "CNPPE", "CMPPE"},
                {"Pointing to a photograph, a man says: 'I have no brother or sister, but that man’s father is my father’s son.' Who is in the photograph?", "His father", "His nephew", "His son", "Himself", "His son"},
                {"In a race of 5 people (A, B, C, D, E): A finished ahead of B but behind C. D finished ahead of E but behind B. Who finished last?", "B", "C", "D", "E", "E"},
                {"In a group of 15 people, 7 read French, 8 read English, and 3 read both. How many read neither?", "2", "3", "4", "5", "3"}
        };
    }

    //stage 3 set
    static String[][] getstage3() {
        return new String[][] {
                {"Find the odd one out among the following units of measurement.", "Inch", "Ounce", "Centimeter", "Yard", "Ounce"},
                {"If a clock shows exactly 3:00, what is the interior angle between the hour and minute hand?", "45°", "60°", "90°", "120°", "90°"},
                {"A pattern: sides increase by 1, dots double. If first is triangle with 2 dots, what is 4th shape?", "Pentagon with 8 dots", "Hexagon with 8 dots", "Pentagon with 16 dots", "Hexagon with 16 dots", "Hexagon with 16 dots"},
                {"A 3X3X3 cube is painted. How many small cubes have exactly two sides painted?", "8", "12", "14", "18", "12"},
                {"Sequence of shapes: line, V, triangle, square. Next shape and diagonals?", "Pentagon; 2 diagonals", "Hexagon; 5 diagonals", "Pentagon; 5 diagonals", "Hexagon; 9 diagonals", "Pentagon; 5 diagonals"}
        };
    }

    //main function
    public static void main(String[] args){

        int totalscore = 0;
        System.out.println("=================================");
        System.out.println("WELCOME TO 3 STAGE APTITUDE TEST.");
        System.out.println("=================================");
        System.out.println("PRESS ENTER TO START...");
        sc.nextLine();

        showrules();

        totalscore += runstage(getstage1(), "STAGE 1: BASIC MATHS", 1);
        totalscore += runstage(getstage2(), "STAGE 2: LOGICAL REASONING", 2);
        totalscore += runstage(getstage3(), "STAGE 3: ABSTRACT THINKING", 3);

        showresult(totalscore);
    }
}

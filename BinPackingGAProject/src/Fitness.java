import java.util.Scanner;

public class Fitness {
    Scanner kb = new Scanner(System.in);

    /*
     * Ask the users for input and would call the GA, SA, and Foolish. After
     * calculating, it would output the result
     */
    public void start() {
        System.out.print("Rank Selection or Roulette (Type RS or R): ");
        String select = kb.next();
        while ((select.equals("RS")) == false && (select.equals("R")) == false) {
            System.out.print("Type RS or R: ");
            select = kb.next();
        }

        System.out.print("Cut-Crossfilled or Point-Based Crossover (CC or PB): ");
        String crossover = kb.next();
        while ((crossover.equals("CC")) == false && (crossover.equals("PB")) == false) {
            System.out.print("Type CC or PB: ");
            crossover = kb.next();

        }

        System.out.print("Single-Move or Pairwise Mutation (SM or PW): ");
        String mutate = kb.next();
        while ((mutate.equals("SM")) == false && (mutate.equals("PW")) == false) {

            System.out.print("Type SM or PW: ");
            mutate = kb.next();
        }

        System.out.print("Set a generation time: ");
        int time = check();

        GeneticAlgorithm g1 = new GeneticAlgorithm(10, select, crossover, mutate);
        SAandFoolish g2 = new SAandFoolish(10, mutate, "SA");
        SAandFoolish g3 = new SAandFoolish(10, mutate, "Foolish");
        Chromosomes best;
        int[] SABest;
        int[] FoolishBest;
        int count = 1;

        System.out.println("Bin Size: 10");
        System.out.println("Algorithm: Generational Genetic Algorithm");
        System.out.println();

        for (int i = 1; i <= time; i++) {
            g1.run();
            if ((count / 50) == 1) {
                best = g1.best();
                System.out.println("Generation: " + i);
                System.out.print("Best Chromosome: [" + best.chromosome[0]);
                for (int j = 1; j < best.chromosome.length; j++) {
                    System.out.print(", " + best.chromosome[j]);
                }
                System.out.print("]");
                System.out.println();
                System.out.print("Fitness: " + best.fitnessVal);
                System.out.println();
                count = 0;
            }
            count++;
        }

        System.out.println(
                "--------------------------------------------------------------------------------");
        System.out.println();

        count = 1;
        System.out.println("Alogrithm: Simulated Annealing Algorithm");
        System.out.println();

        for (int i = 1; i <= time; i++) {
            g2.run();
            if ((count / 50) == 1) {
                SABest = g2.best();
                System.out.println("Generation: " + i);
                System.out.print("Best Chromosome: [" + SABest[0]);
                for (int j = 1; j < SABest.length; j++) {
                    System.out.print(", " + SABest[j]);
                }
                System.out.print("]");
                System.out.println();
                System.out.println("Fitness: " + g2.currentFitnessVal(SABest, false) +
                        " Bins");
                System.out.println();
                count = 0;
            }
            count++;
        }

        count = 1;
        System.out.println(
                "--------------------------------------------------------------------------------");
        System.out.println();

        System.out.println("Alogrithm: Foolish Algorithm");
        System.out.println();

        for (int i = 1; i <= time; i++) {
            g3.run();
            if ((count / 50) == 1) {
                FoolishBest = g3.best();
                System.out.println("Generation: " + i);
                System.out.print("Best Chromosome: [" + FoolishBest[0]);
                for (int j = 1; j < FoolishBest.length; j++) {
                    System.out.print(", " + FoolishBest[j]);
                }
                System.out.print("]");
                System.out.println();
                System.out.println("Fitness: " + g3.currentFitnessVal(FoolishBest, false) +
                        " Bins");
                System.out.println();
                count = 0;
            }
            count++;
        }

    }

    // method that starts the whole program.
    public static void main(String[] args) {
        Fitness f1 = new Fitness();
        f1.start();
    }

    /*
     * a check method that checks if the user is giving the program a number and
     * that it is greater than or equal to 0. If both these cases
     * are not met, than it will repeatdly ask the user to input a valid number.
     * 
     * @return user input for either the bin size or the number of generation
     */
    public int check() {
        int val = 0;
        do {
            try {
                String num = kb.next();
                val = Integer.parseInt(num);
            } catch (NumberFormatException e) {
                System.out.print("That is not a valid input Enter an actual number: ");
            }
            if (val <= 0) {
                System.out.print("Enter a number greater than 0: ");
            }
        } while (val <= 0);
        return val;
    }
}

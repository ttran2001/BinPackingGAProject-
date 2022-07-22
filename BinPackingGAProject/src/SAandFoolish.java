import java.util.ArrayList;

public class SAandFoolish {
    private int binSize;
    private String mutate;
    public int[] solution;
    public int[] solution2;
    private double temperature = 3;
    private String mode;
    private int iteration = 10;
    private ArrayList<Integer> data = new ArrayList<Integer>();
    private int num;
    private ArrayList<Integer> bestfit;
    private double alpha = 0.87;
    private int beta = 1;

    public SAandFoolish(int binSize, String mutate, String mode) {
        this.binSize = binSize; // set the binsize of how much each bin can hold;
        this.mutate = mutate; // setting the mutation that the user chose;
        this.mode = mode;

        Dataset dataset = new Dataset();
        data = dataset.getData();

        solution = new int[data.size()];
        solution2 = new int[data.size()];

        for (int i = 0; i < solution.length; i++) {
            num = (data.remove((int) (Math.random() * data.size())));
            solution[i] = num;
            solution2[i] = num;
        }
    }

    // This starts both the Foolish Hill Climbing Algorithm and Simulated Annealing
    // Algorithm.
    // After a certain about of iteration, the value would replace the temperature
    // and iteration by multiplying
    // alpha with temperature and iteration with beta.
    public void run() {
        for (int i = 0; i < iteration; i++) {
            mutation(solution2);
            best();
        }
        temperature *= alpha;
        iteration *= beta;
    }

    /*
     * This is the best method. If SA, it would use the parameters of either the new
     * solution is better than the previous solution or random is
     * less than e. If it is Foolish, it would just check if it is a better solution
     * than the previous and if it isnt than it would not replace it. If both
     * solution1 and solution2 are the
     * same binsize, we would check the last bin size and the one with the lowest
     * bin size is the best.
     * 
     * @return best chromosomes
     */
    public int[] best() {

        if (mode.equals("SA")) {
            if ((currentFitnessVal(solution, false) > currentFitnessVal(solution2, false))
                    || (random() < Math.pow(2.718,
                            ((currentFitnessVal(solution2, false) - currentFitnessVal(solution, false))
                                    / temperature)))) {
                for (int i = 0; i < solution2.length; i++) {
                    solution[i] = solution2[i];
                }
            } else if (currentFitnessVal(solution, false) == currentFitnessVal(solution2, false)) {
                if (currentFitnessVal(solution, true) > currentFitnessVal(solution2, true)) {
                    for (int i = 0; i < solution2.length; i++) {
                        solution[i] = solution2[i];
                    }
                } else {
                    for (int i = 0; i < solution.length; i++) {
                        solution2[i] = solution[i];
                    }
                }
            } else {
                for (int i = 0; i < solution.length; i++) {
                    solution2[i] = solution[i];
                }
            }
        } else {
            if (currentFitnessVal(solution, false) > currentFitnessVal(solution2, false)) {
                for (int i = 0; i < solution2.length; i++) {
                    solution[i] = solution2[i];
                }
            } else if (currentFitnessVal(solution, false) == currentFitnessVal(solution2, false)) {
                if (currentFitnessVal(solution, true) > currentFitnessVal(solution2, true)) {
                    for (int i = 0; i < solution2.length; i++) {
                        solution[i] = solution2[i];
                    }
                } else {
                    for (int i = 0; i < solution.length; i++) {
                        solution2[i] = solution[i];
                    }
                }
            } else {
                for (int i = 0; i < solution.length; i++) {
                    solution2[i] = solution[i];
                }
            }
        }
        return solution;
    }

    public void mutation(int[] newSolution) {
        int swap = 0;
        int loc = 0;
        int loc2 = 0;
        // Start of Pairwise Mutation. Works
        if (mutate.equals("PW")) {
            loc = (int) (Math.random() * newSolution.length); // get the first allele from the chromosome
            loc2 = (int) (Math.random() * newSolution.length); // get the second allele from the chromosome
            while (loc2 == loc) {
                loc2 = (int) (Math.random() * newSolution.length); // Just in case if loc2 draws the same
                // number as loc1;
            }
            swap = newSolution[loc]; // set the swap to the first allele
            newSolution[loc] = newSolution[loc2]; // set the first allele location to the second
                                                  // allele
            newSolution[loc2] = swap; // set the second allele to the first allele value

        } // End of Pairwise Mutation

        /*
         * This is the Single-Move Mutation Operator. What this mutation does is that it
         * would is that it would choose a random allele and insert
         * it at a random location. Since each chromosomes are a array, we would use for
         * loop to move the alleles to the right spot to perform the
         * insertion. After doing the insertion, the mutated chromosome would then be
         * added to the mutated child mutatedPool.
         */
        else { // Start of Single-Move Mutation
            loc = (int) (Math.random() * newSolution.length);
            loc2 = (int) (Math.random() * newSolution.length);
            while (loc2 == loc) {
                loc2 = (int) (Math.random() * newSolution.length); // If the loc index,
            }
            // good
            swap = newSolution[loc];
            if (loc < loc2) {
                for (int i = loc; i < loc2; i++) {
                    newSolution[i] = newSolution[i + 1];
                }
            } else {
                for (int i = loc; i > loc2; i--) {
                    newSolution[i] = newSolution[i - 1];
                }
            }
            newSolution[loc2] = swap;
        }
    }

    // returns either a 0 or 1 for random
    // @return random value of 0 or 1
    public int random() {
        int val = (int) (Math.random() * 2);
        return val;
    }

    // See currentFitnessVal note in Chromosomes class
    public int currentFitnessVal(int[] chromosome, boolean getTie) {
        int lastBinSize = 0;
        bestfit = new ArrayList<Integer>();
        boolean isStored = false;
        int best = 0;
        int pivot = 0;
        int fitnessVal = 0;
        int size = 0;
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < bestfit.size(); j++) {
                if (binSize >= (bestfit.get(j) + chromosome[i]) && (best < (bestfit.get(j) + chromosome[i]))) {
                    best = bestfit.get(j) + chromosome[i];
                    pivot = j;
                    isStored = true;
                }
            }
            if (isStored) {
                bestfit.set(pivot, best);
            } else {
                if ((size + chromosome[i]) > binSize) {
                    bestfit.add(size);
                    fitnessVal++;
                    size = chromosome[i];
                } else if ((size + chromosome[i]) == binSize) {
                    fitnessVal++;
                    size = 0;
                } else {
                    size += chromosome[i];
                }
            }
            isStored = false;
        }
        if (size != 0) {
            fitnessVal++;
            lastBinSize = size;
        }
        if (getTie) {
            return lastBinSize;
        }
        return fitnessVal;
    }
}

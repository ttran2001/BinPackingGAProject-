import java.util.ArrayList;

public class GeneticAlgorithm {
    private int binSize; // The size of each of the bins
    private String select; // The selection variable to store the string that the user want the selection
    private String cross; // The crossover variable to store the string that the user want the crossover
                          // to be perform
    private String mutate; // The mutate variable to store the string that the user want the mutation to be
                           // perform
    private int crossRate = 100; // 100% Crossover Rate
    private int mutateRate = 20; // 5% Mutation Rate
    private int popsize = 100; // Population of 100
    private ArrayList<Chromosomes> parentpool = new ArrayList<Chromosomes>();
    private ArrayList<Chromosomes> childpool = new ArrayList<Chromosomes>();
    private ArrayList<Chromosomes> mutatedchildpool = new ArrayList<Chromosomes>();
    private ArrayList<Chromosomes> c1 = new ArrayList<Chromosomes>(); // The population pool

    /*
     * This is the GeneticAlgorithm constructor. We set the binsize, select, cross,
     * and mutate to what the user enter or chooses.
     * At the same time, we would then create a dataset for the genetic algorithm
     * and create the population pool by randomizing the dataset and
     * storing it into the population pool.
     */
    public GeneticAlgorithm(int binSize, String select, String cross, String mutate) {
        this.binSize = binSize; // set the bin size of how much each bin can hold;
        this.select = select; // setting the selection that the user chose;
        this.cross = cross; // setting the crossover that the user chose;
        this.mutate = mutate; // setting the mutation that the user chose;

        for (int i = 0; i < popsize; i++) {
            Dataset dataset = new Dataset();
            c1.add(new Chromosomes(dataset.getData(), this.binSize)); // randomized the dataset and create a
                                                                      // choromosome
            c1.get(i).currentFitnessVal();
        }
    }

    /*
     * This method goes through the whole GA algorithm and
     * would find the best chromosomes after the pool goes through the whole
     * Generational GA. Returns the best chromosomes from the mutated child pool
     */

    /*
     * What this method do would start the genetic algorithm by running first going
     * through elitism, selection, crossover, and
     * finally mutation. The mutatedchildpool would be the new population pool
     */
    public void run() {
        Elitism();
        selection();
        crossover();
        mutation();
        for (int i = 0; i < popsize; i++) {
            c1.add(mutatedchildpool.remove(0));
        }
    }

    /*
     * What this methods does is that it would find the fittest chromosomes that has
     * the least amount of bins used. If it was tie, it would
     * call the getTied method.
     * 
     * @return best chromosomes.
     */
    public Chromosomes best() {
        int best = 0;
        for (int i = 0; i < c1.size(); i++) {
            c1.get(i).currentFitnessVal();
        }
        for (int i = 1; i < c1.size(); i++) {
            if (c1.get(i).fitnessVal < c1.get(best).fitnessVal) {
                best = i;
            } else if (c1.get(i).fitnessVal == c1.get(best).fitnessVal) {
                if (c1.get(i).getTied() < c1.get(best).getTied()) {
                    best = i;
                }
            } else {
                continue;
            }
        }
        return c1.get(best);
    }

    /*
     * This methods finds the fittest two choromosomes from the population pool.
     * If there are two chromosomes with the same fitness value, we would check the
     * last bin size
     * instead and would pick the one with the smallest size bin. The two fittest
     * would directly be
     * added to the mutated child pool.
     */
    private void Elitism() { // done. Do not focus on
        int[] position = new int[2]; // creating a array
        position[0] = 0; // Initially set the first two chromosomes as the initial fittest values.
        position[1] = 1;
        int count = 2; // Initialize the count since we have the first two value at the start
        while (count != c1.size()) {
            if (c1.get(count).fitnessVal < c1.get(position[0]).fitnessVal) {
                position[1] = position[0];
                position[0] = count;
                count++;
            } else if (c1.get(count).fitnessVal < c1.get(position[1]).fitnessVal) {
                position[1] = count;
                count++;
            } else if (c1.get(count).fitnessVal == c1.get(position[1]).fitnessVal) {
                if (c1.get(count).getTied() < c1.get(position[1]).getTied()) {
                    position[1] = count;
                    count++;
                } else {
                    count++;
                }
            } else {
                count++;
            }
        }
        mutatedchildpool.add(c1.remove(position[0])); // add the first best chromosomes to the mutated pool
        if (position[1] != 0) {
            mutatedchildpool.add(c1.remove(position[1] - 1)); // add the second best out of the pool to the mutated pool
        }
    }

    /*
     * This is the selection operator. I decided to choose different selection
     * operators which is the
     * Roulette Selection and the Rank Selection. The roulette selection would add
     * all the fittest value of the
     * chromosomes and would pick the chromosome by getting a random value from 1 to
     * the total fitness value.
     * We would track it by adding chromosome fittest value one by one until the
     * track value is greater than the
     * random value. We would get the last chromosome and add it to the parent pool.
     * For the rank selection, we would rank
     * each of the chromosomes by using a different ArrayList. Same thing would be
     * like the roulette selection but
     * we instead add the rank rather than the actual fitness value one by one.
     */
    public void selection() {
        int count = 0;
        int loc = 0;
        int track = 0;
        int randomVal = 0;

        ArrayList<Integer> ranking = new ArrayList<Integer>();
        int rank = 0; // For rank

        // Start of Roulette Selection. Done
        if (select.equals("RS")) { // DONE
            for (int i = 0; i < c1.size(); i++) {
                count += c1.get(i).fitnessVal;
            }

            while (c1.size() != 1) {
                randomVal = (int) ((Math.random() * count) + 1);
                track += c1.get(loc).fitnessVal; // first index of c1
                while (randomVal >= track) {
                    track += c1.get(loc).fitnessVal;
                    loc++;
                }
                if (loc > 0) {
                    loc--;
                }
                count -= c1.get(loc).fitnessVal;
                parentpool.add(c1.remove(loc));
                track = 0;
                loc = 0;
            }
            parentpool.add(c1.remove(0));
        }
        // End of Roulette Selection
        else { // Start of Rank Selection
            while (loc != c1.size()) {
                loc++;
                count += loc;
            }

            while (track != c1.size()) {
                for (int i = 0; i < c1.size(); i++) {
                    for (int j = 0; j < c1.size(); j++) {
                        if (c1.get(i).fitnessVal > c1.get(j).fitnessVal) {
                            rank++;
                        }
                    }
                    ranking.add(rank);
                }
                track++;
            }

            while (c1.size() != 1) {
                loc = 0;
                randomVal = (int) ((Math.random() * count) + 1);
                while (count >= track) {
                    track += ranking.get(loc); // ERROR
                    loc++;
                }
                if (loc > 0) {
                    loc--;
                }
                count -= ranking.get(loc);
                ranking.remove(loc);
                parentpool.add(c1.remove(loc));
            }
            ranking.remove(0);
            parentpool.add(c1.remove(0));
        }
    }

    public void crossover() {
        boolean isRate;
        int crossover = 0;
        Chromosomes parent1;
        Chromosomes parent2;
        ArrayList<Integer> store1 = new ArrayList<Integer>();
        ArrayList<Integer> store2 = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        int val = 0;
        /*
         * Cut-Cross Crossover. Randomly select a slice positions in the chromosomes
         * form index 1 to N-1.
         * Slice both parent into two parts. Copy the left part of parent1 for child1
         * and copy the left part
         * of parent2 to child2. After that, we would than scan parent2 from left to
         * right and fill in the right part of child1 with values
         * from parent2 and the same for child2 but we use parent1. We would skip the
         * alleles that are already
         * contain.
         */
        if (cross.equals("CC")) {
            while (parentpool.size() != 0) {
                Dataset master1 = new Dataset();
                Dataset master2 = new Dataset();
                ArrayList<Integer> masterList1 = master1.getData();
                ArrayList<Integer> masterList2 = master2.getData();

                isRate = rating(crossRate);

                parent1 = parentpool.remove((int) (Math.random() * (parentpool.size())));
                parent2 = parentpool.remove((int) (Math.random() * (parentpool.size())));

                if (isRate) {

                    // Pick a slice point from 1 to N-1
                    crossover = (int) (Math.random() * ((parent1.chromosome.length) + 1));
                    if (crossover == parent1.chromosome.length) {
                        crossover -= 1;
                    }

                    for (int i = 0; i < crossover; i++) {
                        store1.add(parent1.chromosome[i]);
                        masterList1.remove(Integer.valueOf(store1.get(i)));

                        store2.add(parent2.chromosome[i]);
                        masterList2.remove(Integer.valueOf(store2.get(i)));
                    }

                    for (int j = 0; j < parent1.chromosome.length; j++) {
                        if (masterList1.contains(parent2.chromosome[j])) {
                            store1.add(parent2.chromosome[j]);
                            masterList1.remove(Integer.valueOf(parent2.chromosome[j]));
                        }

                        if (masterList2.contains(parent1.chromosome[j])) {
                            store2.add(parent1.chromosome[j]);
                            masterList2.remove(Integer.valueOf(parent1.chromosome[j]));
                        }
                    }
                    for (int k = 0; k < parent1.chromosome.length; k++) {
                        parent1.chromosome[k] = store1.remove(0);
                        parent2.chromosome[k] = store2.remove(0);
                    }
                    childpool.add(parent1);
                    childpool.add(parent2);
                } else {
                    childpool.add(parent1);
                    childpool.add(parent2);
                }
            }
        }
        // End of Cut-and-Crossfilled Crossover
        else { // Start of Position-Based Crossover
            /*
             * For this crossover, we woudl randomly select 4 different positions. The
             * elements of the selected position would stay the same for parent1 and
             * parent2.
             * The remaining elements are inherited from the other parents in order of which
             * they
             * appear.
             */
            while (parentpool.size() != 0) {
                Dataset master1 = new Dataset();
                Dataset master2 = new Dataset();
                ArrayList<Integer> masterList1 = master1.getData();
                ArrayList<Integer> masterList2 = master2.getData();

                isRate = rating(crossRate); // Calculates if the two parents will perform crossover. If not, they would
                                            // just be added to the child pool.
                parent1 = parentpool.remove((int) (Math.random() * (parentpool.size())));
                parent2 = parentpool.remove((int) (Math.random() * (parentpool.size())));

                if (isRate) { // If isRate is equal to true, than perform the uniform crossover. Else, the two
                              // parent chromosomes will just be added to the children pool.
                    while (position.size() != 4) {
                        val = ((int) (Math.random() * (parent1.chromosome.length)));
                        while (position.contains(val)) {
                            val = ((int) (Math.random() * (parent1.chromosome.length)));
                        }
                        position.add(val);
                        masterList1.remove(Integer.valueOf(parent1.chromosome[val]));
                        masterList2.remove(Integer.valueOf(parent2.chromosome[val]));
                    }

                    for (int j = 0; j < parent2.chromosome.length; j++) {
                        if (masterList1.contains(parent2.chromosome[j])) {
                            store1.add(parent2.chromosome[j]);
                            masterList1.remove(Integer.valueOf(parent2.chromosome[j]));
                        }
                        if (masterList2.contains(parent1.chromosome[j])) {
                            store2.add(parent1.chromosome[j]);
                            masterList2.remove(Integer.valueOf(parent1.chromosome[j]));
                        }
                    }

                    for (int k = 0; k < parent1.chromosome.length; k++) {
                        if (position.contains(k) == false) {
                            parent1.chromosome[k] = store1.remove(0);
                            parent2.chromosome[k] = store2.remove(0);
                        }
                    }
                    childpool.add(parent1); // add the first child to the childpool
                    childpool.add(parent2); // add the second child to the childpool
                    position.clear();
                } else {
                    childpool.add(parent1);
                    childpool.add(parent2);
                }
            } // End of Point-Based Crossover
        }
    }

    /*
     * This is the Pairwise Mutation Operator. What this mutation does is tha it
     * would choose two random alleles from each chromosomes
     * when the while loops goes through each chromosomes in the child pool. After
     * choosing the two alleles, they would then be swap. After that,
     * the mutated chromosome would be added to the mutated child pool and this
     * would repeat until there are no more chromosome in the child
     * pool
     */
    public void mutation() {
        boolean isRate;
        Chromosomes temp;
        int swap;
        int loc = 0;
        int loc2 = 0;
        // Start of Pairwise Mutation. Works
        if (mutate.equals("PW")) {
            while (childpool.size() != 0) {
                isRate = rating(mutateRate);
                if (isRate) {
                    temp = childpool.remove(0); // gets the first chromosomes from the child pool
                    loc = (int) (Math.random() * temp.chromosome.length); // get the first allele from the chromosome
                    loc2 = (int) (Math.random() * temp.chromosome.length); // get the second allele from the chromosome
                    while (loc2 == loc) {
                        loc2 = (int) (Math.random() * temp.chromosome.length); // Just in case if loc2 draws the same
                                                                               // number as loc1;
                    }
                    swap = temp.chromosome[loc]; // set the swap to the first allele
                    temp.chromosome[loc] = temp.chromosome[loc2]; // set the first allele location to the second allele
                    temp.chromosome[loc2] = swap; // set the second allele to the first allele value
                    mutatedchildpool.add(temp); // add the chromosome to the mutated child pool after mutation
                } else { // If isRate returns a false, we will just place the first chromosome from the
                         // child pool to the mutated child pool without mutation
                    mutatedchildpool.add(childpool.remove(0));
                }
            }
        } // End of Pairwise Mutation

        /*
         * This is the Single-Move Mutation Operator. What this mutation does is that it
         * would is that it would choose a random allele and insert
         * it at a random location. Since each chromosomes are a array, we would use for
         * loop to move the alleles to the right spot to perform the
         * insertion. After doing the insertion, the mutated chromosome would then be
         * added to the mutated child pool.
         */
        else { // Start of Single-Move Mutation
            while (childpool.size() != 0) {
                isRate = rating(mutateRate);
                if (isRate) { // good
                    temp = childpool.remove(0);
                    loc = (int) (Math.random() * temp.chromosome.length);
                    loc2 = (int) (Math.random() * temp.chromosome.length);
                    while (loc2 == loc) {
                        loc2 = (int) (Math.random() * temp.chromosome.length); // If the loc index,
                    }
                    // good

                    swap = temp.chromosome[loc];

                    if (loc < loc2) {
                        for (int i = loc; i < loc2; i++) {
                            temp.chromosome[i] = temp.chromosome[i + 1];
                        }
                    } else {
                        for (int i = loc; i > loc2; i--) {
                            temp.chromosome[i] = temp.chromosome[i - 1];
                        }
                    }
                    temp.chromosome[loc2] = swap;
                    mutatedchildpool.add(temp);
                } else {
                    mutatedchildpool.add(childpool.remove(0));
                }
            }
        } // End of Single-Move Mutation
    }

    /*
     * This method does the rate. We would call the Math.random and if the value if
     * equal or lower than the val, then we
     * we would call the operator. However if its greater, then the operator would
     * not be called. Done. Do not check.
     * 
     * @param rate
     * 
     * @return boolean if the algorithm should be used
     */
    public static boolean rating(int rate) {
        int val = (int) ((Math.random() * 100) + 1);
        if (val > rate) {
            return false;
        } else {
            return true;
        }
    }
}

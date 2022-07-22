import java.util.ArrayList;

public class Chromosomes {
    public int fitnessVal;
    private int binSize;
    public int[] chromosome;
    private int size;
    public int lastBinSize;
    public ArrayList<Integer> bestfit;

    public Chromosomes(ArrayList<Integer> dataset, int binSize) {
        chromosome = new int[dataset.size()];
        fitnessVal = 0;
        lastBinSize = 0;
        size = 0;
        int count = 0;
        this.binSize = binSize;
        while (dataset.size() != 0) {
            chromosome[count] = (dataset.remove((int) (Math.random() * dataset.size())));
            count++;
        }
    }

    /*
     * The point of this method is to calculate the fitness value for the
     * chromosomes after it has been mutated.
     * This would update both fitnessVal and lastBinSize. This would go through
     * eachj
     * 
     * @return the fitness value
     */
    public void currentFitnessVal() {
        bestfit = new ArrayList<Integer>();
        boolean isStored = false;
        int best = 0;
        int pivot = 0;
        fitnessVal = 0;
        lastBinSize = 0;
        size = 0;
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
    }

    /*
     * Returns the amount of free space in each of the bins plus the size of the
     * last
     * bin
     * 
     * @return the the total of the free spaces in each of the bins
     */
    public int getTied() {
        return lastBinSize;
    }
}

import java.util.ArrayList;

public class Dataset {
    private ArrayList<Integer> dataset = new ArrayList<Integer>();

    public Dataset() {
        dataSet();
    }

    /* The dataset that is used for the packages size */
    public void dataSet() {
        dataset.add(8);
        dataset.add(2);

        dataset.add(7);
        dataset.add(3);

        dataset.add(6);
        dataset.add(3);
        dataset.add(1);

        dataset.add(3);
        dataset.add(3);
        dataset.add(2);
        dataset.add(2);

        dataset.add(5);
        dataset.add(4);
        dataset.add(1);

        dataset.add(4);
        dataset.add(4);
        dataset.add(2);

    }

    public ArrayList<Integer> getData() {
        return dataset;
    }
}

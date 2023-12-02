import com.zeroc.Ice.Current;

import java.util.*;

import Sorting.*;

public class MasterCoordinatorI implements WorkersHandler {

    private ArrayList<SorterPrx> registeredWorkers = new ArrayList<>();
    private int numberOfWorkers;

    @Override
    public void registerWorker(SorterPrx worker, Current current) {
        System.out.println("------- Worker conectado ------");
        registeredWorkers.add(worker);
        numberOfWorkers = registeredWorkers.size();
    }

    @Override
    public void unregisterWorker(SorterPrx worker, Current current) {
        System.out.println("###### Worker desconectado ######");
        registeredWorkers.remove(worker);
        numberOfWorkers = registeredWorkers.size();
        // Lanzar método para cuando se desconecte un worker. ¿Qué sucede con los datos
        // que estaba ordenando?
    }

    @Override
    public List<String> disSort(List<String> dataToSort, Current current) {
        List<List<String>> buckets = divideIntoBuckets(dataToSort);

        dataToSort = parallelSortBuckets(buckets);

        return dataToSort;
    }

    private List<List<String>> divideIntoBuckets(List<String> data) {
        List<List<String>> buckets = new ArrayList<>();

        // Calcular la amplitud de cada bucket
        int min = 48;
        int max = 122;
        int amp = (max - min) / (int) (Math.sqrt(this.numberOfWorkers));

        // Inicializar buckets
        for (int i = 0; i < this.numberOfWorkers; i++) {
            buckets.add(new ArrayList<>());
        }

        // Distribuir elementos en buckets
        for (String value : data) {
            int bucketIndex = Math.min((value.charAt(0) - min) / amp, this.numberOfWorkers - 1);
            buckets.get(bucketIndex).add(value);
        }

        return buckets;
    }

    private List<String> parallelSortBuckets(List<List<String>> buckets) {

        List<List<String>> sortedBuckets = new ArrayList<>();

        
        //Distribute buckets into Workers
        for (int i = 0; i<buckets.size(); i++) {
            sortedBuckets.add(this.registeredWorkers.get(i).sortArray(buckets.get(i)));
        }

        List<String> sortedData = new ArrayList<>();
        //Concat Results this can be optimized by taking the expected position of the sorted Bucket into the original list.
        for (List<String> sortedBucket: sortedBuckets){
            sortedData.addAll(sortedBucket);
        }
        return sortedData;
    }

}


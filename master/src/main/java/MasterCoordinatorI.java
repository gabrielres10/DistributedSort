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
        System.out.println("Entra disSort()");
        List<List<String>> chunks = divideArrayList(dataToSort, (int)(dataToSort.size()/numberOfWorkers));
        System.out.println("datos chunks:\n"+chunks.toString());
        //Distribute buckets into Workers
        List<List<String>> sortedBuckets = new ArrayList<>();
        for (int i = 0; i<numberOfWorkers; i++) {
            sortedBuckets.add(this.registeredWorkers.get(i).sortArray(chunks.get(i)));
        }
        dataToSort = kWayMerge(sortedBuckets);
        System.out.println("datos ordenaos:\n"+dataToSort.toString());

        return dataToSort;
    }

    private List<List<String>> divideArrayList(List<String> originalList, int chunkSize) {
        List<List<String>> dividedList = new ArrayList<>();


        for (int i = 0; i < numberOfWorkers; i ++) {
            List<String> chunk = new ArrayList<>();
            if (i == (this.numberOfWorkers-1)){
                chunk = new ArrayList<>(originalList.subList(i*chunkSize, originalList.size()));
            }else{
                chunk = new ArrayList<>(originalList.subList(i*chunkSize, (i+1)*chunkSize));
            }
            dividedList.add(chunk);
        }

        return dividedList;
    }

    private List<String> kWayMerge(List<List<String>> lists) {
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val.compareTo(b.val));

        // Agregar el primer elemento de cada lista al min heap
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                minHeap.offer(new ListNode(i, 0, lists.get(i).get(0)));
            }
        }

        List<String> result = new ArrayList<>();

        while (!minHeap.isEmpty()) {
            ListNode node = minHeap.poll();
            result.add(node.val);

            // Mover al siguiente elemento de la lista actual
            if (node.index + 1 < lists.get(node.listIndex).size()) {
                minHeap.offer(new ListNode(node.listIndex, node.index + 1, lists.get(node.listIndex).get(node.index + 1)));
            }
        }

        return result;
    }

    class ListNode {
        int listIndex;
        int index;
        String val;

        public ListNode(int listIndex, int index, String val) {
            this.listIndex = listIndex;
            this.index = index;
            this.val = val;
        }
    }

}


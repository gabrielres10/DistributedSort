import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.zeroc.Ice.Current;

import Sorting.*;

public class SortingI implements Sorter {

    @Override
    public List<String> sortArray(List<String> input, Current current) {
        System.out.println("Received array to sort: " + input.toString());

        // Search file

        // Sort the array
        ArrayList<String> arrayList = new ArrayList<>(input);
        Collections.sort(arrayList);

        System.out.println("Sorted array: " + arrayList.toString());

        return arrayList;
    }
}
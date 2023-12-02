import com.zeroc.Ice.Current;

import java.util.*;
import Sorting.*;

public class MasterCoordinatorI implements WorkersHandler{

    private ArrayList<SorterPrx> registeredWorkers = new ArrayList<>();

    @Override
    public void registerWorker(SorterPrx worker, Current current) {
        System.out.println("------- Worker conectado ------");
        registeredWorkers.add(worker);
    }

    @Override
    public void unregisterWorker(SorterPrx worker, Current current) {
        System.out.println("###### Worker desconectado ######");
        registeredWorkers.remove(worker);

        //Lanzar método para cuando se desconecte un worker. ¿Qué sucede con los datos que estaba ordenando?
    }
    
}

import com.zeroc.Ice.*;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Sorting.*;

public class Worker extends Application{
    public static void main(String[] args) {
        Worker app = new Worker();
        int status = app.main("Worker", args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) {
        Communicator communicator = null;
        try {
            communicator = Util.initialize(args, "worker.cfg");

            //Connecting to the MasterCoordinator:
            WorkersHandlerPrx masterCoordinator = WorkersHandlerPrx.checkedCast(communicator.propertyToProxy("Coordinator.proxy"));

            if (masterCoordinator == null){

                throw new Error("Invalid proxy");
            }

            ObjectAdapter adapter = communicator.createObjectAdapter("Sorting.Worker");
            SortingI sorter = new SortingI();
            adapter.add(sorter, Util.stringToIdentity("Sorting"));
            adapter.activate();
            
            //Object to register into Server:
            SorterPrx worker = SorterPrx.uncheckedCast(adapter.createProxy(com.zeroc.Ice.Util.stringToIdentity("Sorting")));

            //Register to MasterCoordinator:
            masterCoordinator.registerWorker(worker);

            System.out.println("Worker Server running...");
            communicator.waitForShutdown();

            masterCoordinator.unregisterWorker(worker);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
        return 0;
    }

   
}

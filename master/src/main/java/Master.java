import com.zeroc.Ice.*;

import Sorting.*;

import java.io.*;
import java.lang.Exception;
import java.net.*;
import java.util.*;

public class Master extends Application {
  
  public static void main(String[] args) {
    Master app = new Master();
    int status = app.main("Master", args);
    System.exit(status);
  }

  @Override
  public int run(String[] args) {
    Communicator communicator = null;
    try {
      communicator = Util.initialize(args, "master.cfg");

      ObjectAdapter adapter = communicator.createObjectAdapter(
        "Master.Coordinator"
      );
      com.zeroc.Ice.Object coordinator = new MasterCoordinatorI();
      adapter.add(coordinator, Util.stringToIdentity("MasterCoordinator"));
      adapter.activate();
      
      System.out.println("Master Server running...");
      
      waitingFileName();
      // -----------------Lógica para ordenar el archivo---------------
      ArrayList<Integer> numbersToOrder = new ArrayList<>();
      numbersToOrder.add(5);
      numbersToOrder.add(2);
      numbersToOrder.add(8);
      numbersToOrder.add(1);
      numbersToOrder.add(9);
      long init = System.currentTimeMillis();
      dis_order(numbersToOrder);

      long end = System.currentTimeMillis();
      System.out.println("Tiempo de transferencia: " + (end - init) + "ms");
      communicator.waitForShutdown();
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

  private void waitingFileName() throws IOException {
    int port = 9876;

    DatagramSocket serverSocket = new DatagramSocket(port);

    byte[] receiveData = new byte[1024];

    System.out.println("Esperando la conexión del cliente...");

    DatagramPacket receivePacket = new DatagramPacket(
            receiveData,
            receiveData.length
    );
    serverSocket.receive(receivePacket);

    String fileName = new String(receivePacket.getData()).trim();
    System.out.println("Recibiendo el nombre del archivo: " + fileName);

    // Enviar confirmación al cliente
    byte[] confirmationBytes = "OK".getBytes();
    DatagramPacket confirmationPacket = new DatagramPacket(
            confirmationBytes,
            confirmationBytes.length,
            receivePacket.getAddress(),
            receivePacket.getPort()
    );
    serverSocket.send(confirmationPacket);

    System.out.println("Nombre del archivo recibido con éxito." + fileName);
    serverSocket.close();
  }


  private <T extends Comparable<T>> void dis_order(List<T> dataToOrder) {
    // Implementación del método para ordenar datos genéricos
    System.out.println(dataToOrder);
}
}

import Sorting.*;
import com.zeroc.Ice.*;
import java.io.*;
import java.lang.Exception;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {

  public static long total = 0;

  public static void main(String[] args) {
    try {
		sendFile();
	} catch (IOException e) {
		e.printStackTrace();
	}
    Communicator communicator = null;
    try {
      communicator = Util.initialize(args, "client.cfg");
      WorkersHandlerPrx masterCoordinator = WorkersHandlerPrx.checkedCast(communicator.propertyToProxy("Coordinator.proxy"));

      if (masterCoordinator == null) {
        throw new RuntimeException("Invalid proxy");
      }

      List<String> inputList = new ArrayList<>();
      inputList.add("banana");
      inputList.add("apple");
      inputList.add("orange");
      inputList.add("grape");
      inputList.add("watermelon");
      ArrayList<String> result = (ArrayList<String>) masterCoordinator.disSort(inputList);
      System.out.println("Sorted result: " + result.toString());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (communicator != null) {
        communicator.destroy();
      }
    }
  }

  private static void sendFile() throws IOException{
    String serverAddress = "localhost"; // Cambia esto con la dirección del servidor
    int serverPort = 9876; // Cambia esto con el puerto del servidor

    DatagramSocket clientSocket = new DatagramSocket();

    File fileToSend = new File("client/src/main/java/ejemplo.txt"); // Cambia esto con la ruta del archivo que deseas enviar
    String fileName = fileToSend.getName();

    // Enviar el nombre del archivo al servidor
    byte[] fileNameBytes = fileName.getBytes();
    DatagramPacket fileNamePacket = new DatagramPacket(
      fileNameBytes,
      fileNameBytes.length,
      InetAddress.getByName(serverAddress),
      serverPort
    );
    clientSocket.send(fileNamePacket);
    clientSocket.close();
  }
  /*private static void printTime(long inicio, long fin, String operation) {
    // Convertir nanosegundos a milisegundos (1 segundo = 1,000,000,000
    // nanosegundos)
    long tiempoTranscurrido = fin - inicio;
    long tiempoEnMilisegundos = tiempoTranscurrido / 1000000;
    System.out.println(
      "Tiempo de ejecución: " + tiempoEnMilisegundos + " " + operation
    );
    total += tiempoEnMilisegundos;
  }

  private static List<String> readDataFromFile(String fileName) {
    List<String> data = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        data.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }*/

}

import Sorting.*;
import com.zeroc.Ice.*;
import java.io.*;
import java.lang.Exception;
import java.net.*;

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
      SorterPrx sorter = SorterPrx.checkedCast(
        communicator.propertyToProxy("Sorter.Proxy")
      );

      if (sorter == null) {
        throw new RuntimeException("Invalid proxy");
      }
      //ArrayList<String> result = (ArrayList<String>) sorter.sortArray();
      //System.out.println("Sorted result: " + result.toString());
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

    File fileToSend = new File("client/src/main/java/ejemplo.txt"); 
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

    // Enviar el contenido del archivo al servidor
    FileInputStream fileInputStream = new FileInputStream(fileToSend);

    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
      DatagramPacket packet = new DatagramPacket(
        buffer,
        bytesRead,
        InetAddress.getByName(serverAddress),
        serverPort
      );
      clientSocket.send(packet);
    }

    // Enviar un paquete adicional para indicar el final del archivo
    byte[] endOfFileBytes = new byte[0];
    DatagramPacket endOfFilePacket = new DatagramPacket(
      endOfFileBytes,
      endOfFileBytes.length,
      InetAddress.getByName(serverAddress),
      serverPort
    );
    clientSocket.send(endOfFilePacket);

    System.out.println("Archivo enviado con éxito.");
    fileInputStream.close();
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

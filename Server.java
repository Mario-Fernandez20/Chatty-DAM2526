/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chatty;
    import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author mario
 */


public class Server {

    private static final int TCP_PORT = 5000;
    private static final int UDP_PORT = 5001;
    private static final int MAX_CLIENTES = 3;

    private static Map<String, InetSocketAddress> clientes = new HashMap<>();

    public static void main(String[] args) throws Exception {

        new Thread(() -> {
            try {
                ServerSocket serverTCP = new ServerSocket(TCP_PORT);
                System.out.println("Servidor TCP iniciado...");

                while (clientes.size() < MAX_CLIENTES) {
                    Socket socket = serverTCP.accept();

                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    String nombre = in.readUTF();
                    int puertoUDP = in.readInt();

                    InetSocketAddress dir =
                            new InetSocketAddress(socket.getInetAddress(), puertoUDP);

                    clientes.put(nombre, dir);
                    System.out.println(nombre + " conectado: " + dir);

                    socket.close();
                }

                System.out.println("3 clientes conectados. Chat activo.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        DatagramSocket socketUDP = new DatagramSocket(UDP_PORT);
        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(paquete);

            for (InetSocketAddress addr : clientes.values()) {
                if (!addr.equals(paquete.getSocketAddress())) {
                    DatagramPacket envio =
                            new DatagramPacket(paquete.getData(),
                                    paquete.getLength(),
                                    addr);
                    socketUDP.send(envio);
                }
            }
        }
    }
}



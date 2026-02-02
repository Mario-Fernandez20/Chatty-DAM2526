/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chatty;

/**
 *
 * @author mario
 */
import java.net.*;
import java.util.Scanner;

public class ClienteUDP {

    private static final String SERVIDOR = "localhost";
    private static final int PUERTO = 6969;

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();

        DatagramSocket socket = new DatagramSocket();

        // Hilo para recibir mensajes
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket paquete =
                            new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);
                    System.out.println(new String(
                            paquete.getData(), 0, paquete.getLength()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Enviar mensajes
        while (true) {
            String mensaje = scanner.nextLine();
            String texto = "> " + nombre + ": " + mensaje;

            byte[] datos = texto.getBytes();
            DatagramPacket paquete =
                    new DatagramPacket(datos,
                            datos.length,
                            InetAddress.getByName(SERVIDOR),
                            PUERTO);
            socket.send(paquete);
        }
    }
}

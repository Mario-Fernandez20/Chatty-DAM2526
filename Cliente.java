/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chatty;
import java.net.*;
import java.io.*;

/**
 *
 * @author mario
 */
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public Cliente(Socket socket, String nombreCliente){
        try{
            this.socket = socket;
            this.nombreCliente = nombreCliente;
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            cerrarTodo();
        }
    }

    public void enviarMensaje(){
        try {
            bufferedWriter.write(nombreCliente);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String mensaje = scanner.nextLine();
                bufferedWriter.write("> " + nombreCliente + ": " + mensaje);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e){
            cerrarTodo();
        }
    }

    public void escucharMensaje(){
        new Thread(() -> {
            String mensaje;
            while (socket.isConnected()){
                try {
                    mensaje = bufferedReader.readLine();
                    System.out.println(mensaje);
                } catch (IOException e){
                    cerrarTodo();
                    break;
                }
            }
        }).start();
    }

    public void cerrarTodo(){
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce tu nombre: ");
        String nombreCliente = scanner.nextLine();

        Socket socket = new Socket("localhost", 6969);
        Cliente cliente = new Cliente(socket, nombreCliente);
        cliente.escucharMensaje();
        cliente.enviarMensaje();
    }
}


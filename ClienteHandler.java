/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Chatty;

/**
 *
 * @author mario
 */
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteHandler implements Runnable {

    private static ArrayList<ClienteHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreCliente;

    public ClienteHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            this.nombreCliente = bufferedReader.readLine();
            clientHandlers.add(this);

            enviarMensaje("SERVER: Se ha unido el cliente " + nombreCliente);

        } catch (IOException e){
            cerrarTodo();
        }
    }

    @Override
    public void run() {
        String mensaje;

        while (socket.isConnected()){
            try {
                mensaje = bufferedReader.readLine();
                enviarMensaje(mensaje);
            } catch (IOException e){
                cerrarTodo();
                break;
            }
        }
    }

    public void enviarMensaje(String mensaje){
        for (ClienteHandler cliente : clientHandlers){
            try {
                if (!cliente.nombreCliente.equals(nombreCliente)){
                    cliente.bufferedWriter.write(mensaje);
                    cliente.bufferedWriter.newLine();
                    cliente.bufferedWriter.flush();
                }
            } catch (IOException e){
                cerrarTodo();
            }
        }
    }

    public void cerrarTodo(){
        clientHandlers.remove(this);
        enviarMensaje("SERVER: " + nombreCliente + " ha salido del chat");
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int getNumeroClientes(){
        return clientHandlers.size();
    }
}

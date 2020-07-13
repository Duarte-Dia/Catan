/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Utilizador
 */
public class Server {

    private static int port = 6666, nClientes = 1;
    private static Vector<ClientHandler> listaClientes = new Vector<>();
    private static Socket client;
    

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(port);
        

        while (true) {
            // Servidor fica a espera de um cliente
            if(nClientes <=4 ){
            System.out.println("[SERVER]Esperando por ligação");
             client = server.accept();
            System.out.println("[SERVER]Estabelecido ligação a um cliente");
            
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            ClientHandler ch = new ClientHandler("Cliente " + nClientes, in, out, client);
            Thread t = new Thread(ch);

            System.out.println("[SERVER]Cliente " + nClientes + " adicionado ");
            listaClientes.add(ch);

            out.writeUTF("Sucesso a Conectar o Cliente "+nClientes+" \n");
            out.writeUTF("#SetPlayer"+nClientes);
            t.start();

            nClientes++;}

        }

        // fechar ligação
        /*
         client.close();
         System.out.println("[Server]A desligar");
         server.close();*/
    }

    private static class ClientHandler implements Runnable {

        private String name;
        final DataInputStream in;
        final DataOutputStream out;
        Socket socket;
        boolean logged;

        public ClientHandler(String name, DataInputStream in, DataOutputStream out, Socket s) {

            this.name = name;
            this.in = in;
            this.out = out;
            this.socket = s;
            logged = true;

        }

        @Override
        public void run() {
            String cmd;

            while (true) {

                try {
                    cmd = in.readUTF();
                    System.out.println(cmd);

                    StringTokenizer st = new StringTokenizer(cmd, "#");
                    String receivingClient = null;
                    try {
                        receivingClient = st.nextToken();
                    } catch (Exception e) {
                    };
                    String msg = null;
                    try {
                        msg = st.nextToken();
                    } catch (Exception e) {
                    };

                    if (msg != null) {

                        for (ClientHandler client : Server.listaClientes) {
                            if (client.name.equals(receivingClient) && client.logged) {
                                client.out.writeUTF("Whisper from " + name + ": " + msg);
                            }

                        }
                    } else {

                        for (ClientHandler client : Server.listaClientes) {
                            if (!client.name.equals(name) && client.logged) {
                                client.out.writeUTF(name + ": " + receivingClient);
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}

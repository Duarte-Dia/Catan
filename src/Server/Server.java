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
 *  Esta classe define e inicia o servidor
 * 
 * @author Utilizador
 */
public class Server {

    private static int port = 6666, nClientes = 1;
    private static Vector<ClientHandler> listaClientes = new Vector<>();
    private static Socket client;
    
    /**
     * Método Main do servidor
     * 
    */

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(port);
        

        while (true) {
            //Servidor fica à espera do cliente
            if(nClientes <=4 ){
            System.out.println("[SERVER]Esperando por ligação");
             client = server.accept();
            System.out.println("[SERVER]Estabelecido ligação a um cliente");
            
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            ClientHandler ch = new ClientHandler("Cliente " + nClientes, in, out, client);
            Thread t = new Thread(ch);
            
            // Quando um Cliente é iniciado este é adicionado à lista de Clientes, e o Servidor recebe a informação
            // que o Cliente  é adicionado
            System.out.println("[SERVER]Cliente " + nClientes + " adicionado ");
            listaClientes.add(ch);
            
            // O Cliente recebe a informação que teve Sucesso a conectar
            out.writeUTF("Sucesso a Conectar o Cliente "+nClientes+" \n");
            out.writeUTF("#SetPlayer"+nClientes);
            // A thread é iniciada
            t.start();

            nClientes++;}

        }

        // fechar ligação
        /*
         client.close();
         System.out.println("[Server]A desligar");
         server.close();*/
    }
       /**
        * Classe que controla os clientes dentro do servidor
        */
    private static class ClientHandler implements Runnable {

        private String name;
        final DataInputStream in;
        final DataOutputStream out;
        Socket socket;
        boolean logged;
    
        /**
         * Construtor da classe ClientHandler
         *
         * @param name parametro que atribui um nome ao cliente
         * @param in parametro referente à informação enviada pelo cliente ao servidor
         * @param out parametro referente à informação recebida do cliente, pelo servidor
         * @param s parametro referente ao Socket
         */
        public ClientHandler(String name, DataInputStream in, DataOutputStream out, Socket s) {

            this.name = name;
            this.in = in;
            this.out = out;
            this.socket = s;
            logged = true;

        }
          /**
           * Método evocado, após o inicio da thread.
           * Manipula as solicitações feitas pelo cliente
           */
        @Override
        public void run() {
            String cmd;

            while (true) {

                try {
                    cmd = in.readUTF();
                    System.out.println(cmd);
                     // Sempre que aparece # numa String
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
                            /* Permite que o cliente envie uma mensagem a outro cliente especifico
                            Procura na lista de Clientes, e quando encontra o Cliente desejado,
                             a mensagem é enviada para ele */
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

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
    
    private static int port = 9090, nClientes=0;
    private static Vector<ClientHandler> listaClientes = new Vector<>();
    
    
    public static void main(String[] args) throws IOException{
    
        ServerSocket server = new ServerSocket(port);
        String msg ="Ligas te ao servidor";
        
        while(true){
        // Servidor fica a espera de um cliente
        System.out.println("[SERVER]Esperando por ligação");
        Socket client = server.accept();
        System.out.println("[SERVER]Estabelecido ligação a um cliente");
        
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        
        ClientHandler ch = new ClientHandler("Cliente "+ nClientes,in,out,client);
        Thread t= new Thread(ch);
        
        System.out.println("[SERVER] Cliente adicionado ");
        listaClientes.add(ch);
        t.start();
        
        nClientes++;
        
        
        }
         
       
        
        
        
        
    // fechar ligação
        /*
        client.close();
        System.out.println("[Server]A desligar");
        server.close();*/ 
    }
}

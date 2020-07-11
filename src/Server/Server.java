/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.*;

/**
 *
 * @author Utilizador
 */
public class Server {
    
    private static int port = 9090;
    
    
    public static void main(String[] args) throws IOException{
    
        ServerSocket server = new ServerSocket(port);
        String msg ="Ligas te te ao servidor like a boss";
        
        // Servidor fica a espera de um cliente
        System.out.println("[Server]Esperando por ligação");
        Socket client = server.accept();
        System.out.println("[Server]Ligado a client");
        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
         BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out.println( msg.toString() );
        
        
        
        
    // fechar ligação
        /*
        client.close();
        System.out.println("[Server]A desligar");
        server.close();*/ 
    }
}

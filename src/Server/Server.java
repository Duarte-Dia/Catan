/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
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
        
        // Servidor fica a espera de um cliente
        Socket client = server.accept();
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println( "Oi mano!" );
        
    // fechar ligação
        client.close();
    }
}

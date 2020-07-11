/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Utilizador
 */
public class Client {
    
    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 9090;
    
    public static void main(String[] args) throws IOException {
        
        Socket socket = new Socket(serverIP, serverPort);
        
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        String serverResponse = input.readLine();
        
        JOptionPane.showMessageDialog(null, serverResponse);
        
        socket.close();
        System.exit(0);
        
    }
    
}

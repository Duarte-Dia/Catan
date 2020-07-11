/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Utilizador
 */
public class ClientHandler implements Runnable {
    
    private String name;
    final DataInputStream in;
    final DataOutputStream out;
    Socket socket;
    boolean logged;
    
    
    public ClientHandler (String name,DataInputStream in,DataOutputStream out,Socket s){
        
        this.name= name;
        this.in=in;
        this.out=out;
        this.socket=s;
        logged=true;
    
    
    }

    @Override
    public void run() {
        String cmd;
        
        while(true){
        
            try{
            cmd= in.readUTF();
            System.out.println(cmd);
            
            
            
            
            } catch(IOException e){}
        
        
        
        }
    }
    
}

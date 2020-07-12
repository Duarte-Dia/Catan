/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import common.Le;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 *
 * @author Utilizador
 */
public class Client {

    
    // ATENÇÃO PODERA HAVER NECESSIDADE DE FUNDIR O CLIENTA HÁ CALSSE PLAYER PARA FACILITAR FUTURAS IMPLEMENTAÇOES
    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 9090;

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket(serverIP, serverPort);
        
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

       Thread enviarMensagem = new Thread(new Runnable(){
           @Override
           public void run(){
               while(true){
                   String msg = Le.umaString();
                   try{
                       out.writeUTF(msg);
                   } catch(IOException e){
                       
                   }
               }
           }
       });
        
        Thread lerMensagem = new Thread(new Runnable(){
           @Override
           public void run(){
               while(true){
                   try{
                      String msg = in.readUTF();
                      System.out.println(msg);
                   } catch(IOException e){
                       
                   }
               }
           }
       });
        
        lerMensagem.setDaemon(true);
        enviarMensagem.start();
        lerMensagem.start();
        

    }

}

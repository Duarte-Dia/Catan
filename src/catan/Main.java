/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import common.Le;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Bruno Ribeiro
 */
public class Main extends Application {
    
    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 6666;

    static int chosenTile = 0;
    static boolean gameover, endPlay;
    static Dice dice = new Dice();
    static List<Player> listPlayers = new ArrayList<Player>();
    static Board board = new Board();
    public static TextArea chat;
    private static TextField inputChat;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        chat = FXMLDocumentController.chat;
        inputChat= FXMLDocumentController.inputChatText;
        Scene scene = new Scene(root);

            
        stage.setScene(scene);
        stage.show();
        
        connectClient();
        
        
        
    }


    public static void main(String[] args) throws UnknownHostException, IOException {
        
        
        launch(args);
        
        
        gameover = true;

        while (!gameover) {
            for (int i = 0; i < listPlayers.size(); i++) {
                endPlay = false;
                chosenTile = dice.throwDice(2);

                for (Hexagon hex : board.getTiles()) {
                    if (chosenTile == hex.getNum()) {
                        givePlayersResources(hex.getResourceID(), hex);
                    }
                }

                do {

                } while (!endPlay);

                longestRoad();

                if (isGameOver()) {
                    gameover = true;
                }
            }
        }
    }

    private static void givePlayersResources(int resource, Hexagon hex) {

        for (Player p : listPlayers) {
            for (City c : p.getListCities()) {
                if (hex.containVector(c.getPosition())) {
                    // PLAYER GETS RESOURCES !!!!!!!!!!!!!
                }
            }
        }
    }

    private static boolean isGameOver() {

        for (Player p : listPlayers) {
            p.addScore(p.getListSettlements().size());

            p.addScore(p.getListCities().size() * 2);

            if (p.isLongestRoad()) {
                p.addScore(2);
            }

            if (p.isBiggestArmy()) {
                p.addScore(2);
            }

         //  p.addScore(p.devCardsPoints());

            if (p.getScore() >= 10) {
                return true;
            }
        }

        return false;
    }

    private static void longestRoad() {
        List<Integer> listRoadSizes = new ArrayList<Integer>();
        int size, playerSelected;

        for (Player p : listPlayers) {
            listRoadSizes.add(p.getListRoads().size());
        }

        size = Collections.max(listRoadSizes);

        if (size > 5) {
            if (Collections.frequency(listRoadSizes, size) == 1) {
                playerSelected = listRoadSizes.indexOf(Collections.max(listRoadSizes));

                for (Player p : listPlayers) {
                    p.setLongestRoad(false);
                }

                listPlayers.get(playerSelected).setLongestRoad(true);
            }
        }
    }
    
    
    private void connectClient() throws IOException{
    
    
        Socket socket = new Socket(serverIP, serverPort);
        
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
        

       Thread enviarMensagem = new Thread(() -> {
           while(true){
               String msg = Le.umaString() + "\n";
               
               try{
                   out.writeUTF(msg);
                   
                   chat.appendText(msg);
               } catch(IOException e){
                   
               }
           }
        });
        
        Thread lerMensagem;
        lerMensagem = new Thread(() -> {
            while(true){
                try{
                    String msg = in.readUTF();

                    System.out.println(msg);
                    chat.appendText(msg);
                } catch(IOException e){
                    
                }
            }
        });
        
        //lerMensagem.setDaemon(true);
        enviarMensagem.start();
        lerMensagem.start();
    
    
    }

}


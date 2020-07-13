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

                if (chosenTile != 7) {
                    for (Hexagon hex : board.getTiles()) {
                        if (chosenTile == hex.getNum()) {
                            givePlayersResources(hex.getResourceID(), hex);
                        }
                    }
                } else {
                    // LANÇOU 7
                    // MOVE LADRAO
                }

                do {
                } while (!endPlay);

                longestRoad();
                biggestArmy();

                if (isGameOver()) {
                    gameover = true;
                }
            }
        }
    }

    private static void givePlayersResources(int resource, Hexagon hex) {
        int current;
        for (Player p : listPlayers) {
            for (City c : p.getListCities()) {
                if (hex.containVector(c.getPosition())) {
                    switch (resource) {
                        case 1: // wool
                            current = p.getWool();
                            p.setWool(current += 2);
                            break;
                        case 2: // timber
                            current = p.getTimber();
                            p.setTimber(current += 2);
                            break;
                        case 3: // brick
                            current = p.getBrick();
                            p.setBrick(current += 2);
                            break;
                        case 4: // wheat
                            current = p.getWheat();
                            p.setWheat(current += 2);
                            break;
                        case 5: // metal
                            current = p.getMetal();
                            p.setMetal(current += 2);
                            break;
                        default:
                            break;
                    }
                }
            }

            for (Settlement s : p.getListSettlements()) {
                if (hex.containVector(s.getPosition())) {
                    switch (resource) {
                        case 1: // wool
                            current = p.getWool();
                            p.setWool(current++);
                            break;
                        case 2: // timber
                            current = p.getTimber();
                            p.setTimber(current++);
                            break;
                        case 3: // brick
                            current = p.getBrick();
                            p.setBrick(current++);
                            break;
                        case 4: // wheat
                            current = p.getWheat();
                            p.setWheat(current++);
                            break;
                        case 5: // metal
                            current = p.getMetal();
                            p.setMetal(current++);
                            break;
                        default:
                            break;
                    }
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

            //p.addScore(p.devCardsPoints());
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

        if (size >= 5) {
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

    private static void biggestArmy() {
        List<Integer> listArmySizes = new ArrayList<Integer>();
        int size, playerSelected;

        for (Player p : listPlayers) {
            listArmySizes.add(p.getArmy());
        }

        size = Collections.max(listArmySizes);

        if (size >= 3) {
            if (Collections.frequency(listArmySizes, size) == 1) {
                playerSelected = listArmySizes.indexOf(Collections.max(listArmySizes));

                for (Player p : listPlayers) {
                    p.setBiggestArmy(false);
                }

                listPlayers.get(playerSelected).setBiggestArmy(true);
            }
        }
    }

    private void tradeResources(Player p1, Player p2, int resource1, int resource2, int quantity1, int quantity2) {
        int currentP1, currentP2;
        switch (resource1) {
            case 1: // wool
                currentP1 = p1.getWool();
                currentP2 = p2.getWool();
                p1.setWool(currentP1 + quantity1);
                p2.setWool(currentP2 - quantity1);
                break;
            case 2: // timber
                currentP1 = p1.getTimber();
                currentP2 = p2.getTimber();
                p1.setTimber(currentP1 + quantity1);
                p2.setTimber(currentP2 - quantity1);
                break;
            case 3: // brick
                currentP1 = p1.getBrick();
                currentP2 = p2.getBrick();
                p1.setBrick(currentP1 + quantity1);
                p2.setBrick(currentP2 - quantity1);
                break;
            case 4: // wheat
                currentP1 = p1.getWheat();
                currentP2 = p2.getWheat();
                p1.setWheat(currentP1 + quantity1);
                p2.setWheat(currentP2 - quantity1);
                break;
            case 5: // metal
                currentP1 = p1.getMetal();
                currentP2 = p2.getMetal();
                p1.setMetal(currentP1 + quantity1);
                p2.setMetal(currentP2 - quantity1);
                break;
            default:
                break;
        }

        switch (resource2) {
            case 1: // wool
                currentP1 = p1.getWool();
                currentP2 = p2.getWool();
                p1.setWool(currentP1 - quantity2);
                p2.setWool(currentP2 + quantity2);
                break;
            case 2: // timber
                currentP1 = p1.getTimber();
                currentP2 = p2.getTimber();
                p1.setTimber(currentP1 - quantity2);
                p2.setTimber(currentP2 + quantity2);
                break;
            case 3: // brick
                currentP1 = p1.getBrick();
                currentP2 = p2.getBrick();
                p1.setBrick(currentP1 - quantity2);
                p2.setBrick(currentP2 + quantity2);
                break;
            case 4: // wheat
                currentP1 = p1.getWheat();
                currentP2 = p2.getWheat();
                p1.setWheat(currentP1 - quantity2);
                p2.setWheat(currentP2 + quantity2);
                break;
            case 5: // metal
                currentP1 = p1.getMetal();
                currentP2 = p2.getMetal();
                p1.setMetal(currentP1 - quantity2);
                p2.setMetal(currentP2 + quantity2);
                break;
            default:
                break;
        }
    }

}

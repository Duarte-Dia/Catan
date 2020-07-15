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
import java.net.UnknownHostException;
import javafx.application.Platform;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 *
 * @author Bruno Ribeiro
 */
public class Main extends Application {

    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 6666;

    static int chosenTile = 0;
    int idJogadorLocal = 1, i;
    static boolean gameover, endPlay;
    static Dice dice = new Dice();
    static List<Player> listPlayers = new ArrayList<Player>();
    static Board board = new Board();
    public static TextArea chat;
    private static TextField inputChat;
    public static Tab tp1, tp2, tp3, tp4;
    public static Button endTurn, roadButton, bankTradeBtn;
    public static MenuItem exitBtn, contributorsBtn, playerOpt1, playerOpt2, playerOpt3, harborOpt1, harborOpt2, harborOpt3;
        DataInputStream in ;
        DataOutputStream out;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        chat = FXMLDocumentController.chat;
        inputChat = FXMLDocumentController.inputChat;

        tp1 = FXMLDocumentController.tp1;
        tp2 = FXMLDocumentController.tp2;
        tp3 = FXMLDocumentController.tp3;
        tp4 = FXMLDocumentController.tp4;
        endTurn = FXMLDocumentController.endTurn;
        roadButton = FXMLDocumentController.roadBtn;
        exitBtn = FXMLDocumentController.exitBtn;
        contributorsBtn = FXMLDocumentController.contributorsBtn;
        playerOpt1 = FXMLDocumentController.playerOpt1;
        playerOpt2 = FXMLDocumentController.playerOpt2;
        playerOpt3 = FXMLDocumentController.playerOpt3;
        harborOpt1 = FXMLDocumentController.harborOpt1;
        harborOpt2 = FXMLDocumentController.harborOpt2;
        bankTradeBtn = FXMLDocumentController.bankTradeBtn;
        
        //Popup Contributors
        Label l1 = new Label("Criado por:");
        Label l2 = new Label("Tiago Neveda:4481");
        Label l3 = new Label("Luis Carvalho:19565");
        Label l4 = new Label("José Sampaio:20734");
        Label l5 = new Label("João Sousa:20770");
        Label l6 = new Label("Bruno Ribeiro:21318");
        Label l7 = new Label("Duarte Dias:21883");
        Button closePopup1Button = new Button("Close");
        Button closePopup2Button = new Button("Close");
        
        Popup popup1 = new Popup();
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle(" -fx-background-color: white;");
        
        grid.setPadding(new Insets(25, 10, 25, 10));
        grid.add(l1, 1, 1);
        grid.add(l2, 1, 2);
        grid.add(l3, 1, 3);
        grid.add(l4, 1, 4);
        grid.add(l5, 1, 5);
        grid.add(l6, 1, 6);
        grid.add(l7, 1, 7);
        grid.add(closePopup1Button, 1, 8);
        
        popup1.getContent().add(grid); 
        
        //Popup in Players/Harbor/Bank
        Label empty = new Label(" ");
        Label r1 = new Label("Wood");
        Label r2 = new Label("Brick");
        Label r3 = new Label("Metal");
        Label r4 = new Label("Wheat");
        Label r5 = new Label("Wool");
        Label f0 = new Label("You");
        Label f1 = new Label("0");
        Label f2 = new Label("0");
        Label f3 = new Label("0");
        Label f4 = new Label("0");
        Label f5 = new Label("0");
        Label o0 = new Label("");
        Label t1 = new Label("0");
        Label t2 = new Label("0");
        Label t3 = new Label("0");
        Label t4 = new Label("0");
        Label t5 = new Label("0");
        Button f1btn = new Button("Wood");
        Button f2btn = new Button("Brick");
        Button f3btn = new Button("Metal");
        Button f4btn = new Button("Wheat");
        Button f5btn = new Button("Wool");
        Button t1btn = new Button("Wood");
        Button t2btn = new Button("Brick");
        Button t3btn = new Button("Metal");
        Button t4btn = new Button("Wheat");
        Button t5btn = new Button("Wool");
        Button sendButton = new Button("Send");
        
        Popup popup2 = new Popup();
        
        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.CENTER);
        grid2.setVgap(40);
        grid2.setHgap(40);
        grid2.setStyle(" -fx-background-color: white;");
        
        grid2.setPadding(new Insets(25, 10, 60, 40));
        grid2.add(r1, 2, 1);
        grid2.add(r2, 3, 1);
        grid2.add(r3, 4, 1);
        grid2.add(r4, 5, 1);
        grid2.add(r5, 6, 1);
        grid2.add(f0, 1, 2);
        grid2.add(f1, 2, 2);
        grid2.add(f2, 3, 2);
        grid2.add(f3, 4, 2);
        grid2.add(f4, 5, 2);
        grid2.add(f5, 6, 2);
        grid2.add(o0, 1, 3);
        grid2.add(t1, 2, 3);
        grid2.add(t2, 3, 3);
        grid2.add(t3, 4, 3);
        grid2.add(t4, 5, 3);
        grid2.add(t5, 6, 3);
        grid2.add(f1btn, 2, 4);
        grid2.add(f2btn, 3, 4);
        grid2.add(f3btn, 4, 4);
        grid2.add(f4btn, 5, 4);
        grid2.add(f5btn, 6, 4);
        grid2.add(t1btn, 2, 5);
        grid2.add(t2btn, 3, 5);
        grid2.add(t3btn, 4, 5);
        grid2.add(t4btn, 5, 5);
        grid2.add(t5btn, 6, 5);
        grid2.add(sendButton, 5, 6);
        grid2.add(closePopup2Button, 6, 6);
        grid2.add(empty, 7, 6);
        
        popup2.getContent().add(grid2); 
        
        EventHandler<ActionEvent> openContributors =  
        new EventHandler<ActionEvent>() { 
   
            public void handle(ActionEvent e) 
            { 
                
                if (!popup1.isShowing()) 
                    popup1.show(stage);  
            } 
        }; 
        
        EventHandler<ActionEvent> openTrades =  
        new EventHandler<ActionEvent>() { 
   
            public void handle(ActionEvent e) 
            { 
                o0.setText("Player");
                if (!popup2.isShowing()) 
                    if(popup1.isShowing())
                        popup1.hide();
                    popup2.show(stage);
            } 
        };
        
        
        EventHandler<ActionEvent> close =  
        new EventHandler<ActionEvent>() { 
   
            public void handle(ActionEvent e) 
            { 
                if (popup1.isShowing()) 
                    popup1.hide(); 
                if(popup2.isShowing())
                    popup2.hide(); 
            } 
        };
        
        EventHandler<ActionEvent> openHarbor =  
        new EventHandler<ActionEvent>() { 
   
            public void handle(ActionEvent e) 
            { 
                o0.setText("Harbor");
                if (!popup2.isShowing()) 
                    if(popup1.isShowing())
                        popup1.hide();
                    popup2.show(stage);
            } 
        };
        
        EventHandler<ActionEvent> openBank =  
        new EventHandler<ActionEvent>() { 
   
            public void handle(ActionEvent e) 
            { 
                o0.setText("Bank");
                if (!popup2.isShowing()) 
                    if(popup1.isShowing())
                        popup1.hide();
                    popup2.show(stage);
            } 
        };
        
        
        
        playerOpt1.setOnAction(openTrades);
        contributorsBtn.setOnAction(openContributors);
        harborOpt1.setOnAction(openHarbor);
        bankTradeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent m) {
                {
                    o0.setText("Bank");
                    if (!popup2.isShowing()) {
                        if (popup1.isShowing()) {
                            popup1.hide();
                        }
                    }
                    popup2.show(stage);
                }
            }
        });
        closePopup2Button.setOnAction(close);
        closePopup1Button.setOnAction(close);
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        connectClient();
        Player p1 = new Player(0, 1, 0, 0, 0, 0, 0, false, false);
        Player p2 = new Player(0, 2, 0, 0, 0, 0, 0, false, false);
        Player p3 = new Player(0, 3, 0, 0, 0, 0, 0, false, false);
        listPlayers.add(p1);
        listPlayers.add(p2);
        listPlayers.add(p3);

        Thread jogo = new Thread(() -> {

            while (!gameover) {

                for (i = 1; i <= listPlayers.size();) {
                    endPlay = false;
                    // possivel problema
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

                    roadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent m) {
                            for (Node n : FXMLDocumentController.linesGroup.getChildren()) {
                                if (idJogadorLocal == i) {
                                    System.out.println(n.getId() + "\n");
                                    n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent m) {
                                            if (idJogadorLocal == i) {
                                                System.out.println(n.getId());
                                                // player tem recursos?
                                                // rua disponivel?
                                                // 
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });

                    endTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent m) {
                            if (idJogadorLocal == i) {
                                System.out.println("Next player");
                                endPlay = true;
                                try {
                                    out.writeUTF("end Turn button pressed");
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });

                    if (endPlay) {
                        i++;
                    }

                    longestRoad();
                    biggestArmy();

                    if (isGameOver()) {
                        gameover = true;
                    }
                }
            }

        });

        jogo.start();

        gameover = false;

    }

    public static void main(String[] args) throws UnknownHostException, IOException {

        launch(args);
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

    private void connectClient() throws IOException {

        Socket socket = new Socket(serverIP, serverPort);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        Thread enviarMensagem = new Thread(() -> {
            while (true) {

                inputChat.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent k) {
                        if (k.getCode().equals(KeyCode.ENTER)) {
                            try {
                                out.writeUTF(inputChat.getText());
                                chat.appendText(inputChat.getText() + "\n");
                                inputChat.setText("");
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
                
                exitBtn.setOnAction(e ->{
                    e.consume();
                    Platform.exit();
            });
                

            }
        });

        Thread lerMensagem;
        lerMensagem = new Thread(() -> {
            while (true) {
                try {
                    String msg = in.readUTF();
                    if (msg.compareTo("#SetPlayer1") == 0) {
                        idJogadorLocal = 1;
                        tp1.setDisable(false);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                    } else if (msg.compareTo("#SetPlayer2") == 0) {
                        idJogadorLocal = 2;
                        tp1.setDisable(true);
                        tp2.setDisable(false);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                    } else if (msg.compareTo("#SetPlayer3") == 0) {
                        idJogadorLocal = 3;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(false);
                        tp4.setDisable(true);
                    } else if (msg.compareTo("#SetPlayer4") == 0) {
                        idJogadorLocal = 4;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(false);
                    } else {
                        System.out.println(msg);
                        chat.appendText(msg + "\n");
                    }
                } catch (IOException e) {

                }
            }
        });

        //lerMensagem.setDaemon(true);
        enviarMensagem.start();
        lerMensagem.start();

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

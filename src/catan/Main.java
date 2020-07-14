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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *  Classe onde o jogo é iniciado, e todas as acções realizadas
 *  pelo utilizador estão definidas
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
    public static Tab tp1, tp2, tp3, tp4;
    public static MenuItem tj1, tj2, tj3;

    
     /**
      * Método que inicia todas as componententes necessárias para a interface gráfica
      *
      * @param stage  Parametro que representa o conteúdo da interface
      * @throws Exception 
      */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        iniciarElementos();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        connectClient();

    }
        /**
         * Método Main, onde o jogo é jogado
         * 
         */
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
            /**
             *  Método que fornece os recursos ao jogador (cliente), que este ganha
             * @param resource  Parametro que representa os recursos
             * @param hex Parametro que representa uma casa de jogo
             */
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
     /**
      * Método que verifica se um jogo termina
      * @return retorna verdadeiro, no caso do jogo ter terminado. Caso não tenha terminado, retorna falso.
      *
      */
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
     
    /**
     * Método que indica se alguém (e quem) atingiu a estrada mais longa
     * Alguém só atinge a estrada mais longa, quem tem pelo menos 5 estradas,
     * ou, no caso de haver mais que um jogador com 5 estradas, mostra qual o jogador
     * com mais estradas
     */
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

    /**
     * Método que define as funcionalidades necessárias para que haja comunicação entre o cliente e o servidor
     * para que o jogo seja iniciado
     * @throws IOException 
     */
    private void connectClient() throws IOException {

        Socket socket = new Socket(serverIP, serverPort);

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

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

            }
        });
// este thread serve para o cliente receber mensagends do servidor e de as filtrar
        Thread lerMensagem;
        lerMensagem = new Thread(() -> {
            while (true) {
                try {
                    String msg = in.readUTF();
                    if (msg.compareTo("#SetPlayer1") == 0) {
                        tp1.setDisable(false);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 2"); 
                        tj2.setText("Jogador 3");
                        tj3.setText("Jogador 4");
                   } else if (msg.compareTo("#SetPlayer2") == 0) {
                        tp1.setDisable(true);
                        tp2.setDisable(false);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1"); 
                        tj2.setText("Jogador 3");
                        tj3.setText("Jogador 4");
                    } else if (msg.compareTo("#SetPlayer3") == 0) {
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(false);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1"); 
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 4");
                    } else if (msg.compareTo("#SetPlayer4") == 0) {
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(false);
                        tj1.setText("Jogador 1"); 
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 3");
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
     /**
      * Método que inicia os elementos da interface
      */
    private void iniciarElementos() {
        chat = FXMLDocumentController.chat;
        inputChat = FXMLDocumentController.inputChat;

        tp1 = FXMLDocumentController.tp1;
        tp2 = FXMLDocumentController.tp2;
        tp3 = FXMLDocumentController.tp3;
        tp4 = FXMLDocumentController.tp4;
        
        tj1 = FXMLDocumentController.tj1;
        tj2 = FXMLDocumentController.tj2;
        tj3 = FXMLDocumentController.tj3;

    }
      /**
       * Método que define quem tem o maior exército.
       */
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
     /**
      * Método que permite haver troca de recursos entre jogadores/clientes
      * @param p1 Parametro que representa o jogador que pretende efetuar a troca.
      * @param p2 Parametro que representa o jogador que recebe o pedido de troca.
      * @param resource1 Parametro que representa os recursos que o jogador pretende receber
      * @param resource2 Parametro que representa os recursos , que o jogador oferece em troca
      * @param quantity1 Parametro que representa as quantidades de cada recurso, que o jogador pretende receber
      * @param quantity2  Parametro que representa as quantidades de cada recurso, que o jogador oferece em troca.
      */
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

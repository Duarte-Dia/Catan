/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import catan.Board;
import catan.City;
import catan.Dice;
import catan.FXMLDocumentController;
import catan.Hexagon;
import catan.Main;
import catan.Player;
import catan.Settlement;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Utilizador
 */
public class Server {

    private static int port = 6666, nClientes = 1;
    private static Vector<ClientHandler> listaClientes = new Vector<>();
    private static Socket client;
    public static TextArea chat;
    private static TextField inputChat;
    static int idJogadorLocal = 1, i;
    static boolean gameover, endPlay;
    static Dice dice = new Dice();
    static List<Player> listPlayers = new ArrayList<Player>();
    public static Tab tp1, tp2, tp3, tp4;
    public static Button endTurn, roadButton;
    static DataInputStream in;
    static DataOutputStream out;
    static int chosenTile = 0;
    static Board board = new Board();

    public static void main(String[] args) throws IOException {
        
        
        
        
        ServerSocket server = new ServerSocket(port);
        Thread servidor = new Thread(() -> {
        while (true) {
            // Servidor fica a espera de um cliente
            if (nClientes <= 4) {
                try {
                    System.out.println("[SERVER]Esperando por ligação");
                    try {
                        client = server.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("[SERVER]Estabelecido ligação a um cliente");
                    
                    in = new DataInputStream(client.getInputStream());
                    out = new DataOutputStream(client.getOutputStream());
                    
                    ClientHandler ch = new ClientHandler("Cliente " + nClientes, in, out, client);
                    Thread t = new Thread(ch);
                    
                    System.out.println("[SERVER]Cliente " + nClientes + " adicionado ");
                    listaClientes.add(ch);
                    
                    try {
                        out.writeUTF("Sucesso a Conectar o Cliente " + nClientes + " \n");
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        out.writeUTF("#SetPlayer" + nClientes);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    t.start();
                    
                    nClientes++;
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        // fechar ligação
        /*
         client.close();
         System.out.println("[Server]A desligar");
         server.close();*/
        });
        
        servidor.start();

        chat = FXMLDocumentController.chat;
        inputChat = FXMLDocumentController.inputChat;

        tp1 = FXMLDocumentController.tp1;
        tp2 = FXMLDocumentController.tp2;
        tp3 = FXMLDocumentController.tp3;
        tp4 = FXMLDocumentController.tp4;
        endTurn = FXMLDocumentController.endTurn;
        roadButton = FXMLDocumentController.roadBtn;

        Player p1 = new Player(0, 1, 0, 0, 0, 0, 0, false, false);
        Player p2 = new Player(0, 2, 0, 0, 0, 0, 0, false, false);
        Player p3 = new Player(0, 3, 0, 0, 0, 0, 0, false, false);
        listPlayers.add(p1);
        listPlayers.add(p2);
        listPlayers.add(p3);

        
        
        
        
        
        
        
        gameover = false;
        
        Thread jogo;
        jogo = new Thread(() -> {
            
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
                    
                    
                    
                    
                    
                    
                    try{
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
                    } catch(Exception e ){
                   
                    }
                    
                    
                    
                    
                    try{
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
                    } catch(Exception e ){
                    
                    e = new NullPointerException();
                    }

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

        

        
    }

    private static class ClientHandler implements Runnable {

        private String name;
        final DataInputStream in;
        final DataOutputStream out;
        Socket socket;
        boolean logged;

        public ClientHandler(String name, DataInputStream in, DataOutputStream out, Socket s) {

            this.name = name;
            this.in = in;
            this.out = out;
            this.socket = s;
            logged = true;

        }

        @Override
        public void run() {
            String cmd;

            while (true) {

                try {
                    cmd = in.readUTF();
                    System.out.println(cmd);

                    StringTokenizer st = new StringTokenizer(cmd, "#");
                    String receivingClient = null;
                    try {
                        receivingClient = st.nextToken();
                    } catch (Exception e) {
                    };
                    String msg = null;
                    try {
                        msg = st.nextToken();
                    } catch (Exception e) {
                    };

                    if (msg != null) {

                        for (ClientHandler client : Server.listaClientes) {
                            if (client.name.equals(receivingClient) && client.logged) {
                                client.out.writeUTF("Whisper from " + name + ": " + msg);
                            }
                        }
                    } else {

                        for (ClientHandler client : Server.listaClientes) {
                            if (!client.name.equals(name) && client.logged) {
                                client.out.writeUTF(name + ": " + receivingClient);
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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
      * 
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

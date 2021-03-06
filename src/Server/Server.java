/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import catan.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe onde o servidor se encontra implementado e é iniciado, e o que ocorre numa sessão de jogo
 *
 * @author Duarte Dias
 */
public class Server {

    private static int port = 6666, nClientes = 1;
    private static Vector<ClientHandler> listaClientes = new Vector<>();
    private static Socket client;
    static int idJogadorLocal = 1, i;
    static boolean gameover, endPlay = false, sendResources = true, dadosLancados, firstPlay = true, secondPlay = false;
    static Dice dice = new Dice();
    static List<Player> listPlayers = new ArrayList<Player>();
    static DataInputStream in;
    static DataOutputStream out;
    static int chosenTile = 0;
    static Board board = new Board();

    public static void main(String[] args) throws IOException {

        dadosLancados = false;
        ServerSocket server = new ServerSocket(port);
         // thread que inicia o servidor 
        Thread servidor = new Thread(() -> {
            while (true) {
                // Servidor fica a espera de um cliente, .
                
                if (nClientes <= 4) {
                    try {
                        System.out.println("[SERVER]Esperando por ligação");

                        client = server.accept();

                        System.out.println("[SERVER]Estabelecido ligação a um cliente");

                        in = new DataInputStream(client.getInputStream());
                        out = new DataOutputStream(client.getOutputStream());

                        ClientHandler ch = new ClientHandler("Cliente " + nClientes, in, out, client);
                        Thread t = new Thread(ch);
                        //Os clientes são adicionados ao servidor
                        System.out.println("[SERVER]Cliente " + nClientes + " adicionado ");
                        listaClientes.add(ch);

                        out.writeUTF("Sucesso a Conectar o Cliente " + nClientes + " \n");
                        // define a posição do jogador
                        out.writeUTF("#SetPlayer" + nClientes);
                      
                        t.start();

                        nClientes++;

                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                     //Atingiu o número de 4 clientes e por isso o jogo é iniciado
                    gameover = false;
                    String resources = "###RESOURCES";
                    for (Player p : listPlayers) {
                        resources = resources.concat("@").concat(Integer.toString(p.getWool())).concat(" ");
                        resources = resources.concat(Integer.toString(p.getTimber())).concat(" ");
                        resources = resources.concat(Integer.toString(p.getBrick())).concat(" ");
                        resources = resources.concat(Integer.toString(p.getWheat())).concat(" ");
                        resources = resources.concat(Integer.toString(p.getMetal()));
                    }

                    if (sendResources) {
                        for (ClientHandler client : listaClientes) {
                            try {
                                client.out.writeUTF(resources);
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        sendResources = false;
                    }

                    if (firstPlay) {
                        for (ClientHandler client : listaClientes) {
                            try {
                                client.out.writeUTF("First Play");
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        firstPlay = false;
                    }

                    if (secondPlay) {
                        for (ClientHandler client : listaClientes) {
                            try {
                                client.out.writeUTF("Second Play");
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        secondPlay = false;
                    }
                }

                // for (Node n : FXMLDocumentController.linesGroup.getChildren()) {
                // }
                // fechar ligação
                /*
                 client.close();
                 System.out.println("[Server]A desligar");
                 server.close();*/
            }
        });
         //os jogadores são inicializados, com o mesmo numero de recursos)
        Player p1 = new Player(0, 1, 2, 4, 4, 2, 0, false, false);
        Player p2 = new Player(0, 2, 2, 4, 4, 2, 0, false, false);
        Player p3 = new Player(0, 3, 2, 4, 4, 2, 0, false, false);
        Player p4 = new Player(0, 4, 2, 4, 4, 2, 0, false, false);
        //Jogadores são adicionados à lista de jogadores
        listPlayers.add(p1);
        listPlayers.add(p2);
        listPlayers.add(p3);
        listPlayers.add(p4);
   
        servidor.start();
        // Thread que inicia o jogo
        Thread jogo;
        jogo = new Thread(() -> {

            while (!gameover) {
                     // Os dados são lançados. sempre que inicia a ronda de um dos 4 jogadores
                for (i = 1; i <= listPlayers.size();) {
                    if (!dadosLancados) {
                        chosenTile = dice.throwDice(2);
                        for (ClientHandler client : listaClientes) {
                            try {
                                client.out.writeUTF("Foi mandado um " + chosenTile);
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        System.out.println(chosenTile);
                        // são atribuidos recursos aos jogadores que tenham settlements/cities no Hexagono
                        // com o valor dado pelos dados, a não ser que calhe no deserto (7)
                        if (chosenTile != 7) {
                            for (Hexagon hex : board.getTiles()) {
                                if (chosenTile == hex.getNum()) {
                                    givePlayersResources(hex.getResourceID(), hex);
                                    sendResources = true;
                                }
                            }
                        } else {
                            // LANÇOU 7
                            // MOVE LADRAO
                        }
                        dadosLancados = true;
                    }
                    if (endPlay) {
                        dadosLancados = false;
                        i++;
                        endPlay = false;
                    }

                    longestRoad();
                    biggestArmy();

                }
                // fim do jogo
                if (isGameOver()) {
                    gameover = true;
                }
            }

        });

        jogo.start();

    }

    /**
     * Classe que permite controlar os clientes. Ela foi implementada aqui de
     * modo a ter acesso há lista de clientes do servidor
     */
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

        /**
         * Método inicializado, quando a thread é iniciada, e manipula as
         * solicitações realizadas pelo cliente
         */
        @Override
        public void run() {
            String cmd;

            while (true) {

                try {

                    cmd = in.readUTF();
                    System.out.println(cmd);

                    if (cmd.compareTo("2 turn") == 0) {
                        endPlay = true;
                        Server.listaClientes.get(1).out.writeUTF("Player2 turn");
                    } else if (cmd.compareTo("3 turn") == 0) {
                        endPlay = true;
                        Server.listaClientes.get(2).out.writeUTF("Player3 turn");
                    } else if (cmd.compareTo("4 turn") == 0) {
                        endPlay = true;
                        Server.listaClientes.get(3).out.writeUTF("Player4 turn");
                    } else if (cmd.compareTo("5 turn") == 0) {
                        endPlay = true;
                        Server.listaClientes.get(0).out.writeUTF("Player1 turn");

                    } else if (cmd.startsWith("Line")) {

                        String[] arraysOfString = cmd.split("@", 6);

                        for (ClientHandler client : Server.listaClientes) {
                            if (!client.name.equals(this.name)) {
                                client.out.writeUTF("Line @" + arraysOfString[1] + "@ styled @" + arraysOfString[3]);
                            } else {
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setTimber(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getTimber() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setBrick(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getBrick() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).addRoad(new Road());
                            }
                        }

                    } else if (cmd.startsWith("Vertice")) {

                        String[] arraysOfString = cmd.split("@", 6);
                        for (ClientHandler client : Server.listaClientes) {
                            if (!client.name.equals(this.name)) {
                                client.out.writeUTF("Vertice @" + arraysOfString[1] + "@ styled @" + arraysOfString[3]);
                            } else {
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setWool(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getWool() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setBrick(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getBrick() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setTimber(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getTimber() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setWheat(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getWheat() - 1);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).addSettlement(new Settlement(defineVertices(arraysOfString[1])));

                            }
                        }
                    } else if (cmd.startsWith("City")) {

                        String[] arraysOfString = cmd.split("@", 6);
                        for (ClientHandler client : Server.listaClientes) {
                            if (!client.name.equals(this.name)) {
                                client.out.writeUTF("City @" + arraysOfString[1] + "@ styled @" + arraysOfString[3]);
                            } else {
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setWheat(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getWheat() - 2);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).setMetal(
                                        listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).getMetal() - 3);
                                listPlayers.get(Integer.parseInt(arraysOfString[4]) - 1).addCity(new City(defineVertices(arraysOfString[1])));
                            }
                        }
                    } else if (cmd.compareTo("Second start") == 0) {
                        secondPlay = true;

                    } else {

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
                                // È enviado um Whisper (mensagem) a um jogador especifico
                            for (ClientHandler client : Server.listaClientes) {
                                if (client.name.equals(receivingClient) && client.logged) {
                                    client.out.writeUTF("Whisper from " + name + ": " + msg);
                                }
                            }
                            // Comando para trocar o turno para o  jogador seguinte
                        } else {

                            for (ClientHandler client : Server.listaClientes) {
                                if (!client.name.equals(name) && client.logged) {
                                    client.out.writeUTF(name + ": " + receivingClient);
                                }

                            }
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    /**
     * Método que fornece os recursos ao jogador (cliente), que este ganha
     *
     * @param resource Parametro que representa os recursos
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
                System.out.println(s.toString());
                if (hex.containVector(s.getPosition())) {
                    switch (resource) {
                        case 1: // wool
                            current = p.getWool();
                            p.setWool(current + 1);
                            break;
                        case 2: // timber
                            current = p.getTimber();
                            p.setTimber(current + 1);
                            break;
                        case 3: // brick
                            current = p.getBrick();
                            p.setBrick(current + 1);
                            break;
                        case 4: // wheat
                            current = p.getWheat();
                            p.setWheat(current + 1);
                            break;
                        case 5: // metal
                            current = p.getMetal();
                            p.setMetal(current + 1);
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
     *
     * @return retorna verdadeiro, no caso do jogo ter terminado. Caso não tenha
     * terminado, retorna falso.
     *
     */
    private static boolean isGameOver() {

        for (Player p : listPlayers) {
            // adiciona pontuação por cada Settlement
            p.addScore(p.getListSettlements().size());
            System.out.println(p.getId());
            // adiciona pontuação por cada City
            p.addScore(p.getListCities().size() * 2);

            /*
             if (p.isLongestRoad()) {
             p.addScore(2);
             }

             if (p.isBiggestArmy()) {
             p.addScore(2);
             }

             p.addScore(p.devCardsPoints());
             */
            // Determina que se algum jogador atinge 10 pontos, o jogo termina e esse jogador
            // é declarado vencedor.
            if (p.getScore() >= 10) {
                System.out.println("VENCEDOR É" + p.getId());
                System.out.println("Com ESTES PONTOS" + p.getScore());

                return true;
            }
        }

        return false;
    }

    /**
     * Método que indica se alguém (e quem) atingiu a estrada mais longa Alguém
     * só atinge a estrada mais longa, quem tem pelo menos 5 estradas, ou, no
     * caso de haver mais que um jogador com 5 estradas, mostra qual o jogador
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
         * Método que define as coordenadas de um vértice
         * @param s Parametro que representa a string de um vetor (v1, v2... até v54
         * @return retorna um dos vértices
         */
    public static Vector3 defineVertices(String s) {
        switch (s) {
            case "v1":
                return new Vector3(0, 1, 0);
            case "v2":
                return new Vector3(1, 2, 0);
            case "v3":
                return new Vector3(2, 3, 0);
            case "v4":
                return new Vector3(0, 0, 0);
            case "v5":
                return new Vector3(1, 1, 0);
            case "v6":
                return new Vector3(2, 2, 0);
            case "v7":
                return new Vector3(3, 3, 0);
            case "v8":
                return new Vector3(0, 0, 1);
            case "v9":
                return new Vector3(1, 1, 1);
            case "v10":
                return new Vector3(2, 2, 1);
            case "v11":
                return new Vector3(3, 3, 1);
            case "v12":
                return new Vector3(0, -1, 1);
            case "v13":
                return new Vector3(1, 0, 1);
            case "v14":
                return new Vector3(2, 1, 1);
            case "v15":
                return new Vector3(3, 2, 1);
            case "v16":
                return new Vector3(4, 3, 1);
            case "v17":
                return new Vector3(0, -1, 2);
            case "v18":
                return new Vector3(1, 0, 2);
            case "v19":
                return new Vector3(2, 1, 2);
            case "v20":
                return new Vector3(3, 2, 2);
            case "v21":
                return new Vector3(4, 3, 2);
            case "v22":
                return new Vector3(0, -2, 2);
            case "v23":
                return new Vector3(1, -1, 2);
            case "v24":
                return new Vector3(2, 0, 2);
            case "v25":
                return new Vector3(3, 1, 2);
            case "v26":
                return new Vector3(4, 2, 2);
            case "v27":
                return new Vector3(5, 3, 2);
            case "v28":
                return new Vector3(0, -2, 3);
            case "v29":
                return new Vector3(1, -1, 3);
            case "v30":
                return new Vector3(2, 0, 3);
            case "v31":
                return new Vector3(3, 1, 3);
            case "v32":
                return new Vector3(4, 2, 3);
            case "v33":
                return new Vector3(5, 3, 3);
            case "v34":
                return new Vector3(1, -2, 3);
            case "v35":
                return new Vector3(2, -1, 3);
            case "v36":
                return new Vector3(3, 0, 3);
            case "v37":
                return new Vector3(4, 1, 3);
            case "v38":
                return new Vector3(5, 2, 3);
            case "v39":
                return new Vector3(1, -2, 4);
            case "v40":
                return new Vector3(2, -1, 4);
            case "v41":
                return new Vector3(3, 0, 4);
            case "v42":
                return new Vector3(4, 1, 4);
            case "v43":
                return new Vector3(5, 2, 4);
            case "v44":
                return new Vector3(2, -2, 4);
            case "v45":
                return new Vector3(3, -1, 4);
            case "v46":
                return new Vector3(4, 0, 4);
            case "v47":
                return new Vector3(5, 1, 4);
            case "v48":
                return new Vector3(2, -2, 5);
            case "v49":
                return new Vector3(3, -1, 5);
            case "v50":
                return new Vector3(4, 0, 5);
            case "v51":
                return new Vector3(5, 1, 5);
            case "v52":
                return new Vector3(3, -2, 5);
            case "v53":
                return new Vector3(4, -1, 5);
            case "v54":
                return new Vector3(5, 0, 5);
            default:
                return new Vector3();
        }
    }

    /**
     * Método que permite haver troca de recursos entre jogadores/clientes
     *
     * @param p1 Parametro que representa o jogador que pretende efetuar a
     * troca.
     * @param p2 Parametro que representa o jogador que recebe o pedido de
     * troca.
     * @param resource1 Parametro que representa os recursos que o jogador
     * pretende receber
     * @param resource2 Parametro que representa os recursos , que o jogador
     * oferece em troca
     * @param quantity1 Parametro que representa as quantidades de cada recurso,
     * que o jogador pretende receber
     * @param quantity2 Parametro que representa as quantidades de cada recurso,
     * que o jogador oferece em troca.
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

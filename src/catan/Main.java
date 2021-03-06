/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Classe onde o jogo é iniciado, e todas as acções do cliente estão definidas
 *
 * @author Bruno Ribeiro
 * @author Duarte Dias
 */
public class Main extends Application {

    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 6666;
    public static TextArea chat;
    private static TextField inputChat;
    public static Tab tp1, tp2, tp3, tp4;
    public static MenuItem tj1, tj2, tj3;
    static boolean gameover, endPlay, startGame, secondPlay = false, firstRounds = false;
    public static Button endTurn, roadButton, settleButton, cityButton, devButton;
    static DataInputStream in;
    static DataOutputStream out;
    //NOTA DE DUARTE.... ESTE I SERVE PARA INDICAR O INFERNO 
    int idJogadorLocal, i = 1, counter = 0;
    String color;
    boolean vertices, edges;
    String[] resources = {"0", "0", "0", "0", "0"};
    VBox v = new VBox();

    /**
     * Método que inicia todas as componententes necessárias para a interface
     * gráfica
     *
     * @param stage Parametro que representa o conteúdo da interface
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        connectClient();
        iniciarElementos();
        vertices = true;
        edges = true;
        buyRoad();
        buySettle();

    }

    /**
     * Método Main
     *
     * @throws UnknownHostException
     * @throws IOException
     */
    public static void main(String[] args) throws UnknownHostException, IOException {

        launch(args);

    }

    /**
     * Método que define as funcionalidades necessárias para que haja
     * comunicação entre o cliente e o servidor para que o jogo seja iniciado
     *
     * @throws IOException
     */
    private void connectClient() throws IOException {

        Socket socket = new Socket(serverIP, serverPort);

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
  // Thread que serve para o cliente envia mensagens para o servidor
        Thread enviarMensagem = new Thread(() -> {

            while (true) {
                  // quando o jogador pressiona em Enter, após adicionar uma mensagem na caixa de texto
                  // esta é enviada para a caixa de chat
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
                        idJogadorLocal = 1;
                        tp1.setDisable(false);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 2");
                        tj2.setText("Jogador 3");
                        tj3.setText("Jogador 4");
                        color = "red";
                    } else if (msg.compareTo("#SetPlayer2") == 0) {
                        idJogadorLocal = 2;
                        tp1.setDisable(true);
                        tp2.setDisable(false);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 3");
                        tj3.setText("Jogador 4");
                        color = "#CCDA0A";
                    } else if (msg.compareTo("#SetPlayer3") == 0) {
                        idJogadorLocal = 3;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(false);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 4");
                        color = "#00FF37";
                    } else if (msg.compareTo("#SetPlayer4") == 0) {
                        idJogadorLocal = 4;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(false);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 3");
                        color = "#00EAFA";
                    } else if (msg.contains("###RESOURCES")) {
                        System.out.println(msg);
                        v = new VBox();
                        switch (idJogadorLocal) {
                            case 1:
                                v = FXMLDocumentController.p1group;
                                break;
                            case 2:
                                v = FXMLDocumentController.p2group;
                                break;
                            case 3:
                                v = FXMLDocumentController.p3group;
                                break;
                            case 4:
                                v = FXMLDocumentController.p4group;
                                break;
                        }
                        String s = msg.split("@", 5)[idJogadorLocal];
                        int j = 0;
                        resources = s.split(" ", 5);
                        for (Node t : v.getChildren()) {
                            if (t instanceof Text) {
                                ((Text) t).setText(resources[j]);
                                j++;
                            }
                        }
                        System.out.println(s);
                    } // receber comando do servidor para atualizar os tabuleiros dos jogadores
                    else if (msg.startsWith("Line")) {
                        String[] arraysOfString = msg.split("@", 4);

                        for (Node n : FXMLDocumentController.linesGroup.getChildren()) {

                            if (n.getId().compareTo(arraysOfString[1]) == 0) {
                                n.setStyle(arraysOfString[3]);
                            }
                        }

                    } else if (msg.startsWith("Vertice")) {
                        String[] arraysOfString = msg.split("@", 4);

                        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {
                            if (n.getId().compareTo(arraysOfString[1]) == 0) {
                                n.setStyle(arraysOfString[3]);
                            }
                        }

                    } else if (msg.startsWith("City")) {
                        String[] arraysOfString = msg.split("@", 4);

                        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {

                            if (n.getId().compareTo(arraysOfString[1]) == 0) {
                                n.setStyle(arraysOfString[3]);
                            }
                        }

                    }// receber comandos do servidor para iniciar as trocas de recursos
                    /*
                     else if (msg.startsWith("Trade")) {
                     String[] arraysOfString = msg.split("@", 4);

                     }*/ // receber comando de servidor para ativar o turno
                    else if (msg.compareTo("Player1 turn") == 0) {
                        i = 1;
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN" + counter + "!!\n");
                        counter++;
                    } else if (msg.compareTo("Player2 turn") == 0) {
                        i = 2;
                        if (startGame == true) {
                            buyRoad();
                            buySettle();
                        }
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN" + counter + "!!\n");
                        counter++;
                    } else if (msg.compareTo("Player3 turn") == 0) {
                        i = 3;
                        if (startGame == true) {
                            buyRoad();
                            buySettle();
                        }
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN" + counter + "!!\n");
                        counter++;
                    } else if (msg.compareTo("Player4 turn") == 0) {
                        i = 4;
                        if (startGame == true) {
                            buyRoad();
                            buySettle();
                        }
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN" + counter + "!!\n");
                        counter++;
                    } else if (msg.compareTo("First Play") == 0) {
                        buyRoad();
                        buySettle();
                        startGame = true;

                    } else if (msg.compareTo("Second Play") == 0) {
                        i = 4;
                        edges = true;
                        vertices = true;
                        buyRoad();
                        buySettle();
                        startGame = false;
                        secondPlay = true;

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
        endTurn = FXMLDocumentController.endTurn;
        roadButton = FXMLDocumentController.roadBtn;
        settleButton = FXMLDocumentController.settleBtn;
        cityButton = FXMLDocumentController.cityBtn;

        tj1 = FXMLDocumentController.tj1;
        tj2 = FXMLDocumentController.tj2;
        tj3 = FXMLDocumentController.tj3;

        Thread buttonListener = new Thread(() -> {

            cityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent m) {
                    vertices = true;
                    buyCity();
                }
            });

            settleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent m) {
                    vertices = true;
                    buySettle();
                }
            });

            roadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent m) {
                    edges = true;
                    buyRoad();
                }
            });

            endTurn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent m) {
                    if (idJogadorLocal == i) {
                        System.out.println("Next player");
                        endPlay = true;
                        try {
                            if (!firstRounds) {
                                if (!secondPlay && i == 4) {
                                    out.writeUTF(4 + " turn");
                                    out.writeUTF("Second start");
                                    secondPlay = true;
                                }
                                if (startGame == false) {
                                    if (i == 2) {
                                        firstRounds = true;
                                        out.writeUTF(5 + " turn");
                                    } else if (i == 1) {
                                        firstRounds = true;
                                        out.writeUTF(5 + " turn");
                                        secondPlay = false;
                                    } else {
                                        firstRounds = true;
                                        out.writeUTF(i - 1 + " turn");
                                    }
                                    i--;
                                } else {
                                    out.writeUTF(i + 1 + " turn");
                                    i++;
                                }
                            } else {
                                out.writeUTF(i + 1 + " turn");
                                i++;
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        });

        buttonListener.start();
    }
   /**
    * Método que permite que um jogador compre uma City
    */
    public void buyCity() {
        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i && Integer.parseInt(resources[3]) >= 2 && Integer.parseInt(resources[4]) >= 3) {
                            //chat.appendText(n.getId());
                            if (!n.getStyle().contains("-fx-stroke-type: outside; -fx-stroke-width: 3") && n.getStyle().contains("-fx-stroke: " + color + "; -fx-fill: " + color + ";") && vertices) {
                                n.setStyle("-fx-stroke: " + color + "; -fx-fill: " + color + "; -fx-stroke-type: outside; -fx-stroke-width: 3");
                                vertices = false;
                                try {
                                    out.writeUTF("City @" + n.getId() + "@ styled @" + n.getStyle());
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            // !!!!!!!!!!! player tem recursos - 2 wheat, 3 metal !!!!!!!!!!!!!!!!
                        }
                    }
                });
            }
        }
    }
    /**
    * Método que permite que um jogador compre um Settlement
    */
    public void buySettle() {
        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i && Integer.parseInt(resources[0]) >= 1 && Integer.parseInt(resources[1]) >= 1 && Integer.parseInt(resources[2]) >= 1 && Integer.parseInt(resources[2]) >= 1) {
                            //chat.appendText(n.getId());
                            if ((!n.getStyle().contains("-fx-stroke:") || n.getStyle().contains("-fx-stroke: black")) && vertices) {
                                n.setStyle("-fx-stroke: " + color + "; -fx-fill: " + color + ";");
                                resources[0] = Integer.toString(Integer.parseInt(resources[0]) - 1);
                                resources[1] = Integer.toString(Integer.parseInt(resources[1]) - 1);
                                resources[2] = Integer.toString(Integer.parseInt(resources[2]) - 1);
                                resources[3] = Integer.toString(Integer.parseInt(resources[3]) - 1);
                                int j = 0;
                                for (Node t : v.getChildren()) {
                                    if (t instanceof Text) {
                                        ((Text) t).setText(resources[j]);
                                        j++;
                                    }
                                }
                                vertices = false;
                                try {
                                    out.writeUTF("Vertice @" + n.getId() + "@ styled @" + n.getStyle() + "@" + i);
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            // !!!!!!!!!!! player tem recursos - 1 wool, 1 wheat, 1 timber, 1 brick !!!!!!!!!!!!!!!!
                        }
                    }
                });
            }
        }
    }
   /**
    * Método que permite que um jogador compre uma Road
    */
    public void buyRoad() {
        for (Node n : FXMLDocumentController.linesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i && Integer.parseInt(resources[1]) >= 1 && Integer.parseInt(resources[2]) >= 1) {
                            //chat.appendText(n.getId());
                            if ((!n.getStyle().contains("-fx-stroke:") || n.getStyle().contains("-fx-stroke: black")) && edges) {
                                n.setStyle("-fx-stroke: " + color + ";");
                                resources[1] = Integer.toString(Integer.parseInt(resources[1]) - 1);
                                resources[2] = Integer.toString(Integer.parseInt(resources[2]) - 1);
                                int j = 0;
                                for (Node t : v.getChildren()) {
                                    if (t instanceof Text) {
                                        ((Text) t).setText(resources[j]);
                                        j++;
                                    }
                                }
                                edges = false;
                                try {
                                    out.writeUTF("Line @" + n.getId() + "@ styled @" + n.getStyle() + "@" + i);
                                } catch (IOException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            // !!!!!!!!!!! player tem recursos - 1 timber, 1 brick !!!!!!!!!!!!!!!!
                        }
                    }
                });
            }
        }
    }

}

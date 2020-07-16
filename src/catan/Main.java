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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * Classe onde o jogo é iniciado, e todas as acções realizadas pelo utilizador
 * estão definidas
 *
 * @author Bruno Ribeiro
 */
public class Main extends Application {

    private static String serverIP = "127.0.0.1";
    private static final int serverPort = 6666;
    public static TextArea chat;
    private static TextField inputChat;
    public static Tab tp1, tp2, tp3, tp4;
    public static MenuItem tj1, tj2, tj3;
    static boolean gameover, endPlay, startServer = true;
    public static Button endTurn, roadButton, settleButton, cityButton, devButton, bankTradeBtn;
    static DataInputStream in;
    static DataOutputStream out;
    public static MenuItem exitBtn, contributorsBtn, playerOpt1, playerOpt2, playerOpt3, harborOpt1, harborOpt2, harborOpt3;
    //NOTA DE DUARTE.... ESTE I SERVE PARA INDICAR O INFERNO 
    int idJogadorLocal, i = 1;
    String color;
    boolean vertices, edges;

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
        iniciarElementos(stage);
        vertices = true;
        edges = true;
        buyRoad();
        buySettle();

    }

    /**
     * Método Main, onde o jogo é jogado
     *
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

                exitBtn.setOnAction(e -> {
                    e.consume();
                    Platform.exit();
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
                        color = "yellow";
                    } else if (msg.compareTo("#SetPlayer3") == 0) {
                        idJogadorLocal = 3;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(false);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 4");
                        color = "green";
                    } else if (msg.compareTo("#SetPlayer4") == 0) {
                        idJogadorLocal = 4;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(true);
                        tp4.setDisable(false);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 3");
                        color = "blue";
                    } else if (msg.contains("###RESOURCES")) {
                        System.out.println(msg);
                        VBox v = new VBox();
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
                        String[] resources = s.split(" ", 5);
                        for (Node t : v.getChildren()) {
                            if (t instanceof Text) {
                                ((Text) t).setText(resources[j]);
                                j++;
                            }
                        }
                        System.out.println(s);
                    } // receber comando de servidor para ativar o turno
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

                    }// receber comando de servidor para ativar o turno
                    else if (msg.compareTo("Player1 turn") == 0) {
                        i = 1;
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                    } else if (msg.compareTo("Player2 turn") == 0) {
                        i = 2;
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                    } else if (msg.compareTo("Player3 turn") == 0) {
                        i = 3;
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                    } else if (msg.compareTo("Player4 turn") == 0) {
                        i = 4;
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                        chat.appendText("PLAYER" + i + " ITS YOUR TURN!!\n");
                    } else if (msg.compareTo("First Play") == 0) {
                        buyRoad();
                        buySettle();
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
    private void iniciarElementos(Stage stage) {

        exitBtn = FXMLDocumentController.exitBtn;
        contributorsBtn = FXMLDocumentController.contributorsBtn;
        playerOpt1 = FXMLDocumentController.playerOpt1;
        playerOpt2 = FXMLDocumentController.playerOpt2;
        playerOpt3 = FXMLDocumentController.playerOpt3;
        harborOpt1 = FXMLDocumentController.harborOpt1;
        harborOpt2 = FXMLDocumentController.harborOpt2;
        bankTradeBtn = FXMLDocumentController.bankTradeBtn;

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

        EventHandler<ActionEvent> openContributors
                = new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e) {

                        if (!popup1.isShowing()) {
                            popup1.show(stage);
                        }
                    }
                };

        EventHandler<ActionEvent> openTrades
                = new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e) {
                        o0.setText("Player");
                        if (!popup2.isShowing()) {
                            if (popup1.isShowing()) {
                                popup1.hide();
                            }
                        }
                        popup2.show(stage);
                    }
                };

        EventHandler<ActionEvent> close
                = new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e) {
                        if (popup1.isShowing()) {
                            popup1.hide();
                        }
                        if (popup2.isShowing()) {
                            popup2.hide();
                        }
                    }
                };

        EventHandler<ActionEvent> openHarbor
                = new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e) {
                        o0.setText("Harbor");
                        if (!popup2.isShowing()) {
                            if (popup1.isShowing()) {
                                popup1.hide();
                            }
                        }
                        popup2.show(stage);
                    }
                };

        EventHandler<ActionEvent> openBank
                = new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e) {
                        o0.setText("Bank");
                        if (!popup2.isShowing()) {
                            if (popup1.isShowing()) {
                                popup1.hide();
                            }
                        }
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
                            out.writeUTF(i + 1 + " turn");
                            i++;
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

        });

        buttonListener.start();
    }

    public void buyCity() {
        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i) {
                            //chat.appendText(n.getId());
                            if (!n.getStyle().contains("-fx-stroke-type: outside; -fx-stroke-width: 3") && n.getStyle().contains("-fx-stroke: " + color + "; -fx-fill: " + color + ";") && vertices) {
                                n.setStyle("-fx-stroke: " + color + "; -fx-fill: " + color + "; -fx-stroke-type: outside; -fx-stroke-width: 3");
                                vertices = false;
                            }
                            // !!!!!!!!!!! player tem recursos - 1 wool, 1 wheat, 1 timber, 1 brick !!!!!!!!!!!!!!!!
                        }
                    }
                });
            }
        }
    }

    public void buySettle() {
        for (Node n : FXMLDocumentController.verticesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i) {
                            //chat.appendText(n.getId());
                            if ((!n.getStyle().contains("-fx-stroke:") || n.getStyle().contains("-fx-stroke: black")) && vertices) {
                                n.setStyle("-fx-stroke: " + color + "; -fx-fill: " + color + ";");
                                vertices = false;
                                try {
                                    out.writeUTF("Vertice @" + n.getId() + "@ styled @" + n.getStyle());
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

    public void buyRoad() {
        for (Node n : FXMLDocumentController.linesGroup.getChildren()) {
            if (idJogadorLocal == i) {
                //System.out.println(n.getId() + "\n");
                n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent m) {
                        if (idJogadorLocal == i) {
                            //chat.appendText(n.getId());
                            if ((!n.getStyle().contains("-fx-stroke:") || n.getStyle().contains("-fx-stroke: black")) && edges) {
                                n.setStyle("-fx-stroke: " + color + ";");
                                edges = false;
                                try {
                                    out.writeUTF("Line @" + n.getId() + "@ styled @" + n.getStyle());
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

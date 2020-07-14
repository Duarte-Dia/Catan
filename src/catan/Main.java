/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import static Server.Server.chat;
import static Server.Server.tp1;
import static Server.Server.tp2;
import static Server.Server.tp3;
import static Server.Server.tp4;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.shape.Line;
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
    static boolean gameover, endPlay;
    public static Button endTurn, roadButton;
    static DataInputStream in;
    static DataOutputStream out;
    int idJogadorLocal = 1, i;

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
                    } else if (msg.compareTo("#SetPlayer2") == 0) {
                        idJogadorLocal = 2;
                        tp1.setDisable(true);
                        tp2.setDisable(false);
                        tp3.setDisable(true);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 3");
                        tj3.setText("Jogador 4");
                    } else if (msg.compareTo("#SetPlayer3") == 0) {
                        idJogadorLocal = 3;
                        tp1.setDisable(true);
                        tp2.setDisable(true);
                        tp3.setDisable(false);
                        tp4.setDisable(true);
                        tj1.setText("Jogador 1");
                        tj2.setText("Jogador 2");
                        tj3.setText("Jogador 4");
                    }  else if (msg.compareTo("#SetPlayer4") == 0) {
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
        endTurn = FXMLDocumentController.endTurn;
        roadButton = FXMLDocumentController.roadBtn;

        tj1 = FXMLDocumentController.tj1;
        tj2 = FXMLDocumentController.tj2;
        tj3 = FXMLDocumentController.tj3;

        Thread buttonListener = new Thread(() -> {

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

        });
        
        buttonListener.start();

    }

}

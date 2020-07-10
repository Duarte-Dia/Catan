/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Bruno Ribeiro
 */
public class Main extends Application {
    
    static int chosenTile = 0;
    static boolean gameover;
    static Dice dice = new Dice();
    static List<Player> listPlayers = new ArrayList<Player>();
    static Board board = new Board();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        gameover = true;
        
        while(!gameover){
            for(int i = 0; i < listPlayers.size(); i++){
                chosenTile = dice.throwDice(2);
                
                for(Hexagon hex : board.getTiles()){
                    if(chosenTile == hex.getNum()){
                        givePlayersResources(hex.getResource());
                    }
                }
            }
        }
    }
    
    private static void givePlayersResources(String Resource){
    }
    
}

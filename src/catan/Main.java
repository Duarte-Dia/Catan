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
    static boolean gameover, endPlay;
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
        int current;
        for (Player p : listPlayers) {
            for (City c : p.getListCities()) {
                if (hex.containVector(c.getPosition())) {
                    switch (resource) {
                        case 1: // wool
                            current = p.getWool();
                            p.setWool(current + resource);
                            break;
                        case 2: // timber
                            current = p.getTimber();
                            p.setTimber(current + resource);
                            break;
                        case 3: // brick
                            current = p.getBrick();
                            p.setBrick(current + resource);
                            break;
                        case 4: // wheat
                            current = p.getWheat();
                            p.setWheat(current + resource);
                            break;
                        case 5: // metal
                            current = p.getMetal();
                            p.setMetal(current + resource);
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
                            p.setWool(current + resource);
                            break;
                        case 2: // timber
                            current = p.getTimber();
                            p.setTimber(current + resource);
                            break;
                        case 3: // brick
                            current = p.getBrick();
                            p.setBrick(current + resource);
                            break;
                        case 4: // wheat
                            current = p.getWheat();
                            p.setWheat(current + resource);
                            break;
                        case 5: // metal
                            current = p.getMetal();
                            p.setMetal(current + resource);
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

}

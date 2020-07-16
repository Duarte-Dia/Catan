/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;

/**
 *
 * @author José Sampaio Criaçao da Classe
 */
public class Board {

    private List<Hexagon> tiles = new ArrayList<Hexagon>();
    Robber thief = new Robber(7);

    public Board() {
        //this.tiles = tiles;
        /*for(int i = 0; i < 19; i++){
            new newHex = new Hexagon(i, );
            tiles.add();
        }*/
        Hexagon newHex01 = new Hexagon(0, 11, 2);
        tiles.add(newHex01);

        Hexagon newHex02 = new Hexagon(1, 12, 1);
        tiles.add(newHex02);

        Hexagon newHex03 = new Hexagon(2, 9, 4);
        tiles.add(newHex03);

        Hexagon newHex04 = new Hexagon(3, 4, 3);
        tiles.add(newHex04);

        Hexagon newHex05 = new Hexagon(4, 6, 5);
        tiles.add(newHex05);

        Hexagon newHex06 = new Hexagon(5, 5, 3);
        tiles.add(newHex06);

        Hexagon newHex07 = new Hexagon(6, 10, 1);
        tiles.add(newHex07);

        Hexagon newHex08 = new Hexagon(7, 7, 0);
        tiles.add(newHex08);

        Hexagon newHex09 = new Hexagon(8, 3, 2);
        tiles.add(newHex09);

        Hexagon newHex10 = new Hexagon(9, 11, 4);
        tiles.add(newHex10);

        Hexagon newHex11 = new Hexagon(10, 4, 2);
        tiles.add(newHex11);

        Hexagon newHex12 = new Hexagon(11, 8, 4);
        tiles.add(newHex12);

        Hexagon newHex13 = new Hexagon(12, 8, 3);
        tiles.add(newHex13);

        Hexagon newHex14 = new Hexagon(13, 10, 1);
        tiles.add(newHex14);

        Hexagon newHex15 = new Hexagon(14, 9, 1);
        tiles.add(newHex15);

        Hexagon newHex16 = new Hexagon(15, 3, 5);
        tiles.add(newHex16);

        Hexagon newHex17 = new Hexagon(16, 5, 5);
        tiles.add(newHex17);

        Hexagon newHex18 = new Hexagon(17, 2, 3);
        tiles.add(newHex18);

        Hexagon newHex19 = new Hexagon(18, 6, 2);
        tiles.add(newHex19);
        
        
    }

    public List<Hexagon> getTiles() {
        return tiles;
    }

    public void setTiles(List<Hexagon> tiles) {
        this.tiles = tiles;
    }
    
    public void isRobbed(int dieValue, int endTile){
        if(dieValue == 7){
            //Moves thief to chosen tile
            thief.setTileId(endTile);
        }
    }

}

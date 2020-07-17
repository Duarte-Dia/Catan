/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *  Classe onde o tabuleiro é definido
 * @author José Sampaio Criaçao da Classe
 */
public class Board {

    private List<Hexagon> tiles = new ArrayList<Hexagon>();
    private Object parser;

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

        Hexagon newHex08 = new Hexagon(9, 11, 4);
        tiles.add(newHex08);

        Hexagon newHex09 = new Hexagon(8, 3, 2);
        tiles.add(newHex09);

        Hexagon newHex10 = new Hexagon(7, 7, 0);
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

        defineHexagonVectors();
    }

    public List<Hexagon> getTiles() {
        return tiles;
    }

    public void setTiles(List<Hexagon> tiles) {
        this.tiles = tiles;
    }
    /**
     * Método que define os Vetores dos Hexagonos
     */
    public void defineHexagonVectors() {
        JSONParser parser = new JSONParser();

        try {
            String p = Board.class.getResource("Hexagons.json").getPath();
            p = p.substring(6, p.length() - 35).concat("src/catan/Hexagons.json");
            System.out.println(p);
            Object obj = parser.parse(new FileReader(p));

            JSONObject jsonObj = (JSONObject) obj;
            JSONObject hexs = (JSONObject) jsonObj.get("hexagons");

            for (int i = 1; i < 20; i++) {
                JSONArray array = (JSONArray) hexs.get(Integer.toString(i));
                for (int j = 0; j < 6; j++) {
                    JSONArray vectors = (JSONArray) array.get(j);
                    tiles.get(i - 1).addVectors(new Vector3(Integer.parseInt(vectors.get(0).toString()),
                            Integer.parseInt(vectors.get(1).toString()),
                            Integer.parseInt(vectors.get(2).toString())));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

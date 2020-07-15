/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

/**
 *
 * @author José Sampaio
 * Criaçao da Classe
 */

/*
    Card types:
    0 - Ore
    1 - Grain
    2 - Lumber
    3 - Wool
    4 - Brick

    5 - Knight/Soldier
    ***PROGRESS***
    6 - Monopoly
    7 - Road Building
    8 - Year of Plenty
    ***VICTORY POINTS***
    9 - Palace
    10 - Library
    11 - Market
    12 - Chapel
    13 - University
    
    14 - Longest Road
    15 - Largest Army
*/
public class Card {
    private int id, resourceType;
    private String info;
    
    public Card(int id, int resourceType, String info){
        this.id = id;
        this.resourceType = resourceType;
        this.info = info;
    }
    
    public int getResourceType(){
        return resourceType;
    }
    
    public void setResourceType(int Resource){
        this.resourceType = resourceType;
    }
    
    public String getInfo(){
        return info;
    }
    
    public void setInfo(String info){
        this.info = info;
    }   
}

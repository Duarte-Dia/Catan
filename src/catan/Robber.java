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
public class Robber {
    private int tileId;
    
    public Robber(int tileId){
        this.tileId = tileId;
    }
    
    public int getTileId(){
        return tileId;
    }
    
    public void setTileId(int newTileId){
        this.tileId = newTileId;
    }
}

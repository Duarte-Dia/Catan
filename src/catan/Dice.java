/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

/**
 *
 * @author José Sampaio
 * Criação da Classe, Novos metodos(throwDice(), diceValue())
 */
public class Dice {
    
    
    private int throwDice(){
        return ((int)Math.random()*5)+1;
    }
    
    public int diceValue(){
        return throwDice() + throwDice();
    }
}

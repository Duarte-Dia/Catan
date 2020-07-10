/**
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
    
    int total = 0;
    
    private int throwDice(int n){
        
        while(n > 0){
            total += ((int)Math.random()*5)+1;
            n--;
        }
        
        return total;
    }
}

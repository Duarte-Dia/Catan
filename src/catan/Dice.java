/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

/**
 *  Classe onde o dado é definido e lançado
 * @author José Sampaio
 * Criação da Classe, Novos metodos(throwDice(), diceValue())
 */
public class Dice {
    
    int total = 0;
    /**
     * Método para o lançamento dos dados
     * @param n 
     * @return  retorna a soma dos valores dos dados, após oseu lançamento
     */
    public int throwDice(int n){
        
        while(n > 0){
            total += ((int)Math.random()*5)+1;
            n--;
        }
        
        return total;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;

/**
 *
 * @author José Sampaio
 * Criaçao da Classe
 */
public class Deck {
    private List<Card> deckOfCards = new ArrayList<Card>();
    private List<Card> specialCards = new ArrayList<Card>();
    
    public Deck(){
        //For cycle to create and add ore cards to the deck
        for(int i = 0; i < 19; i++){
            Card newCard = new Card(i, 0, "Ore");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add grain cards to the deck
        for(int i = 19; i < 38; i++){
            Card newCard = new Card(i, 1, "Grain");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add lumber cards to the deck
        for(int i = 38; i < 57; i++){
            Card newCard = new Card(i, 2, "Lumber");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add wool cards to the deck
        for(int i = 57; i < 76; i++){
            Card newCard = new Card(i, 3, "Wool");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add brick cards to the deck
        for(int i = 76; i < 95; i++){
            Card newCard = new Card(i, 4, "Brick");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add soldier cards to the deck
        for(int i = 95; i < 109; i++){
            Card newCard = new Card(i, 5, "Soldier");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add progress(monopoly) cards to the deck
        for(int i = 109; i < 111; i++){
            Card newCard = new Card(i, 6, "When you play this card announce 1 type of"
                    + "resource. All other players must give you all their resources of that type.");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add progress(road building) cards to the deck
        for(int i = 111; i < 113; i++){
            Card newCard = new Card(i, 7, "Place 2 new roads(or ships) as if you had just built them");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add progress(year of plenty) cards to the deck
        for(int i = 113; i < 115; i++){
            Card newCard = new Card(i, 8, "Take any 2 resources from the bank. Add them to your hand"
                    + ". They can be 2 of the same or different");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add victory points cards to the deck
        for(int i = 115; i < 120; i++){
            Card newCard = new Card(i, 9+(i-115), "1 Victory Point, (Reveal this card on your turn if, with it, you reach the required"
                    + "number of victory points.)");
            deckOfCards.add(newCard);
        }
        
        //For cycle to create and add special cards(longest road) to the deck
        for(int i = 120; i < 121; i++){
            Card newCard = new Card(i, 14, "2 Victory Points, (This card goes to the player with the longest"
                    + "unbroken road of at least 5 segments. Another player who builds a longer road"
                    + "takes this card)");
            specialCards.add(newCard);
        }
        
        //For cycle to create and add special cards(largest army) to the deck
        for(int i = 121; i < 122; i++){
            Card newCard = new Card(i, 15, "2 Victory Points, (The first player to play 3 Soldier cards gets"
                    + "this card. Another player who plays more Soldier cards takes this card.)");
            specialCards.add(newCard);
        }
    } 
}

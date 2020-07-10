/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;

/**
 *
 * @author Bruno Ribeiro Criaçao da Classe
 */
public class Player {

    private int score, id;
    private boolean longestRoad, biggestArmy;
    private List<City> listCities = new ArrayList<City>();
    private List<Settlement> listSettlements = new ArrayList<Settlement>();
    private List<Road> listRoads = new ArrayList<Road>();

    public Player(int score, int id, boolean longestRoad, boolean biggestArmy) {
        this.score = score;
        this.id = id;
        this.longestRoad = longestRoad;
        this.biggestArmy = biggestArmy;
    }

    public List<Settlement> getListSettlements() {
        return listSettlements;
    }

    public void setListSettlements(List<Settlement> listSettlements) {
        this.listSettlements = listSettlements;
    }

    public List<Road> getListRoads() {
        return listRoads;
    }

    public void setListRoads(List<Road> listRoads) {
        this.listRoads = listRoads;
    }

    public List<City> getListCities() {
        return listCities;
    }

    public void setListCities(List<City> listCities) {
        this.listCities = listCities;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(boolean hasLongestRoad) {
        this.longestRoad = hasLongestRoad;
    }

    public boolean isBiggestArmy() {
        return biggestArmy;
    }

    public void setBiggestArmy(boolean hasBiggestArmy) {
        this.biggestArmy = hasBiggestArmy;
    }
}

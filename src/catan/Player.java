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

    private int score, id, wool, timber, brick, wheat, metal, army;
    private boolean longestRoad, biggestArmy;
    private List<City> listCities = new ArrayList<City>();
    private List<Settlement> listSettlements = new ArrayList<Settlement>();
    private List<Road> listRoads = new ArrayList<Road>();

    public Player(int score, int id, int wool, int timber, int brick, int wheat, int metal, boolean longestRoad, boolean biggestArmy) {
        this.score = score;
        this.id = id;
        this.wool = wool;
        this.timber = timber;
        this.brick = brick;
        this.wheat = wheat;
        this.metal = metal;
        this.longestRoad = longestRoad;
        this.biggestArmy = biggestArmy;
    }

    public List<Settlement> getListSettlements() {
        return listSettlements;
    }

    public void setListSettlements(List<Settlement> listSettlements) {
        this.listSettlements = listSettlements;
    }

    public void addSettlement(Settlement s) {
        this.listSettlements.add(s);
    }

    public List<Road> getListRoads() {
        return listRoads;
    }

    public void setListRoads(List<Road> listRoads) {
        this.listRoads = listRoads;
    }

    public void addRoad(Road r) {
        this.listRoads.add(r);
    }

    public List<City> getListCities() {
        return listCities;
    }

    public void setListCities(List<City> listCities) {
        this.listCities = listCities;
    }

    public void addCity(City c) {
        this.listCities.add(c);
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

    public int getWool() {
        return wool;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public int getTimber() {
        return timber;
    }

    public void setTimber(int timber) {
        this.timber = timber;
    }

    public int getBrick() {
        return brick;
    }

    public void setBrick(int brick) {
        this.brick = brick;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal = metal;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }
}
